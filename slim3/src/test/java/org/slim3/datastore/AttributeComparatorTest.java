/*
 * Copyright 2004-2009 the original author or authors.
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

import junit.framework.TestCase;

import org.slim3.datastore.meta.HogeMeta;
import org.slim3.datastore.model.Hoge;

/**
 * @author higa
 * 
 */
public class AttributeComparatorTest extends TestCase {

    private HogeMeta meta = new HogeMeta();

    /**
     * @throws Exception
     */
    public void testCompare() throws Exception {
        AttributeComparator comparator =
            new AttributeComparator(meta.myInteger.asc, meta.myString.desc);
        Hoge hoge = new Hoge();
        hoge.setMyInteger(1);
        hoge.setMyString("aaa");
        assertEquals(0, comparator.compare(hoge, hoge));
        Hoge hoge2 = new Hoge();
        hoge2.setMyInteger(2);
        hoge2.setMyString("bbb");
        assertEquals(-1, comparator.compare(hoge, hoge2));
        assertEquals(1, comparator.compare(hoge2, hoge));
        hoge2.setMyInteger(1);
        assertEquals(1, comparator.compare(hoge, hoge2));
    }
}