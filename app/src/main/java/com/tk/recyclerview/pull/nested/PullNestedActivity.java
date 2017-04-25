package com.tk.recyclerview.pull.nested;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.tk.recyclerview.R;
import com.tk.recyclerview.adapter.LinearAdapter;
import com.tk.recyclerview.pull.common.EmptyLayout;
import com.tk.recyclerview.pull.common.EndLayout;
import com.tk.recyclerview.pull.common.PullAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *     author : TK
 *     time   : 2017/04/03
 *     desc   : 嵌套场景
 * </pre>
 */
public class PullNestedActivity extends AppCompatActivity {
    /**
     * 注意，如果使用ScrollView进行监听扩展，在6.0+真机上无法显示RecyclerView，需重写ScrollView的OnMeasure
     */
    private SimpleNestedScrollView scrollview;
    private RecyclerView recyclerView;
    private PullAdapter adapter;
    private List<String> mList = new ArrayList<>();
    private Handler handler = new Handler();
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pull_nested);
        dialog = new ProgressDialog(this);
        initView();
    }

    private void initView() {
        scrollview = (SimpleNestedScrollView) findViewById(R.id.scrollview);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        scrollview.setOnScrollListener(new SimpleNestedScrollView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(SimpleNestedScrollView view, int scrollState) {
                adapter.applyInNested(recyclerView,scrollState);
            }

            @Override
            public void onScrollChanged(int l, int t, int oldl, int oldt) {

            }
        });
        adapter = new PullAdapter(new LinearAdapter(mList), new EmptyLayout(this), new EndLayout(this));
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);

        adapter.setOnLoadListener(new PullAdapter.OnLoadListener() {
            @Override
            public void onLoad() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mList.add("新数据");
                        adapter.getSourceAdapter().notifyItemRangeInserted(adapter.getSourceAdapter().getItemCount() - 1, 1);
                        adapter.setLoadResult(PullAdapter.LOAD_STANDBY);
                        recyclerView.requestLayout();
                    }
                }, 1000);
            }

            @Override
            public void onReLoad() {

            }
        });
        dialog.show();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 20; i++) {
                    mList.add("第" + i + "条数据");
                }
                dialog.dismiss();
                adapter.getSourceAdapter().notifyDataSetChanged();
                adapter.setLoadResult(PullAdapter.LOAD_STANDBY);
            }
        }, 2000);


    }


}
