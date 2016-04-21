package com.example.darsh.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.example.darsh.helper.Constants;

import java.util.ArrayList;

/**
 * Created by darshan on 18/4/16.
 */
public class EndlessScrollRecyclerView extends RecyclerView {
    private final String TAG = this.getClass().getSimpleName();

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
        if (Constants.DEBUG) Log.i(TAG, "+initialiseView()");
        FooterView footerView = new FooterView(context);
        footerView.setVisibility(GONE);
        addFooterView(footerView);
        if (Constants.DEBUG) Log.i(TAG, "-initialiseView()");
    }

    private void addFooterView(final View view) {
        if (Constants.DEBUG) Log.i(TAG, "+addFooterView()");
        footerViews.clear();
        footerViews.add(view);
        if (Constants.DEBUG) Log.i(TAG, "-addFooterView()");
    }

    @Override
    public void setAdapter(Adapter adapter) {
        if (Constants.DEBUG) Log.i(TAG, "+setAdapter()");
        this.adapter = adapter;
        this.adapter.registerAdapterDataObserver(dataObserver);
        wrapAdapter = new WrapAdapter(this.adapter, footerViews);
        super.setAdapter(wrapAdapter);

        dataObserver.onChanged();

        if (Constants.DEBUG) Log.i(TAG, "-setAdapter()");
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);

        if (Constants.DEBUG) Log.i(TAG, "+onScrollStateChanged()");

        if (Constants.DEBUG) {
            Log.i(TAG, "Child count: " + getLayoutManager().getChildCount());
            Log.i(TAG, "Last visible item position: " + ((GridLayoutManager) getLayoutManager()).findLastVisibleItemPosition());
            Log.i(TAG, "Item count: " + getLayoutManager().getItemCount());
            Log.i(TAG, "Scroll state: " + state);
            Log.i(TAG, "isLoading: " + isLoading);
        }

        if (state == RecyclerView.SCROLL_STATE_IDLE &&
                loadingListener != null &&
                !isLoading) {
            LayoutManager layoutManager = getLayoutManager();
            int lastVisibleItemPosition = -1;
            if (layoutManager instanceof GridLayoutManager) {
                lastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
            }



            if (layoutManager.getChildCount() > 0 &&
                    lastVisibleItemPosition >= layoutManager.getItemCount() - 1 &&
                    layoutManager.getItemCount() > layoutManager.getChildCount() &&
                    !isNoMore) {
                View footerView = footerViews.get(0);
                isLoading = true;
                if (footerView instanceof FooterView) {
                    ((FooterView) footerView).setState(Constants.LOADING);
                } else {
                    footerView.setVisibility(View.VISIBLE);
                }
                loadingListener.onLoadMore();
            }

            if (Constants.DEBUG) Log.i(TAG, "-onScrollStateChanged()");
        } else {
            if (Constants.DEBUG) Log.i(TAG, "-onScrollStateChanged()");
        }
    }

    public void loadingComplete() {
        if (Constants.DEBUG) Log.i(TAG, "loadingComplete");
        isLoading = false;
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

    public interface LoadingListener {
        void onLoadMore();
    }

    public void setLoadingListener(LoadingListener loadingListener) {
        this.loadingListener = loadingListener;
    }
}
