package com.xc.oj.entity;

import javax.persistence.*;

@Entity
@Table(name = "contest", schema = "onlinejudge", catalog = "")
public class ContestInfo {
    private Long id;
    private String title;
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
    @Column(name = "title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
