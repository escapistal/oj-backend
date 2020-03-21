package com.xc.oj.entity;

import javax.persistence.*;

@Entity
@DiscriminatorValue("1")
public class ContestSubmission extends SubmissionBase{
    private ContestInfo contest;

    @ManyToOne
    @JoinColumn(name="contest_id")
    public ContestInfo getContest() {
        return contest;
    }

    public void setContest(ContestInfo contest) {
        this.contest = contest;
    }

}
