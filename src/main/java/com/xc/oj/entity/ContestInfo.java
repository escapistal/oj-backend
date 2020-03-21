package com.xc.oj.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "contest", schema = "onlinejudge", catalog = "")
public class ContestInfo {
    private Long id;
    private String title;
    private Integer penaltyTime;
    private Timestamp startTime;
    private Timestamp endTime;
    private Boolean willLock;
    private Timestamp lockTime;


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
    @Column(name = "title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Basic
    @Column(name = "penalty_time")
    public Integer getPenaltyTime() {
        return penaltyTime;
    }

    public void setPenaltyTime(Integer penaltyTime) {
        this.penaltyTime = penaltyTime;
    }

    @Basic
    @Column(name = "start_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    @Basic
    @Column(name = "end_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    @Basic
    @Column(name = "will_lock")
    public Boolean getWillLock() {
        return willLock;
    }

    public void setWillLock(Boolean willLock) {
        this.willLock = willLock;
    }

    @Basic
    @Column(name = "lock_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    public Timestamp getLockTime() {
        return lockTime;
    }

    public void setLockTime(Timestamp lockTime) {
        this.lockTime = lockTime;
    }
}
