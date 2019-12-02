package com.example.equipos.model.repository;

import android.util.Log;

import com.example.equipos.model.data.Team;
import com.example.equipos.model.rest.TeamClient;

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

public class TeamRepository
{
    private String server;
    private TeamClient teamClient;

    public TeamRepository(String server)
    {
        this.server = server;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://" + server + "/web/equipos/public/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        teamClient = retrofit.create(TeamClient.class);
    }

    public void get(final OnGetTeamRepositoryListener onGetTeamRepositoryListener)
    {
        Call<ArrayList<Team>> call = teamClient.get();
        call.enqueue(new Callback<ArrayList<Team>>()
        {
            @Override
            public void onResponse(Call<ArrayList<Team>> call, Response<ArrayList<Team>> response)
            {
                onGetTeamRepositoryListener.onSuccess(response.body());
            }


            @Override
            public void onFailure(Call<ArrayList<Team>> call, Throwable t)
            {
                onGetTeamRepositoryListener.onError();
            }
        });
    }

    public void add(final Team team, final OnAddTeamRepositoryListener onAddTeamRepositoryListener)
    {
        Call<Long> call = teamClient.post(team);
        call.enqueue(new Callback<Long>()
        {
            @Override
            public void onResponse(Call<Long> call, Response<Long> response)
            {
                onAddTeamRepositoryListener.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<Long> call, Throwable t)
            {
                onAddTeamRepositoryListener.onError();
            }
        });
    }

    public void upload(Long id, File file, final OnUploadTeamRepositoryListener onUploadTeamRepositoryListener)
    {
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpg"), file);
        MultipartBody.Part request = MultipartBody.Part.createFormData("file", id + ".jpg", requestBody);

        Call<Boolean> call = teamClient.upload(request);
        call.enqueue(new Callback<Boolean>()
        {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response)
            {
                onUploadTeamRepositoryListener.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t)
            {
                onUploadTeamRepositoryListener.onError();
            }
        });
    }

    public void update(final Team team, final OnUpdatedTeamRepositoryListener onUpdatedTeamRepositoryListener)
    {
        Call<Boolean> call = teamClient.put(team.getId(), team);
        call.enqueue(new Callback<Boolean>()
        {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response)
            {
                onUpdatedTeamRepositoryListener.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t)
            {
                onUpdatedTeamRepositoryListener.onError();
            }
        });
    }

    public void delete(Team team, final OnDeleteTeamRepositoryListener onDeleteTeamRepositoryListener)
    {
        Call<Boolean> call = teamClient.delete(team.getId());
        call.enqueue(new Callback<Boolean>()
        {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response)
            {
                onDeleteTeamRepositoryListener.onSuccess();
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t)
            {
                onDeleteTeamRepositoryListener.onError();
            }
        });
    }

    public interface OnGetTeamRepositoryListener
    {
        void onSuccess(ArrayList<Team> teams);
        void onError();
    }

    public interface OnAddTeamRepositoryListener
    {
        void onSuccess(Long id);
        void onError();
    }

    public interface OnUploadTeamRepositoryListener
    {
        void onSuccess(Boolean uploaded);
        void onError();
    }

    public interface OnUpdatedTeamRepositoryListener
    {
        void onSuccess(Boolean updated);
        void onError();
    }

    public interface OnDeleteTeamRepositoryListener
    {
        void onSuccess();
        void onError();
    }
}
