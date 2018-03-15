package com.source.yin.yinimageview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * 宽高相等的正方形 ImageView，一般与 scaleType="centerCrop" 配合使用保证内容充满控件
 * Created by yin on 2017/12/25.
 */

public class SquareImageView extends android.support.v7.widget.AppCompatImageView {
    public SquareImageView(Context context) {
        super(context);
    }

    public SquareImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();

        int squareSide;
        //避免当 UNSPECIFIED 等情况时取到最小值为0的情况
        if (measuredWidth <= 0 || measuredHeight <= 0) {
            squareSide = Math.max(measuredWidth, measuredHeight);
        } else {
            squareSide = Math.min(measuredWidth, measuredHeight);
        }
        setMeasuredDimension(squareSide, squareSide);
    }
}
