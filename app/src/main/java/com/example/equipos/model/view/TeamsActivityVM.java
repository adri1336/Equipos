package com.example.equipos.model.view;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.preference.PreferenceManager;

import com.example.equipos.model.data.Team;
import com.example.equipos.model.repository.TeamRepository;
import com.example.equipos.view.TeamsActivity;

import java.util.ArrayList;
import java.util.List;

public class TeamsActivityVM extends AndroidViewModel
{
    private String server;
    private TeamRepository teamRepository;
    private TeamsActivity teamsActivity;

    private MutableLiveData<List<Team>> liveTeams = new MutableLiveData();

    public TeamsActivityVM(@NonNull Application application)
    {
        super(application);
    }

    public void setTeamsActivity(TeamsActivity teamsActivity)
    {
        this.teamsActivity = teamsActivity;

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(teamsActivity);
        server = sharedPreferences.getString("server", "localhost");

        teamRepository = new TeamRepository(server);
        fetchTeams();
    }

    public void fetchTeams()
    {
        teamRepository.get(new TeamRepository.OnGetTeamRepositoryListener()
        {
            @Override
            public void onSuccess(ArrayList<Team> teams)
            {
                TeamsActivityVM.this.teamsActivity.enableErrorMessage(false);
                liveTeams.setValue(teams);
            }

            @Override
            public void onError()
            {
                TeamsActivityVM.this.teamsActivity.enableErrorMessage(true);
            }
        });
    }

    public LiveData<List<Team>> getLiveTeams()
    {
        return liveTeams;
    }

    public String getServer()
    {
        return server;
    }

    public void delete(Team team)
    {
        teamRepository.delete(team, new TeamRepository.OnDeleteTeamRepositoryListener()
        {
            @Override
            public void onSuccess()
            {
                fetchTeams();
            }

            @Override
            public void onError()
            {

            }
        });
    }
}
