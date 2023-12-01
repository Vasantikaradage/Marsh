package com.marsh.android.MB360.onboarding.authentication;

import com.marsh.android.MB360.BuildConfig;
import com.marsh.android.MB360.utilities.LogMyBenefits;
import com.marsh.android.MB360.utilities.LogTags;

import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginClient {

    private static LoginClient instance;
    private final LoginApi loginApi;

    private LoginClient() { //constructor

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request originalRequest = chain.request();

                    Request.Builder builder = originalRequest.newBuilder()
                            .addHeader("Content-Type", "application/json")
                            .header("Authorization",
                                    Credentials.basic(BuildConfig.BASIC_AUTH_USERNAME, BuildConfig.BASIC_AUTH_PASSWORD));

                    Request newRequest = builder.build();
                    LogMyBenefits.d(LogTags.LOGIN_ACTIVITY, newRequest.url().toString());
                    return chain.proceed(newRequest);
                }).build();


        //retrofit object
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        loginApi = retrofit.create(LoginApi.class);
    }

    public static synchronized LoginClient getInstance() {
        if (instance == null) {
            instance = new LoginClient();
        }
        return instance;

    }

    public LoginApi getLoginApi() {
        return loginApi;
    }
}
