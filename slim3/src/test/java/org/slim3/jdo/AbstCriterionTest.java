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
public class AbstCriterionTest extends TestCase {

    private SampleMeta m = new SampleMeta();

    /**
     * @throws Exception
     */
    public void testConstructor() throws Exception {
        MyCriterion criterion = new MyCriterion(m.id);
        assertNotNull(criterion.attributeMeta);
    }

    /**
     * @throws Exception
     */
    public void testCompareValue() throws Exception {
        MyCriterion criterion = new MyCriterion(m.id);
        assertTrue(criterion.compareValue(1L, 2L) < 0);
        assertEquals(0, criterion.compareValue(1L, 1L));
        assertTrue(criterion.compareValue(2L, 1L) > 0);
    }

    /**
     * @throws Exception
     */
    public void testCompareValueForNotComparable() throws Exception {
        MyCriterion criterion = new MyCriterion(m.id);
        try {
            criterion.compareValue(this, 2L);
            fail();
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }

    private static class MyCriterion extends AbstractCriterion {

        /**
         * @param attributeMeta
         */
        public MyCriterion(AttributeMeta attributeMeta) {
            super(attributeMeta);
        }
    }
}
