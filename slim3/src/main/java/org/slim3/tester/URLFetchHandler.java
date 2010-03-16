/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.slim3.tester;

import java.io.IOException;

import com.google.appengine.api.urlfetch.URLFetchServicePb.URLFetchRequest;

/**
 * A handler interface to fetch URL.
 * 
 * @author higa
 * @since 1.0.0
 * 
 */
public interface URLFetchHandler {

    /**
     * Returns the content as an array of bytes.
     * 
     * @param request
     *            the request
     * @return the content as an array of bytes
     * @throws IOException
     *             if {@link IOException} occurred while invoking this method
     */
    byte[] getContent(URLFetchRequest request) throws IOException;

    /**
     * Returns status code.
     * 
     * @param request
     *            the request
     * @return status code
     * @throws IOException
     *             if {@link IOException} occurred while invoking this method
     */
    int getStatusCode(URLFetchRequest request) throws IOException;
}