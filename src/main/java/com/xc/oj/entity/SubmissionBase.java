package com.xc.oj.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "submission", schema = "onlinejudge", catalog = "")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "in_contest", discriminatorType = DiscriminatorType.INTEGER)
public class SubmissionBase implements Serializable{
    private Long id;
    private ProblemBase problem;
    private UserInfo user;
    private String language;
    private String code;
    private Timestamp createTime;
    private JudgeResultEnum status;
    private Integer executeTime;
    private Integer executeMemory;
    private Integer codeLength;
    private List<SingleJudgeResult> detail;
    private Timestamp judgeTime;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne
    @JoinColumn(name="problem_id")
    public ProblemBase getProblem() {
        return problem;
    }

    public void setProblem(ProblemBase problem) {
        this.problem = problem;
    }

    @ManyToOne
    @JoinColumn(name = "user_id")
    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }

    @Basic
    @Column(name = "language")
    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    @Basic
    @Column(name = "code")
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Basic
    @Column(name = "create_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Basic
    @Column(name = "status")
    public JudgeResultEnum getStatus() {
        return status;
    }

    public void setStatus(JudgeResultEnum status) {
        this.status = status;
    }

    @Basic
    @Column(name = "execute_time")
    public Integer getExecuteTime() {
        return executeTime;
    }

    public void setExecuteTime(Integer executeTime) {
        this.executeTime = executeTime;
    }

    @Basic
    @Column(name = "execute_memory")
    public Integer getExecuteMemory() {
        return executeMemory;
    }

    public void setExecuteMemory(Integer executeMemory) {
        this.executeMemory = executeMemory;
    }

    @Basic
    @Column(name = "code_length")
    public Integer getCodeLength() {
        return codeLength;
    }

    public void setCodeLength(Integer codeLength) {
        this.codeLength = codeLength;
    }

    @Basic
    @Type(type = "json" )
    @Column(name = "detail", columnDefinition = "json")
    public List<SingleJudgeResult> getDetail() {
        return detail;
    }

    public void setDetail(List<SingleJudgeResult> detail) {
        this.detail = detail;
    }

    @Basic
    @Column(name = "judge_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    public Timestamp getJudgeTime() {
        return judgeTime;
    }

    public void setJudgeTime(Timestamp judgeTime) {
        this.judgeTime = judgeTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SubmissionBase)) return false;
        SubmissionBase that = (SubmissionBase) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(problem, that.problem) &&
                Objects.equals(user, that.user) &&
                Objects.equals(language, that.language) &&
                Objects.equals(code, that.code) &&
                Objects.equals(createTime, that.createTime) &&
                status == that.status &&
                Objects.equals(executeTime, that.executeTime) &&
                Objects.equals(executeMemory, that.executeMemory) &&
                Objects.equals(codeLength, that.codeLength) &&
                Objects.equals(detail, that.detail) &&
                Objects.equals(judgeTime, that.judgeTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, problem, user, language, code, createTime, status, executeTime, executeMemory, codeLength, detail, judgeTime);
    }
}
