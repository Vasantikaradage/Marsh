package com.marsh.android.MB360.insurance.enrollment.ui;

import android.animation.Animator;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import com.marsh.android.MB360.R;
import com.marsh.android.MB360.databinding.FragmentEnrollmentSummaryBinding;
import com.marsh.android.MB360.insurance.enrollment.adapters.EnrolmentSummaryAdapter;
import com.marsh.android.MB360.insurance.enrollment.interfaces.ViewPagerNavigationMenuHelper;
import com.marsh.android.MB360.insurance.enrollment.repository.EnrollmentViewModel;
import com.marsh.android.MB360.insurance.enrollment.repository.responseclass.EnrollmentSummary;
import com.marsh.android.MB360.insurance.enrollment.repository.responseclass.SummaryItem;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;


public class EnrollmentSummaryFragment extends Fragment {

    FragmentEnrollmentSummaryBinding binding;
    View view;
    ViewPagerNavigationMenuHelper viewPagerNavigationMenuHelper;
    EnrollmentViewModel enrollmentViewModel;
    List<SummaryItem> summaryList = new ArrayList<>();
    EnrolmentSummaryAdapter adapter;
    NavController navController;

    public EnrollmentSummaryFragment() {
        //required empty constructor
    }

    public EnrollmentSummaryFragment(ViewPagerNavigationMenuHelper viewPagerNavigationMenuHelper) {

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
        binding = FragmentEnrollmentSummaryBinding.inflate(inflater, container, false);
        view = binding.getRoot();
        enrollmentViewModel = new ViewModelProvider(requireActivity()).get(EnrollmentViewModel.class);


        //To Navigate
        NavHostFragment navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();

        binding.prevPage2.setOnClickListener(v -> {
            viewPagerNavigationMenuHelper.previousPage();
        });

        enrollmentViewModel.getLoading().observe(getViewLifecycleOwner(), loading -> {
            if (loading) {

                binding.llprogressbar.setVisibility(View.VISIBLE);

            } else {
                binding.llprogressbar.setVisibility(View.GONE);

            }
        });


        //enrollment Complete
        binding.confirmButton.setOnClickListener(v -> {
               playAnimation();
        });
        return view;
    }


