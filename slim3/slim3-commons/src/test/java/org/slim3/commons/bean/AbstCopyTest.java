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
package org.slim3.commons.bean;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.slim3.commons.exception.ConverterRuntimeException;
import org.slim3.commons.util.DateUtil;

/**
 * @author higa
 */
public class AbstCopyTest extends TestCase {

    /**
     * @throws Exception
     */
    public void testInclude() throws Exception {
        MyCopy copy = new MyCopy();
        assertSame(copy, copy.include("hoge"));
        assertEquals(1, copy.includedPropertyNames.length);
        assertEquals("hoge", copy.includedPropertyNames[0]);
    }

    /**
     * @throws Exception
     */
    public void testExclude() throws Exception {
        MyCopy copy = new MyCopy();
        assertSame(copy, copy.exclude("hoge"));
        assertEquals(1, copy.excludedPropertyNames.length);
        assertEquals("hoge", copy.excludedPropertyNames[0]);
    }

    /**
     * @throws Exception
     */
    public void testIsTargetProperty() throws Exception {
        MyCopy copy = new MyCopy();
        assertTrue(copy.isTargetProperty("hoge"));
    }

    /**
     * @throws Exception
     */
    public void testIsTargetPropertyForInclude() throws Exception {
        MyCopy copy = new MyCopy().include("hoge");
        assertTrue(copy.isTargetProperty("hoge"));
        assertFalse(copy.isTargetProperty("hoge2"));
    }

    /**
     * @throws Exception
     */
    public void testIsTargetPropertyForExclude() throws Exception {
        MyCopy copy = new MyCopy().exclude("hoge");
        assertFalse(copy.isTargetProperty("hoge"));
        assertTrue(copy.isTargetProperty("hoge2"));
    }

    /**
     * @throws Exception
     */
    public void testIsTargetPropertyForIncludeExclude() throws Exception {
        MyCopy copy = new MyCopy();
        copy.include("hoge", "hoge2");
        copy.exclude("hoge2", "hoge3");
        assertTrue(copy.isTargetProperty("hoge"));
        assertFalse(copy.isTargetProperty("hoge2"));
        assertFalse(copy.isTargetProperty("hoge3"));
        assertFalse(copy.isTargetProperty("hoge4"));
    }

    /**
     * @throws Exception
     */
    public void testIsTargetValue() throws Exception {
        MyCopy copy = new MyCopy();
        assertTrue(copy.isTargetValue("hoge"));
        assertFalse(copy.isTargetValue(""));
        assertFalse(copy.isTargetValue(null));
    }

    /**
     * @throws Exception
     */
    public void testIsTargetValueForCopyNull() throws Exception {
        MyCopy copy = new MyCopy().copyNull();
        assertTrue(copy.isTargetValue("hoge"));
        assertFalse(copy.isTargetValue(""));
        assertTrue(copy.isTargetValue(null));
    }

    /**
     * @throws Exception
     */
    public void testIsTargetValueForCopyEmptyString() throws Exception {
        MyCopy copy = new MyCopy().copyEmptyString();
        assertTrue(copy.isTargetValue("hoge"));
        assertTrue(copy.isTargetValue(""));
        assertFalse(copy.isTargetValue(null));
    }

    /**
     * @throws Exception
     */
    public void testCopyBeanToBean() throws Exception {
        SrcBean src = new SrcBean();
        src.aaa = "aaa";
        src.bbb = "bbb";
        src.ccc = "ccc";
        src.ggg = "1";
        DestBean dest = new DestBean();
        new MyCopy().copyBeanToBean(src, dest);
        assertNull(dest.bbb);
        assertEquals("ccc", dest.ccc);
        assertNull(dest.ddd);
        assertEquals(new Integer(1), dest.ggg);
    }

    /**
     * @throws Exception
     */
    public void testCopyBeanToBeanForInclude() throws Exception {
        MyBean src = new MyBean();
        src.aaa = "aaa";
        src.bbb = "bbb";
        MyBean dest = new MyBean();
        new MyCopy().include("aaa").copyBeanToBean(src, dest);
        assertEquals("aaa", dest.aaa);
        assertNull(dest.bbb);
    }

