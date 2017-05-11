package com.chenguangl.simplegallery.adapter;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chenguangl.simplegallery.R;
import com.chenguangl.simplegallery.data.Album;
import com.chenguangl.simplegallery.util.ImageUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder> {

    public interface AlbumItemClickListener {

        void onAlbumClicked(View view, Album album);

        void onLongPressed(Album album);

        void onSwipeRight();

        void onSwipeLeft();

        void onRelease();
    }

    private Context context;
    private LayoutInflater layoutInflater;
    private List<Album> albumList;
    private AlbumItemClickListener albumItemClickListener;

    public AlbumAdapter(Context context, List<Album> albumList, AlbumItemClickListener albumItemClickListener) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.albumList = albumList;
        this.albumItemClickListener = albumItemClickListener;
    }

    @Override
    public AlbumViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.list_item_album, parent, false);
        return new AlbumViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AlbumViewHolder holder, int position) {
        Album album = albumList.get(position);
        String coverImageUri = album.getPhotoList().get(0).getUri();
        Picasso.with(context)
                .load(coverImageUri)
                .rotate(ImageUtils.getRotationInDegrees(context, coverImageUri))
                .into(holder.coverImageView);
        String name = album.getName();
        if (TextUtils.isEmpty(name)) {
            holder.nameTextView.setVisibility(View.GONE);
        } else {
            holder.nameTextView.setText(name);
            holder.nameTextView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return albumList == null ? 0 : albumList.size();
    }

    class AlbumViewHolder extends RecyclerView.ViewHolder {

        private static final int SWIPE_MIN_DISTANCE = 60;
        private static final int LONG_PRESS_DELAY = 1000;

        ImageView coverImageView;
        TextView nameTextView;

        AlbumViewHolder(View itemView) {
            super(itemView);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                itemView.setTransitionName(context.getString(R.string.transition_name));
            }
            coverImageView = (ImageView) itemView.findViewById(R.id.album_cover_image_view);
            nameTextView = (TextView) itemView.findViewById(R.id.album_name_text_view);

            itemView.setOnTouchListener(new SimpleTouchListener());
        }

        private class SimpleTouchListener implements View.OnTouchListener {

            private Handler handler;
            private Runnable longPressRunnable;
            private float xDown, xUp;
            private boolean isLongPressed;

            SimpleTouchListener() {
                handler = new Handler();
                longPressRunnable = new Runnable() {
                    public void run() {
                        isLongPressed = true;
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            albumItemClickListener.onLongPressed(albumList.get(position));
                        }
                    }
                };
            }

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    xDown = event.getX();
                    handler.postDelayed(longPressRunnable, LONG_PRESS_DELAY);
                    return false;
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    handler.removeCallbacks(longPressRunnable);
                    xUp = event.getX();
                    if (isLongPressed) {
                        isLongPressed = false;
                        albumItemClickListener.onRelease();
                        return true;
                    } else {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            albumItemClickListener.onAlbumClicked(itemView, albumList.get(position));
                        }
                        return false;
                    }
                } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    handler.removeCallbacks(longPressRunnable);
                    xUp = event.getX();
                    if ((xUp - xDown) > SWIPE_MIN_DISTANCE) {
                        xDown = xUp;
                        if (isLongPressed) {
                            albumItemClickListener.onSwipeRight();
                        }
                    } else if ((xUp - xDown) < -SWIPE_MIN_DISTANCE) {
                        xDown = xUp;
                        if (isLongPressed) {
                            albumItemClickListener.onSwipeLeft();
                        }
                    }
                    return true;
                } else {
                    handler.removeCallbacks(longPressRunnable);
                    if (event.getAction() == MotionEvent.ACTION_CANCEL) {
                        if (isLongPressed) {
                            isLongPressed = false;
                            albumItemClickListener.onRelease();
                        }
                    }
                    return true;
                }
            }
        }
    }
}
