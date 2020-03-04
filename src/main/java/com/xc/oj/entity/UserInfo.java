package com.xc.oj.entity;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "user", schema = "onlinejudge", catalog = "")
public class UserInfo {
    private Long id;
    private String username;
    private String nickname;
    private String realname;
    private List<String> role;
    @Transient
    private String token;

    public UserInfo(){

    }

    public UserInfo(Long id) {
        this.id=id;
    }


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
    @Type(type = "json" )
    @Column(name = "role",columnDefinition = "json")
    public List<String> getRole() {
        return role;
    }

    public void setRole(List<String> role) {
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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
                Objects.equals(role, userInfo.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, nickname, realname, role);
    }
}
