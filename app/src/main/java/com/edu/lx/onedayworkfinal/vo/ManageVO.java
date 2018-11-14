package com.edu.lx.onedayworkfinal.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class ManageVO {

    int projectNumber;
    int candidateStatus;
    int jobNumber;
    int jobPay;
    int jobLimitCount;
    int candidateNumber;
    double projectLat;
    double projectLng;
    String projectName;
    String offerId;
    String projectStartDate;
    String projectEndDate;
    String projectComment;
    String projectSubject;
    String projectEnrollDate;
    String projectRequirement;
    String workStartTime;
    String workEndTime;
    String jobName;
    String jobStartDate;
    String jobEndDate;
    String jobRequirement;
    String seekerId;
    String targetDate;

    public int getProjectNumber() {
        return projectNumber;
    }

    public void setProjectNumber(int projectNumber) {
        this.projectNumber = projectNumber;
    }

    public int getCandidateStatus() {
        return candidateStatus;
    }

    public void setCandidateStatus(int candidateStatus) {
        this.candidateStatus = candidateStatus;
    }

    public int getJobNumber() {
        return jobNumber;
    }

    public void setJobNumber(int jobNumber) {
        this.jobNumber = jobNumber;
    }

    public int getJobPay() {
        return jobPay;
    }

    public void setJobPay(int jobPay) {
        this.jobPay = jobPay;
    }

    public int getJobLimitCount() {
        return jobLimitCount;
    }

    public void setJobLimitCount(int jobLimitCount) {
        this.jobLimitCount = jobLimitCount;
    }

    public int getCandidateNumber() {
        return candidateNumber;
    }

    public void setCandidateNumber(int candidateNumber) {
        this.candidateNumber = candidateNumber;
    }

    public double getProjectLat() {
        return projectLat;
    }

    public void setProjectLat(double projectLat) {
        this.projectLat = projectLat;
    }

    public double getProjectLng() {
        return projectLng;
    }

    public void setProjectLng(double projectLng) {
        this.projectLng = projectLng;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }

    public String getProjectStartDate() {
        return projectStartDate;
    }

    public void setProjectStartDate(String projectStartDate) {
        this.projectStartDate = projectStartDate;
    }

    public String getProjectEndDate() {
        return projectEndDate;
    }

    public void setProjectEndDate(String projectEndDate) {
        this.projectEndDate = projectEndDate;
    }

    public String getProjectComment() {
        return projectComment;
    }

    public void setProjectComment(String projectComment) {
        this.projectComment = projectComment;
    }

    public String getProjectSubject() {
        return projectSubject;
    }

    public void setProjectSubject(String projectSubject) {
        this.projectSubject = projectSubject;
    }

    public String getProjectEnrollDate() {
        return projectEnrollDate;
    }

    public void setProjectEnrollDate(String projectEnrollDate) {
        this.projectEnrollDate = projectEnrollDate;
    }

    public String getProjectRequirement() {
        return projectRequirement;
    }

    public void setProjectRequirement(String projectRequirement) {
        this.projectRequirement = projectRequirement;
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

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobStartDate() {
        return jobStartDate;
    }

    public void setJobStartDate(String jobStartDate) {
        this.jobStartDate = jobStartDate;
    }

    public String getJobEndDate() {
        return jobEndDate;
    }

    public void setJobEndDate(String jobEndDate) {
        this.jobEndDate = jobEndDate;
    }

    public String getJobRequirement() {
        return jobRequirement;
    }

    public void setJobRequirement(String jobRequirement) {
        this.jobRequirement = jobRequirement;
    }

    public String getSeekerId() {
        return seekerId;
    }

    public void setSeekerId(String seekerId) {
        this.seekerId = seekerId;
    }

    public String getTargetDate() {
        return targetDate;
    }

    public void setTargetDate(String targetDate) {
        this.targetDate = targetDate;
    }

    @Override
    public String toString () {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}



