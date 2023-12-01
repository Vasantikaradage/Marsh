package com.marsh.android.MB360.insurance.queries.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.marsh.android.MB360.R;
import com.marsh.android.MB360.databinding.FragmentNewQueryBinding;
import com.marsh.android.MB360.insurance.queries.repository.QueryViewModel;
import com.marsh.android.MB360.insurance.repository.LoadSessionViewModel;
import com.marsh.android.MB360.utilities.FileUtil;
import com.marsh.android.MB360.utilities.LogMyBenefits;
import com.marsh.android.MB360.utilities.LogTags;
import com.marsh.android.MB360.utilities.MediaTypes;
import com.marsh.android.MB360.utilities.UtilMethods;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class NewQueryFragment extends Fragment {

    FragmentNewQueryBinding binding;
    View view;
    QueryViewModel queryViewModel;
    LoadSessionViewModel loadSessionViewModel;
    String category = "";
    String empSrNo = "";


    private final String[] filesExt = {"xlsx", "pdf", "png", "xls", "doc", "docx", "jpeg", "jpg"};
    public String[] PathHolder = new String[5];
    //max files
    private int i = 0;

    private LinearLayout[] fileview;
    private AppCompatTextView[] dots;
    private AppCompatImageView[] fileType;
    private AppCompatButton[] delete;

    ActivityResultLauncher<Intent> fileLauncherActivity;


    public NewQueryFragment() {
        // Required empty public constructor
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
        binding = FragmentNewQueryBinding.inflate(inflater, container, false);
        view = binding.getRoot();

        queryViewModel = new ViewModelProvider(requireActivity()).get(QueryViewModel.class);
        loadSessionViewModel = new ViewModelProvider(requireActivity()).get(LoadSessionViewModel.class);

        loadSessionViewModel.getLoadSessionData().observe(getViewLifecycleOwner(), response -> {
            empSrNo = response.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData()
                    .get(0).getEmployeeSrNo();

        });
        //setUpCategorySpinner
        setUpSpinner();

        binding.categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                category = adapterView.getSelectedItem().toString();
                if (category.equalsIgnoreCase("other")) {
                    binding.etCategoryLayout.setVisibility(View.VISIBLE);
                } else {
                    binding.etCategory.setText("");
                    binding.etCategoryLayout.setVisibility(View.GONE);
                }

                binding.errorCategoryLabel.setVisibility(View.GONE);
                binding.errorQueryLabel.setVisibility(View.GONE);
                binding.errorSelect.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                adapterView.setSelection(0);
            }
        });

        binding.etMSGQry.setOnFocusChangeListener((view, b) -> {
            if (b) {
                binding.rvMSGQry.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.query_border_active));
            } else {
                binding.rvMSGQry.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.queryborder));
            }
        });

        binding.etMSGQry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.queryCounter.setText("Typed " + s.length() + " / 3000 characters");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        binding.btnSubmitQuery.setOnClickListener(v -> {
            binding.errorCategoryLabel.setVisibility(View.GONE);
            binding.errorQueryLabel.setVisibility(View.GONE);
            binding.errorSelect.setVisibility(View.GONE);

            if (category.equalsIgnoreCase("other")) {
                if (binding.etCategory.getText().toString().trim().isEmpty()) {
                    binding.errorCategoryLabel.setVisibility(View.VISIBLE);
                    binding.errorQueryLabel.setVisibility(View.GONE);
                    binding.errorSelect.setVisibility(View.GONE);
                } else if (binding.etMSGQry.getText().toString().trim().isEmpty()) {
                    binding.errorCategoryLabel.setVisibility(View.GONE);
                    binding.errorQueryLabel.setVisibility(View.VISIBLE);
                    binding.errorSelect.setVisibility(View.GONE);
                } else {
                    try {
                        submitNewQuery();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } else if (category.equalsIgnoreCase("select")) {
                binding.errorCategoryLabel.setVisibility(View.GONE);
                binding.errorQueryLabel.setVisibility(View.GONE);
                binding.errorSelect.setVisibility(View.VISIBLE);
            } else if (binding.etMSGQry.getText().toString().trim().isEmpty()) {
                binding.errorCategoryLabel.setVisibility(View.GONE);
                binding.errorQueryLabel.setVisibility(View.VISIBLE);
                binding.errorSelect.setVisibility(View.GONE);
            } else {
                try {
                    submitNewQuery();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


        binding.uploadFile.setOnClickListener(view -> {
            if (i < 5) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);

                try {
                    OpenFileLauncher();
                } catch (android.content.ActivityNotFoundException ex) {
                    // Potentially direct the user to the Market with a Dialog
                    Toast.makeText(requireContext(), "Please install a File Manager.",
                            Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(requireContext(), "Cannot upload more than 5 files.", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    public void OpenFileLauncher() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        Intent.createChooser(intent, "Select a File to Upload");
        fileLauncherActivity.launch(intent);
    }

    private void submitNewQuery() throws JSONException {
        binding.errorCategoryLabel.setVisibility(View.GONE);
        binding.errorQueryLabel.setVisibility(View.GONE);
        binding.errorSelect.setVisibility(View.GONE);


        //json object for replying a query
        //here isReply is true because we are letting user to reply the query
        JSONObject queryRequestJson = new JSONObject();
        queryRequestJson.put("empSrNo", empSrNo);
        queryRequestJson.put("query", binding.etMSGQry.getText().toString().trim());

        //multipart form data
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);

        if (i == 0) {
            builder.addFormDataPart("QueryData", queryRequestJson.toString());

        } else {
            for (int k = 0; k < i; k++) {

                File file = new File(PathHolder[k]);

                final MediaType MEDIA_TYPE = MediaTypes.fromFile(file);

                if (file.exists())
                    builder.addFormDataPart(file.getName(), FileUtil.getFileName(requireContext(), Uri.fromFile(file)),
                            RequestBody.create(MEDIA_TYPE, file));
            }

            builder.addFormDataPart("QueryData", queryRequestJson.toString());

        }

        RequestBody requestBody = builder.build();
        LogMyBenefits.d(LogTags.QUERY_ACTIVITY, "replyQuery: " + requestBody);
        LogMyBenefits.d(LogTags.QUERY_ACTIVITY, "replyQuery: " + queryRequestJson);
        queryViewModel.addQuery(requestBody).observe(getViewLifecycleOwner(), reply -> {
            if (reply != null) {
                if (reply.getStatus()) {
                    //need to clear the edittext and list
                    binding.etMSGQry.setText(null);
                    binding.attachmentLayout.removeAllViews();
                    PathHolder = new String[5];
                    i = 0;


                    Toast.makeText(requireContext(), "Query submitted successfully", Toast.LENGTH_SHORT).show();
                    requireActivity().onBackPressed();
                }
            } else {
                //something went wrong
                //already showed toast in repo class
            }
        });
    }

    private void setUpSpinner() {
        //adding the category
        List<String> categoryList = new ArrayList<>();
        categoryList.add("Select");
        categoryList.add("E-cards");
        categoryList.add("Dependant Addiction/Correction/Deletion");
        categoryList.add("Policy features and Coverages Details");
        categoryList.add("Enrollment Process");
        categoryList.add("How to make a cashless claim");
        categoryList.add("How to make a reimbursement claim");
        categoryList.add("Claim Intimation");
        categoryList.add("Hospital related");
        categoryList.add("Contact Details");
        categoryList.add("Claim Tracking");
        categoryList.add("Other");

        NewQueryCategorySpinnerAdapter adapter = new NewQueryCategorySpinnerAdapter(requireContext(), categoryList);
        binding.categorySpinner.setAdapter(adapter);

    }

    public void UpdateList() {
        try {
            fileview = new LinearLayout[i + 1];

            dots = new AppCompatTextView[i + 1];
            fileType = new AppCompatImageView[i + 1];
            delete = new AppCompatButton[i + 1];

            binding.attachmentLayout.removeAllViews();

            int pixels1 = (int) (15 * getResources().getDisplayMetrics().density + 0.5f);
            int pixels2 = (int) (15 * getResources().getDisplayMetrics().density + 0.5f);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
            LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(
                    pixels1, pixels2);

            layoutParams2.setMargins(4, 10, 4, 10);
            layoutParams.setMargins(4, 10, 4, 10);

            for (int j = 0; j < dots.length; j++) {

                int o = j;
                dots[j] = new AppCompatTextView(requireContext());
                delete[j] = new AppCompatButton(requireContext());
                fileType[j] = new AppCompatImageView(requireContext());
                fileview[j] = new LinearLayout(requireContext());
                delete[j].setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.close));
                fileview[j].setOrientation(LinearLayout.HORIZONTAL);
                if (dots.length > 1)
                    binding.attachmentLayout.setWeightSum((float) dots.length);
                else
                    fileview[j].setWeightSum(2.0f);

                fileview[j].setGravity(Gravity.CENTER_VERTICAL);
                dots[j].setTextSize(10.0f);
                int maxLength = 10;
                InputFilter[] fArray = new InputFilter[1];
                fArray[0] = new InputFilter.LengthFilter(maxLength);
                dots[j].setFilters(fArray);

                String[] s = PathHolder[j].split("/");
                dots[j].setText(s[s.length - 1]);
                dots[j].setPadding(10, 0, 10, 0);
                String[] filenameArray = PathHolder[j].split("\\.");
                String extension = filenameArray[filenameArray.length - 1];

                switch (extension.toLowerCase()) {
                    case "pdf":
                        fileType[j].setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_by_pdf));
                        break;
                    case "xlsx":
                    case "xls":
                        fileType[j].setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_by_xlsx));
                        break;
                    case "docx":
                    case "doc":
                        fileType[j].setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_by_doc));
                        break;
                    case "png":
                    case "jpg":
                    case "jpeg":
                        fileType[j].setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_by_png));
                        break;
                }
                fileview[j].setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.ripple_effect_file));
                fileview[j].addView(fileType[j], layoutParams2);
                fileview[j].addView(dots[j], layoutParams);
                fileview[j].addView(delete[j], layoutParams2);
                binding.attachmentLayout.addView(fileview[j], layoutParams);
                dots[j].setLayoutParams(layoutParams);

                delete[j].setOnClickListener(view -> {
                    try {

                        String[] PathHoler = new String[5];
                        int fileCouunt = 0;
                        for (int l = 0; l < i; l++) {
                            if (PathHolder[l] != PathHolder[o]) {
                                PathHoler[fileCouunt] = PathHolder[l];
                                fileCouunt++;
                            }
                        }

                        i = i - 1;
                        PathHolder = new String[5];
                        PathHolder = PathHoler;

                        UpdateList();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                });
            }
            i++;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        fileLauncherActivity = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        try {
                            File file2 = FileUtil.from(context, data.getData());
                            // File file2 = new File(getPath(data.getData()));

                            String filePath = file2.getPath();
                            boolean FileExists = false;

                            String[] filenameArray = filePath.split("\\.");
                            String extension = filenameArray[filenameArray.length - 1];
                            if (Arrays.asList(filesExt).contains(extension.replace(".", "").toLowerCase())) {
                                for (int fileCheck = 0; fileCheck < i; fileCheck++) {
                                    FileExists = PathHolder[fileCheck].equals(filePath);
                                }

                                if (FileExists)
                                    Toast.makeText(context, "File Already Attached", Toast.LENGTH_SHORT).show();
                                else {

                                    PathHolder[i] = filePath;

                                    UpdateList();

                                }
                            } else
                                Toast.makeText(context,
                                        "Entered file format should be one of the following : xls, xlsx, doc, docx, png, jpeg, jpg, pdf", Toast.LENGTH_SHORT).show();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        int index = NewQueryFragmentArgs.fromBundle(getArguments()).getSpinnerItemIndex();
        binding.categorySpinner.setSelection(index);

        queryViewModel.getReloginState().observe(getActivity(), relogin -> {
            if (relogin) {
                UtilMethods.RedirectToLogin(requireActivity());
            } else {
            }
        });
    }
}