package com.example.equipos.model.repository;

import com.example.equipos.model.data.Player;
import com.example.equipos.model.rest.PlayerClient;

import java.io.File;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PlayerRepository
{
    private String server;
    private PlayerClient playerClient;

    public PlayerRepository(String server)
    {
        this.server = server;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://" + server + "/web/equipos/public/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        playerClient = retrofit.create(PlayerClient.class);
    }

    public void get(Long id_team, final OnGetPlayerRepositoryListener onGetPlayerRepositoryListener)
    {
        Call<ArrayList<Player>> call = playerClient.getTeam(id_team);
        call.enqueue(new Callback<ArrayList<Player>>()
        {
            @Override
            public void onResponse(Call<ArrayList<Player>> call, Response<ArrayList<Player>> response)
            {
                onGetPlayerRepositoryListener.onSuccess(response.body());
            }


            @Override
            public void onFailure(Call<ArrayList<Player>> call, Throwable t)
            {
                onGetPlayerRepositoryListener.onError();
            }
        });
    }

    public void add(final Player player, final OnAddPlayerRepositoryListener onAddPlayerRepositoryListener)
    {
        Call<Long> call = playerClient.post(player);
        call.enqueue(new Callback<Long>()
        {
            @Override
            public void onResponse(Call<Long> call, Response<Long> response)
            {
                onAddPlayerRepositoryListener.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<Long> call, Throwable t)
            {
                onAddPlayerRepositoryListener.onError();
            }
        });
    }

    public void upload(Long id, File file, final OnUploadPlayerRepositoryListener onUploadPlayerRepositoryListener)
    {
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpg"), file);
        MultipartBody.Part request = MultipartBody.Part.createFormData("file", id + ".jpg", requestBody);

        Call<Boolean> call = playerClient.upload(request);
        call.enqueue(new Callback<Boolean>()
        {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response)
            {
                onUploadPlayerRepositoryListener.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t)
            {
                onUploadPlayerRepositoryListener.onError();
            }
        });
    }

    public void update(final Player player, final OnUpdatedPlayerRepositoryListener onUpdatedPlayerRepositoryListener)
    {
        Call<Boolean> call = playerClient.put(player.getId(), player);
        call.enqueue(new Callback<Boolean>()
        {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response)
            {
                onUpdatedPlayerRepositoryListener.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t)
            {
                onUpdatedPlayerRepositoryListener.onError();
            }
        });
    }

    public void delete(Player player, final OnDeletePlayerRepositoryListener onDeletePlayerRepositoryListener)
    {
        Call<Boolean> call = playerClient.delete(player.getId());
        call.enqueue(new Callback<Boolean>()
        {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response)
            {
                onDeletePlayerRepositoryListener.onSuccess();
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t)
            {
                onDeletePlayerRepositoryListener.onError();
            }
        });
    }

    public interface OnGetPlayerRepositoryListener
    {
        void onSuccess(ArrayList<Player> players);
        void onError();
    }

    public interface OnAddPlayerRepositoryListener
    {
        void onSuccess(Long id);
        void onError();
    }

    public interface OnUploadPlayerRepositoryListener
    {
        void onSuccess(Boolean uploaded);
        void onError();
    }

    public interface OnUpdatedPlayerRepositoryListener
    {
        void onSuccess(Boolean updated);
        void onError();
    }

    public interface OnDeletePlayerRepositoryListener
    {
        void onSuccess();
        void onError();
    }
}
