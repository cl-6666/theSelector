package com.cl.picture_selector.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;


/**
 * 正方形RelativeLayout
 */
public class SquareRelativeLayout extends RelativeLayout {

    public SquareRelativeLayout(Context context) {
        this(context, null);
    }

    public SquareRelativeLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SquareRelativeLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}
