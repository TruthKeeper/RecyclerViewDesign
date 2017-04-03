package com.tk.recyclerview.pull.common;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by TK on 2016/10/20.
 */

public class PullAdapter extends RecyclerView.Adapter {
    public static final int TYPE_EMPTY = 23333;
    public static final int TYPE_END = 24444;

    public enum Status {
        /**
         * 待命
         */
        LOAD_STANDBY,
        /**
         * 加载失败
         */
        LOAD_ERROR,
        /**
         * 加载完毕
         */
        LOAD_END,
        /**
         * 加载中
         */
        LOAD_ING,
    }

    /**
     * RecyclerView当前状态
     */
    private Status status = Status.LOAD_STANDBY;
    /**
     * 源Adapter，代理模式
     */
    private RecyclerView.Adapter sourceAdapter;
    /**
     * 空视图，@Nullable
     */
    private View emptyView;
    /**
     * 加载视图，@NonNull
     */
    private View endView;

    private RecyclerView.LayoutManager layoutManager;
    /**
     * 滚动监听
     */
    private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(final RecyclerView recyclerView, final int newState) {
            if (layoutManager instanceof LinearLayoutManager) {
                LinearLayoutManager manager = (LinearLayoutManager) layoutManager;
                /*
                 * 悬停状态
                 * Adapter有数据
                 * 待命状态
                 * 滚动到底
                 */
                if (newState == SCROLL_STATE_IDLE
                        && (sourceAdapter != null && sourceAdapter.getItemCount() != 0)
                        && (status == Status.LOAD_STANDBY)
                        && (manager.findLastVisibleItemPosition() == sourceAdapter.getItemCount()
                        && manager.findFirstCompletelyVisibleItemPosition() != 0)) {
                    //开始加载
                    status = Status.LOAD_ING;
                    endView.setVisibility(VISIBLE);
                    ((IEnd) endView).onShow();
                    if (onLoadListener != null) {
                        onLoadListener.onLoad();
                    }
                }
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
        }
    };
    /**
     * Adapter数据变化监听
     */
    private RecyclerView.AdapterDataObserver dataObserver = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            notifyDataSetChanged();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            notifyItemRangeInserted(positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            notifyItemRangeChanged(positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            notifyItemRangeChanged(positionStart, itemCount, payload);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            notifyItemRangeRemoved(positionStart, itemCount);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            notifyItemMoved(fromPosition, toPosition);
        }
    };
    /**
     * 外部监听
     */
    private OnLoadListener onLoadListener;

    public PullAdapter(@NonNull RecyclerView.Adapter sourceAdapter, @Nullable View emptyView, @NonNull View endView) {
        this.sourceAdapter = sourceAdapter;
        this.emptyView = emptyView;
        initEndView(endView);
    }

    /**
     * 用于嵌套
     *
     * @param state 外部滚动控件的滚动状态
     */
    public void applyInNested(int state) {
        if (layoutManager instanceof LinearLayoutManager) {
            /*
             * 悬停状态
             * Adapter有数据
             * 待命状态
             * 滚动到底
             */
            if (state == SCROLL_STATE_IDLE
                    && (sourceAdapter != null && sourceAdapter.getItemCount() != 0)
                    && (status == Status.LOAD_STANDBY)) {
                View view = layoutManager.findViewByPosition(sourceAdapter.getItemCount());
                int[] location = new int[2];
                view.getLocationInWindow(location);
                if (location[1] <= Resources.getSystem().getDisplayMetrics().heightPixels) {
                    //可见了
                    status = Status.LOAD_ING;
                    endView.setVisibility(VISIBLE);
                    ((IEnd) endView).onShow();
                    if (onLoadListener != null) {
                        onLoadListener.onLoad();
                    }
                }
            }
        }
    }

