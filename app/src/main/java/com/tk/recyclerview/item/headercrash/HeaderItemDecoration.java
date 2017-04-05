package com.tk.recyclerview.item.headercrash;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tk.recyclerview.R;

import java.util.List;

/**
 * <pre>
 *     author : TK
 *     time   : 2017/04/04
 *     desc   : xxxx描述
 * </pre>
 */
public class HeaderItemDecoration extends RecyclerView.ItemDecoration {
    private Paint mPaint;
    private int tagHeight;
    private int tagDrawHeight;
    private int tagLeft;
    private int textHeight;
    private List<Item> list;

    public HeaderItemDecoration(Context context, List<Item> list) {
        this.list = list;
        mPaint = new Paint();
        mPaint.setDither(true);
        mPaint.setAntiAlias(true);
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(context.getResources().getDimensionPixelSize(R.dimen.tag_size));
        tagHeight = context.getResources().getDimensionPixelOffset(R.dimen.tag_height);
        tagLeft = context.getResources().getDimensionPixelOffset(R.dimen.tag_left);
        Paint.FontMetrics fm = mPaint.getFontMetrics();
        textHeight = (int) (fm.bottom + fm.top);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (needDrawOver(parent)) {
            mPaint.setColor(ContextCompat.getColor(parent.getContext(), android.R.color.darker_gray));
            c.drawRect(0, 0, parent.getMeasuredWidth(), tagDrawHeight, mPaint);
            mPaint.setColor(Color.BLACK);
            c.drawText(getDrawTag(parent), tagLeft, (tagDrawHeight + (tagDrawHeight - tagHeight) - textHeight) / 2, mPaint);
        }

    }

    /**
     * 获取要绘制的Tag
     *
     * @param parent
     * @return
     */
    private String getDrawTag(RecyclerView parent) {
        int p = ((LinearLayoutManager) parent.getLayoutManager()).findFirstVisibleItemPosition();
        return list.get(p).getTagText();
    }

    /**
     * 当前是否需要绘制Tag
     *
     * @param parent
     * @return
     */
    private boolean needDrawOver(RecyclerView parent) {
        LinearLayoutManager manager = (LinearLayoutManager) parent.getLayoutManager();
        int position = manager.findFirstCompletelyVisibleItemPosition();
        if (parent.getAdapter().getItemViewType(position) == R.layout.list_tag) {
            //第一个显示的条目是Tag
            View view = manager.findViewByPosition(position);
            int[] viewL = new int[2];
            int[] parentL = new int[2];
            view.getLocationInWindow(viewL);
            parent.getLocationInWindow(parentL);
            if (viewL[1] - parentL[1] > 0) {
                if (viewL[1] - parentL[1] > tagHeight) {
                    tagDrawHeight = tagHeight;
                } else {
                    tagDrawHeight = viewL[1] - parentL[1];
                }
                return true;
            }
            return false;
        } else {
            //赋值
            tagDrawHeight = tagHeight;
            return true;
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
    }
}
