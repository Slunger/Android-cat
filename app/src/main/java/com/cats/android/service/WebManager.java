package com.cats.android.service;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.ResultReceiver;
import com.cats.android.model.AccessToken;
import com.cats.android.model.Cat;
import okhttp3.HttpUrl;

import static com.cats.android.util.Constants.*;

/**
 * Created by andrey on 16.02.17.
 */

public final class WebManager {

    public static AccessToken accessToken;

    private WebManager(){
    }

    public static void getAll(Context context, ResultReceiver rec) {
        Intent intent = new Intent(context, CatIntentService.class);
        intent.setAction(GET_ALL);
        intent.putExtra(RECEIVER, rec);
        context.startService(intent);
    }

    public static void create(Context context, ResultReceiver rec, Cat cat) {
        Intent intent = new Intent(context, CatIntentService.class);
        intent.setAction(CREATE);
        intent.putExtra(CAT, cat);
        intent.putExtra(RECEIVER, rec);
        context.startService(intent);
    }

    public static void update(Context context, ResultReceiver rec, Cat cat) {
        Intent intent = new Intent(context, CatIntentService.class);
        intent.setAction(UPDATE);
        intent.putExtra(CAT, cat);
        intent.putExtra(RECEIVER, rec);
        context.startService(intent);
    }

    public static void get(Context context, ResultReceiver rec, Integer id) {
        Intent intent = new Intent(context, CatIntentService.class);
        intent.setAction(GET);
        intent.putExtra(ID, id);
        intent.putExtra(RECEIVER, rec);
        context.startService(intent);
    }

    public static void delete(Context context, ResultReceiver rec, Integer id) {
        Intent intent = new Intent(context, CatIntentService.class);
        intent.setAction(DELETE);
        intent.putExtra(ID, id);
        intent.putExtra(RECEIVER, rec);
        context.startService(intent);
    }

    public static void authorize(Context context){
        if (accessToken == null || accessToken.isExpired()){
            HttpUrl authorizeUrl = HttpUrl.parse(BASE_URL + "/oauth/authorize")
                    .newBuilder()
                    .addQueryParameter("client_id", CLIENT_ID)
                    .addQueryParameter("redirect_uri", REDIRECT_URI)
                    .addQueryParameter("response_type", "code")
                    .build();
            Intent intent = new Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(authorizeUrl.toString()));
            context.startActivity(intent);
        }
    }

    public static void getAccessToken(Context context, ResultReceiver rec, String code) {
        Intent intent = new Intent(context, CatIntentService.class);
        intent.setAction("getAccessToken");
        intent.putExtra("code", code);
        intent.putExtra(RECEIVER, rec);
        context.startService(intent);
    }
}
