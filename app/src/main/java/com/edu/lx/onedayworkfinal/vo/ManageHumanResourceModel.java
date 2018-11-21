package com.edu.lx.onedayworkfinal.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class ManageHumanResourceModel {
    int jobNumber;
    int projectNumber;
    int total;
    int offWork;

    String seekerId;
    String seekerName;
    String targetDate;
    String seekerSex;
    String seekerBirth;
    public int getJobNumber() {
        return jobNumber;
    }
    public void setJobNumber(int jobNumber) {
        this.jobNumber = jobNumber;
    }
    public int getProjectNumber() {
        return projectNumber;
    }
    public void setProjectNumber(int projectNumber) {
        this.projectNumber = projectNumber;
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
    public String getSeekerId() {
        return seekerId;
    }
    public void setSeekerId(String seekerId) {
        this.seekerId = seekerId;
    }
    public String getSeekerName() {
        return seekerName;
    }
    public void setSeekerName(String seekerName) {
        this.seekerName = seekerName;
    }
    public String getTargetDate() {
        return targetDate;
    }
    public void setTargetDate(String targetDate) {
        this.targetDate = targetDate;
    }
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
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this,ToStringStyle.JSON_STYLE);
    }


}
