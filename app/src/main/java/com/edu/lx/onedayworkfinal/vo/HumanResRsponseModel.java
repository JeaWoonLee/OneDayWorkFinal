package com.edu.lx.onedayworkfinal.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.HashMap;
import java.util.List;

public class HumanResRsponseModel {
    List<JobCandidateVO> jobNumberList;
    HashMap<Integer,List<ManageHumanResourceModel>> recruitMap;
    public List<JobCandidateVO> getJobNumberList() {
        return jobNumberList;
    }
    public void setJobNumberList(List<JobCandidateVO> jobNumberList) {
        this.jobNumberList = jobNumberList;
    }
    public HashMap<Integer, List<ManageHumanResourceModel>> getRecruitMap() {
        return recruitMap;
    }
    public void setRecruitMap(HashMap<Integer, List<ManageHumanResourceModel>> recruitMap) {
        this.recruitMap = recruitMap;
    }
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this,ToStringStyle.JSON_STYLE);
    }
}
