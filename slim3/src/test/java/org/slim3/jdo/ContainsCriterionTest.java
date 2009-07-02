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

import java.util.Arrays;

import junit.framework.TestCase;

/**
 * @author higa
 * 
 */
public class ContainsCriterionTest extends TestCase {

    private SampleMeta m = new SampleMeta();

    /**
     * @throws Exception
     */
    public void testGetQueryString() throws Exception {
        ContainsCriterion criterion = m.aaaArray.contains(1);
        assertEquals("aaaArray.contains(:0)", criterion.getQueryString(":0"));
    }

    /**
     * @throws Exception
     */
    public void testAccept() throws Exception {
        Sample sample = new Sample();
        sample.setAaaArray(new Long[] { 1L });
        ContainsCriterion criterion = m.aaaArray.contains(1);
        assertTrue(criterion.accept(sample));
        sample.setAaaArray(new Long[] { 2L });
        assertFalse(criterion.accept(sample));
    }

    /**
     * @throws Exception
     */
    public void testAcceptForPrimitiveArray() throws Exception {
        Sample sample = new Sample();
        sample.setIntArray(new int[] { 1 });
        ContainsCriterion criterion = m.intArray.contains(1);
        assertTrue(criterion.accept(sample));
        sample.setIntArray(new int[] { 2 });
        assertFalse(criterion.accept(sample));
    }

    /**
     * @throws Exception
     */
    public void testAcceptForList() throws Exception {
        Sample sample = new Sample();
        sample.setAaaList(Arrays.asList(1L));
        ContainsCriterion criterion = m.aaaList.contains(1);
        assertTrue(criterion.accept(sample));
        sample.setAaaList(Arrays.asList(2L));
        assertFalse(criterion.accept(sample));
    }
}
