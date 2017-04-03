package com.tk.recyclerview.pull;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.tk.recyclerview.Item;
import com.tk.recyclerview.MainAdapter;
import com.tk.recyclerview.R;
import com.tk.recyclerview.item.NoLastItemDecoration;
import com.tk.recyclerview.pull.fragment.PullFragmentActivity;
import com.tk.recyclerview.pull.nested.PullNestedActivity;
import com.tk.recyclerview.pull.normal.PullNormalActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *     author : TK
 *     time   : 2017/04/03
 *     desc   : pull
 * </pre>
 */
public class PullActivity extends AppCompatActivity {
    private RecyclerView recyclerview;
    private List<Item> mList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pull);
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recyclerview.setHasFixedSize(true);
        recyclerview.addItemDecoration(new NoLastItemDecoration(this, 0, 0));

        mList.add(new Item("一般场景", new Intent(this, PullNormalActivity.class)));
        mList.add(new Item("嵌套场景", new Intent(this, PullNestedActivity.class)));
        mList.add(new Item("Fragment场景", new Intent(this, PullFragmentActivity.class)));

        recyclerview.setAdapter(new MainAdapter(mList));
    }
}
