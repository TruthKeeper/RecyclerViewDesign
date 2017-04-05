package com.tk.recyclerview.item.folder;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tk.recyclerview.R;


/**
 * <pre>
 *     author : TK
 *     time   : 2017/04/05
 *     desc   : 折叠效果，原理：
 *              通过ItemDecoration 负值；
 *              改变Z轴高度；
 *              滚动偏移；
 * </pre>
 */
public class ItemFolderActivity extends AppCompatActivity {
    private RecyclerView recyclerview;
    private LinearLayoutManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_folder);
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        manager = new LinearLayoutManager(this);
        recyclerview.setLayoutManager(manager);
        recyclerview.setHasFixedSize(true);
        recyclerview.addItemDecoration(new FolderItemDecoration());
        recyclerview.setAdapter(new SampleAdapter());
        recyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //遍历可见以及即将可见的视图，防止复用导致的异常
                int first = manager.findFirstVisibleItemPosition();
                int last = manager.findLastVisibleItemPosition();
                View itemView;
                for (int i = first - 1; i < last + 1; i++) {
                    itemView = manager.findViewByPosition(i);
                    if (itemView != null) {
                        if (i == first) {
                            //1/2的偏移效果
                            itemView.setTranslationY(-itemView.getTop() / 2);
                        } else if (itemView.getTranslationY() != 0) {
                            itemView.setTranslationY(0);
                        }
                    }
                }
            }
        });
    }
}
