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
public class AbstCriterionTest extends TestCase {

    /**
     * @throws Exception
     * 
     */
    public void testConstructor() throws Exception {
        HogeMeta meta = new HogeMeta();
        MyCriterion criterion = new MyCriterion(meta.myInteger);
        assertEquals(meta.myInteger, criterion.attributeMeta);
    }

    /**
     * @throws Exception
     * 
     */
    public void testCompareValue() throws Exception {
        HogeMeta meta = new HogeMeta();
        MyCriterion criterion = new MyCriterion(meta.myInteger);
        assertEquals(-1, criterion.compareValue(1, 2));
        assertEquals(0, criterion.compareValue(1, 1));
        assertEquals(1, criterion.compareValue(2, 1));
    }

    private static class MyCriterion extends AbstractCriterion {

        /**
         * @param attributeMeta
         * @throws NullPointerException
         */
        public MyCriterion(AbstractAttributeMeta<Hoge, Integer> attributeMeta)
                throws NullPointerException {
            super(attributeMeta);
        }

    }
}