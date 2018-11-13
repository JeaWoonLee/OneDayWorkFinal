package com.edu.lx.onedayworkfinal.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@SuppressWarnings("ALL")
public class OfferVO {

    String offerId;
    String offerPw;
    String offerName;
    String offerAccount;
    String bank;
    String offerEmail;
    String companyNo;
    String companyName;
    int offerCash;

    public String getOfferId() {
        return offerId;
    }
    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }
    public String getOfferPw() {
        return offerPw;
    }
    public void setOfferPw(String offerPw) {
        this.offerPw = offerPw;
    }
    public String getOfferName() {
        return offerName;
    }
    public void setOfferName(String offerName) {
        this.offerName = offerName;
    }
    public String getOfferAccount() {
        return offerAccount;
    }
    public void setOfferAccount(String offerAccount) {
        this.offerAccount = offerAccount;
    }
    public String getBank() {
        return bank;
    }
    public void setBank(String bank) {
        this.bank = bank;
    }
    public String getOfferEmail() {
        return offerEmail;
    }
    public void setOfferEmail(String offerEmail) {
        this.offerEmail = offerEmail;
    }
    public String getCompanyNo() {
        return companyNo;
    }
    public void setCompanyNo(String companyNo) {
        this.companyNo = companyNo;
    }
    public String getCompanyName() {
        return companyName;
    }
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
    public int getOfferCash() {
        return offerCash;
    }
    public void setOfferCash(int offerCash) {
        this.offerCash = offerCash;
    }
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this,ToStringStyle.JSON_STYLE);
    }



}
