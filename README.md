# SMART MOVIE


**This is part 1 and part 2 of the Popular Movies App**

## Features
- Discover movies by popular, top rated and favorite
- Optimized UI Scrolling
- Read reviews from other users
- Mark movies as favorites
- Save your favorite movies and view them offline
- Watch Share movie trailer with friends

## ScreenShots
![alt text](https://github.com/otichibueze/smartcinema/blob/master/screenshots/d.png)
![alt text](https://github.com/otichibueze/smartcinema/blob/master/screenshots/c.png)
![alt text](https://github.com/otichibueze/smartcinema/blob/master/screenshots/a.png)
![alt text](https://github.com/otichibueze/smartcinema/blob/master/screenshots/b.png)




## API Key
The app requires API key from themoviedb.org so as to perform requests and fetch data. 
You can obtain an API key for free by signing up for an TMDb account at www.themoviedb.org

#### [Learn how to hide Api Key](https://github.com/otichibueze/smartcinema/blob/master/HIDE_API_KEY.MD)

```
Put API key into ~/.gradle/gradle.properties(global Properties)
MyMovieDbApiKey="your_apikey_from_themoviedb.org"
```

## Data Persistence
- App persists favorite movie details using a database
- App displays favorite movie details (texts, images and user reviews) even when offline
- App uses a ContentProvider to populate favorite movie details

## LIBRARIES:
- [Picasso](https://github.com/square/picasso)
- [ButterKnive](https://github.com/JakeWharton/butterknife)
- [Gson](https://github.com/google/gson)


## Connect With Me On
[![N|Solid](https://github.com/otichibueze/smartcinema/blob/master/screenshots/linkedin.png)](https://www.linkedin.com/in/chibuezeoti)

## License
```
Copyright 2018 Chibusoft, Inc.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
