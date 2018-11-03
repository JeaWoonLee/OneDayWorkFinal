package com.edu.lx.onedayworkfinal.join;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class ResponseVO {

    final static int JOIN_RESPONSE_SUCCESS = 1;
    final static int JOIN_RESPONSE_FAIL = 0;
    final static int OVERLAP_CHECK_SUCCESS = 0;
    final static int OVERLAP_CHECK_FAIL = 1;
    int response;

    public int getResponse () {
        return response;
    }

    public void setResponse (int response) {
        this.response = response;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this,ToStringStyle.JSON_STYLE);
    }
}
