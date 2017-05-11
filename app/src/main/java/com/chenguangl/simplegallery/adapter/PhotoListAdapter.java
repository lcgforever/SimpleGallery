package com.chenguangl.simplegallery.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chenguangl.simplegallery.R;
import com.chenguangl.simplegallery.data.Photo;
import com.chenguangl.simplegallery.util.ImageUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PhotoListAdapter extends RecyclerView.Adapter<PhotoListAdapter.PhotoViewHolder> {

    private Context context;
    private LayoutInflater layoutInflater;
    private List<Photo> photoList;

    public PhotoListAdapter(Context context, List<Photo> photoList) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.photoList = photoList;
    }

    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.list_item_photo, parent, false);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PhotoViewHolder holder, int position) {
        Photo photo = photoList.get(position);
        String name = photo.getName();
        String uri = photo.getUri();
        Picasso.with(context)
                .load(uri)
                .rotate(ImageUtils.getRotationInDegrees(context, uri))
                .into(holder.photoImageView);
        if (TextUtils.isEmpty(name)) {
            holder.nameTextView.setVisibility(View.GONE);
        } else {
            holder.nameTextView.setText(name);
            holder.nameTextView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return photoList == null ? 0 : photoList.size();
    }

    class PhotoViewHolder extends RecyclerView.ViewHolder {

        ImageView photoImageView;
        TextView nameTextView;

        PhotoViewHolder(View itemView) {
            super(itemView);

            photoImageView = (ImageView) itemView.findViewById(R.id.photo_image_view);
            nameTextView = (TextView) itemView.findViewById(R.id.photo_name_text_view);
        }
    }
}
