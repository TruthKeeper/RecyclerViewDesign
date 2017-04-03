package com.tk.recyclerview.pull.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
 *     desc   : fragment
 * </pre>
 */
public class TextFragment extends LazyFragment implements SwipeRefreshLayout.OnRefreshListener, PullAdapter.OnLoadListener {
    private SimpleSwipeLayout swipeLayout;
    private RecyclerView recyclerView;

    private PullAdapter adapter;
    private List<String> mList = new ArrayList<>();
    private Handler handler = new Handler();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pull, null);
        swipeLayout = (SimpleSwipeLayout) view.findViewById(R.id.swipe_layout);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        initView();
        return view;
    }

    private void initView() {
        swipeLayout.setOnRefreshListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        adapter = new PullAdapter(new SimpleAdapter(mList), new EmptyLayout(getContext()), new EndLayout(getContext()));
        recyclerView.setAdapter(adapter);

        adapter.setOnLoadListener(this);
    }

    @Override
    protected void onFragmentVisibleChange(boolean isVisible) {
        if (isVisible) {
            swipeLayout.setRefreshing(true);
            onRefresh();
        }
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
                adapter.getSourceAdapter().notifyDataSetChanged();
                adapter.setLoadResult(PullAdapter.Status.LOAD_STANDBY);
            }
        }, 2000);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
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
                        adapter.getSourceAdapter().notifyItemRangeInserted(adapter.getSourceAdapter().getItemCount() - 1, 1);
                        adapter.setLoadResult(PullAdapter.Status.LOAD_STANDBY);
                    } else {
                        adapter.setLoadResult(PullAdapter.Status.LOAD_ERROR);
                    }
                }
            }, 1000);
        } else {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    adapter.setLoadResult(PullAdapter.Status.LOAD_END);
                }
            }, 1000);
        }
    }
}
