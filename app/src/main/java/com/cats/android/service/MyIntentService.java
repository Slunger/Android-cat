package com.cats.android.service;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import android.os.ResultReceiver;

import com.cats.android.data.CatContent;
import com.cats.android.model.Cat;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyIntentService extends IntentService {

    private static final String GET_ALL = "get_all";
    private static final String CREATE = "create";
    private static final String UPDATE = "update";
    private static final String GET = "get";
    private static final String DELETE = "delete";

    private static final String ID = "id";
    private static final String CAT_AGE = "cat_age";
    private static final String CAT_COLOR = "cat_color";
    private static final String CAT_BREED = "cat_breed";
    private static final String CAT_NAME = "cat_name";
    private static final String CAT_WEIGHT = "cat_weight";

    public MyIntentService() {
        super("MyIntentService");
    }

    public static void getAll(Context context, ResultReceiver rec) {
        Intent intent = new Intent(context, MyIntentService.class);
        intent.setAction(GET_ALL);
        intent.putExtra("receiver", rec);
        context.startService(intent);
    }

    public static void create(Context context, ResultReceiver rec, Cat cat) {
        Intent intent = new Intent(context, MyIntentService.class);
        intent.setAction(CREATE);
        intent.putExtra(CAT_AGE, cat.getAge());
        intent.putExtra(CAT_COLOR, cat.getColor());
        intent.putExtra(CAT_BREED, cat.getBreed());
        intent.putExtra(CAT_NAME, cat.getName());
        intent.putExtra(CAT_WEIGHT, cat.getWeight());
        intent.putExtra("receiver", rec);
        context.startService(intent);
    }

    public static void update(Context context, ResultReceiver rec, Cat cat) {
        Intent intent = new Intent(context, MyIntentService.class);
        intent.setAction(UPDATE);
        intent.putExtra(ID, cat.getId());
        intent.putExtra(CAT_AGE, cat.getAge());
        intent.putExtra(CAT_COLOR, cat.getColor());
        intent.putExtra(CAT_BREED, cat.getBreed());
        intent.putExtra(CAT_NAME, cat.getName());
        intent.putExtra(CAT_WEIGHT, cat.getWeight());
        intent.putExtra("receiver", rec);
        context.startService(intent);
    }

    public static void get(Context context, ResultReceiver rec, Integer id) {
        Intent intent = new Intent(context, MyIntentService.class);
        intent.setAction(GET);
        intent.putExtra(ID, id);
        intent.putExtra("receiver", rec);
        context.startService(intent);
    }

    public static void delete(Context context, ResultReceiver rec, Integer id) {
        Intent intent = new Intent(context, MyIntentService.class);
        intent.setAction(DELETE);
        intent.putExtra(ID, id);
        intent.putExtra("receiver", rec);
        context.startService(intent);
    }

    private ResultReceiver receiver;

    @Override
    protected void onHandleIntent(Intent i) {
        if (i != null) {
            // Extract the receiver passed into the service
            receiver = i.getParcelableExtra("receiver");
            final String action = i.getAction();
            if (GET_ALL.equals(action)) {
                getAll();
            } else if (CREATE.equals(action)) {
                Cat cat = Cat.create()
                        .setAge(i.getIntExtra(CAT_AGE, 0))
                        .setBreed(i.getStringExtra(CAT_BREED))
                        .setColor(i.getStringExtra(CAT_COLOR))
                        .setName(i.getStringExtra(CAT_NAME))
                        .setWeight(i.getIntExtra(CAT_WEIGHT, 0))
                        .get();
                create(cat);
            } else if (UPDATE.equals(action)) {
                Cat cat = Cat.create()
                        .setId(i.getIntExtra(ID, 0))
                        .setAge(i.getIntExtra(CAT_AGE, 0))
                        .setBreed(i.getStringExtra(CAT_BREED))
                        .setColor(i.getStringExtra(CAT_COLOR))
                        .setName(i.getStringExtra(CAT_NAME))
                        .setWeight(i.getIntExtra(CAT_WEIGHT, 0))
                        .get();
                create(cat);
                Integer id = i.getIntExtra(ID, cat.getId());
                update(id, cat);
            } else if (GET.equals(action)) {
                Integer id = i.getIntExtra(ID, 0);
                get(id);
            } else if (DELETE.equals(action)) {
                Integer id = i.getIntExtra(ID, 0);
                delete(id);
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
                if (cats != null) {
                    CatContent.putItems(cats);
                } else {
                    CatContent.setResponse(response.message());
                }
                receiver.send(Activity.RESULT_OK, new Bundle());
            }

            @Override
            public void onFailure(Call<List<Cat>> call, Throwable t) {
                t.printStackTrace();
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
                if (cat != null) {
                    CatContent.setCAT(cat);
                } else {
                    CatContent.setResponse(response.message());
                }
                receiver.send(Activity.RESULT_OK, new Bundle());
            }

            @Override
            public void onFailure(Call<Cat> call, Throwable t) {
                t.printStackTrace();
            }
        }));
    }

    private void create(Cat cat) {
        CatClient catClient = ServiceGenerator.createService(CatClient.class);
        catClient.create(cat);
    }

    private void update(Integer id, Cat cat) {
        CatClient catClient = ServiceGenerator.createService(CatClient.class);
        catClient.update(id, cat);
    }

    private void delete(Integer id) {
        CatClient catClient = ServiceGenerator.createService(CatClient.class);
        catClient.delete(id);
    }
}
