package com.example.equipos.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.equipos.R;
import com.example.equipos.model.data.Team;
import com.example.equipos.model.view.TeamsActivityVM;
import com.example.equipos.model.view.TeamsRecyclerViewAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TeamsActivity extends AppCompatActivity
{
    private TeamsActivityVM teamsActivityVM;

    private FloatingActionButton fabAdd;
    private TextView tvError;
    private RecyclerView rvTeams;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teams);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fabAdd = findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(TeamsActivity.this, AddTeamActivity.class);
                TeamsActivity.this.startActivity(intent);
            }
        });

        tvError = findViewById(R.id.tvError);
        rvTeams = findViewById(R.id.rvTeams);

        teamsActivityVM = ViewModelProviders.of(this).get(TeamsActivityVM.class);
        teamsActivityVM.setTeamsActivity(this);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        rvTeams.setLayoutManager(linearLayoutManager);
        final TeamsRecyclerViewAdapter teamsRecyclerViewAdapter = new TeamsRecyclerViewAdapter(this, teamsActivityVM.getServer());
        rvTeams.setAdapter(teamsRecyclerViewAdapter);
        teamsRecyclerViewAdapter.setOnTeamClickListener(new TeamsRecyclerViewAdapter.OnTeamClickListener()
        {
            @Override
            public void onClick(final Team team, ConstraintLayout clTeam)
            {
                PopupMenu popup = new PopupMenu(TeamsActivity.this, clTeam);
                popup.getMenuInflater().inflate(R.menu.team, popup.getMenu());
                popup.show();

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()
                {
                    @Override
                    public boolean onMenuItemClick(MenuItem item)
                    {
                        switch(item.getItemId())
                        {
                            case R.id.tmPlayers:
                            {
                                break;
                            }
                            case R.id.tmEdit:
                            {
                                break;
                            }
                            case R.id.tmDelete:
                            {
                                Toast.makeText(TeamsActivity.this, TeamsActivity.this.getString(R.string.toastDeleting), Toast.LENGTH_LONG).show();
                                teamsActivityVM.delete(team);
                                break;
                            }
                        }
                        return true;
                    }
                });
            }
        });

        teamsActivityVM.getLiveTeams().observe(this, new Observer<List<Team>>()
        {
            @Override
            public void onChanged(List<Team> teams)
            {
                teamsRecyclerViewAdapter.setData(teams);
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        moveTaskToBack(true);
    }

    public void enableErrorMessage(boolean toggle)
    {
        if(toggle)
        {
            rvTeams.setVisibility(View.INVISIBLE);
            tvError.setVisibility(View.VISIBLE);
        }
        else
        {
            rvTeams.setVisibility(View.VISIBLE);
            tvError.setVisibility(View.INVISIBLE);
        }
    }
}
