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

import com.koma.video.base.BasePresenter;
import com.koma.video.base.BaseView;
import com.koma.video.data.model.Video;

import java.util.List;

public interface VideosConstract {
    interface View extends BaseView<Presenter> {
        Context getContext();

        void setLoadingIndicator(boolean active);

        boolean isActive();

        void showVideos(List<Video> videoList);

        void showLoadingVideosError();

        void showNoVideos();
    }

    interface Presenter extends BasePresenter {
        void loadVideos();

        void registerLocalObserver();

        void unregisterLocalObserver();
    }
}
