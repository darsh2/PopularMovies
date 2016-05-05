# PopularMovies
Popular Movies Stage - 1

App developed as part of coursework for [Udacity Android Nanodegree](https://www.udacity.com/course/android-developer-nanodegree--nd801) , Popular Movies Stage 1.
Current features:
* Popular and top rated movie tabs
* Endless scroll grid of movies
* Movie detail additionally include list of genres (apart from requirements)
* Support for orientation change

Screencast can be found [here](https://youtu.be/Pym0W35S7Gc).

## How to use
Either clone this repository using git or download as zip (and unzip). In app/build.gradle file,
```gradle
buildConfigField "String", "TMDB_API_KEY", "\"YOUR_TMDB_API_KEY\""
```
replace YOUR_TMDB_API_KEY with your api key for TMDb.

## Attribution
This product uses the [TMDb API](https://www.themoviedb.org/documentation/api/) but is not endorsed or certified by TMDb.

## Libraries used
* [Glide](https://github.com/bumptech/glide) - for image loading/caching
* [Retrofit](https://github.com/square/retrofit) - for accessing TMDb api
* [XRecyclerView](https://github.com/jianghejie/XRecyclerView) - modified [here](https://github.com/darsh2/PopularMovies/tree/master/app/src/main/java/com/example/darsh/view) for suiting app needs to support endless scroll

## License
    Copyright 2016 Darshan Dorai.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
