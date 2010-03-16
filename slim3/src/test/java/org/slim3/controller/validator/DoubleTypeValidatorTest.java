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

import java.util.Locale;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slim3.tester.MockHttpServletRequest;
import org.slim3.tester.MockServletContext;
import org.slim3.util.ApplicationMessage;
import org.slim3.util.RequestMap;

/**
 * @author higa
 * 
 */
public class DoubleTypeValidatorTest {

    private MockServletContext servletContext = new MockServletContext();

    private MockHttpServletRequest request =
        new MockHttpServletRequest(servletContext);

    private Map<String, Object> parameters = new RequestMap(request);

    /**
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
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
    public void validateForNull() throws Exception {
        assertThat(
            DoubleTypeValidator.INSTANCE.validate(parameters, "aaa"),
            is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void validateForEmptyString() throws Exception {
        parameters.put("aaa", "");
        assertThat(
            DoubleTypeValidator.INSTANCE.validate(parameters, "aaa"),
            is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void validateForValidValue() throws Exception {
        parameters.put("aaa", "111");
        assertThat(
            DoubleTypeValidator.INSTANCE.validate(parameters, "aaa"),
            is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void validateForInvalidValue() throws Exception {
        parameters.put("aaa", "xxx");
        assertThat(
            DoubleTypeValidator.INSTANCE.validate(parameters, "aaa"),
            is("Aaa must be a double."));
    }

    /**
     * @throws Exception
     */
    @Test
    public void validateForInvalidValueAndSpecificMessage() throws Exception {
        parameters.put("aaa", "xxx");
        assertThat(
            new DoubleTypeValidator("hoge").validate(parameters, "aaa"),
            is("hoge"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getMessageKey() throws Exception {
        assertThat(
            DoubleTypeValidator.INSTANCE.getMessageKey(),
            is("validator.doubleType"));
    }
}