package com.edu.lx.onedayworkfinal.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class OfferWorkVO {

    int total;
    int recruit;
    int absent;
    int jobLimitCount;
    int projectNumber;
    String projectName;
    String workStartTime;
    String workEndTime;

    public int getJobLimitCount() {
        return jobLimitCount;
    }

    public void setJobLimitCount(int jobLimitCount) {
        this.jobLimitCount = jobLimitCount;
    }

    public int getTotal() {
        return total;
    }


    public void setTotal(int total) {
        this.total = total;
    }


    public int getRecruit() {
        return recruit;
    }


    public void setRecruit(int recruit) {
        this.recruit = recruit;
    }


    public int getAbsent() {
        return absent;
    }


    public void setAbsent(int absent) {
        this.absent = absent;
    }


    public int getProjectNumber() {
        return projectNumber;
    }


    public void setProjectNumber(int projectNumber) {
        this.projectNumber = projectNumber;
    }


    public String getProjectName() {
        return projectName;
    }


    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }


    public String getWorkStartTime() {
        return workStartTime;
    }


    public void setWorkStartTime(String workStartTime) {
        this.workStartTime = workStartTime;
    }


    public String getWorkEndTime() {
        return workEndTime;
    }


    public void setWorkEndTime(String workEndTime) {
        this.workEndTime = workEndTime;
    }


    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this,ToStringStyle.JSON_STYLE);
    }

}
