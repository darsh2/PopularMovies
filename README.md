# PopularMovies
Popular Movies Stage - 2

App developed as part of coursework for [Udacity Android Nanodegree](https://www.udacity.com/course/android-developer-nanodegree--nd801) , Popular Movies Stage 2.
Current features:
* Popular, top rated and favorites (locally saved on device) movie tabs
* Endless scroll grid of movies
* Support for orientation change
* Tablet UI
* Save any movie locally as favorite, modify this list
* Movie detail additionally include list of genres, tagline, similar movies (apart from requirements)
* About movie fragment that displays backdrop images, tagline, movie overview, cast and director
* Reviews fragment listing reviews for particular movie
* Share movie trailer or it's tmdb webpage
* List of movies by genre
* List of movies similar to a particular movie

##Screencasts
Stage - 2 (mobile) screencast can be found [here](https://youtu.be/Fr26BafjkNw).

Stage - 2 (tablet) screencast can be found [here](https://youtu.be/Txlp9UkMLhk).

Stage - 1 screencast can be found [here](https://youtu.be/-sCmRRbo8Y8).

##Project Review
Stage 2 - "You have done a superb job implementing Stage 2 of Popular Movies! I was super impressed by what you have built! As a user myself, I had a great time using your app! Your hard work has paid off! Keep up the good work as you continue your Nanodegree journey! Safe journey and bon voyage! :smile:"

Stage 1 - "In my perspective, you have done a perfect job implementing popular movie stage 1. The format of your codes is extremely standard. Also, your codes can be easily understood. Great job for your popular movie stage 1! Hope to see your stage 2 codes soon! :)"

## How to use
Either clone this repository using git or download as zip (and unzip). In app/build.gradle file, comment this line
```gradle
buildConfigField "String", "TMDB_API_KEY", "\"" + getApiKey() + "\""
```
Uncomment this line
```gradle
buildConfigField "String", "TMDB_API_KEY", "\"YOUR_TMDB_API_KEY\""
```
and replace YOUR_TMDB_API_KEY with your api key for TMDb.

## Attribution
This product uses the [TMDb API](https://www.themoviedb.org/documentation/api/) but is not endorsed or certified by TMDb.

## Libraries used
* [CircleIndicator](https://github.com/ongakuer/CircleIndicator) - for view pager indicator
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