    /**
     * @throws Exception
     */
    public void testCopyBeanToBeanForExclude() throws Exception {
        MyBean src = new MyBean();
        src.aaa = "aaa";
        src.bbb = "bbb";
        MyBean dest = new MyBean();
        new MyCopy().exclude("bbb").copyBeanToBean(src, dest);
        assertEquals("aaa", dest.aaa);
        assertNull(dest.bbb);
    }

    /**
     * @throws Exception
     */
    public void testCopyBeanToBeanForNull() throws Exception {
        SrcBean src = new SrcBean();
        src.aaa = "aaa";
        src.bbb = "bbb";
        src.ccc = null;
        DestBean dest = new DestBean();
        dest.ccc = "ccc";
        new MyCopy().copyBeanToBean(src, dest);
        assertEquals("ccc", dest.ccc);
    }

    /**
     * @throws Exception
     */
    public void testCopyBeanToBeanForCopyNull() throws Exception {
        SrcBean src = new SrcBean();
        src.aaa = "aaa";
        src.bbb = "bbb";
        src.ccc = null;
        DestBean dest = new DestBean();
        dest.ccc = "ccc";
        new MyCopy().copyNull().copyBeanToBean(src, dest);
        assertNull(dest.ccc);
    }

    /**
     * @throws Exception
     */
    public void testCopyBeanToBeanForEmptyString() throws Exception {
        SrcBean src = new SrcBean();
        src.ccc = "";
        DestBean dest = new DestBean();
        dest.ccc = "ccc";
        new MyCopy().copyBeanToBean(src, dest);
        assertEquals("ccc", dest.ccc);
    }

    /**
     * @throws Exception
     */
    public void testCopyBeanToBeanForCopyEmptyString() throws Exception {
        SrcBean src = new SrcBean();
        src.ccc = "";
        DestBean dest = new DestBean();
        dest.ccc = "ccc";
        new MyCopy().copyEmptyString().copyBeanToBean(src, dest);
        assertEquals("", dest.ccc);
    }

    /**
     * @throws Exception
     */
    public void testCopyBeanToBeanForConverter() throws Exception {
        SrcBean srcBean = new SrcBean();
        srcBean.ggg = "1,000";
        DestBean destBean = new DestBean();
        new MyCopy().numberConverter("#,##0").copyBeanToBean(srcBean, destBean);
        assertEquals(new Integer(1000), destBean.ggg);
    }

    /**
     * @throws Exception
     */
    public void testCopyBeanToMap() throws Exception {
        SrcBean src = new SrcBean();
        src.aaa = "aaa";
        src.bbb = "bbb";
        src.ccc = "ccc";
        Map<String, Object> dest = new HashMap<String, Object>();
        new MyCopy().copyBeanToMap(src, dest);
        assertEquals("aaa", dest.get("aaa"));
        assertNull(dest.get("bbb"));
        assertEquals("ccc", dest.get("ccc"));
        assertNull(dest.get("ddd"));
    }

    /**
     * @throws Exception
     */
    public void testCopyBeanToMapForInclude() throws Exception {
        SrcBean src = new SrcBean();
        src.aaa = "aaa";
        src.bbb = "bbb";
        src.ccc = "ccc";
        Map<String, Object> dest = new HashMap<String, Object>();
        new MyCopy().include(BeanNames.aaa()).copyBeanToMap(src, dest);
        assertEquals("aaa", dest.get("aaa"));
        assertNull(dest.get("ccc"));
    }

    /**
     * @throws Exception
     */
    public void testCopyBeanToMapForExclude() throws Exception {
        SrcBean src = new SrcBean();
        src.aaa = "aaa";
        src.bbb = "bbb";
        src.ccc = "ccc";
        Map<String, Object> dest = new HashMap<String, Object>();
        new MyCopy().exclude(BeanNames.ccc()).copyBeanToMap(src, dest);
        assertEquals("aaa", dest.get("aaa"));
        assertNull(dest.get("ccc"));
    }

