package com.tk.recyclerview.item.folder;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tk.recyclerview.R;

/**
 * <pre>
 *     author : TK
 *     time   : 2017/04/05
 *     desc   : adapter
 * </pre>
 */
public class SampleAdapter extends RecyclerView.Adapter<SampleAdapter.Holder> {

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_folder, parent, false));

    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        //1，5,11递增
        holder.cardview.setCardElevation(position * 4 + 1);
    }

    @Override
    public int getItemCount() {
        return 20;
    }

    static class Holder extends RecyclerView.ViewHolder {
        CardView cardview;

        public Holder(View itemView) {
            super(itemView);
            cardview = (CardView) itemView.findViewById(R.id.cardview);
        }
    }


}