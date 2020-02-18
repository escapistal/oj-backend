package com.xc.oj.entity;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Objects;

@Entity
@Table(name = "acm_contest_rank", schema = "onlinejudge", catalog = "")
public class AcmContestRank {
    private long id;
    private long contestId;
    private long userId;
    private boolean locked;
    private int acceptedNumber;
    private int submissionNumber;
    private int totalTime;
    private HashMap<Integer, HashMap<String,String>> submissionInfo;

    @Id
    @Column(name = "id")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "contest_id")
    public long getContestId() {
        return contestId;
    }

    public void setContestId(long contestId) {
        this.contestId = contestId;
    }

    @Basic
    @Column(name = "user_id")
    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Basic
    @Column(name = "locked")
    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    @Basic
    @Column(name = "accepted_number")
    public int getAcceptedNumber() {
        return acceptedNumber;
    }

    public void setAcceptedNumber(int acceptedNumber) {
        this.acceptedNumber = acceptedNumber;
    }

    @Basic
    @Column(name = "submission_number")
    public int getSubmissionNumber() {
        return submissionNumber;
    }

    public void setSubmissionNumber(int submissionNumber) {
        this.submissionNumber = submissionNumber;
    }

    @Basic
    @Column(name = "total_time")
    public int getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(int totalTime) {
        this.totalTime = totalTime;
    }

    @Basic
    @Type(type = "json" )
    @Column(name = "submission_info",columnDefinition="json")
    public HashMap<Integer, HashMap<String,String>> getSubmissionInfo() {
        return submissionInfo;
    }

    public void setSubmissionInfo(HashMap<Integer, HashMap<String,String>> submissionInfo) {
        this.submissionInfo = submissionInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AcmContestRank that = (AcmContestRank) o;
        return id == that.id &&
                contestId == that.contestId &&
                userId == that.userId &&
                locked == that.locked &&
                acceptedNumber == that.acceptedNumber &&
                submissionNumber == that.submissionNumber &&
                totalTime == that.totalTime &&
                Objects.equals(submissionInfo, that.submissionInfo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, contestId, userId, locked, acceptedNumber, submissionNumber, totalTime, submissionInfo);
    }
}
