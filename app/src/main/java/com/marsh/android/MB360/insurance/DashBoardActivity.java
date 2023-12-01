package com.marsh.android.MB360.insurance;

import static com.marsh.android.MB360.BuildConfig.USER_AGREEMENT;
import static com.marsh.android.MB360.utilities.AppLocalConstant.AUTH_EMAIL;
import static com.marsh.android.MB360.utilities.AppLocalConstant.AUTH_LOGIN_ID;
import static com.marsh.android.MB360.utilities.AppLocalConstant.AUTH_LOGIN_TYPE;
import static com.marsh.android.MB360.utilities.AppLocalConstant.AUTH_PHONE_NUMBER;
import static com.marsh.android.MB360.utilities.AppLocalConstant.askDefaultPermissions;
import static com.marsh.android.MB360.utilities.StatusBarHelper.setWindowFlag;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.browser.customtabs.CustomTabsSession;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.marsh.android.MB360.BuildConfig;
import com.marsh.android.MB360.IIsolatedService;
import com.marsh.android.MB360.R;
import com.marsh.android.MB360.databinding.ActivityDashBoardActivityBinding;
import com.marsh.android.MB360.databinding.DeclarationPopupLayoutBinding;
import com.marsh.android.MB360.insurance.adminsetting.responseclass.AdminSettingResponse;
import com.marsh.android.MB360.insurance.adminsetting.ui.AdminSettingViewModel;
import com.marsh.android.MB360.insurance.coverages.repository.CoveragesViewModel;
import com.marsh.android.MB360.insurance.ecards.repository.EcardViewModel;
import com.marsh.android.MB360.insurance.ecards.ui.EcardDownloadHelper;
import com.marsh.android.MB360.insurance.ecards.ui.EcardFragment;
import com.marsh.android.MB360.insurance.enrollment.repository.EnrollmentViewModel;
import com.marsh.android.MB360.insurance.enrollmentstatus.EnrollmentStatusViewModel;
import com.marsh.android.MB360.insurance.hospitalnetwork.repository.HospitalNetworkViewModel;
import com.marsh.android.MB360.insurance.myclaims.repository.MyClaimsViewModel;
import com.marsh.android.MB360.insurance.queries.repository.QueryViewModel;
import com.marsh.android.MB360.insurance.repository.LoadSessionViewModel;
import com.marsh.android.MB360.insurance.repository.responseclass.GroupGMCPolicyEmpDependantsDatum;
import com.marsh.android.MB360.insurance.repository.responseclass.GroupGMCPolicyEmployeeDatum;
import com.marsh.android.MB360.insurance.repository.responseclass.LoadSessionResponse;
import com.marsh.android.MB360.insurance.repository.selectedPolicyRepo.SelectedPolicyViewModel;
import com.marsh.android.MB360.insurance.repository.selectedPolicyRepo.responseclass.GroupPolicyData;
import com.marsh.android.MB360.insurance.servicenames.ServiceNamesViewModel;
import com.marsh.android.MB360.magisk.IsolatedService;
import com.marsh.android.MB360.networkmanager.NetworkStateManager;
import com.marsh.android.MB360.notifications.AlarmScheduler;
import com.marsh.android.MB360.onboarding.SplashScreenActivity;
import com.marsh.android.MB360.onboarding.authentication.LoginActivity;
import com.marsh.android.MB360.utilities.AesNew;
import com.marsh.android.MB360.utilities.EncryptionPreference;
import com.marsh.android.MB360.utilities.FridaListener;
import com.marsh.android.MB360.utilities.LogMyBenefits;
import com.marsh.android.MB360.utilities.LogTags;
import com.marsh.android.MB360.utilities.UtilMethods;
import com.marsh.android.MB360.utilities.rootdetection.RootDetection;
import com.marsh.android.MB360.utilities.token.BearerRepository;
import com.marsh.android.MB360.utilities.webcustomtab.CustomTabActivityHelper;
import com.marsh.android.MB360.utilities.webcustomtab.WebviewFallback;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class DashBoardActivity extends AppCompatActivity implements FridaListener {


    /**
     * we are using view-binding from this activity
     **/
    ActivityDashBoardActivityBinding binding;
    View view;

    private IIsolatedService serviceBinder;
    private boolean bServiceBound;
    NavController navController;
    BottomNavigationView bottomNavigationView;

    /**
     * load-session view-model
     **/


    LoadSessionViewModel loadSessionViewModel;
    HospitalNetworkViewModel hospitalNetworkViewModel;
    MyClaimsViewModel myClaimsViewModel;
    QueryViewModel queryViewModel;
    CoveragesViewModel coveragesViewModel;
    EnrollmentViewModel enrollmentViewModel;
    SelectedPolicyViewModel selectedPolicyViewModel;
    EcardViewModel ecardViewModel;
    AdminSettingViewModel adminSettingViewModel;
    ServiceNamesViewModel serviceNamesViewModel;

    EnrollmentStatusViewModel enrollmentStatusViewModel;


    MaterialToolbar toolbar;

    EncryptionPreference encryptionPreference;
    Boolean E_CARD_TOAST = false;
    private CustomTabActivityHelper mCustomTabActivityHelper;
    private static final int INITIAL_HEIGHT_DEFAULT_PX = 1200;
    private static final int CORNER_RADIUS_MAX_DP = 8;
    private static final int CORNER_RADIUS_DEFAULT_DP = CORNER_RADIUS_MAX_DP;
    private static final int BACKGROUND_INTERACT_OFF_VALUE = 2;

    Boolean FROM_OTP_SCREEN = false;

    int OTP = 0;
    private AlertDialog.Builder mBuilder;
    private String error;


    private final Observer<Boolean> activeNetworkStateObserver = new Observer<Boolean>() {
        @Override
        public void onChanged(Boolean isConnected) {
            //here we get to that if the device is connected to internet or no
            if (isConnected) {
                binding.internetStatus.setVisibility(View.GONE);
            } else {
                binding.internetStatus.setVisibility(View.VISIBLE);
            }
        }
    };


    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp() || super.onSupportNavigateUp();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashBoardActivityBinding.inflate(getLayoutInflater());

        //statusbar
      /*  Window window_status = getWindow();
        window_status.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window_status.setStatusBarColor(Color.TRANSPARENT);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );*/


        if (getIntent().getIntExtra(BuildConfig.AUTH_OTP, 0) > OTP) {
            OTP = getIntent().getIntExtra(BuildConfig.AUTH_OTP, 0);
            FROM_OTP_SCREEN = true;
        } else {
            FROM_OTP_SCREEN = false;
        }
//encryption
        encryptionPreference = new EncryptionPreference(this);

        if (!userAgreementPopup()) {
            showDeclarationDialog();
        } else {
            //to ask the permissions at first
            askDefaultPermissions(this);
        }


        //make full transparent statusBar
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }


       /* //check for root
        RootBeer rootBeer = new RootBeer(this);
        if (rootBeer.isRooted()) {
            //we found indication of root
            Toast.makeText(this, "Device is rooted", Toast.LENGTH_SHORT).show();
            finishAffinity();
        } else {
            //we didn't find indication of root
        }*/

        Intent intent = new Intent(getApplicationContext(), IsolatedService.class);
        /*Binding to an isolated service */
        getApplicationContext().bindService(intent, mIsolatedServiceConnection, BIND_AUTO_CREATE);


        view = binding.getRoot();
        setContentView(view);
        toolbar = binding.toolbar.insuranceToolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);


        //to ask the permissions at first
        askDefaultPermissions(this);


        loadSessionViewModel = new ViewModelProvider(this).get(LoadSessionViewModel.class);
        hospitalNetworkViewModel = new ViewModelProvider(this).get(HospitalNetworkViewModel.class);
        myClaimsViewModel = new ViewModelProvider(this).get(MyClaimsViewModel.class);
        queryViewModel = new ViewModelProvider(this).get(QueryViewModel.class);
        coveragesViewModel = new ViewModelProvider(this).get(CoveragesViewModel.class);
        enrollmentViewModel = new ViewModelProvider(this).get(EnrollmentViewModel.class);
        selectedPolicyViewModel = new ViewModelProvider(this).get(SelectedPolicyViewModel.class);
        ecardViewModel = new ViewModelProvider(this).get(EcardViewModel.class);
        adminSettingViewModel = new ViewModelProvider(this).get(AdminSettingViewModel.class);
        serviceNamesViewModel = new ViewModelProvider(this).get(ServiceNamesViewModel.class);
        enrollmentStatusViewModel = new ViewModelProvider(this).get(EnrollmentStatusViewModel.class);
        mCustomTabActivityHelper = new CustomTabActivityHelper();


        //alarm
       /* calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 10) // Set the hour to 10 AM
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)*/

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 10);
        calendar.set(Calendar.MINUTE, 25);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        // addAlarm(calendar);

        NetworkStateManager.getInstance().getNetworkConnectivityStatus().observe(this, activeNetworkStateObserver);


        //this function is responsible for the api call for load session
        //Current issue (api gets called 3 times when a user comes from otp activity)
        loadSessions();


        //this function is to get the enrollment data and instructions for now
        /*  getenrollmentInstructions();*/

        bottomNavigationView = binding.bottomNavigationView;

        bottomNavigationView.setBackground(null);
        bottomNavigationView.getMenu().getItem(2).setEnabled(false);


        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);


        //to navigate
        /** to say bottom navigation that we are on home page or-else it will select the first option in menu in this case (Support) **/
        navController.navigate(R.id.homeFragment);
        //we need to remove a page as we are navigating from something in this case (same page is getting stacked)
        navController.popBackStack();


        binding.fab.setOnClickListener(view -> {
            //cause our home page is the first page
            while (navController.getBackQueue().size() > 2) {
                navController.navigateUp();
            }

        });


        navController.addOnDestinationChangedListener((navController, navDestination, bundle) -> {
            if (navDestination.getId() == R.id.homeFragment) {

                resetSelection();
                // binding.toolbar.btnBack.setVisibility(View.GONE);
                getSupportActionBar().setDisplayUseLogoEnabled(true);
                binding.toolbar.logo.setVisibility(View.VISIBLE);
                getSupportActionBar().setDisplayShowTitleEnabled(false);
                binding.toolbar.getRoot().setVisibility(View.VISIBLE);
                showBottomBar();


            } else if (navDestination.getId() == R.id.enrollmentHomeFragment || navDestination.getId() == R.id.enrollmentSummaryFragment) {
                //we don't need the toolbar in enrollment home page
                binding.toolbar.getRoot().setVisibility(View.GONE);
                //hide bottomBar
                hideBottomBar();


            } else if (navDestination.getId() == R.id.queryDetailsFragment || navDestination.getId() == R.id.myClaimsdetails || navDestination.getId() == R.id.newQueryFragment) {
                binding.toolbar.getRoot().setVisibility(View.VISIBLE);
                binding.toolbar.logo.setVisibility(View.GONE);
                getSupportActionBar().setDisplayShowTitleEnabled(true);
                hideBottomBar();
            } else {
                getSupportActionBar().setDisplayUseLogoEnabled(true);
                getSupportActionBar().setDisplayShowTitleEnabled(true);
                binding.toolbar.logo.setVisibility(View.GONE);
                showBottomBar();
            }

            if (navDestination.getId() == R.id.enrollmentWebView || navDestination.getId() == R.id.claim_data_upload_fragment_app) {
                hideBottomBar();
            }

            if (navDestination.getId() == R.id.ecardFragment) {

                navController.navigateUp();


                //e-card available

                final Dialog dialog = new Dialog(this);
                dialog.setContentView(R.layout.smslayout);
                Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.nhborder));

                AppCompatTextView lblSMS = dialog.findViewById(R.id.lblSMS);
                final AppCompatEditText smsContact = dialog.findViewById(R.id.smsContact);
                AppCompatButton btnSubmit = dialog.findViewById(R.id.btnSubmit);
                AppCompatButton btnCancel = dialog.findViewById(R.id.btnCancel);
                LinearLayout llTitle = dialog.findViewById(R.id.llTitle);
                AppCompatTextView lblSMSHeader = dialog.findViewById(R.id.lblSMSHeader);
                llTitle.setVisibility(View.VISIBLE);
                lblSMS.setText("Benefits You India");
                lblSMSHeader.setText("Do you want to download the E-card?");
                btnSubmit.setText("Yes");
                btnCancel.setText("No");
                smsContact.setVisibility(View.GONE);
                btnSubmit.setOnClickListener(v1 -> {
                    getEcard(dialog);
                });
                btnCancel.setOnClickListener(v -> {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                    dialog.dismiss();

                });
                dialog.setCancelable(false);
                dialog.show();


            }
            for (int i = 0; i < navController.getBackQueue().size(); i++) {
                if (navController.getBackQueue().get(i).getDestination().getLabel() != null && navController.getBackQueue().get(i).getDestination().getLabel().equals("Profile")) {
                    bottomNavigationView.getMenu().getItem(4).setChecked(true);

                }
            }

        });

        NavigationUI.setupActionBarWithNavController(this, navController);

        ecardViewModel.getReloginState().observe(this, relogin -> {
            if (relogin) {
                UtilMethods.RedirectToLogin(this);
            } else {
            }
        });


       /* selectedPolicyViewModel.getSelectedPolicy().observe(this, groupPolicyData -> {
            if (groupPolicyData != null) {

                LoadSessionResponse loadSessionResponse = loadSessionViewModel.getLoadSessionData().getValue();
                String empSrNo = "";
                String groupChildSrnNo = loadSessionResponse.getGroupInfoData().getGroupchildsrno();
                try {
                    if (groupPolicyData.getProductCode().equalsIgnoreCase("GMC")) {

                        empSrNo = loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().get(0).getEmployeeSrNo();
                        enrollmentStatusViewModel.getEnrollmentStatus(empSrNo, groupChildSrnNo, groupPolicyData.getOeGrpBasInfSrNo());

                    } else if (groupPolicyData.getProductCode().equalsIgnoreCase("GPA")) {

                        empSrNo = loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGPAPolicyEmployeeData().get(0).getEmployeeSrNo();
                        enrollmentStatusViewModel.getEnrollmentStatus(empSrNo, groupChildSrnNo, groupPolicyData.getOeGrpBasInfSrNo());


                    } else if (groupPolicyData.getProductCode().equalsIgnoreCase("GTL")) {


                        empSrNo = loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGTLPolicyEmployeeData().get(0).getEmployeeSrNo();
                        enrollmentStatusViewModel.getEnrollmentStatus(empSrNo, groupChildSrnNo, groupPolicyData.getOeGrpBasInfSrNo());
                    }
                } catch (Exception e) {
                    enrollmentStatusViewModel.getEnrollmentStatus("", groupChildSrnNo, groupPolicyData.getOeGrpBasInfSrNo());
                    e.printStackTrace();
                }

            }

        });*/
        try {
            String intent_screen = "";

            intent_screen = "" + getIntent().getStringExtra("SCREEN");


            if (intent_screen.equalsIgnoreCase("ECARD")) {

                final Dialog dialog = new Dialog(this);
                dialog.setContentView(R.layout.smslayout);
                Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.nhborder));

                AppCompatTextView lblSMS = dialog.findViewById(R.id.lblSMS);
                final AppCompatEditText smsContact = dialog.findViewById(R.id.smsContact);
                AppCompatButton btnSubmit = dialog.findViewById(R.id.btnSubmit);
                AppCompatButton btnCancel = dialog.findViewById(R.id.btnCancel);
                LinearLayout llTitle = dialog.findViewById(R.id.llTitle);
                AppCompatTextView lblSMSHeader = dialog.findViewById(R.id.lblSMSHeader);
                llTitle.setVisibility(View.VISIBLE);
                lblSMS.setText("Benefits You India");
                lblSMSHeader.setText("Do you want to download the E-card?");
                btnSubmit.setText("Yes");
                btnCancel.setText("No");
                smsContact.setVisibility(View.GONE);
                btnSubmit.setOnClickListener(v1 -> {
                    getEcard(dialog);
                });
                btnCancel.setOnClickListener(v -> {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                    dialog.dismiss();

                });
                dialog.setCancelable(false);
                dialog.show();
            } else {

            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private final ServiceConnection mIsolatedServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            serviceBinder = IIsolatedService.Stub.asInterface(iBinder);
            bServiceBound = true;
            LogMyBenefits.d("Service", "Service bound");
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            bServiceBound = false;
            LogMyBenefits.d("Service", "Service Unbound");
        }
    };


    private void getEcard(Dialog dialog) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        dialog.dismiss();
        GroupPolicyData groupPolicyData = selectedPolicyViewModel.getSelectedPolicy().getValue();

        LoadSessionResponse loadSessionResponse = loadSessionViewModel.getLoadSessionData().getValue();

        try {
            String employee_id_no = loadSessionResponse.getGroupPoliciesEmployees()
                    .get(0).getGroupGMCPolicyEmployeeData().get(0).getEmployeeIdentificationNo();

            String group_code = loadSessionResponse.getGroupInfoData().getGroupcode();
            String dataRequest = "<DataRequest>" +
                    "<tpacode>MEDI</tpacode>" +
                    "<employeeno>IND6025</employeeno>" +
                    "<policynumber>0239736210_00</policynumber>" +
                    "<policycommencementdt>29~10~2022</policycommencementdt>" +
                    "<policyvaliduptodt>28~10~2023</policyvaliduptodt>" +
                    "<groupcode>GSIPL</groupcode>" +
                    "</DataRequest>";

            String tpa_code = groupPolicyData.getTpaCode();
            try {
                Map<String, String> ecardOptions = new HashMap<>();

                //  for testing
                /*ecardOptions.put("tpacode", "MEDI");
                ecardOptions.put("employeeno", "IND6025");
                ecardOptions.put("policynumber", "0239736210_00");
                ecardOptions.put("policycommencementdt", "29/10/2022");
                ecardOptions.put("policyvaliduptodt", "28/10/2023");
                ecardOptions.put("groupcode", "GSIPL");*/

                if (!loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().isEmpty()) {

                    for (GroupGMCPolicyEmployeeDatum dependant : loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData()) {
                        //dependant.getEmployeeSrNo().equalsIgnoreCase("115038") && dependant.getEmployeeIdentificationNo().equalsIgnoreCase("13362")

                        if (dependant.getEmployeeSrNo().equalsIgnoreCase("115755") && dependant.getEmployeeIdentificationNo().equalsIgnoreCase("265041")) {
                            //todo: remove sujay ecard requirement
                            try {
                                StrictMode.ThreadPolicy gfgPolicy =
                                        new StrictMode.ThreadPolicy.Builder().permitAll().build();
                                StrictMode.setThreadPolicy(gfgPolicy);
                                startActivity(new DownloadFile(this, this)
                                        .downloadFilePDF("Ecard.pdf", "https://tips.vidalhealthtpa.com/EcardService/ecardpdf?policygrpseqId=98E2F8F88AAFCE8A7D14B8B7D27ED828007100650075004100580049004D0058007100480042006A0067004200500057&enrollmentNo=07C48A08B66EA1530850B5644BD5F39FA17CFEA9147E81B4D1EB6EB6CBA3A53D006B006E006800590057005A005600500074004100540076005100550059004A&templateId=7E4A74B0FFE692E7A5325283BD513F3A0076004B005800550061004A0067004800450046006400450078006D0052007A".replace("\"", "")));
                            } catch (ActivityNotFoundException e) {
                                e.printStackTrace();
                            }

                        } else {

                            //todo: remove sujay ecard requirement

                            ecardOptions.put("tpacode", AesNew.encrypt(groupPolicyData.getTpaCode(), getString(R.string.pass_phrase)));
                            ecardOptions.put("employeeno", AesNew.encrypt(employee_id_no, getString(R.string.pass_phrase)));
                            ecardOptions.put("policynumber", AesNew.encrypt(groupPolicyData.getPolicyNumber(), getString(R.string.pass_phrase)));
                            ecardOptions.put("policycommencementdt", AesNew.encrypt(groupPolicyData.getPolicyCommencementDate(), getString(R.string.pass_phrase)));
                            ecardOptions.put("policyvaliduptodt", AesNew.encrypt(groupPolicyData.getPolicyValidUpto(), getString(R.string.pass_phrase)));
                            ecardOptions.put("groupcode", AesNew.encrypt(group_code, getString(R.string.pass_phrase)));
                            ecardOptions.put("OeGrpBasInfSrNo", AesNew.encrypt(groupPolicyData.getOeGrpBasInfSrNo(), getString(R.string.pass_phrase)));

                            LogMyBenefits.d("ECARD", groupPolicyData.getTpaCode());

                            ecardViewModel.getEcard(ecardOptions).observe(this, ecardResponse -> {
                                if (ecardResponse != null) {

                                    if (ecardResponse.getMessage().getStatus()) {

                                        if (ecardResponse.getMessage().getECard().contains("/mybenefits")) {
                                            try {
                                                StrictMode.ThreadPolicy gfgPolicy =
                                                        new StrictMode.ThreadPolicy.Builder().permitAll().build();
                                                StrictMode.setThreadPolicy(gfgPolicy);
                                                startActivity(new DownloadFile(this, this)
                                                        .downloadFilePDF("Ecard.pdf", ecardResponse.getMessage()
                                                                .getECard().replace("\"", "")));
                                            } catch (ActivityNotFoundException e) {
                                                e.printStackTrace();
                                            }
                                        } else {
                                            if (ecardResponse.getMessage().getECard().contains("E-card under process")) {


                                                AdminSettingResponse adminSettingResponse = adminSettingViewModel.getAdminSettingData().getValue();
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
                                                            //ecard not available
                                                            runUIThread("You will be able to download E-card once the endorsement is complete", "Enrolment is in progress.");

                                                        } else {

                                                            runUIThread("E-card will be available soon", "   Policy issuance is in progress   ");
                                                        }
                                                    } else {
                                                        //ecard not available
                                                        runUIThread("E-card will be available soon", "   Policy issuance is in progress   ");
                                                    }

                                                }


                                            } else if (ecardResponse.getMessage().getECard().contains("No records Found")) {

                                                AdminSettingResponse adminSettingResponse = adminSettingViewModel.getAdminSettingData().getValue();
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
                                                            //ecard not available
                                                            runUIThread("You will be able to download E-card once the endorsement is complete", "Enrolment is in progress.");

                                                        } else {

                                                            runUIThread("E-card will be available soon", "   Policy issuance is in progress   ");

                                                        }
                                                    } else {
                                                        //ecard not available
                                                        runUIThread("E-card will be available soon", "   Policy issuance is in progress   ");
                                                    }

                                                }

                                            } else if (ecardResponse.getMessage().getECard().equalsIgnoreCase("")) {
                                                AdminSettingResponse adminSettingResponse = adminSettingViewModel.getAdminSettingData().getValue();
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
                                                            //ecard not available
                                                            runUIThread("You will be able to download E-card once the endorsement is complete", "Enrolment is in progress.");

                                                        } else {
                                                            //Policy issuance is in progress. Ecard will be available soon
                                                            runUIThread("E-card will be available soon", "   Policy issuance is in progress   ");

                                                        }
                                                    } else {
                                                        //ecard not available
                                                        runUIThread("E-card will be available soon", "   Policy issuance is in progress   ");
                                                    }

                                                }

                                            } else {

                                                if (ecardResponse.getMessage().getECard() == null ||
                                                        ecardResponse.getMessage().getECard().equalsIgnoreCase("NA") ||
                                                        ecardResponse.getMessage().getECard().equalsIgnoreCase("")) {

                                                    AdminSettingResponse adminSettingResponse = adminSettingViewModel.getAdminSettingData().getValue();
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
                                                                //ecard not available
                                                                runUIThread("You will be able to download E-card once the endorsement is complete", "Enrolment is in progress.");

                                                            } else {

                                                                runUIThread("E-card will be available soon", "   Policy issuance is in progress   ");

                                                            }
                                                        } else {
                                                            //ecard not available
                                                            runUIThread("E-card will be available soon", "   Policy issuance is in progress   ");
                                                        }

                                                    }

                                                } else if (ecardResponse.getMessage().getECard().contains("http")) {

                                                    if (tpa_code.equalsIgnoreCase("HITS") ||
                                                            tpa_code.equalsIgnoreCase("PHS") ||
                                                            tpa_code.equalsIgnoreCase("ERICT") ||
                                                            tpa_code.equalsIgnoreCase("VMCT") ||
                                                            tpa_code.equalsIgnoreCase("MDH") ||
                                                            tpa_code.equalsIgnoreCase("MEDI") ||
                                                            tpa_code.equalsIgnoreCase("BAJT-I") ||
                                                            tpa_code.equalsIgnoreCase("VHPL")
                                                    ) {
                                                        try {
                                                            StrictMode.ThreadPolicy gfgPolicy =
                                                                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
                                                            StrictMode.setThreadPolicy(gfgPolicy);
                                                            startActivity(new DownloadFile(this, this)
                                                                    .downloadFilePDF("Ecard.pdf", ecardResponse.getMessage().getECard().replace("\"", "")));
                                                        } catch (ActivityNotFoundException e) {
                                                            e.printStackTrace();
                                                        }
                                                    } else {
                                                        openCustomTab(ecardResponse.getMessage().getECard().replace("\"", ""));
                                                    }
                                                }

                                            }
                                        }
                                    } else {
                                        AdminSettingResponse adminSettingResponse = adminSettingViewModel.getAdminSettingData().getValue();
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
                                                    //ecard not available
                                                    runUIThread("You will be able to download E-card once the endorsement is complete", "Enrolment is in progress.");

                                                } else {

                                                    runUIThread("E-card will be available soon", "   Policy issuance is in progress   ");

                                                }
                                            } else {
                                                //ecard not available
                                                runUIThread("E-card will be available soon", "   Policy issuance is in progress   ");
                                            }

                                        } else {
                                            runUIThread("E-card will be available soon", "   Policy issuance is in progress   ");
                                        }
                                    }
                                } else {

                                    AdminSettingResponse adminSettingResponse = adminSettingViewModel.getAdminSettingData().getValue();
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
                                                //ecard not available
                                                runUIThread("You will be able to download E-card once the endorsement is complete", "Enrolment is in progress.");

                                            } else {

                                                runUIThread("E-card will be available soon", "   Policy issuance is in progress   ");

                                            }
                                        } else {
                                            //ecard not available
                                            runUIThread("E-card will be available soon", "   Policy issuance is in progress   ");
                                        }

                                    }

                                }
                            });
                        }
                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


        ecardViewModel.getLoading().observe(this, loading -> {
            if (loading) {
                binding.progressLayoutEcard.setVisibility(View.VISIBLE);
            } else {
                binding.progressLayoutEcard.setVisibility(View.GONE);
            }
        });


    }

    private void runUIThread(String errorInfo, String title) {
        error = errorInfo;

        /*E-Card error*/
        final Dialog alertDialog = new Dialog(this);
        alertDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.nhborder));

        LayoutInflater mLayoutInflater = getLayoutInflater();
        View alertLayout = mLayoutInflater.inflate(R.layout.error_pop_up, null);
        alertDialog.setContentView(alertLayout);
        Button btnDismiss = alertDialog.findViewById(R.id.btnDismiss);
        btnDismiss.setOnClickListener(view -> alertDialog.dismiss());
        alertDialog.setOnDismissListener(dialogInterface -> {

        });

        TextView alertMessage = alertDialog.findViewById(R.id.tvAlertMessage);
        TextView alertMessageTitle = alertDialog.findViewById(R.id.tvAlertMessageTitle);
        alertMessage.setText("" + errorInfo + "");
        alertMessageTitle.setText("" + title.replace(".", "") + "");
        alertDialog.show();

    }

    private void getEcardDownload() {
        String empID = "";
        String GrpCode = "";
        String oe_grp_bas_inf_sr_no = "";
        String URL = BuildConfig.BASE_URL + "/mybenefits/documents/mybenefitsdata/02_PROCESSED_DATA/";
        String Params = GrpCode + "/05_E-CARDS/" + oe_grp_bas_inf_sr_no + "/" + empID + ".pdf";
        this.startActivity(new EcardFragment.DownloadFile(this, this).downloadFilePDF("ecard.pdf", URL + Params));
    }

    private void openCustomTab(String url) {


        // Uses the established session to build a PCCT intent.
        CustomTabsSession session = mCustomTabActivityHelper.getSession();
        CustomTabsIntent.Builder intentBuilder =
                new CustomTabsIntent.Builder(session)
                        .setToolbarColor(ContextCompat.getColor(this, R.color.icon_color))
                        .setUrlBarHidingEnabled(true)
                        .setShowTitle(true)
                        .setStartAnimations(this, R.anim.slide_in_from_right, R.anim.slide_out_to_left);

        int resizeBehavior = CustomTabsIntent.ACTIVITY_HEIGHT_DEFAULT;

        intentBuilder.setInitialActivityHeightPx(INITIAL_HEIGHT_DEFAULT_PX, resizeBehavior);
        int toolbarCornerRadiusDp = CORNER_RADIUS_DEFAULT_DP;
        intentBuilder.setToolbarCornerRadiusDp(toolbarCornerRadiusDp);

        CustomTabsIntent customTabsIntent = intentBuilder.build();


        customTabsIntent.intent.putExtra(
                "androidx.browser.customtabs.extra.INITIAL_ACTIVITY_HEIGHT_IN_PIXEL",
                INITIAL_HEIGHT_DEFAULT_PX);
        int toolbarCornerRadiusPx =
                Math.round(toolbarCornerRadiusDp * getResources().getDisplayMetrics().density);
        customTabsIntent.intent.putExtra(
                "androidx.browser.customtabs.extra.TOOLBAR_CORNER_RADIUS_IN_PIXEL",
                toolbarCornerRadiusPx);
        if (resizeBehavior != CustomTabsIntent.ACTIVITY_HEIGHT_DEFAULT) {
            customTabsIntent.intent.putExtra(
                    CustomTabsIntent.EXTRA_ACTIVITY_RESIZE_BEHAVIOR, resizeBehavior);
        }

        customTabsIntent.intent.putExtra(
                "androix.browser.customtabs.extra.ENABLE_BACKGROUND_INTERACTION",
                BACKGROUND_INTERACT_OFF_VALUE);


        CustomTabActivityHelper.openCustomTab(
                this, customTabsIntent, Uri.parse(url), new WebviewFallback());
    }

    private void hideBottomBar() {
        binding.bottomNavHolder.setVisibility(View.GONE);
    }


    private void showBottomBar() {
        binding.bottomNavHolder.setVisibility(View.VISIBLE);
    }


   /* private void getenrollmentInstructions() {
        enrollmentViewModel.getInstructions();
    }*/


    private void loadSessions() {

        LogMyBenefits.d(LogTags.LOGIN_ACTIVITY, "loadSessions: " + getLoginType());
        switch (getLoginType()) {

            case "PHONE_NUMBER":
                //this is the load session with number block
                if (Boolean.FALSE.equals(loadSessionViewModel.getLoadingState().getValue()) && Boolean.FALSE.equals(loadSessionViewModel.getErrorState().getValue())) {
                    LoadSessionResponse loadSessionResponse = loadSessionViewModel.getLoadSessionData().getValue();
                    assert loadSessionResponse != null;
                    LogMyBenefits.d(LogTags.LOAD_SESSIONS, "FROM ACTIVITY " + loadSessionResponse);

                } else if (Boolean.TRUE.equals(loadSessionViewModel.getErrorState().getValue())) {
                    LogMyBenefits.d(LogTags.LOAD_SESSIONS, "Error From Activity");
                } else if (Boolean.TRUE.equals(loadSessionViewModel.getLoadingState().getValue())) {
                    LogMyBenefits.d(LogTags.LOAD_SESSIONS, "onCreate: loading");

                    if (FROM_OTP_SCREEN) {
                        loadSessionViewModel.loadSessionWithNumber(getPhoneNumber(), String.valueOf(OTP));
                    } else {
                        loadSessionViewModel.loadSessionWithNumber(getPhoneNumber());
                    }
                } else if (loadSessionViewModel.getLoadSessionData() == null) {
                    LogMyBenefits.d(LogTags.LOAD_SESSIONS, "onCreate: LOADING....");
                    loadSessionViewModel.loadSessionWithNumber(getPhoneNumber());
                }
                break;
            case "EMAIL_ID":
                //this is the load session with email block
                if (Boolean.FALSE.equals(loadSessionViewModel.getLoadingState().getValue()) && Boolean.FALSE.equals(loadSessionViewModel.getErrorState().getValue())) {
                    LoadSessionResponse loadSessionResponse = loadSessionViewModel.getLoadSessionData().getValue();
                    assert loadSessionResponse != null;
                    LogMyBenefits.d(LogTags.LOAD_SESSIONS, "FROM ACTIVITY " + loadSessionResponse);

                } else if (Boolean.TRUE.equals(loadSessionViewModel.getErrorState().getValue())) {
                    LogMyBenefits.d(LogTags.LOAD_SESSIONS, "Error From Activity");
                } else if (Boolean.TRUE.equals(loadSessionViewModel.getLoadingState().getValue())) {
                    LogMyBenefits.d(LogTags.LOAD_SESSIONS, "onCreate: loading");

                    if (FROM_OTP_SCREEN) {
                        loadSessionViewModel.loadSessionWithEmail(getEmail(), String.valueOf(OTP));
                    } else {
                        loadSessionViewModel.loadSessionWithEmail(getEmail());
                    }
                } else if (loadSessionViewModel.getLoadSessionData() == null) {
                    LogMyBenefits.d(LogTags.LOAD_SESSIONS, "onCreate: LOADING....");
                    loadSessionViewModel.loadSessionWithEmail(getEmail());
                }
                break;
            case "AUTH_LOGIN_ID":
                //this is the load session with loginId block
                if (Boolean.FALSE.equals(loadSessionViewModel.getLoadingState().getValue()) && Boolean.FALSE.equals(loadSessionViewModel.getErrorState().getValue())) {
                    LoadSessionResponse loadSessionResponse = loadSessionViewModel.getLoadSessionData().getValue();
                    assert loadSessionResponse != null;
                    LogMyBenefits.d(LogTags.LOAD_SESSIONS, "FROM ACTIVITY " + loadSessionResponse);

                } else if (Boolean.TRUE.equals(loadSessionViewModel.getErrorState().getValue())) {
                    LogMyBenefits.d(LogTags.LOAD_SESSIONS, "Error From Activity");
                } else if (Boolean.TRUE.equals(loadSessionViewModel.getLoadingState().getValue())) {
                    LogMyBenefits.d(LogTags.LOAD_SESSIONS, "onCreate: loading");

                    loadSessionViewModel.loadSessionWithID(getLoginID());
                } else if (loadSessionViewModel.getLoadSessionData() == null) {
                    LogMyBenefits.d(LogTags.LOAD_SESSIONS, "onCreate: LOADING....");
                    loadSessionViewModel.loadSessionWithID(getLoginID());
                }
                break;
        }


        loadSessionViewModel.getLoadSessionData().observe(this, loadSessionResponse -> {
            String groupChildSrNo = null;
            try {
                String empSrNo = "";
                String personSrNo = "";
                String empId = "";

                if (encryptionPreference.getEncryptedDataToken(BuildConfig.TOKEN_EMP_SR_NO).isEmpty() ||
                        encryptionPreference.getEncryptedDataToken(BuildConfig.TOKEN_PERSON_SR_NO).isEmpty() ||
                        encryptionPreference.getEncryptedDataToken(BuildConfig.TOKEN_EMP_ID_NO).isEmpty()
                ) {
                    try {
                        empSrNo = loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().get(0).getEmployeeSrNo();

                    } catch (Exception e) {
                        e.printStackTrace();

                    }
                    for (GroupGMCPolicyEmpDependantsDatum person : loadSessionResponse.getGroupPoliciesEmployeesDependants().get(0).getGroupGMCPolicyEmpDependantsData()
                    ) {
                        if (person.getRelationid().equalsIgnoreCase("17")) {
                            personSrNo = person.getPersonSrNo();
                        }

                    }
                    empId = loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().get(0).getEmployeeIdentificationNo();


                    // Create an instance of BearerRepository
                    BearerRepository bearerRepository = new BearerRepository(getApplication());

                    // Start refreshing the token
                    try {
                        /*bearerRepository.startRefreshingToken(AesEncryption.encrypt(empSrNo),
                                AesEncryption.encrypt(personSrNo),
                                AesEncryption.encrypt(empId));*/
                    } catch (Exception e) {
                        LogMyBenefits.e("REFRESH-TOKEN", "TOKEN : error", e);
                        e.printStackTrace();
                    }


                } else {
                    empSrNo = encryptionPreference.getEncryptedDataToken(BuildConfig.TOKEN_EMP_SR_NO);
                    personSrNo = encryptionPreference.getEncryptedDataToken(BuildConfig.TOKEN_PERSON_SR_NO);
                    empId = encryptionPreference.getEncryptedDataToken(BuildConfig.TOKEN_EMP_ID_NO);


                    // Create an instance of BearerRepository
                    BearerRepository bearerRepository = new BearerRepository(getApplication());

                    // Start refreshing the token
                    try {
                        /*bearerRepository.startRefreshingToken(AesEncryption.encrypt(encryptionPreference.getEncryptedDataToken(BuildConfig.TOKEN_EMP_SR_NO)),
                                AesEncryption.encrypt(encryptionPreference.getEncryptedDataToken(BuildConfig.TOKEN_PERSON_SR_NO)),
                                AesEncryption.encrypt(encryptionPreference.getEncryptedDataToken(BuildConfig.TOKEN_EMP_ID_NO)));*/
                    } catch (Exception e) {
                        LogMyBenefits.e("REFRESH-TOKEN", "TOKEN : error", e);
                        e.printStackTrace();
                    }

                }

                encryptionPreference.setEncryptedDataString(BuildConfig.TOKEN_EMP_SR_NO, empSrNo);
                encryptionPreference.setEncryptedDataString(BuildConfig.TOKEN_PERSON_SR_NO, personSrNo);
                encryptionPreference.setEncryptedDataString(BuildConfig.TOKEN_EMP_ID_NO, empId);

                //queryViewModel.getQueries(empSrNo);

                try {
                    groupChildSrNo = loadSessionResponse.getGroupInfoData().getGroupchildsrno();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String oeGrpBasInfoSrNo = "";


                for (GroupPolicyData policy : loadSessionResponse.getGroupPolicies().get(0).getGroupGMCPoliciesData()) {
                    if (policy.getPolicyType().equalsIgnoreCase("base")) {
                        oeGrpBasInfoSrNo = policy.getOeGrpBasInfSrNo();
                    }
                }

                //todo change the parameters

                //String dataRequest = "<DataRequest>" + "<groupchildsrno>1224</groupchildsrno>" + "<oegrpbasinfsrno>1356</oegrpbasinfsrno>" + "<hospitalsearchtext>ALL</hospitalsearchtext>" + "</DataRequest>";
                String dataRequest = "<DataRequest>" + "<groupchildsrno>" + groupChildSrNo + "</groupchildsrno>" + "<oegrpbasinfsrno>" + oeGrpBasInfoSrNo + "</oegrpbasinfsrno>" + "<hospitalsearchtext>ALL</hospitalsearchtext>" + "</DataRequest>";

                //TODO call networkProvider
                hospitalNetworkViewModel.getHospitals(groupChildSrNo, oeGrpBasInfoSrNo, "ALL");
                // hospitalNetworkViewModel.getHospitalsCount("1224", "1356");
                hospitalNetworkViewModel.getHospitalsCount(groupChildSrNo, oeGrpBasInfoSrNo, "ALL");

                //call Service Names show hide button
                // serviceNamesViewModel.getAdminSetting(groupChildSrNo);

                //call AdminSetting API
                adminSettingViewModel.getAdminSetting(groupChildSrNo, oeGrpBasInfoSrNo);


                //myclaims
                String dataRequestMyClaims = "<DataRequest>" + "<groupchildsrno>" + groupChildSrNo + "</groupchildsrno>" + "<employeesrno>" + empSrNo + "</employeesrno>" + "</DataRequest>";
                /*String dataRequestMyClaims = "<DataRequest>" +
                        "<groupchildsrno>1090</groupchildsrno>" +
                        "<employeesrno>109492</employeesrno>" +
                        "</DataRequest>";*/
                myClaimsViewModel.getMyClaims(groupChildSrNo, empSrNo);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        loadSessionViewModel.getReloginState().observe(this, relogin -> {
            if (relogin) {
                UtilMethods.RedirectToLogin(this);
            } else {
                //everything is working
            }
        });

    }

    private boolean userAgreementPopup() {

        try {
            return encryptionPreference.getEncryptedDataBoolUSERAGREEMENT(USER_AGREEMENT);
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }


    private void logout() {

        // Store null values to SharedPreferences for logging out a user
        encryptionPreference.setEncryptedDataString(AUTH_LOGIN_ID, null);
        encryptionPreference.setEncryptedDataString(AUTH_EMAIL, null);
        encryptionPreference.setEncryptedDataString(AUTH_PHONE_NUMBER, null);
        encryptionPreference.setEncryptedDataString(AUTH_LOGIN_TYPE, null);


        Intent logoutIntent = new Intent(this, SplashScreenActivity.class);
        startActivity(logoutIntent);
        finish();


    }


    private void resetSelection() {
        bottomNavigationView.getMenu().setGroupCheckable(0, true, false);
        for (int i = 0, size = bottomNavigationView.getMenu().size(); i < size; i++) {
            bottomNavigationView.getMenu().getItem(i).setChecked(false);
            bottomNavigationView.getMenu().getItem(i).setVisible(true);
        }
        bottomNavigationView.getMenu().setGroupCheckable(0, true, true);
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
        LogMyBenefits.d("DEBUG", "getLoginType: " + login_type);
        if (login_type != null) {
            return login_type;
        } else {
            return "PHONE_NUMBER";
        }
    }

    private String getEmail() {

        String email = encryptionPreference.getEncryptedDataString(AUTH_EMAIL);
        LogMyBenefits.d("DEBUG", "getLoginType: " + email);
        return email;
    }


    @SuppressLint("ResourceAsColor")
    private void showDeclarationDialog() {
        final Dialog dialog = new Dialog(this);

        DeclarationPopupLayoutBinding binding = DeclarationPopupLayoutBinding.inflate(LayoutInflater.from(this));
        dialog.setContentView(binding.getRoot());
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();

        Rect displayRectangle = new Rect();

        Window window = this.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        dialog.getWindow().setLayout((int) (displayRectangle.width() *
                0.9f), dialog.getWindow().getAttributes().height);
        dialog.setCancelable(false);

        binding.term.setOnClickListener(v -> {
            String url = "https://employee.benefitsyou.com/mybenefits/documents/templates/Benefits%20You%20India-%20Terms%20of%20Use.pdf";
            this.startActivity(new DownloadFile(this, this).downloadFilePDF("TermsOfUse.pdf", url));
        });
        binding.privacy.setOnClickListener(v -> {
            String url = "https://www.marsh.com/us/privacy-notice.html";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            openCustomTab(url);

        });
        binding.disclaimer.setOnClickListener(v -> {
           /* String url = "";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            openCustomTab(url);*/
            //startActivity(i);
        });

        binding.buttonContinue.setBackground(binding.buttonContinue.getContext().getDrawable(R.drawable.background_effect_continue));
        binding.checkBox.setOnClickListener(v -> {
            if (binding.checkBox.isChecked()) {
                binding.buttonContinue.setBackground(binding.buttonContinue.getContext().getDrawable(R.drawable.continue_button_clicked));
            } else {
                binding.buttonContinue.setBackground(binding.buttonContinue.getContext().getDrawable(R.drawable.background_effect_continue));
            }
        });

        binding.cancel.setOnClickListener(v -> {
           UtilMethods.RedirectToLogin(this);

        });

        binding.buttonContinue.setOnClickListener(v -> {
            if (binding.checkBox.isChecked()) {
                SharedPreferences settings = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();
                // Store false value to say that user already saw the walk-through to SharedPreference
                encryptionPreference.setEncryptedBoolString(USER_AGREEMENT, true);
                editor.putBoolean(USER_AGREEMENT, true);
                editor.commit();
                dialog.dismiss();
                //to ask the permissions at first
                askDefaultPermissions(this);


            } else {
                Toast.makeText(this, "Please select the checkbox", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (RootDetection.isRooted(this, this, false, this)) {
            // Toast.makeText(this, "Root or in-appropriate app environment detected.", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            //nothing
        }
    }

    @Override
    public void onFridaDetection(boolean result) {
        if (result) {
            Toast.makeText(this, "inappropriate environment found", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public static class DownloadFile {
        Context context;
        Activity activity;
        String fileUrl, fileName;
        File file;
        String extension;
        EcardDownloadHelper ecradDownloadHelper;
        int position;


        public DownloadFile(Context context, Activity activity) {
            this.context = context;
            this.activity = activity;

        }

        public Intent downloadFilePDF(String fileName, String fileUrl) {
            //we can show the loading animation here
            //showLoading()
            ExecutorService executors = Executors.newSingleThreadExecutor();

            Future<Intent> future = executors.submit(() -> {
                //runnable thread
                if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {


                    File folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

                    file = new File(context.getFilesDir(), fileName);
                    Log.d("", "downloadFilePDF: created a new File " + file.getAbsolutePath());

                } else {
                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
                }
                if (Build.VERSION.SDK_INT > 32) {

                    File folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

                    file = new File(context.getFilesDir(), fileName);
                    Log.d("", "downloadFilePDF: created a new File " + file.getAbsolutePath());

                    try {
                        //ecradDownloadHelper.onFinishDownload(position);
                        return FileDownloaderAll.downloadFileWithoutPermission(fileUrl, file, activity, context);

                    } catch (ActivityNotFoundException e) {
                        e.printStackTrace();
                        return null;
                    }
                } else {
                    try {
                        //ecradDownloadHelper.onFinishDownload(position);
                        return FileDownloaderAll.downloadFile(fileUrl, file, activity, context);

                    } catch (ActivityNotFoundException e) {
                        e.printStackTrace();
                        return null;
                    }
                }


            });


            try {
                return future.get();

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

        }

    }


    public void addAlarm(Calendar calendar) {

        AlarmScheduler alarmScheduler = new AlarmScheduler();
        alarmScheduler.scheduleDailyAlarm(this, calendar);


    }


}