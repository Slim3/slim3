/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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
package org.slim3.gae.jdo;

import junit.framework.TestCase;

/**
 * @author higa
 * 
 */
public class JDOTemplate2Test extends TestCase {

    private MyTemplate template = new MyTemplate();

    /**
     * @throws Exception
     */
    public void testToBoolean() throws Exception {
        assertEquals(Boolean.TRUE, template.toBoolean("true"));
    }

    /**
     * @throws Exception
     */
    public void testToByte() throws Exception {
        assertEquals(Byte.valueOf("1"), template.toByte("1"));
    }

    /**
     * @throws Exception
     */
    public void testToShort() throws Exception {
        assertEquals(Short.valueOf("1"), template.toShort("1"));
    }

    /**
     * @throws Exception
     */
    public void testToInteger() throws Exception {
        assertEquals(Integer.valueOf(1), template.toInteger("1"));
    }

    /**
     * @throws Exception
     */
    public void testToLong() throws Exception {
        assertEquals(Long.valueOf(1), template.toLong("1"));
    }

    /**
     * @throws Exception
     */
    public void testFloat() throws Exception {
        assertEquals(Float.valueOf(1), template.toFloat("1"));
    }

    /**
     * @throws Exception
     */
    public void testDouble() throws Exception {
        assertEquals(Double.valueOf(1), template.toDouble("1"));
    }

    /**
     * @throws Exception
     */
    public void testDate() throws Exception {
        assertEquals(new java.util.Date(1), template
                .toDate(new java.sql.Date(1)));
    }

    /**
     * @throws Exception
     */
    public void testDateForString() throws Exception {
        assertEquals(java.util.Date.class, template.toDate(
                "01/01/1970 00:00:00", "MM/dd/yyyy").getClass());
    }

    /**
     * @throws Exception
     */
    public void testKey() throws Exception {
        SampleMeta s = new SampleMeta();
        assertNotNull(template.key(Sample.class, 1));
        assertNotNull(template.key(s, 1));
        assertEquals(template.key(Sample.class, 1), template.key(s, 1));
    }

    private static class MyTemplate extends JDOTemplate<Void> {

        @Override
        protected Void doExecute() {
            return null;
        }
    }
}