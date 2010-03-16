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
package org.slim3.gen.desc;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

/**
 * @author taedium
 * 
 */
public class ViewDescFactoryTest {

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testCreateViewDesc_slashOnly() throws Exception {
        ViewDescFactory factory = new ViewDescFactory();
        ViewDesc viewDesc = factory.createViewDesc("/");
        assertThat(viewDesc.getDirName(), is(""));
        assertThat(viewDesc.getFileName(), is("index.jsp"));
        assertThat(viewDesc.getRelativePath(), is(""));
        assertThat(viewDesc.getTitle(), is("Index"));
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testCreateViewDesc_oneDepth() throws Exception {
        ViewDescFactory factory = new ViewDescFactory();
        ViewDesc viewDesc = factory.createViewDesc("/aaa");
        assertThat(viewDesc.getDirName(), is(""));
        assertThat(viewDesc.getFileName(), is("aaa.jsp"));
        assertThat(viewDesc.getRelativePath(), is("aaa"));
        assertThat(viewDesc.getTitle(), is("Aaa"));
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testCreateViewDesc_oneDepth_endsWithSlash() throws Exception {
        ViewDescFactory factory = new ViewDescFactory();
        ViewDesc viewDesc = factory.createViewDesc("/aaa/");
        assertThat(viewDesc.getDirName(), is("/aaa"));
        assertThat(viewDesc.getFileName(), is("index.jsp"));
        assertThat(viewDesc.getRelativePath(), is(""));
        assertThat(viewDesc.getTitle(), is("aaa Index"));
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testCreateViewDesc_twoDepth() throws Exception {
        ViewDescFactory factory = new ViewDescFactory();
        ViewDesc viewDesc = factory.createViewDesc("/aaa/bbb");
        assertThat(viewDesc.getDirName(), is("/aaa"));
        assertThat(viewDesc.getFileName(), is("bbb.jsp"));
        assertThat(viewDesc.getRelativePath(), is("bbb"));
        assertThat(viewDesc.getTitle(), is("aaa Bbb"));
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testCreateViewDesc_twoDepth_endsWithSlash() throws Exception {
        ViewDescFactory factory = new ViewDescFactory();
        ViewDesc viewDesc = factory.createViewDesc("/aaa/bbb/");
        assertThat(viewDesc.getDirName(), is("/aaa/bbb"));
        assertThat(viewDesc.getFileName(), is("index.jsp"));
        assertThat(viewDesc.getRelativePath(), is(""));
        assertThat(viewDesc.getTitle(), is("aaa bbb Index"));
    }
}
