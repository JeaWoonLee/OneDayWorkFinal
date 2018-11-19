package com.edu.lx.onedayworkfinal.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@SuppressWarnings("ALL")
public class ProjectVO {



    int projectNumber;
    //프로젝트 이름
    String projectName;
    //발주자
    String offerId;
    //프로젝트 기간
    String projectStartDate;
    String projectEndDate;
    //프로젝트 위치
    double projectLat;
    double projectLng;
    //프로젝트 설명
    String projectComment;
    //업종 분류
    String projectSubject;
    //등록날짜
    String projectEnrollDate;
    //근무시간
    String workStartTime;
    String workEndTime;
    String seekerId;
    //제약사항
    String constCertificate;
    String requestPicture;
    //제공사항
    String morning;
    String launch;
    String evening;
    String commute;
    String offWork;


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


    public String getSeekerId() {
        return seekerId;
    }


    public void setSeekerId(String seekerId) {
        this.seekerId = seekerId;
    }


    public String getConstCertificate() {
        return constCertificate;
    }


    public void setConstCertificate(String constCertificate) {
        this.constCertificate = constCertificate;
    }


    public String getRequestPicture() {
        return requestPicture;
    }


    public void setRequestPicture(String requestPicture) {
        this.requestPicture = requestPicture;
    }


    public String getMorning() {
        return morning;
    }


    public void setMorning(String morning) {
        this.morning = morning;
    }


    public String getLaunch() {
        return launch;
    }


    public void setLaunch(String launch) {
        this.launch = launch;
    }


    public String getEvening() {
        return evening;
    }


    public void setEvening(String evening) {
        this.evening = evening;
    }


    public String getCommute() {
        return commute;
    }


    public void setCommute(String commute) {
        this.commute = commute;
    }


    public String getOffWork() {
        return offWork;
    }


    public void setOffWork(String offWork) {
        this.offWork = offWork;
    }


    @Override
    public String toString () {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
