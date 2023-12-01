package com.marsh.android.MB360.insurance.enrollment.repository.responseclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EmployeeResponse {
    @SerializedName("employee_details")
    @Expose
    private List<EmployeeDetail> employeeDetails = null;

    public List<EmployeeDetail> getEmployeeDetails() {
        return employeeDetails;
    }

    public void setEmployeeDetails(List<EmployeeDetail> employeeDetails) {
        this.employeeDetails = employeeDetails;
    }

    @Override
    public String toString() {
        return "EmployeeResponse{" +
                "employeeDetails=" + employeeDetails +
                '}';
    }
}
