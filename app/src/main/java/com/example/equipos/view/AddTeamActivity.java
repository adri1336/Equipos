package com.example.equipos.view;

import android.content.Intent;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.equipos.R;
import com.example.equipos.model.data.Team;
import com.example.equipos.model.view.AddTeamActivityVM;
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

public class AddTeamActivity extends AppCompatActivity
{
    private static final int PHOTO_SELECTED = 1;

    private AddTeamActivityVM addTeamActivityVM;
    private ImageView ivTeam;
    private TextInputLayout tilName, tilCity, tilStadium, tilStadium_capacity;
    private TextInputEditText tietName, tietCity, tietStadium, tietStadium_capacity;
    private Button btAdd;

    private ConstraintLayout clLoading, clForm;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_team);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        addTeamActivityVM = ViewModelProviders.of(this).get(AddTeamActivityVM.class);
        addTeamActivityVM.setAddTeamActivity(this);

        clLoading = findViewById(R.id.clLoading);
        clForm = findViewById(R.id.clForm);

        ivTeam = findViewById(R.id.ivTeam);
        tilName = findViewById(R.id.tilName);
        tilCity = findViewById(R.id.tilCity);
        tilStadium = findViewById(R.id.tilStadium);
        tilStadium_capacity = findViewById(R.id.tilStadium_capacity);
        tietName = findViewById(R.id.tietName);
        tietCity = findViewById(R.id.tietCity);
        tietStadium = findViewById(R.id.tietStadium);
        tietStadium_capacity = findViewById(R.id.tietStadium_capacity);
        btAdd = findViewById(R.id.btAdd);

        //Text input layouts watchers
        tietName.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
                tilName.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                addTeamActivityVM.setName(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s)
            {
            }
        });

        tietCity.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
                tilCity.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                addTeamActivityVM.setCity(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s)
            {
            }
        });

        tietStadium.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
                tilStadium.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                addTeamActivityVM.setStadium(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s)
            {
            }
        });

        tietStadium_capacity.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
                tilStadium_capacity.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if(!s.toString().isEmpty())
                    addTeamActivityVM.setStadium_capacity(Integer.parseInt(s.toString()));
            }

            @Override
            public void afterTextChanged(Editable s)
            {
            }
        });

        //Set values
        if(addTeamActivityVM.getImage() != null)
            ivTeam.setImageURI(addTeamActivityVM.getImage());

        if(addTeamActivityVM.getName() != null && !addTeamActivityVM.getName().isEmpty())
            tietName.setText(addTeamActivityVM.getName());

        if(addTeamActivityVM.getCity() != null && !addTeamActivityVM.getCity().isEmpty())
            tietCity.setText(addTeamActivityVM.getCity());

        if(addTeamActivityVM.getStadium() != null && !addTeamActivityVM.getStadium().isEmpty())
            tietStadium.setText(addTeamActivityVM.getStadium());

        if(addTeamActivityVM.getStadium_capacity() != null)
            tietStadium_capacity.setText(addTeamActivityVM.getStadium_capacity().toString());

        //Team shield image
        ivTeam.setOnClickListener(new View.OnClickListener()
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
                if(!addTeamActivityVM.isAdding())
                {
                    boolean add = true;
                    String name, city, stadium, stadium_capacity;
                    name = tietName.getText().toString();
                    city = tietCity.getText().toString();
                    stadium = tietStadium.getText().toString();
                    stadium_capacity = tietStadium_capacity.getText().toString();

                    if(name.isEmpty())
                    {
                        tilName.setError(AddTeamActivity.this.getString(R.string.etNoValue));
                        add = false;
                    }

                    if(city.isEmpty())
                    {
                        tilCity.setError(AddTeamActivity.this.getString(R.string.etNoValue));
                        add = false;
                    }

                    if(stadium.isEmpty())
                    {
                        tilStadium.setError(AddTeamActivity.this.getString(R.string.etNoValue));
                        add = false;
                    }

                    if(stadium_capacity.isEmpty())
                    {
                        tilStadium_capacity.setError(AddTeamActivity.this.getString(R.string.etNoValue));
                        add = false;
                    }

                    if(add)
                    {
                        addTeamActivityVM.setAdding(true);
                        setLoading(true);

                        Team team = new Team(name, city, stadium, Integer.parseInt(stadium_capacity));
                        addTeamActivityVM.add(team);
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
            addTeamActivityVM.setImage(data.getData());
            Glide.with(this)
                    .load(addTeamActivityVM.getImage())
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.ic_team_default)
                            .fitCenter()
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true))
                    .into(ivTeam);
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
