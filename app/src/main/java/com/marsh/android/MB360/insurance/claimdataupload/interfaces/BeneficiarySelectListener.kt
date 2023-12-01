package com.marsh.android.MB360.insurance.claimdataupload.interfaces

import com.marsh.android.MB360.insurance.repository.responseclass.GroupGMCPolicyEmpDependantsDatum

interface BeneficiarySelectListener {

    fun onBeneficiarySelect(position: Int, person: GroupGMCPolicyEmpDependantsDatum)
}