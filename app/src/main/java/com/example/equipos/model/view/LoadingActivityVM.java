package com.example.equipos.model.view;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.preference.PreferenceManager;

import com.example.equipos.R;
import com.example.equipos.model.data.Team;
import com.example.equipos.model.repository.TeamRepository;
import com.example.equipos.view.LoadingActivity;
import com.example.equipos.view.TeamsActivity;

import java.util.ArrayList;

public class LoadingActivityVM extends AndroidViewModel
{
    private boolean started;
    private boolean connecting;
    private boolean connected;
    private boolean remember;
    private String server;
    private TeamRepository teamRepository;
    private LoadingActivity loadingActivity;

    public LoadingActivityVM(@NonNull Application application)
    {
        super(application);
    }

    public void setLoadingActivity(LoadingActivity loadingActivity)
    {
        this.loadingActivity = loadingActivity;
    }

    public boolean isConnecting()
    {
        return connecting;
    }

    public boolean isConnected()
    {
        return connected;
    }

    public boolean isStarted()
    {
        return started;
    }

    public void setStarted(boolean started)
    {
        this.started = started;
    }

    public void setRemember(boolean remember)
    {
        this.remember = remember;
    }

    public boolean isRemember()
    {
        return remember;
    }

    public void setServer(String server)
    {
        this.server = server;
    }

    public String getServer()
    {
        return server;
    }

    public void connectToServer()
    {
        this.connecting = true;
        if(!this.server.isEmpty())
        {
            loadingActivity.setLoading(true);

            teamRepository = new TeamRepository(server);
            teamRepository.get(new TeamRepository.OnGetTeamRepositoryListener()
            {
                @Override
                public void onSuccess(ArrayList<Team> teams)
                {

                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(loadingActivity);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("server", LoadingActivityVM.this.server);

                    if(loadingActivity.isRememberCheckBoxEnabled()) editor.putBoolean("remember_server", true);
                    else editor.putBoolean("remember_server", false);

                    editor.apply();

                    LoadingActivityVM.this.connecting = false;
                    LoadingActivityVM.this.connected = true;

                    Intent intent = new Intent(loadingActivity, TeamsActivity.class);
                    loadingActivity.startActivity(intent);
                }

                @Override
                public void onError()
                {
                    loadingActivity.setTextInputLayoutError(loadingActivity.getString(R.string.tietInvalidServer));
                    loadingActivity.setLoading(false);
                    LoadingActivityVM.this.connecting = false;
                }
            });
        }
        else this.connecting = false;
    }
}
