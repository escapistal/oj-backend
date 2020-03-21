package com.xc.oj.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Entity
@DiscriminatorValue("1")
//@Table(name = "contest_problem", schema = "onlinejudge", catalog = "")
public class ContestProblem extends ProblemBase{
    private Contest contest;
    private Problem problem;
    private Integer submissionNumberLocked;
    private Integer acceptedNumberLocked;

    public ContestProblem() {
    }

    public ContestProblem(Long pid) {
        this.problem=new Problem(pid);
    }

    @ManyToOne
    @JoinColumn(name="contest_id")
    public Contest getContest() {
        return contest;
    }

    public void setContest(Contest contest) {
        this.contest = contest;
    }

    @ManyToOne
    @JoinColumn(name="problem_id")
    public Problem getProblem() {
        return problem;
    }

    public void setProblem(Problem problem) {
        this.problem = problem;
    }

    @Basic
    @Column(name = "submission_number_locked")
    public Integer getSubmissionNumberLocked() {
        return submissionNumberLocked;
    }

    public void setSubmissionNumberLocked(Integer submissionNumberLocked) {
        this.submissionNumberLocked = submissionNumberLocked;
    }


    @Basic
    @Column(name = "accepted_number_locked")
    public Integer getAcceptedNumberLocked() {
        return acceptedNumberLocked;
    }

    public void setAcceptedNumberLocked(Integer acceptedNumberLocked) {
        this.acceptedNumberLocked = acceptedNumberLocked;
    }

    @Override public Integer getSortId() {
        return super.getSortId()==null?problem.getSortId():super.getSortId();
    }
    @Override public String getName() {
        return super.getName()==null?problem.getName():super.getName();
    }
    @Override public String getTitle() {
        return super.getTitle()==null?problem.getTitle():super.getTitle();
    }
    @Override public String getDescription() {
        return super.getDescription()==null?problem.getDescription():super.getDescription();
    }
    @Override public String getInputDescription() {
        return super.getInputDescription()==null?problem.getInputDescription():super.getInputDescription();
    }
    @Override public String getOutputDescription() {
        return super.getOutputDescription()==null?problem.getOutputDescription():super.getOutputDescription();
    }
    @Override public List<HashMap<String,String>> getSample() {
        return super.getSample()==null?problem.getSample():super.getSample();
    }
    @Override public String getHint() {
        return super.getHint()==null?problem.getHint():super.getHint();
    }
    @Override public String getTestCaseMd5() {
        return super.getTestCaseMd5()==null?problem.getTestCaseMd5():super.getTestCaseMd5();
    }
    @Override public List<HashMap<String,String>> getAllowLanguage() {
        return super.getAllowLanguage()==null?problem.getAllowLanguage():super.getAllowLanguage();
    }
    @Override public List<String> getTag() {
        return super.getTag()==null?problem.getTag():super.getTag();
    }
    @Override public Integer getTimeLimit() {
        return super.getTimeLimit()==null?problem.getTimeLimit():super.getTimeLimit();
    }
    @Override public Integer getMemoryLimit() {
        return super.getMemoryLimit()==null?problem.getMemoryLimit():super.getMemoryLimit();
    }
    @Override public Boolean getVisible() {
        return super.getVisible()==null?problem.getVisible():super.getVisible();
    }
    @Override public Boolean getSpj() {
        return super.getSpj()==null?problem.getSpj():super.getSpj();
    }
    @Override public String getSpjLanguage() {
        return super.getSpjLanguage()==null?problem.getSpjLanguage():super.getSpjLanguage();
    }
    @Override public String getSpjCode() {
        return super.getSpjCode()==null?problem.getSpjCode():super.getSpjCode();
    }
    @Override public String getSpjMd5() {
        return super.getSpjMd5()==null?problem.getSpjMd5():super.getSpjMd5();
    }

//    @JsonIgnore
//    @Transient
//    public Integer getRealTimeLimit(String lang){
//        Integer timeLimit=getTimeLimit();
//        for(HashMap<String,String> mp:getAllowLanguage()){
//            if(mp.get("language").equals(lang)) {
//                timeLimit = (int)Math.round(timeLimit*Double.parseDouble(mp.get("time_factor")));
//                break;
//            }
//        }
//        return timeLimit;
//    }
//
//    @JsonIgnore
//    @Transient
//    public Integer getRealMemoryLimit(String lang){
//        Integer memoryLimit=getMemoryLimit();
//        for(HashMap<String,String> mp:getAllowLanguage()){
//            if(mp.get("language").equals(lang)) {
//                memoryLimit = (int)Math.round(memoryLimit*Double.parseDouble(mp.get("memory_factor")));
//                break;
//            }
//        }
//        return memoryLimit;
//    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ContestProblem)) return false;
        if (!super.equals(o)) return false;
        ContestProblem that = (ContestProblem) o;
        return Objects.equals(contest, that.contest) &&
                Objects.equals(problem, that.problem) &&
                Objects.equals(submissionNumberLocked, that.submissionNumberLocked) &&
                Objects.equals(acceptedNumberLocked, that.acceptedNumberLocked);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), contest, problem, submissionNumberLocked, acceptedNumberLocked);
    }
}
