package com.kda.kdatalk.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.kda.kdatalk.R;

public class ProgressView extends RelativeLayout {

    public LinearLayout progress;

    public ProgressView(Context context) {
        super(context);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.kda_progress_layout, this, true);

        progress = (LinearLayout) getChildAt(0);
        progress.setVisibility(VISIBLE);

    }


    public ProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.kda_progress_layout, this, true);


        progress = (LinearLayout) getChildAt(0);
        progress.setVisibility(VISIBLE);
    }

    public ProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.kda_progress_layout, this, true);


        progress = (LinearLayout) getChildAt(0);
        progress.setVisibility(VISIBLE);
    }

    public ProgressView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.kda_progress_layout, this, true);


        progress = (LinearLayout) getChildAt(0);
        progress.setVisibility(VISIBLE);
    }

}
