package com.gjjg.textbook_app_java;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.cardview.widget.CardView;

public class FeedAsciiItem extends LinearLayout {

    public FeedAsciiItem(Context context) {
        super(context);
    }

    public FeedAsciiItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FeedAsciiItem(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(heightMeasureSpec, heightMeasureSpec);
    }
}