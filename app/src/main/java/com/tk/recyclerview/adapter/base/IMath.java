package com.tk.recyclerview.adapter.base;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Collection;
import java.util.List;

/**
 * <pre>
 *     author : TK
 *     time   : 2017/07/08
 *     desc   : xxxx描述
 * </pre>
 */
public interface IMath<T> {
    /**
     * 重新设置数据源
     *
     * @param list
     */
    void setData(@Nullable List<T> list);

    /**
     * 重新设置数据源，计算差异性刷新，建议重写T的equals保证准确性
     *
     * @param list
     */
    void setDataByDiff(@Nullable final List<T> list);

    /**
     * 添加数据
     *
     * @param t
     */
    void add(@NonNull T t);

    /**
     * 添加数据
     *
     * @param index
     * @param t
     */
    void add(int index, @NonNull T t);

    /**
     * 添加数据集
     *
     * @param collection
     */
    void addAll(@NonNull Collection<? extends T> collection);

    /**
     * 添加数据集
     *
     * @param index
     * @param collection
     */
    void addAll(int index, @NonNull Collection<? extends T> collection);

    /**
     * 移除
     *
     * @param t
     */
    T remove(@NonNull T t);

    /**
     * 移除
     *
     * @param t
     * @param immediately 是否取消动画，即时刷新notifyDataSetChanged
     */
    T remove(@NonNull T t, boolean immediately);

    /**
     * 移除
     *
     * @param position
     */
    T remove(int position);

    /**
     * 移除
     *
     * @param position
     * @param immediately 是否取消动画，即时刷新notifyDataSetChanged
     */
    T remove(int position, boolean immediately);

    /**
     * 条件移除
     *
     * @param predicate
     * @return
     */
    boolean removeIf(@NonNull Predicate<T> predicate);

    /**
     * 条件移除
     *
     * @param predicate
     * @param immediately 是否取消动画，即时刷新notifyDataSetChanged
     * @return
     */
    boolean removeIf(@NonNull Predicate<T> predicate, boolean immediately);

    /**
     * 清理数据集合
     */
    void clear();

    /**
     * 清理数据集合
     *
     * @param immediately
     */
    void clear(boolean immediately);

    /**
     * 获取Adapter数据集合
     *
     * @return
     */
    List<T> getList();

    /**
     * 获取数据集合中的某条数据
     *
     * @param position
     * @return
     */
    T getListItem(int position);

}
