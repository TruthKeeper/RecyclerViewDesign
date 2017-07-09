package com.tk.recyclerview.adapter.base;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

import static android.view.ViewGroup.LayoutParams;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * <pre>
 *     author : TK
 *     time   : 2017/05/10
 *     desc   : 封装简化Holder操作；
 *              封装空数据时的EmptyView；
 *              封装头部视图和足部视图；
 * </pre>
 */

public abstract class BaseAdapter<T> extends MathAdapter<T, RecyclerView.ViewHolder> implements IMath<T> {
    public static final int TYPE_EMPTY = 1 << 16;
    public static final int TYPE_HEADER = 1 << 17;
    public static final int TYPE_FOOTER = 1 << 18;
    private OnItemClickListener mListener;
    private FrameLayout mEmptyContainer;
    private LinearLayout mHeaderContainer;
    private LinearLayout mFooterContainer;
    /**
     * 是否启用空视图
     */
    private boolean isEmptyEnabled = true;

    protected List<T> mList;

    public BaseAdapter() {
        this(null);
    }

    public BaseAdapter(@Nullable List<T> list) {
        if (null == list) {
            mList = new ArrayList<>();
        } else {
            mList = list;
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            final GridLayoutManager.SpanSizeLookup lookup = gridLayoutManager.getSpanSizeLookup();
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int type = getItemViewType(position);
                    switch (type) {
                        case TYPE_HEADER:
                        case TYPE_EMPTY:
                        case TYPE_FOOTER:
                            return gridLayoutManager.getSpanCount();
                    }
                    if (null != lookup) {
                        return lookup.getSpanSize(position - getHeaderViewSpace());
                    }
                    return 1;
                }
            });
        }
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        //瀑布流的支持
        ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
        if (null != params
                && params instanceof StaggeredGridLayoutManager.LayoutParams
                && (holder instanceof EmptyHolder || holder instanceof HeaderHolder || holder instanceof FooterHolder)) {
            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) params;
            //独占一行、列
            p.setFullSpan(true);
        }
    }

    /**
     * 设置空视图
     *
     * @param emptyView
     */
    public void setEmptyView(View emptyView) {
        if (null == emptyView) {
            if (null != mEmptyContainer) {
                mEmptyContainer.removeAllViews();
            }
            return;
        }
        boolean insert = false;
        if (null == mEmptyContainer) {
            mEmptyContainer = new FrameLayout(emptyView.getContext());
            mEmptyContainer.setLayoutParams(new LayoutParams(MATCH_PARENT, WRAP_CONTENT));
            insert = true;
        }
        mEmptyContainer.removeAllViews();
        mEmptyContainer.addView(emptyView);
        if (insert && 1 == getEmptyViewSpace()) {
            //insert
            notifyItemInserted(0);
        }
    }

    /**
     * 添加头view
     *
     * @param view
     */
    public void addHeaderView(@NonNull View view) {
        addHeaderView(view, -1);
    }

    /**
     * 添加头view
     *
     * @param view
     * @param index
     */
    public void addHeaderView(@NonNull View view, int index) {
        if (null == mHeaderContainer) {
            mHeaderContainer = new LinearLayout(view.getContext());
            mHeaderContainer.setOrientation(LinearLayout.VERTICAL);
            mHeaderContainer.setLayoutParams(new LayoutParams(MATCH_PARENT, WRAP_CONTENT));
        }
        mHeaderContainer.addView(view, index);
        if (1 == mHeaderContainer.getChildCount()) {
            notifyItemInserted(0);
        }
    }

    /**
     * 添加足View
     *
     * @param view
     */
    public void addFooterView(@NonNull View view) {
        addFooterView(view, -1);
    }

    /**
     * 添加足View
     *
     * @param view
     * @param index
     */
    public void addFooterView(@NonNull View view, int index) {
        if (null == mFooterContainer) {
            mFooterContainer = new LinearLayout(view.getContext());
            mFooterContainer.setOrientation(LinearLayout.VERTICAL);
            mFooterContainer.setLayoutParams(new LayoutParams(MATCH_PARENT, WRAP_CONTENT));
        }
        mFooterContainer.addView(view, index);
        if (1 == mFooterContainer.getChildCount()) {
            notifyItemInserted(getFooterPosition());
        }
    }

    /**
     * 移除头View
     *
     * @param view
     */
    public void removeHeaderView(@NonNull View view) {
        if (0 == getHeaderViewSpace()) {
            return;
        }
        mHeaderContainer.removeView(view);
        if (0 == mHeaderContainer.getChildCount()) {
            notifyItemRemoved(0);
        }
    }

    /**
     * 移除足View
     *
     * @param view
     */
    public void removeFooterView(@NonNull View view) {
        if (0 == getFooterViewSpace()) {
            return;
        }
        mFooterContainer.removeView(view);
        if (0 == mFooterContainer.getChildCount()) {
            notifyItemRemoved(getFooterPosition());
        }
    }

    /**
     * 移除所有头View
     */
    public void removeAllHeaderViews() {
        if (0 == getHeaderViewSpace()) {
            return;
        }
        mHeaderContainer.removeAllViews();
        notifyItemRemoved(0);
    }

    /**
     * 移除所有足View
     */
    public void removeAllFooterViews() {
        if (0 == getFooterViewSpace()) {
            return;
        }
        mFooterContainer.removeAllViews();
        notifyItemRemoved(getFooterPosition());
    }

    public void setEmptyEnabled(boolean emptyEnabled) {
        isEmptyEnabled = emptyEnabled;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_HEADER:
                return new HeaderHolder(mHeaderContainer);
            case TYPE_EMPTY:
                return new EmptyHolder(mEmptyContainer);
            case TYPE_FOOTER:
                return new FooterHolder(mFooterContainer);
        }

        View itemView = LayoutInflater.from(parent.getContext()).inflate(bindLayout(viewType), parent, false);
        final BaseHolder holder = new BaseHolder(itemView, viewType);
        if (null != mListener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onClick(BaseAdapter.this, holder.itemView, holder.getAdapterPosition());
                }
            });
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof BaseHolder) {
            bindData((BaseHolder) holder, mList.get(holder.getAdapterPosition() - getHeaderViewSpace()));
        }
    }

    @Override
    public int getItemCount() {
        if (1 == getEmptyViewSpace()) {
            return getHeaderViewSpace() + 1 + getFooterViewSpace();
        } else {
            return getHeaderViewSpace() + mList.size() + getFooterViewSpace();
        }
    }

    @Override
    public int getItemViewType(int position) {
        boolean hasHeader = 0 != getHeaderViewSpace();
        if (1 == getEmptyViewSpace()) {
            switch (position) {
                case 0:
                    return hasHeader ? TYPE_HEADER : TYPE_EMPTY;
                case 1:
                    return hasHeader ? TYPE_EMPTY : TYPE_FOOTER;
                case 2:
                    return TYPE_FOOTER;
                default:
                    //理论不出现
                    return TYPE_EMPTY;
            }
        } else {
            boolean hasFooter = 0 != getFooterViewSize();
            if (0 == position) {
                return hasHeader ? TYPE_HEADER : super.getItemViewType(position);
            } else if (position == getItemCount() - 1) {
                return hasFooter ? TYPE_FOOTER : super.getItemViewType(hasHeader ? position - 1 : position);
            } else {
                return super.getItemViewType(hasHeader ? position - 1 : position);
            }
        }
    }

    /**
     * 获取空视图的占用
     *
     * @return
     */
    public int getEmptyViewSpace() {
        if (null == mEmptyContainer || 0 == mEmptyContainer.getChildCount() || (!isEmptyEnabled)) {
            return 0;
        }
        if (mList.isEmpty()) {
            return 1;
        }
        return 0;
    }

    /**
     * 获取头View的占用
     *
     * @return
     */
    public int getHeaderViewSpace() {
        return null == mHeaderContainer || 0 == mHeaderContainer.getChildCount() ? 0 : 1;
    }

    /**
     * 获取足View的占用
     *
     * @return
     */
    public int getFooterViewSpace() {
        return null == mFooterContainer || 0 == mFooterContainer.getChildCount() ? 0 : 1;
    }

    /**
     * 获取头View数量
     *
     * @return
     */
    public int getHeaderViewSize() {
        return null == mHeaderContainer ? 0 : mHeaderContainer.getChildCount();
    }

    /**
     * 获取足View数量
     *
     * @return
     */
    public int getFooterViewSize() {
        return null == mFooterContainer ? 0 : mFooterContainer.getChildCount();
    }

    /**
     * 获取足视图该出现的位置
     *
     * @return
     */
    public int getFooterPosition() {
        if (getEmptyViewSpace() == 1) {
            //在空视图之下
            return 1 + getHeaderViewSpace();
        } else {
            return getHeaderViewSpace() + mList.size();
        }
    }

    /**
     * 重新设置数据源
     *
     * @param list
     */
    @Override
    public void setData(@Nullable List<T> list) {
        if (null == list || list.isEmpty()) {
            clear(false);
        } else {
            mList = list;
            notifyDataSetChanged();
        }
    }

    /**
     * 重新设置数据源，计算差异性刷新，
     * 只在无header时生效，并且建议重写T的equals保证准确性
     *
     * @param list
     */
    @Override
    public void setDataByDiff(@Nullable final List<T> list) {
        if (null == list || list.isEmpty()) {
            clear(false);
        } else {
            if (0 != getHeaderViewSpace()) {
                setData(list);
                return;
            }
            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return mList.size();
                }

                @Override
                public int getNewListSize() {
                    return list.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    //对比equals
                    return mList.get(oldItemPosition).equals(list.get(newItemPosition));
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    return true;
                }
            });
            diffResult.dispatchUpdatesTo(this);
            mList = list;
        }
    }

    /**
     * 添加数据
     *
     * @param t
     */
    @Override
    public void add(@NonNull T t) {
        int space = getEmptyViewSpace();
        mList.add(t);
        if (1 == space) {
            notifyItemChanged(getHeaderViewSpace());
        } else {
            notifyItemInserted(mList.size() - 1 + getHeaderViewSpace());
        }
    }

    /**
     * 添加数据
     *
     * @param index
     * @param t
     */
    @Override
    public void add(int index, @NonNull T t) {
        int space = getEmptyViewSpace();
        mList.add(index, t);
        if (1 == space) {
            notifyItemChanged(getHeaderViewSpace());
        } else {
            notifyItemInserted(index + getHeaderViewSpace());
        }
    }

    /**
     * 添加数据集
     *
     * @param collection
     */
    @Override
    public void addAll(@NonNull Collection<? extends T> collection) {
        int space = getEmptyViewSpace();
        int index = mList.size();
        mList.addAll(collection);
        if (1 == space) {
            notifyItemRangeChanged(getHeaderViewSpace(), collection.size());
        } else {
            notifyItemRangeInserted(index + getHeaderViewSpace(), collection.size());
        }
    }

    /**
     * 添加数据集
     *
     * @param index
     * @param collection
     */
    @Override
    public void addAll(int index, @NonNull Collection<? extends T> collection) {
        int space = getEmptyViewSpace();
        mList.addAll(index, collection);
        if (1 == space) {
            notifyItemRangeChanged(getHeaderViewSpace(), collection.size());
        } else {
            notifyItemRangeInserted(index + getHeaderViewSpace(), collection.size());
        }
    }

    /**
     * 移除
     *
     * @param t
     */
    @Override
    public T remove(@NonNull T t) {
        return remove(mList.indexOf(t), false);
    }

    /**
     * 移除
     *
     * @param t
     * @param immediately 是否取消动画，即时刷新notifyDataSetChanged
     */
    @Override
    public T remove(@NonNull T t, boolean immediately) {
        return remove(mList.indexOf(t), false);
    }

    /**
     * 移除
     *
     * @param position
     */
    @Override
    public T remove(int position) {
        return remove(position, false);
    }

    /**
     * 移除
     *
     * @param position
     * @param immediately 是否取消动画，即时刷新notifyDataSetChanged
     */
    @Override
    public T remove(int position, boolean immediately) {
        if (-1 != position) {
            T t = mList.remove(position);
            if (immediately) {
                notifyDataSetChanged();
            } else {
                notifyItemRemoved(position + getHeaderViewSpace());
            }
            return t;
        }
        return null;
    }

    /**
     * 条件移除
     *
     * @param predicate
     * @return
     */
    @Override
    public boolean removeIf(@NonNull Predicate<T> predicate) {
        return removeIf(predicate, false);
    }

    /**
     * 条件移除
     *
     * @param predicate
     * @param immediately 是否取消动画，即时刷新notifyDataSetChanged
     * @return
     */
    @Override
    public boolean removeIf(@NonNull Predicate<T> predicate, boolean immediately) {
        boolean removed = false;
        final ListIterator<T> listIterator = mList.listIterator();
        int nextIndex;
        while (listIterator.hasNext()) {
            nextIndex = listIterator.nextIndex();
            if (predicate.removeConfirm(listIterator.next())) {
                listIterator.remove();
                if (!immediately) {
                    notifyItemRemoved(nextIndex + getHeaderViewSpace());
                }
                removed = true;
            }
        }
        if (immediately) {
            notifyDataSetChanged();
        }
        return removed;
    }

    /**
     * 清理数据集合
     */
    @Override
    public void clear() {
        clear(false);
    }

    /**
     * 清理数据集合
     *
     * @param immediately
     */
    @Override
    public void clear(boolean immediately) {
        int size = mList.size();
        mList.clear();
        if (immediately) {
            notifyDataSetChanged();
        } else {
            notifyItemRangeRemoved(getHeaderViewSpace(), size);
        }
    }

    /**
     * 获取Adapter数据集合
     *
     * @return
     */
    @Override
    public List<T> getList() {
        return mList;
    }

    /**
     * 获取数据集合中的某条数据
     *
     * @param position
     * @return
     */
    @Override
    public T getListItem(int position) {
        return mList.get(position);
    }

    /**
     * 布局Id
     *
     * @param viewType
     * @return
     */
    @LayoutRes
    public abstract int bindLayout(int viewType);

    public abstract void bindData(BaseHolder holder, T data);

    /**
     * 设置点击监听
     *
     * @param onItemClickListener
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mListener = onItemClickListener;
    }

    public static class EmptyHolder extends RecyclerView.ViewHolder {
        public EmptyHolder(View itemView) {
            super(itemView);
        }
    }

    public static class HeaderHolder extends RecyclerView.ViewHolder {
        public HeaderHolder(View itemView) {
            super(itemView);
        }
    }

    public static class FooterHolder extends RecyclerView.ViewHolder {
        public FooterHolder(View itemView) {
            super(itemView);
        }
    }

    public interface OnItemClickListener {
        void onClick(BaseAdapter adapter, View view, int position);
    }
}
