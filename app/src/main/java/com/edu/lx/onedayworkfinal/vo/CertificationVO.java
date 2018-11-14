package com.edu.lx.onedayworkfinal.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class CertificationVO {

    String seekerId;
    String seekerCertificateIndex;
    String seekerCertificateNumber;
    String certificateId;
    String certificateName;

    public String getSeekerId() {
        return seekerId;
    }
    public void setSeekerId(String seekerId) {
        this.seekerId = seekerId;
    }
    public String getSeekerCertificateIndex() {
        return seekerCertificateIndex;
    }
    public void setSeekerCertificateIndex(String seekerCertificateIndex) {
        this.seekerCertificateIndex = seekerCertificateIndex;
    }
    public String getSeekerCertificateNumber() {
        return seekerCertificateNumber;
    }
    public void setSeekerCertificateNumber(String seekerCertificateNumber) {
        this.seekerCertificateNumber = seekerCertificateNumber;
    }
    public String getCertificateId() {
        return certificateId;
    }
    public void setCertificateId(String certificateId) {
        this.certificateId = certificateId;
    }
    public String getCertificateName() {
        return certificateName;
    }
    public void setCertificateName(String certificateName) {
        this.certificateName = certificateName;
    }
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this,ToStringStyle.JSON_STYLE);
    }
}
