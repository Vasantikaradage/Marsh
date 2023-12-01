
package com.marsh.android.MB360.insurance.enrollment.repository.responseclass;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class EnrollTopupOptions {

    @SerializedName("TopupApplicability_data")
    @Expose
    private TopupApplicabilityData topupApplicabilityData;
    @SerializedName("TopupSumInsured_Cls_data")
    @Expose
    private TopupSumInsuredClsData topupSumInsuredClsData;

    public TopupApplicabilityData getTopupApplicabilityData() {
        return topupApplicabilityData;
    }

    public void setTopupApplicabilityData(TopupApplicabilityData topupApplicabilityData) {
        this.topupApplicabilityData = topupApplicabilityData;
    }

    public TopupSumInsuredClsData getTopupSumInsuredClsData() {
        return topupSumInsuredClsData;
    }

    public void setTopupSumInsuredClsData(TopupSumInsuredClsData topupSumInsuredClsData) {
        this.topupSumInsuredClsData = topupSumInsuredClsData;
    }

}
