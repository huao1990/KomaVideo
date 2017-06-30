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
package com.koma.video;

import android.app.Application;
import android.os.StrictMode;

import com.bumptech.glide.Glide;
import com.koma.video.data.DaggerVideosRepositoryComponent;
import com.koma.video.data.VideosRepositoryComponent;
import com.koma.video.util.LogUtils;
import com.squareup.leakcanary.LeakCanary;

public class KomaVideoApplication extends Application {
    private static final String TAG = KomaVideoApplication.class.getSimpleName();

    private VideosRepositoryComponent mRepositoryComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        LogUtils.i(TAG, "onCreate");

        enableStrictMode();

        mRepositoryComponent = DaggerVideosRepositoryComponent.builder()
                .applicationModule(new ApplicationModule(getApplicationContext()))
                .build();

        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
    }

    public VideosRepositoryComponent getVideosRepositoryComponent() {
        return mRepositoryComponent;
    }


    @Override
    public void onLowMemory() {
        LogUtils.e(TAG, "onLowMemory");

        //clear cache
        Glide.get(getApplicationContext()).clearMemory();

        super.onLowMemory();
    }

    private void enableStrictMode() {
        final StrictMode.ThreadPolicy.Builder threadPolicyBuilder = new StrictMode.ThreadPolicy.Builder()
                .detectAll().penaltyLog();
        final StrictMode.VmPolicy.Builder vmPolicyBuilder = new StrictMode.VmPolicy.Builder()
                .detectAll().penaltyLog();

        threadPolicyBuilder.penaltyFlashScreen();
        StrictMode.setThreadPolicy(threadPolicyBuilder.build());
        StrictMode.setVmPolicy(vmPolicyBuilder.build());
    }
}
