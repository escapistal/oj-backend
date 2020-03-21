package com.xc.oj.entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("0")
public class Problem extends ProblemBase {
    public Problem() {
    }

    public Problem(Long pid) {
        this.setId(pid);
    }
}
