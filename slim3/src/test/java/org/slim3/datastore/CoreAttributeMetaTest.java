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

/**
 * @author higa
 * 
 */
public class CoreAttributeMetaTest extends TestCase {

    /**
     * @throws Exception
     * 
     */
    public void testEqual() throws Exception {
        HogeMeta meta = new HogeMeta();
        assertEquals(EqualCriterion.class, meta.myString.equal("a").getClass());
        assertNull(meta.myString.equal(null));
    }
}