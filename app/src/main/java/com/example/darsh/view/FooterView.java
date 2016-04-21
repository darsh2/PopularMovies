package com.example.darsh.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.example.darsh.helper.Constants;
import com.example.darsh.popularmovies.R;

/**
 * Created by darshan on 18/4/16.
 */
public class FooterView extends LinearLayout {
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
        setLayoutParams(parentLayoutParams);

        ProgressBar progressBar = new ProgressBar(context);
        progressBar.setLayoutParams(layoutParams);
        progressBar.setIndeterminate(true);
        addView(progressBar);
    }

    public void setState(int state) {
        switch (state) {
            case Constants.LOADING: {
                setVisibility(VISIBLE);
                break;
            }

            case Constants.DONE: {
                setVisibility(INVISIBLE);
                break;
            }

            case Constants.NO_MORE: {
                setVisibility(GONE);
                break;
            }
        }
    }
}
