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

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.slim3.datastore.meta.HogeMeta;
import org.slim3.datastore.model.Hoge;

/**
 * @author higa
 * 
 */
public class AttributeComparatorTest {

    private HogeMeta meta = new HogeMeta();

    /**
     * @throws Exception
     */
    @Test
    public void constructorForVarargs() throws Exception {
        AttributeComparator comparator =
            new AttributeComparator(meta.myInteger.asc, meta.myString.desc);
        assertThat(comparator.sortCriteria.size(), is(2));
    }

    /**
     * @throws Exception
     */
    @Test
    public void compare() throws Exception {
        AttributeComparator comparator =
            new AttributeComparator(meta.myInteger.asc, meta.myString.desc);
        Hoge hoge = new Hoge();
        hoge.setMyInteger(1);
        hoge.setMyString("aaa");
        assertThat(comparator.compare(hoge, hoge), is(0));
        Hoge hoge2 = new Hoge();
        hoge2.setMyInteger(2);
        hoge2.setMyString("bbb");
        assertThat(comparator.compare(hoge, hoge2), is(-1));
        assertThat(comparator.compare(hoge2, hoge), is(1));
        hoge2.setMyInteger(1);
        assertThat(comparator.compare(hoge, hoge2), is(1));
    }
}