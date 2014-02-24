AsyncOkHttpClient Http Client for Android
=================

This is a asynchronous library based on James Smith [Android Async Http Library](https://github.com/loopj/android-async-http).

Instead use Apache http librarys this library use the Square Inc. [OkHttpClient](https://github.com/square/okhttp), so its mutch more fast and can be used on any android version.

Features
--------
- Make **asynchronous** HTTP requests, handle responses in **anonymous callbacks**
- HTTP requests happen **outside the UI thread**
- Requests use a **threadpool** to cap concurrent resource usage
- GET/POST **params builder** (RequestParams)

Maven:
=================
```
<dependency>
  <groupId>com.github.leonardoxh</groupId>
  <artifactId>AsyncOkHttpClient</artifactId>
  <version>1.0</version>
  <type>aar</type>
</dependency>
```


Licence:
=================
```
Copyright 2013-2014 Leonardo Rossetto

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
