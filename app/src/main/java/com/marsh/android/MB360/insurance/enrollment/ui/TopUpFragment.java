package com.marsh.android.MB360.insurance.enrollment.ui;


import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.marsh.android.MB360.R;
import com.marsh.android.MB360.databinding.FragmentGmcTopUpBinding;
import com.marsh.android.MB360.insurance.enrollment.adapters.TopUpsAdapter;
import com.marsh.android.MB360.insurance.enrollment.interfaces.TopUpSelected;
import com.marsh.android.MB360.insurance.enrollment.interfaces.ViewPagerNavigationMenuHelper;
import com.marsh.android.MB360.insurance.enrollment.repository.EnrollmentViewModel;
import com.marsh.android.MB360.insurance.enrollment.repository.responseclass.TopSumInsuredsValue;
import com.marsh.android.MB360.insurance.enrollment.repository.responseclass.WindowPeriodEnrollmentResponse;
import com.marsh.android.MB360.utilities.TopUpItemTouchHelper;
import com.marsh.android.MB360.utilities.WindowPeriodCounter;

import java.text.MessageFormat;
import java.text.ParseException;
import java.util.List;


public class TopUpFragment extends Fragment implements TopUpSelected, TopUpItemTouchHelper.RecyclerItemTouchHelperListener {

    FragmentGmcTopUpBinding binding;
    View view;
    EnrollmentViewModel enrollmentViewModel;
    TopUpsAdapter adapter;
    ViewPagerNavigationMenuHelper viewPagerNavigationMenuHelper;
    List<TopSumInsuredsValue> topUpList;
    String page = "";
    boolean isWindowPeriodActive = false;

    int animation_count = 0;
    boolean animations = true;

    public TopUpFragment() {
        // Required empty public constructor
    }

    public TopUpFragment(ViewPagerNavigationMenuHelper viewPagerNavigationMenuHelper, String page) {
        // Required empty public constructor
        this.viewPagerNavigationMenuHelper = viewPagerNavigationMenuHelper;
        this.page = page;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentGmcTopUpBinding.inflate(getLayoutInflater(), container, false);
        view = binding.getRoot();
        enrollmentViewModel = new ViewModelProvider(requireActivity()).get(EnrollmentViewModel.class);

        binding.lblInst.setText(MessageFormat.format(getString(R.string.lblTopupQuery), page));

        getWindowPeriodTimer(page);


        return view;
    }

