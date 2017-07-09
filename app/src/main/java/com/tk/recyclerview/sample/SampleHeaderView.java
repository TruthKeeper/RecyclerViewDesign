package com.tk.recyclerview.sample;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.tk.recyclerview.R;

/**
 * <pre>
 *     author : TK
 *     time   : 2017/07/08
 *     desc   : xxxx描述
 * </pre>
 */
public class SampleHeaderView extends FrameLayout {
    private TextView item;

    public SampleHeaderView(@NonNull Context context) {
        this(context, null);
    }

    public SampleHeaderView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SampleHeaderView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.item_header, this);
        item = (TextView) findViewById(R.id.item);
    }

    public void setText(String str) {
        item.setText(str);
    }
}