    private void getSummary() {
        enrollmentViewModel.getSummary();
        enrollmentViewModel.getSummaryData().observe(getViewLifecycleOwner(), enrollmentSummaryResponse -> {
            if (enrollmentSummaryResponse != null) {
                //here we get the summary
                //we need to build the summary here.
                EnrollmentSummary summary = enrollmentSummaryResponse.getSummary().getData().get(0);

                //Group Sum insured
                summaryList.add(0, new SummaryItem(0, "Group Sum Insured", getString(R.string.summary_rs), true, false));
                if (summary.getGmcBaseSi() != null && !summary.getGmcBaseSi().equalsIgnoreCase("")) {
                    summaryList.add(new SummaryItem(1, "Health Insurance", summary.getGmcBaseSi(), false, false));
                }
                if (summary.getGmcBaseSiParentSet1() != null && !summary.getGmcBaseSiParentSet1().equalsIgnoreCase("")) {
                    summaryList.add(new SummaryItem(1, "Parents (1st Set)", summary.getGmcBaseSiParentSet1(), false, false));
                }
                if (summary.getGmcBaseSiParentSet2() != null && !summary.getGmcBaseSiParentSet2().equalsIgnoreCase("")) {
                    summaryList.add(new SummaryItem(1, "Parents (2nd Set)", summary.getGmcBaseSiParentSet2(), false, false));
                }
                if (summary.getGmcTopupSi() != null && !summary.getGmcTopupSi().equalsIgnoreCase("")) {
                    summaryList.add(new SummaryItem(1, "Health Insurance Top-up", summary.getGmcTopupSi(), false, false));
                }
                summaryList.add(new SummaryItem(2, "footer", "footer", false, true));

                if (summary.getGpaBaseSi() != null && !summary.getGpaBaseSi().equalsIgnoreCase("")) {
                    summaryList.add(new SummaryItem(1, "Personal Accident", summary.getGpaBaseSi(), false, false));
                }
                if (summary.getGpaTopupSi() != null && !summary.getGpaTopupSi().equalsIgnoreCase("")) {
                    summaryList.add(new SummaryItem(1, "Personal Accident Top-up", summary.getGpaTopupSi(), false, false));
                }
                summaryList.add(new SummaryItem(2, "footer", "footer", false, true));

                if (summary.getGtlBaseSi() != null && !summary.getGtlBaseSi().equalsIgnoreCase("")) {
                    summaryList.add(new SummaryItem(1, "Term Life", summary.getGtlBaseSi(), false, false));
                }
                if (summary.getGtlTopupSi() != null && !summary.getGtlTopupSi().equalsIgnoreCase("")) {
                    summaryList.add(new SummaryItem(1, "Term Life Top-up", summary.getGtlTopupSi(), false, false));
                }
                summaryList.add(new SummaryItem(2, "footer", "footer", false, true));

                //Health insurance Parent Premium
                summaryList.add(new SummaryItem(0, "Health Insurance Parent Premium", getString(R.string.summary_rs), true, false));

                if (summary.getParent1Premium() != null && !summary.getParent1Premium().equalsIgnoreCase("")) {
                    summaryList.add(new SummaryItem(1, "Parents (1st Set)", summary.getParent1Premium(), false, false));
                }
                if (summary.getParent2Premium() != null && !summary.getParent2Premium().equalsIgnoreCase("")) {
                    summaryList.add(new SummaryItem(1, "Parents (2nd Set)", summary.getParent2Premium(), false, false));
                }
                summaryList.add(new SummaryItem(2, "footer", "footer", false, true));

                //Group TOP up
                summaryList.add(new SummaryItem(0, "Group Top-up Premium", getString(R.string.summary_rs), true, false));
                if (summary.getGmcTopupPremium() != null && !summary.getGmcTopupPremium().equalsIgnoreCase("")) {
                    summaryList.add(new SummaryItem(1, "Health Insurance", summary.getGmcTopupPremium(), false, false));
                }
                if (summary.getGpaTopupPremium() != null && !summary.getGpaTopupPremium().equalsIgnoreCase("")) {
                    summaryList.add(new SummaryItem(1, "Personal Accident", summary.getGpaTopupPremium(), false, false));
                }
                if (summary.getGtlTopupPremium() != null && !summary.getGtlTopupPremium().equalsIgnoreCase("")) {
                    summaryList.add(new SummaryItem(1, "Term Life", summary.getGtlTopupPremium(), false, false));
                }
                summaryList.add(new SummaryItem(2, "footer", "footer", false, true));


                //you pay
                binding.tvValue.setText(summary.getTotalPremium());

                binding.installPremium.setText(MessageFormat.format(getString(R.string.prem_inst), summary.getTotalPremium(), summary.getNoOfInstallments()));


                List<SummaryItem> toRemoveArray = new ArrayList<>();


                for (int i = 0; i < summaryList.size(); i++) {
                    if (summaryList.get(i).getId() == 0) {
                        if (summaryList.get(i + 1).getId() == 2) {
                            //remove the both item
                            toRemoveArray.add(summaryList.get(i));
                            toRemoveArray.add(summaryList.get(i + 1));
                        }
                    }
                }

                if (!toRemoveArray.isEmpty()) {
                    for (SummaryItem position : toRemoveArray) {
                        summaryList.remove(position);
                    }
                }

            } else {
                //something is wrong
            }


            adapter = new EnrolmentSummaryAdapter(summaryList, requireContext());
            binding.rvSummaryView.setAdapter(adapter);

        });
    }

    private void playAnimation() {
        binding.lottieLayerName.enableMergePathsForKitKatAndAbove(true);
        binding.lottieLayerName2.enableMergePathsForKitKatAndAbove(true);
        binding.scroll1.smoothScrollTo(0, 0);
        binding.lottieLayerName.setSpeed(0.75f);
        binding.lottieLayerName2.setSpeed(0.75f);
        binding.lottieLayerName2.playAnimation();

        binding.lottieLayerName2.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                binding.lottieLayerName.setVisibility(View.VISIBLE);
                binding.lottieLayerName.playAnimation();

                new Handler().postDelayed(() -> {
                    binding.lottieLayerName.setVisibility(View.GONE);
                }, 750);

                new Handler().postDelayed(() -> {
                    NavDirections action = EnrollmentSummaryFragmentDirections.actionEnrollmentSummaryFragmentToHomeFragment();
                    navController.navigateUp();
                }, 1500);


            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getSummary();

    }

    @Override
    public void onResume() {
        super.onResume();

    }
}