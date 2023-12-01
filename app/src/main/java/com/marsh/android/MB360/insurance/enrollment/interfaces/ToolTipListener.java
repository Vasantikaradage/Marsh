package com.marsh.android.MB360.insurance.enrollment.interfaces;

import android.view.View;
import android.view.ViewGroup;

public interface ToolTipListener {
    void OnToolTipListener(String text, View view, ViewGroup rootview);

    void onWindowPeriodClickListener(String endDate);

}
