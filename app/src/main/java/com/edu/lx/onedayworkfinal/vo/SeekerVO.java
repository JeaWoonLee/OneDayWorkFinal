package com.edu.lx.onedayworkfinal.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class SeekerVO {


    String seekerId;
    String seekerPw;
    String jobId;
    String seekerName;
    String seekerAccount;
    String bank;
    String seekerBirth;
    int seekerCash;
    String seekerEmail;
    String seekerInfo;
    String seekerHeight;
    String seekerWeight;
    String seekerSex;
    double seekerLatitude;
    double seekerLongitude;

    public String getSeekerSex() {
        return seekerSex;
    }

    public void setSeekerSex(String seekerSex) {
        this.seekerSex = seekerSex;
    }

    public String getSeekerId() {
        return seekerId;
    }
    public void setSeekerId(String seekerId) {
        this.seekerId = seekerId;
    }
    public String getSeekerPw() {
        return seekerPw;
    }
    public void setSeekerPw(String seekerPw) {
        this.seekerPw = seekerPw;
    }
    public String getJobId() {
        return jobId;
    }
    public void setJobId(String jobId) {
        this.jobId = jobId;
    }
    public String getSeekerName() {
        return seekerName;
    }
    public void setSeekerName(String seekerName) {
        this.seekerName = seekerName;
    }
    public String getSeekerAccount() {
        return seekerAccount;
    }
    public void setSeekerAccount(String seekerAccount) {
        this.seekerAccount = seekerAccount;
    }
    public String getBank() {
        return bank;
    }
    public void setBank(String bank) {
        this.bank = bank;
    }
    public String getSeekerBirth() {
        return seekerBirth;
    }
    public void setSeekerBirth(String seekerBirth) {
        this.seekerBirth = seekerBirth;
    }
    public int getSeekerCash() {
        return seekerCash;
    }
    public void setSeekerCash(int seekerCash) {
        this.seekerCash = seekerCash;
    }
    public String getSeekerEmail() {
        return seekerEmail;
    }
    public void setSeekerEmail(String seekerEmail) {
        this.seekerEmail = seekerEmail;
    }
    public String getSeekerInfo() {
        return seekerInfo;
    }
    public void setSeekerInfo(String seekerInfo) {
        this.seekerInfo = seekerInfo;
    }
    public String getSeekerHeight() {
        return seekerHeight;
    }
    public void setSeekerHeight(String seekerHeight) {
        this.seekerHeight = seekerHeight;
    }
    public String getSeekerWeight() {
        return seekerWeight;
    }
    public void setSeekerWeight(String seekerWeight) {
        this.seekerWeight = seekerWeight;
    }
    public double getSeekerLatitude() {
        return seekerLatitude;
    }
    public void setSeekerLatitude(double seekerLatitude) {
        this.seekerLatitude = seekerLatitude;
    }
    public double getSeekerLongitude() {
        return seekerLongitude;
    }
    public void setSeekerLongitude(double seekerLongitude) {
        this.seekerLongitude = seekerLongitude;
    }
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this,ToStringStyle.JSON_STYLE);
    }

}
