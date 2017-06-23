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
package com.koma.video.play;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;

import com.koma.video.KomaVideoApplication;
import com.koma.video.R;
import com.koma.video.base.BaseActivity;
import com.koma.video.util.ActivityUtils;
import com.koma.video.util.LogUtils;

import javax.inject.Inject;

/**
 * Created by koma on 6/22/17.
 */

public class PlayerActivity extends BaseActivity {
    private static final String TAG = PlayerActivity.class.getSimpleName();

    @Inject
    PlayerPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LogUtils.i(TAG, "onCreate");
    }

    @Override
    public void init() {
        PlayerFragment playerFragment = (PlayerFragment) getSupportFragmentManager()
                .findFragmentById(R.id.content_main);

        if (playerFragment == null) {
            playerFragment = PlayerFragment.newInstance();

            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), playerFragment,
                    R.id.content_main);
        }

        DaggerPlayerComponent.builder().videosRepositoryComponent(
                ((KomaVideoApplication) getApplication()).getVideosRepositoryComponent())
                .playerPresenterModule(new PlayerPresenterModule(playerFragment))
                .build()
                .inject(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        LogUtils.i(TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();

        LogUtils.i(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();

        LogUtils.i(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();

        LogUtils.i(TAG, "onStop");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        LogUtils.i(TAG, "onConfigurationChanged newConfig : " + newConfig.toString());
    }

    @Override
    public void onMultiWindowModeChanged(boolean isInMultiWindowMode) {
        super.onMultiWindowModeChanged(isInMultiWindowMode);

        LogUtils.i(TAG, "onMultiWindowModeChanged: " + isInMultiWindowMode);
    }

    protected boolean isInMultiWindow() {
        return Build.VERSION.SDK_INT == Build.VERSION_CODES.N && isInMultiWindowMode();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_player;
    }
}
