package com.cats.android.model;

import com.cats.android.util.Constants;
import com.cats.android.util.WebManager;

import java.io.IOException;

import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by andrey on 16.02.17.
 */

public class SessionInterceptor implements Interceptor {

    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        Request original = chain.request();

        Request.Builder builder = original.newBuilder();
        if (WebManager.getAccessToken() != null) {
            builder.addHeader("Authorization", WebManager.getAccessToken().getTokenType()
                    + " " + WebManager.getAccessToken().getAccessToken());
        } else {
            builder.addHeader("Authorization", Credentials.basic(Constants.CLIENT_ID, Constants.CLIENT_SECRET));
        }

        Request request = builder.build();
        return chain.proceed(request);
    }
}
