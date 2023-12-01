package com.marsh.android.MB360.insurance.enrollmentstatus.retrofit;

import com.marsh.android.MB360.insurance.enrollmentstatus.responseclass.EnrollmentStatus;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface EnrollmentStatusApi {

    @POST("EnrollmentStatus/GetEnrollStatus")
    Call<EnrollmentStatus> getEnrollmentStatus(@Query("employeesrno") String employeeSrNo,
                                               @Query("GroupChildSrNo") String groupChildSrNo,
                                               @Query("OeGrpBasInfSrNo") String oeGrpBasInfSrNo);

}
