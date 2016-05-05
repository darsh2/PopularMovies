package com.example.darsh.adapter;

import android.app.Activity;
import android.content.Context;
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
 * Created by darshan on 19/4/16.
 */
public class MoviesListAdapter extends RecyclerView.Adapter<MoviesListAdapter.ViewHolder> {
    private OnMovieClickListener onMovieClickListener;
    private Context context;
    private ArrayList<Movie> movies;

    private final String BASE_URL = "http://image.tmdb.org/t/p/w185";

    public MoviesListAdapter(Activity activity, ArrayList<Movie> movies) {
        this.onMovieClickListener = (OnMovieClickListener) activity;
        this.context = activity.getApplicationContext();
        this.movies = movies;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_item_movie, parent, false);
        final ViewHolder viewHolder = new ViewHolder(cardView);
        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onMovieClickListener != null) {
                    onMovieClickListener.onMovieClick(movies.get(viewHolder.getAdapterPosition()));
                }
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Glide.with(context)
                .load(BASE_URL + movies.get(position).getPosterPath())
                .placeholder(R.drawable.image_placeholder)
                .into(holder.poster);
        holder.title.setText(movies.get(position).getTitle());
        String rating = Double.toString(movies.get(position).getVoteAverage());
        holder.rating.setText(rating);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public CardView cardView;

        public ImageView poster;
        public TextView title;
        public TextView rating;

        public ViewHolder(CardView cardView) {
            super(cardView);
            this.cardView = cardView;
            this.poster = (ImageView) cardView.findViewById(R.id.image_view_poster);
            this.title = (TextView) cardView.findViewById(R.id.text_view_title);
            this.rating = (TextView) cardView.findViewById(R.id.text_view_rating);
        }
    }

    public interface OnMovieClickListener {
        void onMovieClick(Movie movie);
    }
}
