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
package org.slim3.controller.validator;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slim3.controller.ControllerConstants;
import org.slim3.tester.MockHttpServletRequest;
import org.slim3.tester.MockServletContext;
import org.slim3.util.ApplicationMessage;
import org.slim3.util.RequestMap;

/**
 * @author higa
 * 
 */
public class ValidatorsTest {

    private MockServletContext servletContext;

    private MockHttpServletRequest request;

    private Map<String, Object> parameters;

    private Validators v;

    /**
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        servletContext = new MockServletContext();
        request = new MockHttpServletRequest(servletContext);
        parameters = new RequestMap(request);
        parameters.put(ControllerConstants.ERRORS_KEY, new Errors());
        v = new Validators(parameters);
        ApplicationMessage.setBundle("test", Locale.ENGLISH);
    }

    /**
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        ApplicationMessage.clearBundle();
    }

    /**
     * @throws Exception
     */
    @Test
    public void constructorWithoutErrors() throws Exception {
        Validators v2 = new Validators(new HashMap<String, Object>());
        assertThat(v2.getErrors(), is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void validValue() throws Exception {
        parameters.put("aaa", "1");
        v.add("aaa", v.required(), v.byteType());
        assertThat(v.validate(), is(true));
        assertThat(
            parameters.get(ControllerConstants.ERRORS_KEY),
            is(not(nullValue())));
        assertThat(v.errors.isEmpty(), is(true));
        assertThat(
            (Errors) parameters.get(ControllerConstants.ERRORS_KEY),
            is(sameInstance(v.errors)));
    }

    /**
     * @throws Exception
     */
    @Test
    public void validateForInvalidValue() throws Exception {
        v.add("aaa", v.required(), v.byteType());
        assertThat(v.validate(), is(false));
        assertThat(
            parameters.get(ControllerConstants.ERRORS_KEY),
            is(not(nullValue())));
        assertThat(v.errors.get("aaa"), is("Aaa is required."));
    }

    /**
     * @throws Exception
     */
    @Test
    public void validateWhenErrorsIsNotEmpty() throws Exception {
        v.errors.put("aaa", "xxx");
        assertThat(v.validate(), is(true));
    }

    /**
     * @throws Exception
     */
    @Test
    public void required() throws Exception {
        assertThat(v.required(), is(RequiredValidator.class));
        assertThat(v.required("hoge"), is(RequiredValidator.class));
        parameters.put("aaa", "123");
        assertThat(v.required().validate(parameters, "aaa"), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void byteType() throws Exception {
        assertThat(v.byteType(), is(ByteTypeValidator.class));
        assertThat(v.byteType("hoge"), is(ByteTypeValidator.class));
        parameters.put("aaa", "123");
        assertThat(v.byteType().validate(parameters, "aaa"), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void shortType() throws Exception {
        assertThat(v.shortType(), is(ShortTypeValidator.class));
        assertThat(v.shortType("hoge"), is(ShortTypeValidator.class));
        parameters.put("aaa", "123");
        assertThat(v.shortType().validate(parameters, "aaa"), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void integerType() throws Exception {
        assertThat(v.integerType(), is(IntegerTypeValidator.class));
        assertThat(v.integerType("hoge"), is(IntegerTypeValidator.class));
        parameters.put("aaa", "123");
        assertThat(v.integerType().validate(parameters, "aaa"), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void longType() throws Exception {
        assertThat(v.longType(), is(LongTypeValidator.class));
        assertThat(v.longType("hoge"), is(LongTypeValidator.class));
        parameters.put("aaa", "123");
        assertThat(v.longType().validate(parameters, "aaa"), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void floatType() throws Exception {
        assertThat(v.floatType(), is(FloatTypeValidator.class));
        assertThat(v.floatType("hoge"), is(FloatTypeValidator.class));
        parameters.put("aaa", "123");
        assertThat(v.floatType().validate(parameters, "aaa"), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void doubleType() throws Exception {
        assertThat(v.doubleType(), is(DoubleTypeValidator.class));
        assertThat(v.doubleType("hoge"), is(DoubleTypeValidator.class));
        parameters.put("aaa", "123");
        assertThat(v.doubleType().validate(parameters, "aaa"), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void numberType() throws Exception {
        assertThat(v.numberType("###"), is(NumberTypeValidator.class));
        assertThat(v.numberType("###", "hoge"), is(NumberTypeValidator.class));
        parameters.put("aaa", "123");
        assertThat(
            v.numberType("####").validate(parameters, "aaa"),
            is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void dateType() throws Exception {
        assertThat(v.dateType("MM/dd/yyyy"), is(DateTypeValidator.class));
        assertThat(
            v.dateType("MM/dd/yyyy", "hoge"),
            is(DateTypeValidator.class));
        parameters.put("aaa", "01/01/1970");
        assertThat(
            v.dateType("MM/dd/yyyy").validate(parameters, "aaa"),
            is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void minlength() throws Exception {
        assertThat(v.minlength(3), is(MinlengthValidator.class));
        assertThat(v.minlength(3, "hoge"), is(MinlengthValidator.class));
        parameters.put("aaa", "xxxx");
        assertThat(v.minlength(3).validate(parameters, "aaa"), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void maxlength() throws Exception {
        assertThat(v.maxlength(3), is(MaxlengthValidator.class));
        assertThat(v.maxlength(3, "hoge"), is(MaxlengthValidator.class));
        parameters.put("aaa", "xx");
        assertThat(v.maxlength(3).validate(parameters, "aaa"), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void longRange() throws Exception {
        assertThat(v.longRange(3, 5), is(LongRangeValidator.class));
        assertThat(v.longRange(3, 5, "hoge"), is(LongRangeValidator.class));
        parameters.put("aaa", "4");
        assertThat(
            v.longRange(3, 5).validate(parameters, "aaa"),
            is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void doubleRange() throws Exception {
        assertThat(v.doubleRange(3, 5), is(DoubleRangeValidator.class));
        assertThat(v.doubleRange(3, 5, "hoge"), is(DoubleRangeValidator.class));
        parameters.put("aaa", "4.1");
        assertThat(
            v.doubleRange(3, 5).validate(parameters, "aaa"),
            is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void regexp() throws Exception {
        assertThat(v.regexp("abc"), is(RegexpValidator.class));
        assertThat(v.regexp("abc", "hoge"), is(RegexpValidator.class));
        parameters.put("aaa", "abc");
        assertThat(v.regexp("abc").validate(parameters, "aaa"), is(nullValue()));
    }
}