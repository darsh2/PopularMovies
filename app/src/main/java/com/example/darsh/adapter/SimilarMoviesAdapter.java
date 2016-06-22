package com.example.darsh.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.darsh.model.Movie;
import com.example.darsh.popularmovies.R;

import java.util.ArrayList;

/**
 * Created by darshan on 21/6/16.
 */
public class SimilarMoviesAdapter extends RecyclerView.Adapter<SimilarMoviesAdapter.ViewHolder> {
    private ArrayList<Movie> movies;

    private OnMovieClickListener listener;
    private Context context;

    private final String BASE_URL = "http://image.tmdb.org/t/p/w185";

    public SimilarMoviesAdapter(Fragment fragment) {
        this.listener = (OnMovieClickListener) fragment;
        this.context = fragment.getContext();
        this.movies = new ArrayList<>();
    }

    public void setSimilarMovies(ArrayList<Movie> similarMovies) {
        this.movies = similarMovies;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_item_thumbnail, parent, false);
        final ViewHolder viewHolder = new ViewHolder(cardView);
        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onMovieClick(movies.get(viewHolder.getAdapterPosition()));
                }
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Glide.with(context)
                .load(BASE_URL + movies.get(position).getPosterPath())
                .placeholder(R.drawable.image_placeholder)
                .into(holder.imageView);
        holder.textView.setText(movies.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public CardView cardView;
        public ImageView imageView;
        public TextView textView;

        public ViewHolder(CardView cardView) {
            super(cardView);
            this.cardView = cardView;
            this.imageView = (ImageView) cardView.findViewById(R.id.image_view_thumbnail);
            this.textView = (TextView) cardView.findViewById(R.id.text_view_name);
        }
    }

    public interface OnMovieClickListener {
        void onMovieClick(Movie movie);
    }
}
