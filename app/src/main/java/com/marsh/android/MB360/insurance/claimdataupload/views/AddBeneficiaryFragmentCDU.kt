package com.marsh.android.MB360.insurance.claimdataupload.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.marsh.android.MB360.databinding.FragmentAddBeneficiaryCDUBinding
import com.marsh.android.MB360.insurance.claimdataupload.adapters.ClaimBenefeciaryAdapter
import com.marsh.android.MB360.insurance.claimdataupload.interfaces.BeneficiarySelectListener
import com.marsh.android.MB360.insurance.claimdataupload.interfaces.ViewPagerNavigation
import com.marsh.android.MB360.insurance.repository.LoadSessionViewModel
import com.marsh.android.MB360.insurance.repository.responseclass.GroupGMCPolicyEmpDependantsDatum
import com.marsh.android.MB360.utilities.EncryptionPreference
import com.marsh.android.MB360.utilities.LogMyBenefits


class AddBeneficiaryFragmentCDU : Fragment(), BeneficiarySelectListener {

    private var viewPagerNavigation: ViewPagerNavigation? = null
    private val binding get() = _binding!!
    private var _binding: FragmentAddBeneficiaryCDUBinding? = null
    var loadsessionViewModel: LoadSessionViewModel? = null
    private var items: List<GroupGMCPolicyEmpDependantsDatum> = mutableListOf()
    lateinit var adapter: ClaimBenefeciaryAdapter
    private var tpaSelected = false


    companion object {
        fun newInstance(viewPagerNavigation: ViewPagerNavigation, onTpaSelect: Boolean): AddBeneficiaryFragmentCDU {
            val fragment = AddBeneficiaryFragmentCDU()
            val args = Bundle()
            args.putSerializable("viewPagerNavigation", viewPagerNavigation)
            fragment.arguments = args
            fragment.tpaSelected = onTpaSelect

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
        // Inflate the layout for this fragment
        _binding = FragmentAddBeneficiaryCDUBinding.inflate(inflater, container, false)
        loadsessionViewModel = ViewModelProvider(requireActivity()).get(LoadSessionViewModel::class.java)
        val view = binding.root

        adapter = ClaimBenefeciaryAdapter(items, requireContext(), this)
        binding.dependantCycle.adapter = adapter


        loadsessionViewModel!!.loadSessionData.observe(viewLifecycleOwner) { loadSessionResponse ->

            if (loadSessionResponse != null) {
                if (loadSessionResponse.groupPoliciesEmployeesDependants[0].groupGMCPolicyEmpDependantsData != null) {

                    if (loadSessionResponse.groupPoliciesEmployeesDependants[0].groupGMCPolicyEmpDependantsData.isNotEmpty()) {
                        items = loadSessionResponse.groupPoliciesEmployeesDependants[0].groupGMCPolicyEmpDependantsData
                        adapter.updateData(items)
                        LogMyBenefits.d("LIST", items.toString())

                    } else {
                        //empty person list
                    }

                } else {
                    //no response
                }
            } else {

            }
        }

        if (getTpaPref()) {
            binding.claimIntimationLayout.visibility = View.VISIBLE
        } else {
            binding.claimIntimationLayout.visibility = View.GONE
        }




        return view
    }

    override fun onBeneficiarySelect(position: Int, person: GroupGMCPolicyEmpDependantsDatum) {
        binding.dependantCycle.post {
            for (item in items) {
                if (item.isSelected) {
                    item.isSelected = false
                }
            }
            items[position].isSelected = true
            person.isSelected = true
            adapter.notifyItemRangeChanged(0, items.size)
            viewPagerNavigation?.enableNextLayout()
        }


    }

    private fun validate() {
        val atLeastOneSelected = items.any { it.isSelected }
        if (atLeastOneSelected) {
            viewPagerNavigation?.enableNextLayout()
        } else {
            viewPagerNavigation?.disableNextLayout()
        }
    }

    private fun getTpaPref(): Boolean {
        return try {
            /*  val pref = EncryptionPreference(requireContext())
              pref.getEncryptedDataBooldefFalse("isTPA")*/
            val pref = EncryptionPreference(requireContext())

/*
            val preferenceChangeListener: OnSharedPreferenceChangeListener = pref.sharedPreferenceListener.onSharedPreferenceChanged { sharedPreferences, key ->
                // Handle the change for the specific preference key
                if ("your_preference_key" == key) {
                    val updatedValue = sharedPreferences.getString(key, defaultValue)
                    // Perform your desired action here
                }
            }*/



            pref.getEncryptedDataBooldefFalse("isTPA")

        } catch (ex: Exception) {

            ex.printStackTrace()
            false
        }

    }

    override fun onResume() {
        super.onResume()
        viewPagerNavigation?.disableNextLayout()
        validate()
        getTpaPref()
    }

}