package com.example.darsh.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.example.darsh.helper.Constants;

import java.util.ArrayList;

/*
Copyright 2015 jianghejie

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

/**
 * Modified by darshan on 18/4/16.
 */
public class EndlessScrollRecyclerView extends RecyclerView {
    private Adapter adapter;
    private Adapter wrapAdapter;

    private ArrayList<View> footerViews = new ArrayList<>();

    private boolean isLoading;
    private boolean isNoMore;
    private int previousTotal = 0;

    private LoadingListener loadingListener;

    public EndlessScrollRecyclerView(Context context) {
        this(context, null);
    }

    public EndlessScrollRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EndlessScrollRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialiseView(context);
    }

    private void initialiseView(Context context) {
        FooterView footerView = new FooterView(context);
        footerView.setVisibility(GONE);
        addFooterView(footerView);
    }

    private void addFooterView(final View view) {
        footerViews.clear();
        footerViews.add(view);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        this.adapter = adapter;
        this.adapter.registerAdapterDataObserver(dataObserver);
        wrapAdapter = new WrapAdapter(this.adapter, footerViews);
        super.setAdapter(wrapAdapter);

        dataObserver.onChanged();
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        if (state == RecyclerView.SCROLL_STATE_IDLE &&
                loadingListener != null &&
                !isLoading) {
            LayoutManager layoutManager = getLayoutManager();
            int lastVisibleItemPosition = -1;
            if (layoutManager instanceof GridLayoutManager) {
                lastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
            }

            if ((layoutManager.getChildCount() > 0 &&
                    lastVisibleItemPosition >= layoutManager.getItemCount() - footerViews.size() &&
                    layoutManager.getItemCount() > layoutManager.getChildCount() &&
                    !isNoMore) ||
                    (lastVisibleItemPosition == 0 && layoutManager.getChildCount() == 1 && layoutManager.getItemCount() == 1)) {
                View footerView = footerViews.get(0);
                if (footerView instanceof FooterView) {
                    ((FooterView) footerView).setState(Constants.LOADING);
                } else {
                    footerView.setVisibility(View.VISIBLE);
                }
                loadingListener.onLoadMore();
            }
        }
    }

    public void loadingComplete() {
        View footerView = footerViews.get(0);
        if (previousTotal < getLayoutManager().getItemCount()) {
            if (footerView instanceof FooterView) {
                ((FooterView) footerView).setState(Constants.DONE);
            } else {
                footerView.setVisibility(View.GONE);
            }
        } else {
            if (footerView instanceof FooterView) {
                ((FooterView) footerView).setState(Constants.NO_MORE);
            } else {
                footerView.setVisibility(View.GONE);
            }
            footerViews.clear();
            isNoMore = true;
        }
        previousTotal = getLayoutManager().getItemCount();
    }

    public void setState(int code) {
        if (footerViews == null || footerViews.size() == 0) {
            return;
        }
        View footerView = footerViews.get(0);
        if (footerView instanceof FooterView) {
            ((FooterView) footerView).setState(code);
        } else {
            footerView.setVisibility(VISIBLE);
        }
    }

    public void setIsLoading(boolean isLoading) {
        this.isLoading = isLoading;
    }

    private final RecyclerView.AdapterDataObserver dataObserver = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            if (previousTotal == adapter.getItemCount()) {
                return;
            }

            if (wrapAdapter != null) {
                wrapAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            wrapAdapter.notifyItemRangeInserted(positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            wrapAdapter.notifyItemRangeChanged(positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            wrapAdapter.notifyItemRangeChanged(positionStart, itemCount, payload);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            wrapAdapter.notifyItemMoved(fromPosition, toPosition);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            wrapAdapter.notifyItemRangeRemoved(positionStart, itemCount);
        }
    };

    private class WrapAdapter extends RecyclerView.Adapter {
        private RecyclerView.Adapter adapter;
        private ArrayList<View> footerViews;

        public WrapAdapter(RecyclerView.Adapter adapter, ArrayList<View> footerViews) {
            this.adapter = adapter;
            this.footerViews = footerViews;
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (layoutManager instanceof GridLayoutManager) {
                final GridLayoutManager gridManager = ((GridLayoutManager) layoutManager);
                gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        return isFooter(position) ? gridManager.getSpanCount() : 1;
                    }
                });
            }
        }

        private boolean isFooter(int position) {
            return position < getItemCount() &&
                    position >= getItemCount() - footerViews.size();
        }

        public int getFootersCount() {
            return footerViews.size();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == Constants.FOOTER) {
                return new SimpleViewHolder(footerViews.get(0));
            }
            return adapter.onCreateViewHolder(parent, viewType);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (adapter != null) {
                int adapterCount = adapter.getItemCount();
                if (position < adapterCount) {
                    adapter.onBindViewHolder(holder, position);
                }
            }
        }

        @Override
        public int getItemCount() {
            if (adapter != null) {
                return getFootersCount() + adapter.getItemCount();
            }
            return getFootersCount();
        }

        @Override
        public int getItemViewType(int position) {
            if (isFooter(position)) {
                return Constants.FOOTER;
            }

            if (adapter != null) {
                int adapterCount = adapter.getItemCount();
                if (position < adapterCount) {
                    return adapter.getItemViewType(position);
                }
            }
            return Constants.NORMAL;
        }

        @Override
        public long getItemId(int position) {
            if (adapter != null) {
                int adapterCount = adapter.getItemCount();
                if (position < adapterCount) {
                    return adapter.getItemId(position);
                }
            }
            return -1;
        }

        @Override
        public void unregisterAdapterDataObserver(AdapterDataObserver observer) {
            if (adapter != null) {
                adapter.unregisterAdapterDataObserver(observer);
            }
        }

        @Override
        public void registerAdapterDataObserver(AdapterDataObserver observer) {
            if (adapter != null) {
                adapter.registerAdapterDataObserver(observer);
            }
        }

        private class SimpleViewHolder extends RecyclerView.ViewHolder {
            public SimpleViewHolder(View itemView) {
                super(itemView);
            }
        }
    }

    /**
     * Interface to load more movies when recycler view
     * scrolls to the end of current list.
     */
    public interface LoadingListener {
        void onLoadMore();
    }

    public void setLoadingListener(LoadingListener loadingListener) {
        this.loadingListener = loadingListener;
    }
}