    /**
     * @throws Exception
     */
    public void testCopyBeanToMapForNull() throws Exception {
        SrcBean src = new SrcBean();
        src.aaa = "aaa";
        src.bbb = "bbb";
        src.ccc = null;
        BeanMap dest = new BeanMap();
        dest.put("ccc", "ccc");
        new MyCopy().copyBeanToMap(src, dest);
        assertNotNull(dest.get("ccc"));
    }

    /**
     * @throws Exception
     */
    public void testCopyBeanToMapForCopyNull() throws Exception {
        SrcBean src = new SrcBean();
        src.aaa = "aaa";
        src.bbb = "bbb";
        src.ccc = null;
        BeanMap dest = new BeanMap();
        dest.put("ccc", "ccc");
        new MyCopy().copyNull().copyBeanToMap(src, dest);
        assertNull(dest.get("ccc"));
    }

    /**
     * @throws Exception
     */
    public void testCopyBeanToMapForEmptyString() throws Exception {
        SrcBean src = new SrcBean();
        src.ccc = "";
        BeanMap dest = new BeanMap();
        dest.put("ccc", "ccc");
        new MyCopy().copyBeanToMap(src, dest);
        assertEquals("ccc", dest.get("ccc"));
    }

    /**
     * @throws Exception
     */
    public void testCopyBeanToMapForCopyEmptyString() throws Exception {
        SrcBean src = new SrcBean();
        src.ccc = "";
        BeanMap dest = new BeanMap();
        dest.put("ccc", "ccc");
        new MyCopy().copyEmptyString().copyBeanToMap(src, dest);
        assertEquals("", dest.get("ccc"));
    }

    /**
     * @throws Exception
     */
    public void testCopyBeanToMapForConverterToString() throws Exception {
        Bean bean = new Bean();
        bean.bbb = 1000;
        Map<String, Object> map = new HashMap<String, Object>();
        new MyCopy().numberConverter("#,##0").copyBeanToMap(bean, map);
        assertEquals("1,000", map.get("bbb"));
    }

    /**
     * @throws Exception
     */
    public void testCopyBeanToMapForConverterFromStringWithProperty()
            throws Exception {
        Bean bean = new Bean();
        bean.aaa = "1,000";
        BeanMap map = new BeanMap();
        new MyCopy().numberConverter("#,##0", BeanNames.aaa()).copyBeanToMap(
            bean,
            map);
        assertEquals(new Long(1000), map.get("aaa"));
    }

    /**
     * @throws Exception
     */
    public void testCopyBeanToMapForConverterFromStringWithoutProperty()
            throws Exception {
        Bean bean = new Bean();
        bean.aaa = "1,000";
        BeanMap map = new BeanMap();
        new MyCopy().numberConverter("#,##0").copyBeanToMap(bean, map);
        assertEquals("1,000", map.get("aaa"));
    }

    /**
     * @throws Exception
     */
    public void testCopyMapToBean() throws Exception {
        BeanMap src = new BeanMap();
        src.put("aaa", "aaa");
        src.put("bbb", "bbb");
        src.put("ccc", "ccc");
        DestBean dest = new DestBean();
        dest.ddd = "ddd";
        new MyCopy().copyMapToBean(src, dest);
        assertEquals("bbb", dest.bbb);
        assertEquals("ccc", dest.ccc);
        assertEquals("ddd", dest.ddd);
    }

    /**
     * @throws Exception
     */
    public void testCopyMapToBeanForConverterToString() throws Exception {
        BeanMap map = new BeanMap();
        map.put("aaa", new Integer(1000));
        Bean bean = new Bean();
        new MyCopy().numberConverter("#,##0").copyMapToBean(map, bean);
        assertEquals("1,000", bean.aaa);
    }

