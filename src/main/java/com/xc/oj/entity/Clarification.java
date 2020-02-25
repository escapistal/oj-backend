package com.xc.oj.entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

@Entity
public class Clarification implements Comparable<Clarification>{
    private Long id;
    private Long contestId;
    private Long problemId;
    private String content;
    private UserInfo createUser;
    private Timestamp createTime;
    private List<ClarificationReply> reply;

    @Id
    @Column(name = "id")
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
    @Column(name = "problem_id")
    public Long getProblemId() {
        return problemId;
    }

    public void setProblemId(Long problemId) {
        this.problemId = problemId;
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
    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
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
                Objects.equals(problemId, that.problemId) &&
                Objects.equals(content, that.content) &&
                Objects.equals(createUser, that.createUser) &&
                Objects.equals(createTime, that.createTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, contestId, problemId, content, createUser, createTime);
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
