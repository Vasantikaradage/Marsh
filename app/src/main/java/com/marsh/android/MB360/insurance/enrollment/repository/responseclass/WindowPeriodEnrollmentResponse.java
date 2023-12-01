package com.marsh.android.MB360.insurance.enrollment.repository.responseclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WindowPeriodEnrollmentResponse {
    @SerializedName("windowPeriod")
    @Expose
    private WindowPeriod windowPeriod;
    @SerializedName("message")
    @Expose
    private Message message;

    public WindowPeriod getWindowPeriod() {
        return windowPeriod;
    }

    public void setWindowPeriod(WindowPeriod windowPeriod) {
        this.windowPeriod = windowPeriod;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "WindowPeriodEnrollmentResponse{" +
                "windowPeriod=" + windowPeriod +
                ", message=" + message +
                '}';
    }
}
