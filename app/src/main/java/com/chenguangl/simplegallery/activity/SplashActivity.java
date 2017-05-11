package com.chenguangl.simplegallery.activity;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.chenguangl.simplegallery.R;
import com.chenguangl.simplegallery.data.Album;
import com.chenguangl.simplegallery.data.Photo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        loadSampleAssets();
    }

    private void loadSampleAssets() {
        AssetManager assetManager = getAssets();
        try {
            ArrayList<Album> albumList = new ArrayList<>();
            String[] albums = assetManager.list("samples");
            for (String albumName : albums) {
                Album album = new Album();
                album.setName(albumName);
                List<Photo> photoList = new ArrayList<>();
                String[] photos = assetManager.list("samples" + File.separator + albumName);
                for (String photoName : photos) {
                    Photo photo = new Photo();
                    photo.setName(photoName);
                    photo.setUri("file:///android_asset/samples/" + albumName + File.separator + photoName);
                    photoList.add(photo);
                }
                album.setPhotoList(photoList);
                albumList.add(album);
            }

            MainActivity.start(this, albumList);
            finish();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, R.string.load_assets_error_message, Toast.LENGTH_SHORT).show();
        }
    }
}
