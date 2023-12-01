package com.marsh.android.MB360.insurance.claimdataupload

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.marsh.android.MB360.databinding.FragmentAllClaimsBinding
import com.marsh.android.MB360.insurance.claimdataupload.interfaces.ViewPagerNavigation


class AllClaimsFragment : Fragment() {

    private var _binding: FragmentAllClaimsBinding? = null
    private var viewPagerNavigation: ViewPagerNavigation? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        _binding = FragmentAllClaimsBinding.inflate(inflater, container, false)

        val view = binding.root

        return view
    }

    companion object
}