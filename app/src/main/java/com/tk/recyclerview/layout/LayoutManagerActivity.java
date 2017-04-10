package com.tk.recyclerview.layout;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.tk.recyclerview.Item;
import com.tk.recyclerview.MainAdapter;
import com.tk.recyclerview.R;
import com.tk.recyclerview.item.NoLastItemDecoration;
import com.tk.recyclerview.layout.flow.FlowActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *     author : TK
 *     time   : 2017/04/05
 *     desc   : LayoutManager
 * </pre>
 */
public class LayoutManagerActivity extends AppCompatActivity {
    private RecyclerView recyclerview;
    private List<Item> mList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout_manager);
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recyclerview.setHasFixedSize(true);
        recyclerview.addItemDecoration(new NoLastItemDecoration(this, 0, 0));

        mList.add(new Item(getString(R.string.flow_layout), new Intent(this, FlowActivity.class)));

        recyclerview.setAdapter(new MainAdapter(mList));
    }


}
