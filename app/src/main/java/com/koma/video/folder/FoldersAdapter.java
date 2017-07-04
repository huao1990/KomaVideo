/*
 * Copyright 2017 Koma
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.koma.video.folder;

import android.content.Context;
import android.net.Uri;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.koma.video.R;
import com.koma.video.data.model.Video;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by koma on 6/30/17.
 */

public class FoldersAdapter extends RecyclerView.Adapter<FoldersAdapter.FoldersViewHolder> {
    private List<List<Video>> mData;

    private Context mContext;

    private RequestOptions mRequestOptions;

    public FoldersAdapter(Context context) {
        mContext = context;

        mRequestOptions = new RequestOptions().centerCrop()
                .placeholder(R.drawable.ic_default_video)
                .error(R.drawable.ic_default_video);
    }

    public void replaceData(final List<List<Video>> folderList) {
        if (mData == null) {
            mData = folderList;

            notifyItemRangeInserted(0, folderList.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return mData.size();
                }

                @Override
                public int getNewListSize() {
                    return folderList.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    List<Video> oldList = mData.get(oldItemPosition);
                    List<Video> newList = folderList.get(newItemPosition);
                    if (oldList.size() != newList.size()) {
                        return false;
                    } else {
                        for (int i = 0; i < oldList.size(); i++) {
                            if (newList.get(i).getId() == oldList.get(i).getId()) {
                                continue;
                            }
                            return true;
                        }
                        return false;
                    }
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    List<Video> newVideoList = folderList.get(newItemPosition);
                    List<Video> oldVideoList = folderList.get(oldItemPosition);
                    if (newVideoList.size() != oldVideoList.size()) {
                        return false;
                    } else {
                        for (int i = 0; i < newVideoList.size(); i++) {
                            if (newVideoList.get(i).equals(oldVideoList.get(i))) {
                                continue;
                            }
                            return true;
                        }
                        return false;
                    }
                }
            });
            mData = folderList;

            result.dispatchUpdatesTo(this);
        }
    }

    @Override
    public FoldersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_video, parent, false);
        return new FoldersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FoldersViewHolder holder, int position) {
        holder.itemView.setTag(position);

        Glide.with(mContext).load(Uri.fromFile(new File(mData.get(position).get(0).getPath())))
                .apply(mRequestOptions)
                .into(holder.mVideoImage);

        holder.mVideoTitle.setText(mData.get(position).get(0).getFolderPath());
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    static class FoldersViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_video)
        ImageView mVideoImage;
        @BindView(R.id.tv_title)
        TextView mVideoTitle;

        public FoldersViewHolder(View view) {
            super(view);

            ButterKnife.bind(this, view);
        }
    }
}
