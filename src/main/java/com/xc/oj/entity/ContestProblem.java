package com.xc.oj.entity;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "contest_problem", schema = "onlinejudge", catalog = "")
public class ContestProblem implements Serializable{
    private Long id;
    private Long contestId;
    private Integer sortId;
    private Problem problem;
    private String shortname;
    private List<HashMap<String,String>> allowLanguage;
    private Boolean visible;
    private Integer submissionNumber;
    private Integer acceptedNumber;
    private Integer submissionNumberLocked;
    private Integer acceptedNumberLocked;
    private Timestamp createTime;
    private Timestamp updateTime;
    private UserInfo createUser;
    private UserInfo  updateUser;

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

    @Basic
    @Column(name = "sort_id")
    public Integer getSortId() {
        return sortId;
    }

    public void setSortId(Integer sortId) {
        this.sortId = sortId;
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
    @Column(name = "shortname")
    public String getShortname() {
        return shortname;
    }

    public void setShortname(String shortname) {
        this.shortname = shortname;
    }

    @Basic
    @Type(type = "json" )
    @Column(name = "allow_language",columnDefinition = "json")
    public List<HashMap<String,String>> getAllowLanguage() {
        return allowLanguage;
    }

    public void setAllowLanguage(List<HashMap<String,String>> allowLanguage) {
        this.allowLanguage = allowLanguage;
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
    @Column(name = "submission_number")
    public Integer getSubmissionNumber() {
        return submissionNumber;
    }

    public void setSubmissionNumber(Integer submissionNumber) {
        this.submissionNumber = submissionNumber;
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
    @Column(name = "submission_number_locked")
    public Integer getSubmissionNumberLocked() {
        return submissionNumberLocked;
    }

    public void setSubmissionNumberLocked(Integer submissionNumberLocked) {
        this.submissionNumberLocked = submissionNumberLocked;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ContestProblem)) return false;
        ContestProblem that = (ContestProblem) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(contestId, that.contestId) &&
                Objects.equals(sortId, that.sortId) &&
                Objects.equals(problem, that.problem) &&
                Objects.equals(shortname, that.shortname) &&
                Objects.equals(allowLanguage, that.allowLanguage) &&
                Objects.equals(visible, that.visible) &&
                Objects.equals(submissionNumber, that.submissionNumber) &&
                Objects.equals(acceptedNumber, that.acceptedNumber) &&
                Objects.equals(submissionNumberLocked, that.submissionNumberLocked) &&
                Objects.equals(acceptedNumberLocked, that.acceptedNumberLocked) &&
                Objects.equals(createTime, that.createTime) &&
                Objects.equals(updateTime, that.updateTime) &&
                Objects.equals(createUser, that.createUser) &&
                Objects.equals(updateUser, that.updateUser);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, contestId, sortId, problem, shortname, allowLanguage, visible, submissionNumber, acceptedNumber, submissionNumberLocked, acceptedNumberLocked, createTime, updateTime, createUser, updateUser);
    }

    @Basic
    @Column(name = "accepted_number_locked")
    public Integer getAcceptedNumberLocked() {
        return acceptedNumberLocked;
    }

    public void setAcceptedNumberLocked(Integer acceptedNumberLocked) {
        this.acceptedNumberLocked = acceptedNumberLocked;
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
    public UserInfo  getCreateUser() {
        return createUser;
    }

    public void setCreateUser(UserInfo  createUser) {
        this.createUser = createUser;
    }

    @ManyToOne
    @JoinColumn(name="update_id")
    public UserInfo  getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(UserInfo  updateUser) {
        this.updateUser = updateUser;
    }


}
