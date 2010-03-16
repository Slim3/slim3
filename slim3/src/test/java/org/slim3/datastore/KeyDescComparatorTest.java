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
package org.slim3.datastore;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.slim3.tester.AppEngineTestCase;

import com.google.appengine.api.datastore.KeyFactory;

/**
 * @author higa
 * 
 */
public class KeyDescComparatorTest extends AppEngineTestCase {

    /**
     * @throws Exception
     * 
     */
    @Test
    public void compare() throws Exception {
        assertThat(KeyDescComparator.INSTANCE.compare(KeyFactory.createKey(
            "Hoge",
            1), KeyFactory.createKey("Hoge", 2)), is(1));
        assertThat(KeyDescComparator.INSTANCE.compare(KeyFactory.createKey(
            "Hoge",
            2), KeyFactory.createKey("Hoge", 1)), is(-1));
        assertThat(KeyDescComparator.INSTANCE.compare(KeyFactory.createKey(
            "Hoge",
            1), KeyFactory.createKey("Hoge", 1)), is(0));
    }
}