    /**
     * @throws Exception
     */
    public void testCopyMapToBeanForConverterFromString() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("bbb", "1,000");
        Bean bean = new Bean();
        new MyCopy().numberConverter("#,##0").copyMapToBean(map, bean);
        assertEquals(new Integer(1000), bean.bbb);
    }

    /**
     * @throws Exception
     */
    public void testCopyMapToBeanForInclude() throws Exception {
        Map<String, Object> src = new HashMap<String, Object>();
        src.put("aaa", "aaa");
        src.put("bbb", "bbb");
        src.put("ccc", "ccc");
        DestBean dest = new DestBean();
        new MyCopy().include(BeanNames.bbb()).copyMapToBean(src, dest);
        assertEquals("bbb", dest.bbb);
        assertNull(dest.ccc);
    }

    /**
     * @throws Exception
     */
    public void testCopyMapToBeanForExclude() throws Exception {
        Map<String, Object> src = new HashMap<String, Object>();
        src.put("aaa", "aaa");
        src.put("bbb", "bbb");
        src.put("ccc", "ccc");
        DestBean dest = new DestBean();
        new MyCopy().exclude(BeanNames.ccc()).copyMapToBean(src, dest);
        assertEquals("bbb", dest.bbb);
        assertNull(dest.ccc);
    }

    /**
     * @throws Exception
     */
    public void testCopyMapToBeanForNull() throws Exception {
        Map<String, Object> src = new HashMap<String, Object>();
        src.put("aaa", "aaa");
        src.put("bbb", "bbb");
        src.put("ccc", null);
        DestBean dest = new DestBean();
        dest.ccc = "ccc";
        new MyCopy().copyMapToBean(src, dest);
        assertEquals("ccc", dest.ccc);
    }

    /**
     * @throws Exception
     */
    public void testCopyMapToBeanForCopyNull() throws Exception {
        Map<String, Object> src = new HashMap<String, Object>();
        src.put("aaa", "aaa");
        src.put("bbb", "bbb");
        src.put("ccc", null);
        DestBean dest = new DestBean();
        dest.ccc = "ccc";
        new MyCopy().copyNull().copyMapToBean(src, dest);
        assertNull(dest.ccc);
    }

    /**
     * @throws Exception
     */
    public void testCopyMapToBeanForEmptyString() throws Exception {
        Map<String, Object> src = new HashMap<String, Object>();
        src.put("ccc", "");
        DestBean dest = new DestBean();
        dest.ccc = "ccc";
        new MyCopy().copyMapToBean(src, dest);
        assertEquals("ccc", dest.ccc);
    }

    /**
     * @throws Exception
     */
    public void testCopyMapToBeanForCopyEmptyString() throws Exception {
        Map<String, Object> src = new HashMap<String, Object>();
        src.put("ccc", "");
        DestBean dest = new DestBean();
        dest.ccc = "ccc";
        new MyCopy().copyEmptyString().copyMapToBean(src, dest);
        assertEquals("", dest.ccc);
    }

    /**
     * @throws Exception
     */
    public void testCopyMapToMap() throws Exception {
        Map<String, Object> src = new HashMap<String, Object>();
        src.put("aaa", "aaa");
        src.put("bbb", "bbb");
        Map<String, Object> dest = new HashMap<String, Object>();
        new MyCopy().copyMapToMap(src, dest);
        assertEquals("aaa", dest.get("aaa"));
        assertEquals("bbb", dest.get("bbb"));
    }

    /**
     * @throws Exception
     */
    public void testCopyMapToMapForInclude() throws Exception {
        Map<String, Object> src = new HashMap<String, Object>();
        src.put("aaa", "aaa");
        src.put("bbb", "bbb");
        Map<String, Object> dest = new HashMap<String, Object>();
        new MyCopy().include(BeanNames.aaa()).copyMapToMap(src, dest);
        assertEquals("aaa", dest.get("aaa"));
        assertNull(dest.get("bbb"));
    }

    /**
     * @throws Exception
     */
    public void testCopyMapToMapForExclude() throws Exception {
        Map<String, Object> src = new HashMap<String, Object>();
        src.put("aaa", "aaa");
        src.put("bbb", "bbb");
        Map<String, Object> dest = new HashMap<String, Object>();
        new MyCopy().exclude(BeanNames.bbb()).copyMapToMap(src, dest);
        assertEquals("aaa", dest.get("aaa"));
        assertNull(dest.get("bbb"));
    }

    /**
     * @throws Exception
     */
    public void testCopyMapToMapForNull() throws Exception {
        Map<String, Object> src = new HashMap<String, Object>();
        src.put("aaa", "aaa");
        src.put("bbb", null);
        Map<String, Object> dest = new HashMap<String, Object>();
        dest.put("bbb", "bbb");
        new MyCopy().copyMapToMap(src, dest);
        assertEquals("bbb", dest.get("bbb"));
    }

    /**
     * @throws Exception
     */
    public void testCopyMapToMapForCopyNull() throws Exception {
        Map<String, Object> src = new HashMap<String, Object>();
        src.put("aaa", "aaa");
        src.put("bbb", null);
        Map<String, Object> dest = new HashMap<String, Object>();
        dest.put("bbb", "bbb");
        new MyCopy().copyNull().copyMapToMap(src, dest);
        assertNull(dest.get("bbb"));
    }

    /**
     * @throws Exception
     */
    public void testCopyMapToMapForEmptyString() throws Exception {
        Map<String, Object> src = new HashMap<String, Object>();
        src.put("bbb", "");
        Map<String, Object> dest = new HashMap<String, Object>();
        dest.put("bbb", "bbb");
        new MyCopy().copyMapToMap(src, dest);
        assertEquals("bbb", dest.get("bbb"));
    }

    /**
     * @throws Exception
     */
    public void testCopyMapToMapForCopyEmptyString() throws Exception {
        Map<String, Object> src = new HashMap<String, Object>();
        src.put("bbb", "");
        Map<String, Object> dest = new HashMap<String, Object>();
        dest.put("bbb", "bbb");
        new MyCopy().copyEmptyString().copyMapToMap(src, dest);
        assertEquals("", dest.get("bbb"));
    }

    /**
     * @throws Exception
     */
    public void testCopyMapToMapForConverterToString() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("aaa", new Integer(1000));
        Map<String, Object> map2 = new HashMap<String, Object>();
        new MyCopy().numberConverter("#,##0").copyMapToMap(map, map2);
        assertEquals("1,000", map2.get("aaa"));
    }

    /**
     * @throws Exception
     */
    public void testCopyMapToMapForConverterFromStringWithoutProperty()
            throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("aaa", "1,000");
        Map<String, Object> map2 = new HashMap<String, Object>();
        new MyCopy().numberConverter("#,##0").copyMapToMap(map, map2);
        assertEquals("1,000", map2.get("aaa"));
    }

    /**
     * @throws Exception
     */
    public void testCopyMapToMapForConverterFromStringWithProperty()
            throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("aaa", "1,000");
        Map<String, Object> map2 = new HashMap<String, Object>();
        new MyCopy()
            .converter(new NumberConverter("#,##0"), "aaa")
            .copyMapToMap(map, map2);
        assertEquals(new Long(1000), map2.get("aaa"));
    }

    /**
     * @throws Exception
     */
    public void testConvertValueForZeroConverter() throws Exception {
        assertEquals(new Integer(1), new MyCopy().convertValue(
            new Integer(1),
            "aaa",
            null));
    }

    /**
     * @throws Exception
     */
    public void testConvertValueForPropertyConverterAsString() throws Exception {
        assertEquals("1", new MyCopy().converter(
            new NumberConverter("##0"),
            "aaa").convertValue(new Integer(1), "aaa", String.class));
    }

    /**
     * @throws Exception
     */
    public void testConvertValueForPropertyConverterAsObject() throws Exception {
        assertEquals(new Long(1), new MyCopy().converter(
            new NumberConverter("##0"),
            "aaa").convertValue("1", "aaa", Long.class));
    }

    /**
     * @throws Exception
     */
    public void testConvertValueForTypeConverterAsString() throws Exception {
        assertEquals("1", new MyCopy()
            .converter(new NumberConverter("##0"))
            .convertValue(new Integer(1), "aaa", String.class));
    }

    /**
     * @throws Exception
     */
    public void testConvertValueForTypeConverterAsObject() throws Exception {
        assertEquals(new Long(1), new MyCopy().converter(
            new NumberConverter("##0")).convertValue("1", "aaa", Integer.class));
        assertEquals("19700101", new MyCopy().converter(
            new DateConverter("yyyyMMdd")).convertValue(
            new Timestamp(0),
            "aaa",
            String.class));
    }

    /**
     * @throws Exception
     */
    public void testConvertValueForThrowable() throws Exception {
        try {
            new MyCopy().converter(new NumberConverter("##0")).convertValue(
                "a",
                "aaa",
                Integer.class);
        } catch (ConverterRuntimeException e) {
            System.out.println(e);
        }
    }

    /**
     * @throws Exception
     */
    public void testConvertValueForDefaultConverter() throws Exception {
        assertEquals(DateUtil.toDate("2009-04-07"), new MyCopy().convertValue(
            "2009-04-07",
            "aaa",
            Date.class));
    }

    /**
     * @throws Exception
     */
    public void testDateConverter() throws Exception {
        assertEquals("19700101", new MyCopy()
            .dateConverter("yyyyMMdd")
            .convertValue(new java.util.Date(0), "aaa", String.class));
    }

    /**
     * @throws Exception
     */
    public void testNumberConverter() throws Exception {
        assertEquals("1,000", new MyCopy()
            .numberConverter("0,000")
            .convertValue(new Integer(1000), "aaa", String.class));
    }

    /**
     * @throws Exception
     */
    public void testFindDefaultConverter() throws Exception {
        assertEquals(AbstractCopy.DEFAULT_DATE_CONVERTER, new MyCopy()
            .findDefaultConverter(java.util.Date.class));
        assertNull(new MyCopy().findDefaultConverter(Integer.class));
    }

    /**
     * 
     */
    private static class MyCopy extends AbstractCopy<MyCopy> {

    }

    /**
     * 
     */
    @SuppressWarnings("unused")
    public static class SrcBean {

        private String aaa;

        private String bbb;

        private String ccc;

        /**
         * 
         */
        public String ddd;

        /**
         * 
         */
        public String ggg;

        /**
         * @return the result
         */
        public String getAaa() {
            return aaa;
        }

        /**
         * @param aaa
         */
        public void setAaa(String aaa) {
            this.aaa = aaa;
        }

        /**
         * @param bbb
         */
        public void setBbb(String bbb) {
            this.bbb = bbb;
        }

        /**
         * @return the result
         */
        public String getCcc() {
            return ccc;
        }
    }

    /**
     * 
     */
    public static class DestBean {

        private String bbb;

        private String ccc;

        private String ddd;

        /**
         * 
         */
        public Integer ggg;

        /**
         * @param bbb
         */
        public void setBbb(String bbb) {
            this.bbb = bbb;
        }

        /**
         * @param ccc
         */
        public void setCcc(String ccc) {
            this.ccc = ccc;
        }

        /**
         * @return the result
         */
        public String getDdd() {
            return ddd;
        }

        /**
         * @param ddd
         */
        public void setDdd(String ddd) {
            this.ddd = ddd;
        }
    }

    /**
     * 
     */
    public static class Bean {

        /**
         * 
         */
        public String aaa;

        /**
         * 
         */
        public Integer bbb;
    }

    /**
     *
     */
    public static class BeanNames {

        /**
         * 
         * @param name
         * @return the result
         */
        protected static CharSequence createCharSequence(final String name) {
            return new CharSequence() {

                @Override
                public String toString() {
                    return name;
                }

                public char charAt(int index) {
                    return name.charAt(index);
                }

                public int length() {
                    return name.length();
                }

                public CharSequence subSequence(int start, int end) {
                    return name.subSequence(start, end);
                }

            };
        }

        /**
         * @return the result
         */
        public static CharSequence aaa() {
            return createCharSequence("aaa");
        }

        /**
         * @return the result
         */
        public static CharSequence bbb() {
            return createCharSequence("bbb");
        }

        /**
         * @return the result
         */
        public static CharSequence ccc() {
            return createCharSequence("ccc");
        }
    }
}
