package com.marsh.android.MB360.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.marsh.android.MB360.utilities.LogMyBenefits
import com.marsh.android.MB360.utilities.LogTags
import java.util.Calendar

class AlarmScheduler {
    fun scheduleDailyAlarm(context: Context, calendar: Calendar) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(context, AlarmReceiver::class.java)
        alarmIntent.action = "com.marsh.android.MB360"

        val pendingIntent = PendingIntent.getBroadcast(
                context,
                0,
                alarmIntent,
                PendingIntent.FLAG_MUTABLE
        )


        try {
            alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
            )
            LogMyBenefits.d(LogTags.ALARM, "SCHEDULED")
        } catch (e: SecurityException) {
            LogMyBenefits.d(LogTags.ALARM, e.toString())
        }
    }
}