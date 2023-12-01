package com.marsh.android.MB360.utilities.nonswipeableviewpager

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

class NonSwipeableViewPager(context: Context, attrs: AttributeSet?) : ViewPager(context, attrs) {

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        // Disable swipe by intercepting touch events
        return false
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        // Disable touch events
        return false
    }
}