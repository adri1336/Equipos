package com.example.equipos.model.rest;

import com.example.equipos.model.data.Player;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface PlayerClient
{
    @GET("player")
    Call<ArrayList<Player>> get();

    @POST("player")
    Call<Player> post(@Body Player player);

    @GET("player/{id}")
    Call<Player> getId(@Path("id") long id);

    @PUT("player/{id}")
    Call<Boolean> put(@Path("id") long id, @Body Player player);

    @DELETE("player/{id}")
    Call<Boolean> delete(@Path("id") long id);
}
