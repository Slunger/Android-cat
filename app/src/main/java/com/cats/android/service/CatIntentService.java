package com.cats.android.service;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import com.cats.android.repository.CatRepository;
import com.cats.android.model.AccessToken;
import com.cats.android.model.Cat;
import com.cats.android.util.WebManager;
import com.google.firebase.crash.FirebaseCrash;

import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.cats.android.util.Constants.*;

public class CatIntentService extends IntentService {

    private ResultReceiver receiver;

    private static final String TAG = "CatIntentService";

    public CatIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent i) {
        FirebaseCrash.logcat(Log.INFO, TAG, "Service started");

        if (i != null) {
            // Extract the receiver passed into the service
            receiver = i.getParcelableExtra(RECEIVER);

            final String action = i.getAction();
            if (GET_ALL.equals(action)) {
                getAll();
            } else if (CREATE.equals(action)) {
                Cat cat = (Cat) i.getSerializableExtra(CAT);
                create(cat);
            } else if (UPDATE.equals(action)) {
                Cat cat = (Cat) i.getSerializableExtra(CAT);
                update(cat.getId(), cat);
            } else if (GET.equals(action)) {
                Integer id = i.getIntExtra(ID, 0);
                get(id);
            } else if (DELETE.equals(action)) {
                Integer id = i.getIntExtra(ID, 0);
                delete(id);
            } else if (GET_TOKEN.equals(action)) {
                getAccessToken(i.getStringExtra(CODE));
            } else if (SEND_TOKEN.equals(action)) {
                sendToken(i.getStringExtra(TOKEN));
            } else if (LIKED.equals(action)) {
                liked(i.getIntExtra(ID, 0));
            } else {
                return;
            }
        }
    }

    private void getAll() {
        FirebaseCrash.logcat(Log.INFO, TAG, "getAll()");

        CatClient catClient = ServiceGenerator.createService(CatClient.class);
        Call<List<Cat>> call = catClient.getAll();
        call.enqueue((new Callback<List<Cat>>() {
            @Override
            public void onResponse(Call<List<Cat>> call, Response<List<Cat>> response) {
                List<Cat> cats = response.body();
                Bundle bundle = new Bundle();
                if (cats != null) {
                    CatRepository.putItems(cats);
                } else {
                    bundle.putString(RESPONSE, response.message());
                }
                receiver.send(Activity.RESULT_OK, bundle);
            }

            @Override
            public void onFailure(Call<List<Cat>> call, Throwable t) {
                FirebaseCrash.logcat(Log.ERROR, TAG, t.getMessage());
                FirebaseCrash.report(t);
                receiver.send(Activity.RESULT_CANCELED, new Bundle());
            }
        }));
    }

    private void get(Integer id) {
        FirebaseCrash.logcat(Log.INFO, TAG, String.format(Locale.ENGLISH, "get(%d) ", id));

        CatClient catClient = ServiceGenerator.createService(CatClient.class);
        Call<Cat> call = catClient.get(id);
        call.enqueue((new Callback<Cat>() {
            @Override
            public void onResponse(Call<Cat> call, Response<Cat> response) {
                Cat cat = response.body();
                Bundle bundle = new Bundle();
                if (cat != null) {
                    CatRepository.setCAT(cat);
                } else {
                    bundle.putString(RESPONSE, response.message());
                }
                receiver.send(Activity.RESULT_OK, bundle);
            }

            @Override
            public void onFailure(Call<Cat> call, Throwable t) {
                FirebaseCrash.logcat(Log.ERROR, TAG, t.getMessage());
                FirebaseCrash.report(t);
                receiver.send(Activity.RESULT_CANCELED, new Bundle());
            }
        }));
    }

    private void create(Cat cat) {
        FirebaseCrash.logcat(Log.INFO, TAG, String.format("create(%s)", cat.toString()));

        CatClient catClient = ServiceGenerator.createService(CatClient.class);
        Call<ResponseBody> responseCall = catClient.create(cat);
        responseCall.enqueue(receiveResponseMessage());
    }

    private void update(Integer id, Cat cat) {
        FirebaseCrash.logcat(Log.INFO, TAG, String.format(Locale.ENGLISH, "update(%d, %s) ", id, cat.toString()));

        CatClient catClient = ServiceGenerator.createService(CatClient.class);
        Call<ResponseBody> responseCall = catClient.update(id, cat);
        responseCall.enqueue(receiveResponseMessage());
    }

    private void delete(Integer id) {
        FirebaseCrash.logcat(Log.INFO, TAG, String.format(Locale.ENGLISH, "delete(%d) ", id));

        CatClient catClient = ServiceGenerator.createService(CatClient.class);
        Call<ResponseBody> responseCall = catClient.delete(id);
        responseCall.enqueue(receiveResponseMessage());
    }

    private Callback<ResponseBody> receiveResponseMessage() {
        return new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Bundle bundle = new Bundle();
                bundle.putString(RESPONSE, response.message());
                receiver.send(Activity.RESULT_OK, bundle);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                FirebaseCrash.logcat(Log.ERROR, TAG, t.getMessage());
                FirebaseCrash.report(t);
                receiver.send(Activity.RESULT_CANCELED, new Bundle());
            }
        };
    }

    private void sendToken(String token) {
        FirebaseCrash.logcat(Log.INFO, TAG, String.format(Locale.ENGLISH, "sendToken(%s) ", token));

        CatClient catClient = ServiceGenerator.createService(CatClient.class);
        Call<ResponseBody> responseCall = catClient.sendToken(token);
        responseCall.enqueue(receiveResponseMessage());
    }

    private void liked(Integer id) {
        FirebaseCrash.logcat(Log.INFO, TAG, String.format(Locale.ENGLISH, "liked(%d) ", id));

        CatClient catClient = ServiceGenerator.createService(CatClient.class);
        Call<ResponseBody> responseCall = catClient.liked(id);
        responseCall.enqueue(receiveResponseMessage());
    }

    private void getAccessToken(String code) {
        FirebaseCrash.logcat(Log.INFO, TAG, String.format("getAccessToken(%s) ", code));

        CatClient loginService =
                ServiceGenerator.createService(CatClient.class);
        Call<AccessToken> call = loginService.getAccessToken(code, "authorization_code", REDIRECT_URI);
        call.enqueue((new Callback<AccessToken>() {
            @Override
            public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                WebManager.setAccessToken(response.body());
                if (WebManager.getAccessToken() != null) {
                    Bundle bundle = new Bundle();
                    receiver.send(Activity.RESULT_OK, bundle);
                } else {
                    Bundle bundle = new Bundle();
                    receiver.send(Activity.RESULT_CANCELED, bundle);
                }
            }

            @Override
            public void onFailure(Call<AccessToken> call, Throwable t) {
                FirebaseCrash.logcat(Log.ERROR, TAG, t.getMessage());
                FirebaseCrash.report(t);
                receiver.send(Activity.RESULT_CANCELED, new Bundle());
            }
        }));
    }
}
