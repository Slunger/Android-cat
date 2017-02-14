package com.cats.android.service;

import com.cats.android.model.Cat;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by andrey on 13.02.17.
 */

public interface CatClient {

    @GET("/cats")
    Call<List<Cat>> getAll();

    @POST("/cats")
    Call<ResponseBody> create(@Body Cat cat);

    @PUT("/cats/{id}")
    Call<ResponseBody> update(@Path("id") Integer id, @Body Cat cat);

    @GET("/cats/{id}")
    Call<Cat> get(@Path("id") Integer id);

    @DELETE("/cats/{id}")
    Call<ResponseBody> delete(@Path("id") Integer id);
}
