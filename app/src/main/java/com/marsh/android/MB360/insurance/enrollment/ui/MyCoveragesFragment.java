package com.marsh.android.MB360.insurance.enrollment.ui;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.marsh.android.MB360.R;
import com.marsh.android.MB360.databinding.FragmentMyCoveragesBinding;
import com.marsh.android.MB360.insurance.enrollment.adapters.EnrollmentCoveragesAdapter;
import com.marsh.android.MB360.insurance.enrollment.interfaces.ToolTipListener;
import com.marsh.android.MB360.insurance.enrollment.interfaces.ViewPagerNavigationMenuHelper;
import com.marsh.android.MB360.insurance.enrollment.repository.EnrollmentViewModel;
import com.marsh.android.MB360.insurance.enrollment.repository.responseclass.WindowPeriodEnrollmentResponse;
import com.marsh.android.MB360.utilities.WindowPeriodCounter;
import com.skydoves.balloon.ArrowPositionRules;
import com.skydoves.balloon.Balloon;
import com.skydoves.balloon.BalloonAnimation;
import com.skydoves.balloon.BalloonSizeSpec;

import java.text.ParseException;
import java.util.Arrays;
import java.util.List;


public class MyCoveragesFragment extends Fragment implements ToolTipListener {

    FragmentMyCoveragesBinding binding;
    View view;
    EnrollmentViewModel enrollmentViewModel;
    EnrollmentCoveragesAdapter adapter;
    ViewPagerNavigationMenuHelper viewPagerNavigationMenuHelper;
    boolean isWindowPeriodActive = false;
    boolean animations = true;


    public MyCoveragesFragment() {
        // Required empty public constructor
    }

    public MyCoveragesFragment(ViewPagerNavigationMenuHelper viewPagerNavigationMenuHelper) {
        this.viewPagerNavigationMenuHelper = viewPagerNavigationMenuHelper;
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
        binding = FragmentMyCoveragesBinding.inflate(inflater, container, false);
        view = binding.getRoot();

        enrollmentViewModel = new ViewModelProvider(requireActivity()).get(EnrollmentViewModel.class);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //to hide summary option
        viewPagerNavigationMenuHelper.hideSummaryOption();


    }


    private void getCoverages() {
        enrollmentViewModel.getCoverages();

        enrollmentViewModel.getCoveragesData().observe(getViewLifecycleOwner(), coverages -> {
            if (coverages != null) {
                WindowPeriodEnrollmentResponse windowPeriod = enrollmentViewModel.getWindowPeriod().getValue();

                if (windowPeriod != null) {
                    String gmc_date = "";
                    String gpa_date = "";
                    String gtl_date = "";
                    gmc_date = windowPeriod.getWindowPeriod().getWindowEndDateGmc();
                    gpa_date = windowPeriod.getWindowPeriod().getWindowEndDateGpa();
                    gtl_date = windowPeriod.getWindowPeriod().getWindowEndDateGtl();

                    List<String> dates = Arrays.asList(gmc_date, gpa_date, gtl_date);

                    // to check if the dates are same
                    if (dates.stream().distinct().count() == 1) {
                        //here we have to show the single timer
                        adapter = new EnrollmentCoveragesAdapter(requireContext(), requireActivity(), coverages.getCoverages(), this, enrollmentViewModel.getWindowPeriod().getValue(), false, animations);
                        binding.coveragesCycle.setAdapter(adapter);
                        animations = false;

                        //show the common timer
                        binding.timerCoverageDetails.setVisibility(View.VISIBLE);
                        getWindowPeriodTimer();

                    } else {
                        //here we have to show respected timer
                        adapter = new EnrollmentCoveragesAdapter(requireContext(), requireActivity(), coverages.getCoverages(), this, enrollmentViewModel.getWindowPeriod().getValue(), true, animations);
                        binding.coveragesCycle.setAdapter(adapter);
                        animations = false;

                        //hide the common timer
                        binding.timerCoverageDetails.setVisibility(View.GONE);
                    }

                }
            }
        });
    }

    private void getWindowPeriodTimer() {
        WindowPeriodEnrollmentResponse windowPeriod = enrollmentViewModel.getWindowPeriod().getValue();
        if (windowPeriod != null) {
            WindowPeriodCounter windowPeriodCounter = new WindowPeriodCounter(windowPeriod.getWindowPeriod().getWindowEndDateGmc(), requireContext(), requireActivity());
            try {
                CountDownTimer timer = windowPeriodCounter.getTimer(binding.timerCoverageDetails);
                if (timer != null) {
                    isWindowPeriodActive = true;
                    timer.start();
                } else {
                    isWindowPeriodActive = false;
                    binding.timerCoverageDetails.setText("Window period has expired");
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

    @Override
    public void OnToolTipListener(String text, View view, ViewGroup rootView) {
        Typeface typeface = getResources().getFont(R.font.poppinssemibold);

        Balloon balloon = new Balloon.Builder(requireContext()).setWidth(BalloonSizeSpec.WRAP).setHeight(BalloonSizeSpec.WRAP).setText(text).setTextTypeface(typeface).setTextColorResource(R.color.white).setTextSize(15f).setTextGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT).setArrowPositionRules(ArrowPositionRules.ALIGN_ANCHOR).setArrowSize(10).setArrowPosition(0.5f).setPadding(8).setCornerRadius(8f).setBackgroundColorResource(R.color.black).setLifecycleOwner(getViewLifecycleOwner()).setBalloonAnimation(BalloonAnimation.FADE).build();
        balloon.showAlignTop(view);
    }

    @Override
    public void onWindowPeriodClickListener(String endDate) {
        WindowPeriodCounter counter = new WindowPeriodCounter(endDate, requireContext(), requireActivity());
        try {
            CountDownTimer timer = counter.countDownTimer(true);
            if (timer != null) {
                timer.start();
            } else {
                //window period is ended
                Toast.makeText(requireContext(), "Window period has expired", Toast.LENGTH_SHORT).show();
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
       /* viewPagerNavigationMenuHelper.hideSummaryOption();
        viewPagerNavigationMenuHelper.showHomeButton();*/

        if (animations) {
            Animation animation = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_in_down);
            binding.imgTitle.startAnimation(animation);
            binding.subtitle1.startAnimation(animation);
            binding.subtitle2.startAnimation(animation);
            binding.imgTitle.setVisibility(View.VISIBLE);
            binding.subtitle1.setVisibility(View.VISIBLE);
            binding.subtitle2.setVisibility(View.VISIBLE);

        }else{
            binding.imgTitle.setVisibility(View.VISIBLE);
            binding.subtitle1.setVisibility(View.VISIBLE);
            binding.subtitle2.setVisibility(View.VISIBLE);
        }

        getCoverages();
    }
}