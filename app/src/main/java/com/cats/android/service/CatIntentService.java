package com.cats.android.service;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;

import com.cats.android.repository.CatRepository;
import com.cats.android.model.AccessToken;
import com.cats.android.model.Cat;
import com.cats.android.util.WebManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.cats.android.util.Constants.*;

public class CatIntentService extends IntentService {

    public CatIntentService() {
        super("CatIntentService");
    }

    private ResultReceiver receiver;

    @Override
    protected void onHandleIntent(Intent i) {
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
            } else {
                return;
            }
        }
    }

    private void getAll() {
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
                t.printStackTrace();
                receiver.send(Activity.RESULT_CANCELED, new Bundle());
            }
        }));
    }

    private void get(Integer id) {
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
                t.printStackTrace();
                receiver.send(Activity.RESULT_CANCELED, new Bundle());
            }
        }));
    }

    private void create(Cat cat) {
        CatClient catClient = ServiceGenerator.createService(CatClient.class);
        Call<ResponseBody> responseCall = catClient.create(cat);
        responseCall.enqueue(receiveResponseMessage());
    }

    private void update(Integer id, Cat cat) {
        CatClient catClient = ServiceGenerator.createService(CatClient.class);
        Call<ResponseBody> responseCall = catClient.update(id, cat);
        responseCall.enqueue(receiveResponseMessage());
    }

    private void delete(Integer id) {
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
                t.printStackTrace();
                receiver.send(Activity.RESULT_CANCELED, new Bundle());
            }
        };
    }

    private void getAccessToken(String code) {
        CatClient loginService =
                ServiceGenerator.createService(CatClient.class, CLIENT_ID, CLIENT_SECRET);
        Call<ResponseBody> call = loginService.getAccessToken(code, "authorization_code", REDIRECT_URI);
        call.enqueue((new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject object = new JSONObject(response.body().string());
                    WebManager.accessToken = new AccessToken(Long.parseLong(object.getString("expires_in")), object.getString("token_type"),
                            object.getString("access_token"));
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (WebManager.accessToken != null) {
                    Bundle bundle = new Bundle();
                    receiver.send(Activity.RESULT_OK, bundle);
                } else {
                    Bundle bundle = new Bundle();
                    receiver.send(Activity.RESULT_CANCELED, bundle);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                receiver.send(Activity.RESULT_CANCELED, new Bundle());
            }
        }));
    }
}
