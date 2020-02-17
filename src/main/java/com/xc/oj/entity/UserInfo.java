package com.xc.oj.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "user", schema = "onlinejudge", catalog = "")
public class UserInfo {
    private Long id;
    private String username;
    private String nickname;
    private String realname;
    private String type;

    @Id
    @Column(name = "id")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Basic
    @Column(name = "nickname")
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Basic
    @Column(name = "realname")
    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    @Basic
    @Column(name = "type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserInfo)) return false;
        UserInfo userInfo = (UserInfo) o;
        return Objects.equals(id, userInfo.id) &&
                Objects.equals(username, userInfo.username) &&
                Objects.equals(nickname, userInfo.nickname) &&
                Objects.equals(realname, userInfo.realname) &&
                Objects.equals(type, userInfo.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, nickname, realname, type);
    }
}
