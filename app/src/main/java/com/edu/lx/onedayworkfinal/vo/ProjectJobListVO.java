package com.edu.lx.onedayworkfinal.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class ProjectJobListVO {

    int jobNumber;
    int projectNumber;
    int jobPay;
    String jobName;
    String jobStartDate;
    String jobEndDate;
    String jobRequirement;
    String workStartTime;
    String workEndTime;
    int jobLimitCount;
    int jobCurrentCount;

    public String getJobName () {
        return jobName;
    }

    public void setJobName (String jobName) {
        this.jobName = jobName;
    }

    public int getJobNumber () {
        return jobNumber;
    }

    public void setJobNumber (int jobNumber) {
        this.jobNumber = jobNumber;
    }

    public int getProjectNumber () {
        return projectNumber;
    }

    public void setProjectNumber (int projectNumber) {
        this.projectNumber = projectNumber;
    }

    public int getJobPay () {
        return jobPay;
    }

    public void setJobPay (int jobPay) {
        this.jobPay = jobPay;
    }

    public String getJobStartDate () {
        return jobStartDate;
    }

    public void setJobStartDate (String jobStartDate) {
        this.jobStartDate = jobStartDate;
    }

    public String getJobEndDate () {
        return jobEndDate;
    }

    public void setJobEndDate (String jobEndDate) {
        this.jobEndDate = jobEndDate;
    }

    public String getJobRequirement () {
        return jobRequirement;
    }

    public void setJobRequirement (String jobRequirement) {
        this.jobRequirement = jobRequirement;
    }

    public String getWorkStartTime () {
        return workStartTime;
    }

    public void setWorkStartTime (String workStartTime) {
        this.workStartTime = workStartTime;
    }

    public String getWorkEndTime () {
        return workEndTime;
    }

    public void setWorkEndTime (String workEndTime) {
        this.workEndTime = workEndTime;
    }

    public int getJobLimitCount () {
        return jobLimitCount;
    }

    public void setJobLimitCount (int jobLimitCount) {
        this.jobLimitCount = jobLimitCount;
    }

    public int getJobCurrentCount () {
        return jobCurrentCount;
    }

    public void setJobCurrentCount (int jobCurrentCount) {
        this.jobCurrentCount = jobCurrentCount;
    }

    @Override
    public String toString () {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
