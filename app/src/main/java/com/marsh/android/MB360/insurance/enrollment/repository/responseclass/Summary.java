package com.marsh.android.MB360.insurance.enrollment.repository.responseclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Summary {

    @SerializedName("data")
    @Expose
    private List<EnrollmentSummary> data = null;

    public List<EnrollmentSummary> getData() {
        return data;
    }

    public void setData(List<EnrollmentSummary> data) {
        this.data = data;
    }
}
