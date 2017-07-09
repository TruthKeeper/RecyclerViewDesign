package com.tk.recyclerview.sample;


import com.tk.recyclerview.R;
import com.tk.recyclerview.adapter.base.BaseAdapter;
import com.tk.recyclerview.adapter.base.BaseHolder;

/**
 * <pre>
 *     author : TK
 *     time   : 2017/07/08
 *     desc   : xxxx描述
 * </pre>
 */
public class SampleAdapter extends BaseAdapter<String> {
    @Override
    public int bindLayout(int viewType) {
        return R.layout.item_sample;
    }

    @Override
    public void bindData(BaseHolder holder, String data) {
        holder.setText(R.id.item, data);
    }
}
