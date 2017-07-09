package com.tk.recyclerview.adapter.base;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * <pre>
 *     author : TK
 *     time   : 2017/05/10
 *     desc   : RecyclerView Holder封装
 * </pre>
 */
public class BaseHolder extends RecyclerView.ViewHolder {
    private SparseArray<View> mViews;
    private SparseArray<Object> mTags;
    private int mViewType;

    public BaseHolder(View itemView, int viewType) {
        super(itemView);
        mViews = new SparseArray<>();
        mViewType = viewType;
    }

    /**
     * 获取上下文
     *
     * @return
     */
    public Context getContext() {
        return itemView.getContext();
    }

    /**
     * 获取视图类型
     *
     * @return
     */
    public int getViewType() {
        return mViewType;
    }

    /**
     * 通过id获取控件
     *
     * @param viewId
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T extends View> T findViewById(@IdRes int viewId) {
        View view = mViews.get(viewId);
        if (null == view) {
            view = itemView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    /**
     * 设置图像加载
     *
     * @param viewId
     * @param drawable
     * @return
     */
    public BaseHolder setImage(@IdRes int viewId, Drawable drawable) {
        this.<ImageView>findViewById(viewId).setImageDrawable(drawable);
        return this;
    }

    /**
     * 设置图像
     *
     * @param viewId
     * @param resId
     * @return
     */
    public BaseHolder setImage(@IdRes int viewId, @DrawableRes int resId) {
        this.<ImageView>findViewById(viewId).setImageResource(resId);
        return this;
    }

    /**
     * 设置文本
     *
     * @param viewId
     * @param resId
     * @return
     */
    public BaseHolder setText(@IdRes int viewId, @StringRes int resId) {
        this.<TextView>findViewById(viewId).setText(resId);
        return this;
    }

    /**
     * 设置文本
     *
     * @param viewId
     * @param text
     * @return
     */
    public BaseHolder setText(@IdRes int viewId, CharSequence text) {
        this.<TextView>findViewById(viewId).setText(text);
        return this;
    }

    /**
     * 设置文本
     *
     * @param viewId
     * @param text
     * @param nullText
     * @return
     */
    public BaseHolder setTextOrNull(@IdRes int viewId, CharSequence text, CharSequence nullText) {
        this.<TextView>findViewById(viewId).setText(TextUtils.isEmpty(text) ? nullText : text);
        return this;
    }

    /**
     * 设置文本颜色
     *
     * @param viewId
     * @param colorRes
     * @return
     */
    public BaseHolder setTextColorByRes(@IdRes int viewId, @ColorRes int colorRes) {
        this.<TextView>findViewById(viewId).setTextColor(ContextCompat.getColor(itemView.getContext(), colorRes));
        return this;
    }

    /**
     * 设置文本颜色
     *
     * @param viewId
     * @param textColor
     * @return
     */
    public BaseHolder setTextColorByInt(@IdRes int viewId, @ColorInt int textColor) {
        this.<TextView>findViewById(viewId).setTextColor(textColor);
        return this;
    }

    /**
     * 设置文本大小
     *
     * @param viewId
     * @param textSize
     * @return
     */
    public BaseHolder setTextSize(@IdRes int viewId, float textSize) {
        this.<TextView>findViewById(viewId).setTextSize(textSize);
        return this;
    }

    /**
     * 设置进度
     *
     * @param viewId
     * @param progress
     * @return
     */
    public BaseHolder setProgress(@IdRes int viewId, int progress) {
        this.<ProgressBar>findViewById(viewId).setProgress(progress);
        return this;
    }

    /**
     * 设置是否可见
     *
     * @param viewId
     * @param visibility
     * @return
     */
    public BaseHolder setVisibility(@IdRes int viewId, int visibility) {
        findViewById(viewId).setVisibility(visibility);
        return this;
    }

    /**
     * 设置是否被选中
     *
     * @param viewId
     * @param checked
     * @return
     */
    public BaseHolder setChecked(@IdRes int viewId, boolean checked) {
        this.<CompoundButton>findViewById(viewId).setChecked(checked);
        return this;
    }

    /**
     * 设置是否被选中
     *
     * @param viewId
     * @param selected
     * @return
     */
    public BaseHolder setSelected(@IdRes int viewId, boolean selected) {
        findViewById(viewId).setSelected(selected);
        return this;
    }

    /**
     * 设置是否可用
     *
     * @param viewId
     * @param enabled
     * @return
     */
    public BaseHolder setEnabled(@IdRes int viewId, boolean enabled) {
        findViewById(viewId).setEnabled(enabled);
        return this;
    }

    /**
     * 设置点击监听
     *
     * @param viewId
     * @param listener
     * @return
     */
    public BaseHolder setOnClickListener(@IdRes int viewId, View.OnClickListener listener) {
        findViewById(viewId).setOnClickListener(listener);
        return this;
    }

    /**
     * 设置长点击监听
     *
     * @param viewId
     * @param listener
     * @return
     */
    public BaseHolder setOnLongClickListener(@IdRes int viewId, View.OnLongClickListener listener) {
        findViewById(viewId).setOnLongClickListener(listener);
        return this;
    }

    /**
     * 设置一个额外存储Tag
     *
     * @param tagKey
     * @param tag
     */
    public void setTag(int tagKey, Object tag) {
        if (null == mTags) {
            mTags = new SparseArray<>(2);
        }
        mTags.put(tagKey, tag);
    }

    /**
     * 获取额外存储Tag
     *
     * @param tagKey
     * @param <T>
     * @return
     */
    public <T> T getTag(int tagKey) {
        if (null == mTags) {
            return null;
        }
        return (T) mTags.get(tagKey);
    }
}
