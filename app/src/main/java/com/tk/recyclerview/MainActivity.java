package com.tk.recyclerview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.tk.recyclerview.item.NoLastItemDecoration;
import com.tk.recyclerview.pull.PullActivity;

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

        mList.add(new Item("上拉加载", new Intent(this, PullActivity.class)));
//        mList.add(new Item("ItemDecoration", new Intent(this,  .class));
//        mList.add(new Item("LayoutManager", new Intent(this,  .class));
//        mList.add(new Item("ItemAnimator", new Intent(this,  .class));
//        mList.add(new Item("ItemTouch", new Intent(this,  .class));

        recyclerview.setAdapter(new MainAdapter(mList));
    }
}
