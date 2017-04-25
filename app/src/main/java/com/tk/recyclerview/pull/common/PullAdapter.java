package com.tk.recyclerview.pull.common;

import android.content.res.Resources;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;
import java.util.List;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * <pre>
 *     author : TK
 *     time   : 2017/04/05
 *     desc   : 上拉加载，代理模式，原理：
 *              ItemViewType；
 *              监听RecyclerView滚动；
 * </pre>
 */
public class PullAdapter extends RecyclerView.Adapter {
    public static final int TYPE_EMPTY = 23333;
    public static final int TYPE_END = 24444;
    /**
     * 待命
     */
    public static final int LOAD_STANDBY = 0x00;
    /**
     * 加载失败
     */
    public static final int LOAD_ERROR = 0x01;
    /**
     * 加载完毕
     */
    public static final int LOAD_END = 0x02;
    /**
     * 加载中
     */
    public static final int LOAD_ING = 0x03;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({LOAD_STANDBY, LOAD_ERROR, LOAD_END, LOAD_ING})
    public @interface Status {
    }


    /**
     * RecyclerView当前状态
     */
    private int status = LOAD_STANDBY;
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
    /**
     * 无数据时是否显示空视图，默认显示
     */
    private boolean emptyViewFitShow = true;

    private RecyclerView.LayoutManager layoutManager;
    /**
     * 滚动监听
     */
    private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(final RecyclerView recyclerView, final int newState) {
            int lastVisibleItemPosition = 0;
            int firstCompletelyVisibleItemPosition = 0;

            if (layoutManager instanceof LinearLayoutManager) {
                LinearLayoutManager manager = (LinearLayoutManager) layoutManager;
                lastVisibleItemPosition = manager.findLastVisibleItemPosition();
                firstCompletelyVisibleItemPosition = manager.findFirstCompletelyVisibleItemPosition();

            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                StaggeredGridLayoutManager manager = (StaggeredGridLayoutManager) layoutManager;
                int[] pos = new int[manager.getSpanCount()];
                manager.findLastVisibleItemPositions(pos);
                Arrays.sort(pos);
                lastVisibleItemPosition = pos[manager.getSpanCount() - 1];
                manager.findFirstCompletelyVisibleItemPositions(pos);
                Arrays.sort(pos);
                firstCompletelyVisibleItemPosition = pos[0];
            }
             /*
              * 悬停状态
              * Adapter有数据
              * 待命状态
              * 滚动到底
              */
            if (newState == SCROLL_STATE_IDLE
                    && (sourceAdapter != null && sourceAdapter.getItemCount() != 0)
                    && (status == LOAD_STANDBY)
                    && (lastVisibleItemPosition == sourceAdapter.getItemCount()
                    && firstCompletelyVisibleItemPosition != 0)) {
                //开始加载
                status = LOAD_ING;
                endView.setVisibility(VISIBLE);
                ((IEnd) endView).onShow();
                if (onLoadListener != null) {
                    onLoadListener.onLoad();
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
        this.emptyView.setVisibility(VISIBLE);
        initEndView(endView);
    }

    /**
     * 用于嵌套场景
     * @param state NestedScrollView或者ScrollView的滚动状态
     */
    public void applyInNested(int state) {
            /*
             * 悬停状态
             * Adapter有数据
             * 待命状态
             * 滚动到底
             */
        if (state == SCROLL_STATE_IDLE
                && (sourceAdapter != null && sourceAdapter.getItemCount() != 0)
                && (status == LOAD_STANDBY)) {
            View view = layoutManager.findViewByPosition(sourceAdapter.getItemCount());
            int[] location = new int[2];
            view.getLocationInWindow(location);
            if (location[1] <= Resources.getSystem().getDisplayMetrics().heightPixels) {
                //可见了
                status = LOAD_ING;
                endView.setVisibility(VISIBLE);
                ((IEnd) endView).onShow();
                if (onLoadListener != null) {
                    onLoadListener.onLoad();
                }
            }
        }
    }

    /**
     * 加载完回调
     *
     * @param status，刷新到底了 / 网络错误 / 刷新结束，待命状态
     */
    public void setLoadResult(@Status int status) {
        switch (status) {
            case LOAD_END:
                ((IEnd) endView).inTheEnd();
                break;
            case LOAD_ERROR:
                ((IEnd) endView).onError();
                break;
            case LOAD_STANDBY:
                ((IEnd) endView).onDismiss();
                break;
            default:
                return;
        }
        this.status = status;
    }

    /**
     * <pre>
     *     适配空数据时是否显示 空视图
     *     场景：初始化成false，页面第一次加载后设置为true,
     *     效果为第一次进页面不显示空视图，后续加载如无数据显示空视图
     * <pre/>
     *
     * @param emptyViewFitShow
     */
    public void setEmptyViewFitShow(boolean emptyViewFitShow) {
        if (this.emptyViewFitShow != emptyViewFitShow) {
            this.emptyViewFitShow = emptyViewFitShow;
            if (sourceAdapter.getItemCount() == 0) {
                notifyDataSetChanged();
            }
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
                if (status == LOAD_ERROR) {
                    status = LOAD_ING;
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

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        //瀑布流的支持
        ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
        if (params != null
                && params instanceof StaggeredGridLayoutManager.LayoutParams
                && holder instanceof UIHolder) {
            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) params;
            //独占一行、列
            p.setFullSpan(true);
        }
        sourceAdapter.onViewAttachedToWindow(holder);
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
            if (emptyView == null) {
                return 0;
            } else {
                return emptyViewFitShow ? 1 : 0;
            }
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
