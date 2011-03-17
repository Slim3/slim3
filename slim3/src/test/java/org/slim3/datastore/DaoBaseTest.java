/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
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

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.slim3.datastore.model.Aaa;
import org.slim3.tester.AppEngineTestCase;

/**
 * @author higa
 * 
 */
public class DaoBaseTest extends AppEngineTestCase {

    private MyDao dao = new MyDao();

    private MyDao2 dao2 = new MyDao2();

    private MyDao4 dao4 = new MyDao4();

    /**
     * @throws Exception
     */
    @Test
    public void getModelClass() throws Exception {
        assertThat(dao.getModelClass().getName(), is(Aaa.class.getName()));
        assertThat(dao2.getModelClass().getName(), is(Aaa.class.getName()));
        assertThat(dao4.getModelClass().getName(), is(Aaa.class.getName()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getModelMeta() throws Exception {
        assertThat(dao.getModelMeta(), is(notNullValue()));
    }

    private static class MyDao extends DaoBase<Aaa> {

    }

    private static class MyDao2 extends MyDao {

    }

    private static class MyDao3<T> extends DaoBase<T> {

    }

    private static class MyDao4 extends MyDao3<Aaa> {

    }
}