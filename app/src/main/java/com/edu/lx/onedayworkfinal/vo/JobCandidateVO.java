package com.edu.lx.onedayworkfinal.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@SuppressWarnings("ALL")
public class JobCandidateVO {

    public int candidateNumber;
    public String seekerId;
    public int jobNumber;
    public String targetDate;
    public int candidateStatus;
    public String jobName;
    public String seekerName;
    public String score;
    public String evaluate;
    public int total;
    public int offWork;
    public int jobLimitCount;
    public int recruit;
    public String seekerSex;
    public String seekerBirth;



    public String getSeekerSex() {
        return seekerSex;
    }


    public void setSeekerSex(String seekerSex) {
        this.seekerSex = seekerSex;
    }


    public String getSeekerBirth() {
        return seekerBirth;
    }


    public void setSeekerBirth(String seekerBirth) {
        this.seekerBirth = seekerBirth;
    }

    public int getRecruit() {
        return recruit;
    }

    public void setRecruit(int recruit) {
        this.recruit = recruit;
    }
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


    public int getOffWork() {
        return offWork;
    }


    public void setOffWork(int offWork) {
        this.offWork = offWork;
    }
    public String getScore() {
        return score;
    }


    public void setScore(String score) {
        this.score = score;
    }


    public String getEvaluate() {
        return evaluate;
    }


    public void setEvaluate(String evaluate) {
        this.evaluate = evaluate;
    }


    public String getSeekerName() {
        return seekerName;
    }


    public void setSeekerName(String seekerName) {
        this.seekerName = seekerName;
    }


    public String getJobName() {
        return jobName;
    }


    public void setJobName(String jobName) {
        this.jobName = jobName;
    }


    public int getCandidateNumber() {
        return candidateNumber;
    }


    public void setCandidateNumber(int candidateNumber) {
        this.candidateNumber = candidateNumber;
    }


    public String getSeekerId() {
        return seekerId;
    }


    public void setSeekerId(String seekerId) {
        this.seekerId = seekerId;
    }


    public int getJobNumber() {
        return jobNumber;
    }


    public void setJobNumber(int jobNumber) {
        this.jobNumber = jobNumber;
    }


    public String getTargetDate() {
        return targetDate;
    }


    public void setTargetDate(String targetDate) {
        this.targetDate = targetDate;
    }


    public int getCandidateStatus() {
        return candidateStatus;
    }


    public void setCandidateStatus(int candidateStatus) {
        this.candidateStatus = candidateStatus;
    }


    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this,ToStringStyle.JSON_STYLE);
    }
}
