package com.edu.lx.onedayworkfinal.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class DateVO {

    int projectNumber;
    int targetDate;
    String projectSubject;
    String projectStartDate;
    String projectEndDate;
    String projectEnrollDate;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }

    public int getProjectNumber() {
        return projectNumber;
    }

    public void setProjectNumber(int projectNumber) {
        this.projectNumber = projectNumber;
    }

    public int getTargetDate() {
        return targetDate;
    }

    public void setTargetDate(int targetDate) {
        this.targetDate = targetDate;
    }

    public String getProjectSubject() {
        return projectSubject;
    }

    public void setProjectSubject(String projectSubject) {
        this.projectSubject = projectSubject;
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

    public String getProjectEnrollDate() {
        return projectEnrollDate;
    }

    public void setProjectEnrollDate(String projectEnrollDate) {
        this.projectEnrollDate = projectEnrollDate;
    }
}
