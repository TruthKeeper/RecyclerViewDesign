package com.tk.recyclerview.adapter;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tk.recyclerview.R;

import java.util.List;

/**
 * <pre>
 *     author : TK
 *     time   : 2017/04/25
 *     desc   : xxxx描述
 * </pre>
 */
public class GridAdapter extends RecyclerView.Adapter<GridAdapter.ItemHolder> {
    private List<String> mList;
    private int s;

    public GridAdapter(List<String> mList, int span) {
        this.mList = mList;
        //简单粗暴的写法，正式设计删除
        s = Resources.getSystem().getDisplayMetrics().widthPixels / span;
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grid, parent,false));
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        ((TextView) holder.itemView).setText(mList.get(position));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ItemHolder extends RecyclerView.ViewHolder {

        public ItemHolder(View itemView) {
            super(itemView);
            ViewGroup.LayoutParams p = itemView.getLayoutParams();
            p.height = s;
            itemView.setLayoutParams(p);
        }
    }

}