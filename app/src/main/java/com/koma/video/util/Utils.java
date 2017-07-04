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
package com.koma.video.util;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.ArrayMap;

import com.koma.video.data.model.Video;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static Uri getVideoUri(long id) {
        return ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id);
    }

    public static List<List<Video>> getFolders(List<Video> videos) {
        ArrayMap<String, List<Video>> folders = new ArrayMap<>();
        for (Video video : videos) {
            if (folders.containsKey(video.getFolderPath())) {
                folders.get(video.getFolderPath()).add(video);
            } else {
                List<Video> videoList = new ArrayList<>();
                videoList.add(video);
                folders.put(video.getFolderPath(), videoList);
            }
        }

        List<List<Video>> lists = new ArrayList<>();
        for (int i = 0; i < folders.size(); i++) {
            lists.add(folders.valueAt(i));
        }

        return lists;
    }

    public static String getFolderPath(String path) {
        return path.substring(0, path.lastIndexOf(File.separator));
    }
}
