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
package org.slim3.controller.validator;

import java.util.Locale;
import java.util.Map;

import junit.framework.TestCase;

import org.slim3.util.ApplicationMessage;

/**
 * @author higa
 * 
 */
public class AbstractValidatorTest extends TestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        ApplicationMessage.setBundle("test", Locale.ENGLISH);
    }

    @Override
    protected void tearDown() throws Exception {
        ApplicationMessage.clearBundle();
        super.tearDown();
    }

    /**
     * @throws Exception
     */
    public void testGetLabel() throws Exception {
        assertEquals("Aaa", new MyValidator().getLabel("aaa"));
    }

    private static class MyValidator extends AbstractValidator {

        @Override
        public String validate(Map<String, Object> parameters, String name) {
            return null;
        }
    }
}
