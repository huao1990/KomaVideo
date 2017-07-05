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
package com.koma.video.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.ButterKnife;

/**
 * Created by koma on 7/5/17.
 */

public class BaseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private BaseAdapter mAdapter;

    public BaseViewHolder(View view, BaseAdapter adapter) {
        super(view);

        ButterKnife.bind(this, view);

        mAdapter = adapter;

        this.itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int position = getAdapterPosition();
        if (mAdapter.getItemClickListener() != null) {
            // Get the permission to activate the View from user
            if (mAdapter.getItemClickListener().onItemClick(position)) {

            }
        }
    }
}
