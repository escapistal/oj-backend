package com.xc.oj.service;

import com.xc.oj.entity.*;
import com.xc.oj.util.OJPropertiesUtil;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;

@Service
public class CommonService {

    final SubmissionBaseService submissionBaseService;
    final SubmissionService submissionService;
    final ContestSubmissionService contestSubmissionService;
    final AcmContestRankService acmContestRankService;
    final UserService userService;
    final ProblemService problemService;
    final ContestProblemService contestProblemService;

    public CommonService(SubmissionBaseService submissionBaseService, SubmissionService submissionService, ContestSubmissionService contestSubmissionService, AcmContestRankService acmContestRankService, UserService userService, ProblemService problemService, ContestProblemService contestProblemService) {
        this.submissionBaseService = submissionBaseService;
        this.submissionService = submissionService;
        this.contestSubmissionService = contestSubmissionService;
        this.acmContestRankService = acmContestRankService;
        this.userService = userService;
        this.problemService = problemService;
        this.contestProblemService = contestProblemService;
    }

    private void getSummary(SubmissionBase submission,JudgeResult judgeResult){//补充评测细节并存库
        Integer maxExecTime=0;
        Integer maxExecMemory=0;
        JudgeResultEnum firstErr=null;
        int err=0,pe=0,se=0;
        for (SingleJudgeResult res : judgeResult.getDetail()) {
            maxExecTime=Math.max(maxExecTime,res.getExecTime());
            maxExecMemory=Math.max(maxExecMemory,res.getExecMemory());
            if(res.getResult()!=JudgeResultEnum.AC){
                ++err;
                if(res.getResult()==JudgeResultEnum.PE)
                    ++pe;
                if(res.getResult()==JudgeResultEnum.SE)
                    ++se;
                if(firstErr==null&&res.getResult()!=JudgeResultEnum.PE)
                    firstErr=res.getResult();
            }
        }
        if(se>0)
            submission.setStatus(JudgeResultEnum.SE);
        else if(err==0)//AC
            submission.setStatus(JudgeResultEnum.AC);
        else if(err==pe||firstErr==null)//全部非AC点都PE才算PE
            submission.setStatus(JudgeResultEnum.PE);
        else
            submission.setStatus(firstErr);
        submission.setDetail(judgeResult.getDetail());
        submission.setExecuteTime(maxExecTime);
        submission.setExecuteMemory(maxExecMemory);
        submission.setJudgeTime(new Timestamp(new Date().getTime()));
        submissionBaseService.save(submission);
    }

    public void updateSubmissionResult(JudgeResult judgeResult) {
        Long sid = judgeResult.getSubmissionId();
        SubmissionBase submissionBase = submissionBaseService.findById(sid).orElse(null);
        getSummary(submissionBase, judgeResult);
        if (submissionBase instanceof ContestSubmission) {//赛题提交分流
            updateContestSubmissionResult((ContestSubmission) submissionBase,judgeResult);
            return;
        }
        //常规题库提交
        Submission submission = (Submission) submissionBase;
        Problem problem = (Problem) submission.getProblem();
        User user = userService.findById(submission.getUser().getId()).orElse(null);
        boolean upd = false;
        if (submission.getStatus() == JudgeResultEnum.AC) {//AC结果
            if (!user.getAcceptedId().contains(problem.getId())) {//未曾AC过
                problem.setSubmissionNumber(problem.getSubmissionNumber() + 1);
                problem.setAcceptedNumber(problem.getAcceptedNumber() + 1);
                user.setSubmissionNumber(user.getSubmissionNumber() + 1);
                user.setAcceptedNumber(user.getAcceptedId().size());
                user.getAcceptedId().add(problem.getId());
                upd = true;
            }
        } else if (!Arrays.asList(JudgeResultEnum.SE, JudgeResultEnum.CE).contains(submission.getStatus())) {//其他除SE与CE结果
            problem.setSubmissionNumber(problem.getSubmissionNumber() + 1);
            user.setSubmissionNumber(user.getSubmissionNumber() + 1);
            upd = true;
        }
        if (upd) {
            userService.save(user);
            problemService.save(problem);
        }
    }

