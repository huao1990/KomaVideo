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
package com.koma.video.data.source.local;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.koma.video.data.VideosDataSource;
import com.koma.video.data.model.Video;
import com.koma.video.util.Constants;
import com.koma.video.util.LogUtils;
import com.koma.video.util.Utils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;

@Singleton
public class VideosLocalDataSource implements VideosDataSource {
    private static final String TAG = VideosLocalDataSource.class.getSimpleName();

    private static final String VIDEOS_SELECTION = MediaStore.Video.Media.TITLE + " != ''";
    private static final String VIDEOS_SORT_ORDER = MediaStore.Video.Media.DATE_ADDED + " DESC";

    private static final String[] VIDEOS_PROJECTION = new String[]{
            MediaStore.Video.Media._ID, MediaStore.Video.Media.DATA,
            MediaStore.Video.Media.TITLE, MediaStore.Video.Media.DATE_ADDED,
    };

    private Context mContext;

    @Inject
    public VideosLocalDataSource(@NonNull Context context) {
        mContext = context;
    }

    @Override
    public void loadVideos(@NonNull final LoadVideosCallback callback, final String folderpath) {
        LogUtils.i(TAG, "loadLocalVideos");

        final Flowable<List<Video>> flowable = Flowable.create(new FlowableOnSubscribe<List<Video>>() {
            @Override
            public void subscribe(@NonNull FlowableEmitter<List<Video>> e) throws Exception {
                e.onNext(listVideos(folderpath));
                e.onComplete();
            }
        }, BackpressureStrategy.LATEST);

        callback.onVideosLoaded(flowable);
    }

   /* private List<Video> listVideos() {
        ContentResolver resolver = mContext.getContentResolver();

        List<Video> videoList = new ArrayList<>();

        Cursor cursor = resolver.query(Constants.VIDEO_URI, VIDEOS_PROJECTION, VIDEOS_SELECTION, null,
                VIDEOS_SORT_ORDER);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                Video video = new Video();
                video.setId(cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media._ID)));
                video.setPath(cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA)));
                video.setFolderPath(Utils.getFolderPath(video.getPath()));
                video.setTitle(cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE)));
                videoList.add(video);
            } while (cursor.moveToNext());

            cursor.close();
            cursor = null;
        }
        return videoList;
    }*/

    private List<Video> listVideos(String folderPath) {
        boolean hasSelection = !TextUtils.isEmpty(folderPath);
        StringBuilder selection = new StringBuilder(VIDEOS_SELECTION);
        ContentResolver resolver = mContext.getContentResolver();

        List<Video> videoList = new ArrayList<>();
        if (hasSelection) {
            selection.append("AND (" + MediaStore.Video.Media.DATA + " LIKE '%" + folderPath + "')");
        }


        Cursor cursor = resolver.query(Constants.VIDEO_URI, VIDEOS_PROJECTION, selection.toString(), null,
                VIDEOS_SORT_ORDER);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                Video video = new Video();
                video.setId(cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media._ID)));

                video.setTitle(cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE)));

                video.setPath(cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA)));

                if (hasSelection) {
                    if (folderPath.equals(Utils.getFolderPath(video.getPath()))) {
                        video.setFolderPath(folderPath);
                    } else {
                        continue;
                    }
                } else {
                    video.setFolderPath(Utils.getFolderPath(video.getPath()));
                }

                videoList.add(video);
            } while (cursor.moveToNext());

            cursor.close();
            cursor = null;
        }
        return videoList;
    }
}
