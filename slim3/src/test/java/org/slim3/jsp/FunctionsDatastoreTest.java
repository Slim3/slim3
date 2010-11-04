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
package org.slim3.jsp;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.slim3.tester.ServletTestCase;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

/**
 * @author higa
 * 
 */
public class FunctionsDatastoreTest extends ServletTestCase {

    /**
     * @throws Exception
     */
    @Test
    public void hForKey() throws Exception {
        Key key = KeyFactory.createKey("Hoge", 1);
        String encodedKey = KeyFactory.keyToString(key);
        assertThat(Functions.h(key), is(encodedKey));
    }

    /**
     * @throws Exception
     */
    @Test
    public void blobstoreUrl() throws Exception {
        String basePath = "/blobstore/";
        String url = "upload";
        tester.request.setServletPath(basePath);
        assertThat(Functions.blobstoreUrl(url), is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void blobstoreUrlforEmpty() throws Exception {
        String basePath = "/blobstore/";
        String url = "";
        tester.request.setServletPath(basePath);
        assertThat(Functions.blobstoreUrl(url), is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void blobstoreUrlforNull() throws Exception {
        String basePath = "/blobstore/";
        String url = null;
        tester.request.setServletPath(basePath);
        assertThat(Functions.blobstoreUrl(url), is(notNullValue()));
    }
}