package com.services.joshi.serviceprovider.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    //    private static final String AUTH = "Basic " + Base64.encodeToString(("JigneshJoshi:Jbrothers").getBytes(), Base64.NO_WRAP);
//    private static final String BASE_URL = "http://192.168.43.56/ServiceProviderAPI/public/";
//    private static final String BASE_URL = "https://jbrothers.000webhostapp.com/public/";
    private static final String BASE_URL = "http://192.168.42.91:8080/api/";
//    private static final String BASE_URL = "http://192.168.43.56:8080/api/";

    private static RetrofitClient mInstance;
    private Retrofit retrofit;

    private RetrofitClient() {

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();


        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }



    public static synchronized RetrofitClient getInstance() {
        if (mInstance == null) {
            mInstance = new RetrofitClient();
        }
        return mInstance;
    }

    public Api getApi() {
        return retrofit.create(Api.class);
    }
}
