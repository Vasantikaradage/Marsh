package com.marsh.android.MB360.insurance.enrollment.ui;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.marsh.android.MB360.insurance.enrollment.interfaces.ViewPagerNavigationMenuHelper;

public class EnrollmentViewPagerAdapter extends FragmentStateAdapter {
    ViewPagerNavigationMenuHelper viewPagerNavigationMenuHelper;

    EnrollmentWelcomeFragment enrollmentWelcomeFragment;
    InstructionFragment instructionFragment;
    MyCoveragesFragment myCoveragesFragment;
    EmployeeDetailsFragment employeeDetailsFragment;
    DependantDetailsFragment dependantDetailsFragment;
    ParentalDetailsFragment parentalDetailsFragment;
    TopUpFragment topUpFragment_GMC;
    TopUpFragment topUpFragment_GPA;
    TopUpFragment topUpFragment_GTL;
    EnrollmentSummaryFragment enrollmentSummaryFragment;

    public EnrollmentViewPagerAdapter(FragmentActivity activity) {
        super(activity);

    }

    public EnrollmentViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, ViewPagerNavigationMenuHelper viewPagerNavigationMenuHelper) {
        super(fragmentManager, lifecycle);
        this.viewPagerNavigationMenuHelper = viewPagerNavigationMenuHelper;

        enrollmentWelcomeFragment = new EnrollmentWelcomeFragment(viewPagerNavigationMenuHelper);
        instructionFragment = new InstructionFragment(viewPagerNavigationMenuHelper);
        myCoveragesFragment = new MyCoveragesFragment(viewPagerNavigationMenuHelper);
        employeeDetailsFragment = new EmployeeDetailsFragment(viewPagerNavigationMenuHelper);
        dependantDetailsFragment = new DependantDetailsFragment(viewPagerNavigationMenuHelper);
        parentalDetailsFragment = new ParentalDetailsFragment(viewPagerNavigationMenuHelper);
        topUpFragment_GMC = new TopUpFragment(viewPagerNavigationMenuHelper, "GMC");
        topUpFragment_GPA = new TopUpFragment(viewPagerNavigationMenuHelper, "GPA");
        topUpFragment_GTL = new TopUpFragment(viewPagerNavigationMenuHelper, "GTL");
        enrollmentSummaryFragment = new EnrollmentSummaryFragment(viewPagerNavigationMenuHelper);

    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return enrollmentWelcomeFragment;
            case 1:
                return instructionFragment;
            case 2:
                return myCoveragesFragment;
            case 3:
                return employeeDetailsFragment;
            case 4:
                return dependantDetailsFragment;
            case 5:
                return parentalDetailsFragment;
            case 6:
                return topUpFragment_GMC;
            case 7:
                return topUpFragment_GPA;
            case 8:
                return topUpFragment_GTL;
            case 9:
                return enrollmentSummaryFragment;
            default:
                //error due to the viewpager position
                return null;
        }

    }

    @Override
    public int getItemCount() {
        return 10;
    }
}
