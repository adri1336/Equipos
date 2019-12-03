package com.example.equipos.model.view;

import android.app.Application;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.preference.PreferenceManager;

import com.example.equipos.R;
import com.example.equipos.model.data.Player;
import com.example.equipos.model.repository.PlayerRepository;
import com.example.equipos.view.PlayersActivity;

import java.util.ArrayList;
import java.util.List;

public class PlayersActivityVM extends AndroidViewModel
{
    private String server;
    private PlayerRepository playerRepository;
    private PlayersActivity playersActivity;

    private MutableLiveData<List<Player>> livePlayers = new MutableLiveData();
    private Long id_team;

    public PlayersActivityVM(@NonNull Application application)
    {
        super(application);
    }

    public void setPlayersActivity(PlayersActivity playersActivity, Long id_team)
    {
        this.playersActivity = playersActivity;
        this.id_team = id_team;

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(playersActivity);
        server = sharedPreferences.getString("server", "localhost");

        playerRepository = new PlayerRepository(server);
        fetchPlayers(id_team);
    }

    public void fetchPlayers(Long id_team)
    {
        playerRepository.get(id_team, new PlayerRepository.OnGetPlayerRepositoryListener()
        {
            @Override
            public void onSuccess(ArrayList<Player> players)
            {
                PlayersActivityVM.this.playersActivity.enableErrorMessage(false);
                livePlayers.setValue(players);
            }

            @Override
            public void onError()
            {
                PlayersActivityVM.this.playersActivity.enableErrorMessage(true);
            }
        });
    }

    public LiveData<List<Player>> getLivePlayers()
    {
        return livePlayers;
    }

    public String getServer()
    {
        return server;
    }

    public void delete(Player player)
    {
        playersActivity.makeToast(playersActivity, playersActivity.getString(R.string.toastDeleting), Toast.LENGTH_LONG);
        playerRepository.delete(player, new PlayerRepository.OnDeletePlayerRepositoryListener()
        {
            @Override
            public void onSuccess()
            {
                fetchPlayers(id_team);
                playersActivity.makeToast(playersActivity, playersActivity.getString(R.string.toastDeleted), Toast.LENGTH_SHORT);
            }

            @Override
            public void onError()
            {
                playersActivity.makeToast(playersActivity, null, Toast.LENGTH_SHORT);
            }
        });
    }
}
