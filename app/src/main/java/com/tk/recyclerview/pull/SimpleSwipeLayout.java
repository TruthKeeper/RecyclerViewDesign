package com.tk.recyclerview.pull;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;

import com.tk.recyclerview.DensityUtil;

/**
 * <pre>
 *     author : TK
 *     time   : 2017/04/03
 *     desc   : 简单封装
 * </pre>
 */
public class SimpleSwipeLayout extends SwipeRefreshLayout {
    public SimpleSwipeLayout(Context context) {
        this(context, null);
    }

    public SimpleSwipeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setProgressViewOffset(false, 0, DensityUtil.dp2px(24));
    }
}
