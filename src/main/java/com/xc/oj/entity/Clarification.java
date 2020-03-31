package com.xc.oj.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

@Entity
public class Clarification implements Comparable<Clarification>{
    private Long id;
    private Long contestId;
    private ContestProblem problem;
    private String content;
    private UserInfo createUser;
    private Timestamp createTime;
    private Boolean readByUser;
    private Boolean readByAdmin;
    private List<ClarificationReply> reply;

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
    @JoinColumn(name="problem_id")
    public ContestProblem getProblem() {
        return problem;
    }

    public void setProblem(ContestProblem problem) {
        this.problem = problem;
    }

    @Basic
    @Column(name = "content")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @ManyToOne
    @JoinColumn(name="create_id")
    public UserInfo getCreateUser() {
        return createUser;
    }

    public void setCreateUser(UserInfo createUser) {
        this.createUser = createUser;
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
    @Column(name = "read_by_user")
    public Boolean getReadByUser() {
        return readByUser;
    }

    public void setReadByUser(Boolean readByUser) {
        this.readByUser = readByUser;
    }

    @Basic
    @Column(name = "read_by_admin")
    public Boolean getReadByAdmin() {
        return readByAdmin;
    }

    public void setReadByAdmin(Boolean readByAdmin) {
        this.readByAdmin = readByAdmin;
    }

    @OneToMany(targetEntity = ClarificationReply.class)
    @JoinColumn(name="clar_id")
    public List<ClarificationReply> getReply() {
        return reply;
    }

    public void setReply(List<ClarificationReply> reply) {
        this.reply = reply;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Clarification)) return false;
        Clarification that = (Clarification) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(contestId, that.contestId) &&
                Objects.equals(problem, that.problem) &&
                Objects.equals(content, that.content) &&
                Objects.equals(createUser, that.createUser) &&
                Objects.equals(createTime, that.createTime) &&
                Objects.equals(readByUser, that.readByUser) &&
                Objects.equals(readByAdmin, that.readByAdmin) &&
                Objects.equals(reply, that.reply);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, contestId, problem, content, createUser, createTime, readByUser, readByAdmin, reply);
    }

    @Override
    public int compareTo(Clarification o) {
        if(createTime.before(o.getCreateTime()))
            return -1;
        if(createTime.after(o.getCreateTime()))
            return 1;
        return 0;
    }
}
