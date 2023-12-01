package com.marsh.android.MB360.insurance.enrollment.ui;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.marsh.android.MB360.databinding.FragmentEmployeeDetailsBinding;
import com.marsh.android.MB360.insurance.enrollment.adapters.EmployeeDetailsAdapter;
import com.marsh.android.MB360.insurance.enrollment.interfaces.ViewPagerNavigationMenuHelper;
import com.marsh.android.MB360.insurance.enrollment.repository.EnrollmentViewModel;
import com.marsh.android.MB360.insurance.enrollment.repository.responseclass.WindowPeriodEnrollmentResponse;
import com.marsh.android.MB360.utilities.LogMyBenefits;
import com.marsh.android.MB360.utilities.LogTags;
import com.marsh.android.MB360.utilities.WindowPeriodCounter;

import java.text.ParseException;


public class EmployeeDetailsFragment extends Fragment {

    FragmentEmployeeDetailsBinding binding;
    View view;
    EnrollmentViewModel enrollmentViewModel;
    EmployeeDetailsAdapter adapter;
    ViewPagerNavigationMenuHelper viewPagerNavigationMenuHelper;
    boolean isWindowPeriodActive = false;


    public EmployeeDetailsFragment() {
        // Required empty public constructor
    }

    public EmployeeDetailsFragment(ViewPagerNavigationMenuHelper viewPagerNavigationMenuHelper) {
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
        binding = FragmentEmployeeDetailsBinding.inflate(inflater, container, false);
        view = binding.getRoot();
        enrollmentViewModel = new ViewModelProvider(requireActivity()).get(EnrollmentViewModel.class);


        getEmployeeDetails();
        getWindowPeriodTimer();


        return view;

    }

    private void getWindowPeriodTimer() {
        WindowPeriodEnrollmentResponse windowPeriod = enrollmentViewModel.getWindowPeriod().getValue();
        if (windowPeriod != null) {
            WindowPeriodCounter windowPeriodCounter = new WindowPeriodCounter(windowPeriod.getWindowPeriod().getWindowEndDateGmc(), requireContext(), requireActivity());
            try {
                CountDownTimer timer = windowPeriodCounter.getTimer(binding.timerEmployeeDetails);
                if (timer != null) {
                    timer.start();
                } else {
                    binding.timerEmployeeDetails.setText("Window period has expired");
                }
            } catch (ParseException e) {
                //unable to parse date
                Toast.makeText(requireContext(), "Unable to retrieve window period date.", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

        } else {
            //no window period available
        }
    }

    private void getEmployeeDetails() {

        WindowPeriodEnrollmentResponse windowPeriod = enrollmentViewModel.getWindowPeriod().getValue();
        if (windowPeriod != null) {
            enrollmentViewModel.getEmployee();
            WindowPeriodCounter windowPeriodCounter = new WindowPeriodCounter(windowPeriod.getWindowPeriod().getWindowEndDateGmc(), requireContext(), requireActivity());
            try {
                CountDownTimer timer = windowPeriodCounter.countDownTimer(false);
                isWindowPeriodActive = timer != null;
            } catch (ParseException e) {
                e.printStackTrace();
            }

            enrollmentViewModel.getEmployeeData().observe(getViewLifecycleOwner(), employeeResponse -> {
                if (employeeResponse != null) {
                    adapter = new EmployeeDetailsAdapter(requireActivity(), employeeResponse.getEmployeeDetails(), windowPeriod, isWindowPeriodActive);

                    GridLayoutManager layoutManager = new GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false);
                    layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                        @Override
                        public int getSpanSize(int position) {

                            return (employeeResponse.getEmployeeDetails().get(position).getFieldName().length() < 12 ? 1 : 2);//span condition here
                        }
                    });
                    binding.employeeDetailsCycle.setLayoutManager(layoutManager);
                    binding.employeeDetailsCycle.setAdapter(adapter);

                } else {
                    LogMyBenefits.e(LogTags.ENROLLMENT, "getEmployeeDetails: Null");
                }

                binding.loadingBar.setVisibility(View.GONE);
            });
        } else {
            //here we get window period as null
            //we must let user not interact with anything..
            //error state
            Toast.makeText(requireContext(), "Failed to get window period.", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    @Override
    public void onResume() {
        super.onResume();
       /* viewPagerNavigationMenuHelper.hideSummaryOption();
        viewPagerNavigationMenuHelper.showHomeButton();*/
    }
}