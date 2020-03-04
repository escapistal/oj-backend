package com.xc.oj.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
public class Announcement {
    private Long id;
    private Integer sortId;
    private String title;
    private String content;
    private Boolean visible;
    private Timestamp createTime;
    private Timestamp updateTime;
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
    @Column(name = "title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Basic
    @Column(name = "content")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Basic
    @Column(name = "update_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Announcement)) return false;
        Announcement that = (Announcement) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(sortId, that.sortId) &&
                Objects.equals(title, that.title) &&
                Objects.equals(content, that.content) &&
                Objects.equals(visible, that.visible) &&
                Objects.equals(createTime, that.createTime) &&
                Objects.equals(updateTime, that.updateTime) &&
                Objects.equals(createUser, that.createUser) &&
                Objects.equals(updateUser, that.updateUser);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sortId, title, content, visible, createTime, updateTime, createUser, updateUser);
    }
}