package com.example.darsh.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.darsh.helper.Constants;
import com.example.darsh.popularmovies.R;

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
public class FooterView extends LinearLayout {
    private TextView textView;

    public FooterView(Context context) {
        this(context, null);
    }

    public FooterView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FooterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialiseView(context);
    }

    private void initialiseView(Context context) {
        ViewGroup.LayoutParams parentLayoutParams = new ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        );
        setLayoutParams(parentLayoutParams);
        setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        int margin = (int) getResources().getDimension(R.dimen.progress_bar_margin);
        layoutParams.setMargins(margin, margin, margin, margin);

        textView = new TextView(context);
        textView.setLayoutParams(layoutParams);
        addView(textView);
    }

    /**
     * Helper function to set visibility of this
     * view and the text displayed based on state.
     * @param state Integer describing the possible
     *              states ({@link Constants#LOADING},
     *              {@link Constants#DONE},
     *              {@link Constants#NO_MORE},
     *              {@link Constants#NETWORK_ERROR},
     *              {@link Constants#SERVER_ERROR},
     *              {@link Constants#LOADING_FAVORITES},
     *              {@link Constants#NONE},
     *              {@link Constants#CURSOR_ERROR})
     *              the view can be in.
     */
    public void setState(int state) {
        switch (state) {
            case Constants.LOADING: {
                textView.setText(getContext().getString(R.string.loading));
                setVisibility(VISIBLE);
                break;
            }

            case Constants.DONE: {
                setVisibility(INVISIBLE);
                break;
            }

            case Constants.NO_MORE: {
                textView.setText(getContext().getString(R.string.no_more));
                setVisibility(VISIBLE);
                break;
            }

            case Constants.NETWORK_ERROR: {
                textView.setText(getContext().getString(R.string.network_error_movie_list));
                setVisibility(VISIBLE);
                break;
            }

            case Constants.SERVER_ERROR: {
                textView.setText(getContext().getString(R.string.server_error));
                setVisibility(VISIBLE);
                break;
            }

            case Constants.LOADING_FAVORITES: {
                setVisibility(INVISIBLE);
                break;
            }

            case Constants.NONE: {
                textView.setText(getContext().getString(R.string.no_favorites));
                setVisibility(VISIBLE);
                break;
            }

            case Constants.CURSOR_ERROR: {
                textView.setText(getContext().getString(R.string.cursor_error));
                setVisibility(VISIBLE);
                break;
            }
        }
    }
}
