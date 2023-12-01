package com.marsh.android.MB360.utilities.progressview.listeners;


import com.marsh.android.MB360.utilities.progressview.ProgressView;
import com.marsh.android.MB360.utilities.progressview.components.StateItem;

public interface OnStateItemClickListener {

    void onStateItemClick(ProgressView stateProgressBar, StateItem stateItem, int stateNumber, boolean isCurrentState);

}
