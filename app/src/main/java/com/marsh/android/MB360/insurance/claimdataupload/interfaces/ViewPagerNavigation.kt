package com.marsh.android.MB360.insurance.claimdataupload.interfaces

import java.io.Serializable

interface ViewPagerNavigation : Serializable {
    //just for commit
    fun nextPage()
    fun previousPage()
    fun disableNextLayout()
    fun enableNextLayout()

    fun onTpaSelect(boolean: Boolean)
}