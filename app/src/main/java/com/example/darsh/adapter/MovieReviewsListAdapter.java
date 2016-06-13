package com.example.darsh.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.darsh.model.MovieReview;
import com.example.darsh.popularmovies.R;

import java.util.ArrayList;

/**
 * Created by darshan on 10/6/16.
 */
public class MovieReviewsListAdapter extends RecyclerView.Adapter<MovieReviewsListAdapter.ViewHolder>{
    private Context context;
    private ArrayList<MovieReview> movieReviews;

    public MovieReviewsListAdapter(Context context, ArrayList<MovieReview> movieReviews) {
        this.context = context;
        this.movieReviews = movieReviews;
    }

    @Override
    public MovieReviewsListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.recycler_view_item_movie_review, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieReviewsListAdapter.ViewHolder holder, int position) {
        holder.author.setText(movieReviews.get(position).getAuthor());
        holder.content.setText(movieReviews.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return movieReviews.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View view;
        public TextView author;
        public TextView content;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            this.author = (TextView) view.findViewById(R.id.text_view_review_author);
            this.content = (TextView) view.findViewById(R.id.text_view_review_content);
        }
    }
}
