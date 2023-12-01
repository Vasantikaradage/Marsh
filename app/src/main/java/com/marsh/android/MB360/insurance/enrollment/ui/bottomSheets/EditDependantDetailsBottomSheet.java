package com.marsh.android.MB360.insurance.enrollment.ui.bottomSheets;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.aminography.primecalendar.PrimeCalendar;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.marsh.android.MB360.databinding.EditDependantBottomSheetBinding;
import com.marsh.android.MB360.insurance.enrollment.interfaces.DependantListener;
import com.marsh.android.MB360.insurance.enrollment.repository.responseclass.DependantHelperModel;

import java.io.File;

public class EditDependantDetailsBottomSheet extends BottomSheetDialogFragment {
    EditDependantBottomSheetBinding binding;
    View view;
    DependantListener dependantListener;
    DependantHelperModel dependant;
    File file;
    PrimeCalendar birth_date;
    int position;


    public EditDependantDetailsBottomSheet(DependantHelperModel dependant, int position) {
        this.position = position;
        this.dependant = dependant;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = EditDependantBottomSheetBinding.inflate(inflater, container, false);
        view = binding.getRoot();

        //todo
        // set the date and make it functional from prime-calendar

        //todo change the name

        //todo change the differently able certificate


        //todo save the edit with the changes and update the list

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }
}
