package com.tk.recyclerview.pull.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

/**
 * <pre>
 *     author : TK
 *     time   : 2017/04/03
 *     desc   : 懒加载
 * </pre>
 */
public abstract class LazyFragment extends Fragment {
    private boolean created;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!created) {
            //还未创建视图
            return;
        }
        if (isVisibleToUser) {
            onFragmentVisibleChange(true);
        } else {
            onFragmentVisibleChange(false);
        }
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        created = true;
        if (getUserVisibleHint()) {
            onFragmentVisibleChange(true);
        }
    }

    /**
     * @param isVisible true  不可见 -> 可见
     *                  false 可见  -> 不可见
     */
    protected abstract void onFragmentVisibleChange(boolean isVisible);
}

