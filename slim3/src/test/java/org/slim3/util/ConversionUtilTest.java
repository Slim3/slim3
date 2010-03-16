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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

/**
 * @author higa
 * 
 */
public class ConversionUtilTest {

    /**
     * 
     * @throws Exception
     */
    @Test(expected = IllegalArgumentException.class)
    public void convertNullToCharacter() throws Exception {
        ConversionUtil.convert(null, char.class);
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void convertNullToByte() throws Exception {
        assertThat(ConversionUtil.convert(null, byte.class), is((byte) 0));
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void convertNullToShort() throws Exception {
        assertThat(ConversionUtil.convert(null, short.class), is((short) 0));
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void convertNullToInteger() throws Exception {
        assertThat(ConversionUtil.convert(null, int.class), is(0));
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void convertNullToLong() throws Exception {
        assertThat(ConversionUtil.convert(null, long.class), is(0L));
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void convertNullToFloat() throws Exception {
        assertThat(ConversionUtil.convert(null, float.class), is(0f));
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void convertNullToDouble() throws Exception {
        assertThat(ConversionUtil.convert(null, double.class), is(0d));
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void convertNullToBoolean() throws Exception {
        assertThat(ConversionUtil.convert(null, boolean.class), is(false));
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void convertNullToNotPrimitive() throws Exception {
        assertThat(ConversionUtil.convert(null, Object.class), is(nullValue()));
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void convertForSameClass() throws Exception {
        MyDto dto = new MyDto();
        assertThat(
            ConversionUtil.convert(dto, MyDto.class),
            is(sameInstance(dto)));
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void convertForAssignableClass() throws Exception {
        MyDto2 dto = new MyDto2();
        assertThat(
            (MyDto2) ConversionUtil.convert(dto, MyDto.class),
            is(sameInstance(dto)));
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void convertStringToByte() throws Exception {
        assertThat(ConversionUtil.convert("1", Byte.class), is((byte) 1));
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void convertStringToShort() throws Exception {
        assertThat(ConversionUtil.convert("1", Short.class), is((short) 1));
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void convertStringToInteger() throws Exception {
        assertThat(ConversionUtil.convert("1", Integer.class), is(1));
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void convertStringToLong() throws Exception {
        assertThat(ConversionUtil.convert("1", Long.class), is(1L));
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void convertStringToFloat() throws Exception {
        assertThat(ConversionUtil.convert("1", Float.class), is(1f));
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void convertStringToDouble() throws Exception {
        assertThat(ConversionUtil.convert("1", Double.class), is(1d));
    }

    /**
     * 
     * @throws Exception
     */
    @Test(expected = IllegalArgumentException.class)
    public void convertStringToBigInteger() throws Exception {
        ConversionUtil.convert("1", BigInteger.class);
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void convertStringToBigDecimal() throws Exception {
        assertThat(
            ConversionUtil.convert("1", BigDecimal.class),
            is(new BigDecimal("1")));
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void convertCalendarToDate() throws Exception {
        Calendar cal = Calendar.getInstance();
        assertThat(ConversionUtil.convert(cal, Date.class), is(cal.getTime()));
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void convertStringToBoolean() throws Exception {
        assertThat(ConversionUtil.convert("1", Boolean.class), is(true));
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void convertIntegerToEnum() throws Exception {
        assertThat(ConversionUtil.convert(1, MyEnum.class), is(MyEnum.TWO));
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void convertStringToEnum() throws Exception {
        assertThat(ConversionUtil.convert("TWO", MyEnum.class), is(MyEnum.TWO));
    }

    /**
     * 
     * @throws Exception
     */
    @Test(expected = IllegalArgumentException.class)
    public void convertIllegalArgumentToEnum() throws Exception {
        ConversionUtil.convert(new Date(), MyEnum.class);
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void convertBooleanToString() throws Exception {
        assertThat(
            ConversionUtil.convert(Boolean.TRUE, String.class),
            is("true"));
    }

    /**
     * 
     * @throws Exception
     */
    @Test(expected = IllegalArgumentException.class)
    public void convertForIllegalArgument() throws Exception {
        ConversionUtil.convert(1, MyDto.class);
    }

    private static enum MyEnum {
        /**
         * 
         */
        ONE, TWO;
    }

    private static class MyDto {

    }

    private static class MyDto2 extends MyDto {

    }
}
