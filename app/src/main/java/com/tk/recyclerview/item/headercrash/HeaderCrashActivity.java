package com.tk.recyclerview.item.headercrash;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.tk.recyclerview.R;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *     author : TK
 *     time   : 2017/04/03
 *     desc   : 头部碰撞位移
 * </pre>
 */
public class HeaderCrashActivity extends AppCompatActivity {
    private RecyclerView recyclerview;
    public static final String[] animals = new String[]{"猫", "狗", "老鼠", "大象", "鸟", "鱼", "老虎", "猪", "狮子"};
    public static final String[] traffic = new String[]{"汽车", "自行车", "走路", "飞机", "火车"};
    public static final String[] sport = new String[]{"跑步", "篮球", "羽毛球", "乒乓球", "足球", "网球"};
    public static final String[] foot = new String[]{"鸡肉", "猪肉", "鱼肉", "鸭肉", "蔬菜", "甜点"};
    public static final String[] fruits = new String[]{"苹果", "西瓜", "桔子", "梨", "西瓜", "桃子"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_header_crash);
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recyclerview.setHasFixedSize(true);
        List<Item> list = initData();
        recyclerview.addItemDecoration(new HeaderItemDecoration(this,list));
        recyclerview.setAdapter(new SampleAdapter(list));
    }

    private List<Item> initData() {
        List<Item> list = new ArrayList<>();

        Item item = new Item();
        item.setTag(true);
        item.setTagText("动物");
        list.add(item);
        for (int i = 0; i < animals.length; i++) {
            item = new Item();
            item.setContent(animals[i]);
            item.setTagText("动物");
            list.add(item);
        }

        item = new Item();
        item.setTag(true);
        item.setTagText("交通工具");
        list.add(item);
        for (int i = 0; i < traffic.length; i++) {
            item = new Item();
            item.setTagText("交通工具");
            item.setContent(traffic[i]);
            list.add(item);
        }

        item = new Item();
        item.setTag(true);
        item.setTagText("运动");
        list.add(item);
        for (int i = 0; i < sport.length; i++) {
            item = new Item();
            item.setTagText("运动");
            item.setContent(sport[i]);
            list.add(item);
        }

        item = new Item();
        item.setTag(true);
        item.setTagText("食物");
        list.add(item);
        for (int i = 0; i < foot.length; i++) {
            item = new Item();
            item.setTagText("食物");
            item.setContent(foot[i]);
            list.add(item);
        }

        item = new Item();
        item.setTag(true);
        item.setTagText("水果");
        list.add(item);
        for (int i = 0; i < fruits.length; i++) {
            item = new Item();
            item.setTagText("水果");
            item.setContent(fruits[i]);
            list.add(item);
        }
        return list;
    }
}
