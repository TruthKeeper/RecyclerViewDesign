package com.tk.recyclerview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.tk.recyclerview.adapter.MainAdapter;
import com.tk.recyclerview.item.ItemActivity;
import com.tk.recyclerview.item.NoLastItemDecoration;
import com.tk.recyclerview.layout.LayoutManagerActivity;
import com.tk.recyclerview.pull.PullActivity;
import com.tk.recyclerview.sample.SampleActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *     author : TK
 *     time   : 2017/04/03
 *     desc   : 主界面
 * </pre>
 */
public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerview;
    private List<Item> mList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recyclerview.setHasFixedSize(true);
        recyclerview.addItemDecoration(new NoLastItemDecoration(this, 0, 0));

        mList.add(new Item(getString(R.string.pull), new Intent(this, PullActivity.class)));
        mList.add(new Item(getString(R.string.item_decoration), new Intent(this, ItemActivity.class)));
        mList.add(new Item(getString(R.string.layout_manager), new Intent(this, LayoutManagerActivity.class)));
        mList.add(new Item(getString(R.string.sample), new Intent(this, SampleActivity.class)));
//        mList.add(new Item("ItemAnimator", new Intent(this,  .class));
//        mList.add(new Item("ItemTouch", new Intent(this,  .class));

        recyclerview.setAdapter(new MainAdapter(mList));
    }
}
