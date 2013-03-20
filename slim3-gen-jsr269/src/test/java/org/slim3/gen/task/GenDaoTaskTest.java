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
package org.slim3.gen.task;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.slim3.gen.desc.DaoDesc;

/**
 * @author sue445
 *
 */
public class GenDaoTaskTest {

    /**
     * @throws Exception
     */
    @Test
    public void testCreateDaoDesc() throws Exception{
        GenDaoTask task = new GenDaoTask();
        task.setModelDefinition("Hoge");
        task.setPackageName("slim3");

        DaoDesc daoDesc = task.createDaoDesc();
        assertThat(daoDesc.getSimpleName(), is("HogeDao"));
        assertThat(daoDesc.getPackageName(), is("slim3.dao"));
        assertThat(daoDesc.getModelClassName(), is("slim3.model.Hoge"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testCreateDaoDescWithSuperClass() throws Exception{
        GenDaoTask task = new GenDaoTask();
        task.setModelDefinition("Hoge extends Foo");
        task.setPackageName("slim3");

        DaoDesc daoDesc = task.createDaoDesc();
        assertThat(daoDesc.getSimpleName(), is("HogeDao"));
        assertThat(daoDesc.getPackageName(), is("slim3.dao"));
        assertThat(daoDesc.getModelClassName(), is("slim3.model.Hoge"));
    }
}
