package com.marsh.android.MB360.insurance.enrollment.ui.bottomSheets;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
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
import com.marsh.android.MB360.databinding.AddDependantBottomSheetBinding;
import com.marsh.android.MB360.insurance.enrollment.interfaces.DependantListener;
import com.marsh.android.MB360.insurance.enrollment.repository.EnrollmentViewModel;
import com.marsh.android.MB360.insurance.enrollment.repository.responseclass.DependantHelperModel;
import com.marsh.android.MB360.insurance.repository.LoadSessionViewModel;
import com.marsh.android.MB360.insurance.repository.responseclass.LoadSessionResponse;
import com.marsh.android.MB360.monthpicker.DatePickerTheme;
import com.marsh.android.MB360.utilities.FileUtil;
import com.marsh.android.MB360.utilities.LogMyBenefits;
import com.marsh.android.MB360.utilities.LogTags;
import com.marsh.android.MB360.utilities.UtilMethods;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddDependantBottomSheet extends BottomSheetDialogFragment {
    AddDependantBottomSheetBinding binding;
    View view;
    DependantListener dependantListener;
    DependantHelperModel dependant;
    //file launcher callback
    ActivityResultLauncher<Intent> fileLauncherActivity;
    File file;
    int position;
    Boolean EDIT_STATE;
    EnrollmentViewModel enrollmentViewModel;
    LoadSessionViewModel loadSessionViewModel;

    //employee gender
    String EMP_GENDER = "MALE";

    String GENDER = "MALE";


    //TODO get data for max age and set here.
    static final int MAX_AGE_SPOUSE = 50;
    static final int MAX_AGE_SON = 25;
    static final int MAX_AGE_DAUGHTER = 30;
    static final int MAX_AGE_TWINS = 25;
    static final int MAX_AGE_PARENTS = 100;

    //TODO get data for min age and set here.
    static final int MIN_AGE_SPOUSE = 18;
    static final int MIN_AGE_SON = 18;
    static final int MIN_AGE_DAUGHTER = 18;
    static final int MIN_AGE_TWINS = 18;
    static final int MIN_AGE_PARENTS = 18;


    public AddDependantBottomSheet(DependantListener dependantListener, DependantHelperModel dependant, int position) {
        this.dependantListener = dependantListener;
        this.dependant = dependant;
        this.position = position;
        this.EDIT_STATE = false;
    }

    public AddDependantBottomSheet(DependantListener dependantListener, DependantHelperModel dependant, int position, Boolean edit) {
        this.dependantListener = dependantListener;
        this.dependant = dependant;
        this.position = position;
        this.EDIT_STATE = edit;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        loadSessionViewModel = new ViewModelProvider(requireActivity()).get(LoadSessionViewModel.class);

        LoadSessionResponse loadSessionResponse = loadSessionViewModel.getLoadSessionData().getValue();


        if (loadSessionResponse != null) {
            try {
                EMP_GENDER = loadSessionResponse.getGroupPoliciesEmployeesDependants().get(0).getGroupGMCPolicyEmpDependantsData().get(0).getGender();

                if (EMP_GENDER.equalsIgnoreCase("") || EMP_GENDER.equalsIgnoreCase("NOT AVAILABLE")) {
                    EMP_GENDER = "MALE";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        binding = AddDependantBottomSheetBinding.inflate(inflater, container, false);
        view = binding.getRoot();


        if (EDIT_STATE){

            binding.titleTxt.setText("Update dependant");
            binding.btnEdit.setText("save");
            binding.saveBtn.btnAdd.setText("save");
        }

        //if partner
        //todo get the gender of the partner
        if (dependant.getRelationName().equalsIgnoreCase("partner")) {
            binding.rbGenderPartner.setVisibility(View.VISIBLE);
        } else {
            binding.rbGenderPartner.setVisibility(View.GONE);
        }


        //gender handling
        binding.rbMale.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                GENDER = "MALE";
            }
        });

        binding.rbFemale.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                GENDER = "FEMALE";
            }
        });

        //editing capabilities
        if (EDIT_STATE) {
            //here we can edit the dependant details
            binding.etNAme.setText(dependant.getName());
            binding.etAge.setText(dependant.getAge());
            binding.etDOB.setText(dependant.getDateOfBirth());
            binding.lblRelation.setText(dependant.getRelationName());
        }

        //showing the existing data
        binding.lblRelation.setText(dependant.getRelationName());

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
            CivilCalendar maxAge = new CivilCalendar();
            maxAge.set(2018, 0, 12);
            PrimeDatePicker datePicker = PrimeDatePicker.Companion.dialogWith(getMinAge(dependant.getRelationName()))
                    .pickSingleDay(callback)
                    .minPossibleDate(getMaxAge(dependant.getRelationName()))
                    .maxPossibleDate(getMinAge(dependant.getRelationName()))
                    .applyTheme(themeFactory)
                    .build();
            datePicker.show(requireActivity().getSupportFragmentManager(), "SOME_TAG");
        });

        //to handle the file upload layout
        binding.swisDiffabled.setChecked(false);
        binding.lliddiffabled.setVisibility(View.GONE);

        binding.swisDiffabled.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                //disable file upload
                binding.lliddiffabled.setVisibility(View.VISIBLE);
                dependant.setIsDifferentlyAble(true);
            } else {
                binding.lliddiffabled.setVisibility(View.GONE);
            }
        });


        //handle file upload
        binding.btnUploadDoc.setOnClickListener(v -> {
            String[] mimeTypes = {"image/*", "application/pdf"};
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);

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


        switch (relationName.toUpperCase()) {
            case "SPOUSE":
            case "PARTNER":
                maxAge.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR) - MAX_AGE_SPOUSE);
                break;
            case "SON":
                maxAge.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR) - MAX_AGE_SON);
                break;
            case "DAUGHTER":
                maxAge.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR) - MAX_AGE_DAUGHTER);
                break;

            case "MOTHER":
            case "FATHER":
            case "MOTHER-IN-LAW":
            case "FATHER-IN-LAW":
            case "MOTHER IN LAW":
            case "FATHER IN LAW":
                maxAge.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR) - MAX_AGE_PARENTS);
                break;


        }
        LogMyBenefits.d("DATE", "getMaxAge: " + maxAge.getTime());

        return maxAge;
    }

    private CivilCalendar getMinAge(String relationName) {


        CivilCalendar minAge = new CivilCalendar();
        Calendar now = Calendar.getInstance();
        Calendar min_now = Calendar.getInstance();


        switch (relationName.toUpperCase()) {
            case "SPOUSE":
            case "PARTNER":
                minAge.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR) - MIN_AGE_SPOUSE);
                break;
            case "SON":
                minAge.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR) - MIN_AGE_SON);
                break;
            case "DAUGHTER":
                minAge.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR) - MIN_AGE_DAUGHTER);
                break;

            case "MOTHER":
            case "FATHER":
            case "MOTHER-IN-LAW":
            case "FATHER-IN-LAW":
            case "MOTHER IN LAW":
            case "FATHER IN LAW":
                minAge.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR) - MIN_AGE_PARENTS);
                break;


        }

        return minAge;
    }

    private Boolean validateInputs() {

        //gender logic
        switch (dependant.getRelationName().toLowerCase()) {
            case "spouse":
                if (EMP_GENDER.equalsIgnoreCase("MALE")) {
                    dependant.setGender("FEMALE");
                } else {
                    dependant.setGender("MALE");
                }
                break;
            case "partner":
                dependant.setGender(GENDER);
                break;
            case "son":
                dependant.setGender("MALE");
                break;
            case "daughter":
                dependant.setGender("FEMALE");
                break;

        }

        if (binding.swisDiffabled.isChecked()) {
            if (!binding.etDOB.getText().toString().trim().isEmpty() && !binding.etNAme.getText().toString().trim().isEmpty() && dependant.getDocument() != null) {
                dependant.setDateOfBirth(binding.etDOB.getText().toString().trim());
                dependant.setName(binding.etNAme.getText().toString().trim());
                dependant.setAge(binding.etAge.getText().toString().trim());
                dependant.setIsAdded(true);
                dependant.setCanDelete(true);
                dependant.setCanEdit(true);
                if (EDIT_STATE) {
                    dependantListener.onDependantEditedListener(dependant, position);
                } else {
                    dependantListener.onDependantSavedListener(dependant, position);
                }
                return true;
            } else {
                if (binding.etNAme.getText().toString().trim().isEmpty()) {
                    binding.tilName.setError("Enter the name");
                } else if (binding.etDOB.getText().toString().trim().isEmpty()) {
                    binding.tilDOB.setError("Enter the date of birth");
                } else if (dependant.getDocument() == null) {
                    Toast.makeText(requireContext(), "Please upload the required document!", Toast.LENGTH_SHORT).show();
                }
                return false;
            }

        } else {
            if (!binding.etDOB.getText().toString().trim().isEmpty() && !binding.etNAme.getText().toString().trim().isEmpty()) {
                dependant.setDateOfBirth(binding.etDOB.getText().toString().trim());
                dependant.setName(binding.etNAme.getText().toString().trim());
                dependant.setIsAdded(true);
                dependant.setCanDelete(true);
                dependant.setCanEdit(true);
                if (EDIT_STATE) {
                    dependantListener.onDependantEditedListener(dependant, position);
                } else {
                    dependantListener.onDependantSavedListener(dependant, position);
                }
                return true;
            } else {
                if (binding.etNAme.getText().toString().trim().isEmpty()) {
                    binding.tilName.setError("Enter the name");
                } else if (binding.etDOB.getText().toString().trim().isEmpty()) {
                    binding.tilDOB.setError("Enter the date of birth");
                }
                return false;
            }
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        fileLauncherActivity = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {

            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                try {
                    if (data != null) {
                        if (data.getData() != null) {
                            File file = FileUtil.from(context, data.getData());
                            //set the file
                            dependant.setDocument(file);
                            this.file = file;
                            binding.tvuploadeddocname.setText(file.getName());
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

    //file upload function
    public void OpenFileLauncher() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        Intent.createChooser(intent, "Select a File to Upload");
        fileLauncherActivity.launch(intent);
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);
    }
}
