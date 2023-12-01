package com.marsh.android.MB360.insurance.claimdataupload.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.marsh.android.MB360.databinding.FragmentClaimsDetailCDUBinding
import com.marsh.android.MB360.insurance.claimdataupload.interfaces.ViewPagerNavigation
import com.marsh.android.MB360.utilities.EncryptionPreference


class ClaimsDetailFragmentCDU : Fragment() {

    private var viewPagerNavigation: ViewPagerNavigation? = null
    private var _binding: FragmentClaimsDetailCDUBinding? = null

    private var isClaimIntimated: Boolean = false
    private var whereClaimIsIntimated: Boolean = false
    private var typeOfClaim: Boolean = false

    private val binding get() = _binding!!

    companion object {
        fun newInstance(viewPagerNavigation: ViewPagerNavigation): ClaimsDetailFragmentCDU {
            val fragment = ClaimsDetailFragmentCDU()
            val args = Bundle()
            args.putSerializable("viewPagerNavigation", viewPagerNavigation)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            viewPagerNavigation = it.getSerializable("viewPagerNavigation") as? ViewPagerNavigation
        }

    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentClaimsDetailCDUBinding.inflate(inflater, container, false)
        val view = binding.root

        setupCheckBoxListeners()
        validate()

        binding.claimIntimatedYes.setOnCheckedChangeListener { compoundButton, b ->
            isClaimIntimated = true
            validate()
        }
        binding.claimIntimatedNo.setOnCheckedChangeListener { compoundButton, b ->
            isClaimIntimated = true
            validate()
        }

        binding.claimIntimatedYes2.setOnCheckedChangeListener { compoundButton, b ->
            whereClaimIsIntimated = true
            validate()
        }
        binding.claimIntimatedNo2.setOnCheckedChangeListener { compoundButton, b ->
           // viewPagerNavigation?.onTpaSelect(b)
            whereClaimIsIntimated = true
            validate()
            saveTPApref(b)
        }



        return view
    }

    private fun saveTPApref(isTpa: Boolean) {
        val pref = EncryptionPreference(requireContext())

        pref.setEncryptedBoolString("isTPA", isTpa)
    }

    private fun setupCheckBoxListeners() {
        val checkBoxes = arrayOf(
                binding.checkboxPreHospital,
                binding.checkboxMainHospital,
                binding.checkboxPostHospital
        )

        for (checkBox in checkBoxes) {
            checkBox.setOnCheckedChangeListener { _, isChecked ->
                // Check if at least one checkbox is checked
                val atLeastOneChecked = checkBoxes.any { it.isChecked }
                typeOfClaim = atLeastOneChecked
                validate()
            }
        }
    }

    private fun validate() {
        if (isClaimIntimated && whereClaimIsIntimated && typeOfClaim) {
            viewPagerNavigation?.enableNextLayout()
        } else {
            viewPagerNavigation?.disableNextLayout()
        }
    }

    override fun onResume() {
        super.onResume()
        validate()
    }


}