package com.tk.recyclerview.pull.normal;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.tk.recyclerview.R;
import com.tk.recyclerview.adapter.GridAdapter;
import com.tk.recyclerview.adapter.LinearAdapter;
import com.tk.recyclerview.adapter.StaggereddAdapter;
import com.tk.recyclerview.pull.SimpleSwipeLayout;
import com.tk.recyclerview.pull.common.EmptyLayout;
import com.tk.recyclerview.pull.common.EndLayout;
import com.tk.recyclerview.pull.common.PullAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * <pre>
 *     author : TK
 *     time   : 2017/04/03
 *     desc   : 一般场景
 * </pre>
 */
public class PullNormalActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, PullAdapter.OnLoadListener {

    private SimpleSwipeLayout swipeLayout;
    private RecyclerView recyclerView;

    private PullAdapter pullAdapter;
    private List<String> mList = new ArrayList<>();
    private Handler handler = new Handler();
    private int mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pull_normal);
        initView();
    }

    private void initView() {
        swipeLayout = (SimpleSwipeLayout) findViewById(R.id.swipe_layout);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);

        swipeLayout.setOnRefreshListener(this);
        mode = getIntent().getIntExtra("mode", 0);
        switch (mode) {
            case 1:
                recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
                pullAdapter = new PullAdapter(new GridAdapter(mList, 3), new EmptyLayout(this), new EndLayout(this));
                break;
            case 2:
                recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
                pullAdapter = new PullAdapter(new StaggereddAdapter(mList), new EmptyLayout(this), new EndLayout(this));
                break;
            default:
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                pullAdapter = new PullAdapter(new LinearAdapter(mList), new EmptyLayout(this), new EndLayout(this));
                break;
        }

        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(pullAdapter);

        pullAdapter.setOnLoadListener(this);
        swipeLayout.setRefreshing(true);
        onRefresh();
    }

    @Override
    public void onRefresh() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mList.clear();
                for (int i = 0; i < 20; i++) {
                    mList.add("第" + i + "条数据");
                }
                swipeLayout.setRefreshing(false);
                pullAdapter.getSourceAdapter().notifyDataSetChanged();
            }
        }, 1500);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onLoad() {
        getData();
    }

    @Override
    public void onReLoad() {
        getData();
    }

    private void getData() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                final int r = new Random().nextInt(10);
                if (r > 3) {
                    mList.add("新数据");
                    pullAdapter.getSourceAdapter().notifyItemRangeInserted(pullAdapter.getSourceAdapter().getItemCount() - 1, 1);
                    pullAdapter.setLoadResult(PullAdapter.LOAD_STANDBY);
                } else if (r > 1) {
                    pullAdapter.setLoadResult(PullAdapter.LOAD_END);
                } else {
                    pullAdapter.setLoadResult(PullAdapter.LOAD_ERROR);
                }
            }
        }, 1000);

    }
}
