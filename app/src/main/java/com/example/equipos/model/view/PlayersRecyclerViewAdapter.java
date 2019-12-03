package com.example.equipos.model.view;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.equipos.R;
import com.example.equipos.model.data.Player;
import com.example.equipos.view.PlayersActivity;

import java.util.List;

public class PlayersRecyclerViewAdapter extends RecyclerView.Adapter<PlayersRecyclerViewAdapter.ItemHolder>
{
    private PlayersActivity playersActivity;
    private LayoutInflater layoutInflater;
    private List<Player> players;
    private String server;
    private PlayersRecyclerViewAdapter.OnPlayerClickListener onPlayerClickListener;

    public PlayersRecyclerViewAdapter(PlayersActivity playersActivity, String server)
    {
        this.playersActivity = playersActivity;
        this.server = server;
        layoutInflater = LayoutInflater.from(playersActivity);
    }

    @NonNull
    @Override
    public PlayersRecyclerViewAdapter.ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = layoutInflater.inflate(R.layout.player_item, parent, false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final PlayersRecyclerViewAdapter.ItemHolder holder, int position)
    {
        final Player player = players.get(position);

        Uri uri = Uri.parse("http://" + server + "/web/equipos/public/upload/images/players/" + player.getId() + ".jpg");
        Glide.with(playersActivity)
                .load(uri)
                .apply(new RequestOptions()
                        .placeholder(R.drawable.ic_player_default)
                        .fitCenter()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true))
                .into(holder.ivPlayer);

        holder.tvPlayer.setText(player.getFirstname() + " " + player.getLastname());
        holder.tvTeam.setText("Team ID: " + player.getId_team());

        if(onPlayerClickListener != null)
        {
            holder.clPlayer.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    onPlayerClickListener.onClick(player, holder.clPlayer);
                }
            });
        }
    }

    public void setOnPlayerClickListener(OnPlayerClickListener onPlayerClickListener)
    {
        this.onPlayerClickListener = onPlayerClickListener;
    }

    @Override
    public int getItemCount()
    {
        return players == null ? 0 : players.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder
    {
        private ConstraintLayout clPlayer;
        private ImageView ivPlayer;
        private TextView tvPlayer, tvTeam;

        public ItemHolder(@NonNull View itemView)
        {
            super(itemView);

            clPlayer = itemView.findViewById(R.id.clPlayer);
            ivPlayer = itemView.findViewById(R.id.ivPlayer);
            tvPlayer = itemView.findViewById(R.id.tvPlayer);
            tvTeam = itemView.findViewById(R.id.tvTeam);
        }
    }

    public void setData(List<Player> players)
    {
        this.players = players;
        notifyDataSetChanged();
    }

    public interface OnPlayerClickListener
    {
        void onClick(Player player, ConstraintLayout clPlayer);
    }
}
