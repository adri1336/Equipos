package com.example.equipos.view;

import android.content.Intent;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.equipos.R;
import com.example.equipos.model.data.Player;
import com.example.equipos.model.view.AddPlayerActivityVM;
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
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class AddPlayerActivity extends AppCompatActivity
{
    private static final int PHOTO_SELECTED = 1;

    private AddPlayerActivityVM addPlayerActivityVM;
    private ImageView ivPlayer;
    private TextInputLayout tilFirstname, tilLastname;
    private TextInputEditText tietFirstname, tietLastname;
    private Button btAdd;

    private ConstraintLayout clLoading, clForm;
    private Long id_team;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_player);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        id_team = getIntent().getExtras().getLong("id_team", 0);
        if(id_team == 0) finish();

        addPlayerActivityVM = ViewModelProviders.of(this).get(AddPlayerActivityVM.class);
        addPlayerActivityVM.setAddPlayerActivity(this);

        clLoading = findViewById(R.id.clLoading);
        clForm = findViewById(R.id.clForm);

        ivPlayer = findViewById(R.id.ivPlayer);
        tilFirstname = findViewById(R.id.tilFirstname);
        tilLastname = findViewById(R.id.tilLastname);
        tietFirstname = findViewById(R.id.tietFirstname);
        tietLastname = findViewById(R.id.tietLastname);
        btAdd = findViewById(R.id.btAdd);

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
                addPlayerActivityVM.setFirstname(s.toString());
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
                addPlayerActivityVM.setLastname(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s)
            {
            }
        });

        //Set values
        if(addPlayerActivityVM.getImage() != null)
            ivPlayer.setImageURI(addPlayerActivityVM.getImage());

        if(addPlayerActivityVM.getFirstname() != null && !addPlayerActivityVM.getFirstname().isEmpty())
            tietFirstname.setText(addPlayerActivityVM.getFirstname());

        if(addPlayerActivityVM.getLastname() != null && !addPlayerActivityVM.getLastname().isEmpty())
            tietLastname.setText(addPlayerActivityVM.getLastname());

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

        //Add button
        btAdd.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(!addPlayerActivityVM.isAdding())
                {
                    boolean add = true;
                    String firstname, lastname;
                    firstname = tietFirstname.getText().toString();
                    lastname = tietLastname.getText().toString();

                    if(firstname.isEmpty())
                    {
                        tilFirstname.setError(AddPlayerActivity.this.getString(R.string.etNoValue));
                        add = false;
                    }

                    if(lastname.isEmpty())
                    {
                        tilLastname.setError(AddPlayerActivity.this.getString(R.string.etNoValue));
                        add = false;
                    }

                    if(add)
                    {
                        addPlayerActivityVM.setAdding(true);
                        setLoading(true);

                        Player player = new Player(firstname, lastname, id_team);
                        addPlayerActivityVM.add(player);
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
            addPlayerActivityVM.setImage(data.getData());
            Glide.with(this)
                    .load(addPlayerActivityVM.getImage())
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
