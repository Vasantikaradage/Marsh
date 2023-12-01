package com.marsh.android.MB360.insurance.enrollment.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.fragment.app.Fragment;

import com.marsh.android.MB360.R;
import com.marsh.android.MB360.databinding.FragmentEnrollmentWelcomeBinding;
import com.marsh.android.MB360.insurance.enrollment.interfaces.ViewPagerNavigationMenuHelper;
import com.marsh.android.MB360.insurance.enrollment.repository.EnrollmentViewModel;

/**
 * this is the fragment for
 * Enrollment home page.
 **/

public class EnrollmentWelcomeFragment extends Fragment {

    //here we initiate the first page in enrollment


    //view-binding for enrollment welcome.
    FragmentEnrollmentWelcomeBinding binding;

    //view-model for enrollment
    EnrollmentViewModel enrollmentViewModel;

    //enrollment navigation
    ViewPagerNavigationMenuHelper viewPagerNavigationMenuHelper;

    //view for the fragment to be returned
    View view;


    public EnrollmentWelcomeFragment(ViewPagerNavigationMenuHelper viewPagerNavigationMenuHelper) {
        // Required empty public constructor
        this.viewPagerNavigationMenuHelper = viewPagerNavigationMenuHelper;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentEnrollmentWelcomeBinding.inflate(inflater, container, false);
        view = binding.getRoot();

        enrollmentGiftAnimation();

        //this is to proceed
        binding.next.setOnClickListener(v -> {
            viewPagerNavigationMenuHelper.nextPage();
        });

        return view;
    }

    /**
     * @see {enrollmentGiftAnimation }
     * for the animation of the oboarding of the user in enrollment home page
     **/
    private void enrollmentGiftAnimation() {
        //animation starts here
        Animation giftAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_down);
        Animation userNameAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_from_left);
        Animation letsBeginAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_from_right);
        binding.introImg.startAnimation(giftAnimation);
        binding.next.startAnimation(letsBeginAnimation);
        binding.textHome.startAnimation(userNameAnimation);

        giftAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                binding.textHome.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        userNameAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                binding.next.setVisibility(View.VISIBLE);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }
}