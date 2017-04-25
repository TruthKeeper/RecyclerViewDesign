package com.tk.recyclerview.layout.flow;

import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.tk.recyclerview.R;

import java.util.List;

/**
 * <pre>
 *     author : TK
 *     time   : 2017/04/07
 *     desc   : adapter
 * </pre>
 */
public class SampleAdapter extends RecyclerView.Adapter<SampleAdapter.ItemHolder> {
    private List<String> mList;
    private SparseBooleanArray array = new SparseBooleanArray();

    public SampleAdapter(List<String> mList) {
        this.mList = mList;
    }

    public void clear(int position) {
        array.delete(position);
    }

    public void clear() {
        array.clear();
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_flow, parent, false));
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        holder.checkBox.setText(mList.get(position));
        holder.checkBox.setChecked(array.get(position, false));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ItemHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;

        public ItemHolder(View itemView) {
            super(itemView);
            checkBox = (CheckBox) itemView;
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    array.put(getAdapterPosition(), isChecked);
                }
            });
        }
    }

}