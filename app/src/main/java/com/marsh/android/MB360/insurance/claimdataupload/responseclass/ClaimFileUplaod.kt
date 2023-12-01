package com.marsh.android.MB360.insurance.claimdataupload.responseclass

import android.net.Uri

data class ClaimFileUpload(
        var items: String,
        private var int: Int,
        var status: Boolean,
        var fileSize: String,
        var fileName: String,
        var mandatory: Boolean,
        var data: Uri?
)


