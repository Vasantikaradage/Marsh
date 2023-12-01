package com.marsh.android.MB360.insurance.servicenames.retrofit;

import static com.marsh.android.MB360.BuildConfig.BASE_URL;

import android.content.Context;

import com.marsh.android.MB360.BuildConfig;
import com.marsh.android.MB360.utilities.EncryptionPreference;
import com.marsh.android.MB360.utilities.LogMyBenefits;

import okhttp3.CertificatePinner;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceNamesRetrofitClient {
    private static ServiceNamesRetrofitClient instance;
    private final ServiceNamesApi serviceNamesApi;
    private final EncryptionPreference encryptionPreference;


    private ServiceNamesRetrofitClient(Context context) {//constructor
        //getting the token
        encryptionPreference = new EncryptionPreference(context);

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

                               /* .header("Authorization",
                                        Credentials.basic(AesEncryption.encrypt(BASIC_AUTH_USERNAME), AesEncryption.encrypt(BASIC_AUTH_PASSWORD)));
                  */
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Request newRequest = builder.build();
                    LogMyBenefits.d("LOAD-SESSIONS", newRequest.url().toString());

                    return chain.proceed(newRequest);
                }).build();

        //retrofit object
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        serviceNamesApi = retrofit.create(ServiceNamesApi.class);

    }

    public static synchronized ServiceNamesRetrofitClient getInstance(Context context) {
        if (instance == null) {
            instance = new ServiceNamesRetrofitClient(context);
        }
        return instance;

    }

    public ServiceNamesApi getServiceNamesApi() {
        return serviceNamesApi;
    }

}

