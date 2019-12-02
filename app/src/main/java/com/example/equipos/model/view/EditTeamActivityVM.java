package com.example.equipos.model.view;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.preference.PreferenceManager;

import com.example.equipos.R;
import com.example.equipos.model.data.Team;
import com.example.equipos.model.repository.TeamRepository;
import com.example.equipos.view.EditTeamActivity;
import com.example.equipos.view.TeamsActivity;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class EditTeamActivityVM extends AndroidViewModel
{
    private Uri image;
    private String name, city, stadium;
    private Integer stadium_capacity;

    private EditTeamActivity editTeamActivity;
    private String server;
    private TeamRepository teamRepository;
    private boolean updating;
    private File imageFile;

    public EditTeamActivityVM(@NonNull Application application)
    {
        super(application);
    }

    public boolean isUpdating()
    {
        return updating;
    }

    public void setUpdating(boolean updating)
    {
        this.updating = updating;
    }

    public void update(final Team team)
    {
        updating = true;
        teamRepository.update(team, new TeamRepository.OnUpdatedTeamRepositoryListener()
        {
            @Override
            public void onSuccess(Boolean updated)
            {
                if(imageFile == null)
                {
                    updating = false;
                    Intent intent = new Intent(editTeamActivity, TeamsActivity.class);
                    editTeamActivity.startActivity(intent);
                }
                else
                {
                    teamRepository.upload(team.getId(), imageFile, new TeamRepository.OnUploadTeamRepositoryListener()
                    {
                        @Override
                        public void onSuccess(Boolean uploaded)
                        {
                            updating = false;
                            Intent intent = new Intent(editTeamActivity, TeamsActivity.class);
                            editTeamActivity.startActivity(intent);
                        }

                        @Override
                        public void onError()
                        {
                            updating = false;
                            Intent intent = new Intent(editTeamActivity, TeamsActivity.class);
                            editTeamActivity.startActivity(intent);
                        }
                    });
                }
            }

            @Override
            public void onError()
            {
                updating = false;
                editTeamActivity.setLoading(false);
                Toast.makeText(editTeamActivity, editTeamActivity.getText(R.string.toastUpdatingError), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void setEditTeamActivity(EditTeamActivity editTeamActivity)
    {
        this.editTeamActivity = editTeamActivity;

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(editTeamActivity);
        server = sharedPreferences.getString("server", "localhost");
        teamRepository = new TeamRepository(server);
    }

    public Uri getImage()
    {
        return image;
    }

    public void setImage(Uri image)
    {
        this.image = image;
        saveSelectedImageInFile(image);
    }

    public String getServer()
    {
        return server;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getCity()
    {
        return city;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    public String getStadium()
    {
        return stadium;
    }

    public void setStadium(String stadium)
    {
        this.stadium = stadium;
    }

    public Integer getStadium_capacity()
    {
        return stadium_capacity;
    }

    public void setStadium_capacity(Integer stadium_capacity)
    {
        this.stadium_capacity = stadium_capacity;
    }

    public void saveSelectedImageInFile(Uri uri)
    {
        Bitmap bitmap = null;
        if(Build.VERSION.SDK_INT < 28)
        {
            try
            {
                bitmap = MediaStore.Images.Media.getBitmap(editTeamActivity.getContentResolver(), uri);
            }
            catch (IOException e)
            {
                bitmap = null;
            }
        }
        else
        {
            try
            {
                final InputStream in = editTeamActivity.getContentResolver().openInputStream(uri);
                BufferedInputStream bufferedInputStream = new BufferedInputStream(in);
                bitmap = BitmapFactory.decodeStream(bufferedInputStream);
            }
            catch (IOException e)
            {
                bitmap = null;
            }
        }

        if(bitmap != null)
        {
            this.imageFile = saveBitmapInFile(bitmap);
        }
    }

    private File saveBitmapInFile(Bitmap bitmap)
    {
        File file = new File(editTeamActivity.getFilesDir(), "temp.jpg");
        FileOutputStream out = null;
        try
        {
            out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        }
        catch (IOException e)
        {
            file = null;
        }
        return file;
    }
}
