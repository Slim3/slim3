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
package org.slim3.util;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;

/**
 * @author higa
 */
public class CopyOptionsTest {

    private CopyOptions options = new CopyOptions();

    /**
     * @throws Exception
     */
    @Test
    public void include() throws Exception {
        assertThat(options.include("hoge"), is(sameInstance(options)));
        assertThat(options.includedPropertyNames.length, is(1));
        assertThat(options.includedPropertyNames[0], is("hoge"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void exclude() throws Exception {
        assertThat(options.exclude("hoge"), is(sameInstance(options)));
        assertThat(options.excludedPropertyNames.length, is(1));
        assertThat(options.excludedPropertyNames[0], is("hoge"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void isTargetProperty() throws Exception {
        assertThat(options.isTargetProperty("hoge"), is(true));
    }

    /**
     * @throws Exception
     */
    @Test
    public void isTargetPropertyForInclude() throws Exception {
        options.include("hoge");
        assertThat(options.isTargetProperty("hoge"), is(true));
        assertThat(options.isTargetProperty("hoge2"), is(false));
    }

    /**
     * @throws Exception
     */
    @Test
    public void isTargetPropertyForExclude() throws Exception {
        options.exclude("hoge");
        assertThat(options.isTargetProperty("hoge"), is(false));
        assertThat(options.isTargetProperty("hoge2"), is(true));
    }

    /**
     * @throws Exception
     */
    @Test
    public void isTargetPropertyForIncludeExclude() throws Exception {
        options.include("hoge", "hoge2").exclude("hoge2", "hoge3");
        assertThat(options.isTargetProperty("hoge"), is(true));
        assertThat(options.isTargetProperty("hoge2"), is(false));
        assertThat(options.isTargetProperty("hoge3"), is(false));
        assertThat(options.isTargetProperty("hoge4"), is(false));
    }

    /**
     * @throws Exception
     */
    @Test
    public void isTargetValue() throws Exception {
        assertThat(options.isTargetValue("hoge"), is(true));
        assertThat(options.isTargetValue(""), is(true));
        assertThat(options.isTargetValue(null), is(true));
    }

    /**
     * @throws Exception
     */
    @Test
    public void isTargetValueForExcludeNull() throws Exception {
        options.excludeNull();
        assertThat(options.isTargetValue("hoge"), is(true));
        assertThat(options.isTargetValue(""), is(true));
        assertThat(options.isTargetValue(null), is(false));
    }

    /**
     * @throws Exception
     */
    @Test
    public void isTargetValueForExcludeEmptyString() throws Exception {
        options.excludeEmptyString();
        assertThat(options.isTargetValue("hoge"), is(true));
        assertThat(options.isTargetValue(""), is(false));
        assertThat(options.isTargetValue(null), is(true));
    }

    /**
     * @throws Exception
     */
    @SuppressWarnings({ "rawtypes" })
    @Test
    public void findConvererForDate() throws Exception {
        Converter converter =
            options.dateConverter("MM/dd/yyyy").findConverter(
                java.sql.Date.class);
        assertThat(converter, is(notNullValue()));
        assertThat(converter.getClass().getName(), is(DateConverter.class
            .getName()));
    }

    /**
     * @throws Exception
     */
    @SuppressWarnings({ "rawtypes" })
    @Test
    public void findConvererForNumber() throws Exception {
        Converter converter =
            options.numberConverter("#,###").findConverter(Long.class);
        assertThat(converter, is(notNullValue()));
        assertThat(converter.getClass().getName(), is(NumberConverter.class
            .getName()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void convertObjectForConverterSpecifiedByProperty() throws Exception {
        Object value =
            options
                .dateConverter("yyyyMMdd")
                .dateConverter("yyyy", "aaa")
                .convertObject(new Date(0), "aaa", String.class);
        assertThat((String) value, is("1970"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void convertObjectForDestClassNullWithConverter() throws Exception {
        Object value =
            options.dateConverter("yyyy", "aaa").convertObject(
                new Date(0),
                "aaa",
                null);
        assertThat((String) value, is("1970"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void convertObjectForDestClassStringWithConverter() throws Exception {
        Object value =
            options.dateConverter("yyyy").convertObject(
                new Date(0),
                "aaa",
                String.class);
        assertThat((String) value, is("1970"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void convertObjectForDestNullNoConverter() throws Exception {
        assertThat(
            (Integer) options.convertObject(new Integer(0), "aaa", null),
            is(0));
        assertThat(
            (Date) options.convertObject(new Date(0), "aaa", null),
            is(new Date(0)));
    }

    /**
     * @throws Exception
     */
    @Test
    public void convertObjectForDestNotString() throws Exception {
        Object value =
            options.dateConverter("yyyy").convertObject(
                new Date(0),
                "aaa",
                Date.class);
        assertThat((Date) value, is(new Date(0)));
    }

    /**
     * @throws Exception
     */
    @Test
    public void convertStringForConverterSpecifiedByProperty() throws Exception {
        Object value =
            options
                .numberConverter("###")
                .numberConverter("#,###", "aaa")
                .convertString("1,111", "aaa", Long.class);
        assertThat((Long) value, is(1111L));
    }

    /**
     * @throws Exception
     */
    @Test
    public void convertStringForDestTypeNull() throws Exception {
        Object value =
            options.numberConverter("###").convertString("1,111", "aaa", null);
        assertThat((String) value, is("1,111"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void convertStringForDestTypeString() throws Exception {
        Object value =
            options.numberConverter("###").convertString(
                "1,111",
                "aaa",
                String.class);
        assertThat((String) value, is("1,111"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void convertStringForConverterSpecifiedByClass() throws Exception {
        Object value =
            options.numberConverter("###").convertString(
                "111",
                "aaa",
                Long.class);
        assertThat((Long) value, is(111L));
    }

    /**
     * @throws Exception
     */
    @Test
    public void convertValueForNull() throws Exception {
        assertThat(
            options.convertValue(null, "aaa", Integer.class),
            is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test(expected = IllegalArgumentException.class)
    public void convertValueForBadValue() throws Exception {
        options.dateConverter("yyyy", "aaa").convertValue(
            new Integer(0),
            "aaa",
            String.class);
    }
}