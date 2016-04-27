package com.example.darsh.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.darsh.model.Genre;
import com.example.darsh.popularmovies.R;

import java.util.ArrayList;

/**
 * Created by darshan on 27/4/16.
 */
public class GenresListAdapter extends RecyclerView.Adapter<GenresListAdapter.ViewHolder> {
    private ArrayList<Genre> genres;

    public GenresListAdapter(ArrayList<Genre> genres) {
        this.genres = genres;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TextView textView = (TextView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_item_genre, parent, false);
        ViewHolder viewHolder = new ViewHolder(textView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textView.setText(genres.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return genres.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;

        public ViewHolder(TextView textView) {
            super(textView);
            this.textView = textView;
        }
    }
}
