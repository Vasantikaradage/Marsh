package com.marsh.android.MB360.utilities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.DataType;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class AppLocalConstant {


    /**
     * in-app update config
     */
    public static final int UPDATE_CODE = 1806;

    /**
     * Comma separated Pattern.
     */
    public static final String NUMBER_FORMAT = "##,##,##0";

    /**
     * all the Strings are considered as keys for sharedPreferences
     **/
    //key for login
    public static String AUTH_PHONE_NUMBER = "AUTH_PHONE_NUMBER";
    public static String AUTH_EMAIL = "AUTH_EMAIL";
    public static String AUTH_LOGIN_ID = "AUTH_LOGIN_ID";
    public static String AUTH_FIRST_TIME_USER = "AUTH_FIRST_TIME_USER";
    public static final String USER_AGREEMENT = "USER_AGREEMENT";


    /* BASIC KEYS FOR CHECK */
    public static String AUTH_LOGIN_TYPE = "AUTH_LOGIN_TYPE";

    /* MAPS KEY */
    // public static final String MAPS_KEY = BuildConfig.MAPS_API_KEY_V3;

    //default Permission request
    private static final int REQUEST_DEFAULTS = 100;

    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    //DATE OF BIRTH CONSTANT
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
    //DATE OF BIRTH CONSTANT (DIGITS MONTH)
    public static final SimpleDateFormat DATE_FORMAT_DIGIT_MONTH = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);


    //fitness google sign-in REQUEST CODE
    public static final int REQUEST_GOOGLE_FITNESS = 2404;


    private static final String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    //location permissions
    private static final int REQUEST_LOCATION = 2;

    //Fitness request Code
    private static final int REQUEST_FITNESS = 3;

    //Notification request code
    private static final int REQUEST_NOTIFICATIONS = 1806;

    private static final String[] PERMISSIONS_LOCATION = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
    };

    private static final String[] PERMISSIONS_FITNESS = {
            Manifest.permission.ACTIVITY_RECOGNITION
    };

    private static final String[] PERMISSIONS_NOTIFICATIONS = {
            Manifest.permission.POST_NOTIFICATIONS
    };

    /**
     * Checks if the app has permission to write to device storage
     * <p>
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (ContextCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(activity,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }


    /**
     * Checks if the app has permission to write to Location service
     * <p>
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */

    public static void verifyLocationPermissions(Activity activity) {
        // Check if we have location permissions
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION);
        int fine_permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION);

        if (permission != PackageManager.PERMISSION_GRANTED || fine_permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_LOCATION,
                    REQUEST_LOCATION
            );
        }
    }

    /**
     * Checks if the app has permission to check that activity is recognized
     * <p>
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public static void verifyFitnessPermissions(Activity activity) {
        // Check if we have activity recognition permissions
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACTIVITY_RECOGNITION);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We have to request permission prompt the user
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ActivityCompat.requestPermissions(
                        activity,
                        PERMISSIONS_FITNESS,
                        REQUEST_FITNESS
                );
            }


        }
    }

    public static void askDefaultPermissions(Activity activity) {
        String[] PERMISSIONS = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACTIVITY_RECOGNITION,
                Manifest.permission.POST_NOTIFICATIONS
        };

        ActivityCompat.requestPermissions(
                activity,
                PERMISSIONS,
                REQUEST_DEFAULTS
        );

    }

    public static void askNotificationPermission(Activity activity) {

        ActivityCompat.requestPermissions(activity, PERMISSIONS_NOTIFICATIONS, REQUEST_NOTIFICATIONS);


    }


    public static void requestGoogleFitness(Activity activity, Context context) {
        FitnessOptions fitnessOptions =
                FitnessOptions.builder()
                        .addDataType(DataType.TYPE_ACTIVITY_SEGMENT,
                                FitnessOptions.ACCESS_READ)
                        .addDataType(DataType.TYPE_HEART_RATE_BPM,
                                FitnessOptions.ACCESS_READ)
                        .addDataType(DataType.TYPE_STEP_COUNT_DELTA,
                                FitnessOptions.ACCESS_READ)
                        .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA,
                                FitnessOptions.ACCESS_READ)
                        .build();

        GoogleSignIn.requestPermissions(
                activity,
                REQUEST_GOOGLE_FITNESS,
                GoogleSignIn.getLastSignedInAccount(context),
                fitnessOptions);
    }
}