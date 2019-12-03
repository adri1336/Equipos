package com.example.equipos.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.equipos.R;
import com.example.equipos.model.data.Player;
import com.example.equipos.model.view.EditPlayerActivityVM;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProviders;

import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class EditPlayerActivity extends AppCompatActivity
{
    private static final int PHOTO_SELECTED = 1;

    private EditPlayerActivityVM editPlayerActivityVM;
    private ImageView ivPlayer;
    private TextInputLayout tilFirstname, tilLastname;
    private TextInputEditText tietFirstname, tietLastname;
    private Button btUpdate;

    private Player player;

    private ConstraintLayout clLoading, clForm;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_player);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        player = getIntent().getExtras().getParcelable("player");

        editPlayerActivityVM = ViewModelProviders.of(this).get(EditPlayerActivityVM.class);
        editPlayerActivityVM.setEditPlayerActivity(this);

        if(editPlayerActivityVM.getImage() == null)
            editPlayerActivityVM.setImage(Uri.parse("http://" + editPlayerActivityVM.getServer() + "/web/equipos/public/upload/images/players/" + player.getId() + ".jpg"));

        if(editPlayerActivityVM.getFirstname() == null)
            editPlayerActivityVM.setFirstname(player.getFirstname());

        if(editPlayerActivityVM.getLastname() == null)
            editPlayerActivityVM.setLastname(player.getLastname());

        clLoading = findViewById(R.id.clLoading);
        clForm = findViewById(R.id.clForm);

        ivPlayer = findViewById(R.id.ivPlayer);
        tilFirstname = findViewById(R.id.tilFirstname);
        tilLastname = findViewById(R.id.tilLastname);
        tietFirstname = findViewById(R.id.tietFirstname);
        tietLastname = findViewById(R.id.tietLastname);
        btUpdate = findViewById(R.id.btUpdate);

        //Text input layouts watchers
        tietFirstname.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
                tilFirstname.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                editPlayerActivityVM.setFirstname(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s)
            {
            }
        });

        tietLastname.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
                tilLastname.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                editPlayerActivityVM.setLastname(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s)
            {
            }
        });

        //Set values
        if(editPlayerActivityVM.getImage() != null)
        {
            Glide.with(this)
                    .load(editPlayerActivityVM.getImage())
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.ic_player_default)
                            .fitCenter()
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true))
                    .into(ivPlayer);
        }

        if(editPlayerActivityVM.getFirstname() != null && !editPlayerActivityVM.getFirstname().isEmpty())
            tietFirstname.setText(editPlayerActivityVM.getFirstname());

        if(editPlayerActivityVM.getLastname() != null && !editPlayerActivityVM.getLastname().isEmpty())
            tietLastname.setText(editPlayerActivityVM.getLastname());

        //Player shield image
        ivPlayer.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, PHOTO_SELECTED);
            }
        });

        //Update button
        btUpdate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(!editPlayerActivityVM.isUpdating())
                {
                    boolean add = true;
                    String firstname, lastname;
                    firstname = tietFirstname.getText().toString();
                    lastname = tietLastname.getText().toString();

                    if(firstname.isEmpty())
                    {
                        tilFirstname.setError(EditPlayerActivity.this.getString(R.string.etNoValue));
                        add = false;
                    }

                    if(lastname.isEmpty())
                    {
                        tilLastname.setError(EditPlayerActivity.this.getString(R.string.etNoValue));
                        add = false;
                    }

                    if(add)
                    {
                        editPlayerActivityVM.setUpdating(true);
                        setLoading(true);

                        player.setFirstname(firstname);
                        player.setLastname(lastname);
                        editPlayerActivityVM.update(player);
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PHOTO_SELECTED && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            editPlayerActivityVM.setImage(data.getData());
            Glide.with(this)
                    .load(editPlayerActivityVM.getImage())
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.ic_player_default)
                            .fitCenter()
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true))
                    .into(ivPlayer);
        }
    }

    public void setLoading(boolean toggle)
    {
        if(toggle)
        {
            clLoading.setVisibility(View.VISIBLE);
            clForm.setVisibility(View.INVISIBLE);
        }
        else
        {
            clLoading.setVisibility(View.INVISIBLE);
            clForm.setVisibility(View.VISIBLE);
        }
    }
}
