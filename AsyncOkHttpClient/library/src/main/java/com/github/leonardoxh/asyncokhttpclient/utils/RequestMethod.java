/*
 * Copyright 2013-2014 Leonardo Rossetto
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.opens.asyncokhttpclient.utils;

/**
 * Class that hold the supported request methods on this library
 * on the most use cases to be used with {@code com.opens.asyncokhttpclient.RequestModel}
 * @author Leonardo Rossetto <leonardoxh@gmail.com>
 * @see com.opens.asyncokhttpclient.RequestModel
 */
public final class RequestMethod {

    /** No instances */
    private RequestMethod() { }

    public static final String GET = "GET";

    public static final String POST = "POST";

    public static final String PUT = "PUT";

    public static final String DELETE = "DELETE";

}
