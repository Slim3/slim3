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
package org.slim3.jdo;

import junit.framework.TestCase;

/**
 * @author higa
 * 
 */
public class AttributeComparatorTest extends TestCase {

    /**
     * @throws Exception
     */
    public void testCompare() throws Exception {
        SampleMeta m = new SampleMeta();
        AttributeComparator comparator =
            new AttributeComparator(m.id.asc(), m.name.desc());
        Sample sample = new Sample();
        sample.setId(1L);
        sample.setName("aaa");
        assertEquals(0, comparator.compare(sample, sample));
        Sample sample2 = new Sample();
        sample2.setId(2L);
        sample2.setName("bbb");
        assertEquals(-1, comparator.compare(sample, sample2));
        assertEquals(1, comparator.compare(sample2, sample));
        sample2.setId(1L);
        assertEquals(1, comparator.compare(sample, sample2));
    }
}