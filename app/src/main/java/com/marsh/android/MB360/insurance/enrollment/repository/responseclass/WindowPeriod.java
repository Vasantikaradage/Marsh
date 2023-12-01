package com.marsh.android.MB360.insurance.enrollment.repository.responseclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WindowPeriod {
    @SerializedName("groupChildSrNo")
    @Expose
    private String groupChildSrNo;
    @SerializedName("oeGrpBasInfoSrNo")
    @Expose
    private String oeGrpBasInfoSrNo;
    @SerializedName("windowStartDate_gmc")
    @Expose
    private String windowStartDateGmc;
    @SerializedName("windowStartDate_gpa")
    @Expose
    private String windowStartDateGpa;
    @SerializedName("windowStartDate_gtl")
    @Expose
    private String windowStartDateGtl;
    @SerializedName("windowEndDate_gmc")
    @Expose
    private String windowEndDateGmc;
    @SerializedName("windowEndDate_gpa")
    @Expose
    private String windowEndDateGpa;
    @SerializedName("windowEndDate_gtl")
    @Expose
    private String windowEndDateGtl;

    public String getGroupChildSrNo() {
        return groupChildSrNo;
    }

    public void setGroupChildSrNo(String groupChildSrNo) {
        this.groupChildSrNo = groupChildSrNo;
    }

    public String getOeGrpBasInfoSrNo() {
        return oeGrpBasInfoSrNo;
    }

    public void setOeGrpBasInfoSrNo(String oeGrpBasInfoSrNo) {
        this.oeGrpBasInfoSrNo = oeGrpBasInfoSrNo;
    }

    public String getWindowStartDateGmc() {
        return windowStartDateGmc;
    }

    public void setWindowStartDateGmc(String windowStartDateGmc) {
        this.windowStartDateGmc = windowStartDateGmc;
    }

    public String getWindowStartDateGpa() {
        return windowStartDateGpa;
    }

    public void setWindowStartDateGpa(String windowStartDateGpa) {
        this.windowStartDateGpa = windowStartDateGpa;
    }

    public String getWindowStartDateGtl() {
        return windowStartDateGtl;
    }

    public void setWindowStartDateGtl(String windowStartDateGtl) {
        this.windowStartDateGtl = windowStartDateGtl;
    }

    public String getWindowEndDateGmc() {
        return windowEndDateGmc;
    }

    public void setWindowEndDateGmc(String windowEndDateGmc) {
        this.windowEndDateGmc = windowEndDateGmc;
    }

    public String getWindowEndDateGpa() {
        return windowEndDateGpa;
    }

    public void setWindowEndDateGpa(String windowEndDateGpa) {
        this.windowEndDateGpa = windowEndDateGpa;
    }

    public String getWindowEndDateGtl() {
        return windowEndDateGtl;
    }

    public void setWindowEndDateGtl(String windowEndDateGtl) {
        this.windowEndDateGtl = windowEndDateGtl;
    }

    @Override
    public String toString() {
        return "WindowPeriod{" +
                "groupChildSrNo='" + groupChildSrNo + '\'' +
                ", oeGrpBasInfoSrNo='" + oeGrpBasInfoSrNo + '\'' +
                ", windowStartDateGmc='" + windowStartDateGmc + '\'' +
                ", windowStartDateGpa='" + windowStartDateGpa + '\'' +
                ", windowStartDateGtl='" + windowStartDateGtl + '\'' +
                ", windowEndDateGmc='" + windowEndDateGmc + '\'' +
                ", windowEndDateGpa='" + windowEndDateGpa + '\'' +
                ", windowEndDateGtl='" + windowEndDateGtl + '\'' +
                '}';
    }
}
