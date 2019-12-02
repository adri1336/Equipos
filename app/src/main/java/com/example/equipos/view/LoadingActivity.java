package com.example.equipos.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.PreferenceManager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import com.example.equipos.R;
import com.example.equipos.model.view.LoadingActivityVM;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoadingActivity extends AppCompatActivity
{

    private LoadingActivityVM loadingActivityVM;
    private TextInputLayout tilAddress;
    private TextInputEditText tietAddress;
    private CheckBox cbRemember;

    private ConstraintLayout clLoading, clForm;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        loadingActivityVM = ViewModelProviders.of(this).get(LoadingActivityVM.class);
        loadingActivityVM.setLoadingActivity(this);


        clLoading = findViewById(R.id.clLoading);
        clForm = findViewById(R.id.clForm);

        tilAddress = findViewById(R.id.tilAddress);
        tietAddress = findViewById(R.id.tietAddress);
        cbRemember = findViewById(R.id.cbRemember);
        Button btConnect = findViewById(R.id.btConnect);

        tietAddress.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
                tilAddress.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });

        btConnect.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(!loadingActivityVM.isConnecting() && !loadingActivityVM.isConnected())
                {
                    String server = tietAddress.getText().toString();
                    if(server.isEmpty()) setTextInputLayoutError(getString(R.string.tietInvalidServer));
                    loadingActivityVM.setServer(server);
                    loadingActivityVM.connectToServer();
                }
            }
        });

        if(!loadingActivityVM.isStarted())
        {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            loadingActivityVM.setRemember(sharedPreferences.getBoolean("remember_server", false));
            loadingActivityVM.setServer(sharedPreferences.getString("server", ""));
            loadingActivityVM.setStarted(true);

            if(!loadingActivityVM.getServer().isEmpty())
                tietAddress.setText(loadingActivityVM.getServer());

            if(loadingActivityVM.isRemember())
            {
                cbRemember.setChecked(true);
                loadingActivityVM.connectToServer();
            }
            else
            {
                clLoading.setVisibility(View.INVISIBLE);
                clForm.setVisibility(View.VISIBLE);
            }
        }
    }

    public void setTextInputLayoutError(String error)
    {
        tilAddress.setError(error);
    }

    public boolean isRememberCheckBoxEnabled()
    {
        return cbRemember.isChecked();
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
