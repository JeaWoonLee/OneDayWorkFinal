package com.edu.lx.onedayworkfinal.vo;

@SuppressWarnings("ALL")
public class JobCandidateVO {


    int candidateNumber;
    int jobNumber;
    int candidateStatus;
    String targetDate;
    String seekerId;

    public void setCandidateNumber(int candidateNumber) {
        this.candidateNumber = candidateNumber;
    }

    public void setJobNumber(int jobNumber) {
        this.jobNumber = jobNumber;
    }

    @Override
    public String toString() {
        return "JobCandidateVO{" +
                "candidateNumber=" + candidateNumber +
                ", jobNumber=" + jobNumber +
                ", candidateStatus=" + candidateStatus +
                ", targetDate='" + targetDate + '\'' +
                ", seekerId='" + seekerId + '\'' +
                '}';
    }

    public void setCandidateStatus(int candidateStatus) {
        this.candidateStatus = candidateStatus;
    }

    public void setTargetDate(String targetDate) {
        this.targetDate = targetDate;
    }

    public void setSeekerId(String seekerId) {
        this.seekerId = seekerId;
    }

    public int getCandidateNumber() {

        return candidateNumber;
    }

    public int getJobNumber() {
        return jobNumber;
    }

    public int getCandidateStatus() {
        return candidateStatus;
    }

    public String getTargetDate() {
        return targetDate;
    }

    public String getSeekerId() {
        return seekerId;
    }
}
