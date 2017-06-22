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

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.koma.video.R;
import com.koma.video.base.BaseFragment;
import com.koma.video.data.model.Video;
import com.koma.video.util.KomaLogUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindColor;
import butterknife.BindString;
import butterknife.BindView;

/**
 * Created by koma on 5/27/17.
 */

public class VideosFragment extends BaseFragment implements VideosConstract.View {
    private static final String TAG = VideosFragment.class.getSimpleName();

    @BindString(R.string.loading_videos_error)
    String mErrorMessage;

    @BindColor(R.color.colorPrimary)
    int mColorPrimary;
    @BindColor(R.color.colorPrimaryDark)
    int mColorPrimaryDark;
    @BindColor(R.color.colorAccent)
    int mColorAccent;

    @BindView(R.id.tv_no_videos)
    View mNoVideosView;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout mRefreshLayout;

    @NonNull
    private VideosConstract.Presenter mPresenter;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private VideosAdapter mAdapter;

    public VideosFragment() {
        // Requires empty public constructor
    }

    public static VideosFragment newInstance() {
        return new VideosFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        KomaLogUtils.i(TAG, "onViewCreated");

        initViews();
    }

    private void initViews() {
        mAdapter = new VideosAdapter(mContext, new ArrayList<Video>());

        GridLayoutManager layoutManager = new GridLayoutManager(mContext, 2);
        layoutManager.setOrientation(GridLayoutManager.VERTICAL);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);

        mRefreshLayout.setColorSchemeColors(mColorPrimary, mColorAccent, mColorPrimary);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.loadVideos();
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.videos_fragment_layout;
    }

    @Override
    public void onStart() {
        super.onStart();

        KomaLogUtils.i(TAG, "onStart");

        if (mPresenter != null) {
            mPresenter.subscribe();
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        KomaLogUtils.i(TAG, "onStop");

        if (mPresenter != null) {
            mPresenter.unSubscribe();
        }
    }

    @Override
    public void setPresenter(@NonNull VideosConstract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void setLoadingIndicator(final boolean active) {
        KomaLogUtils.i(TAG, "setLoadingIndicator active : " + active);

        mRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.setRefreshing(active);
            }
        });
    }

    @Override
    public void showVideos(List<Video> videoList) {
        KomaLogUtils.i(TAG, "showVideos");

        mAdapter.replaceData(videoList);

        mRecyclerView.setVisibility(View.VISIBLE);
        mNoVideosView.setVisibility(View.GONE);
    }

    @Override
    public void showLoadingVideosError() {
        KomaLogUtils.i(TAG, "showLoadingError");

        showMessage(mErrorMessage);
    }

    private void showMessage(String message) {
        if (getView() == null) {
            return;
        }

        Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showNoVideos() {
        mRecyclerView.setVisibility(View.GONE);

        mNoVideosView.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean isActive() {
        return this.isAdded();
    }
}
