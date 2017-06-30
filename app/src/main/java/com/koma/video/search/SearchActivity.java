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
package com.koma.video.search;

import android.os.Bundle;

import com.koma.video.R;
import com.koma.video.base.BaseActivity;
import com.koma.video.util.ActivityUtils;
import com.koma.video.util.LogUtils;
import com.koma.video.video.VideosFragment;

/**
 * Created by koma on 6/30/17.
 */

public class SearchActivity extends BaseActivity {
    private static final String TAG = SearchActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LogUtils.i(TAG, "onCreate");

        init();
    }

    private void init() {
        VideosFragment videosFragment = (VideosFragment) getSupportFragmentManager()
                .findFragmentById(R.id.content_main);

        if (videosFragment == null) {
            videosFragment = VideosFragment.newInstance();

            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), videosFragment,
                    R.id.content_main);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_search;
    }
}