    private void updateContestSubmissionResult(ContestSubmission submission,JudgeResult judgeResult) {
        //TODO 评测结果入redis缓存
        ContestInfo contest=submission.getContest();
        ContestProblem problem= (ContestProblem) submission.getProblem();
        AcmContestRank acmContestRank,acmContestRankLocked;
        SingleSubmissionInfo info;
        //赛中且非管理员提交，才需要更新
        if(!submission.getCreateTime().before(contest.getStartTime())&&!submission.getCreateTime().after(contest.getEndTime())
                && !submission.getUser().getRole().contains("admin")) {
            acmContestRank = acmContestRankService.findByContestIdAndUserIdAndLocked(contest.getId(), submission.getUser().getId(), false).orElse(null);
            acmContestRankLocked = acmContestRankService.findByContestIdAndUserIdAndLocked(contest.getId(), submission.getUser().getId(), true).orElse(null);
            if (acmContestRank == null) {
                acmContestRank = new AcmContestRank();
                acmContestRank.setLocked(false);
                acmContestRank.setContestId(contest.getId());
                acmContestRank.setUser(submission.getUser());
            }
            if (acmContestRankLocked == null) {
                acmContestRankLocked = new AcmContestRank();
                acmContestRankLocked.setLocked(true);
                acmContestRankLocked.setContestId(contest.getId());
                acmContestRankLocked.setUser(submission.getUser());
            }
            if (acmContestRank.getSubmissionInfo().get(problem.getId()) == null
                    || !acmContestRank.getSubmissionInfo().get(problem.getId()).getAc()) {//此前未ac过才需要更新榜单
                if (submission.getStatus() == JudgeResultEnum.AC) {//AC结果
                    problem.setSubmissionNumber(problem.getSubmissionNumber() + 1);
                    problem.setAcceptedNumber(problem.getAcceptedNumber() + 1);
                    acmContestRank.setSubmissionNumber(acmContestRank.getSubmissionNumber() + 1);
                    acmContestRank.setAcceptedNumber(acmContestRank.getAcceptedNumber() + 1);
                    info = acmContestRank.getSubmissionInfo().get(problem.getId());
                    if (info == null)
                        info = new SingleSubmissionInfo();
                    info.setAc(true);
                    info.setAcTime((int) ((submission.getCreateTime().getTime() - contest.getStartTime().getTime()) / 1000));
                    //优先遵从contest局部配置罚时，否则遵从配置文件
                    Integer penaltyTime=contest.getPenaltyTime()==null? Integer.valueOf(OJPropertiesUtil.get("penalty_time")) :contest.getPenaltyTime();
                    acmContestRank.setTotalTime(acmContestRank.getTotalTime() + info.getError() * penaltyTime + info.getAcTime());
                    acmContestRank.getSubmissionInfo().put(problem.getId(), info);
                    if (!contest.getWillLock() || submission.getCreateTime().before(contest.getLockTime())) {//不封榜或未到封榜时间
                        problem.setSubmissionNumberLocked(problem.getSubmissionNumberLocked() + 1);
                        problem.setAcceptedNumberLocked(problem.getAcceptedNumberLocked() + 1);
                        acmContestRankLocked.setSubmissionNumber(acmContestRankLocked.getSubmissionNumber() + 1);
                        acmContestRankLocked.setAcceptedNumber(acmContestRankLocked.getAcceptedNumber() + 1);
                        acmContestRankLocked.getSubmissionInfo().put(problem.getId(), info);
                    }
                } else {//非AC结果
                    problem.setSubmissionNumber(problem.getSubmissionNumber() + 1);
                    acmContestRank.setSubmissionNumber(acmContestRank.getSubmissionNumber() + 1);
                    info = acmContestRank.getSubmissionInfo().get(problem.getId());
                    if (info == null)
                        info = new SingleSubmissionInfo();
                    info.setError(info.getError() + 1);
                    acmContestRank.getSubmissionInfo().put(problem.getId(), info);
                    if (!contest.getWillLock() || submission.getCreateTime().before(contest.getLockTime())) {//不封榜或未到封榜时间
                        problem.setSubmissionNumberLocked(problem.getSubmissionNumberLocked() + 1);
                        acmContestRankLocked.setSubmissionNumber(acmContestRankLocked.getSubmissionNumber() + 1);
                        acmContestRankLocked.getSubmissionInfo().put(problem.getId(), info);
                    }
                }
            }
            contestProblemService.save(problem);
            acmContestRankService.save(acmContestRank);
            acmContestRankService.save(acmContestRankLocked);
        }
    }
}
