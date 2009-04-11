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
package org.slim3.struts.validator;

import junit.framework.TestCase;

/**
 * @author higa
 * 
 */
public class S3GenericValidatorTest extends TestCase {

    /**
     * @throws Exception
     */
    public void testMinByteLength() throws Exception {
        assertTrue(S3GenericValidator.minByteLength("\u3042", 2, "UTF-8"));
        assertFalse(S3GenericValidator.minByteLength("a", 2, "UTF-8"));
    }

    /**
     * @throws Exception
     */
    public void testMaxByteLength() throws Exception {
        assertFalse(S3GenericValidator.maxByteLength("\u3042", 1, "UTF-8"));
        assertTrue(S3GenericValidator.maxByteLength("a", 1, "UTF-8"));
    }
}