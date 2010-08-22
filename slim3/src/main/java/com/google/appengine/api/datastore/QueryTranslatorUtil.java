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
package com.google.appengine.api.datastore;

/**
 * A utility for QueryTranslator.
 * 
 * @author higa
 * @since 1.0.6
 * 
 */
public final class QueryTranslatorUtil {

    /**
     * Converts the query to a protocol buffer query.
     * 
     * @param query
     *            the query
     * @param fetchOptions
     *            the fetch options
     * @return a protocol buffer query
     */
    public static com.google.apphosting.api.DatastorePb.Query convertToPb(
            Query query, FetchOptions fetchOptions) {
        return QueryTranslator.convertToPb(query, fetchOptions);
    }

    private QueryTranslatorUtil() {
    }
}