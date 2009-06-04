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
package org.slim3.controller;

import java.util.Locale;

import junit.framework.TestCase;

import org.slim3.tester.MockHttpServletRequest;
import org.slim3.tester.MockServletContext;
import org.slim3.util.ApplicationMessage;

/**
 * @author higa
 * 
 */
public class ValidatorsTest extends TestCase {

    private MockServletContext servletContext = new MockServletContext();

    private MockHttpServletRequest request =
        new MockHttpServletRequest(servletContext);

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
    public void testValidateForValid() throws Exception {
        request.setAttribute("aaa", "1");
        Validators v = new Validators(request);
        v.add("aaa", v.required(), v.byteType());
        assertTrue(v.validate());
        assertNotNull(request.getAttribute(ControllerConstants.ERRORS_KEY));
        assertTrue(v.errors.isEmpty());
        assertSame(
            request.getAttribute(ControllerConstants.ERRORS_KEY),
            v.errors);
    }

    /**
     * @throws Exception
     */
    public void testValidateForInvalid() throws Exception {
        Validators v = new Validators(request);
        v.add("aaa", v.required(), v.byteType());
        assertFalse(v.validate());
        assertNotNull(request.getAttribute(ControllerConstants.ERRORS_KEY));
        assertEquals("Aaa is required.", v.errors.get("aaa"));
    }

    /**
     * @throws Exception
     */
    public void testRequired() throws Exception {
        Validators v = new Validators(request);
        assertEquals(RequiredValidator.class, v.required().getClass());
    }

    /**
     * @throws Exception
     */
    public void testByteType() throws Exception {
        Validators v = new Validators(request);
        assertEquals(ByteTypeValidator.class, v.byteType().getClass());
    }

    /**
     * @throws Exception
     */
    public void testShortType() throws Exception {
        Validators v = new Validators(request);
        assertEquals(ShortTypeValidator.class, v.shortType().getClass());
    }

    /**
     * @throws Exception
     */
    public void testIntegerType() throws Exception {
        Validators v = new Validators(request);
        assertEquals(IntegerTypeValidator.class, v.integerType().getClass());
    }

    /**
     * @throws Exception
     */
    public void testLongType() throws Exception {
        Validators v = new Validators(request);
        assertEquals(LongTypeValidator.class, v.longType().getClass());
    }

    /**
     * @throws Exception
     */
    public void testFloatType() throws Exception {
        Validators v = new Validators(request);
        assertEquals(FloatTypeValidator.class, v.floatType().getClass());
    }

    /**
     * @throws Exception
     */
    public void testDoubleType() throws Exception {
        Validators v = new Validators(request);
        assertEquals(DoubleTypeValidator.class, v.doubleType().getClass());
    }

    /**
     * @throws Exception
     */
    public void testNumberType() throws Exception {
        Validators v = new Validators(request);
        assertEquals(NumberTypeValidator.class, v.numberType("###").getClass());
    }

    /**
     * @throws Exception
     */
    public void testDateType() throws Exception {
        Validators v = new Validators(request);
        assertEquals(DateTypeValidator.class, v
            .dateType("MM/dd/yyyy")
            .getClass());
    }

    /**
     * @throws Exception
     */
    public void testMinlength() throws Exception {
        Validators v = new Validators(request);
        assertEquals(MinlengthValidator.class, v.minlength(3).getClass());
    }
}