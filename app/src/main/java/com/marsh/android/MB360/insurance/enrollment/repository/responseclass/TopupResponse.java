
package com.marsh.android.MB360.insurance.enrollment.repository.responseclass;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class TopupResponse {

    @SerializedName("SumInsuredData")
    @Expose
    private SumInsuredData sumInsuredData;
    @SerializedName("IsEnrollmentSaved")
    @Expose
    private Integer isEnrollmentSaved;
    @SerializedName("IsWindowPeriodOpen")
    @Expose
    private Integer isWindowPeriodOpen;
    @SerializedName("ExtGroupSrNo")
    @Expose
    private Integer extGroupSrNo;
    @SerializedName("message")
    @Expose
    private Message message;

    public SumInsuredData getSumInsuredData() {
        return sumInsuredData;
    }

    public void setSumInsuredData(SumInsuredData sumInsuredData) {
        this.sumInsuredData = sumInsuredData;
    }

    public Integer getIsEnrollmentSaved() {
        return isEnrollmentSaved;
    }

    public void setIsEnrollmentSaved(Integer isEnrollmentSaved) {
        this.isEnrollmentSaved = isEnrollmentSaved;
    }

    public Integer getIsWindowPeriodOpen() {
        return isWindowPeriodOpen;
    }

    public void setIsWindowPeriodOpen(Integer isWindowPeriodOpen) {
        this.isWindowPeriodOpen = isWindowPeriodOpen;
    }

    public Integer getExtGroupSrNo() {
        return extGroupSrNo;
    }

    public void setExtGroupSrNo(Integer extGroupSrNo) {
        this.extGroupSrNo = extGroupSrNo;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

}
