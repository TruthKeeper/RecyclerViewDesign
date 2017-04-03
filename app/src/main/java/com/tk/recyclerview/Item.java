package com.tk.recyclerview;

import android.content.Intent;

/**
 * <pre>
 *     author : TK
 *     time   : 2017/04/03
 *     desc   : 条目
 * </pre>
 */
public class Item {
    private String content;
    private Intent intent;

    public Item(String content, Intent intent ) {
        this.content = content;
        this.intent = intent;
    }

    public String getContent() {
        return content;
    }

    public Intent getIntent() {
        return intent;
    }

}
