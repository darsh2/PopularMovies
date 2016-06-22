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
import com.example.darsh.model.MovieVideo;
import com.example.darsh.popularmovies.R;

import java.util.ArrayList;

/**
 * Created by darshan on 8/6/16.
 */
public class VideosListAdapter extends RecyclerView.Adapter<VideosListAdapter.ViewHolder> {
    private ArrayList<MovieVideo> videos;

    private OnVideoClickListener listener;
    private Context context;

    private final String YOUTUBE_THUMBNAIL_URL = "http://img.youtube.com/vi/%s/0.jpg";

    public VideosListAdapter(Fragment fragment) {
        this.listener = (OnVideoClickListener) fragment;
        this.context = fragment.getContext();
        this.videos = new ArrayList<>();
    }

    public void setVideos(ArrayList<MovieVideo> videos) {
        this.videos.addAll(videos);
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
                    listener.onVideoClick(videos.get(viewHolder.getAdapterPosition()));
                }
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Glide.with(context)
                .load(String.format(YOUTUBE_THUMBNAIL_URL, videos.get(position).getKey()))
                .placeholder(R.drawable.image_placeholder)
                .into(holder.imageView);
        holder.textView.setText(videos.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return videos.size();
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

    public interface OnVideoClickListener {
        void onVideoClick(MovieVideo movieVideo);
    }
}
