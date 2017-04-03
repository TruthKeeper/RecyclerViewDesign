package com.tk.recyclerview.pull.normal;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.tk.recyclerview.R;
import com.tk.recyclerview.SimpleAdapter;
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
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        pullAdapter = new PullAdapter(new SimpleAdapter(mList), new EmptyLayout(this), new EndLayout(this));
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
        }, 2000);
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
        if (mList.size() < 25) {
            final int r = new Random().nextInt(2);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (r > 0) {
                        mList.add("新数据");
                        pullAdapter.getSourceAdapter().notifyItemRangeInserted(pullAdapter.getSourceAdapter().getItemCount() - 1, 1);
                        pullAdapter.setLoadResult(PullAdapter.Status.LOAD_STANDBY);
                    } else {
                        pullAdapter.setLoadResult(PullAdapter.Status.LOAD_ERROR);
                    }
                }
            }, 1000);
        } else {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    pullAdapter.setLoadResult(PullAdapter.Status.LOAD_END);
                }
            }, 1000);
        }
    }
}
