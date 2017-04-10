package com.tk.recyclerview.item;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tk.recyclerview.DensityUtil;


/**
 * <pre>
 *     author : TK
 *     time   : 2017/04/03
 *     desc   : 设置左右间距
 * </pre>
 */
public class PaddingItemDecoration extends RecyclerView.ItemDecoration {
    public static final int DIVIDER = 1;
    protected ColorDrawable mDrawable;
    protected int divider;
    protected int paddingLeft;
    protected int paddingRight;

    public PaddingItemDecoration(Context mContext, int paddingLeft, int paddingRight) {
        this(mContext, paddingLeft, paddingRight,
                ContextCompat.getColor(mContext, android.R.color.darker_gray), DIVIDER);
    }

    public PaddingItemDecoration(Context mContext, int paddingLeft, int paddingRight, int color, int divier) {
        this.paddingLeft = DensityUtil.dp2px(paddingLeft);
        this.paddingRight = DensityUtil.dp2px(paddingRight);
        this.mDrawable = new ColorDrawable(color);
        this.divider = DensityUtil.dp2px(divier);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.set(0, 0, 0, divider);
    }


    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        drawVertical(c, parent);
    }

    public void drawVertical(Canvas c, RecyclerView parent) {
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int top = child.getBottom() + params.bottomMargin +
                    Math.round(ViewCompat.getTranslationY(child));
            final int bottom = top + divider;
            mDrawable.setBounds(left + paddingLeft, top, right - paddingRight, bottom);
            mDrawable.draw(c);
        }
    }

//    public void drawHorizontal(Canvas c, RecyclerView parent) {
//        final int top = parent.getPaddingTop();
//        final int bottom = parent.getHeight() - parent.getPaddingBottom();
//        final int childCount = parent.getChildCount();
//        for (int i = 0; i < childCount; i++) {
//            final View child = parent.getChildAt(i);
//            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
//                    .getLayoutParams();
//            final int left = child.getRight() + params.rightMargin +
//                    Math.round(ViewCompat.getTranslationX(child));
//            final int right = left + divider;
//            mDrawable.setBounds(left, top + paddingTop, right, bottom - paddingBottom);
//            mDrawable.draw(c);
//        }
//    }

}
