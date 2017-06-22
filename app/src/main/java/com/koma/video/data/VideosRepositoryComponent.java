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

import com.koma.video.ApplicationModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by koma on 6/20/17.
 */

/**
 * This is a Dagger component. Refer to {@link com.koma.video.KomaVideoApplication} for the list of
 * Dagger components
 * used in this application.
 * <p>
 * Even though Dagger allows annotating a {@link Component @Component} as a singleton, the code
 * itself must ensure only one instance of the class is created. This is done in {@link
 * com.koma.video.KomaVideoApplication}.
 */
@Singleton
@Component(modules = {VideosRepositoryModule.class, ApplicationModule.class})
public interface VideosRepositoryComponent {
    VideosRepository getVideoRepository();
}
