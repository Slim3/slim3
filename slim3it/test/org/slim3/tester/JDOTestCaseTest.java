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
package org.slim3.tester;

import slim3.it.meta.BlogMeta;
import slim3.it.model.Blog;

/**
 * @author higa
 * 
 */
public class JDOTestCaseTest extends JDOTestCase {

    /**
     * @throws Exception
     */
    public void testFrom() throws Exception {
        assertNotNull(from(new BlogMeta()));
        assertNotNull(from(Blog.class));
    }

    /**
     * @throws Exception
     */
    public void testMakePersistentInTx() throws Exception {
        makePersistentInTx(new Blog());
        assertEquals(1, count(Blog.class));
    }

    /**
     * @throws Exception
     */
    public void testDeletePersistentInTx() throws Exception {
        Blog blog = new Blog();
        makePersistentInTx(blog);
        deletePersistentInTx(blog);
        assertEquals(0, count(Blog.class));
    }
}