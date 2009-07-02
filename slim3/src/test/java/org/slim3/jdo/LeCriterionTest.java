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
public class LeCriterionTest extends TestCase {

    private SampleMeta m = new SampleMeta();

    /**
     * @throws Exception
     */
    public void testGetQueryString() throws Exception {
        FilterCriterion criterion = m.id.le(1);
        assertEquals("id <= :0", criterion.getQueryString(":0"));
    }

    /**
     * @throws Exception
     */
    public void testAccept() throws Exception {
        Sample sample = new Sample();
        sample.setId(2L);
        FilterCriterion criterion = m.id.le(1);
        assertFalse(criterion.accept(sample));
        sample.setId(1L);
        assertTrue(criterion.accept(sample));
        sample.setId(0L);
        assertTrue(criterion.accept(sample));
    }
}
