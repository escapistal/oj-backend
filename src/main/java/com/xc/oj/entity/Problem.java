package com.xc.oj.entity;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "problem", schema = "onlinejudge", catalog = "")
public class Problem implements Serializable {
    private Long id;
    private Integer sortId;
    private String name;
    private String title;
    private String description;
    private String inputDescription;
    private String outputDescription;
    private List<HashMap<String,String>> samples;
    private String hint;
    private String testCaseMd5;
    private List<HashMap<String,String>> allowLanguage;
    private Integer timeLimit;
    private Integer memoryLimit;
    private Boolean visible;
    private Integer submissionNumber;
    private Integer acceptedNumber;
    private Timestamp createTime;
    private Timestamp updateTime;
    //    private Integer createId;
//    private Integer updateId;
    private Boolean spj;
    private String spjLanguage;
    private String spjCode;
    private String spjMd5;

    private UserInfo createUser;
    private UserInfo updateUser;

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
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
    @Column(name = "input_description")
    public String getInputDescription() {
        return inputDescription;
    }

    public void setInputDescription(String inputDescription) {
        this.inputDescription = inputDescription;
    }

    @Basic
    @Column(name = "output_description")
    public String getOutputDescription() {
        return outputDescription;
    }

    public void setOutputDescription(String outputDescription) {
        this.outputDescription = outputDescription;
    }

    @Basic
    @Type(type = "json" )
    @Column(name = "samples",columnDefinition = "json")
    public List<HashMap<String,String>> getSamples() {
        return samples;
    }

    public void setSamples( List<HashMap<String,String>> samples) {
        this.samples = samples;
    }

    @Basic
    @Column(name = "hint")
    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    @Basic
    @Column(name = "test_case_md5")
    public String getTestCaseMd5() {
        return testCaseMd5;
    }

    public void setTestCaseMd5(String testCaseMd5) {
        this.testCaseMd5 = testCaseMd5;
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
    @Column(name = "time_limit")
    public Integer getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(Integer timeLimit) {
        this.timeLimit = timeLimit;
    }

    @Basic
    @Column(name = "memory_limit")
    public Integer getMemoryLimit() {
        return memoryLimit;
    }

    public void setMemoryLimit(Integer memoryLimit) {
        this.memoryLimit = memoryLimit;
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

//    @Basic
//    @Column(name = "create_id")
//    public Integer getCreateId() {
//        return createId;
//    }
//
//    public void setCreateId(Integer createId) {
//        this.createId = createId;
//    }

//    @Basic
//    @Column(name = "update_id")
//    public Integer getUpdateId() {
//        return updateId;
//    }
//
//    public void setUpdateId(Integer updateId) {
//        this.updateId = updateId;
//    }

    @Basic
    @Column(name = "spj")
    public Boolean getSpj() {
        return spj;
    }

    public void setSpj(Boolean spj) {
        this.spj = spj;
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

    @Basic
    @Column(name = "spj_language")
    public String getSpjLanguage() {
        return spjLanguage;
    }

    public void setSpjLanguage(String spjLanguage) {
        this.spjLanguage = spjLanguage;
    }

    @Basic
    @Column(name = "spj_code")
    public String getSpjCode() {
        return spjCode;
    }

    public void setSpjCode(String spjCode) {
        this.spjCode = spjCode;
    }

    @Basic
    @Column(name = "spj_md5")
    public String getSpjMd5() {
        return spjMd5;
    }

    public void setSpjMd5(String spjMd5) {
        this.spjMd5 = spjMd5;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Problem)) return false;
        Problem problem = (Problem) o;
        return Objects.equals(id, problem.id) &&
                Objects.equals(sortId, problem.sortId) &&
                Objects.equals(title, problem.title) &&
                Objects.equals(description, problem.description) &&
                Objects.equals(inputDescription, problem.inputDescription) &&
                Objects.equals(outputDescription, problem.outputDescription) &&
                Objects.equals(samples, problem.samples) &&
                Objects.equals(hint, problem.hint) &&
                Objects.equals(testCaseMd5, problem.testCaseMd5) &&
                Objects.equals(allowLanguage, problem.allowLanguage) &&
                Objects.equals(timeLimit, problem.timeLimit) &&
                Objects.equals(memoryLimit, problem.memoryLimit) &&
                Objects.equals(visible, problem.visible) &&
                Objects.equals(submissionNumber, problem.submissionNumber) &&
                Objects.equals(acceptedNumber, problem.acceptedNumber) &&
                Objects.equals(createTime, problem.createTime) &&
                Objects.equals(updateTime, problem.updateTime) &&
                Objects.equals(spj, problem.spj) &&
                Objects.equals(spjLanguage, problem.spjLanguage) &&
                Objects.equals(spjCode, problem.spjCode) &&
                Objects.equals(spjMd5, problem.spjMd5) &&
                Objects.equals(createUser, problem.createUser) &&
                Objects.equals(updateUser, problem.updateUser);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, sortId, title, description, inputDescription, outputDescription, samples, hint, testCaseMd5, allowLanguage, timeLimit, memoryLimit, visible, submissionNumber, acceptedNumber, createTime, updateTime, spj, spjLanguage, spjCode, spjMd5, createUser, updateUser);
    }
}
