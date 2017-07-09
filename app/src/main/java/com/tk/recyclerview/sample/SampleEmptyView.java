package com.tk.recyclerview.sample;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.tk.recyclerview.R;

/**
 * <pre>
 *     author : TK
 *     time   : 2017/07/08
 *     desc   : xxxx描述
 * </pre>
 */
public class SampleEmptyView extends FrameLayout {
    public SampleEmptyView(@NonNull Context context) {
        this(context, null);
    }

    public SampleEmptyView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SampleEmptyView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.item_type_empty, this);
    }
}
