package com.chenguangl.simplegallery.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.chenguangl.simplegallery.R;
import com.chenguangl.simplegallery.adapter.PhotoListAdapter;
import com.chenguangl.simplegallery.data.Album;

public class PhotoListActivity extends AppCompatActivity {

    private static final String EXTRA_ALBUM = "EXTRA_ALBUM";

    private Album album;

    public static void start(Context context, Album album, ActivityOptionsCompat optionsCompat) {
        Intent intent = new Intent(context, PhotoListActivity.class);
        intent.putExtra(EXTRA_ALBUM, album);
        context.startActivity(intent, optionsCompat.toBundle());
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_list);

        if (getIntent() != null) {
            album = (Album) getIntent().getSerializableExtra(EXTRA_ALBUM);
        } else {
            album = (Album) savedInstanceState.getSerializable(EXTRA_ALBUM);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.photo_list_activity_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(album.getName());
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        RecyclerView photoRecyclerView = (RecyclerView) findViewById(R.id.photo_list_recycler_view);
        photoRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        PhotoListAdapter photoListAdapter = new PhotoListAdapter(this, album.getPhotoList());
        photoRecyclerView.setAdapter(photoListAdapter);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(EXTRA_ALBUM, album);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getSupportActionBar().hide();
                supportFinishAfterTransition();
                break;
        }
        return true;
    }
}
