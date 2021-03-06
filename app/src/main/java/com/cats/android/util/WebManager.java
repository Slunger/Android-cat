package com.cats.android.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.ResultReceiver;
import android.util.Log;

import com.cats.android.model.AccessToken;
import com.cats.android.model.Cat;
import com.cats.android.service.CatIntentService;
import com.google.firebase.crash.FirebaseCrash;

import okhttp3.HttpUrl;

import static com.cats.android.util.Constants.*;

/**
 * Created by andrey on 16.02.17.
 */

public final class WebManager {

    private static AccessToken accessToken;

    private static String firebaseToken;

    private static final String TAG = "WebManager";

    private WebManager() {
    }

    private static Intent buildCatIntent(Context context, String action, ResultReceiver rec) {
        return new Intent(context, CatIntentService.class)
                .setAction(action)
                .putExtra(RECEIVER, rec);
    }

    public static void getAll(Context context, ResultReceiver rec) {
        context.startService(buildCatIntent(context, GET_ALL, rec));
    }

    public static void create(Context context, ResultReceiver rec, Cat cat) {
        context.startService(buildCatIntent(context, CREATE, rec)
                .putExtra(CAT, cat));
    }

    public static void update(Context context, ResultReceiver rec, Cat cat) {
        context.startService(buildCatIntent(context, UPDATE, rec)
                .putExtra(CAT, cat));
    }

    public static void get(Context context, ResultReceiver rec, Integer id) {
        context.startService(buildCatIntent(context, GET, rec)
                .putExtra(ID, id));
    }

    public static void delete(Context context, ResultReceiver rec, Integer id) {
        context.startService(buildCatIntent(context, DELETE, rec)
                .putExtra(ID, id));
    }

    public static void getAccessToken(Context context, ResultReceiver rec, String code) {
        context.startService(buildCatIntent(context, GET_TOKEN, rec)
                .putExtra(CODE, code));
    }

    public static void authorize(Context context) {
        FirebaseCrash.logcat(Log.INFO, TAG, "Authorization activity");

        if (accessToken == null || accessToken.isExpired()) {
            HttpUrl authorizeUrl = HttpUrl.parse(BASE_URL + "/oauth/authorize")
                    .newBuilder()
                    .addQueryParameter("client_id", CLIENT_ID)
                    .addQueryParameter("redirect_uri", REDIRECT_URI)
                    .addQueryParameter("response_type", CODE)
                    .build();
            context.startActivity(new Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(authorizeUrl.toString())));
        }
    }

    public static void sendToken(Context context, ResultReceiver rec, String token) {
        context.startService(buildCatIntent(context, SEND_TOKEN, rec)
                .putExtra(TOKEN, token));
    }

    public static void liked(Context context, ResultReceiver rec, Integer id) {
        context.startService(buildCatIntent(context, LIKED, rec)
                .putExtra(ID, id));
    }

    public static AccessToken getAccessToken() {
        return accessToken;
    }

    public static void setAccessToken(AccessToken accessToken) {
        WebManager.accessToken = accessToken;
    }

    public static String getFirebaseToken() {
        return firebaseToken;
    }

    public static void setFirebaseToken(String firebaseToken) {
        WebManager.firebaseToken = firebaseToken;
    }
}
