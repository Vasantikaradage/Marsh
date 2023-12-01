package com.marsh.android.MB360.insurance;

import static com.marsh.android.MB360.utilities.AppLocalConstant.AUTH_EMAIL;
import static com.marsh.android.MB360.utilities.AppLocalConstant.AUTH_LOGIN_ID;
import static com.marsh.android.MB360.utilities.AppLocalConstant.AUTH_LOGIN_TYPE;
import static com.marsh.android.MB360.utilities.AppLocalConstant.AUTH_PHONE_NUMBER;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.marsh.android.MB360.BuildConfig;
import com.marsh.android.MB360.R;
import com.marsh.android.MB360.databinding.FragmentHomeBinding;
import com.marsh.android.MB360.insurance.adminsetting.ui.AdminSettingViewModel;
import com.marsh.android.MB360.insurance.coverages.repository.CoveragesViewModel;
import com.marsh.android.MB360.insurance.dialogues.PolicyChangeDialogue;
import com.marsh.android.MB360.insurance.ecards.repository.EcardViewModel;
import com.marsh.android.MB360.insurance.enrollment.repository.EnrollmentViewModel;
import com.marsh.android.MB360.insurance.enrollment.repository.SummaryViewModel;
import com.marsh.android.MB360.insurance.enrollmentstatus.EnrollmentStatusViewModel;
import com.marsh.android.MB360.insurance.hospitalnetwork.repository.HospitalNetworkViewModel;
import com.marsh.android.MB360.insurance.myclaims.repository.MyClaimsViewModel;
import com.marsh.android.MB360.insurance.queries.repository.QueryViewModel;
import com.marsh.android.MB360.insurance.repository.LoadSessionViewModel;
import com.marsh.android.MB360.insurance.repository.responseclass.GroupGMCPolicyEmployeeDatum;
import com.marsh.android.MB360.insurance.repository.responseclass.GroupGPAPolicyEmployeeDatum;
import com.marsh.android.MB360.insurance.repository.responseclass.GroupGTLPolicyEmployeeDatum;
import com.marsh.android.MB360.insurance.repository.responseclass.GroupProduct;
import com.marsh.android.MB360.insurance.repository.responseclass.LoadSessionResponse;
import com.marsh.android.MB360.insurance.repository.selectedPolicyRepo.SelectedPolicyViewModel;
import com.marsh.android.MB360.insurance.repository.selectedPolicyRepo.responseclass.GroupPolicyData;
import com.marsh.android.MB360.insurance.servicenames.ServiceNamesViewModel;
import com.marsh.android.MB360.utilities.BadgesDrawable;
import com.marsh.android.MB360.utilities.EncryptionPreference;
import com.marsh.android.MB360.utilities.FileDownloader;
import com.marsh.android.MB360.utilities.LoadingInsuranceDialogue;
import com.marsh.android.MB360.utilities.LogMyBenefits;
import com.marsh.android.MB360.utilities.UtilMethods;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class HomeFragment extends Fragment implements DashboardItemClickListener {

    FragmentHomeBinding binding;
    NavController navController;
    View view;
    LoadSessionViewModel loadSessionViewModel;
    SelectedPolicyViewModel selectedPolicyViewModel;
    HospitalNetworkViewModel hospitalNetworkViewModel;
    MyClaimsViewModel myClaimsViewModel;
    CoveragesViewModel coveragesViewModel;
    QueryViewModel queryViewModel;
    EcardViewModel ecardViewModel;
    EnrollmentViewModel enrollmentViewModel;
    ServiceNamesViewModel serviceNamesViewModel;

    EnrollmentStatusViewModel enrollmentStatusViewModel;
    List<GroupPolicyData> policyData;
    LayerDrawable icon;
    String PRODUCT_CODE = "GMC";
    String oeGrpBasInfoSrNo = "";
    int temp_position = 0;
    AdminSettingViewModel adminSettingViewModel;

    LoadingInsuranceDialogue loadingInsuranceDialogue;

    String groupChildSrnNo = "";
    String empSrNo = "";

    boolean userSelect = false;
    boolean SELECTED_FIRST_POLICY = true;

    //this integer helps to hold the position of the policy whenever the dialogue re-opens.
    int selectedIndex;


    //new list of dashboard
    List<DashBoardModel> dashBoardItemList = new ArrayList<>();

    //adapter for dashboard item
    DashboardItemsAdapter dashboardAdapter;

    EncryptionPreference encryptionPreference;

    Boolean TO_SHOW_HOSPITAL = true;

    DashBoardModel providerNetworkModel = new DashBoardModel();
    DashBoardModel myClaimModel = new DashBoardModel();
    DashBoardModel intimateNowModel = new DashBoardModel();
    SummaryViewModel summaryViewModel;
    private AlertDialog.Builder mBuilder;

    private boolean CARD_ENROLL_ENABLE = false;

    private GroupPolicyData lastObservedData;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //encryption preference
        encryptionPreference = new EncryptionPreference(requireContext());
        loadingInsuranceDialogue = new LoadingInsuranceDialogue(requireContext(), requireActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        view = binding.getRoot();


        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        /** here we load the load session data from the dashboard activity */
        //passing require activity because view-models is scoped in dashboard activity
        loadSessionViewModel = new ViewModelProvider(requireActivity()).get(LoadSessionViewModel.class);
        summaryViewModel = new ViewModelProvider(this).get(SummaryViewModel.class);
        //view-model for selected policy to access its details
        selectedPolicyViewModel = new ViewModelProvider(requireActivity()).get(SelectedPolicyViewModel.class);
        hospitalNetworkViewModel = new ViewModelProvider(requireActivity()).get(HospitalNetworkViewModel.class);
        myClaimsViewModel = new ViewModelProvider(requireActivity()).get(MyClaimsViewModel.class);
        queryViewModel = new ViewModelProvider(requireActivity()).get(QueryViewModel.class);
        coveragesViewModel = new ViewModelProvider(this).get(CoveragesViewModel.class);
        ecardViewModel = new ViewModelProvider(requireActivity()).get(EcardViewModel.class);
        enrollmentViewModel = new ViewModelProvider(this).get(EnrollmentViewModel.class);
        serviceNamesViewModel = new ViewModelProvider(this).get(ServiceNamesViewModel.class);
        adminSettingViewModel = new ViewModelProvider(requireActivity()).get(AdminSettingViewModel.class);
        enrollmentStatusViewModel = new ViewModelProvider(this).get(EnrollmentStatusViewModel.class);

        providerNetworkModel.setDashBoardTextDescription("");
        providerNetworkModel.setDashBoardImage(ContextCompat.getDrawable(requireContext(), R.drawable.ic_by_provider_network));
        providerNetworkModel.setDashBoardHeader(getString(R.string.provider_network));

        myClaimModel.setDashBoardTextDescription("");
        myClaimModel.setDashBoardImage(ContextCompat.getDrawable(requireContext(), R.drawable.ic_by_my_claims));
        myClaimModel.setDashBoardHeader(getString(R.string.my_claims));

        intimateNowModel.setDashBoardImage(ContextCompat.getDrawable(requireContext(), R.drawable.ic_by_intimate_now));
        intimateNowModel.setDashBoardHeader(getString(R.string.intimate_now));
        intimateNowModel.setDashBoardTextDescription("");


        if (dashBoardItemList.isEmpty()) {
            dashBoardItemList.add(new DashBoardModel(getString(R.string.my_coverages), ContextCompat.getDrawable(requireContext(), R.drawable.ic_by_my_coverages), ""));
            //dashBoardItemList.add(new DashBoardModel(getString(R.string.my_claims), ContextCompat.getDrawable(requireContext(), R.drawable.ic_by_my_claims), ""));
            //dashBoardItemList.add(new DashBoardModel(getString(R.string.my_queries), ContextCompat.getDrawable(requireContext(), R.drawable.ic_by_my_query), ""));
            dashBoardItemList.add(new DashBoardModel("Claim\nProcedures", ContextCompat.getDrawable(requireContext(), R.drawable.ic_by_claim_procedure), ""));
            // dashBoardItemList.add(new DashBoardModel("Intimate\nClaim", ContextCompat.getDrawable(requireContext(), R.drawable.ic_by_intimate_now), ""));
            dashBoardItemList.add(new DashBoardModel("Policy\nFeatures", ContextCompat.getDrawable(requireContext(), R.drawable.ic_by_polic_features), ""));
            dashBoardItemList.add(new DashBoardModel("FAQs", ContextCompat.getDrawable(requireContext(), R.drawable.ic_by_faq), ""));
            //  dashBoardItemList.add(new DashBoardModel("Claim Data Upload Web", ContextCompat.getDrawable(requireContext(), R.drawable.ic_by_my_claims), ""));
            //   dashBoardItemList.add(new DashBoardModel("Claim Data Upload App", ContextCompat.getDrawable(requireContext(), R.drawable.ic_by_my_claims), ""));


        }

        dashboardAdapter = new DashboardItemsAdapter(requireContext(), dashBoardItemList, this);
        binding.dashboardCycle.setAdapter(dashboardAdapter);
        RecyclerView.ItemAnimator animator = binding.dashboardCycle.getItemAnimator();
        if (animator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
        }

        //to navigate
        NavHostFragment navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();


        int mutipleSpan = 6; //least common multiple
        int maxSpan = 3;//max span count

        GridLayoutManager layoutManager = new GridLayoutManager(requireContext(), mutipleSpan);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int actualSize = 0;

                if (position > 3) {
                    actualSize = 3;
                } else {
                    actualSize = 2;
                }

                if (actualSize < maxSpan) {
                    return mutipleSpan / actualSize;
                }
                return mutipleSpan / maxSpan;

            }
        });
        binding.dashboardCycle.setLayoutManager(layoutManager);

        //has menu options (title bar for policies)
        MenuProvider menuProvider = new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {

                inflater.inflate(R.menu.home_fragment_menu, menu);
                MenuItem item = menu.findItem(R.id.policy_change);
                icon = (LayerDrawable) item.getIcon();
                //set count on badge
                selectedPolicyViewModel.totalPolicyCount().observe(getViewLifecycleOwner(), totalCount -> {
                    setBadgeCount(getContext(), icon, String.valueOf(totalCount));
                });
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.policy_change) {
                    //policy number Change Dialogue (so that they can See for what details he is changing for!)
                    PolicyChangeDialogue policyChangeDialogue = new PolicyChangeDialogue(requireActivity(), selectedPolicyViewModel);
                    policyChangeDialogue.showPolicyAlert(policyData, selectedIndex);
                }

                return true;


            }
        };
        // requireActivity().addMenuProvider(menuProvider, getViewLifecycleOwner());

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                exitWarning();
            }
        });


        //setup of the policy bar
        //adding thr tabs


        loadSessionViewModel.getLoadSessionData().observe(requireActivity(), loadSessionResponse -> {

            lastObservedData = null;
            if (loadSessionResponse != null) {
                try {


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
                    }

                } catch (Exception e) {
                    //we don't get the loadSession.
                    e.printStackTrace();

                }
            }

            GroupPolicyData data = selectedPolicyViewModel.getSelectedPolicy().getValue();

            if (data == null) {
                if (loadSessionResponse != null && SELECTED_FIRST_POLICY) {
                    SELECTED_FIRST_POLICY = true;
                    LogMyBenefits.d("HOME-FRAGMENT", "onCreateView: " + loadSessionResponse);
                    //setting all the three policies
                    //sort all policy according to oegrpbasinfo.
                    selectedPolicyViewModel.setGroupGMCPoliciesData(sort(loadSessionResponse.getGroupPolicies().get(0).getGroupGMCPoliciesData()));
                    // selectedPolicyViewModel.setGroupGPAPoliciesData(sort(loadSessionResponse.getGroupPolicies().get(0).getGroupGPAPoliciesData()));
                    //selectedPolicyViewModel.setGroupGTLPoliciesData(sort(loadSessionResponse.getGroupPolicies().get(0).getGroupGTLPoliciesData()));

                    //getting tall the policies that are applicable to check!
                    selectedPolicyViewModel.getAllPoliciesData();
                    selectChip(null);
                    TO_SHOW_HOSPITAL = true;
                }
            } else {
                binding.selectedPolicyText.setText("" + data.getPolicyNumber());
            }

        });


        selectedPolicyViewModel.getSelectedIndex().observe(getViewLifecycleOwner(), index -> {
            selectedIndex = index;
        });

        //if in-case we lost our activity then we can show a loading ui until the data is back
        loadSessionViewModel.getLoadingState().observe(requireActivity(), loading -> {
            if (loading) {
                showLoading();
               /* binding.homeShimmer.getRoot().setVisibility(View.VISIBLE);
                binding.contentLayout.setVisibility(View.GONE);*/
                binding.refreshLayout.setRefreshing(true);
            } else {
                hideLoading();
              /*  binding.homeShimmer.getRoot().setVisibility(View.GONE);
                binding.contentLayout.setVisibility(View.VISIBLE);*/
                binding.refreshLayout.setRefreshing(false);

            }
        });

        //refresh
        binding.refreshLayout.setOnRefreshListener(this::loadSessions);

        selectedPolicyViewModel.getPolicyData().observe(getViewLifecycleOwner(), groupPolicyData -> {
            policyData = groupPolicyData;
        });


        //getClaims();


        //to get the coverages and spinner data setup
        selectedPolicyViewModel.getSelectedPolicy().observe(getViewLifecycleOwner(), groupPolicyData -> {

            if (lastObservedData == null || !lastObservedData.equals(groupPolicyData)) {
                lastObservedData = groupPolicyData;

                LogMyBenefits.d("HOME-FRAGMENT", "PolicySelected: " + groupPolicyData.toString());
                binding.selectedPolicyText.setText("" + groupPolicyData.getPolicyNumber());

                //change the selection chips ui
                selectChip(groupPolicyData);
                setTextWithFancyAnimation(binding.selectedPolicyText, "" + groupPolicyData.getPolicyNumber());


                getEnrollmentStatus();
                //getAdminSettings();
                getClaims();
                getCoverage();

                try {
                    if (groupPolicyData.getProductCode().equalsIgnoreCase("GMC")) {
                        //show the hospital card
                        TO_SHOW_HOSPITAL = true;
                        addProviderNetwork();
                    } else {
                        TO_SHOW_HOSPITAL = false;
                        //remove the hospital card
                        removeProviderNetwork();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    TO_SHOW_HOSPITAL = true;
                }

            }


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


        //on clicked Wellness
        binding.titleMenu.rlTabWellness.setOnClickListener(view1 -> {
          /*  Intent wellnessIntent = new Intent(requireActivity(), WellnessDashBoardActivity.class);
            startActivity(wellnessIntent);
            requireActivity().finish();*/
        });

        binding.titleMenu.rlTabFitness.setOnClickListener(view1 -> {
           /* Intent fitness = new Intent(requireActivity(), FitnessDashBoardActivity.class);
            startActivity(fitness);
            requireActivity().finish();*/
        });


        hospitalNetworkViewModel.getHospitalsCountData().observe(getViewLifecycleOwner(), hospital -> {
            DashBoardModel itemToEdit = dashBoardItemList.get(1);
            if (hospital != null) {
                if (hospital.getHospitalCount() != null) {

                    if (hospital.getHospitalCount() != 0) {
                        providerNetworkModel.setDashBoardTextDescription(String.format("%s Hospitals", UtilMethods.PriceFormat(String.valueOf(hospital.getHospitalCount()))));

                        if (dashBoardItemList.get(1).getDashBoardHeader().equalsIgnoreCase(getString(R.string.provider_network))) {
                            dashBoardItemList.set(1, providerNetworkModel);
                            dashboardAdapter.notifyItemChanged(1);
                        }
                    } else {
                        providerNetworkModel.setDashBoardTextDescription("0 Hospitals");

                        if (dashBoardItemList.get(1).getDashBoardHeader().equalsIgnoreCase(getString(R.string.provider_network))) {
                            dashBoardItemList.set(1, providerNetworkModel);
                            dashboardAdapter.notifyItemChanged(1);
                        }
                    }
                } else {
                    providerNetworkModel.setDashBoardTextDescription("0 Hospitals");

                    if (dashBoardItemList.get(1).getDashBoardHeader().equalsIgnoreCase(getString(R.string.provider_network))) {
                        dashBoardItemList.set(1, providerNetworkModel);
                        dashboardAdapter.notifyItemChanged(1);
                    }

                }
                if (hospital.getHospitalCount() != null) {

                    if (hospital.getHospitalCount() != 0) {
                        providerNetworkModel.setDashBoardTextDescription(String.format("%s Hospitals", UtilMethods.PriceFormat(String.valueOf(hospital.getHospitalCount()))));

                        if (dashBoardItemList.get(1).getDashBoardHeader().equalsIgnoreCase(getString(R.string.provider_network))) {
                            dashBoardItemList.set(1, providerNetworkModel);
                            dashboardAdapter.notifyItemChanged(1);
                        }
                    } else {
                        providerNetworkModel.setDashBoardTextDescription("0 Hospitals");

                        if (dashBoardItemList.get(1).getDashBoardHeader().equalsIgnoreCase(getString(R.string.provider_network))) {
                            dashBoardItemList.set(1, providerNetworkModel);
                            dashboardAdapter.notifyItemChanged(1);
                        }
                    }
                } else {

                    providerNetworkModel.setDashBoardTextDescription("0 Hospitals");

                    if (dashBoardItemList.get(1).getDashBoardHeader().equalsIgnoreCase(getString(R.string.provider_network))) {
                        dashBoardItemList.set(1, providerNetworkModel);
                        dashboardAdapter.notifyItemChanged(1);
                    }

                }
            } else {

                providerNetworkModel.setDashBoardTextDescription("0 Hospitals");
                if (dashBoardItemList.get(1).getDashBoardHeader().equalsIgnoreCase(getString(R.string.provider_network))) {
                    dashBoardItemList.set(1, providerNetworkModel);
                    dashboardAdapter.notifyItemChanged(1);
                }
            }

        });


        //admin settings
        adminSettingViewModel.getAdminSettingData().observe(getViewLifecycleOwner(), adminSettingResponse -> {
            GroupPolicyData groupPolicyData = selectedPolicyViewModel.getSelectedPolicy().getValue();
            if (adminSettingResponse != null) {
                if (adminSettingResponse.getGroup_Admin_Basic_Settings() != null) {
                    String serverDate = adminSettingResponse.getGroup_Admin_Basic_Settings().getServerDate();
                    String endDate = adminSettingResponse.getGroup_Window_Period_Info().getOpenEnroll_WP_Information_data().getWINDOW_PERIOD_END_DATE();
                    Calendar calendarEnd = Calendar.getInstance();
                    Calendar calendarStart = Calendar.getInstance();

                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss", Locale.ENGLISH);

                    try {
                        calendarEnd.setTime(Objects.requireNonNull(sdf.parse(endDate + " 23:59:59")));
                        calendarStart.setTime(Objects.requireNonNull(sdf.parse(serverDate + " 00:00:00")));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    //end date in millis
                    long millis = calendarEnd.getTimeInMillis();

                    //current date in millis
                    long now = calendarStart.getTimeInMillis();

                    //difference of millis
                    long milisInFuture = millis - now;

                    if (milisInFuture > 0) {
                        long diff = calendarEnd.getTimeInMillis() - calendarStart.getTimeInMillis();
                        long seconds = diff / 1000;
                        long minutes = seconds / 60;
                        long hours = minutes / 60;
                        // long days = (hours / 24) + 1;
                        long days = (hours / 24);
                        LogMyBenefits.d("days", "" + days);
                        String daysCount = String.valueOf(days);


                        binding.daycount.setText(daysCount);
                        binding.daysLeft.setVisibility(View.VISIBLE);


                        binding.cardEnroll.setOnClickListener(v -> {

                            if (CARD_ENROLL_ENABLE)
                                if (adminSettingResponse.getGroup_Admin_Basic_Settings().getAppEnrollmentType() != null) {
                                    LoadSessionResponse loadSessionResponse = loadSessionViewModel.getLoadSessionData().getValue();
                                    if (adminSettingResponse.getGroup_Admin_Basic_Settings().getAppEnrollmentType() != null) {
                                        if (adminSettingResponse.getGroup_Admin_Basic_Settings().getAppEnrollmentType().equalsIgnoreCase("2")) {
                                            if (loadSessionResponse != null) {
                                                String emp_sr_no = loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().get(0).getEmployeeSrNo();
                                                //NavDirections actions = HomeFragmentDirections.actionHomeFragmentToEnrollmentWebView(BuildConfig.DOWNLOAD_BASE_URL + "HandleAppSession.ASPX?APPLOGIN=" + emp_sr_no);
                                                NavDirections actions = HomeFragmentDirections.actionHomeFragmentToEnrollmentWebView(BuildConfig.DOWNLOAD_BASE_URL + "HandleAppSession.ASPX?APPLOGIN=" + encryptionPreference.getEncryptedDataToken(BuildConfig.BEARER_TOKEN));
                                                navController.navigate(actions);
                                            } else {
                                                //  Toast.makeText(requireContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();
                                                //something went wrong
                                            }
                                        } else {
                                            if (loadSessionResponse != null) {
                                                String emp_sr_no = loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().get(0).getEmployeeSrNo();
                                                //NavDirections actions = HomeFragmentDirections.actionHomeFragmentToEnrollmentWebView(BuildConfig.DOWNLOAD_BASE_URL + "HandleAppSession.ASPX?APPLOGIN=" + emp_sr_no);
                                                NavDirections actions = HomeFragmentDirections.actionHomeFragmentToEnrollmentWebView(BuildConfig.DOWNLOAD_BASE_URL + "HandleAppSession.ASPX?APPLOGIN=" + encryptionPreference.getEncryptedDataToken(BuildConfig.BEARER_TOKEN));
                                                navController.navigate(actions);
                                            } else {
                                                //  Toast.makeText(requireContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();
                                                //something went wrong
                                            }
                                        }
                                    } else {
                                        if (loadSessionResponse != null) {
                                            String emp_sr_no = loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().get(0).getEmployeeSrNo();
                                            //NavDirections actions = HomeFragmentDirections.actionHomeFragmentToEnrollmentWebView(BuildConfig.DOWNLOAD_BASE_URL + "HandleAppSession.ASPX?APPLOGIN=" + emp_sr_no);
                                            NavDirections actions = HomeFragmentDirections.actionHomeFragmentToEnrollmentWebView(BuildConfig.DOWNLOAD_BASE_URL + "HandleAppSession.ASPX?APPLOGIN=" + encryptionPreference.getEncryptedDataToken(BuildConfig.BEARER_TOKEN));
                                            navController.navigate(actions);
                                        } else {
                                            Toast.makeText(requireContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();
                                            //something went wrong
                                        }
                                    }
                                } else {
                                    LoadSessionResponse loadSessionResponse = loadSessionViewModel.getLoadSessionData().getValue();
                                    if (loadSessionResponse != null) {
                                        String emp_sr_no = loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().get(0).getEmployeeSrNo();
                                        //NavDirections actions = HomeFragmentDirections.actionHomeFragmentToEnrollmentWebView(BuildConfig.DOWNLOAD_BASE_URL + "HandleAppSession.ASPX?APPLOGIN=" + emp_sr_no);
                                        NavDirections actions = HomeFragmentDirections.actionHomeFragmentToEnrollmentWebView(BuildConfig.DOWNLOAD_BASE_URL + "HandleAppSession.ASPX?APPLOGIN=" + encryptionPreference.getEncryptedDataToken(BuildConfig.BEARER_TOKEN));
                                        navController.navigate(actions);
                                    } else {
                                        Toast.makeText(requireContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();
                                        //something went wrong
                                    }
                                }


                        });

                    } else {
                        binding.dayCountLayout.setVisibility(View.GONE);
                        binding.daycount.setText("");
                        binding.daysLeft.setVisibility(View.GONE);
                        binding.dependantImage.setVisibility(View.VISIBLE);
                       /* binding.bottomtxt.setText("Download summary");
                        binding.enrollmentStatus.setText("CLOSED");*/
                     /*   binding.addDependantImage.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_by_download));
                        binding.addDependantImage.setVisibility(View.VISIBLE);*/
                        binding.cardEnroll.setOnClickListener(v -> {
                            if (CARD_ENROLL_ENABLE)
                                downloadSummary();
                        });
                    }
                } else {

                    binding.dayCountLayout.setVisibility(View.GONE);
                    binding.daycount.setText("");
                    binding.daysLeft.setVisibility(View.GONE);
                    //  binding.dependantImage.setVisibility(View.VISIBLE);
               /*     binding.bottomtxt.setText("");
                    binding.enrollmentStatus.setText("CLOSED");*/
                  /*  binding.addDependantImage.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_by_download));
                    binding.addDependantImage.setVisibility(View.GONE);*/

                }
            } else {
                binding.dayCountLayout.setVisibility(View.GONE);
                binding.daycount.setText("");
                binding.daysLeft.setVisibility(View.GONE);
                // binding.dependantImage.setVisibility(View.VISIBLE);
              /*  binding.bottomtxt.setText("");
                binding.enrollmentStatus.setText("CLOSED");*/
               /* binding.addDependantImage.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_by_download));
                binding.addDependantImage.setVisibility(View.GONE);*/
                binding.cardEnroll.setOnClickListener(v -> {

                });
            }

            if (groupPolicyData != null) {

                if (groupPolicyData.getProductCode().equalsIgnoreCase("GPA")) {
                    /*binding.addDependantImage.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_by_download));
                    binding.addDependantImage.setVisibility(View.GONE);
                    binding.bottomtxt.setText("");*/
                    binding.cardEnroll.setOnClickListener(v -> {
                        //do nothing
                    });
                } else if (groupPolicyData.getProductCode().equalsIgnoreCase("GTL")) {

                 /*   binding.addDependantImage.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_by_download));
                    binding.addDependantImage.setVisibility(View.GONE);
                    binding.bottomtxt.setText("");*/
                    binding.cardEnroll.setOnClickListener(v -> {

                    });
                }

            }

        });


        //get serviceName
        serviceNamesViewModel.getServices().observe(getViewLifecycleOwner(), serviceNamesResponse -> {
            if (serviceNamesResponse != null) {
                for (int i = 0; i < serviceNamesResponse.getShowButtons().size(); i++) {
                    if (serviceNamesResponse.getShowButtons().get(i).getServiceName().equalsIgnoreCase("INSURANCE")) {
                        binding.titleMenu.rlTabInsurance.setVisibility(View.VISIBLE);
                    } else if (serviceNamesResponse.getShowButtons().get(i).getServiceName().equalsIgnoreCase("WELLNESS")) {
                        binding.titleMenu.rlTabWellness.setVisibility(View.VISIBLE);
                    } else if (serviceNamesResponse.getShowButtons().get(i).getServiceName().equalsIgnoreCase("FITNESS")) {
                        binding.titleMenu.rlTabFitness.setVisibility(View.VISIBLE);
                    }
                }
            }
        });


        loadSessionViewModel.getReloginState().observe(getActivity(), relogin -> {
            if (relogin) {
                UtilMethods.RedirectToLogin(requireActivity());
            } else {
            }
        });

        binding.cardEnroll.setOnClickListener(v -> {
            if (CARD_ENROLL_ENABLE)
                downloadSummary();
        });
    }

    private void getEnrollmentStatus() {
        GroupPolicyData groupPolicyData = selectedPolicyViewModel.getSelectedPolicy().getValue();
        if (groupPolicyData != null) {

            LoadSessionResponse loadSessionResponse = loadSessionViewModel.getLoadSessionData().getValue();
            if (loadSessionResponse != null) {
                groupChildSrnNo = loadSessionResponse.getGroupInfoData().getGroupchildsrno();
                oeGrpBasInfoSrNo = groupPolicyData.getOeGrpBasInfSrNo();
                if (groupPolicyData.getProductCode().equalsIgnoreCase("GMC")) {

                    try {
                        empSrNo = loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().get(0).getEmployeeSrNo();

                    } catch (Exception e) {
                        e.printStackTrace();
                        DashBoardModel itemToEdit = new DashBoardModel();
                        itemToEdit.setDashBoardTextDescription("0 Claim");

                    }
                    myClaimsViewModel.getMyClaims(groupChildSrnNo, empSrNo);

                } else if (groupPolicyData.getProductCode().equalsIgnoreCase("GPA")) {
                    try {
                        empSrNo = loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGPAPolicyEmployeeData().get(0).getEmployeeSrNo();

                    } catch (Exception e) {
                        e.printStackTrace();
                        DashBoardModel itemToEdit = new DashBoardModel();
                        itemToEdit.setDashBoardTextDescription("0 Claim");

                    }
                } else if (groupPolicyData.getProductCode().equalsIgnoreCase("GTL")) {
                    try {
                        empSrNo = loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGTLPolicyEmployeeData().get(0).getEmployeeSrNo();

                    } catch (Exception e) {
                        e.printStackTrace();
                        DashBoardModel itemToEdit = new DashBoardModel();
                        itemToEdit.setDashBoardTextDescription("0 Claim");

                    }
                }
                enrollmentStatusViewModel.getEnrollmentStatus(empSrNo, groupChildSrnNo, oeGrpBasInfoSrNo).observe(getViewLifecycleOwner(), enrollmentStatusResponse -> {

                    if (enrollmentStatusResponse != null) {
                        LogMyBenefits.d("ENROLLMENT-STATUS", "FROM HOME FRAGMENT: " + enrollmentStatusResponse.toString());
                        if (enrollmentStatusResponse.getEnrollmentStatus() != null) {

                            if ((enrollmentStatusResponse.getEnrollmentStatus().getIsEnrollmentSaved() == 0 &&
                                    enrollmentStatusResponse.getEnrollmentStatus().getIsWindowPeriodOpen() == 0) ||
                                    (enrollmentStatusResponse.getEnrollmentStatus().getIsWindowPeriodOpen() == 9)) {
                                binding.bottomtxt.setText("");
                                binding.enrollmentStatus.setText("CLOSED");
                                binding.addDependantImage.setVisibility(View.GONE);
                                binding.dayCountLayout.setVisibility(View.GONE);
                                binding.dependantImage.setVisibility(View.VISIBLE);
                                CARD_ENROLL_ENABLE = false;
                            } else if (enrollmentStatusResponse.getEnrollmentStatus().getIsEnrollmentSaved() == 0
                                    && enrollmentStatusResponse.getEnrollmentStatus().getIsWindowPeriodOpen() == 1) {
                                binding.bottomtxt.setText("Add Dependant");
                                binding.addDependantImage.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_by_avatar_add));
                                binding.addDependantImage.setVisibility(View.VISIBLE);
                                binding.enrollmentStatus.setText("OPEN");
                                binding.dayCountLayout.setVisibility(View.VISIBLE);
                                binding.dependantImage.setVisibility(View.GONE);
                                CARD_ENROLL_ENABLE = true;


                                if (groupPolicyData.getProductCode().equalsIgnoreCase("GPA")) {

                                    binding.bottomtxt.setText("Continue Enrollment");
                                    binding.addDependantImage.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_continue_enrollment));
                                    binding.addDependantImage.setVisibility(View.VISIBLE);
                                    binding.enrollmentStatus.setText("OPEN");
                                    binding.dayCountLayout.setVisibility(View.VISIBLE);
                                    binding.dependantImage.setVisibility(View.GONE);
                                    CARD_ENROLL_ENABLE = true;

                                } else if (groupPolicyData.getProductCode().equalsIgnoreCase("GTL")) {

                                    binding.bottomtxt.setText("Continue Enrollment");
                                    binding.addDependantImage.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_continue_enrollment));
                                    binding.addDependantImage.setVisibility(View.VISIBLE);
                                    binding.enrollmentStatus.setText("OPEN");
                                    binding.dayCountLayout.setVisibility(View.VISIBLE);
                                    binding.dependantImage.setVisibility(View.GONE);
                                    CARD_ENROLL_ENABLE = true;

                                }

                                adminSettingViewModel.getAdminSetting(groupChildSrnNo, groupPolicyData.getOeGrpBasInfSrNo()).observe(getViewLifecycleOwner(), adminSettingResponse -> {
                                    if (adminSettingResponse != null) {
                                        if (adminSettingResponse.getGroup_Admin_Basic_Settings() != null) {
                                            String serverDate = adminSettingResponse.getGroup_Admin_Basic_Settings().getServerDate();
                                            String endDate = adminSettingResponse.getGroup_Window_Period_Info().getOpenEnroll_WP_Information_data().getWINDOW_PERIOD_END_DATE();
                                            Calendar calendarEnd = Calendar.getInstance();
                                            Calendar calendarStart = Calendar.getInstance();

                                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss", Locale.ENGLISH);

                                            try {
                                                calendarEnd.setTime(Objects.requireNonNull(sdf.parse(endDate + " 23:59:59")));
                                                calendarStart.setTime(Objects.requireNonNull(sdf.parse(serverDate + " 00:00:00")));
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }
                                            //end date in millis
                                            long millis = calendarEnd.getTimeInMillis();

                                            //current date in millis
                                            long now = calendarStart.getTimeInMillis();

                                            //difference of millis
                                            long milisInFuture = millis - now;

                                            if (milisInFuture > 0) {
                                                long diff = calendarEnd.getTimeInMillis() - calendarStart.getTimeInMillis();
                                                long seconds = diff / 1000;
                                                long minutes = seconds / 60;
                                                long hours = minutes / 60;
                                                // long days = (hours / 24) + 1;
                                                long days = (hours / 24);
                                                LogMyBenefits.d("days", "" + days);
                                                String daysCount = String.valueOf(days);


                                                binding.daycount.setText(daysCount);
                                                binding.daysLeft.setVisibility(View.VISIBLE);


                                                binding.cardEnroll.setOnClickListener(v -> {

                                                    if (CARD_ENROLL_ENABLE)
                                                            if (loadSessionResponse != null) {
                                                                String emp_sr_no = loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().get(0).getEmployeeSrNo();
                                                                //NavDirections actions = HomeFragmentDirections.actionHomeFragmentToEnrollmentWebView(BuildConfig.DOWNLOAD_BASE_URL + "HandleAppSession.ASPX?APPLOGIN=" + emp_sr_no);
                                                                NavDirections actions = HomeFragmentDirections.actionHomeFragmentToEnrollmentWebView(BuildConfig.DOWNLOAD_BASE_URL + "HandleAppSession.ASPX?APPLOGIN=" + encryptionPreference.getEncryptedDataToken(BuildConfig.BEARER_TOKEN));
                                                                navController.navigate(actions);
                                                            }
                                                });

                                            } else {
                                                binding.bottomtxt.setText("");
                                                binding.enrollmentStatus.setText("CLOSED");
                                                binding.addDependantImage.setVisibility(View.GONE);
                                                binding.dayCountLayout.setVisibility(View.GONE);
                                                binding.dependantImage.setVisibility(View.VISIBLE);
                                                CARD_ENROLL_ENABLE = false;

                                            }
                                        } else {

                                            binding.bottomtxt.setText("");
                                            binding.enrollmentStatus.setText("CLOSED");
                                            binding.addDependantImage.setVisibility(View.GONE);
                                            binding.dayCountLayout.setVisibility(View.GONE);
                                            binding.dependantImage.setVisibility(View.VISIBLE);
                                            CARD_ENROLL_ENABLE = false;

                                        }
                                    }
                                });


                            } else if (enrollmentStatusResponse.getEnrollmentStatus().getIsEnrollmentSaved() == 1
                                    && enrollmentStatusResponse.getEnrollmentStatus().getIsWindowPeriodOpen() == 1) {
                                binding.bottomtxt.setText("Continue Enrollment");
                                binding.addDependantImage.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_continue_enrollment));
                                binding.addDependantImage.setVisibility(View.VISIBLE);
                                binding.enrollmentStatus.setText("OPEN");
                                binding.dayCountLayout.setVisibility(View.VISIBLE);
                                binding.dependantImage.setVisibility(View.GONE);

                                adminSettingViewModel.getAdminSetting(groupChildSrnNo, groupPolicyData.getOeGrpBasInfSrNo()).observe(getViewLifecycleOwner(), adminSettingResponse -> {
                                    if (adminSettingResponse != null) {
                                        if (adminSettingResponse.getGroup_Admin_Basic_Settings() != null) {
                                            String serverDate = adminSettingResponse.getGroup_Admin_Basic_Settings().getServerDate();
                                            String endDate = adminSettingResponse.getGroup_Window_Period_Info().getOpenEnroll_WP_Information_data().getWINDOW_PERIOD_END_DATE();
                                            Calendar calendarEnd = Calendar.getInstance();
                                            Calendar calendarStart = Calendar.getInstance();

                                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss", Locale.ENGLISH);

                                            try {
                                                calendarEnd.setTime(Objects.requireNonNull(sdf.parse(endDate + " 23:59:59")));
                                                calendarStart.setTime(Objects.requireNonNull(sdf.parse(serverDate + " 00:00:00")));
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }
                                            //end date in millis
                                            long millis = calendarEnd.getTimeInMillis();

                                            //current date in millis
                                            long now = calendarStart.getTimeInMillis();

                                            //difference of millis
                                            long milisInFuture = millis - now;

                                            if (milisInFuture > 0) {
                                                long diff = calendarEnd.getTimeInMillis() - calendarStart.getTimeInMillis();
                                                long seconds = diff / 1000;
                                                long minutes = seconds / 60;
                                                long hours = minutes / 60;
                                                // long days = (hours / 24) + 1;
                                                long days = (hours / 24);
                                                LogMyBenefits.d("days", "" + days);
                                                String daysCount = String.valueOf(days);


                                                binding.daycount.setText(daysCount);
                                                binding.daysLeft.setVisibility(View.VISIBLE);


                                                binding.cardEnroll.setOnClickListener(v -> {

                                                    if (CARD_ENROLL_ENABLE)

                                                            if (loadSessionResponse != null) {
                                                                String emp_sr_no = loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().get(0).getEmployeeSrNo();
                                                                //NavDirections actions = HomeFragmentDirections.actionHomeFragmentToEnrollmentWebView(BuildConfig.DOWNLOAD_BASE_URL + "HandleAppSession.ASPX?APPLOGIN=" + emp_sr_no);
                                                                NavDirections actions = HomeFragmentDirections.actionHomeFragmentToEnrollmentWebView(BuildConfig.DOWNLOAD_BASE_URL + "HandleAppSession.ASPX?APPLOGIN=" + encryptionPreference.getEncryptedDataToken(BuildConfig.BEARER_TOKEN));
                                                                navController.navigate(actions);
                                                            }

                                                });

                                            } else {
                                                binding.bottomtxt.setText("");
                                                binding.enrollmentStatus.setText("CLOSED");
                                                binding.addDependantImage.setVisibility(View.GONE);
                                                binding.dayCountLayout.setVisibility(View.GONE);
                                                binding.dependantImage.setVisibility(View.VISIBLE);
                                                CARD_ENROLL_ENABLE = false;

                                            }
                                        } else {

                                            binding.bottomtxt.setText("");
                                            binding.enrollmentStatus.setText("CLOSED");
                                            binding.addDependantImage.setVisibility(View.GONE);
                                            binding.dayCountLayout.setVisibility(View.GONE);
                                            binding.dependantImage.setVisibility(View.VISIBLE);
                                            CARD_ENROLL_ENABLE = false;

                                        }
                                    }
                                });

                            } else if (enrollmentStatusResponse.getEnrollmentStatus().getIsEnrollmentSaved() == 1
                                    && enrollmentStatusResponse.getEnrollmentStatus().getIsWindowPeriodOpen() == 0) {
                                binding.bottomtxt.setText("Download summary");
                                CARD_ENROLL_ENABLE = true;
                                binding.addDependantImage.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_by_download));
                                binding.addDependantImage.setVisibility(View.VISIBLE);
                                binding.enrollmentStatus.setText("CLOSED");
                                binding.dayCountLayout.setVisibility(View.GONE);
                                binding.dependantImage.setVisibility(View.VISIBLE);

                                if (groupPolicyData.getProductCode().equalsIgnoreCase("GPA")) {

                                    binding.bottomtxt.setText("");
                                    binding.addDependantImage.setVisibility(View.GONE);
                                    CARD_ENROLL_ENABLE = false;

                                } else if (groupPolicyData.getProductCode().equalsIgnoreCase("GTL")) {

                                    binding.bottomtxt.setText("");
                                    binding.addDependantImage.setVisibility(View.GONE);
                                    CARD_ENROLL_ENABLE = false;

                                }
                            }


                        } else {
                            //closed state
                            binding.bottomtxt.setText("");
                            binding.enrollmentStatus.setText("CLOSED");
                            binding.addDependantImage.setVisibility(View.GONE);
                            binding.dayCountLayout.setVisibility(View.GONE);
                            CARD_ENROLL_ENABLE = false;
                        }
                    } else {
                        //closed state
                        CARD_ENROLL_ENABLE = false;
                        binding.bottomtxt.setText("");
                        binding.enrollmentStatus.setText("CLOSED");
                        binding.addDependantImage.setVisibility(View.GONE);
                    }


                });


            } else {

            }
        }

    }

    private void getAdminSettings() {
        GroupPolicyData groupPolicyData = selectedPolicyViewModel.getSelectedPolicy().getValue();
        LoadSessionResponse loadSessionResponse = loadSessionViewModel.getLoadSessionData().getValue();
        if (loadSessionResponse != null) {
            groupChildSrnNo = loadSessionResponse.getGroupInfoData().getGroupchildsrno();
            if (groupPolicyData != null) {
                adminSettingViewModel.getAdminSetting(groupChildSrnNo, groupPolicyData.getOeGrpBasInfSrNo());
            }
        }
    }


    private void getClaims() {

        GroupPolicyData groupPolicyData = selectedPolicyViewModel.getSelectedPolicy().getValue();
        if (groupPolicyData != null) {

            LoadSessionResponse loadSessionResponse = loadSessionViewModel.getLoadSessionData().getValue();
            if (loadSessionResponse != null) {
                groupChildSrnNo = loadSessionResponse.getGroupInfoData().getGroupchildsrno();
                if (groupPolicyData.getProductCode().equalsIgnoreCase("GMC")) {

                    try {
                        empSrNo = loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().get(0).getEmployeeSrNo();

                    } catch (Exception e) {
                        e.printStackTrace();
                        DashBoardModel itemToEdit = new DashBoardModel();
                        itemToEdit.setDashBoardTextDescription("0 Claim");

                    }
                    myClaimsViewModel.getMyClaims(groupChildSrnNo, empSrNo);
                }
            }
        }

        myClaimsViewModel.getMyClaimsData().observe(getViewLifecycleOwner(), claims -> {
            DashBoardModel itemToEdit = new DashBoardModel();
            if (claims != null) {
                if (claims.getClaimInformation() != null) {

                    if (claims.getClaimInformation().size() > 1) {
                        myClaimModel.setDashBoardTextDescription(String.format("%s Claims ", UtilMethods.PriceFormat(String.valueOf(claims.getClaimInformation().size()))));
                        dashboardAdapter.notifyItemChanged(2);
                    } else {
                        myClaimModel.setDashBoardTextDescription(String.format("%s Claim ", UtilMethods.PriceFormat(String.valueOf(claims.getClaimInformation().size()))));
                        dashboardAdapter.notifyItemChanged(2);
                    }
                } else {
                    myClaimModel.setDashBoardTextDescription("0 Claim");
                    dashboardAdapter.notifyItemChanged(2);
                }
            } else {

                myClaimModel.setDashBoardTextDescription("0 Claim");
                dashboardAdapter.notifyItemChanged(2);
            }
        });
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


    private List<GroupPolicyData> sort(List<GroupPolicyData> list) {

        list.sort(Comparator.comparing(GroupPolicyData::getOeGrpBasInfSrNo));

        return list;
    }


    private void exitWarning() {
        final Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.smslayout);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.nhborder));


        DisplayMetrics dm = new DisplayMetrics();

        requireActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;

        dialog.getWindow().setLayout((int) (width * .6), WindowManager.LayoutParams.WRAP_CONTENT);

        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;

        dialog.getWindow().setAttributes(params);
        AppCompatTextView lblSMS = dialog.findViewById(R.id.lblSMS);
        final AppCompatEditText smsContact = dialog.findViewById(R.id.smsContact);
        AppCompatButton btnSubmit = dialog.findViewById(R.id.btnSubmit);
        AppCompatButton btnCancel = dialog.findViewById(R.id.btnCancel);
        AppCompatTextView lblSMSHeader = dialog.findViewById(R.id.lblSMSHeader);
        lblSMS.setText(getString(R.string.my_benifits));
        lblSMSHeader.setText("Are you sure you want to Exit?");
        lblSMS.setText("Benefits You India");
        btnSubmit.setText("Exit");
        btnCancel.setText("Cancel");
        smsContact.setVisibility(View.GONE);

        btnSubmit.setOnClickListener(v -> {
            dialog.dismiss();
            requireActivity().finishAffinity();
        });

        btnCancel.setOnClickListener(v -> {
            dialog.dismiss();
        });
        dialog.show();
    }

    private void getCoverage() {
        try {
            GroupPolicyData groupPolicyData = selectedPolicyViewModel.getSelectedPolicy().getValue();
            if (groupPolicyData != null) {


                PRODUCT_CODE = groupPolicyData.getProductCode();
                oeGrpBasInfoSrNo = groupPolicyData.getOeGrpBasInfSrNo();

                //to get the  coverages data we need some parameters from load session values
                LoadSessionResponse loadSessionResponse = loadSessionViewModel.getLoadSessionData().getValue();
                if (loadSessionResponse != null) {

                    try {
                        LogMyBenefits.d("HOME-FRAGMENT", "onCreateView: " + loadSessionResponse.toString());

                        String groupChildSrvNo = loadSessionResponse.getGroupInfoData().getGroupchildsrno();
                        String employeeSrNo = "";
                        String gmc_employee_srNo = loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().get(0).getEmployeeSrNo();


                        switch (PRODUCT_CODE) {
                            case "GMC":
                                List<GroupGMCPolicyEmployeeDatum> gmcPolicy = loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData();

                                employeeSrNo = gmcPolicy.get(0).getEmployeeSrNo();
                                if (oeGrpBasInfoSrNo.equalsIgnoreCase("")) {
                                    //todo somethings wrong with the server-response.
                                }

                                // coveragesViewModel.getCoveragePolicyData(groupChildSrvNo, oeGrpBasInfoSrNo);
                                coveragesViewModel.getCoverageDetails(groupChildSrvNo, oeGrpBasInfoSrNo, PRODUCT_CODE, employeeSrNo, gmc_employee_srNo);


                                break;
                            case "GPA":
                                List<GroupGPAPolicyEmployeeDatum> gpaPolicy = loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGPAPolicyEmployeeData();

                                employeeSrNo = gpaPolicy.get(0).getEmployeeSrNo();
                                if (oeGrpBasInfoSrNo.equalsIgnoreCase("")) {
                                    //todo somethings wrong with the server-response.
                                }


                                // coveragesViewModel.getCoveragePolicyData(groupChildSrvNo, oeGrpBasInfoSrNo);
                                coveragesViewModel.getCoverageDetails(groupChildSrvNo, oeGrpBasInfoSrNo, PRODUCT_CODE, employeeSrNo, gmc_employee_srNo);


                                break;
                            case "GTL":
                                List<GroupGTLPolicyEmployeeDatum> gtlPolicy = loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGTLPolicyEmployeeData();

                                employeeSrNo = gtlPolicy.get(0).getEmployeeSrNo();
                                if (oeGrpBasInfoSrNo.equalsIgnoreCase("")) {
                                    //todo somethings wrong with the server-response.
                                }


                                // coveragesViewModel.getCoveragePolicyData(groupChildSrvNo, oeGrpBasInfoSrNo);
                                coveragesViewModel.getCoverageDetails(groupChildSrvNo, oeGrpBasInfoSrNo, PRODUCT_CODE, employeeSrNo, gmc_employee_srNo);


                                break;
                        }
                    } catch (Exception e) {
                        //error
                        coveragesViewModel.setRelationGroupData();
                        FirebaseCrashlytics crashlytics = FirebaseCrashlytics.getInstance();
                        Throwable throwable = new Throwable(e);
                        crashlytics.recordException(throwable);
                    }
                }

            }


            coveragesViewModel.getCoveragesDetailsData().observe(getViewLifecycleOwner(), coverageDetailsResponse ->

            {

                DashBoardModel itemToEdit = dashBoardItemList.get(0);
                coveragesViewModel.getRelationGroupData().observe(getViewLifecycleOwner(), relationGroup -> {
                    if (relationGroup.isEmpty() || relationGroup.isBlank()) {
                        /* setTextWithFancyAnimation(binding.txtRelations, "-");*/

                        itemToEdit.setDashBoardTextDescription("-");
                        dashBoardItemList.set(0, itemToEdit);
                        dashboardAdapter.notifyItemChanged(0);
                    } else {
                        if (relationGroup.equalsIgnoreCase("e")) {
                            /*  setTextWithFancyAnimation(binding.txtRelations, "Employee");*/
                            itemToEdit.setDashBoardTextDescription("Employee");
                            dashBoardItemList.set(0, itemToEdit);
                            dashboardAdapter.notifyItemChanged(0);
                        } else {
                            /*  setTextWithFancyAnimation(binding.txtRelations, relationGroup);*/
                            itemToEdit.setDashBoardTextDescription(relationGroup);
                            dashBoardItemList.set(0, itemToEdit);
                            dashboardAdapter.notifyItemChanged(0);
                        }

                    }
                });


            });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    //load session refresh
    private void loadSessions() {

        LogMyBenefits.d("HOME-FRAGMENT", "loadSessions: " + getLoginType());
        switch (getLoginType()) {

            case "PHONE_NUMBER":
                //this is the load session with number block
                loadSessionViewModel.loadSessionWithNumber(getPhoneNumber());
                break;
            case "EMAIL_ID":
                //this is the load session with email block
                loadSessionViewModel.loadSessionWithEmail(getEmail());
                break;
            case "AUTH_LOGIN_ID":
                //this is the load session with loginId block
                loadSessionViewModel.loadSessionWithID(getLoginID());
                break;
        }


    }


    private String getPhoneNumber() {

        String phone_number = encryptionPreference.getEncryptedDataString(AUTH_PHONE_NUMBER);
        return phone_number;
    }

    private String getLoginID() {
        String loginId = encryptionPreference.getEncryptedDataString(AUTH_LOGIN_ID);
        return loginId;
    }

    private String getLoginType() {

        String login_type = encryptionPreference.getEncryptedDataString(AUTH_LOGIN_TYPE);

        if (login_type != null) {
            return login_type;
        } else {
            return "PHONE_NUMBER";
        }
    }

    private String getEmail() {
        String phone_number = encryptionPreference.getEncryptedDataString(AUTH_EMAIL);
        return phone_number;
    }

    public static void setBadgeCount(Context context, LayerDrawable icon, String count) {

        BadgesDrawable badge;

        // Reuse drawable if possible
        Drawable reuse = icon.findDrawableByLayerId(R.id.ic_badge);
        if (reuse instanceof BadgesDrawable) {
            badge = (BadgesDrawable) reuse;
        } else {
            badge = new BadgesDrawable(context);
        }

        badge.setCount(count);
        icon.mutate();
        icon.setDrawableByLayerId(R.id.ic_badge, badge);
    }


    private void showLoading() {
        binding.progressLayout.setVisibility(View.GONE);
        binding.contentLayout.setVisibility(View.GONE);
        binding.homeShimmer.getRoot().setVisibility(View.VISIBLE);
    }

    private void hideLoading() {
        binding.progressLayout.setVisibility(View.GONE);
        binding.contentLayout.setVisibility(View.VISIBLE);
        binding.homeShimmer.getRoot().setVisibility(View.GONE);
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


    @Override
    public void onDashboardItemClicked(String menuName) {
        NavDirections actions;
        switch (menuName.toLowerCase()) {
            case "my coverages":
                actions = HomeFragmentDirections.actionHomeFragmentToCoverages();
                navController.navigate(actions);
                break;
            case "my claims":
                actions = HomeFragmentDirections.actionHomeFragmentToMyClaimsFragment();
                navController.navigate(actions);
                break;
            case "intimate\nclaim":
                actions = HomeFragmentDirections.actionHomeFragmentToClaimsFragment();
                navController.navigate(actions);
                break;
            case "provider network":
                actions = HomeFragmentDirections.actionHomeFragmentToProviderNetwork();
                navController.navigate(actions);
                break;
            case "my queries":
                actions = HomeFragmentDirections.actionHomeFragmentToMyQueriesFragment();
                navController.navigate(actions);
                break;
            case "policy\nfeatures":
                actions = HomeFragmentDirections.actionHomeFragmentToPolicyFeaturesFragment();
                navController.navigate(actions);
                break;
            case "claim\nprocedures":
                actions = HomeFragmentDirections.actionHomeFragmentToClaimsProcedureFragment();
                navController.navigate(actions);
                break;
            case "faqs":
                actions = HomeFragmentDirections.actionHomeFragmentToFaqFragment();
                navController.navigate(actions);
                break;
            case "claim data upload web":
                actions = HomeFragmentDirections.actionHomeFragmentToClaimDataUploadFragment();
                navController.navigate(actions);
            case "claim data upload app":
                actions = HomeFragmentDirections.actionHomeFragmentToClaimDataUploadFragmentApp();
                navController.navigate(actions);
        }
    }


    private void setPolicyWithChips(String code) {
        try {
            LoadSessionResponse loadSessionResponse = loadSessionViewModel.getLoadSessionData().getValue();
            if (loadSessionResponse != null) {
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
                    //
                }
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


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addProviderNetwork() {
        if (dashBoardItemList.get(1).getDashBoardHeader().equalsIgnoreCase(getString(R.string.provider_network))) {
            //we already have the provider network
        } else {
            dashBoardItemList.add(1, providerNetworkModel);
            dashboardAdapter.notifyItemInserted(1);
            dashboardAdapter.notifyItemRangeChanged(0, dashBoardItemList.size());


            if (dashBoardItemList.get(2).getDashBoardHeader().equalsIgnoreCase(getString(R.string.my_claims))) {

            } else {
                dashBoardItemList.add(2, myClaimModel);
                dashboardAdapter.notifyItemInserted(2);
                dashboardAdapter.notifyItemRangeChanged(0, dashBoardItemList.size());
            }

            if (dashBoardItemList.get(4).getDashBoardHeader().equalsIgnoreCase(getString(R.string.intimate_now))) {

            } else {

                dashBoardItemList.add(4, intimateNowModel);
                dashboardAdapter.notifyItemInserted(4);
                dashboardAdapter.notifyItemRangeChanged(0, dashBoardItemList.size());
            }

        }
    }

    private void removeProviderNetwork() {

        if (dashBoardItemList.get(1).getDashBoardHeader().equalsIgnoreCase(getString(R.string.provider_network))) {
            //we have to remove
            dashBoardItemList.remove(providerNetworkModel);
            dashboardAdapter.notifyItemRemoved(1);
            dashboardAdapter.notifyItemRangeChanged(0, dashBoardItemList.size());
        } else {
            // nothing to remove
        }

        if (dashBoardItemList.get(1).getDashBoardHeader().equalsIgnoreCase(getString(R.string.my_claims))) {
            dashBoardItemList.remove(1);
            dashboardAdapter.notifyItemRemoved(1);
            dashboardAdapter.notifyItemRangeChanged(0, dashBoardItemList.size());
        }


        if (dashBoardItemList.get(2).getDashBoardHeader().equalsIgnoreCase(getString(R.string.intimate_now))) {
            //we have to remove
            dashBoardItemList.remove(intimateNowModel);
            dashboardAdapter.notifyItemRemoved(2);
            dashboardAdapter.notifyItemRangeChanged(0, dashBoardItemList.size());
        }


    }

    private void downloadSummary() {
        loadingInsuranceDialogue.showLoading("");
        LoadSessionResponse loadSessionResponse = loadSessionViewModel.getLoadSessionData().getValue();
        if (loadSessionResponse != null) {
            String emp_sr_no = loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().get(0).getEmployeeSrNo();
            summaryViewModel.getSummary(emp_sr_no).observe(getViewLifecycleOwner(), summeryFileResponse -> {
                if (summeryFileResponse != null) {
                    if (summeryFileResponse.getStatus()) {
                        download_summaryFile(BuildConfig.FILE_DOWNLOAD_FOR_SUMMARY + summeryFileResponse.getResponseData().getFileUrl().substring(10));
                        loadingInsuranceDialogue.hideLoading();
                    } else {
                        loadingInsuranceDialogue.hideLoading();
                        showMessage();
                        // Toast.makeText(requireActivity(), "Summary file not found.", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    loadingInsuranceDialogue.hideLoading();
                    showMessage();
                    // Toast.makeText(requireActivity(), "Summary file not available.", Toast.LENGTH_SHORT).show();
                }
            });

        }

    }

    private void showMessage() {
        /*summary not available*/
        final Dialog alertDialog = new Dialog(requireActivity());
        alertDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.nhborder));

        LayoutInflater mLayoutInflater = getLayoutInflater();
        View alertLayout = mLayoutInflater.inflate(R.layout.dialog_internet_error, null);
        alertDialog.setContentView(alertLayout);
        ImageView alertIcon = alertLayout.findViewById(R.id.alertIcon);
        //alertIcon.setImageResource(R.drawable.ic_mark_as_solved);
        Button btnDismiss = alertDialog.findViewById(R.id.btnDismiss);
        btnDismiss.setOnClickListener(view -> alertDialog.dismiss());
        alertDialog.setOnDismissListener(dialogInterface -> {

        });
        TextView alertMessage = alertDialog.findViewById(R.id.tvAlertMessage);
        alertMessage.setText("            " + "Summary not available" + "            ");
        alertDialog.show();
    }


    private void download_summaryFile(String url) {
        new DownloadFile(getContext(), getActivity())
                .downloadFilePDF(System.currentTimeMillis() + ".pdf", url);
    }


    static class DownloadFile {
        Context context;
        Activity activity;
        String fileUrl, fileName;
        File file;
        String extension;


        public DownloadFile(Context context, Activity activity) {
            this.context = context;
            this.activity = activity;
            this.extension = extension;
        }

        public void downloadFilePDF(String fileName, String fileUrl) {
            //we can show the loading animation here
            //showLoading()
            ExecutorService executors = Executors.newSingleThreadExecutor();

            Handler handler = new Handler(Looper.getMainLooper());
            executors.execute(() -> {
                //runnable thread
                if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    this.fileUrl = fileUrl;
                    this.fileName = fileName.toLowerCase();

                    File folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

                    file = new File(context.getFilesDir(), this.fileName);
                    Log.d("", "downloadFilePDF: created a new File " + file.getAbsolutePath());

                } else {
                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
                }
                if (Build.VERSION.SDK_INT > 32) {
                    this.fileUrl = fileUrl;
                    this.fileName = fileName.toLowerCase();

                    File folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

                    file = new File(context.getFilesDir(), this.fileName);
                    Log.d("", "downloadFilePDF: created a new File " + file.getAbsolutePath());

                    try {
                        FileDownloader.downloadFileWithoutPermission(fileUrl, file, activity, context);
                    } catch (ActivityNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        FileDownloader.downloadFile(fileUrl, file, activity, context);
                    } catch (ActivityNotFoundException e) {
                        e.printStackTrace();
                    }
                }


            });


        }

    }

}