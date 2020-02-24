package com.xc.oj.entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "clarification_reply", schema = "onlinejudge", catalog = "")
public class ClarificationReply {
    private Long id;
    private Long clarId;
    private String content;
    private UserInfo createUser;
    private Timestamp createTime;

    @Id
    @Column(name = "id")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "clar_id")
    public Long getClarId() {
        return clarId;
    }

    public void setClarId(Long clarId) {
        this.clarId = clarId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ClarificationReply)) return false;
        ClarificationReply that = (ClarificationReply) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(clarId, that.clarId) &&
                Objects.equals(content, that.content) &&
                Objects.equals(createUser, that.createUser) &&
                Objects.equals(createTime, that.createTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, clarId, content, createUser, createTime);
    }
}
