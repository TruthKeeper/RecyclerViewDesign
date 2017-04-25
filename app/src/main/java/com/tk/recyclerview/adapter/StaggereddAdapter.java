package com.tk.recyclerview.adapter;

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
public class StaggereddAdapter extends RecyclerView.Adapter<StaggereddAdapter.ItemHolder> {
    private List<String> mList;

    public StaggereddAdapter(List<String> mList) {
        this.mList = mList;
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_staggered, parent, false));
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        holder.itemView.getLayoutParams().height = 320 + (1 - position % 5) * 60;
        ((TextView) holder.itemView).setText(mList.get(position));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ItemHolder extends RecyclerView.ViewHolder {

        public ItemHolder(View itemView) {
            super(itemView);
        }
    }

}