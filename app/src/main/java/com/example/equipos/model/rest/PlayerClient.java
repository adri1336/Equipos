package com.example.equipos.model.rest;

import com.example.equipos.model.data.Player;

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

public interface PlayerClient
{
    @GET("player")
    Call<ArrayList<Player>> get();

    @GET("player_team/{id_team}")
    Call<ArrayList<Player>> getTeam(@Path("id_team") Long id_team);

    @POST("player")
    Call<Long> post(@Body Player player);

    @Multipart
    @POST("upload_player")
    Call<Boolean> upload(@Part MultipartBody.Part file);

    @GET("player/{id}")
    Call<Player> getId(@Path("id") Long id);

    @PUT("player/{id}")
    Call<Boolean> put(@Path("id") Long id, @Body Player player);

    @DELETE("player/{id}")
    Call<Boolean> delete(@Path("id") Long id);
}
