package com.marsh.android.MB360.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.marsh.android.MB360.utilities.LogMyBenefits
import com.marsh.android.MB360.utilities.LogTags

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {
        LogMyBenefits.d(LogTags.ALARM, "TRIGGERED")

        //workmanager or api call

    }
}