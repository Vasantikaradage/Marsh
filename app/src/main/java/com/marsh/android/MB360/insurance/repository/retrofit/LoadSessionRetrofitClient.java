package com.marsh.android.MB360.insurance.repository.retrofit;


import static com.marsh.android.MB360.BuildConfig.BASE_URL;

import android.content.Context;

import com.marsh.android.MB360.BuildConfig;
import com.marsh.android.MB360.utilities.EncryptionPreference;
import com.marsh.android.MB360.utilities.LogMyBenefits;

import java.util.concurrent.TimeUnit;

import okhttp3.CertificatePinner;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoadSessionRetrofitClient {

    private static LoadSessionRetrofitClient instance;
    private final LoadSessionApi loadSessionApi;
    private final EncryptionPreference encryptionPreference;


    private LoadSessionRetrofitClient(Context context) {//constructor
        //getting the token
        encryptionPreference = new EncryptionPreference(context);

        //ssl pinning
        CertificatePinner certPinner = new CertificatePinner.Builder()
                .add(BuildConfig.DOMAIN_STAR,
                        BuildConfig.CERT_256).build();

        OkHttpClient client = new OkHttpClient.Builder()
                .certificatePinner(certPinner)
                .addInterceptor(chain -> {
                    Request originalRequest = chain.request();

                    Request.Builder builder = null;
                    try {
                        builder = originalRequest.newBuilder()
                                .addHeader("Content-Type", "application/json")
                                .header("Authorization", "Bearer " + encryptionPreference.getEncryptedDataToken(BuildConfig.BEARER_TOKEN));
                        LogMyBenefits.d("==TOKEN==", encryptionPreference.getEncryptedDataToken(BuildConfig.BEARER_TOKEN));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Request newRequest = builder.build();
                    LogMyBenefits.d("LOAD-SESSIONS", newRequest.url().toString());

                    return chain.proceed(newRequest);
                }).readTimeout(12, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build();

        //retrofit object
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        loadSessionApi = retrofit.create(LoadSessionApi.class);

    }

    public static synchronized LoadSessionRetrofitClient getInstance(Context context) {
        if (instance == null) {
            instance = new LoadSessionRetrofitClient(context);
        }
        return instance;

    }

    public LoadSessionApi getLoadSessionApi() {
        return loadSessionApi;
    }

}