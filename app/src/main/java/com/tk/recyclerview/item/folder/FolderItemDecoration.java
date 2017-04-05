package com.tk.recyclerview.item.folder;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tk.recyclerview.DensityUtil;

/**
 * <pre>
 *     author : TK
 *     time   : 2017/04/05
 *     desc   : 折叠效果
 * </pre>
 */
public class FolderItemDecoration extends RecyclerView.ItemDecoration {
    private int margin = DensityUtil.dp2px(12);

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (parent.getChildAdapterPosition(view) == parent.getAdapter().getItemCount() - 1) {
            //末尾
            outRect.set(margin, 0, margin, margin);
        } else if (parent.getChildAdapterPosition(view) == 0) {
            //首
            outRect.set(margin, margin, margin, -DensityUtil.dp2px(24));
        } else {
            outRect.set(margin, 0, margin, -DensityUtil.dp2px(24));
        }
    }


}
