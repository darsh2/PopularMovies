package com.example.darsh.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.darsh.model.Movie;
import com.example.darsh.popularmovies.R;

import java.util.ArrayList;

/**
 * Created by darshan on 19/4/16.
 */
public class MoviesListAdapter extends RecyclerView.Adapter<MoviesListAdapter.ViewHolder> {
    private final String TAG = this.getClass().getSimpleName();

    private Context context;
    private ArrayList<Movie> movies;

    private final String BASE_URL = "http://image.tmdb.org/t/p/w185";

    public MoviesListAdapter(Context context, ArrayList<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ImageView imageView = (ImageView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grid_view_item_movie, parent, false);
        ViewHolder viewHolder = new ViewHolder(imageView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Glide.with(context)
                .load(BASE_URL + movies.get(position).getPosterPath())
                .placeholder(R.drawable.image_placeholder)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }



    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;

        public ViewHolder(ImageView imageView) {
            super(imageView);
            this.imageView = imageView;
        }
    }
}
