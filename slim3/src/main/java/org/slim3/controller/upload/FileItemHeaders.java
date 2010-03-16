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
package org.slim3.controller.upload;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.slim3.util.ArrayMap;

/**
 * This class provides support for accessing the headers for a file or form item
 * that was received within a <code>multipart/form-data</code> POST request.
 * 
 * @author higa
 * @since 1.0.0
 * 
 */
public class FileItemHeaders {

    /**
     * The map for the header.
     */
    protected ArrayMap<String, List<String>> headerMap =
        new ArrayMap<String, List<String>>();

    /**
     * Returns the header value.
     * 
     * @param name
     *            the header name
     * @return the header value
     */
    public String getHeader(String name) {
        List<String> values = getHeaderList(name);
        if (values != null) {
            return values.get(0);
        }
        return null;
    }

    /**
     * Returns the header values.
     * 
     * @param name
     *            the header name
     * @return the header values
     */
    @SuppressWarnings("unchecked")
    public Iterator<String> getHeaders(String name) {
        List<String> values = getHeaderList(name);
        if (values != null) {
            return values.iterator();
        }
        return Collections.EMPTY_LIST.iterator();
    }

    /**
     * Returns the header names.
     * 
     * @return the header names
     */
    public Iterator<String> getHeaderNames() {
        return headerMap.keySet().iterator();
    }

    /**
     * Adds the header.
     * 
     * @param name
     *            the name
     * @param value
     *            the value
     */
    public void addHeader(String name, String value) {
        List<String> values = getHeaderList(name);
        if (values == null) {
            values = new ArrayList<String>();
        }
        values.add(value);
        headerMap.put(name.toLowerCase(), values);
    }

    /**
     * Return the list for header.
     * 
     * @param name
     *            the name
     * @return the list for header
     */
    protected List<String> getHeaderList(String name) {
        return headerMap.get(name.toLowerCase());
    }
}
