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
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.koma.video.KomaVideoApplication;
import com.koma.video.R;
import com.koma.video.base.BaseAdapter;
import com.koma.video.base.BaseFragment;
import com.koma.video.data.model.Video;
import com.koma.video.util.LogUtils;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindColor;
import butterknife.BindString;
import butterknife.BindView;

/**
 * Created by koma on 5/27/17.
 */

public class VideosFragment extends BaseFragment implements VideosContract.View,
        BaseAdapter.OnItemClickListener {
    private static final String TAG = VideosFragment.class.getSimpleName();

    @BindString(R.string.loading_videos_error)
    String mErrorMessage;

    @BindColor(R.color.colorPrimary)
    @ColorInt
    int mColorPrimary;
    @BindColor(R.color.colorPrimaryDark)
    @ColorInt
    int mColorPrimaryDark;
    @BindColor(R.color.colorAccent)
    @ColorInt
    int mColorAccent;

    @BindView(R.id.tv_no_videos)
    View mNoVideosView;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout mRefreshLayout;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private VideosAdapter mAdapter;

    @Inject
    VideosPresenter mPresenter;

    public VideosFragment() {
        // Requires empty public constructor
    }

    public static VideosFragment newInstance() {
        return new VideosFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LogUtils.i(TAG, "onCreate");
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LogUtils.i(TAG, "onViewCreated");

        initViews();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        LogUtils.i(TAG, "onActivityCreated");

        DaggerVideosComponent.builder()
                .videosRepositoryComponent(
                        ((KomaVideoApplication) getActivity().getApplication()).getVideosRepositoryComponent())
                .videosPresenterModule(new VideosPresenterModule(this))
                .build()
                .inject(this);
    }

    private void initViews() {
        mAdapter = new VideosAdapter(mContext);
        mAdapter.setOnItemClickListener(this);

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

        LogUtils.i(TAG, "onStart");

        if (mPresenter != null) {
            mPresenter.subscribe();
            LogUtils.i(TAG,"mPresenter :" + mPresenter.hashCode());
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        LogUtils.i(TAG, "onStop");

        if (mPresenter != null) {
            mPresenter.unSubscribe();
        }
    }

    @Override
    public void setPresenter(@NonNull VideosContract.Presenter presenter) {
    }

    @Override
    public Context getContext() {
        return mContext;
    }

    @Override
    public void setLoadingIndicator(final boolean active) {
        LogUtils.i(TAG, "setLoadingIndicator active : " + active);

        mRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.setRefreshing(active);
            }
        });
    }

    @Override
    public void showVideos(List<Video> videoList) {
        LogUtils.i(TAG, "showVideos");

        mAdapter.replaceData(videoList);

        mRecyclerView.setVisibility(View.VISIBLE);
        mNoVideosView.setVisibility(View.GONE);
    }

    @Override
    public void showLoadingVideosError() {
        LogUtils.i(TAG, "showLoadingError");

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

    @Override
    public boolean onItemClick(int position) {
        return false;
    }
}
