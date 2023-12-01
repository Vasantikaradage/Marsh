package com.marsh.android.MB360.insurance.claimdataupload.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.marsh.android.MB360.insurance.claimdataupload.interfaces.ViewPagerNavigation
import com.marsh.android.MB360.insurance.claimdataupload.views.AddBeneficiaryFragmentCDU
import com.marsh.android.MB360.insurance.claimdataupload.views.ClaimsDetailFragmentCDU
import com.marsh.android.MB360.insurance.claimdataupload.views.FileUploadFragmentCDU
import com.marsh.android.MB360.insurance.claimdataupload.views.FileUploadStatusFragment

class ClaimViewPagerAdapter(activity: FragmentActivity, private val viewPagerNavigation: ViewPagerNavigation) :
        FragmentStateAdapter(activity) {
    private var onTpaSelect = false

    fun ontpaSelect(show: Boolean) {
        onTpaSelect = show
    }

    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ClaimsDetailFragmentCDU.newInstance(viewPagerNavigation)
            1 -> AddBeneficiaryFragmentCDU.newInstance(viewPagerNavigation, onTpaSelect)
            2 -> FileUploadFragmentCDU.newInstance(viewPagerNavigation)
            3 -> FileUploadStatusFragment.newInstance(viewPagerNavigation)
            else -> ClaimsDetailFragmentCDU.newInstance(viewPagerNavigation)
        }
    }
}