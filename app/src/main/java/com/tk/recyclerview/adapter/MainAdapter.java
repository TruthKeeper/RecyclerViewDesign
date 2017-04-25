package com.tk.recyclerview.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tk.recyclerview.Item;
import com.tk.recyclerview.R;

import java.util.List;

/**
 * <pre>
 *     author : TK
 *     time   : 2017/04/03
 *     desc   : adapter
 * </pre>
 */
public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ItemHolder> {
    private List<Item> mList;
    private LayoutInflater mInflater;

    public MainAdapter(List<Item> mList) {
        this.mList = mList;
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main, parent, false));
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        ((TextView) holder.itemView).setText(mList.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ItemHolder extends RecyclerView.ViewHolder {
        public ItemHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.getContext().startActivity(mList.get(getAdapterPosition()).getIntent());
                }
            });
        }
    }

}
