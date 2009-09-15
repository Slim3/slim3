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

import java.util.ArrayList;
import java.util.List;

import javax.jdo.JDOHelper;
import javax.jdo.JDOOptimisticVerificationException;
import javax.jdo.PersistenceManager;

import org.slim3.tester.JDOTestCase;

import slim3.it.dao.BlogDao;
import slim3.it.model.Blog;

/**
 * @author higa
 * 
 */
public class GenericDaoTest extends JDOTestCase {

    /**
     * @throws Exception
     */
    public void testConstructor() throws Exception {
        BlogDao dao = new BlogDao();
        assertEquals(PersistenceManagerProxy.class, dao.pm.getClass());
    }

    /**
     * @throws Exception
     */
    public void testReopenPersistenceManager() throws Exception {
        BlogDao dao = new BlogDao();
        PersistenceManager pm = CurrentPersistenceManager.get();
        dao.reopenPersistenceManager();
        assertNotSame(pm, CurrentPersistenceManager.get());
    }

    /**
     * @throws Exception
     */
    public void testGetObjectById() throws Exception {
        BlogDao dao = new BlogDao();
        Blog blog = new Blog();
        dao.makePersistentInTx(blog);
        assertNotNull(dao.getObjectById(blog.getKey()));
    }

    /**
     * @throws Exception
     */
    public void testGetObjectByIdAndVersion() throws Exception {
        BlogDao dao = new BlogDao();
        Blog blog = new Blog();
        dao.makePersistentInTx(blog);
        long version = (Long) JDOHelper.getVersion(blog);
        assertNotNull(dao.getObjectById(blog.getKey(), version));
    }

    /**
     * @throws Exception
     */
    public void testGetObjectByIdForOptimisticException() throws Exception {
        BlogDao dao = new BlogDao();
        Blog blog = new Blog();
        dao.makePersistentInTx(blog);
        long version = (Long) JDOHelper.getVersion(blog) + 1;
        try {
            dao.getObjectById(blog.getKey(), version);
            fail();
        } catch (JDOOptimisticVerificationException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * @throws Exception
     */
    public void testGetObjectsById() throws Exception {
        BlogDao dao = new BlogDao();
        Blog blog = new Blog();
        dao.makePersistentInTx(blog);
        List<String> keys = new ArrayList<String>();
        keys.add(blog.getKey());
        List<Blog> list = dao.getObjectsById(keys);
        assertEquals(1, list.size());
    }

    /**
     * @throws Exception
     */
    public void testFindAll() throws Exception {
        BlogDao dao = new BlogDao();
        Blog blog = new Blog();
        dao.makePersistentInTx(blog);
        List<Blog> list = dao.findAll();
        assertEquals(1, list.size());
    }
}