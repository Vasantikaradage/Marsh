package com.marsh.android.MB360.onboarding.authentication.responseclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginResponse {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("OTPStatusInformation")
    @Expose
    private String oTPStatusInformation;

    public String getStatus() {
        return status;
    }

    public String getOTPStatusInformation() {
        return oTPStatusInformation;
    }
}
