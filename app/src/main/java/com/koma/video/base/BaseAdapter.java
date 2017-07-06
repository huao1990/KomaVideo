package com.koma.video.base;

import android.annotation.SuppressLint;
import android.support.annotation.IntDef;
import android.support.v7.widget.RecyclerView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static com.koma.video.base.BaseAdapter.Mode.IDLE;
import static com.koma.video.base.BaseAdapter.Mode.MULTI;

/**
 * Created by koma on 7/5/17.
 */

public abstract class BaseAdapter<T extends BaseViewHolder> extends RecyclerView.Adapter<BaseViewHolder> {
    protected boolean mSelectAll = false;
    private final Set<Integer> mSelectedPositions;
    /* Listeners */
    private OnItemClickListener mItemClickListener;

    private OnItemLongClickListener mItemLongClickListener;

    private OnMultiModeChangeListener mMultiModeChangeListener;

    /**
     * Annotation interface for selection modes: {@link #IDLE}, {@link #MULTI}
     */
    @SuppressLint("UniqueConstants")
    @IntDef({IDLE, MULTI})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Mode {
        int IDLE = 0, MULTI = 1;
    }

    private int mMode;

    public BaseAdapter() {
        mMode = IDLE;

        mSelectedPositions = Collections.synchronizedSet(new TreeSet<Integer>());
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    public OnItemClickListener getItemClickListener() {
        return this.mItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.mItemLongClickListener = listener;
    }

    public OnItemLongClickListener getItemLongClickListener() {
        return this.mItemLongClickListener;
    }

    public void setMultiModeChangeListener(OnMultiModeChangeListener listener) {
        this.mMultiModeChangeListener = listener;
    }

    public OnMultiModeChangeListener getMultiModeChangeListener() {
        return this.mMultiModeChangeListener;
    }

    /**
     * Indicates if the item, at the provided position, is selected.
     *
     * @param position Position of the item to check.
     * @return true if the item is selected, false otherwise.
     * @since 1.0.0
     */
    public boolean isSelected(int position) {
        return mSelectedPositions.contains(position);
    }

    public void setMode(@Mode int mode) {
        if (this.mMultiModeChangeListener != null) {
            this.mMultiModeChangeListener.onMultiModeChange(mode);
        }

        if (mode == IDLE) {
            clearSelection();
        }
        this.mMode = mode;
    }

    @Mode
    public int getMode() {
        return mMode;
    }

    public void toggleSelection(int position) {
        if (position < 0) {
            return;
        }

        boolean contains = mSelectedPositions.contains(position);
        if (contains) {
            removeSelection(position);
        } else {
            addSelection(position);
        }

        notifyItemChanged(position);
    }

    public final boolean addSelection(int position) {
        return mSelectedPositions.add(position);
    }


    public final boolean removeSelection(int position) {
        return mSelectedPositions.remove(position);
    }

    /**
     * Helper method to easily swap selection between 2 positions only if one of the positions
     * is <i>not</i> selected.
     *
     * @param fromPosition first position
     * @param toPosition   second position
     */
    protected void swapSelection(int fromPosition, int toPosition) {
        if (isSelected(fromPosition) && !isSelected(toPosition)) {
            removeSelection(fromPosition);
            addSelection(toPosition);
        } else if (!isSelected(fromPosition) && isSelected(toPosition)) {
            removeSelection(toPosition);
            addSelection(fromPosition);
        }
    }

    public void selectAll(Integer... viewTypes) {
        mSelectAll = true;
        List<Integer> viewTypesToSelect = Arrays.asList(viewTypes);
        int positionStart = 0, itemCount = 0;
        for (int i = 0; i < getItemCount(); i++) {
            if ((viewTypesToSelect.isEmpty() || viewTypesToSelect.contains(getItemViewType(i)))) {
                mSelectedPositions.add(i);
                itemCount++;
            } else {
                // Optimization for ItemRangeChanged
                if (positionStart + itemCount == i) {
                    notifySelectionChanged(positionStart, itemCount);
                    itemCount = 0;
                    positionStart = i;
                }
            }
        }
        notifySelectionChanged(positionStart, getItemCount());
    }

    public boolean isSelectAll() {
        return mSelectAll;
    }

    public void clearSelection() {
        synchronized (mSelectedPositions) {
            Iterator<Integer> iterator = mSelectedPositions.iterator();
            int positionStart = 0, itemCount = 0;
            // The notification is done only on items that are currently selected.
            while (iterator.hasNext()) {
                int position = iterator.next();
                iterator.remove();
                // Optimization for ItemRangeChanged
                if (positionStart + itemCount == position) {
                    itemCount++;
                } else {
                    // Notify previous items in range
                    notifySelectionChanged(positionStart, itemCount);
                    positionStart = position;
                    itemCount = 1;
                }
            }
            // Notify remaining items in range
            notifySelectionChanged(positionStart, itemCount);
        }
    }

    private void notifySelectionChanged(int positionStart, int itemCount) {
        if (itemCount > 0) {
            notifyItemRangeChanged(positionStart, itemCount);
        }
    }

    public int getSelectedItemCount() {
        return mSelectedPositions.size();
    }

    public List<Integer> getSelectedPositions() {
        return new ArrayList<>(mSelectedPositions);
    }

    public Set<Integer> getSelectedPositionsAsSet() {
        return mSelectedPositions;
    }

    public interface OnItemClickListener {
        boolean onItemClick(int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(int position);
    }

    public interface OnMultiModeChangeListener {
        void onMultiModeChange(@Mode int mode);
    }
}
