package com.xc.oj.entity;


import java.io.Serializable;
import java.util.Objects;

public class SingleSubmissionInfo implements Serializable {

    private Boolean isAc;
    private Integer acTime;
    private Integer error;

    public SingleSubmissionInfo() {
        isAc=false;
        acTime=0;
        error=0;
    }

    public Boolean getAc() {
        return isAc;
    }

    public void setAc(Boolean ac) {
        isAc = ac;
    }

    public Integer getAcTime() {
        return acTime;
    }

    public void setAcTime(Integer acTime) {
        this.acTime = acTime;
    }

    public Integer getError() {
        return error;
    }

    public void setError(Integer error) {
        this.error = error;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SingleSubmissionInfo)) return false;
        SingleSubmissionInfo that = (SingleSubmissionInfo) o;
        return Objects.equals(isAc, that.isAc) &&
                Objects.equals(acTime, that.acTime) &&
                Objects.equals(error, that.error);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isAc, acTime, error);
    }
}
