package com.koma.video.base;

import android.support.v7.widget.RecyclerView;

/**
 * Created by koma on 7/5/17.
 */

public abstract class BaseAdapter<T extends BaseViewHolder> extends RecyclerView.Adapter<BaseViewHolder> {
    /* Listeners */
    private OnItemClickListener mItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    public OnItemClickListener getItemClickListener() {
        return this.mItemClickListener;
    }


    public interface OnItemClickListener {
        boolean onItemClick(int position);
    }
}
