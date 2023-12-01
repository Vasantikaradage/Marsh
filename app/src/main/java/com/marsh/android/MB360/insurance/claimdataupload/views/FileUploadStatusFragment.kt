package com.marsh.android.MB360.insurance.claimdataupload.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.marsh.android.MB360.databinding.FragmentFileUploadStatusBinding
import com.marsh.android.MB360.insurance.claimdataupload.interfaces.ViewPagerNavigation

class FileUploadStatusFragment : Fragment() {

    private var _binding: FragmentFileUploadStatusBinding? = null
    private var viewPagerNavigation: ViewPagerNavigation? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            viewPagerNavigation = it.getSerializable("viewPagerNavigation") as? ViewPagerNavigation
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentFileUploadStatusBinding.inflate(inflater, container, false)

        val view = binding.root

        binding.gotToAllClaimsBtn.setOnClickListener {
            requireActivity().onBackPressed()
        }

        return view
    }

    companion object {
        fun newInstance(viewPagerNavigation: ViewPagerNavigation): FileUploadStatusFragment {
            val fragment = FileUploadStatusFragment()
            val args = Bundle()
            args.putSerializable("viewPagerNavigation", viewPagerNavigation)
            fragment.arguments = args
            return fragment
        }
    }
}