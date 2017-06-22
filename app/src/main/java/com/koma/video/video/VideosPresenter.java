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

import com.koma.video.data.VideosRepository;
import com.koma.video.data.model.Video;
import com.koma.video.util.KomaLogUtils;

import java.util.List;

import javax.inject.Inject;

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
public class VideosPresenter implements VideosConstract.Presenter {
    private static final String TAG = VideosPresenter.class.getSimpleName();

    private VideosConstract.View mView;

    private VideosRepository mRepository;

    private CompositeDisposable mDisposables;

    /**
     * Dagger strictly enforces that arguments not marked with {@code @Nullable} are not injected
     * with {@code @Nullable} values.
     */
    @Inject
    public VideosPresenter(VideosConstract.View view, VideosRepository repository) {
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
        loadVideos();
    }

    @Override
    public void unSubscribe() {
        if (mDisposables != null) {
            mDisposables.clear();
        }
    }

    @Override
    public void loadVideos() {
        mView.setLoadingIndicator(true);

        if (mDisposables != null) {
            mDisposables.clear();
        }

        Disposable disposable = mRepository.getVideos()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<List<Video>>() {

                    @Override
                    public void onNext(List<Video> videoList) {
                        onVideosLoaded(videoList);
                    }

                    @Override
                    public void onError(Throwable t) {
                        KomaLogUtils.e(TAG, "onError error : " + t.toString());

                        mView.setLoadingIndicator(false);
                        mView.showLoadingVideosError();
                    }

                    @Override
                    public void onComplete() {
                        KomaLogUtils.i(TAG, "onComplete");

                        mView.setLoadingIndicator(false);
                    }
                });

        mDisposables.add(disposable);
    }

    @Override
    public void onVideosLoaded(List<Video> videoList) {
        if (mView != null && mView.isActive()) {
            if (videoList.size() == 0) {
                mView.showNoVideos();
            } else {
                mView.showVideos(videoList);
            }
        }
    }
}