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

import android.database.ContentObserver;
import android.os.Handler;
import android.os.Looper;

import com.koma.video.data.VideosDataSource;
import com.koma.video.data.VideosRepository;
import com.koma.video.data.model.Video;
import com.koma.video.util.Constants;
import com.koma.video.util.LogUtils;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

/**
 * Listens to user actions from the UI ({@link VideosFragment}), retrieves the data and updates the
 * UI as required.
 * <p/>
 * By marking the constructor with {@code @Inject}, Dagger injects the dependencies required to
 * create an instance of the TasksPresenter (if it fails, it emits a compiler error).  It uses
 * {@link VideosPresenterModule} to do so.
 * <p/>
 * Dagger generated code doesn't require public access to the constructor or class, and
 * therefore, to ensure the developer doesn't instantiate the class manually and bypasses Dagger,
 * it's good practice minimise the visibility of the class/constructor as much as possible.
 **/
public class VideosPresenter implements VideosContract.Presenter {
    private static final String TAG = VideosPresenter.class.getSimpleName();

    private static final int REFRESH_TIME = 500;

    private final VideosContract.View mView;

    private final VideosRepository mRepository;

    private final CompositeDisposable mDisposables;

    private final Handler mHandler = new Handler(Looper.getMainLooper());

    /**
     * Dagger strictly enforces that arguments not marked with {@code @Nullable} are not injected
     * with {@code @Nullable} values.
     */
    @Inject
    public VideosPresenter(VideosContract.View view, VideosRepository repository) {
        mView = view;

        mRepository = repository;

        mDisposables = new CompositeDisposable();
    }

    /**
     * Method injection is used here to safely reference {@code this} after the object is created.
     */
    @Inject
    void setupListeners() {
        mView.setPresenter(this);
    }

    @Override
    public void subscribe() {
        // register vidio uri observer
        registerLocalObserver();

        loadVideos();
    }

    private final ContentObserver mObserver = new ContentObserver(mHandler) {
        @Override
        public void onChange(boolean selfChange) {
            mHandler.removeCallbacks(mRefreshRunnable);
            mHandler.postDelayed(mRefreshRunnable, REFRESH_TIME);
        }
    };

    private final Runnable mRefreshRunnable = new Runnable() {
        @Override
        public void run() {
            loadVideos();
        }
    };

    @Override
    public void unSubscribe() {
        unregisterLocalObserver();

        if (mDisposables != null) {
            mDisposables.clear();
        }
    }

    @Override
    public void loadVideos() {
        if (mDisposables != null) {
            mDisposables.clear();
        }

        mRepository.loadVideos(new VideosDataSource.LoadVideosCallback() {
            @Override
            public void onVideosLoaded(Flowable<List<Video>> flowable) {
                Disposable disposable = flowable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSubscriber<List<Video>>() {

                            @Override
                            public void onNext(List<Video> videoList) {
                                if (mView != null && mView.isActive()) {
                                    if (videoList.size() == 0) {
                                        mView.showNoVideos();
                                    } else {
                                        mView.showVideos(videoList);
                                    }
                                }
                            }

                            @Override
                            public void onError(Throwable t) {
                                LogUtils.e(TAG, "onError error : " + t.toString());

                                mView.setLoadingIndicator(false);
                                mView.showLoadingVideosError();
                            }

                            @Override
                            public void onComplete() {
                                LogUtils.i(TAG, "onComplete");

                                mView.setLoadingIndicator(false);
                            }
                        });

                mDisposables.add(disposable);
            }
        });
    }

    @Override
    public void registerLocalObserver() {
        mView.getContext().getContentResolver()
                .registerContentObserver(Constants.VIDEO_URI, false, mObserver);
    }

    @Override
    public void unregisterLocalObserver() {
        mView.getContext().getContentResolver().unregisterContentObserver(mObserver);
    }
}
