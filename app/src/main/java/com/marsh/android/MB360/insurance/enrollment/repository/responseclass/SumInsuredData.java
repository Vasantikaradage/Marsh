
package com.marsh.android.MB360.insurance.enrollment.repository.responseclass;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class SumInsuredData {

    @SerializedName("Enroll_Topup_Options")
    @Expose
    private EnrollTopupOptions enrollTopupOptions;
    @SerializedName("message")
    @Expose
    private Message message;

    public EnrollTopupOptions getEnrollTopupOptions() {
        return enrollTopupOptions;
    }

    public void setEnrollTopupOptions(EnrollTopupOptions enrollTopupOptions) {
        this.enrollTopupOptions = enrollTopupOptions;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

}
