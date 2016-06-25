package com.example.darsh.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.darsh.fragment.BackdropMovieImageFragment;
import com.example.darsh.model.MovieImage;

import java.util.ArrayList;

/**
 * Created by darshan on 22/6/16.
 */
public class BackdropMovieImagesAdapter extends FragmentStatePagerAdapter {
    private ArrayList<MovieImage> movieImages = new ArrayList<>();

    public BackdropMovieImagesAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    public void addMovieImages(ArrayList<MovieImage> movieImages) {
        this.movieImages.clear();
        this.movieImages.addAll(movieImages);
    }

    @Override
    public Fragment getItem(int position) {
        return BackdropMovieImageFragment.getInstance(movieImages.get(position).getFilePath());
    }

    @Override
    public int getCount() {
        return movieImages.size();
    }

    @Override
    public int getItemPosition(Object object) {
        /*
        Dynamically update fragments in ViewPager.
         */
        return POSITION_NONE;
    }
}
