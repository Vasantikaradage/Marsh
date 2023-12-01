package com.marsh.android.MB360.insurance.claimdataupload.interfaces

import java.io.Serializable

interface FileUploadListerner : Serializable {
    fun fileOnClickListerner(param: Int)
}