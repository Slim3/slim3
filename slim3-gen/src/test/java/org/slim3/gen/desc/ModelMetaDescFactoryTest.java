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

import java.util.Collections;
import java.util.Locale;
import java.util.Map;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

import junit.framework.TestCase;

/**
 * @author taedium
 * 
 */
public class ModelMetaDescFactoryTest extends TestCase {

    /**
     * 
     * @throws Exception
     */
    public void testReplaceModelPackageName() throws Exception {
        MyProcessingEnvironment env = new MyProcessingEnvironment();
        AttributeMetaDescFactory amdf = new AttributeMetaDescFactory(env);
        ModelMetaDescFactory mmdf = new ModelMetaDescFactory(env, amdf);

        assertEquals("aaa.bbb.meta", mmdf
            .replaceModelPackageName("aaa.bbb.model"));
        assertEquals("aaa.bbb.meta.ccc", mmdf
            .replaceModelPackageName("aaa.bbb.model.ccc"));
        assertEquals("aaa.bbb.ccc", mmdf.replaceModelPackageName("aaa.bbb.ccc"));
    }

    private static class MyProcessingEnvironment implements
            ProcessingEnvironment {

        @Override
        public Elements getElementUtils() {
            return null;
        }

        @Override
        public Filer getFiler() {
            return null;
        }

        @Override
        public Locale getLocale() {
            return null;
        }

        @Override
        public Messager getMessager() {
            return null;
        }

        @Override
        public Map<String, String> getOptions() {
            return Collections.emptyMap();
        }

        @Override
        public SourceVersion getSourceVersion() {
            return null;
        }

        @Override
        public Types getTypeUtils() {
            return null;
        }

    }
}
