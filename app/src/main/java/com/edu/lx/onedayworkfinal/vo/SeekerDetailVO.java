package com.edu.lx.onedayworkfinal.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;

public class SeekerDetailVO {
    SeekerVO seekerVO;
    List<JobCandidateVO> record;
    List<CertificationVO> certificate;
    public SeekerVO getSeekerVO() {
        return seekerVO;
    }
    public void setSeekerVO(SeekerVO seekerVO) {
        this.seekerVO = seekerVO;
    }
    public List<JobCandidateVO> getRecord() {
        return record;
    }
    public void setRecord(List<JobCandidateVO> record) {
        this.record = record;
    }
    public List<CertificationVO> getCertificate() {
        return certificate;
    }
    public void setCertificate(List<CertificationVO> certificate) {
        this.certificate = certificate;
    }
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this,ToStringStyle.JSON_STYLE);
    }
}
