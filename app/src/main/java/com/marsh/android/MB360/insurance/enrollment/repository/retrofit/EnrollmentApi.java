package com.marsh.android.MB360.insurance.enrollment.repository.retrofit;

import com.marsh.android.MB360.insurance.enrollment.repository.responseclass.DependantDetailsResponse;
import com.marsh.android.MB360.insurance.enrollment.repository.responseclass.Dependent;
import com.marsh.android.MB360.insurance.enrollment.repository.responseclass.EmployeeResponse;
import com.marsh.android.MB360.insurance.enrollment.repository.responseclass.EnrollmentMessage;
import com.marsh.android.MB360.insurance.enrollment.repository.responseclass.InstructionResponse;
import com.marsh.android.MB360.insurance.enrollment.repository.responseclass.MyCoveragesResponse;
import com.marsh.android.MB360.insurance.enrollment.repository.responseclass.Parent;
import com.marsh.android.MB360.insurance.enrollment.repository.responseclass.WindowPeriodEnrollmentResponse;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface EnrollmentApi {
    //here we write the end points for enrollment api.


    //EnrollmentWindow period
    @GET("window period")
    Call<WindowPeriodEnrollmentResponse> getWindowPeriod();


    //instruction api
    @GET("instruction url")
    Call<InstructionResponse> getEnrollmentInstructions();


    //Enrollment Coverages Data Call
    @GET("coverages url")
    Call<MyCoveragesResponse> getEnrollmentCoverages();

    //Enrollment employee details call
    @GET("getEmployeeDetails")
    Call<EmployeeResponse> getEnrollmentEmployeeDetails();

    //Enrollment dependant details call
    @GET("dependant details response")
    Call<DependantDetailsResponse> getRelationShipGroup();

    //Enrollment get Dependant call
    @GET("EnrollmentDetails/GetDependants")
    Call<List<Dependent>> getDependants(@Query("Windowperiodactive") String windowPeriodActive,
                                        @Query("Grpchildsrno") String groupChildSrNo,
                                        @Query("Oegrpbasinfsrno") String oeGrpBasInfoSrNo,
                                        @Query("EmpSrNo") String employeeSrNo);

    //Enrollment Add Dependant call and Parent
    @POST("EnrollmentDetails/AddDependant")
    Call<EnrollmentMessage> addDependant(@QueryMap(encoded = true) Map<String, String> dependantsOption);

    //Enrollment Delete Dependant call
    @POST("EnrollmentDetails/DeleteDependant")
    Call<EnrollmentMessage> deleteDependant(@QueryMap Map<String, String> dependantsOption);

    //Enrollment Edit Dependant call
    @POST("EnrollmentDetails/UpdateDependant")
    Call<EnrollmentMessage> updateDependant(@QueryMap(encoded = true) Map<String, String> dependantOption);

    //Enrollment get Parental call
    @GET("EnrollmentDetails/GetParentalDependants")
    Call<List<Parent>> getParentalDependant(@Query("IsWindowPeriodActive") String windowPeriodActive,
                                            @Query("GroupChildSrNo") String groupChildSrNo,
                                            @Query("OeGrpBasInfSrNo") String oeGrpBasInfoSrNo,
                                            @Query("EmpSrNo") String employeeSrNo,
                                            @Query("parentalPremium") String parentalPremium);

    //Enrollment update Parental call
    @POST("EnrollmentDetails/UpdateDependant")
    Call<EnrollmentMessage> updateParent(@QueryMap(encoded = true) Map<String, String> parentOption);


    //Enrollment delete Parental call
    @POST("EnrollmentDetails/DeleteParentalDependant")
    Call<EnrollmentMessage> deleteParent(@QueryMap(encoded = true) Map<String, String> parentOption);

}
