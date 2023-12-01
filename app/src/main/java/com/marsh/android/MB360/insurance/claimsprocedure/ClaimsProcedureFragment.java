package com.marsh.android.MB360.insurance.claimsprocedure;

import static com.marsh.android.MB360.BuildConfig.DOWNLOAD_BASE_URL;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerDrawable;
import com.google.android.material.tabs.TabLayout;
import com.marsh.android.MB360.R;
import com.marsh.android.MB360.databinding.FragmentClaimsProcedureBinding;
import com.marsh.android.MB360.insurance.claimsprocedure.adapters.ClaimProcedureHelplineNumberAdapters;
import com.marsh.android.MB360.insurance.claimsprocedure.repository.ClaimProcedureViewModel;
import com.marsh.android.MB360.insurance.claimsprocedure.repository.responseclass.ClaimProcLayoutInfo;
import com.marsh.android.MB360.insurance.dialogues.PolicyChangeDialogue;
import com.marsh.android.MB360.insurance.repository.LoadSessionViewModel;
import com.marsh.android.MB360.insurance.repository.responseclass.GroupProduct;
import com.marsh.android.MB360.insurance.repository.responseclass.LoadSessionResponse;
import com.marsh.android.MB360.insurance.repository.selectedPolicyRepo.SelectedPolicyViewModel;
import com.marsh.android.MB360.insurance.repository.selectedPolicyRepo.responseclass.GroupPolicyData;
import com.marsh.android.MB360.utilities.LogMyBenefits;
import com.marsh.android.MB360.utilities.LogTags;
import com.marsh.android.MB360.utilities.UtilMethods;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class ClaimsProcedureFragment extends Fragment {


    FragmentClaimsProcedureBinding binding;
    View view;

    //viewModels
    LoadSessionViewModel loadSessionViewModel;
    SelectedPolicyViewModel selectedPolicyViewModel;
    ClaimProcedureViewModel claimProcedureViewModel;
    String groupChildSrNo = "";
    String PROCEDURE_TYPE = "CASHLESS";
    NavController navController;

    private final String css_classes = "<head><style>@font-face { font-family:MyFont; src: url('file:///android_res/font/noto_sans_regular.ttf');} .main-container{ text-align: justify; font-size:13px; font-family:MyFont; color:#696969;} ul { padding-left: 0px; }ul li{ color:#696969; line-height: 1;font-size : 13px; font-family:MyFont; text-align: justify; margin: 0px 0px 10px; }.main-container .text-center {text-align: center;}.main-container ul { padding-left:20px; }span.clearfix { color:#696969; font-size:13px; font-size:13px; font-family:MyFont;}.h1 {font-size: 24px;}.main-container h2.sbold {font-size: larger;}.sbold { font-weight: 400!important; }.main-container h2,.text-primary,.text-info { color: #0096d6; }.h1, .h2, .h3, h1, h2, h3 {margin-top: 20px;margin-bottom: 10px; } </style></head>";


    String css_class = "<style>\n" + ".list-inline, .list-unstyled {\n" + "    padding-left: 0;\n" + "    list-style: none;\n" + "}\n" + ".margin-bottom-10 {\n" + "    margin-bottom: 10px!important;\n" + "}\n" + ".col-xs-2 {\n" + "    width: 16.66667%;\n" + "}\n" + ".col-xs-10 {\n" + "    width: 83.33333%;\n" + "}\n" + ".text-right {\n" + "    text-align: center;\n" + "}\n" + ".btn-group>.btn-group, .btn-toolbar .btn, .btn-toolbar .btn-group, .btn-toolbar .input-group, .col-xs-1, .col-xs-10, .col-xs-11, .col-xs-12, .col-xs-2, .col-xs-3, .col-xs-4, .col-xs-5, .col-xs-6, .col-xs-7, .col-xs-8, .col-xs-9, .dropdown-menu {\n" + "    float: left;\n" + "}\n" + ".badge-success {\n" + "    background-color: #0070d5;\n" + "}\n" + ".badge {\n" + "    font-size: 11px!important;\n" + "    font-weight: 300;\n" + "    height: 18px;\n" + "    color: #fff;\n" + "    padding: 3px 6px;\n" + "    -webkit-border-radius: 12px!important;\n" + "    -moz-border-radius: 12px!important;\n" + "    border-radius: 12px!important;\n" + "    text-shadow: none!important;\n" + "    text-align: center;\n" + "    vertical-align: middle;\n" + "}\n" + ".badge {\n" + "    display: inline-block;\n" + "    min-width: 10px;\n" + "    \n" + "}\n" + ".note.note-info {\n" + "    background-color: #f5f8fd;\n" + "    border-color: #1e88ea;\n" + "    color: #010407;\n" + "}" + "</style>";
    String oeGrpBasInfSrNo = "";
    String product_code = "";
    String tpa_code = "";

    ClaimProcedureHelplineNumberAdapters adapter;

    TabLayout.OnTabSelectedListener tabSelectedListener;
    SpinnerAdapter spinnerAdapter;


    int selectedIndex;
    List<GroupPolicyData> policyData = new ArrayList<>();

    public ClaimsProcedureFragment() {
        // Required empty public constructor
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
        binding = FragmentClaimsProcedureBinding.inflate(inflater, container, false);
        view = binding.getRoot();
        binding.noStepsFound.inflate();
        binding.additionalNoStepsFound.inflate();

        return view;
    }

    private void setuptabs() {

        LoadSessionResponse loadSessionResponse = loadSessionViewModel.getLoadSessionData().getValue();
        if (loadSessionResponse != null) {
            if (!loadSessionResponse.getGroupProducts().isEmpty()) {
                for (GroupProduct groupProduct : loadSessionResponse.getGroupProducts()) {

                    switch (groupProduct.getProductCode().toLowerCase()) {
                        case "gmc":
                            if (!groupProduct.getActive().equalsIgnoreCase("1")) {

                                binding.tabs.viewPagerTabs.getTabAt(0).view.setEnabled(false);
                                binding.tabs.viewPagerTabs.getTabAt(0).setTabLabelVisibility(TabLayout.TAB_LABEL_VISIBILITY_UNLABELED);
                            }
                            break;
                        case "gpa":
                            if (!groupProduct.getActive().equalsIgnoreCase("1")) {

                                binding.tabs.viewPagerTabs.getTabAt(1).view.setEnabled(false);
                                binding.tabs.viewPagerTabs.getTabAt(1).setTabLabelVisibility(TabLayout.TAB_LABEL_VISIBILITY_UNLABELED);
                            }
                            break;
                        case "gtl":
                            if (!groupProduct.getActive().equalsIgnoreCase("1")) {

                                binding.tabs.viewPagerTabs.getTabAt(2).view.setEnabled(false);
                                binding.tabs.viewPagerTabs.getTabAt(2).setTabLabelVisibility(TabLayout.TAB_LABEL_VISIBILITY_UNLABELED);
                            }
                            break;
                    }

                }
            }

            groupChildSrNo = loadSessionResponse.getGroupInfoData().getGroupchildsrno();


            //get claim procedure Image
            //claimProcedureViewModel.getClaimProcedureImage(groupChildSrNo, oeGrpBasInfSrNo, product_code, PROCEDURE_TYPE);

        } else {
            //something went wrong
        }

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (isAdded()) {
            initView();
            getClaimProcedure();
        }
    }


    public void initView() {
        loadSessionViewModel = new ViewModelProvider(requireActivity()).get(LoadSessionViewModel.class);
        selectedPolicyViewModel = new ViewModelProvider(requireActivity()).get(SelectedPolicyViewModel.class);
        claimProcedureViewModel = new ViewModelProvider(this).get(ClaimProcedureViewModel.class);

        //To Navigate
        NavHostFragment navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();

        binding.tabs.viewPagerTabs.addTab(binding.tabs.viewPagerTabs.newTab().setText("GHI"));
        binding.tabs.viewPagerTabs.addTab(binding.tabs.viewPagerTabs.newTab().setText("GPA"));
        binding.tabs.viewPagerTabs.addTab(binding.tabs.viewPagerTabs.newTab().setText("GTL"));

        //default cashless title for header
        binding.claimProcedureStepsHeader.setText(getString(R.string.cashless_procedure_steps));

        //cashless/reimbursement layout
        binding.cashlessStepsLayout.setOnClickListener(v -> {
            PROCEDURE_TYPE = "CASHLESS";
            product_code = "GMC";
            binding.reimbursementStepsLayout.setBackgroundResource(0);
            binding.cashlessText.setTextColor(ContextCompat.getColor(requireContext(), R.color.white));
            binding.reimbursementText.setTextColor(ContextCompat.getColor(requireContext(), R.color.black));
            binding.cashlessStepsLayout.setBackgroundResource(R.drawable.roundedsquare);
            binding.claimProcedureStepsHeader.setText(getString(R.string.cashless_procedure_steps));
            //to check if we have existing categories or no
            setuptabs();


            Glide.with(this).load(R.drawable.cashless).error(R.drawable.ic_placeholder).into(binding.imgCash);


        });

        binding.reimbursementStepsLayout.setOnClickListener(v -> {
            PROCEDURE_TYPE = "REIMBURSEMENT";
            product_code = "GMC";
            binding.cashlessStepsLayout.setBackgroundResource(0);
            binding.reimbursementText.setTextColor(ContextCompat.getColor(requireContext(), R.color.white));
            binding.cashlessText.setTextColor(ContextCompat.getColor(requireContext(), R.color.black));
            binding.reimbursementStepsLayout.setBackgroundResource(R.drawable.roundedsquare);
            binding.claimProcedureStepsHeader.setText(getString(R.string.reimbursment_procedure_steps));
            setuptabs();
            Glide.with(this).load(R.drawable.reimbursement).error(R.drawable.ic_placeholder).into(binding.imgCash);
        });

        tabSelectedListener = new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                switch (tab.getPosition()) {
                    case 0:
                        product_code = "GMC";
                        PROCEDURE_TYPE = "CASHLESS";
                        binding.gmcStepsToggle.setVisibility(View.VISIBLE);
                        binding.cashlessStepsLayout.performClick();

                        binding.stepsImageCardHolder.setVisibility(View.VISIBLE);
                        getProcedureClaimsOnTabs("gmc");
                        showHelpLineSection();
                        break;
                    case 1:
                        product_code = "GPA";
                        binding.stepsImageCardHolder.setVisibility(View.GONE);
                        binding.claimProcedureStepsHeader.setText(getString(R.string.gpa_claims_steps));
                        getProcedureClaimsOnTabs("gpa");
                        hideHelpLineSection();
                        break;
                    case 2:
                        product_code = "GTL";
                        binding.stepsImageCardHolder.setVisibility(View.GONE);
                        binding.claimProcedureStepsHeader.setText(getString(R.string.gtl_claims_steps));
                        getProcedureClaimsOnTabs("gtl");
                        hideHelpLineSection();
                        break;
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        };

        binding.tabs.viewPagerTabs.addOnTabSelectedListener(tabSelectedListener);

    }

    public void getClaimProcedure() {


        selectedPolicyViewModel.getSelectedPolicy().observe(getViewLifecycleOwner(), groupPolicyData -> {

            //change the selection chips ui
            selectChip(groupPolicyData);
            setTextWithFancyAnimation(binding.selectedPolicyText, "" + groupPolicyData.getPolicyNumber());


            binding.policySelectionText.setText(groupPolicyData.getPolicyNumber());
            product_code = groupPolicyData.getProductCode();
            // tpa_code = groupPolicyData.getTpaCode();


            LoadSessionResponse loadSessionResponse = loadSessionViewModel.getLoadSessionData().getValue();
            if (loadSessionResponse.getGroupPolicies().get(0).getGroupGMCPoliciesData().get(0).getPolicyType().equalsIgnoreCase("BASE")) {
                tpa_code = loadSessionResponse.getGroupPolicies().get(0).getGroupGMCPoliciesData().get(0).getTpaCode();
            }
            oeGrpBasInfSrNo = groupPolicyData.getOeGrpBasInfSrNo();


            if (loadSessionResponse != null) {
                if (!loadSessionResponse.getGroupProducts().isEmpty()) {
                    for (GroupProduct groupProduct : loadSessionResponse.getGroupProducts()) {

                        switch (groupProduct.getProductCode().toLowerCase()) {
                            case "gmc":
                                if (!groupProduct.getActive().equalsIgnoreCase("1")) {

                                    binding.tabs.viewPagerTabs.getTabAt(0).view.setEnabled(false);
                                    binding.tabs.viewPagerTabs.getTabAt(0).setTabLabelVisibility(TabLayout.TAB_LABEL_VISIBILITY_UNLABELED);
                                    binding.ghiChip.setVisibility(View.GONE);
                                }
                                break;
                            case "gpa":
                                if (!groupProduct.getActive().equalsIgnoreCase("1")) {

                                    binding.tabs.viewPagerTabs.getTabAt(1).view.setEnabled(false);
                                    binding.tabs.viewPagerTabs.getTabAt(1).setTabLabelVisibility(TabLayout.TAB_LABEL_VISIBILITY_UNLABELED);
                                    binding.gpaChip.setVisibility(View.GONE);
                                }
                                break;
                            case "gtl":
                                if (!groupProduct.getActive().equalsIgnoreCase("1")) {

                                    binding.tabs.viewPagerTabs.getTabAt(2).view.setEnabled(false);
                                    binding.tabs.viewPagerTabs.getTabAt(2).setTabLabelVisibility(TabLayout.TAB_LABEL_VISIBILITY_UNLABELED);
                                    binding.gtlChip.setVisibility(View.GONE);
                                }
                                break;
                        }

                    }
                }
                groupChildSrNo = loadSessionResponse.getGroupInfoData().getGroupchildsrno();

                switch (product_code.toLowerCase()) {
                    case "gmc":
                        binding.gmcStepsToggle.setVisibility(View.VISIBLE);
                        claimProcedureViewModel.getClaimsProcedureLayoutInfo(groupChildSrNo, oeGrpBasInfSrNo, product_code.toUpperCase(), PROCEDURE_TYPE);
                        binding.stepsImageCardHolder.setVisibility(View.VISIBLE);
                        //showHelpLineSection();
                        binding.tabs.viewPagerTabs.removeOnTabSelectedListener(tabSelectedListener);
                        binding.tabs.viewPagerTabs.getTabAt(0).select();
                        binding.tabs.viewPagerTabs.addOnTabSelectedListener(tabSelectedListener);
                        binding.cashlessStepsLayout.performClick();


                        //image in gmc only
                       // claimProcedureViewModel.getClaimProcedureImage(groupChildSrNo, oeGrpBasInfSrNo, product_code.toUpperCase(), PROCEDURE_TYPE);


                        if (groupPolicyData.getTypeOfPolSrNo().equalsIgnoreCase("8")) {
                            binding.stepsImageCardHolder.setVisibility(View.VISIBLE);
                            binding.stepsCard.setVisibility(View.GONE);
                            binding.additionalStepsCard.setVisibility(View.GONE);
                            binding.emergencyContactCard.setVisibility(View.GONE);
                            binding.imgCash.setVisibility(View.GONE);
                            binding.errorLayout.setVisibility(View.VISIBLE);
                        } else {
                            binding.stepsImageCardHolder.setVisibility(View.GONE);
                            binding.stepsCard.setVisibility(View.VISIBLE);
                            binding.additionalStepsCard.setVisibility(View.VISIBLE);
                            binding.emergencyContactCard.setVisibility(View.VISIBLE);
                            binding.imgCash.setVisibility(View.GONE);
                            binding.errorLayout.setVisibility(View.GONE);
                        }

                        break;
                    case "gpa":
                        binding.claimProcedureStepsHeader.setText(getString(R.string.gpa_claims_steps));
                        PROCEDURE_TYPE = "NOT AVAILABLE";
                        claimProcedureViewModel.getClaimsProcedureLayoutInfo(groupChildSrNo, oeGrpBasInfSrNo, product_code.toUpperCase(), PROCEDURE_TYPE);
                        binding.stepsImageCardHolder.setVisibility(View.GONE);
                        binding.gmcStepsToggle.setVisibility(View.GONE);
                        hideHelpLineSection();
                        binding.tabs.viewPagerTabs.removeOnTabSelectedListener(tabSelectedListener);
                        binding.tabs.viewPagerTabs.getTabAt(1).select();
                        binding.tabs.viewPagerTabs.addOnTabSelectedListener(tabSelectedListener);

                        //procedure information (steps)
                        claimProcedureViewModel.getClaimProcedureInformation(groupChildSrNo, oeGrpBasInfSrNo, product_code.toUpperCase(), PROCEDURE_TYPE);
                        //additional information
                        claimProcedureViewModel.getClaimProcedureText(groupChildSrNo, oeGrpBasInfSrNo, product_code.toUpperCase(), PROCEDURE_TYPE);
                        break;
                    case "gtl":
                        binding.claimProcedureStepsHeader.setText(getString(R.string.gtl_claims_steps));
                        PROCEDURE_TYPE = "NOT AVAILABLE";
                        claimProcedureViewModel.getClaimsProcedureLayoutInfo(groupChildSrNo, oeGrpBasInfSrNo, product_code.toUpperCase(), PROCEDURE_TYPE);
                        binding.stepsImageCardHolder.setVisibility(View.GONE);
                        binding.gmcStepsToggle.setVisibility(View.GONE);
                        hideHelpLineSection();
                        binding.tabs.viewPagerTabs.removeOnTabSelectedListener(tabSelectedListener);
                        binding.tabs.viewPagerTabs.getTabAt(2).select();
                        binding.tabs.viewPagerTabs.addOnTabSelectedListener(tabSelectedListener);

                        //procedure information (steps)
                        claimProcedureViewModel.getClaimProcedureInformation(groupChildSrNo, oeGrpBasInfSrNo, product_code.toUpperCase(), PROCEDURE_TYPE);
                        //additional information
                        claimProcedureViewModel.getClaimProcedureText(groupChildSrNo, oeGrpBasInfSrNo, product_code.toUpperCase(), PROCEDURE_TYPE);

                        break;
                }


                //emergency contact
                claimProcedureViewModel.getEmergencyContact(tpa_code);
            } else {
                //something went wrong
            }
        });

        claimProcedureViewModel.getClaimsProcedureLayoutInfoData().observe(getViewLifecycleOwner(), layoutInfo -> {
            if (layoutInfo != null) {
                if (!layoutInfo.getClaimProcLayoutInfo().isEmpty()) {
                    for (ClaimProcLayoutInfo layout : layoutInfo.getClaimProcLayoutInfo()) {
                        if (!layout.getLayoutPart().isEmpty() || !layout.getLayoutPart().equalsIgnoreCase("")) {
                            binding.errorLayout.setVisibility(View.GONE);
                            switch (layout.getLayoutPart().toLowerCase()) {
                                case "part1":
                                    if (layout.getLayoutPartVisibility().equalsIgnoreCase("0")) {
                                        binding.stepsImageCardHolder.setVisibility(View.GONE);
                                    } else {
                                        binding.stepsImageCardHolder.setVisibility(View.VISIBLE);
                                    }
                                    break;
                                case "part2":
                                    if (layout.getLayoutPartVisibility().equalsIgnoreCase("0")) {
                                        binding.stepsCard.setVisibility(View.GONE);
                                    } else {
                                        binding.stepsCard.setVisibility(View.VISIBLE);
                                    }
                                    break;
                                case "part3":
                                    if (layout.getLayoutPartVisibility().equalsIgnoreCase("0")) {
                                        binding.additionalStepsCard.setVisibility(View.GONE);
                                    } else {
                                        binding.additionalStepsCard.setVisibility(View.VISIBLE);
                                    }

                                case "part4":
                                    if (layout.getLayoutPartVisibility().equalsIgnoreCase("0")) {
                                        //downloadable claim forms
                                    }
                                    break;
                                case "part5":
                                    if (layout.getLayoutPartVisibility().equalsIgnoreCase("0")) {
                                        binding.emergencyContactCard.setVisibility(View.GONE);
                                    } else {
                                        binding.emergencyContactCard.setVisibility(View.VISIBLE);
                                    }
                                    break;
                            }
                        } else {
                            binding.stepsImageCardHolder.setVisibility(View.GONE);
                            binding.stepsCard.setVisibility(View.GONE);
                            binding.additionalStepsCard.setVisibility(View.GONE);
                            binding.emergencyContactCard.setVisibility(View.GONE);
                            binding.imgCash.setVisibility(View.GONE);
                            binding.errorLayout.setVisibility(View.VISIBLE);
                        }
                    }
                    if (product_code.equalsIgnoreCase("gmc")) {
                        binding.stepsImageCardHolder.setVisibility(View.GONE);
                        binding.stepsCard.setVisibility(View.GONE);
                        binding.additionalStepsCard.setVisibility(View.GONE);
                        binding.emergencyContactCard.setVisibility(View.GONE);
                        binding.imgCash.setVisibility(View.VISIBLE);


                    } else if (product_code.equalsIgnoreCase("gpa")) {

                        binding.imgCash.setVisibility(View.GONE);
                    } else if (product_code.equalsIgnoreCase("gtl")) {

                        binding.imgCash.setVisibility(View.GONE);
                    }

                } else {
                    //here we get the layout as empty List
                }
            }
        });

        claimProcedureViewModel.getClaimProcedureImageData().observe(getViewLifecycleOwner(), imageResponse -> {

            try {

                String claims_procedure_image = DOWNLOAD_BASE_URL + "mybenefits/claimprocedures/" + groupChildSrNo + "/" + groupChildSrNo + "-" + oeGrpBasInfSrNo + "/displayimage/" + imageResponse.getClaimProcImagePath().get(0).getClmProcP1ImgPath();

                LogMyBenefits.d(LogTags.CLAIMS_PROCEDURE_ACTIVITY, "GLIDE IMAGE: " + claims_procedure_image);
                Shimmer shimmer = new Shimmer.AlphaHighlightBuilder().setDuration(1000).setBaseAlpha(0.85f).setHighlightAlpha(0.6f).setDirection(Shimmer.Direction.LEFT_TO_RIGHT).setAutoStart(true).build();

                // This is the placeholder for the imageView
                ShimmerDrawable shimmerDrawable = new ShimmerDrawable();
                shimmerDrawable.setShimmer(shimmer);

                //  Glide.with(this).load(claims_procedure_image).placeholder(shimmerDrawable).error(R.drawable.ic_placeholder).into(binding.claimsProcedureImage);

                if (PROCEDURE_TYPE.equalsIgnoreCase("cashless")) {
                    Glide.with(this).load(R.drawable.cashless).placeholder(shimmerDrawable).error(R.drawable.ic_placeholder).into(binding.imgCash);
                } else {
                    Glide.with(this).load(R.drawable.reimbursement).placeholder(shimmerDrawable).error(R.drawable.ic_placeholder).into(binding.imgCash);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        claimProcedureViewModel.getClaimProcedureLayoutInstructionInfoData().observe(getViewLifecycleOwner(), textResponse -> {

            if (textResponse.getClaimProcInstructionInfo().get(0).getIsDefInstTextFromFile().equals("1")) {
                //binding.claimsStepsText.loadData(claimProcedureTextFileData, "text/html", "utf-8");
                LogMyBenefits.d(LogTags.DOWNLOAD_ACTIVITY, "onViewCreated: " + textResponse);
                String url = DOWNLOAD_BASE_URL + "mybenefits/claimprocedures/" + groupChildSrNo + "/" + groupChildSrNo + "-" + oeGrpBasInfSrNo + "/displayinstructions/" + textResponse.getClaimProcInstructionInfo().get(0).getDefInstTextFromFilePath();

                LogMyBenefits.d(LogTags.CLAIMS_PROCEDURE_ACTIVITY, "STEPS FILE: " + url);

                try {
                    claimProcedureViewModel.getClaimStepsHtmlData(groupChildSrNo, groupChildSrNo + "-" + oeGrpBasInfSrNo, textResponse.getClaimProcInstructionInfo().get(0).getDefInstTextFromFilePath());
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }


        });

        //observer of html txt response
        claimProcedureViewModel.getClaimStepsHtmlDataObserver().observe(getViewLifecycleOwner(), claimProcedureStepsData -> {
            if (claimProcedureStepsData.equalsIgnoreCase("")) {

                binding.noStepsFound.setVisibility(View.VISIBLE);
            } else {
                binding.noStepsFound.setVisibility(View.GONE);


                binding.claimsStepsText.setWebViewClient(new WebViewClient());
                binding.claimsStepsText.getSettings().setJavaScriptEnabled(true);
                binding.claimsStepsText.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
                binding.claimsStepsText.getSettings().setPluginState(WebSettings.PluginState.ON);
                binding.claimsStepsText.getSettings().setMediaPlaybackRequiresUserGesture(false);
                binding.claimsStepsText.setWebChromeClient(new WebChromeClient());
                binding.claimsStepsText.loadDataWithBaseURL(null, css_class + claimProcedureStepsData, "text/html", "UTF-8", null);


            }
        });

        //additional information
        claimProcedureViewModel.getClaimProcedureTextData().observe(getViewLifecycleOwner(), additionalInstruction -> {
            if (!additionalInstruction.getClaimProcTextPath().get(0).getClmProcP1WysiwygTextPath().isEmpty()) {
                //binding.claimsStepsText.loadData(claimProcedureTextFileData, "text/html", "utf-8");
                LogMyBenefits.d(LogTags.CLAIMS_PROCEDURE_ACTIVITY, "additional instruction: " + additionalInstruction);

                try {
                    String group_oegrpbasInfo = groupChildSrNo + "-" + oeGrpBasInfSrNo;
                    claimProcedureViewModel.getClaimStepsHtmlAdditionalData(groupChildSrNo, group_oegrpbasInfo, additionalInstruction.getClaimProcTextPath().get(0).getClmProcP1WysiwygTextPath()).observe(getViewLifecycleOwner(), claimProcedureStepsData -> {
                        if (claimProcedureStepsData.equalsIgnoreCase("")) {

                            binding.additionalNoStepsFound.setVisibility(View.VISIBLE);

                        } else {
                            binding.additionalNoStepsFound.setVisibility(View.GONE);
                            binding.additionalClaimsStepsText.loadData(css_class + claimProcedureStepsData, "text/html", "utf-8");
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    binding.additionalNoStepsFound.setVisibility(View.VISIBLE);
                }

            } else {
                binding.additionalNoStepsFound.setVisibility(View.VISIBLE);
            }


        });


        claimProcedureViewModel.getClaimProcedureEmergencyContactData().observe(getViewLifecycleOwner(), contact -> {
            if (contact != null) {
                if (contact.getEmergencyContactNo() != null) {
                    if (!contact.getEmergencyContactNo().isEmpty()) {
                        binding.noContacts.setVisibility(View.GONE);
                        binding.claimProceduresHelplineCycle.setVisibility(View.VISIBLE);
                        adapter = new ClaimProcedureHelplineNumberAdapters(requireContext(), contact.getEmergencyContactNo());
                        binding.claimProceduresHelplineCycle.setAdapter(adapter);
                        adapter.notifyItemRangeChanged(0, contact.getEmergencyContactNo().size());
                    } else {
                        adapter.notifyDataSetChanged();
                        binding.claimProceduresHelplineCycle.setVisibility(View.GONE);
                        binding.noContacts.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        claimProcedureViewModel.getLoadingState().observe(getViewLifecycleOwner(), loading -> {
            if (loading) {
                showLoading();
            } else {
                hideLoading();
            }
        });


        binding.spinnerHolder.setOnClickListener(v -> {

            PolicyChangeDialogue policyChangeDialogue = new PolicyChangeDialogue(requireActivity(), selectedPolicyViewModel);
            policyChangeDialogue.showPolicyAlert(policyData, selectedIndex);

        });

        selectedPolicyViewModel.getPolicyData().observe(getViewLifecycleOwner(), policyData -> {
            this.policyData = policyData;
        });

        selectedPolicyViewModel.getSelectedIndex().observe(getViewLifecycleOwner(), index -> {
            this.selectedIndex = index;
        });

        binding.ghiChip.setOnClickListener(v -> {
            LoadSessionResponse loadSessionResponse = loadSessionViewModel.getLoadSessionData().getValue();
            try {

                if (!loadSessionResponse.getGroupPolicies().get(0).getGroupGMCPoliciesData().isEmpty()) {
                    setPolicyWithChips("gmc");
                } else {
                    Toast.makeText(requireActivity(), "Policy not available!", Toast.LENGTH_SHORT).show();
                }


            } catch (Exception e) {
                Toast.makeText(requireActivity(), "Policy not available!", Toast.LENGTH_SHORT).show();
            }

        });
        binding.gpaChip.setOnClickListener(v -> {
            LoadSessionResponse loadSessionResponse = loadSessionViewModel.getLoadSessionData().getValue();
            try {

                if (!loadSessionResponse.getGroupPolicies().get(0).getGroupGPAPoliciesData().isEmpty()) {
                    setPolicyWithChips("gpa");
                } else {
                    Toast.makeText(requireActivity(), "Policy not available!", Toast.LENGTH_SHORT).show();
                }


            } catch (Exception e) {
                Toast.makeText(requireActivity(), "Policy not available!", Toast.LENGTH_SHORT).show();
            }
        });
        binding.gtlChip.setOnClickListener(v -> {
            LoadSessionResponse loadSessionResponse = loadSessionViewModel.getLoadSessionData().getValue();
            try {

                if (!loadSessionResponse.getGroupPolicies().get(0).getGroupGTLPoliciesData().isEmpty()) {
                    setPolicyWithChips("gtl");
                } else {
                    Toast.makeText(requireActivity(), "Policy not available!", Toast.LENGTH_SHORT).show();
                }


            } catch (Exception e) {
                Toast.makeText(requireActivity(), "Policy not available!", Toast.LENGTH_SHORT).show();
            }
        });

        binding.selectPolicyChip.setOnClickListener(v -> {
            PolicyChangeDialogue policyChangeDialogue = new PolicyChangeDialogue(requireActivity(), selectedPolicyViewModel);
            policyChangeDialogue.showPolicyAlert(policyData, selectedIndex);
        });
        claimProcedureViewModel.getReloginState().observe(getActivity(), relogin -> {
            if (relogin) {
                UtilMethods.RedirectToLogin(requireActivity());
            } else {
            }
        });


    }


    public void getProcedureClaimsOnTabs(String code) {

        try {


            LoadSessionResponse loadSessionResponse = loadSessionViewModel.getLoadSessionData().getValue();

            if (!loadSessionResponse.getGroupProducts().isEmpty()) {
                for (GroupProduct groupProduct : loadSessionResponse.getGroupProducts()) {

                    switch (groupProduct.getProductCode().toLowerCase()) {
                        case "gmc":
                            if (!groupProduct.getActive().equalsIgnoreCase("1")) {

                                binding.tabs.viewPagerTabs.getTabAt(0).view.setEnabled(false);
                                binding.tabs.viewPagerTabs.getTabAt(0).setTabLabelVisibility(TabLayout.TAB_LABEL_VISIBILITY_UNLABELED);
                            }
                            break;
                        case "gpa":
                            if (!groupProduct.getActive().equalsIgnoreCase("1")) {

                                binding.tabs.viewPagerTabs.getTabAt(1).view.setEnabled(false);
                                binding.tabs.viewPagerTabs.getTabAt(1).setTabLabelVisibility(TabLayout.TAB_LABEL_VISIBILITY_UNLABELED);
                            }
                            break;
                        case "gtl":
                            if (!groupProduct.getActive().equalsIgnoreCase("1")) {

                                binding.tabs.viewPagerTabs.getTabAt(2).view.setEnabled(false);
                                binding.tabs.viewPagerTabs.getTabAt(2).setTabLabelVisibility(TabLayout.TAB_LABEL_VISIBILITY_UNLABELED);
                            }
                            break;
                    }

                }
            }

            groupChildSrNo = loadSessionResponse.getGroupInfoData().getGroupchildsrno();
            product_code = code;

            /*List<GroupPolicyData> gmcPolicy = sort(loadSessionResponse.getGroupPolicies().get(0).getGroupGMCPoliciesData());
            for (GroupPolicyData policy : gmcPolicy) {
                if (policy.getPolicyType().equalsIgnoreCase("base")) {
                    oeGrpBasInfSrNo = policy.getOeGrpBasInfSrNo();
                    tpa_code = policy.getTpaCode();
                }
            }*/

            switch (product_code.toLowerCase()) {
                case "gmc":
                    List<GroupPolicyData> gmcPolicy = sort(loadSessionResponse.getGroupPolicies().get(0).getGroupGMCPoliciesData());


                    selectedPolicyViewModel.setGroupGMCPoliciesData(gmcPolicy);
                    selectedPolicyViewModel.setGroupGPAPoliciesData(new ArrayList<>());
                    selectedPolicyViewModel.setGroupGTLPoliciesData(new ArrayList<>());
                    selectedPolicyViewModel.getAllPoliciesData();
                    selectedPolicyViewModel.setSelectedIndex(0);
                    selectedPolicyViewModel.setSelectedPolicyFromDropDown(gmcPolicy.get(0));


                    binding.stepsImageCardHolder.setVisibility(View.VISIBLE);
                    showHelpLineSection();

                   /* for (GroupPolicyData policy : gmcPolicy) {
                        if (policy.getPolicyType().equalsIgnoreCase("base")) {
                            oeGrpBasInfSrNo = policy.getOeGrpBasInfSrNo();
                            tpa_code = policy.getTpaCode();
                        }
                    }*/

                    break;
                case "gpa":
                    List<GroupPolicyData> gpaPolicy = sort(loadSessionResponse.getGroupPolicies().get(0).getGroupGPAPoliciesData());

                    selectedPolicyViewModel.setGroupGMCPoliciesData(new ArrayList<>());
                    selectedPolicyViewModel.setGroupGPAPoliciesData(gpaPolicy);
                    selectedPolicyViewModel.setGroupGTLPoliciesData(new ArrayList<>());
                    selectedPolicyViewModel.getAllPoliciesData();
                    selectedPolicyViewModel.setSelectedIndex(0);
                    selectedPolicyViewModel.setSelectedPolicyFromDropDown(gpaPolicy.get(0));


                    binding.stepsImageCardHolder.setVisibility(View.GONE);
                    hideHelpLineSection();
                   /* for (GroupPolicyData policy : gpaPolicy) {
                        if (policy.getPolicyType().equalsIgnoreCase("base")) {
                            oeGrpBasInfSrNo = policy.getOeGrpBasInfSrNo();
                            tpa_code = policy.getTpaCode();
                        }
                    }*/

                    break;
                case "gtl":
                    List<GroupPolicyData> gtlPolicy = sort(loadSessionResponse.getGroupPolicies().get(0).getGroupGTLPoliciesData());

                    selectedPolicyViewModel.setGroupGMCPoliciesData(new ArrayList<>());
                    selectedPolicyViewModel.setGroupGPAPoliciesData(new ArrayList<>());
                    selectedPolicyViewModel.setGroupGTLPoliciesData(gtlPolicy);
                    selectedPolicyViewModel.getAllPoliciesData();
                    selectedPolicyViewModel.setSelectedIndex(0);
                    selectedPolicyViewModel.setSelectedPolicyFromDropDown(gtlPolicy.get(0));


                    binding.stepsImageCardHolder.setVisibility(View.GONE);
                    hideHelpLineSection();
                   /* for (GroupPolicyData policy : gtlPolicy) {
                        if (policy.getPolicyType().equalsIgnoreCase("base")) {
                            oeGrpBasInfSrNo = policy.getOeGrpBasInfSrNo();
                            tpa_code = policy.getTpaCode();
                        }
                    }*/

                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void hideLoading() {
        binding.progressBar.setVisibility(View.GONE);
        binding.claimsProcedureContentLayout.setVisibility(View.VISIBLE);
    }

    private void showLoading() {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.claimsProcedureContentLayout.setVisibility(View.GONE);
    }

    private void hideHelpLineSection() {
        binding.emergencyContactCard.setVisibility(View.GONE);
    }

    private void showHelpLineSection() {
        binding.emergencyContactCard.setVisibility(View.VISIBLE);
    }


    /**
     * @link {{@link #sort(List)}}
     * to sort the list before setting up to the spinner.
     **/
    private List<GroupPolicyData> sort(List<GroupPolicyData> list) {

        list.sort(Comparator.comparing(GroupPolicyData::getOeGrpBasInfSrNo));

        return list;
    }


    private void selectChip(GroupPolicyData groupPolicyData) {
        if (groupPolicyData != null) {
            switch (groupPolicyData.getProductCode().toLowerCase()) {
                case "gmc":
                    binding.ghiChip.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.chips_background_selected));
                    binding.gpaChip.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.chips_background));
                    binding.gtlChip.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.chips_background));

                    //text color
                    binding.ghiChipText.setTextColor(ContextCompat.getColor(requireContext(), R.color.white));
                    binding.gpaChipText.setTextColor(ContextCompat.getColor(requireContext(), R.color.dark_grey));
                    binding.gtlChipText.setTextColor(ContextCompat.getColor(requireContext(), R.color.dark_grey));

                    break;
                case "gpa":
                    binding.ghiChip.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.chips_background));
                    binding.gpaChip.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.chips_background_selected));
                    binding.gtlChip.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.chips_background));

                    //text color
                    binding.ghiChipText.setTextColor(ContextCompat.getColor(requireContext(), R.color.dark_grey));
                    binding.gpaChipText.setTextColor(ContextCompat.getColor(requireContext(), R.color.white));
                    binding.gtlChipText.setTextColor(ContextCompat.getColor(requireContext(), R.color.dark_grey));
                    break;
                case "gtl":
                    binding.ghiChip.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.chips_background));
                    binding.gpaChip.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.chips_background));
                    binding.gtlChip.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.chips_background_selected));

                    //text color
                    binding.ghiChipText.setTextColor(ContextCompat.getColor(requireContext(), R.color.dark_grey));
                    binding.gpaChipText.setTextColor(ContextCompat.getColor(requireContext(), R.color.dark_grey));
                    binding.gtlChipText.setTextColor(ContextCompat.getColor(requireContext(), R.color.white));
                    break;
            }
        } else {
            //selecting gmc default

            binding.ghiChip.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.chips_background_selected));
            binding.gpaChip.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.chips_background));
            binding.gtlChip.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.chips_background));

            //text color
            binding.ghiChipText.setTextColor(ContextCompat.getColor(requireContext(), R.color.white));
            binding.gpaChipText.setTextColor(ContextCompat.getColor(requireContext(), R.color.dark_grey));
            binding.gtlChipText.setTextColor(ContextCompat.getColor(requireContext(), R.color.dark_grey));
        }
    }

    private void setTextWithFancyAnimation(TextView codeView, String value) {
        Animation translateIn = new TranslateAnimation(0, 0, codeView.getHeight(), 0);
        translateIn.setInterpolator(new OvershootInterpolator());
        translateIn.setDuration(500);

        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setDuration(200);

        AnimationSet animationSet = new AnimationSet(false);
        animationSet.addAnimation(fadeIn);
        animationSet.addAnimation(translateIn);
        animationSet.reset();
        animationSet.setStartTime(0);

        codeView.setText(String.valueOf(value));
        codeView.clearAnimation();
        codeView.startAnimation(animationSet);
    }

    private void setPolicyWithChips(String code) {
        try {
            loadSessionViewModel.getLoadSessionData().observe(getViewLifecycleOwner(), loadSessionResponse -> {
                if (!loadSessionResponse.getGroupProducts().isEmpty()) {
                    for (GroupProduct groupProduct : loadSessionResponse.getGroupProducts()) {

                        switch (groupProduct.getProductCode().toLowerCase()) {
                            case "gmc":
                                if (!groupProduct.getActive().equalsIgnoreCase("1")) {
                                    binding.ghiChip.setVisibility(View.GONE);

                                }
                                break;
                            case "gpa":
                                if (!groupProduct.getActive().equalsIgnoreCase("1")) {
                                    binding.gpaChip.setVisibility(View.GONE);
                                }
                                break;
                            case "gtl":
                                if (!groupProduct.getActive().equalsIgnoreCase("1")) {
                                    binding.gtlChip.setVisibility(View.GONE);
                                }
                                break;
                        }

                    }
                } else {
                    Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }


                switch (code.toUpperCase()) {
                    case "GMC":
                        List<GroupPolicyData> gmcPolicy = sort(loadSessionResponse.getGroupPolicies().get(0).getGroupGMCPoliciesData());

                        selectedPolicyViewModel.setGroupGMCPoliciesData(gmcPolicy);
                        selectedPolicyViewModel.setGroupGPAPoliciesData(new ArrayList<>());
                        selectedPolicyViewModel.setGroupGTLPoliciesData(new ArrayList<>());
                        selectedPolicyViewModel.getAllPoliciesData();
                        selectedPolicyViewModel.setSelectedIndex(0);
                        selectedPolicyViewModel.setSelectedPolicyFromDropDown(gmcPolicy.get(0));

                        break;
                    case "GPA":
                        List<GroupPolicyData> gpaPolicy = sort(loadSessionResponse.getGroupPolicies().get(0).getGroupGPAPoliciesData());
                        selectedPolicyViewModel.setGroupGMCPoliciesData(new ArrayList<>());
                        selectedPolicyViewModel.setGroupGPAPoliciesData(gpaPolicy);
                        selectedPolicyViewModel.setGroupGTLPoliciesData(new ArrayList<>());
                        selectedPolicyViewModel.getAllPoliciesData();
                        selectedPolicyViewModel.setSelectedIndex(0);
                        selectedPolicyViewModel.setSelectedPolicyFromDropDown(gpaPolicy.get(0));


                        break;
                    case "GTL":
                        List<GroupPolicyData> gtlPolicy = sort(loadSessionResponse.getGroupPolicies().get(0).getGroupGTLPoliciesData());

                        selectedPolicyViewModel.setGroupGMCPoliciesData(new ArrayList<>());
                        selectedPolicyViewModel.setGroupGPAPoliciesData(new ArrayList<>());
                        selectedPolicyViewModel.setGroupGTLPoliciesData(gtlPolicy);
                        selectedPolicyViewModel.getAllPoliciesData();
                        selectedPolicyViewModel.setSelectedIndex(0);
                        selectedPolicyViewModel.setSelectedPolicyFromDropDown(gtlPolicy.get(0));


                        break;
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() != null) {
            initView();
            getClaimProcedure();
        }
    }
}