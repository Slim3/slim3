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

import com.google.appengine.api.datastore.Query.SortDirection;

/**
 * @author higa
 * 
 */
public class CoreAttributeMetaTest extends TestCase {

    private HogeMeta meta = new HogeMeta();

    /**
     * @throws Exception
     * 
     */
    public void testEqual() throws Exception {
        assertEquals(EqualCriterion.class, meta.myString.equal("a").getClass());
        assertNotNull(meta.myString.equal(null));
        EqualCriterion c =
            (EqualCriterion) meta.myEnum.equal(SortDirection.ASCENDING);
        assertEquals("ASCENDING", c.value);
    }

    /**
     * @throws Exception
     * 
     */
    public void testLessThan() throws Exception {
        assertEquals(LessThanCriterion.class, meta.myString
            .lessThan("a")
            .getClass());
        assertNotNull(meta.myString.lessThan(null));
        LessThanCriterion c =
            (LessThanCriterion) meta.myEnum.lessThan(SortDirection.ASCENDING);
        assertEquals("ASCENDING", c.value);
    }

    /**
     * @throws Exception
     * 
     */
    public void testLessThanOrEqual() throws Exception {
        assertEquals(LessThanOrEqualCriterion.class, meta.myString
            .lessThanOrEqual("a")
            .getClass());
        assertNotNull(meta.myString.lessThanOrEqual(null));
        LessThanOrEqualCriterion c =
            (LessThanOrEqualCriterion) meta.myEnum
                .lessThanOrEqual(SortDirection.ASCENDING);
        assertEquals("ASCENDING", c.value);
    }

    /**
     * @throws Exception
     * 
     */
    public void testGreaterThan() throws Exception {
        assertEquals(GreaterThanCriterion.class, meta.myString
            .greaterThan("a")
            .getClass());
        assertNotNull(meta.myString.greaterThan(null));
        GreaterThanCriterion c =
            (GreaterThanCriterion) meta.myEnum
                .greaterThan(SortDirection.ASCENDING);
        assertEquals("ASCENDING", c.value);
    }

    /**
     * @throws Exception
     * 
     */
    public void testGreaterThanOrEqual() throws Exception {
        assertEquals(GreaterThanOrEqualCriterion.class, meta.myString
            .greaterThanOrEqual("a")
            .getClass());
        assertNotNull(meta.myString.greaterThanOrEqual(null));
        GreaterThanOrEqualCriterion c =
            (GreaterThanOrEqualCriterion) meta.myEnum
                .greaterThanOrEqual(SortDirection.ASCENDING);
        assertEquals("ASCENDING", c.value);
    }

    /**
     * @throws Exception
     * 
     */
    public void testIsNotNull() throws Exception {
        assertEquals(IsNotNullCriterion.class, meta.myString
            .isNotNull()
            .getClass());
    }
}