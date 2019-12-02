package com.example.equipos.model.rest;


import com.example.equipos.model.data.Team;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface TeamClient
{
    @GET("team")
    Call<ArrayList<Team>> get();

    @POST("team")
    Call<Long> post(@Body Team team);

    @Multipart
    @POST("upload_team")
    Call<Boolean> upload(@Part MultipartBody.Part file);

    @GET("team/{id}")
    Call<Team> getId(@Path("id") Long id);

    @PUT("team/{id}")
    Call<Boolean> put(@Path("id") Long id, @Body Team team);

    @DELETE("team/{id}")
    Call<Boolean> delete(@Path("id") Long id);
}
