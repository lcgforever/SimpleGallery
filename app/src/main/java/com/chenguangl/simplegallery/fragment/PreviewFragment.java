package com.chenguangl.simplegallery.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.chenguangl.simplegallery.R;
import com.chenguangl.simplegallery.data.Album;
import com.chenguangl.simplegallery.data.Photo;
import com.chenguangl.simplegallery.util.ImageUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PreviewFragment extends Fragment {

    private static final String EXTRA_ALBUM = "EXTRA_ALBUM";

    private ViewPager viewPager;
    private Album album;

    public static PreviewFragment newInstance(Album album) {
        PreviewFragment fragment = new PreviewFragment();
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_ALBUM, album);
        fragment.setArguments(args);
        fragment.setRetainInstance(true);
        return fragment;
    }

    public PreviewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        album = (Album) getArguments().getSerializable(EXTRA_ALBUM);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_preview, container, false);
        viewPager = (ViewPager) view.findViewById(R.id.preview_photo_view_pager);
        viewPager.setAdapter(new PreviewPagerAdapter(album.getPhotoList()));
        viewPager.setCurrentItem(0);

        ScaleAnimation translation;
        translation = new ScaleAnimation(0.95f, 1F, 0.95f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        translation.setStartOffset(0);
        translation.setDuration(600);
        translation.setFillAfter(true);
        translation.setInterpolator(new BounceInterpolator());
        view.startAnimation(translation);
        return view;
    }

    public void navigatePrev() {
        int position = viewPager.getCurrentItem();
        if (position > 0) {
            viewPager.setCurrentItem(position - 1);
        }
    }

    public void navigateNext() {
        int position = viewPager.getCurrentItem();
        if (position + 1 < viewPager.getAdapter().getCount()) {
            viewPager.setCurrentItem(position + 1);
        }
    }


    private class PreviewPagerAdapter extends PagerAdapter {

        private List<Photo> photoList;
        private LayoutInflater layoutInflater;

        PreviewPagerAdapter(List<Photo> photoList) {
            this.photoList = photoList;
            layoutInflater = LayoutInflater.from(getContext());
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Photo photo = photoList.get(position);
            View view = layoutInflater.inflate(R.layout.view_pager_item_photo_preview, container, false);
            ImageView imageView = (ImageView) view.findViewById(R.id.preview_image_view);
            String uri = photo.getUri();
            Picasso.with(getContext())
                    .load(uri)
                    .rotate(ImageUtils.getRotationInDegrees(getContext(), uri))
                    .into(imageView);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return photoList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}
