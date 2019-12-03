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
import com.example.equipos.view.AddPlayerActivity;
import com.example.equipos.view.PlayersActivity;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class AddPlayerActivityVM extends AndroidViewModel
{
    private Uri image;
    private String firstname, lastname;

    private AddPlayerActivity addPlayerActivity;
    private String server;
    private PlayerRepository playerRepository;
    private boolean adding;
    private File imageFile;

    public AddPlayerActivityVM(@NonNull Application application)
    {
        super(application);
    }

    public boolean isAdding()
    {
        return adding;
    }

    public void setAdding(boolean adding)
    {
        this.adding = adding;
    }

    public void add(final Player player)
    {
        adding = true;
        playerRepository.add(player, new PlayerRepository.OnAddPlayerRepositoryListener()
        {
            @Override
            public void onSuccess(Long id)
            {
                if(imageFile == null)
                {
                    adding = false;
                    Intent intent = new Intent(addPlayerActivity, PlayersActivity.class);
                    intent.putExtra("id_team", player.getId_team());
                    addPlayerActivity.startActivity(intent);
                }
                else
                {
                    playerRepository.upload(id, imageFile, new PlayerRepository.OnUploadPlayerRepositoryListener()
                    {
                        @Override
                        public void onSuccess(Boolean uploaded)
                        {
                            adding = false;
                            Intent intent = new Intent(addPlayerActivity, PlayersActivity.class);
                            intent.putExtra("id_team", player.getId_team());
                            addPlayerActivity.startActivity(intent);
                        }

                        @Override
                        public void onError()
                        {
                            adding = false;
                            Intent intent = new Intent(addPlayerActivity, PlayersActivity.class);
                            intent.putExtra("id_team", player.getId_team());
                            addPlayerActivity.startActivity(intent);
                        }
                    });
                }
            }

            @Override
            public void onError()
            {
                adding = false;
                addPlayerActivity.setLoading(false);
                Toast.makeText(addPlayerActivity, addPlayerActivity.getText(R.string.toastAddingError), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void setAddPlayerActivity(AddPlayerActivity addPlayerActivity)
    {
        this.addPlayerActivity = addPlayerActivity;

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(addPlayerActivity);
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
                bitmap = MediaStore.Images.Media.getBitmap(addPlayerActivity.getContentResolver(), uri);
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
                final InputStream in = addPlayerActivity.getContentResolver().openInputStream(uri);
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
        File file = new File(addPlayerActivity.getFilesDir(), "temp.jpg");
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
