package com.marsh.android.MB360.insurance.enrollment.ui.bottomSheets;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.aminography.primecalendar.PrimeCalendar;
import com.aminography.primecalendar.civil.CivilCalendar;
import com.aminography.primedatepicker.picker.PrimeDatePicker;
import com.aminography.primedatepicker.picker.callback.SingleDayPickCallback;
import com.aminography.primedatepicker.picker.theme.LightThemeFactory;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.marsh.android.MB360.databinding.AddTwinsBottomSheetBinding;
import com.marsh.android.MB360.insurance.enrollment.interfaces.DependantListener;
import com.marsh.android.MB360.insurance.enrollment.repository.EnrollmentViewModel;
import com.marsh.android.MB360.insurance.enrollment.repository.responseclass.DependantHelperModel;
import com.marsh.android.MB360.monthpicker.DatePickerTheme;
import com.marsh.android.MB360.utilities.FileUtil;
import com.marsh.android.MB360.utilities.LogMyBenefits;
import com.marsh.android.MB360.utilities.LogTags;
import com.marsh.android.MB360.utilities.UtilMethods;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddTwinsBottomSheet extends BottomSheetDialogFragment {

    AddTwinsBottomSheetBinding binding;
    View view;
    DependantListener dependantListener;
    DependantHelperModel dependant = new DependantHelperModel("TWINS",
            2, "2", true, "", "", false, null, "", true, true, "","21");
    DependantHelperModel dependant2 = new DependantHelperModel("TWINS",
            2, "2", true, "", "", false, null, "", true, true, "","21");

    //file launcher callback;
    ActivityResultLauncher<Intent> fileLauncherActivity;
    ActivityResultLauncher<Intent> fileLauncherActivity2;
    //for uploading the dependant details.
    File file, file2;
    int position;
    //for twins age
    PrimeCalendar twinsBdate = new CivilCalendar();
    boolean edit;
    DependantHelperModel editTwin1;
    DependantHelperModel editTwin2;
    EnrollmentViewModel enrollmentViewModel;

    static final int MAX_AGE_TWINS = 18;


    public AddTwinsBottomSheet(DependantListener dependantListener, int position) {
        this.dependantListener = dependantListener;
        this.position = position;
    }

    public AddTwinsBottomSheet(DependantListener dependantListener, int position, boolean edit) {
        this.dependantListener = dependantListener;
        this.position = position;
        this.edit = edit;


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = AddTwinsBottomSheetBinding.inflate(inflater, container, false);
        view = binding.getRoot();
        enrollmentViewModel = new ViewModelProvider(requireActivity()).get(EnrollmentViewModel.class);

        //here we get the data from the mutable live data of the twins
        //can delete the twins when we complete the enrollment
        //if we get the existing twins we hve to add it in the mutable data before calling this class

        editTwin1 = enrollmentViewModel.getTwin1().getValue();
        editTwin2 = enrollmentViewModel.getTwin2().getValue();


        //id the bottom sheet is called after the edit mode.
        if (edit) {
            binding.titleTxt.setText("Edit Dependant");
            LogMyBenefits.d("TWINS", "onEditTwin1: " + editTwin1.toString());
            LogMyBenefits.d("TWINS", "onEditTwin2: " + editTwin2.toString());
            binding.etNAme.setText(editTwin1.getName());
            binding.etAge.setText(editTwin1.getAge());
            binding.etDOB.setText(editTwin1.getDateOfBirth());
            binding.rbTwinMale.setChecked(editTwin1.getGender().equalsIgnoreCase("male"));
            binding.rbTwinFemale.setChecked(editTwin1.getGender().equalsIgnoreCase("female"));
            binding.rbTwinMale2.setChecked(editTwin2.getGender().equalsIgnoreCase("male"));
            binding.rbTwinFemale2.setChecked(editTwin2.getGender().equalsIgnoreCase("female"));

            if (editTwin1.isDifferentlyAble()) {
                binding.swisDiffabled1.setChecked(true);
                dependant.setDocument(editTwin1.getDocument());
                binding.tvuploadeddocname1.setText(editTwin1.getDocument().getName());
                binding.lliddiffabled1.setVisibility(View.VISIBLE);
            }

            if (editTwin2.isDifferentlyAble()) {
                binding.swisDiffabled2.setChecked(true);
                dependant2.setDocument(editTwin2.getDocument());
                binding.tvuploadeddocname2.setText(editTwin2.getDocument().getName());
                binding.lliddiffabled2.setVisibility(View.VISIBLE);
            }

            binding.etNAme2.setText(editTwin2.getName());
            binding.etAge2.setText(editTwin2.getAge());
            binding.etDOB2.setText(editTwin2.getDateOfBirth());
        }

        //showing the existing data
        binding.lblRelation.setText(dependant.getRelationName());


        //handling the date of birth
        SingleDayPickCallback callback = dateOfBirth -> {
            SimpleDateFormat dateRangeFormat = UtilMethods.DATE_FORMAT;
            binding.etDOB.setText(dateRangeFormat.format(dateOfBirth.getTime()));
            binding.etDOB2.setText(dateRangeFormat.format(dateOfBirth.getTime()));
            binding.etAge.setText(getAge(dateOfBirth));
            binding.etAge2.setText(getAge(dateOfBirth));
            dependant.setAge(binding.etAge.getText().toString());
            twinsBdate = dateOfBirth.clone();
        };
        LightThemeFactory themeFactory = DatePickerTheme.Companion.getEnrollmentTheme();

        //handling the date of birth
        SingleDayPickCallback callback2 = dateOfBirth -> {
            SimpleDateFormat dateRangeFormat = UtilMethods.DATE_FORMAT;
            binding.etDOB2.setText(dateRangeFormat.format(dateOfBirth.getTime()));
            binding.etAge2.setText(getAge(dateOfBirth));
            dependant2.setAge(getAge(dateOfBirth));
        };

        binding.etDOB.setOnClickListener(v -> {
            binding.tilDOB.setError(null);
            PrimeDatePicker datePicker = PrimeDatePicker.Companion.dialogWith(new CivilCalendar())
                    .pickSingleDay(callback)
                    .initiallyPickedSingleDay(twinsBdate)
                    .minPossibleDate(getMaxAge("TWINS"))
                    .maxPossibleDate(new CivilCalendar())
                    .applyTheme(themeFactory)
                    .build();
            datePicker.show(requireActivity().getSupportFragmentManager(), "SOME_TAG");
        });


        binding.etDOB2.setOnClickListener(v -> {
            binding.tilDOB2.setError(null);
            PrimeDatePicker datePicker = PrimeDatePicker.Companion.dialogWith(new CivilCalendar())
                    .pickSingleDay(callback2)
                    .initiallyPickedSingleDay(twinsBdate)
                    .minPossibleDate(getMaxAge("TWINS"))
                    .maxPossibleDate(new CivilCalendar())
                    .applyTheme(themeFactory)
                    .build();
            datePicker.show(requireActivity().getSupportFragmentManager(), "SOME_TAG");
        });

        //to handle the file upload layout
        binding.swisDiffabled1.setChecked(false);
        binding.lliddiffabled1.setVisibility(View.GONE);

        binding.swisDiffabled2.setChecked(false);
        binding.lliddiffabled2.setVisibility(View.GONE);


        binding.swisDiffabled1.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                //disable file upload
                binding.lliddiffabled1.setVisibility(View.VISIBLE);
                dependant.setIsDifferentlyAble(true);
            } else {
                binding.lliddiffabled1.setVisibility(View.GONE);
            }
        });

        binding.swisDiffabled2.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                //disable file upload
                binding.lliddiffabled2.setVisibility(View.VISIBLE);
                dependant2.setIsDifferentlyAble(true);
            } else {
                binding.lliddiffabled2.setVisibility(View.GONE);
            }
        });

        //handle file upload
        binding.btnUploadDoc1.setOnClickListener(v -> {
            String[] mimeTypes = {"image/*", "application/pdf"};
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);

            try {
                OpenFileLauncher();
            } catch (android.content.ActivityNotFoundException ex) {
                // Potentially direct the user to the Market with a Dialog
                Toast.makeText(requireContext(), "Please install a File Manager.",
                        Toast.LENGTH_SHORT).show();
            }
        });

        //handle file upload
        binding.btnUploadDoc2.setOnClickListener(v -> {
            String[] mimeTypes = {"image/*", "application/pdf"};
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);

            try {
                OpenFileLauncher2();
            } catch (android.content.ActivityNotFoundException ex) {
                // Potentially direct the user to the Market with a Dialog
                Toast.makeText(requireContext(), "Please install a File Manager.",
                        Toast.LENGTH_SHORT).show();
            }
        });

        binding.ivdeleteimage1.setOnClickListener(v -> {
            if (dependant.getDocument() != null) {
                dependant.setDocument(null);
                binding.tvuploadeddocname1.setText("Select file");
            } else {
                //nothing
            }
        });
        binding.ivdeleteimage2.setOnClickListener(v -> {
            if (dependant2.getDocument() != null) {
                dependant2.setDocument(null);
                binding.tvuploadeddocname2.setText("Select file");
            } else {
                //nothing
            }
        });

        //removing errors in UI
        binding.rbTwinFemale.setOnCheckedChangeListener((compoundButton, selected) -> {
            if (selected) {
                binding.genderError1.setVisibility(View.GONE);
                dependant.setGender("FEMALE");
            }
        });
        binding.rbTwinMale.setOnCheckedChangeListener((compoundButton, selected) -> {
            if (selected) {
                binding.genderError1.setVisibility(View.GONE);
                dependant.setGender("MALE");
            }
        });

        binding.rbTwinFemale2.setOnCheckedChangeListener((compoundButton, selected) -> {
            if (selected) {
                binding.genderError2.setVisibility(View.GONE);
                dependant2.setGender("FEMALE");
            }
        });
        binding.rbTwinMale2.setOnCheckedChangeListener((compoundButton, selected) -> {
            if (selected) {
                binding.genderError2.setVisibility(View.GONE);
                dependant2.setGender("MALE");
            }
        });

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
        binding.etNAme2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().isEmpty()) {
                    //to show error directly
                    validateInput2();
                } else {
                    binding.tilName2.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        //save function
        binding.save.btnAdd.setOnClickListener(v -> {
            if (validateInputs() && validateInput2()) {
                dependant.setName(binding.etNAme.getText().toString().trim());
                dependant.setAge(binding.etAge.getText().toString());
                dependant.setGender(binding.rbTwinMale.isChecked() ? "MALE" : "FEMALE");
                dependant.setRelationID(binding.rbTwinMale.isChecked() ? "3" : "4");

                dependant2.setName(binding.etNAme2.getText().toString().trim());
                dependant2.setAge(binding.etAge2.getText().toString());
                dependant2.setGender(binding.rbTwinMale2.isChecked() ? "MALE" : "FEMALE");
                dependant2.setRelationID(binding.rbTwinMale2.isChecked() ? "3" : "4");
                dependant2.setCanDelete(true);
                dependant2.setIsAdded(true);
                dependant2.setCanEdit(true);

                dependantListener.onTwinsAdded(dependant, dependant2, position, edit);
                dismiss();
            }
        });


        //cancel button
        //to dismiss
        binding.cancelLayout.cancelAction.setOnClickListener(v -> {
            dismiss();
        });

        return view;
    }

    private CivilCalendar getMaxAge(String relation) {

        CivilCalendar maxAge = new CivilCalendar();
        Calendar now = Calendar.getInstance();
        now.add(Calendar.YEAR, -MAX_AGE_TWINS);
        maxAge.setYear(now.get(Calendar.YEAR));
        return maxAge;

    }

    private Boolean validateInputs() {
        if (binding.swisDiffabled1.isChecked()) {
            if (!binding.etDOB.getText().toString().trim().isEmpty() && !binding.etNAme.getText().toString().trim().isEmpty() && dependant.getDocument() != null && (binding.rbTwinMale.isChecked() || binding.rbTwinFemale.isChecked())) {
                dependant.setDateOfBirth(binding.etDOB.getText().toString().trim());
                dependant.setName(binding.etNAme.getText().toString().trim());
                dependant.setAge(binding.etAge.getText().toString().trim());

                return true;
            } else {
                if (binding.etNAme.getText().toString().trim().isEmpty()) {
                    binding.tilName.setError("Enter the name");
                } else if (binding.etDOB.getText().toString().trim().isEmpty()) {
                    binding.tilDOB.setError("Enter the Date of Birth");
                } else if (dependant.getDocument() == null) {
                    Toast.makeText(requireContext(), "Please Upload the required document!", Toast.LENGTH_SHORT).show();
                } else if (!binding.rbTwinMale.isChecked() && !binding.rbTwinFemale.isChecked()) {
                    binding.genderError1.setVisibility(View.VISIBLE);
                }
                return false;
            }

        } else {
            if (!binding.etDOB.getText().toString().trim().isEmpty() && !binding.etNAme.getText().toString().trim().isEmpty() && (binding.rbTwinMale.isChecked() || binding.rbTwinFemale.isChecked())) {
                dependant.setDateOfBirth(binding.etDOB.getText().toString().trim());
                dependant.setName(binding.etNAme.getText().toString().trim());
                dependant.setAge(getAge(twinsBdate));

                return true;
            } else {
                if (binding.etNAme.getText().toString().trim().isEmpty()) {
                    binding.tilName.setError("Enter the name");
                } else if (binding.etDOB.getText().toString().trim().isEmpty()) {
                    binding.tilDOB.setError("Enter the Date of Birth");
                } else if (!binding.rbTwinMale.isChecked() && !binding.rbTwinFemale.isChecked()) {
                    binding.genderError1.setVisibility(View.VISIBLE);
                }
                return false;
            }
        }
    }

    private Boolean validateInput2() {
        if (binding.swisDiffabled2.isChecked()) {
            if (!binding.etDOB2.getText().toString().trim().isEmpty() && !binding.etNAme2.getText().toString().trim().isEmpty() && dependant2.getDocument() != null && (binding.rbTwinMale2.isChecked() || binding.rbTwinFemale2.isChecked())) {
                dependant2.setDateOfBirth(binding.etDOB2.getText().toString().trim());
                dependant2.setName(binding.etNAme2.getText().toString().trim());
                dependant2.setAge(binding.etAge2.getText().toString().trim());

                return true;
            } else {
                if (binding.etNAme2.getText().toString().trim().isEmpty()) {
                    binding.tilName2.setError("Enter the name");
                } else if (binding.etDOB2.getText().toString().trim().isEmpty()) {
                    binding.tilDOB2.setError("Enter the Date of Birth");
                } else if (dependant2.getDocument() == null) {
                    Toast.makeText(requireContext(), "Please Upload the required document!", Toast.LENGTH_SHORT).show();
                } else if (!binding.rbTwinMale2.isChecked() && !binding.rbTwinFemale2.isChecked()) {
                    binding.genderError2.setVisibility(View.VISIBLE);
                }
                return false;
            }

        } else {
            if (!binding.etDOB2.getText().toString().trim().isEmpty() && !binding.etNAme2.getText().toString().trim().isEmpty() && (binding.rbTwinMale2.isChecked() || binding.rbTwinFemale2.isChecked())) {
                dependant2.setDateOfBirth(binding.etDOB2.getText().toString().trim());
                dependant2.setName(binding.etNAme2.getText().toString().trim());
                dependant2.setAge(binding.etAge2.getText().toString().trim());

                return true;
            } else {
                if (binding.etNAme2.getText().toString().trim().isEmpty()) {
                    binding.tilName2.setError("Enter the name");
                } else if (binding.etDOB2.getText().toString().trim().isEmpty()) {
                    binding.tilDOB2.setError("Enter the Date of Birth");
                } else if (!binding.rbTwinMale2.isChecked() && !binding.rbTwinFemale2.isChecked()) {
                    binding.genderError2.setVisibility(View.VISIBLE);
                }
                return false;
            }
        }
    }


    //file upload
    //file upload function
    public void OpenFileLauncher() {

        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        Intent.createChooser(intent, "Select a File to Upload");
        fileLauncherActivity.launch(intent);
    }

    //file upload
    //file upload function
    public void OpenFileLauncher2() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        Intent.createChooser(intent, "Select a File to Upload");
        fileLauncherActivity2.launch(intent);
    }

    private String getAge(PrimeCalendar date_of_birth) {
        Calendar today = Calendar.getInstance();

        int age = today.get(Calendar.YEAR) - date_of_birth.get(Calendar.YEAR);

        /*if (today.get(Calendar.DAY_OF_YEAR) < date_of_birth.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }*/

        Integer ageInt = Integer.valueOf(age);

        return ageInt.toString();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        fileLauncherActivity = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {

                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        try {
                            if (data != null) {
                                if (data.getData() != null) {
                                    File file = FileUtil.from(context, data.getData());
                                    //set the file
                                    dependant.setDocument(file);
                                    this.file = file;
                                    binding.tvuploadeddocname1.setText(file.getName());
                                } else {
                                    LogMyBenefits.d(LogTags.ENROLLMENT, "onFilePicked : No file picked");
                                }
                            } else {
                                LogMyBenefits.d(LogTags.ENROLLMENT, "onFilePicked : No file picked");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });

        fileLauncherActivity2 = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {

                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        try {
                            if (data != null) {
                                if (data.getData() != null) {
                                    File file = FileUtil.from(context, data.getData());
                                    //set the file
                                    dependant2.setDocument(file);
                                    this.file2 = file;
                                    binding.tvuploadeddocname2.setText(file.getName());
                                } else {
                                    LogMyBenefits.d(LogTags.ENROLLMENT, "onFilePicked : No file picked");
                                }
                            } else {
                                LogMyBenefits.d(LogTags.ENROLLMENT, "onFilePicked : No file picked");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });
    }
}
