package com.marsh.android.MB360.insurance.enrollment.repository.retrofit;


import com.marsh.android.MB360.insurance.enrollment.repository.responseclass.SummaryFileResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SummeryAPi {
    //Download Enrollment Summary
    @GET("EmployeeEnrollement/EnrollmentSummaryFile?")
    Call<SummaryFileResponse> getSummaryDetails(@Query("EmpSrNo") String empSrNo);

}
