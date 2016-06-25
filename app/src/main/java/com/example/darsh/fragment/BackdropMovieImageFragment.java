package com.example.darsh.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.darsh.helper.Constants;
import com.example.darsh.popularmovies.R;

/**
 * Created by darshan on 22/6/16.
 */

/**
 * This fragment is used to load movie backdrop images in
 * the viewPager. In case of a gallery app, it is advised
 * to use this style of creating a viewPager of full sized
 * images instead of using a viewPager with imageViews.
 * It can lead to app crash due to {@link OutOfMemoryError}
 * as the imageViews are retained in memory. Fragments are
 * recycled. In this scenario, at most five images will be
 * present so either method can be used. Used the fragment
 * method just for better understanding.
 */
public class BackdropMovieImageFragment extends Fragment {
    private String filePath;

    private final String BACKDROP_IMAGE_URL = "http://image.tmdb.org/t/p/w500";

    public BackdropMovieImageFragment() {}

    public static BackdropMovieImageFragment getInstance(String filePath) {
        BackdropMovieImageFragment fragment = new BackdropMovieImageFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.BUNDLE_FILE_PATH, filePath);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        filePath = getArguments().getString(Constants.BUNDLE_FILE_PATH);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ImageView imageView = new ImageView(getContext());
        imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);

        Glide.with(getContext())
                .load(BACKDROP_IMAGE_URL + filePath)
                .placeholder(R.drawable.image_placeholder)
                .into(imageView);

        return imageView;
    }
}
