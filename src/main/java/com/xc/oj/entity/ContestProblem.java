package com.xc.oj.entity;

import javax.persistence.*;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Entity
@DiscriminatorValue("1")
//@Table(name = "contest_problem", schema = "onlinejudge", catalog = "")
public class ContestProblem extends Problem{
    private Long contestId;
    private Problem problem;
    private Integer submissionNumberLocked;
    private Integer acceptedNumberLocked;

    public ContestProblem() {
    }

    public ContestProblem(Long pid) {
        this.problem=new Problem(pid);
    }

    @Basic
    @Column(name = "contest_id")
    public Long getContestId() {
        return contestId;
    }

    public void setContestId(Long contestId) {
        this.contestId = contestId;
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

    @Transient
    public Integer getRealTimeLimit(){
        Integer timeLimit=getTimeLimit();
        if(timeLimit==null||timeLimit==0)
            timeLimit=problem.getTimeLimit();
        return timeLimit;
    }

    @Transient
    public Integer getRealMemoryLimit(){
        Integer memoryLimit=getMemoryLimit();
        if(memoryLimit==null||memoryLimit==0)
            memoryLimit=problem.getMemoryLimit();
        return memoryLimit;
    }
    @Transient
    public Integer getRealTimeLimit(String lang){
        Integer timeLimit=getRealTimeLimit();
        for(HashMap<String,String> mp:getRealAllowLanguage()){
            if(mp.get("language").equals(lang)) {
                timeLimit = (int)Math.round(timeLimit*Double.parseDouble(mp.get("time_factor")));
                break;
            }
        }
        return timeLimit;
    }

    @Transient
    public Integer getRealMemoryLimit(String lang){
        Integer memoryLimit=getRealMemoryLimit();
        for(HashMap<String,String> mp:getRealAllowLanguage()){
            if(mp.get("language").equals(lang)) {
                memoryLimit = (int)Math.round(memoryLimit*Double.parseDouble(mp.get("memory_factor")));
                break;
            }
        }
        return memoryLimit;
    }

    @Transient
    public List<HashMap<String,String>> getRealAllowLanguage(){
        List<HashMap<String,String>> allowLanguage=getAllowLanguage();
        if(allowLanguage==null||allowLanguage.isEmpty())
            allowLanguage=problem.getAllowLanguage();
        return allowLanguage;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ContestProblem)) return false;
        if (!super.equals(o)) return false;
        ContestProblem that = (ContestProblem) o;
        return Objects.equals(contestId, that.contestId) &&
                Objects.equals(problem, that.problem) &&
                Objects.equals(submissionNumberLocked, that.submissionNumberLocked) &&
                Objects.equals(acceptedNumberLocked, that.acceptedNumberLocked);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), contestId, problem, submissionNumberLocked, acceptedNumberLocked);
    }
}
