package com.edu.lx.onedayworkfinal.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;
import java.util.Map;

public class CommuteInfoVO {

    OfferWorkVO offerWorkVO;
    List<JobVO> jobList;
    Map<Integer,List<JobCandidateVO>> candidateMap;

    public OfferWorkVO getOfferWorkVO() {
        return offerWorkVO;
    }

    public void setOfferWorkVO(OfferWorkVO offerWorkVO) {
        this.offerWorkVO = offerWorkVO;
    }

    public List<JobVO> getJobList() {
        return jobList;
    }

    public void setJobList(List<JobVO> jobList) {
        this.jobList = jobList;
    }

    public Map<Integer, List<JobCandidateVO>> getCandidateMap() {
        return candidateMap;
    }

    public void setCandidateMap(Map<Integer, List<JobCandidateVO>> candidateMap) {
        this.candidateMap = candidateMap;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this,ToStringStyle.JSON_STYLE);
    }

}