    /**
     * 加载完回调
     *
     * @param status，刷新到底了 / 网络错误 / 刷新结束，待命状态
     */
    public void setLoadResult(Status status) {
        if (status == Status.LOAD_END) {
            this.status = status;
            ((IEnd) endView).inTheEnd();
        } else if (status == Status.LOAD_ERROR) {
            this.status = status;
            ((IEnd) endView).onError();
        } else if (status == Status.LOAD_STANDBY) {
            this.status = status;
            ((IEnd) endView).onDismiss();
        }
    }

    /**
     * 设置底部View IEnd实现类
     *
     * @param endView
     */
    private void initEndView(View endView) {
        if (!(endView instanceof IEnd)) {
            throw new IllegalArgumentException("EndView must be IEnd Impl !");
        }
        this.endView = endView;
        this.endView.setVisibility(GONE);
        this.endView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (status == Status.LOAD_ERROR) {
                    status = Status.LOAD_ING;
                    ((IEnd) v).onShow();
                    if (onLoadListener != null) {
                        onLoadListener.onReLoad();
                    }
                }
            }
        });
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        sourceAdapter.onAttachedToRecyclerView(recyclerView);
        sourceAdapter.registerAdapterDataObserver(dataObserver);
        initLayoutManager(recyclerView);
        initListener(recyclerView);
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        sourceAdapter.onDetachedFromRecyclerView(recyclerView);
        sourceAdapter.unregisterAdapterDataObserver(dataObserver);
        recyclerView.removeOnScrollListener(onScrollListener);
    }

    /**
     * 初始化滚动监听
     *
     * @param recyclerView
     */
    private void initListener(RecyclerView recyclerView) {
        recyclerView.addOnScrollListener(onScrollListener);
    }

    /**
     * 初始化LayoutManager
     *
     * @param recyclerView
     */
    private void initLayoutManager(RecyclerView recyclerView) {
        layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            final GridLayoutManager.SpanSizeLookup lookup = gridLayoutManager.getSpanSizeLookup();
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (getItemViewType(position) == TYPE_EMPTY) {
                        return gridLayoutManager.getSpanCount();
                    }
                    if (getItemViewType(position) == TYPE_END) {
                        return gridLayoutManager.getSpanCount();
                    }
                    if (lookup != null) {
                        return lookup.getSpanSize(position);
                    }
                    return 1;
                }
            });
        }
    }


    @Override
    public int getItemViewType(int position) {
        if (sourceAdapter.getItemCount() == 0) {
            return TYPE_EMPTY;
        } else {
            if (position == sourceAdapter.getItemCount()) {
                return TYPE_END;
            } else {
                return sourceAdapter.getItemViewType(position);
            }
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_EMPTY) {
            return new UIHolder(emptyView);
        } else if (viewType == TYPE_END) {
            return new UIHolder(endView);
        } else {
            return sourceAdapter.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List payloads) {
        if (!(holder instanceof UIHolder)) {
            sourceAdapter.onBindViewHolder(holder, position, payloads);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //empty
    }

    @Override
    public int getItemCount() {
        if (endView == null) {
            throw new IllegalArgumentException("EndView is null!");
        }
        if (sourceAdapter.getItemCount() == 0) {
            return emptyView == null ? 0 : 1;
        }
        return sourceAdapter.getItemCount() + 1;
    }


    public static class UIHolder extends RecyclerView.ViewHolder {

        public UIHolder(View itemView) {
            super(itemView);
        }
    }

    /**
     * 获取源dapter
     *
     * @return
     */
    public RecyclerView.Adapter getSourceAdapter() {
        return sourceAdapter;
    }

    /**
     * 设置监听(嵌套时无效)，用applyInNested(int state)方法
     *
     * @param onLoadListener
     */
    public void setOnLoadListener(OnLoadListener onLoadListener) {
        this.onLoadListener = onLoadListener;
    }

    public interface OnLoadListener {
        /**
         * 触发下拉刷新
         */
        void onLoad();

        /**
         * 网络异常是内置点击重试
         */
        void onReLoad();
    }
}
