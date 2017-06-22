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
package com.koma.video.data.model;

import android.text.TextUtils;

import java.util.Arrays;

/**
 * Created by koma on 5/27/17.
 */

public final class Video {
    private long mId;
    private String mPath, mTitle;

    public long getId() {
        return this.mId;
    }

    public void setId(long id) {
        this.mId = id;
    }

    public String getPath() {
        return this.mPath;
    }

    public void setPath(String path) {
        this.mPath = path;
    }

    public String getTitle() {
        return this.mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Video video = (Video) o;

        return mId == video.mId && TextUtils.equals(mTitle, video.mTitle) && TextUtils.equals(mPath, video.mPath);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(new Object[]{mId, mTitle, mPath});
    }

    @Override
    public String toString() {
        return "Video with title " + mTitle + ",path " + mPath;
    }
}
