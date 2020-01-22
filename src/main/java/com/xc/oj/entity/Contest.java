package com.xc.oj.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "contest", schema = "onlinejudge", catalog = "")
public class Contest implements Serializable {
    private Long id;
    private Integer sortId;
    private String title;
    private String description;
    private Boolean realTimeRank;
    private String password;
    private String ruleType;
    private Integer penaltyTime;
    private Timestamp startTime;
    private Timestamp endTime;
    private Boolean willLock;
    private Timestamp lockTime;
    private Timestamp unlockTime;
    private Boolean visible;
    private Timestamp createTime;
    private Timestamp updateTime;
    private UserInfo createUser;
    private UserInfo updateUser;
    List<ContestProblem> problemList;

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
    @Column(name = "sort_id")
    public Integer getSortId() {
        return sortId;
    }

    public void setSortId(Integer sortId) {
        this.sortId = sortId;
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
    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Basic
    @Column(name = "real_time_rank")
    public Boolean getRealTimeRank() {
        return realTimeRank;
    }

    public void setRealTimeRank(Boolean realTimeRank) {
        this.realTimeRank = realTimeRank;
    }

    @Basic
    @Column(name = "password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Basic
    @Column(name = "rule_type")
    public String getRuleType() {
        return ruleType;
    }

    public void setRuleType(String ruleType) {
        this.ruleType = ruleType;
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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    @Basic
    @Column(name = "end_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    public Timestamp getLockTime() {
        return lockTime;
    }

    public void setLockTime(Timestamp lockTime) {
        this.lockTime = lockTime;
    }

    @Basic
    @Column(name = "unlock_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    public Timestamp getUnlockTime() {
        return unlockTime;
    }

    public void setUnlockTime(Timestamp unlockTime) {
        this.unlockTime = unlockTime;
    }

    @Basic
    @Column(name = "visible")
    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    @Basic
    @Column(name = "create_time")
    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Basic
    @Column(name = "update_time")
    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    @ManyToOne
    @JoinColumn(name="create_id")
    public UserInfo getCreateUser() {
        return createUser;
    }

    public void setCreateUser(UserInfo createUser) {
        this.createUser = createUser;
    }

    @ManyToOne
    @JoinColumn(name="update_id")
    public UserInfo getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(UserInfo updateUser) {
        this.updateUser = updateUser;
    }

    @OneToMany(targetEntity = ContestProblem.class)
    @JoinColumn(name="contest_id")
    public List<ContestProblem> getProblemList() {
        return problemList;
    }

    public void setProblemList(List<ContestProblem> problemList) {
        this.problemList = problemList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Contest)) return false;
        Contest contest = (Contest) o;
        return Objects.equals(id, contest.id) &&
                Objects.equals(sortId, contest.sortId) &&
                Objects.equals(title, contest.title) &&
                Objects.equals(description, contest.description) &&
                Objects.equals(realTimeRank, contest.realTimeRank) &&
                Objects.equals(password, contest.password) &&
                Objects.equals(ruleType, contest.ruleType) &&
                Objects.equals(penaltyTime, contest.penaltyTime) &&
                Objects.equals(startTime, contest.startTime) &&
                Objects.equals(endTime, contest.endTime) &&
                Objects.equals(willLock, contest.willLock) &&
                Objects.equals(lockTime, contest.lockTime) &&
                Objects.equals(unlockTime, contest.unlockTime) &&
                Objects.equals(visible, contest.visible) &&
                Objects.equals(createTime, contest.createTime) &&
                Objects.equals(updateTime, contest.updateTime) &&
                Objects.equals(createUser, contest.createUser) &&
                Objects.equals(updateUser, contest.updateUser) &&
                Objects.equals(problemList, contest.problemList);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, sortId, title, description, realTimeRank, password, ruleType, penaltyTime, startTime, endTime, willLock, lockTime, unlockTime, visible, createTime, updateTime, createUser, updateUser, problemList);
    }
}
