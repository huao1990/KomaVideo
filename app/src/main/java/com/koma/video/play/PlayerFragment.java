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

import android.support.annotation.NonNull;

import com.koma.video.R;
import com.koma.video.base.BaseFragment;
import com.koma.video.util.LogUtils;

/**
 * Created by koma on 6/23/17.
 */

public class PlayerFragment extends BaseFragment implements PlayerContract.View {
    private static final String TAG = PlayerFragment.class.getSimpleName();

    @NonNull
    private PlayerContract.Presenter mPresenter;

    public PlayerFragment() {
    }

    public static PlayerFragment newInstance() {
        return new PlayerFragment();
    }

    @Override
    public void onStart() {
        super.onStart();

        LogUtils.i(TAG, "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();

        LogUtils.i(TAG, "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();

        LogUtils.i(TAG, "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();

        LogUtils.i(TAG, "onStop");
    }

    @Override
    public int getLayoutId() {
        return R.layout.player_fragment_layout;
    }

    @Override
    public void setPresenter(PlayerContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public boolean isActive() {
        return this.isAdded();
    }
}
