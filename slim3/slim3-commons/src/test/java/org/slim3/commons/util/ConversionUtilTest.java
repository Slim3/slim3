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
package org.slim3.commons.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;

import junit.framework.TestCase;

import org.slim3.commons.exception.ClassCanNotAssignedRuntimeException;

/**
 * @author higa
 * 
 */
public class ConversionUtilTest extends TestCase {

    /**
     * 
     * @throws Exception
     */
    public void testConvertNullToCharacter() throws Exception {
        try {
            ConversionUtil.convert(null, char.class);
            fail();
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testConvertNullToByte() throws Exception {
        assertEquals(Byte.valueOf((byte) 0), ConversionUtil.convert(
            null,
            byte.class));
    }

    /**
     * 
     * @throws Exception
     */
    public void testConvertNullToShort() throws Exception {
        assertEquals(Short.valueOf((short) 0), ConversionUtil.convert(
            null,
            short.class));
    }

    /**
     * 
     * @throws Exception
     */
    public void testConvertNullToInteger() throws Exception {
        assertEquals(Integer.valueOf(0), ConversionUtil
            .convert(null, int.class));
    }

    /**
     * 
     * @throws Exception
     */
    public void testConvertNullToLong() throws Exception {
        assertEquals(Long.valueOf(0), ConversionUtil.convert(null, long.class));
    }

    /**
     * 
     * @throws Exception
     */
    public void testConvertNullToFloat() throws Exception {
        assertEquals(Float.valueOf(0), ConversionUtil
            .convert(null, float.class));
    }

    /**
     * 
     * @throws Exception
     */
    public void testConvertNullToDouble() throws Exception {
        assertEquals(Double.valueOf(0), ConversionUtil.convert(
            null,
            double.class));
    }

    /**
     * 
     * @throws Exception
     */
    public void testConvertNullToBoolean() throws Exception {
        assertEquals(Boolean.FALSE, ConversionUtil.convert(null, boolean.class));
    }

    /**
     * 
     * @throws Exception
     */
    public void testConvertNullToNotPrimitive() throws Exception {
        assertNull(ConversionUtil.convert(null, Object.class));
    }

    /**
     * 
     * @throws Exception
     */
    public void testConvertForSameClass() throws Exception {
        MyDto dto = new MyDto();
        assertSame(dto, ConversionUtil.convert(dto, MyDto.class));
    }

    /**
     * 
     * @throws Exception
     */
    public void testConvertForAssignableClass() throws Exception {
        MyDto2 dto = new MyDto2();
        assertSame(dto, ConversionUtil.convert(dto, MyDto.class));
    }

    /**
     * 
     * @throws Exception
     */
    public void testConvertStringToByte() throws Exception {
        assertEquals(Byte.valueOf((byte) 1), ConversionUtil.convert(
            "1",
            Byte.class));
    }

    /**
     * 
     * @throws Exception
     */
    public void testConvertStringToShort() throws Exception {
        assertEquals(Short.valueOf((short) 1), ConversionUtil.convert(
            "1",
            Short.class));
    }

    /**
     * 
     * @throws Exception
     */
    public void testConvertStringToInteger() throws Exception {
        assertEquals(Integer.valueOf(1), ConversionUtil.convert(
            "1",
            Integer.class));
    }

    /**
     * 
     * @throws Exception
     */
    public void testConvertStringToLong() throws Exception {
        assertEquals(Long.valueOf(1), ConversionUtil.convert("1", Long.class));
    }

    /**
     * 
     * @throws Exception
     */
    public void testConvertStringToFloat() throws Exception {
        assertEquals(Float.valueOf(1), ConversionUtil.convert("1", Float.class));
    }

    /**
     * 
     * @throws Exception
     */
    public void testConvertStringToDouble() throws Exception {
        assertEquals(Double.valueOf(1), ConversionUtil.convert(
            "1",
            Double.class));
    }

    /**
     * 
     * @throws Exception
     */
    public void testConvertStringToBigInteger() throws Exception {
        try {
            ConversionUtil.convert("1", BigInteger.class);
            fail();
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testConvertStringToBigDecimal() throws Exception {
        try {
            ConversionUtil.convert("1", BigDecimal.class);
            fail();
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testConvertCalendarToDate() throws Exception {
        Calendar cal = Calendar.getInstance();
        assertEquals(cal.getTime(), ConversionUtil.convert(cal, Date.class));
    }

    /**
     * 
     * @throws Exception
     */
    public void testConvertStringToBoolean() throws Exception {
        assertEquals(Boolean.TRUE, ConversionUtil.convert("1", Boolean.class));
    }

    /**
     * 
     * @throws Exception
     */
    public void testConvertIntegerToEnum() throws Exception {
        assertEquals(MyEnum.TWO, ConversionUtil.convert(1, MyEnum.class));
    }

    /**
     * 
     * @throws Exception
     */
    public void testConvertBooleanToString() throws Exception {
        assertEquals("true", ConversionUtil.convert(Boolean.TRUE, String.class));
    }

    /**
     * 
     * @throws Exception
     */
    public void testConvertForClassCanNotAssignedRuntimeException()
            throws Exception {
        try {
            ConversionUtil.convert(1, MyDto.class);
            fail();
        } catch (ClassCanNotAssignedRuntimeException e) {
            System.out.println(e);
            assertEquals(Integer.class, e.getOriginalClass());
            assertEquals(MyDto.class, e.getDestinationClass());
        }
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
