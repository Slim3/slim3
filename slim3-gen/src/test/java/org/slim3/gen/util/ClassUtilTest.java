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
package org.slim3.gen.util;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

/**
 * @author taedium
 * 
 */
public class ClassUtilTest {

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testGetPackageName() throws Exception {
        assertThat(ClassUtil.getPackageName("aaa.bbb.Ccc"), is("aaa.bbb"));
        assertThat(ClassUtil.getPackageName("Ccc"), is(""));
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testGetSimpleName() throws Exception {
        assertThat(ClassUtil.getSimpleName("aaa.bbb.Ccc"), is("Ccc"));
        assertThat(ClassUtil.getSimpleName("Ccc"), is("Ccc"));
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testGetQualifiedName() throws Exception {
        assertThat(ClassUtil.getQualifiedName("aaa", "Bbb"), is("aaa.Bbb"));
        assertThat(ClassUtil.getQualifiedName("", "Bbb"), is("Bbb"));
        assertThat(ClassUtil.getQualifiedName(null, "Bbb"), is("Bbb"));
    }
}
