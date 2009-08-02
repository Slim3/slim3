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

import org.slim3.controller.ControllerConstants;
import org.slim3.tester.MockHttpServletRequest;
import org.slim3.tester.MockServletContext;
import org.slim3.util.ApplicationMessage;
import org.slim3.util.RequestMap;

/**
 * @author higa
 * 
 */
public class ValidatorsTest extends TestCase {

    private MockServletContext servletContext;

    private MockHttpServletRequest request;

    private Map<String, Object> parameters;

    private Validators v;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        servletContext = new MockServletContext();
        request = new MockHttpServletRequest(servletContext);
        parameters = new RequestMap(request);
        parameters.put(ControllerConstants.ERRORS_KEY, new Errors());
        v = new Validators(parameters);

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
        parameters.put("aaa", "1");
        v.add("aaa", v.required(), v.byteType());
        assertTrue(v.validate());
        assertNotNull(parameters.get(ControllerConstants.ERRORS_KEY));
        assertTrue(v.errors.isEmpty());
        assertSame(parameters.get(ControllerConstants.ERRORS_KEY), v.errors);
    }

    /**
     * @throws Exception
     */
    public void testValidateForInvalid() throws Exception {
        v.add("aaa", v.required(), v.byteType());
        assertFalse(v.validate());
        assertNotNull(parameters.get(ControllerConstants.ERRORS_KEY));
        assertEquals("Aaa is required.", v.errors.get("aaa"));
    }

    /**
     * @throws Exception
     */
    public void testRequired() throws Exception {
        assertEquals(RequiredValidator.class, v.required().getClass());
        assertEquals(RequiredValidator.class, v.required("hoge").getClass());
        parameters.put("aaa", "123");
        assertNull(v.required().validate(parameters, "aaa"));
    }

    /**
     * @throws Exception
     */
    public void testByteType() throws Exception {
        assertEquals(ByteTypeValidator.class, v.byteType().getClass());
        assertEquals(ByteTypeValidator.class, v.byteType("hoge").getClass());
        parameters.put("aaa", "123");
        assertNull(v.byteType().validate(parameters, "aaa"));
    }

    /**
     * @throws Exception
     */
    public void testShortType() throws Exception {
        assertEquals(ShortTypeValidator.class, v.shortType().getClass());
        assertEquals(ShortTypeValidator.class, v.shortType("hoge").getClass());
        parameters.put("aaa", "123");
        assertNull(v.shortType().validate(parameters, "aaa"));
    }

    /**
     * @throws Exception
     */
    public void testIntegerType() throws Exception {
        assertEquals(IntegerTypeValidator.class, v.integerType().getClass());
        assertEquals(IntegerTypeValidator.class, v
            .integerType("hoge")
            .getClass());
        parameters.put("aaa", "123");
        assertNull(v.integerType().validate(parameters, "aaa"));
    }

    /**
     * @throws Exception
     */
    public void testLongType() throws Exception {
        assertEquals(LongTypeValidator.class, v.longType().getClass());
        assertEquals(LongTypeValidator.class, v.longType("hoge").getClass());
        parameters.put("aaa", "123");
        assertNull(v.longType().validate(parameters, "aaa"));
    }

    /**
     * @throws Exception
     */
    public void testFloatType() throws Exception {
        assertEquals(FloatTypeValidator.class, v.floatType().getClass());
        assertEquals(FloatTypeValidator.class, v.floatType("hoge").getClass());
        parameters.put("aaa", "123");
        assertNull(v.floatType().validate(parameters, "aaa"));
    }

    /**
     * @throws Exception
     */
    public void testDoubleType() throws Exception {
        assertEquals(DoubleTypeValidator.class, v.doubleType().getClass());
        assertEquals(DoubleTypeValidator.class, v.doubleType("hoge").getClass());
        parameters.put("aaa", "123");
        assertNull(v.doubleType().validate(parameters, "aaa"));
    }

    /**
     * @throws Exception
     */
    public void testNumberType() throws Exception {
        Validators v = new Validators(parameters);
        assertEquals(NumberTypeValidator.class, v.numberType("###").getClass());
        assertEquals(NumberTypeValidator.class, v
            .numberType("###", "hoge")
            .getClass());
        parameters.put("aaa", "123");
        assertNull(v.numberType("####").validate(parameters, "aaa"));
    }

    /**
     * @throws Exception
     */
    public void testDateType() throws Exception {
        assertEquals(DateTypeValidator.class, v
            .dateType("MM/dd/yyyy")
            .getClass());
        assertEquals(DateTypeValidator.class, v
            .dateType("MM/dd/yyyy", "hoge")
            .getClass());
        parameters.put("aaa", "01/01/1970");
        assertNull(v.dateType("MM/dd/yyyy").validate(parameters, "aaa"));
    }

    /**
     * @throws Exception
     */
    public void testMinlength() throws Exception {
        assertEquals(MinlengthValidator.class, v.minlength(3).getClass());
        assertEquals(MinlengthValidator.class, v
            .minlength(3, "hoge")
            .getClass());
        parameters.put("aaa", "xxxx");
        assertNull(v.minlength(3).validate(parameters, "aaa"));
    }

    /**
     * @throws Exception
     */
    public void testMaxlength() throws Exception {
        assertEquals(MaxlengthValidator.class, v.maxlength(3).getClass());
        assertEquals(MaxlengthValidator.class, v
            .maxlength(3, "hoge")
            .getClass());
        parameters.put("aaa", "xx");
        assertNull(v.maxlength(3).validate(parameters, "aaa"));
    }

    /**
     * @throws Exception
     */
    public void testLongRange() throws Exception {
        assertEquals(LongRangeValidator.class, v.longRange(3, 5).getClass());
        assertEquals(LongRangeValidator.class, v
            .longRange(3, 5, "hoge")
            .getClass());
        parameters.put("aaa", "4");
        assertNull(v.longRange(3, 5).validate(parameters, "aaa"));
    }

    /**
     * @throws Exception
     */
    public void testDoubleRange() throws Exception {
        assertEquals(DoubleRangeValidator.class, v.doubleRange(3, 5).getClass());
        assertEquals(DoubleRangeValidator.class, v
            .doubleRange(3, 5, "hoge")
            .getClass());
        parameters.put("aaa", "4.1");
        assertNull(v.doubleRange(3, 5).validate(parameters, "aaa"));
    }
}