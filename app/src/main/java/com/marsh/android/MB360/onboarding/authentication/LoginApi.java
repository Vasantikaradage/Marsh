package com.marsh.android.MB360.onboarding.authentication;

import com.marsh.android.MB360.onboarding.authentication.responseclass.LoginEmailRequest;
import com.marsh.android.MB360.onboarding.authentication.responseclass.LoginIDResponse;
import com.marsh.android.MB360.onboarding.authentication.responseclass.LoginIdRequest;
import com.marsh.android.MB360.onboarding.authentication.responseclass.LoginRequest;
import com.marsh.android.MB360.onboarding.authentication.responseclass.LoginResponse;
import com.marsh.android.MB360.onboarding.validation.responseclass.ValidationEmailRequest;
import com.marsh.android.MB360.onboarding.validation.responseclass.ValidationRequest;
import com.marsh.android.MB360.onboarding.validation.responseclass.ValidationResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LoginApi {

    @POST("Login/RequestOTP")
        //endpoint
    Call<LoginResponse> RequestOTP(
            @Body LoginRequest mobileNumber
    );

    @POST("Login/RequestOTP")
        //endpoint
    Call<LoginResponse> RequestEmailOTP(
            @Body LoginEmailRequest emailId
    );

    @POST("Login/ValidateLogin")
        //endpoint
    Call<LoginIDResponse> RequestLoginIdOTP(
            @Body LoginIdRequest loginIdRequest
    );


    @POST("Login/ValidateOTP")
        //endpoint
    Call<ValidationResponse> ValidateOTP(
            @Body ValidationRequest mobileNumber
    );

    @POST("Login/ValidateOTP")
        //endpoint
    Call<ValidationResponse> ValidateEmailOTP(
            @Body ValidationEmailRequest validationEmailRequest
    );
}
