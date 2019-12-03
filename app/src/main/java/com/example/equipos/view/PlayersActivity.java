package com.example.equipos.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.equipos.R;
import com.example.equipos.model.data.Player;
import com.example.equipos.model.view.PlayersActivityVM;
import com.example.equipos.model.view.PlayersRecyclerViewAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class PlayersActivity extends AppCompatActivity
{

    private Long id_team;

    private PlayersActivityVM playersActivityVM;

    private FloatingActionButton fabAdd;
    private TextView tvError;
    private RecyclerView rvPlayers;

    private static Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_players);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        id_team = getIntent().getExtras().getLong("id_team", 0);
        if(id_team == 0) finish();

        fabAdd = findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(PlayersActivity.this, AddPlayerActivity.class);
                intent.putExtra("id_team", id_team);
                PlayersActivity.this.startActivity(intent);
            }
        });

        tvError = findViewById(R.id.tvError);
        rvPlayers = findViewById(R.id.rvPlayers);

        playersActivityVM = ViewModelProviders.of(this).get(PlayersActivityVM.class);
        playersActivityVM.setPlayersActivity(this, id_team);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        rvPlayers.setLayoutManager(linearLayoutManager);
        final PlayersRecyclerViewAdapter playersRecyclerViewAdapter = new PlayersRecyclerViewAdapter(this, playersActivityVM.getServer());
        rvPlayers.setAdapter(playersRecyclerViewAdapter);
        playersRecyclerViewAdapter.setOnPlayerClickListener(new PlayersRecyclerViewAdapter.OnPlayerClickListener()
        {
            @Override
            public void onClick(final Player player, ConstraintLayout clPlayer)
            {
                PopupMenu popup = new PopupMenu(PlayersActivity.this, clPlayer);
                popup.getMenuInflater().inflate(R.menu.player, popup.getMenu());
                popup.show();

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()
                {
                    @Override
                    public boolean onMenuItemClick(MenuItem item)
                    {
                        switch(item.getItemId())
                        {
                            case R.id.tmEdit:
                            {
                                Intent intent = new Intent(PlayersActivity.this, EditPlayerActivity.class);
                                intent.putExtra("player", player);
                                PlayersActivity.this.startActivity(intent);
                                break;
                            }
                            case R.id.tmDelete:
                            {
                                playersActivityVM.delete(player);
                                break;
                            }
                        }
                        return true;
                    }
                });
            }
        });

        playersActivityVM.getLivePlayers().observe(this, new Observer<List<Player>>()
        {
            @Override
            public void onChanged(List<Player> players)
            {
                playersRecyclerViewAdapter.setData(players);
            }
        });
    }

    public void enableErrorMessage(boolean toggle)
    {
        if(toggle)
        {
            rvPlayers.setVisibility(View.INVISIBLE);
            tvError.setVisibility(View.VISIBLE);
        }
        else
        {
            rvPlayers.setVisibility(View.VISIBLE);
            tvError.setVisibility(View.INVISIBLE);
        }
    }

    public static void makeToast(Context context, String title, int duration)
    {
        if(toast != null)
        {
            toast.cancel();
            toast = null;
        }

        if(title != null)
        {
            toast = Toast.makeText(context, title, duration);
            toast.show();
        }
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Intent intent = new Intent(this, TeamsActivity.class);
        startActivity(intent);
    }
}