    private void getWindowPeriodTimer(String page) {
        WindowPeriodEnrollmentResponse windowPeriod = enrollmentViewModel.getWindowPeriod().getValue();

        if (windowPeriod != null) {

            String windowPeriodEndDate = "";
            if (page.equalsIgnoreCase("gmc")) {
                windowPeriodEndDate = windowPeriod.getWindowPeriod().getWindowEndDateGmc();
            } else if (page.equalsIgnoreCase("gpa")) {
                windowPeriodEndDate = windowPeriod.getWindowPeriod().getWindowEndDateGpa();
            } else if (page.equalsIgnoreCase("gtl")) {
                windowPeriodEndDate = windowPeriod.getWindowPeriod().getWindowEndDateGtl();

            }

            WindowPeriodCounter windowPeriodCounter = new WindowPeriodCounter(windowPeriodEndDate, requireContext(), requireActivity());
            try {
                CountDownTimer timer = windowPeriodCounter.getTimer(binding.timerTopUpDetails);
                if (timer != null) {
                    isWindowPeriodActive = true;
                    timer.start();
                } else {
                    isWindowPeriodActive = false;
                    binding.timerTopUpDetails.setText("Window period has expired");
                }
            } catch (ParseException e) {
                //unable to parse date
                isWindowPeriodActive = false;
                Toast.makeText(requireContext(), "Unable to retrieve window period date.", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

        } else {
            //no window period available
        }
    }

    private void getTopUP(String type_of_policy) {
        WindowPeriodEnrollmentResponse windowPeriod = enrollmentViewModel.getWindowPeriod().getValue();
        if (windowPeriod != null) {

            String windowPeriodEndDate = "";
            if (page.equalsIgnoreCase("gmc")) {
                windowPeriodEndDate = windowPeriod.getWindowPeriod().getWindowEndDateGmc();
            } else if (page.equalsIgnoreCase("gpa")) {
                windowPeriodEndDate = windowPeriod.getWindowPeriod().getWindowEndDateGpa();
            } else if (page.equalsIgnoreCase("gtl")) {
                windowPeriodEndDate = windowPeriod.getWindowPeriod().getWindowEndDateGtl();

            }

            WindowPeriodCounter windowPeriodCounter = new WindowPeriodCounter(windowPeriodEndDate, requireContext(), requireActivity());
            try {
                CountDownTimer timer = windowPeriodCounter.countDownTimer(false);
                isWindowPeriodActive = timer != null;
            } catch (ParseException e) {
                isWindowPeriodActive = false;
                Toast.makeText(requireContext(), "Unable to parse window period date.", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            enrollmentViewModel.getTopups();
            enrollmentViewModel.getTopUpsData().observe(getViewLifecycleOwner(), topUpData -> {
                if (topUpData != null) {
                    switch (type_of_policy) {
                        case "GMC":
                            topUpList = topUpData.getSumInsuredData().getEnrollTopupOptions().getTopupSumInsuredClsData().getGMCTopupOptionsData().get(0).getTopSumInsuredsValues();
                            adapter = new TopUpsAdapter(requireContext(), topUpList, this, isWindowPeriodActive, animations);
                            binding.spnTopUpamt.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            if (isWindowPeriodActive) {
                                ItemTouchHelper.SimpleCallback itemTouchHelperCallbackGMC = new TopUpItemTouchHelper(0, ItemTouchHelper.LEFT, this, topUpList);
                                new ItemTouchHelper(itemTouchHelperCallbackGMC).attachToRecyclerView(binding.spnTopUpamt);
                            } else {
                                //do nothing

                            }
                            break;
                        case "GPA":
                            topUpList = topUpData.getSumInsuredData().getEnrollTopupOptions().getTopupSumInsuredClsData().getGPATopupOptionsData().get(0).getTopSumInsuredsValues();
                            adapter = new TopUpsAdapter(requireContext(), topUpList, this, isWindowPeriodActive, animations);
                            binding.spnTopUpamt.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            if (isWindowPeriodActive) {
                                ItemTouchHelper.SimpleCallback itemTouchHelperCallbackGPA = new TopUpItemTouchHelper(0, ItemTouchHelper.LEFT, this, topUpList);
                                new ItemTouchHelper(itemTouchHelperCallbackGPA).attachToRecyclerView(binding.spnTopUpamt);

                            } else {
                                //do nothing
                            }
                            break;
                        case "GTL":
                            topUpList = topUpData.getSumInsuredData().getEnrollTopupOptions().getTopupSumInsuredClsData().getGTLTopupOptionsData().get(0).getTopSumInsuredsValues();
                            adapter = new TopUpsAdapter(requireContext(), topUpList, this, isWindowPeriodActive, animations);
                            binding.spnTopUpamt.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            if (isWindowPeriodActive) {
                                ItemTouchHelper.SimpleCallback itemTouchHelperCallbackGTL = new TopUpItemTouchHelper(0, ItemTouchHelper.LEFT, this, topUpList);
                                new ItemTouchHelper(itemTouchHelperCallbackGTL).attachToRecyclerView(binding.spnTopUpamt);

                            } else {
                                //do nothing
                            }
                            break;
                    }

                }
            });
        } else {
            Toast.makeText(requireContext(), "Unable to retrieve window period", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void OnTopUpSelected(TopSumInsuredsValue topUp, int position) {
        //here we mock the data that user selected the
        for (TopSumInsuredsValue topUpValue : topUpList) {
            if (!topUpValue.equals(topUp)) {
                topUpValue.setOpted("NO");
                adapter.notifyItemChanged(topUpList.indexOf(topUpValue));
            }
        }

        topUpList.get(position).setOpted("YES");
        adapter.notifyItemChanged(position);

    }

    @Override
    public void onResume() {
        super.onResume();
        if (animation_count == 0) {
            animations = true;

            Animation animation = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_in_down);
            binding.titleIcon.startAnimation(animation);
            binding.lblTopupType.startAnimation(animation);
            binding.timerHolder.startAnimation(animation);
            binding.lblInst.startAnimation(animation);

            binding.titleIcon.setVisibility(View.VISIBLE);
            binding.lblTopupType.setVisibility(View.VISIBLE);
            binding.timerHolder.setVisibility(View.VISIBLE);
            binding.lblInst.setVisibility(View.VISIBLE);

        } else {
            animations = false;

            binding.titleIcon.setVisibility(View.VISIBLE);
            binding.lblTopupType.setVisibility(View.VISIBLE);
            binding.timerHolder.setVisibility(View.VISIBLE);
            binding.lblInst.setVisibility(View.VISIBLE);
        }
        switch (page) {
            case "GMC":
                binding.titleIcon.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_gmctopup));
                binding.lblTopupType.setText("GMC Top-Up");
                //get the topupsdata
                getTopUP(page);
                break;
            case "GPA":
                binding.titleIcon.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_gpa_sheild));
                binding.lblTopupType.setText("GPA Top-Up");
                //get the topupsdata
                getTopUP(page);
                break;
            case "GTL":
                binding.titleIcon.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_gtl_sheild));
                binding.lblTopupType.setText("GTL Top-Up");
                //get the topupsdata
                getTopUP(page);
                break;


        }
        animations = false;
        animation_count++;


    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        topUpList.get(position).setOpted("NO");
        adapter.notifyItemChanged(position);

    }
}