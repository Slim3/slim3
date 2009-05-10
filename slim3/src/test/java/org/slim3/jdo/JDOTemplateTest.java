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
package org.slim3.jdo;

import org.slim3.jdo.JDOTemplate;

import junit.framework.TestCase;

/**
 * @author higa
 * 
 */
public class JDOTemplateTest extends TestCase {

    private MyTemplate template = new MyTemplate();

    /**
     * @throws Exception
     */
    public void testKey() throws Exception {
        SampleMeta s = new SampleMeta();
        assertNotNull(template.key(Sample.class, 1));
        assertNotNull(template.key(Sample.class, "hoge"));
        assertNotNull(template.key(s, 1));
        assertNotNull(template.key(s, "hoge"));
        assertEquals(template.key(Sample.class, 1), template.key(s, 1));
        assertEquals(template.key(Sample.class, "hoge"), template
            .key(s, "hoge"));
    }

    private static class MyTemplate extends JDOTemplate {

        @Override
        protected Object doRun() {
            return null;
        }
    }
}