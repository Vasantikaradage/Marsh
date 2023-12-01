package com.marsh.android.MB360.insurance.enrollment.ui.bottomSheets;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.aminography.primecalendar.PrimeCalendar;
import com.aminography.primecalendar.civil.CivilCalendar;
import com.aminography.primedatepicker.picker.PrimeDatePicker;
import com.aminography.primedatepicker.picker.callback.SingleDayPickCallback;
import com.aminography.primedatepicker.picker.theme.LightThemeFactory;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.marsh.android.MB360.databinding.AddDependantBottomSheetBinding;
import com.marsh.android.MB360.insurance.enrollment.interfaces.DependantListenerNew;
import com.marsh.android.MB360.insurance.enrollment.repository.responseclass.Dependent;
import com.marsh.android.MB360.monthpicker.DatePickerTheme;
import com.marsh.android.MB360.utilities.UtilMethods;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddDependantSheetNew extends BottomSheetDialogFragment {

    AddDependantBottomSheetBinding binding;
    View view;
    DependantListenerNew dependantListener;
    Dependent dependant;
    //file launcher callback
    ActivityResultLauncher<Intent> fileLauncherActivity;
    File file;
    int position;
    Boolean EDIT_STATE = false;

    //TODO MAX AGE
    static final int MAX_AGE_SPOUSE = 40;
    static final int MAX_AGE_SON = 25;
    static final int MAX_AGE_DAUGHTER = 30;
    static final int MAX_AGE_TWINS = 25;
    static final int MAX_AGE_PARENTS = 100;


    public AddDependantSheetNew(Dependent dependent, int position, DependantListenerNew dependantListener) {
        this.dependant = dependent;
        this.position = position;
        this.dependantListener = dependantListener;

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = AddDependantBottomSheetBinding.inflate(inflater, container, false);
        view = binding.getRoot();

        //if partner
        //todo get the gender of the partner
        if (dependant.getRelation().equalsIgnoreCase("partner")) {
            binding.rbGenderPartner.setVisibility(View.VISIBLE);
        } else {
            binding.rbGenderPartner.setVisibility(View.GONE);
        }


        //gender handling
        binding.rbMale.setOnCheckedChangeListener((compoundButton, b) -> {

        });

        binding.rbFemale.setOnCheckedChangeListener((compoundButton, b) -> {

        });

        //editing capabilities
        if (EDIT_STATE) {
            //here we can edit the dependant details
            binding.etNAme.setText(dependant.getName());
            binding.etAge.setText(dependant.getAge());
            binding.etDOB.setText(dependant.getDateOfBirth());
            binding.lblRelation.setText(dependant.getRelation());
        }

        //showing the existing data
        binding.lblRelation.setText(dependant.getRelation());

        //name is handled in the xml file.
        binding.etNAme.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (charSequence.toString().isEmpty()) {
                    //to show error directly
                    validateInputs();
                } else {
                    binding.tilName.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //handling the date of birth
        SingleDayPickCallback callback = dateOfBirth -> {
            SimpleDateFormat dateRangeFormat = UtilMethods.DATE_FORMAT;
            binding.etDOB.setText(dateRangeFormat.format(dateOfBirth.getTime()));
            binding.etAge.setText(getAge(dateOfBirth));
        };
        LightThemeFactory themeFactory = DatePickerTheme.Companion.getEnrollmentTheme();

        binding.etDOB.setOnClickListener(v -> {
            binding.tilDOB.setError(null);
            PrimeDatePicker datePicker = PrimeDatePicker.Companion.dialogWith(new CivilCalendar()).pickSingleDay(callback).minPossibleDate(getMaxAge(dependant.getRelation())).maxPossibleDate(new CivilCalendar()).applyTheme(themeFactory).build();
            datePicker.show(requireActivity().getSupportFragmentManager(), "SOME_TAG");
        });

        //to handle the file upload layout
        binding.swisDiffabled.setChecked(false);
        binding.lliddiffabled.setVisibility(View.GONE);

        binding.swisDiffabled.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                //disable file upload
                binding.lliddiffabled.setVisibility(View.VISIBLE);
                dependant.setIsDisabled(true);
            } else {
                binding.lliddiffabled.setVisibility(View.GONE);
            }
        });


        //handle file upload
        binding.btnUploadDoc.setOnClickListener(v -> {

            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            String[] mimeTypes = {"image/*", "application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document",  // .doc & .docx
                    "application/vnd.ms-excel", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",  // .xls & .xlsx
                    "application/pdf"};
            intent.setType("*/*");
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);

            try {
                OpenFileLauncher();
            } catch (android.content.ActivityNotFoundException ex) {
                // Potentially direct the user to the Market with a Dialog
                Toast.makeText(requireContext(), "Please install a File Manager.", Toast.LENGTH_SHORT).show();
            }
        });

        binding.ivdeleteimage.setOnClickListener(v -> {
            if (dependant.getDocument() != null) {
                dependant.setDocument(null);
                binding.tvuploadeddocname.setText("Select file");
            } else {
                //nothing
            }
        });


        //max age handle


        binding.saveBtn.btnAdd.setOnClickListener(v -> {
            //here we check the changes from the input and make
            //the new dependant object and we pass the data back to previous page
            if (validateInputs()) {
                dismiss();
            }
        });

        //cancell
        //dismiss the bottomsheet
        binding.cancelLayout.cancelAction.setOnClickListener(v -> {
            dismiss();
        });

        return view;
    }

    private CivilCalendar getMaxAge(String relationName) {

        CivilCalendar maxAge = new CivilCalendar();
        Calendar now = Calendar.getInstance();

        switch (relationName) {
            case "SPOUSE":
            case "PARTNER":
                now.add(Calendar.YEAR, -MAX_AGE_SPOUSE);
                maxAge.setYear(now.get(Calendar.YEAR));
                break;
            case "SON":
                now.add(Calendar.YEAR, -MAX_AGE_SON);
                maxAge.setYear(now.get(Calendar.YEAR));
                break;
            case "DAUGHTER":
                now.add(Calendar.YEAR, -MAX_AGE_DAUGHTER);
                maxAge.setYear(now.get(Calendar.YEAR));
                break;

            case "MOTHER":
            case "FATHER":
            case "MOTHER-IN-LAW":
            case "FATHER-IN-LAW":
            case "MOTHER IN LAW":
            case "FATHER IN LAW":
                now.add(Calendar.YEAR, -MAX_AGE_PARENTS);
                maxAge.setYear(now.get(Calendar.YEAR));
                break;


        }

        return maxAge;
    }

    private Boolean validateInputs() {

        if (binding.swisDiffabled.isChecked()) {
            if (!binding.etDOB.getText().toString().trim().isEmpty() && !binding.etNAme.getText().toString().trim().isEmpty() && dependant.getDocument() != null) {
                dependant.setDateOfBirth(binding.etDOB.getText().toString().trim());
                dependant.setName(binding.etNAme.getText().toString().trim());
                dependant.setAge(binding.etAge.getText().toString().trim());
                dependant.setIsAdded(true);
                dependant.setCanDelete(true);
                dependant.setCanUpdate(true);
                dependant.setIsDisabled(true);
                dependantListener.onDependantAdded(dependant, position);
                return true;
            } else {
                if (binding.etNAme.getText().toString().trim().isEmpty()) {
                    binding.tilName.setError("Enter the name");
                } else if (binding.etDOB.getText().toString().trim().isEmpty()) {
                    binding.tilDOB.setError("Enter the Date of Birth");
                } else if (dependant.getDocument() == null) {
                    Toast.makeText(requireContext(), "Please Upload the required document!", Toast.LENGTH_SHORT).show();
                }
                return false;
            }

        } else {
            if (!binding.etDOB.getText().toString().trim().isEmpty() && !binding.etNAme.getText().toString().trim().isEmpty()) {
                dependant.setDateOfBirth(binding.etDOB.getText().toString().trim());
                dependant.setName(binding.etNAme.getText().toString().trim());
                dependant.setIsAdded(true);
                dependant.setCanDelete(true);
                dependant.setCanUpdate(true);
                dependant.setIsDisabled(false);
                dependantListener.onDependantAdded(dependant, position);
                return true;
            } else {
                if (binding.etNAme.getText().toString().trim().isEmpty()) {
                    binding.tilName.setError("Enter the name");
                } else if (binding.etDOB.getText().toString().trim().isEmpty()) {
                    binding.tilDOB.setError("Enter the Date of Birth");
                }
                return false;
            }
        }
    }

    private String getAge(PrimeCalendar date_of_birth) {
        Calendar today = Calendar.getInstance();

        int age = today.get(Calendar.YEAR) - date_of_birth.get(Calendar.YEAR);

       /* if (today.get(Calendar.DAY_OF_YEAR) < date_of_birth.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }*/

        Integer ageInt = Integer.valueOf(age);

        dependant.setAge(ageInt.toString());
        return ageInt.toString();
    }

    //file upload function
    public void OpenFileLauncher() {

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        String[] mimeTypes = {"image/*", "application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document",  // .doc & .docx
                "application/vnd.ms-excel", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",  // .xls & .xlsx
                "application/pdf"};
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);

        Intent.createChooser(intent, "Select a File to Upload");
        fileLauncherActivity.launch(intent);
    }


}
