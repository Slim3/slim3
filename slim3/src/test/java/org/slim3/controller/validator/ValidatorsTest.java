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

import junit.framework.TestCase;

import org.slim3.controller.ControllerConstants;
import org.slim3.controller.validator.ByteTypeValidator;
import org.slim3.controller.validator.DateTypeValidator;
import org.slim3.controller.validator.DoubleRangeValidator;
import org.slim3.controller.validator.DoubleTypeValidator;
import org.slim3.controller.validator.FloatTypeValidator;
import org.slim3.controller.validator.IntegerTypeValidator;
import org.slim3.controller.validator.LongRangeValidator;
import org.slim3.controller.validator.LongTypeValidator;
import org.slim3.controller.validator.MaxlengthValidator;
import org.slim3.controller.validator.MinlengthValidator;
import org.slim3.controller.validator.NumberTypeValidator;
import org.slim3.controller.validator.RequiredValidator;
import org.slim3.controller.validator.ShortTypeValidator;
import org.slim3.controller.validator.Validators;
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

    private Validators v = new Validators(request);

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
        v.add("aaa", v.required(), v.byteType());
        assertFalse(v.validate());
        assertNotNull(request.getAttribute(ControllerConstants.ERRORS_KEY));
        assertEquals("Aaa is required.", v.errors.get("aaa"));
    }

    /**
     * @throws Exception
     */
    public void testRequired() throws Exception {
        assertEquals(RequiredValidator.class, v.required().getClass());
    }

    /**
     * @throws Exception
     */
    public void testByteType() throws Exception {
        assertEquals(ByteTypeValidator.class, v.byteType().getClass());
    }

    /**
     * @throws Exception
     */
    public void testShortType() throws Exception {
        assertEquals(ShortTypeValidator.class, v.shortType().getClass());
    }

    /**
     * @throws Exception
     */
    public void testIntegerType() throws Exception {
        assertEquals(IntegerTypeValidator.class, v.integerType().getClass());
    }

    /**
     * @throws Exception
     */
    public void testLongType() throws Exception {
        assertEquals(LongTypeValidator.class, v.longType().getClass());
    }

    /**
     * @throws Exception
     */
    public void testFloatType() throws Exception {
        assertEquals(FloatTypeValidator.class, v.floatType().getClass());
    }

    /**
     * @throws Exception
     */
    public void testDoubleType() throws Exception {
        assertEquals(DoubleTypeValidator.class, v.doubleType().getClass());
    }

    /**
     * @throws Exception
     */
    public void testNumberType() throws Exception {
        Validators v = new Validators(request);
        assertEquals(NumberTypeValidator.class, v.numberType("###").getClass());
        request.setAttribute("aaa", "123");
        assertNull(v.numberType("####").validate(request, "aaa"));
    }

    /**
     * @throws Exception
     */
    public void testDateType() throws Exception {
        assertEquals(DateTypeValidator.class, v
            .dateType("MM/dd/yyyy")
            .getClass());
        request.setAttribute("aaa", "01/01/1970");
        assertNull(v.dateType("MM/dd/yyyy").validate(request, "aaa"));
    }

    /**
     * @throws Exception
     */
    public void testMinlength() throws Exception {
        assertEquals(MinlengthValidator.class, v.minlength(3).getClass());
        request.setAttribute("aaa", "xxxx");
        assertNull(v.minlength(3).validate(request, "aaa"));
    }

    /**
     * @throws Exception
     */
    public void testMaxlength() throws Exception {
        assertEquals(MaxlengthValidator.class, v.maxlength(3).getClass());
        request.setAttribute("aaa", "xx");
        assertNull(v.maxlength(3).validate(request, "aaa"));
    }

    /**
     * @throws Exception
     */
    public void testLongRange() throws Exception {
        assertEquals(LongRangeValidator.class, v.longRange(3, 5).getClass());
        request.setAttribute("aaa", "4");
        assertNull(v.longRange(3, 5).validate(request, "aaa"));
    }

    /**
     * @throws Exception
     */
    public void testDoubleRange() throws Exception {
        assertEquals(DoubleRangeValidator.class, v.doubleRange(3, 5).getClass());
        request.setAttribute("aaa", "4.1");
        assertNull(v.doubleRange(3, 5).validate(request, "aaa"));
    }
}