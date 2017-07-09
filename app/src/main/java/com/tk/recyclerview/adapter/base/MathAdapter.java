package com.tk.recyclerview.adapter.base;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

/**
 * <pre>
 *     author : TK
 *     time   : 2017/07/08
 *     desc   : 封装集合运算的Adapter
 * </pre>
 */
public abstract class MathAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> implements IMath<T> {
    protected List<T> mList;

    public MathAdapter() {
        this(null);
    }

    public MathAdapter(@Nullable List<T> list) {
        if (null == list) {
            mList = new ArrayList<>();
        } else {
            mList = list;
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
     * 重新设置数据源，计算差异性刷新，建议重写T的equals保证准确性
     *
     * @param list
     */
    @Override
    public void setDataByDiff(@Nullable final List<T> list) {
        if (null == list || list.isEmpty()) {
            clear(false);
        } else {
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
        mList.add(t);
        notifyItemInserted(mList.size() - 1);
    }

    /**
     * 添加数据
     *
     * @param index
     * @param t
     */
    @Override
    public void add(int index, @NonNull T t) {
        mList.add(index, t);
        notifyItemRangeInserted(index, 1);
    }

    /**
     * 添加数据集
     *
     * @param collection
     */
    @Override
    public void addAll(@NonNull Collection<? extends T> collection) {
        int index = mList.size();
        mList.addAll(collection);
        notifyItemRangeInserted(index, collection.size());
    }

    /**
     * 添加数据集
     *
     * @param index
     * @param collection
     */
    @Override
    public void addAll(int index, @NonNull Collection<? extends T> collection) {
        mList.addAll(index, collection);
        notifyItemRangeInserted(index, collection.size());
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
                notifyItemRemoved(position);
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
                    notifyItemRemoved(nextIndex);
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
            notifyItemRangeRemoved(0, size);
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
}