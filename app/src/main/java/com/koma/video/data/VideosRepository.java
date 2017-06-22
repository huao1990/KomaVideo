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
package com.koma.video.data;

import com.koma.video.data.model.Video;
import com.koma.video.data.source.local.VideosLocalDataSource;
import com.koma.video.data.source.remote.VideosRemoteDataSource;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Flowable;

@Singleton
public class VideosRepository implements VideosDataSource {
    private VideosLocalDataSource mLocalDataSource;

    private VideosRemoteDataSource mRemoteDataSource;

    @Inject
    public VideosRepository(@Local VideosLocalDataSource localDataSource,
                            @Remote VideosRemoteDataSource remoteDataSource) {
        mLocalDataSource = localDataSource;

        mRemoteDataSource = remoteDataSource;
    }

    @Override
    public Flowable<List<Video>> getVideos() {
        return mLocalDataSource.getVideos();
    }
}
