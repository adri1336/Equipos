package com.example.equipos.model.view;

import android.net.Uri;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.equipos.R;
import com.example.equipos.model.data.Team;
import com.example.equipos.view.TeamsActivity;

import java.text.NumberFormat;
import java.util.List;

public class TeamsRecyclerViewAdapter extends RecyclerView.Adapter<TeamsRecyclerViewAdapter.ItemHolder>
{
    private TeamsActivity teamsActivity;
    private LayoutInflater layoutInflater;
    private List<Team> teams;
    private String server;
    private OnTeamClickListener onTeamClickListener;

    public TeamsRecyclerViewAdapter(TeamsActivity teamsActivity, String server)
    {
        this.teamsActivity = teamsActivity;
        this.server = server;
        layoutInflater = LayoutInflater.from(teamsActivity);
    }

    @NonNull
    @Override
    public TeamsRecyclerViewAdapter.ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = layoutInflater.inflate(R.layout.team_item, parent, false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final TeamsRecyclerViewAdapter.ItemHolder holder, int position)
    {
        final Team team = teams.get(position);

        Uri uri = Uri.parse("http://" + server + "/web/equipos/public/upload/images/teams/" + team.getId() + ".jpg");
        Glide.with(teamsActivity)
                .load(uri)
                .apply(new RequestOptions()
                        .placeholder(R.drawable.ic_team_default)
                        .fitCenter()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true))
                .into(holder.ivTeam);

        holder.tvTeam.setText(team.getName());
        holder.tvCity.setText(team.getCity());

        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        holder.tvStadium.setText(team.getStadium() + " (" + numberFormat.format(team.getStadium_capacity()) + " " + teamsActivity.getString(R.string.stadium_capacity) + ")");

        if(onTeamClickListener != null)
        {
            holder.clTeam.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    onTeamClickListener.onClick(team, holder.clTeam);
                }
            });
        }
    }

    public void setOnTeamClickListener(OnTeamClickListener onTeamClickListener)
    {
        this.onTeamClickListener = onTeamClickListener;
    }

    @Override
    public int getItemCount()
    {
        return teams == null ? 0 : teams.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder
    {
        private ConstraintLayout clTeam;
        private ImageView ivTeam;
        private TextView tvTeam, tvCity, tvStadium;

        public ItemHolder(@NonNull View itemView)
        {
            super(itemView);

            clTeam = itemView.findViewById(R.id.clTeam);
            ivTeam = itemView.findViewById(R.id.ivTeam);
            tvTeam = itemView.findViewById(R.id.tvTeam);
            tvCity = itemView.findViewById(R.id.tvCity);
            tvStadium = itemView.findViewById(R.id.tvStadium);
        }
    }

    public void setData(List<Team> teams)
    {
        this.teams = teams;
        notifyDataSetChanged();
    }

    public interface OnTeamClickListener
    {
        void onClick(Team team, ConstraintLayout clTeam);
    }
}
