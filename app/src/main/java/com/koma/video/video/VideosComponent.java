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

import com.koma.video.MainActivity;
import com.koma.video.data.VideosRepositoryComponent;
import com.koma.video.util.FragmentScoped;

import dagger.Component;

/**
 * This is a Dagger component. Refer to {@link com.koma.video.KomaVideoApplication} for the list of Dagger components
 * used in this application.
 * <p>
 * Because this component depends on the {@link VideosRepositoryComponent}, which is a singleton, a
 * scope must be specified. All fragment components use a custom scope for this purpose.
 */
@FragmentScoped
@Component(dependencies = VideosRepositoryComponent.class, modules = {VideosPresenterModule.class})
public interface VideosComponent {
    void inject(MainActivity activity);
}
