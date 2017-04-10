package com.tk.recyclerview.layout.flow;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.tk.recyclerview.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * <pre>
 *     author : TK
 *     time   : 2017/04/05
 *     desc   : 流式布局
 * </pre>
 */
public class FlowActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnAdd;
    private Button btnDelete;
    private Button btnInit;
    private RecyclerView recyclerview;
    private SampleAdapter adapter;
    private List<String> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flow_layout);
        btnAdd = (Button) findViewById(R.id.btn_add);
        btnDelete = (Button) findViewById(R.id.btn_delete);
        btnInit = (Button) findViewById(R.id.btn_init);
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(new FlowLayoutManager());
        recyclerview.setHasFixedSize(true);

        list = initData();
        adapter = new SampleAdapter(list);
        recyclerview.setAdapter(adapter);

        btnAdd.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        btnInit.setOnClickListener(this);
    }

    private List<String> initData() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            list.add(getString() + i);
        }
        return list;
    }

    private String getString() {
        int r = new Random().nextInt(4);
        String s = "标签";
        StringBuilder builder = new StringBuilder(s);
        while (r > 0) {
            builder.append(s);
            r--;
        }
        return builder.toString();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add:
                list.add(getString() + (list.size()));
                adapter.notifyItemInserted(list.size());
                break;
            case R.id.btn_delete:
                int p = list.size() - 1;
                list.remove(p);
                adapter.clear(p);
                adapter.notifyItemRemoved(list.size());
                break;
            case R.id.btn_init:
                list.clear();
                list.addAll(initData());
                adapter.clear();
                adapter.notifyDataSetChanged();
                break;
        }
    }
}
