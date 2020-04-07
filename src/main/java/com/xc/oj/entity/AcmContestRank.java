package com.xc.oj.entity;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Objects;

@Entity
@Table(name = "acm_contest_rank", schema = "onlinejudge", catalog = "")
public class AcmContestRank implements Comparable<AcmContestRank>{
    private Long id;
    private Long contestId;
    private UserInfo user;
    private Boolean locked;
    private Integer acceptedNumber;
    private Integer submissionNumber;
    private Integer totalTime;
    private HashMap<Long, SingleSubmissionInfo> submissionInfo;

    public AcmContestRank() {
        acceptedNumber=0;
        submissionNumber=0;
        totalTime=0;
        submissionInfo=new HashMap<>();
    }

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
    @JoinColumn(name="user_id")
    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }

    @Basic
    @Column(name = "locked")
    public Boolean getLocked() {
        return locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    @Basic
    @Column(name = "accepted_number")
    public Integer getAcceptedNumber() {
        return acceptedNumber;
    }

    public void setAcceptedNumber(Integer acceptedNumber) {
        this.acceptedNumber = acceptedNumber;
    }

    @Basic
    @Column(name = "submission_number")
    public Integer getSubmissionNumber() {
        return submissionNumber;
    }

    public void setSubmissionNumber(Integer submissionNumber) {
        this.submissionNumber = submissionNumber;
    }

    @Basic
    @Column(name = "total_time")
    public Integer getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(Integer totalTime) {
        this.totalTime = totalTime;
    }

    @Basic
    @Type(type = "json" )
    @Column(name = "submission_info",columnDefinition="json")
    public HashMap<Long, SingleSubmissionInfo> getSubmissionInfo() {
        return submissionInfo;
    }

    public void setSubmissionInfo(HashMap<Long, SingleSubmissionInfo> submissionInfo) {
        this.submissionInfo = submissionInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AcmContestRank)) return false;
        AcmContestRank that = (AcmContestRank) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(contestId, that.contestId) &&
                Objects.equals(user, that.user) &&
                Objects.equals(locked, that.locked) &&
                Objects.equals(acceptedNumber, that.acceptedNumber) &&
                Objects.equals(submissionNumber, that.submissionNumber) &&
                Objects.equals(totalTime, that.totalTime) &&
                Objects.equals(submissionInfo, that.submissionInfo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, contestId, user, locked, acceptedNumber, submissionNumber, totalTime, submissionInfo);
    }

    @Override
    public int compareTo(AcmContestRank o) {
        if(getAcceptedNumber()!=o.getAcceptedNumber())
            return o.getAcceptedNumber()-getAcceptedNumber();
        if(getTotalTime()!=o.getTotalTime())
            return getTotalTime()-o.getTotalTime();
        HashMap<Long, SingleSubmissionInfo> x=getSubmissionInfo(),y=o.getSubmissionInfo();
        int xLastAc=0,yLastAc=0;
        for(Long pid:x.keySet()){
            SingleSubmissionInfo data=x.get(pid);
            if(data.getAc()){
                xLastAc=Math.max(xLastAc,data.getAcTime());
            }
        }
        for(Long pid:y.keySet()){
            SingleSubmissionInfo data=y.get(pid);
            if(data.getAc()){
                yLastAc=Math.max(yLastAc,data.getAcTime());
            }
        }
        if(xLastAc!=yLastAc)
            return xLastAc-yLastAc;
        return getUser().getNickname().compareTo(o.getUser().getNickname());
    }
}