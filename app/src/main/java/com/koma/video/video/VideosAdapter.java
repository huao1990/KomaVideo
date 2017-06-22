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
package com.koma.video.video;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import com.koma.video.util.Utils;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.VideoViewHolder> {
    private List<Video> mData;

    private Context mContext;

    private RequestOptions mRequestOptions;

    public VideosAdapter(Context context, List<Video> data) {
        mContext = context;

        mRequestOptions = new RequestOptions().centerCrop()
                .placeholder(R.drawable.ic_default_video)
                .error(R.drawable.ic_default_video);

        mData = data;
    }

    public void replaceData(List<Video> videoList) {
        setList(videoList);

        notifyDataSetChanged();
    }

    private void setList(List<Video> videoList) {
        mData = videoList;
    }


    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_video, parent, false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final VideoViewHolder holder, int position) {
        holder.itemView.setTag(position);
        Glide.with(mContext).load(Uri.fromFile(new File(mData.get(position).getPath())))
                .apply(mRequestOptions)
                .into(holder.mVideoImage);
        holder.mVideoTitle.setText(mData.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    class VideoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.iv_video)
        ImageView mVideoImage;
        @BindView(R.id.tv_title)
        TextView mVideoTitle;

        public VideoViewHolder(View view) {
            super(view);

            ButterKnife.bind(this, view);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = (int) v.getTag();
            Intent intent = new Intent(Intent.ACTION_VIEW);
            String type = "video/*";
            intent.setDataAndType(Utils.getVideoUri(mData.get(position).getId()), type);
            mContext.startActivity(intent);
        }
    }
}
