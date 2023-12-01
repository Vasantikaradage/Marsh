package com.marsh.android.MB360.insurance.enrollment.repository.retrofit;


import com.marsh.android.MB360.BuildConfig;
import com.marsh.android.MB360.utilities.LogMyBenefits;
import com.marsh.android.MB360.utilities.LogTags;

import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EnrollmentRetrofitClient {

    private static EnrollmentRetrofitClient instance;


    EnrollmentApi enrollmentApi;

    public EnrollmentRetrofitClient() {//construction

        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(chain -> {
            Request originalRequest = chain.request();

            Request.Builder builder = originalRequest.newBuilder()
                    .addHeader("Content-Type", "application/json")
                    .header("Authorization",
                            Credentials.basic(BuildConfig.BASIC_AUTH_USERNAME,
                                    BuildConfig.BASIC_AUTH_PASSWORD));


            Request newRequest = builder.build();
            LogMyBenefits.d(LogTags.ENROLLMENT, newRequest.url().toString());

            return chain.proceed(newRequest);
        }).build();

        ///retrofit object
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BuildConfig.BASE_URL).addConverterFactory(GsonConverterFactory.create()).client(client).build();

        enrollmentApi = retrofit.create(EnrollmentApi.class);


    }

    public static synchronized EnrollmentRetrofitClient getInstance() {
        if (instance == null) {
            instance = new EnrollmentRetrofitClient();
        }
        return instance;
    }

    public EnrollmentApi getEnrollmentApi() {
        return enrollmentApi;
    }
}
