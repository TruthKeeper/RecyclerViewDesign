package com.tk.recyclerview.layout.flow;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

/**
 * <pre>
 *     author : TK
 *     time   : 2017/04/07
 *     desc   : 流式布局
 * </pre>
 */
public class FlowLayoutManager extends RecyclerView.LayoutManager {
    /**
     * 竖直滚动偏移量
     */
    private int verticalOffset;
    /**
     * 可见的第一个View的Position
     */
    private int firstVisibilePosition;
    /**
     * 可见的最后一个View的Position
     */
    private int lastVisibilePosition;
    /**
     * 正序layout时用来记录layout，逆序时获取，因为正序是一定会经历的
     */
    private SparseArray<Rect> itemRects;

    {
        itemRects = new SparseArray<>();
        setAutoMeasureEnabled(true);
    }


    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    /**
     * 类似ViewGroup的onLayout方法
     *
     * @param recycler
     * @param state
     */
    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        //全部分离并回收
        detachAndScrapAttachedViews(recycler);
        if (getItemCount() == 0) {
            return;
        }

        verticalOffset = 0;
        firstVisibilePosition = 0;
        lastVisibilePosition = getItemCount();
        //布局
        fill(recycler, state, 0);
    }

    /**
     * 填充
     *
     * @param recycler
     * @param state
     * @param dy
     * @return
     */
    private int fill(RecyclerView.Recycler recycler, RecyclerView.State state, int dy) {
        int childCount = getChildCount();
        View child;
        int leftOffset = getPaddingLeft();
        int topOffset = getPaddingTop();
        int lineHeight = 0;

        if (childCount > 0) {
            //已经有子View时，说明在滑动时调用，需要回收不再显示的View
            for (int i = childCount - 1; i >= 0; i--) {
                child = getChildAt(i);
                if (dy > 0) {
                    //上滑，是否回收上屏幕的View
                    if (getDecoratedBottom(child) - dy < topOffset) {
                        //回收
                        removeAndRecycleView(child, recycler);
                        firstVisibilePosition++;
                    }
                } else if (dy < 0) {
                    //下滑，是否回收下屏幕的View
                    if (getDecoratedTop(child) - dy > getHeight() - getPaddingBottom()) {
                        //回收
                        removeAndRecycleView(child, recycler);
                        lastVisibilePosition--;
                    }
                }
            }
        }

        //重新赋值
        childCount = getChildCount();
        //开始布局
        if (dy >= 0) {
            //上滑，从最后一个显示的开始添加view，<正序>
            int minPos = firstVisibilePosition;
            lastVisibilePosition = getItemCount() - 1;
            if (childCount > 0) {
                //之前回收过，修正偏移
                View lastChild = getChildAt(childCount - 1);
                minPos = getPosition(lastChild) + 1;
                topOffset = getRealChildTop(lastChild);
                leftOffset = getDecoratedRight(lastChild);
                lineHeight = Math.max(lineHeight, getChildRealH(lastChild));
            }
            Rect childRect;
            for (int i = minPos; i <= lastVisibilePosition; i++) {
                child = recycler.getViewForPosition(i);
                //测量并添加
                measureChildWithMargins(child, 0, 0);
                addView(child);

                int childW = getChildRealW(child);
                int childH = getChildRealH(child);
                if (leftOffset + childW <= getRealW()) {
                    //一行还留有空间
                    layoutDecoratedWithMargins(child,
                            leftOffset,
                            topOffset,
                            leftOffset + childW,
                            topOffset + childH);
                    //加上偏移
                    childRect = new Rect(leftOffset,
                            topOffset + verticalOffset,
                            leftOffset + childW,
                            topOffset + childH + verticalOffset);
                    //记录，以便倒序layout
                    itemRects.put(i, childRect);
                    //修正偏移
                    leftOffset += childW;
                    lineHeight = Math.max(lineHeight, childH);
                } else {
                    //换行了
                    //修正偏移
                    leftOffset = getPaddingLeft();
                    topOffset += lineHeight;
                    lineHeight = 0;
                    if (topOffset - dy > getHeight() - getPaddingBottom()) {
                        //不可见了
                        removeAndRecycleView(child, recycler);
                        lastVisibilePosition = i - 1;
                    } else {
                        layoutDecoratedWithMargins(child,
                                leftOffset,
                                topOffset,
                                leftOffset + childW,
                                topOffset + childH);
                        //加上偏移
                        childRect = new Rect(leftOffset,
                                topOffset + verticalOffset,
                                leftOffset + childW,
                                topOffset + childH + verticalOffset);
                        //记录，以便倒序layout
                        itemRects.put(i, childRect);
                        //修正偏移
                        leftOffset += childW;
                        lineHeight = Math.max(lineHeight, childH);
                    }
                }
            }

            //添加完子view后，屏幕下还有空间，对dy进行修正
            View lastChild = getChildAt(getChildCount() - 1);
            if (getPosition(lastChild) == getItemCount() - 1) {
                int space = getHeight() - getPaddingBottom() - getDecoratedBottom(lastChild);
                if (space > 0) {
                    dy -= space;
                }

            }
        } else {
            //下滑，从第一个显示的开始添加view，<倒序>
            firstVisibilePosition = 0;
            View firstView = getChildAt(0);
            int maxPos = getPosition(firstView) - 1;
            Rect childRect;
            for (int i = maxPos; i >= firstVisibilePosition; i--) {
                //倒序添加
                childRect = itemRects.get(i);

                if (childRect.bottom - dy - verticalOffset < getPaddingTop()) {
                    firstVisibilePosition = i + 1;
                    break;
                } else {
                    child = recycler.getViewForPosition(i);
                    measureChildWithMargins(child, 0, 0);
                    addView(child, 0);

                    layoutDecoratedWithMargins(child, childRect.left, childRect.top - verticalOffset,
                            childRect.right, childRect.bottom - verticalOffset);
                }
            }
        }
        return dy;
    }

    @Override
    public boolean canScrollVertically() {
        //支持上下滑动
        return true;
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.
            State state) {
        if (dy == 0 || getChildCount() == 0) {
            //没有子View
            return 0;
        }
        int realOffset = dy;//实际滑动的距离， 可能会在边界处被修复
        //边界修复代码
        if (verticalOffset + realOffset < 0) {
            //下滑到顶了
            realOffset = -verticalOffset;
        } else if (realOffset > 0) {

            View lastChild = getChildAt(getChildCount() - 1);
            if (getItemCount() == getChildCount() && (getHeight() - getPaddingBottom()) >= getDecoratedBottom(lastChild)) {
                //屏幕下方留空时
                realOffset = 0;
            } else if (getPosition(lastChild) == getItemCount() - 1) {
                int gap = getHeight() - getPaddingBottom() - getDecoratedBottom(lastChild);
                if (gap > 0) {
                    realOffset = -gap;
                } else if (gap == 0) {
                    realOffset = 0;
                } else {
                    realOffset = Math.min(realOffset, -gap);
                }
            }
        }
        if (realOffset == 0) {
            return 0;
        }
        realOffset = fill(recycler, state, realOffset);//先填充，再位移。

        verticalOffset += realOffset;//累加实际滑动距离

        offsetChildrenVertical(-realOffset);//滑动
        return realOffset;
    }

    /**
     * 获取view的占用宽度
     *
     * @param child
     * @return
     */

    private int getChildRealW(View child) {
        ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
        return getDecoratedMeasuredWidth(child) + p.leftMargin + p.rightMargin;
    }

    /**
     * 获取view的占用高度
     *
     * @param child
     * @return
     */
    private int getChildRealH(View child) {
        ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
        return getDecoratedMeasuredHeight(child) + p.topMargin + p.bottomMargin;
    }

    /**
     * 获取RecyclerView可用宽度
     *
     * @return
     */
    private int getRealW() {
        return getWidth() - getPaddingLeft() - getPaddingRight();
    }


    private int getRealChildTop(View child) {
        ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
        return getDecoratedTop(child) - p.topMargin;
    }
}
