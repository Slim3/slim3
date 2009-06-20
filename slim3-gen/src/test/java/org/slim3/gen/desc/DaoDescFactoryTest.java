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
package org.slim3.gen.desc;

import junit.framework.TestCase;

/**
 * @author taedium
 * 
 */
public class DaoDescFactoryTest extends TestCase {

    /**
     * 
     * @throws Exception
     */
    public void testCreateDaoDesc() throws Exception {
        DaoDescFactory factory =
            new DaoDescFactory("aaa", "bbb.Ccc", "ddd.Eee", "fff.Ddd");
        DaoDesc daoDesc = factory.createDaoDesc();
        assertEquals("aaa", daoDesc.getPackageName());
        assertEquals("DddDao", daoDesc.getSimpleName());
        assertEquals("bbb.Ccc", daoDesc.getSuperclassName());
        assertEquals("ddd.Eee", daoDesc.getTestCaseSuperclassName());
        assertEquals("fff.Ddd", daoDesc.getModelClassName());
    }
}
