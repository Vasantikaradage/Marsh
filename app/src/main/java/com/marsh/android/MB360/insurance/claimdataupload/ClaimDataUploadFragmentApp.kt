package com.marsh.android.MB360.insurance.claimdataupload

import android.content.res.ColorStateList
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.marsh.android.MB360.R
import com.marsh.android.MB360.databinding.FragmentClaimDataUploadAppBinding
import com.marsh.android.MB360.insurance.claimdataupload.adapters.ClaimViewPagerAdapter
import com.marsh.android.MB360.insurance.claimdataupload.interfaces.ViewPagerNavigation
import com.marsh.android.MB360.insurance.claimdataupload.views.AddBeneficiaryFragmentCDU
import com.marsh.android.MB360.utilities.LogMyBenefits
import com.marsh.android.MB360.utilities.progressview.ProgressView


class ClaimDataUploadFragmentApp() : Parcelable, Fragment(), ViewPagerNavigation {
    private var _binding: FragmentClaimDataUploadAppBinding? = null

    private val binding get() = _binding!!
    private lateinit var viewPager: ViewPager2
    private var pagerAdapter: ClaimViewPagerAdapter? = null
    private var descriptionData = arrayOf("Claims Details", "Add Beneficiary", "File Upload")
    var onPageChangeCallback: ViewPager2.OnPageChangeCallback? = null

    constructor(parcel: Parcel) : this() {
        descriptionData = parcel.createStringArray()!!
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentClaimDataUploadAppBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.stateProgressBar.setStateDescriptionData(descriptionData)
        binding.stateProgressBar.setStateDescriptionTypeface("font/noto_sans_medium.ttf")



        viewPager = binding.claimDataUploadViewPager
        viewPager.isUserInputEnabled = false
        pagerAdapter = ClaimViewPagerAdapter(requireActivity(), this)
        viewPager.adapter = pagerAdapter



        binding.backLayout.setOnClickListener {
            previousPage()
        }

        binding.nextLayout.setOnClickListener {
            nextPage()
        }
    }

    override fun nextPage() {
        disableNextLayout()

        val currentItem = viewPager.currentItem
        LogMyBenefits.d("CDU", (currentItem + 1).toString())
        viewPager.setCurrentItem(currentItem + 1, true)

        when (viewPager.currentItem) {
            0 -> {
                binding.stateProgressBar.enableAnimationToCurrentState(true)
                binding.stateProgressBar.animationDuration = 550
                binding.stateProgressBar.setCurrentStateNumber(ProgressView.StateNumber.ONE)
                binding.backLayout.visibility = View.GONE
                binding.nextLayout.visibility = View.VISIBLE
                binding.nextLayoutText.text = "Beneficiary details"
            }

            1 -> {
                binding.stateProgressBar.enableAnimationToCurrentState(true)
                binding.stateProgressBar.animationDuration = 550
                binding.stateProgressBar.setCurrentStateNumber(ProgressView.StateNumber.TWO)
                binding.backLayout.visibility = View.VISIBLE
                binding.nextLayoutText.text = "Upload Documents"
                binding.nextLayout.visibility = View.VISIBLE
            }

            2 -> {
                binding.stateProgressBar.enableAnimationToCurrentState(true)
                binding.stateProgressBar.animationDuration = 550
                binding.stateProgressBar.setCurrentStateNumber(ProgressView.StateNumber.THREE)
                binding.backLayout.visibility = View.VISIBLE
                binding.nextLayoutText.text = "Submit All"
            }

            3 -> {

                binding.stateProgressBar.setAllStatesCompleted(true)
                binding.backLayout.visibility = View.VISIBLE
                binding.nextLayoutText.visibility = View.VISIBLE
                binding.bottomNavLayout.visibility = View.GONE
            }


        }


    }

    override fun previousPage() {


        val currentItem = viewPager.currentItem
        LogMyBenefits.d("CDU", (currentItem - 1).toString())
        viewPager.setCurrentItem(currentItem - 1, true)

        when (viewPager.currentItem) {
            0 -> {
                binding.stateProgressBar.enableAnimationToCurrentState(false)
                binding.stateProgressBar.setCurrentStateNumber(ProgressView.StateNumber.ONE)
                binding.backLayout.visibility = View.GONE
                binding.nextLayout.visibility = View.VISIBLE
                binding.nextLayoutText.text = "Beneficiary details"
            }

            1 -> {
                binding.stateProgressBar.enableAnimationToCurrentState(false)
                binding.stateProgressBar.setCurrentStateNumber(ProgressView.StateNumber.TWO)
                binding.backLayout.visibility = View.VISIBLE
                binding.nextLayoutText.text = "Upload Documents"
                binding.nextLayout.visibility = View.VISIBLE
                AddBeneficiaryFragmentCDU
            }

            2 -> {
                binding.stateProgressBar.enableAnimationToCurrentState(false)
                binding.stateProgressBar.setCurrentStateNumber(ProgressView.StateNumber.THREE)
                binding.backLayout.visibility = View.VISIBLE
                binding.nextLayoutText.text = "Submit All"
            }

        }
    }

    override fun disableNextLayout() {
        /* val disableColor = ColorStateList.valueOf(-0x322c23)
         binding.nextLayoutText.setTextColor(disableColor)
         binding.nextLayout.setCardBackgroundColor(disableColor)
         binding.nextLayout.isClickable = false*/
        try {
            val cardBackgroundColor = ContextCompat.getColor(requireContext(), R.color.stroke_color)
            val textColor = ContextCompat.getColor(requireContext(), R.color.background)
            binding.nextLayout.setCardBackgroundColor(cardBackgroundColor)
            binding.nextLayoutText.setTextColor(textColor)
            binding.nextLayoutImage.imageTintList = ColorStateList.valueOf(textColor)
            binding.nextLayout.isEnabled = false
        }catch (ex:Exception){
            ex.printStackTrace()
        }

    }

    override fun enableNextLayout() {
        /*    val disableColor = ColorStateList.valueOf(-0xffff01)
            binding.nextLayoutText.setTextColor(disableColor)
            binding.nextLayout.setCardBackgroundColor(disableColor)
            binding.nextLayout.isClickable = true*/
        try {
            val cardBackgroundColor = ContextCompat.getColor(requireContext(), R.color.gradient_start)
            val textColor = parentFragment?.let { ContextCompat.getColor(it.requireContext(), R.color.white) }
            binding.nextLayout.setCardBackgroundColor(cardBackgroundColor)
            if (textColor != null) {
                binding.nextLayoutText.setTextColor(textColor)
            }
            binding.nextLayoutImage.imageTintList = textColor?.let { ColorStateList.valueOf(it) }
            binding.nextLayout.isEnabled = true
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

    }

    override fun onTpaSelect(boolean: Boolean) {
        pagerAdapter?.ontpaSelect(boolean)
        pagerAdapter?.notifyDataSetChanged()
    }


    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeStringArray(descriptionData)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ClaimDataUploadFragmentApp> {
        override fun createFromParcel(parcel: Parcel): ClaimDataUploadFragmentApp {
            return ClaimDataUploadFragmentApp(parcel)
        }

        override fun newArray(size: Int): Array<ClaimDataUploadFragmentApp?> {
            return arrayOfNulls(size)
        }
    }


}