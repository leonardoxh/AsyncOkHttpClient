For 1.2:
---
- Cookies persistence support
- Authentication
- Proxy
- Cancel requests (Wooooha)
- Request cache using OkResponseCache from OkHttp
- Bump OkHttp dependency to latest version
- Unit tests using Mockito, Robolectric and MockWebServer (not sure on 1.2 or 1.3)

1.1:
---
- Removed methods that support Content-Type directly, since the Content-Type is a normal header and it can be set by <code>AsyncOkHttpClient.addHeader(java.lang.String, java.lang.String)</code>
is the case of get, post, put and delete overloads.
- Added a JSON fail callbacks with a json response, since a valid json can become on request with a status code >= 303,
all this json is parsed outside UIThread and received on UIThread.
- Added a ByteAsyncHttpResponse, its very power full, can download imagens or bytes from a server very very fast.
- Bug fixes
