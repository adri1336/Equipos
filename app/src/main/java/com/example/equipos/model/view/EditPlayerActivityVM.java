package com.example.equipos.model.view;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.preference.PreferenceManager;

import com.example.equipos.R;
import com.example.equipos.model.data.Player;
import com.example.equipos.model.repository.PlayerRepository;
import com.example.equipos.view.EditPlayerActivity;
import com.example.equipos.view.PlayersActivity;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class EditPlayerActivityVM extends AndroidViewModel
{
    private Uri image;
    private String firstname, lastname;

    private EditPlayerActivity editPlayerActivity;
    private String server;
    private PlayerRepository playerRepository;
    private boolean updating;
    private File imageFile;

    public EditPlayerActivityVM(@NonNull Application application)
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

    public void update(final Player player)
    {
        updating = true;
        playerRepository.update(player, new PlayerRepository.OnUpdatedPlayerRepositoryListener()
        {
            @Override
            public void onSuccess(Boolean updated)
            {
                if(imageFile == null)
                {
                    updating = false;
                    Intent intent = new Intent(editPlayerActivity, PlayersActivity.class);
                    intent.putExtra("id_team", player.getId_team());
                    editPlayerActivity.startActivity(intent);
                }
                else
                {
                    playerRepository.upload(player.getId(), imageFile, new PlayerRepository.OnUploadPlayerRepositoryListener()
                    {
                        @Override
                        public void onSuccess(Boolean uploaded)
                        {
                            updating = false;
                            Intent intent = new Intent(editPlayerActivity, PlayersActivity.class);
                            intent.putExtra("id_team", player.getId_team());
                            editPlayerActivity.startActivity(intent);
                        }

                        @Override
                        public void onError()
                        {
                            updating = false;
                            Intent intent = new Intent(editPlayerActivity, PlayersActivity.class);
                            intent.putExtra("id_team", player.getId_team());
                            editPlayerActivity.startActivity(intent);
                        }
                    });
                }
            }

            @Override
            public void onError()
            {
                updating = false;
                editPlayerActivity.setLoading(false);
                Toast.makeText(editPlayerActivity, editPlayerActivity.getText(R.string.toastUpdatingError), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void setEditPlayerActivity(EditPlayerActivity editPlayerActivity)
    {
        this.editPlayerActivity = editPlayerActivity;

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(editPlayerActivity);
        server = sharedPreferences.getString("server", "localhost");
        playerRepository = new PlayerRepository(server);
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

    public String getFirstname()
    {
        return firstname;
    }

    public void setFirstname(String firstname)
    {
        this.firstname = firstname;
    }

    public String getLastname()
    {
        return lastname;
    }

    public void setLastname(String lastname)
    {
        this.lastname = lastname;
    }

    public void saveSelectedImageInFile(Uri uri)
    {
        Bitmap bitmap = null;
        if(Build.VERSION.SDK_INT < 28)
        {
            try
            {
                bitmap = MediaStore.Images.Media.getBitmap(editPlayerActivity.getContentResolver(), uri);
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
                final InputStream in = editPlayerActivity.getContentResolver().openInputStream(uri);
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
        File file = new File(editPlayerActivity.getFilesDir(), "temp.jpg");
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

    public String getServer()
    {
        return server;
    }
}