package com.marsh.android.MB360.insurance.claimdataupload

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.marsh.android.MB360.BuildConfig
import com.marsh.android.MB360.databinding.FragmentClaimDataUploadBinding
import com.marsh.android.MB360.insurance.repository.LoadSessionViewModel

class ClaimDataUploadFragment : Fragment() {

    private var _binding: FragmentClaimDataUploadBinding? = null
    private val binding get() = _binding!!
    var loadSessionViewModel: LoadSessionViewModel? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        _binding = FragmentClaimDataUploadBinding.inflate(inflater, container, false)
        loadSessionViewModel = ViewModelProvider(requireActivity()).get(LoadSessionViewModel::class.java)
        val loadSessionResponse = loadSessionViewModel!!.loadSessionData.value
        val view = binding.root
        val emp_sr_no: String = loadSessionResponse!!.groupPoliciesEmployees.get(0).groupGMCPolicyEmployeeData.get(0).employeeSrNo
        binding.webviewClaim.loadUrl(BuildConfig.DOWNLOAD_BASE_URL + "HandleAppSession.ASPX?APPLOGIN=" + emp_sr_no + "?QVAL=claimsdocumentupload")
        return view
    }


}