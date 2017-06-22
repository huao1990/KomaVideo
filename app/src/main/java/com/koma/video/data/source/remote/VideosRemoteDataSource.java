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
package com.koma.video.data.source.remote;

import com.koma.video.data.VideosDataSource;
import com.koma.video.data.model.Video;
import com.koma.video.util.KomaLogUtils;

import java.util.List;

import javax.inject.Singleton;

import io.reactivex.Flowable;

/**
 * Created by koma on 6/20/17.
 */
@Singleton
public class VideosRemoteDataSource implements VideosDataSource {
    private static final String TAG = VideosRemoteDataSource.class.getSimpleName();

    public VideosRemoteDataSource() {
    }

    @Override
    public Flowable<List<Video>> getVideos() {
        KomaLogUtils.i(TAG, "getLocalVideos");
        return null;
    }
}
