package com.tk.recyclerview.sample;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.tk.recyclerview.R;
import com.tk.recyclerview.adapter.base.BaseAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * <pre>
 *     author : TK
 *     time   : 2017/07/08
 *     desc   : xxxx描述
 * </pre>
 */
public class SampleActivity extends AppCompatActivity implements BaseAdapter.OnItemClickListener {
    private RecyclerView recyclerview;
    private ProgressDialog dialog;
    private Handler handler = new Handler();

    private SampleAdapter adapter;
    private List<SampleHeaderView> headerList = new ArrayList<>();
    private List<SampleFooterView> footerList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);
        dialog = new ProgressDialog(this);
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerview.setHasFixedSize(true);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SampleAdapter();
        adapter.setEmptyEnabled(false);
        adapter.setEmptyView(new SampleEmptyView(this));

        recyclerview.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
    }

    public void init(View v) {
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < 20; i++) {
            list.add("第" + i + "条数据");
        }
        adapter.setEmptyEnabled(true);
        adapter.setData(list);
    }

    public void clear(View v) {
        adapter.clear();
    }

    public void randomDelete(View v) {
        if (!adapter.getList().isEmpty()) {
            adapter.remove(new Random().nextInt(adapter.getList().size()));
        }
    }

    public void diff(View v) {
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < 20; i++) {
            list.add("第" + i + "条数据");
        }
        adapter.setDataByDiff(list);
    }

    public void addHeader(View v) {
        SampleHeaderView view = new SampleHeaderView(this);
        view.setText("头部" + (headerList.size() + 1));
        headerList.add(view);
        adapter.addHeaderView(view);
    }

    public void removeHeader(View v) {
        if (headerList.isEmpty()) {
            return;
        }
        adapter.removeHeaderView(headerList.remove(headerList.size() - 1));
    }

    public void addFooter(View v) {
        SampleFooterView view = new SampleFooterView(this);
        view.setText("足部" + (footerList.size() + 1));
        footerList.add(view);
        adapter.addFooterView(view);
    }

    public void removeFooter(View v) {
        if (footerList.isEmpty()) {
            return;
        }
        adapter.removeFooterView(footerList.remove(footerList.size() - 1));
    }

    @Override
    public void onClick(BaseAdapter adapter, View view, int position) {
        Toast.makeText(this, (String) adapter.getListItem(position), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
