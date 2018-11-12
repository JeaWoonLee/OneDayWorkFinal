package com.edu.lx.onedayworkfinal.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class JobVO {

    int jobNumber;
    int projectNumber;
    int jobPay;
    String jobName;
    String jobStartDate;
    String jobEndDate;
    String jobRequirement;
    int jobLimitCount;

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

    public String getJobName () {
        return jobName;
    }

    public void setJobName (String jobName) {
        this.jobName = jobName;
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

    public int getJobLimitCount () {
        return jobLimitCount;
    }

    public void setJobLimitCount (int jobLimitCount) {
        this.jobLimitCount = jobLimitCount;
    }

    @Override
    public String toString () {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
