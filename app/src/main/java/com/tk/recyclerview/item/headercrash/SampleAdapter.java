package com.tk.recyclerview.item.headercrash;

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
 *     time   : 2017/04/04
 *     desc   : adapter
 * </pre>
 */
public class SampleAdapter extends RecyclerView.Adapter {
    private List<Item> mList;

    public SampleAdapter(List<Item> mList) {
        this.mList = mList;
    }

    @Override
    public int getItemViewType(int position) {
        return mList.get(position).isTag() ? R.layout.item_tag : R.layout.item_main;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == R.layout.item_tag) {
            return new TagHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tag, parent, false));
        } else {
            return new ContentHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_linear, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TagHolder) {
            ((TextView) holder.itemView).setText(mList.get(position).getTagText());
        } else {
            ((TextView) holder.itemView).setText(mList.get(position).getContent());
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    static class ContentHolder extends RecyclerView.ViewHolder {

        public ContentHolder(View itemView) {
            super(itemView);

        }
    }

    static class TagHolder extends RecyclerView.ViewHolder {

        public TagHolder(View itemView) {
            super(itemView);

        }
    }

}