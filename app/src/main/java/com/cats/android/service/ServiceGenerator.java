package com.cats.android.service;

import com.cats.android.model.SessionInterceptor;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.cats.android.util.Constants.BASE_URL;

/**
 * Created by andrey on 13.02.17.
 */

public class ServiceGenerator {

    private static Retrofit retrofit;

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());

    private static OkHttpClient.Builder httpClient =
            new OkHttpClient.Builder();

    private static HttpLoggingInterceptor logging =
            new HttpLoggingInterceptor()
                    .setLevel(HttpLoggingInterceptor.Level.BODY);

    public static <S> S createService(
            Class<S> serviceClass) {

        if (!httpClient.interceptors().contains(logging)) {
            SessionInterceptor interceptor =
                    new SessionInterceptor();

            httpClient.addInterceptor(interceptor);
            httpClient.addInterceptor(logging);
            builder.client(httpClient.build());
            retrofit = builder.build();
        }
        return retrofit.create(serviceClass);
    }
}
