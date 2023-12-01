package com.marsh.android.MB360.insurance.enrollment.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.marsh.android.MB360.databinding.FragmentDependantDetailsNewBinding;
import com.marsh.android.MB360.insurance.enrollment.adapters.DependantDetailsAdapterNew;
import com.marsh.android.MB360.insurance.enrollment.interfaces.DependantListenerNew;
import com.marsh.android.MB360.insurance.enrollment.interfaces.ViewPagerNavigationMenuHelper;
import com.marsh.android.MB360.insurance.enrollment.repository.EnrollmentViewModel;
import com.marsh.android.MB360.insurance.enrollment.repository.responseclass.Dependent;
import com.marsh.android.MB360.insurance.enrollment.ui.bottomSheets.AddDependantSheetNew;

public class DependantDetailsNew extends Fragment implements DependantListenerNew {

    FragmentDependantDetailsNewBinding binding;
    View view;
    EnrollmentViewModel enrollmentViewModel;
    DependantDetailsAdapterNew adapter;


    public DependantDetailsNew() {
        // Required empty public constructor
    }

    public DependantDetailsNew(ViewPagerNavigationMenuHelper viewPagerNavigationMenuHelper) {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enrollmentViewModel = new ViewModelProvider(requireActivity()).get(EnrollmentViewModel.class);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDependantDetailsNewBinding.inflate(inflater, container, false);
        view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //get Dependant Details
        getDependantDetails();
    }

    private void getDependantDetails() {
        enrollmentViewModel.getDependantDetailsNew().observe(getViewLifecycleOwner(), dependantDetailsResponseNew -> {
            adapter = new DependantDetailsAdapterNew(dependantDetailsResponseNew.getDependent(), requireContext(), this);
            binding.dependantCycle.setAdapter(adapter);
        });
    }

    @Override
    public void openDependantBottomSheet(Dependent dependent, int position, DependantListenerNew dependantListener) {
        AddDependantSheetNew addDependantSheetNew = new AddDependantSheetNew(dependent, position, dependantListener);
        addDependantSheetNew.setCancelable(true);
        addDependantSheetNew.show(getChildFragmentManager(), addDependantSheetNew.getTag());
    }

    @Override
    public void onDependantAdded(Dependent dependent, int position) {
        //adding the dependant
        Toast.makeText(requireContext(), dependent.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDependantRemoved() {

    }

    @Override
    public void onDependantUpdated() {

    }
}