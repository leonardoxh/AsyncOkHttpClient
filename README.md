AsyncOkHttpClient Http Client for Android
=================

This is a asynchronous library based on James Smith Async Http Library.
https://github.com/loopj/android-async-http

Instead use Apache http librarys this library use the Square Inc OkHttpClient with HttpURLConnection implementation
https://github.com/square/okhttp

Instead of annotation magic for a Android rest client, I build this just override the callbacks and ok, the requests happen out of ui thread but the callbacks are executed in the ui thread as well.
Basically the codes is from Async Http Library adapted for use with OkHttp, and yes my inglish is very very good.

Features
--------
- Make **asynchronous** HTTP requests, handle responses in **anonymous callbacks**
- HTTP requests happen **outside the UI thread**
- Requests use a **threadpool** to cap concurrent resource usage
- GET/POST **params builder** (RequestParams)

Note
-------- 
- **This library is beta yet and no have all the resources, such as unity tests, file upload, json response, documentation and Maven integration**
- **This library needs the OkHttpClient jar in the libs folder.**


=================
   Copyright [2013] [Leonardo Rossetto]

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
