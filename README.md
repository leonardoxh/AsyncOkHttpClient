AsyncOkHttpClient Http Client for Android
=================

This is a asynchronous library based on James Smith [Android Async Http Library](https://github.com/loopj/android-async-http).

Instead use Apache http librarys this library use the Square Inc. [OkHttpClient](https://github.com/square/okhttp), so its mutch more fast and can be used on Android 2.2 and above.

Important Note:
--------
Now the OkHttpClient have their own async API, which means this library will no be longer maintained.
I think the Square OkHttpClient async API is more effetive in allways, and have a better support for Async calls and features, no other layer on top of it is required, so...
I will release on next weeks a final version of this library, but I strongly recommend use the OkHttp async layer instead of this.
--------

Features
--------
- Make **asynchronous** HTTP requests, handle responses in **anonymous callbacks**
- HTTP requests happen **outside the UI thread**
- Requests use a **threadpool** to cap concurrent resource usage
- GET/POST/PUT **params builder** (RequestParams)

Example:
--------
```
AsyncOkHttpClient client = new AsyncOkHttpClient();
client.get("http://www.google.com", new AsyncHttpResponse() {
    
  @Override
  public void onStart() {
    System.out.println("onStart is called on UIThread");
  }

  @Override
  public void onFinish() {
    System.out.println("onFinish");
  }

  @Override
  public void onSuccess(int statusCode, String content) {
    System.out.println("CONTENT: " + content);
  }

  @Override
  public void onError(Throwable error, String content) {
    //Error called check the Throwable instance
    if(error instanceof HttpRetryException) {
      //Get the error code
      HttpRetryException detailError = (HttpRetryException) error;
      System.out.println("Response Code: " + detailError.responseCode());
    } else {
      //Another error cause like permission not declared on AndroidManifest or time out exception
    }
  }
  
});
```


Gradle:
=================
```
Latest stable version is: 1.1 and snapshot is 1.1-SNAPSHOT
```

```
dependencies {
    compile 'com.github.leonardoxh:AsyncOkHttpClient:1.1@aar'
}
```

Changelog:
=================
```
Read the Changelog.md
```

Contributing:
=================
```
All pull request are wellcome, but first think in a real use case for the 
pull request, this library **will not** support lots of features.
Before submit any change please read the CONTRIBUTING.md
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
