package com.chenguangl.simplegallery.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chenguangl.simplegallery.R;
import com.chenguangl.simplegallery.adapter.AlbumAdapter;
import com.chenguangl.simplegallery.data.Album;
import com.chenguangl.simplegallery.fragment.PreviewFragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AlbumAdapter.AlbumItemClickListener {

    private static final String EXTRA_ALBUM_LIST = "EXTRA_ALBUM_LIST";

    private ArrayList<Album> albumList;
    private PreviewFragment previewFragment;

    public static void start(Context context, ArrayList<Album> albumList) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(EXTRA_ALBUM_LIST, albumList);
        context.startActivity(intent);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getIntent() != null) {
            albumList = (ArrayList<Album>) getIntent().getSerializableExtra(EXTRA_ALBUM_LIST);
        } else {
            albumList = (ArrayList<Album>) savedInstanceState.getSerializable(EXTRA_ALBUM_LIST);
        }

        RecyclerView albumRecyclerView = (RecyclerView) findViewById(R.id.album_recycler_view);
        albumRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        AlbumAdapter albumAdapter = new AlbumAdapter(this, albumList, this);
        albumRecyclerView.setAdapter(albumAdapter);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(EXTRA_ALBUM_LIST, albumList);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onAlbumClicked(View view, Album album) {
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this, view, getString(R.string.transition_name));
        PhotoListActivity.start(this, album, optionsCompat);
    }

    @Override
    public void onLongPressed(Album album) {
        previewFragment = PreviewFragment.newInstance(album);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.preview_fragment_container, previewFragment)
                .commit();
    }

    @Override
    public void onSwipeRight() {
        if (previewFragment != null) {
            previewFragment.navigatePrev();
        }
    }

    @Override
    public void onSwipeLeft() {
        if (previewFragment != null) {
            previewFragment.navigateNext();
        }
    }

    @Override
    public void onRelease() {
        if (previewFragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .remove(previewFragment)
                    .commit();
            previewFragment = null;
        }
    }
}
