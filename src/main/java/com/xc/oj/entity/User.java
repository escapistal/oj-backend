package com.xc.oj.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.*;

@Entity
@TypeDef(name = "json", typeClass = JsonStringType.class)
@Table(name = "user", schema = "onlinejudge", catalog = "")
public class User implements Serializable, UserDetails, Comparable<User> {
    private Long id;
    private String username;
    private String password;
    private Timestamp createTime;
    private Timestamp lastLoginTime;
    private String email;
    private Boolean disabled;
    private List<String> role;
    private String nickname;
    private String realname;
    private String avatar;
    private List<Long> acceptedId;
    private Integer acceptedNumber;
    private Integer submissionNumber;

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
    @Column(name = "username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
    @Column(name = "create_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Basic
    @Column(name = "last_login_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    public Timestamp getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Timestamp lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    @Basic
    @Column(name = "email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Basic
    @Column(name = "disabled")
    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
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
    @Column(name = "avatar")
    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Basic
    @Type(type = "json" )
    @Column(name = "accepted_id",columnDefinition = "json")
    public List<Long> getAcceptedId() {
        return acceptedId;
    }

    public void setAcceptedId(List<Long> acceptedId) {
        this.acceptedId = acceptedId;
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
    @Column(name = "submission_number")
    public Integer getSubmissionNumber() {
        return submissionNumber;
    }

    public void setSubmissionNumber(Integer submissionNumber) {
        this.submissionNumber = submissionNumber;
    }

    @Transient
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Transient
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Transient
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Transient
    @Override
    public boolean isEnabled() {
        return !getDisabled();
    }

    @Transient
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> list=new ArrayList<>();
        role.forEach(r->list.add(new SimpleGrantedAuthority(r)));
        return list;
    }

    @Override
    public int compareTo(User o) {
        if(!acceptedNumber.equals(o.getAcceptedNumber()))
            return -acceptedNumber.compareTo(o.getAcceptedNumber());
        return submissionNumber.compareTo(o.getSubmissionNumber());
    }
}
