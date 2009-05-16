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
package org.slim3.util;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

/**
 * @author higa
 * 
 */
public class BeanUtilTest extends TestCase {

    /**
     * @throws Exception
     */
    public void testGetBeanDesc() throws Exception {
        BeanDesc beanDesc = BeanUtil.getBeanDesc(getClass());
        assertNotNull(beanDesc);
        assertSame(beanDesc, BeanUtil.getBeanDesc(getClass()));
    }

    /**
     * @throws Exception
     */
    public void testCopyBB() throws Exception {
        SrcBB src = new SrcBB();
        src.aaa = "111";
        DestBB dest = new DestBB();
        BeanUtil.copy(src, dest);
        assertEquals("111", dest.aaa);
    }

    /**
     * @throws Exception
     */
    public void testBBCopyFromReadOnlyToWriteOnly() throws Exception {
        SrcBB src = new SrcBB();
        src.bbb = "111";
        DestBB dest = new DestBB();
        BeanUtil.copy(src, dest);
        assertEquals("111", dest.bbb);
    }

    /**
     * @throws Exception
     */
    public void testCopyBBFromReadOnlyToReadOnly() throws Exception {
        SrcBB src = new SrcBB();
        src.ccc = "111";
        DestBB dest = new DestBB();
        BeanUtil.copy(src, dest);
        assertNull(dest.ccc);
    }

    /**
     * @throws Exception
     */
    public void testCopyBBFromWriteOnlyToWriteOnly() throws Exception {
        SrcBB src = new SrcBB();
        src.ddd = "111";
        DestBB dest = new DestBB();
        BeanUtil.copy(src, dest);
        assertNull(dest.ddd);
    }

    /**
     * @throws Exception
     */
    public void testCopyBBFromWriteOnlyToReadOnly() throws Exception {
        SrcBB src = new SrcBB();
        src.eee = "111";
        DestBB dest = new DestBB();
        BeanUtil.copy(src, dest);
        assertNull(dest.eee);
    }

    /**
     * @throws Exception
     */
    public void testCopyBBForInclude() throws Exception {
        SrcBB src = new SrcBB();
        src.aaa = "111";
        src.bbb = "222";
        DestBB dest = new DestBB();
        BeanUtil.copy(src, dest, new CopyOptions().include("aaa"));
        assertEquals("111", dest.aaa);
        assertNull(dest.bbb);
    }

    /**
     * @throws Exception
     */
    public void testCopyBBForExclude() throws Exception {
        SrcBB src = new SrcBB();
        src.aaa = "111";
        src.bbb = "222";
        DestBB dest = new DestBB();
        BeanUtil.copy(src, dest, new CopyOptions().exclude("bbb"));
        assertEquals("111", dest.aaa);
        assertNull(dest.bbb);
    }

    /**
     * @throws Exception
     */
    public void testCopyBBForNull() throws Exception {
        SrcBB src = new SrcBB();
        DestBB dest = new DestBB();
        dest.aaa = "111";
        BeanUtil.copy(src, dest);
        assertEquals("111", dest.aaa);
    }

    /**
     * @throws Exception
     */
    public void testCopyBBForCopyNull() throws Exception {
        SrcBB src = new SrcBB();
        DestBB dest = new DestBB();
        dest.aaa = "111";
        BeanUtil.copy(src, dest, new CopyOptions().copyNull());
        assertNull(dest.aaa);
    }

    /**
     * @throws Exception
     */
    public void testCopyBBForEmptyString() throws Exception {
        SrcBB src = new SrcBB();
        src.aaa = "";
        DestBB dest = new DestBB();
        dest.aaa = "111";
        BeanUtil.copy(src, dest);
        assertEquals("111", dest.aaa);
    }

    /**
     * @throws Exception
     */
    public void testCopyBBForCopyEmptyString() throws Exception {
        SrcBB src = new SrcBB();
        src.aaa = "";
        DestBB dest = new DestBB();
        dest.aaa = "111";
        BeanUtil.copy(src, dest, new CopyOptions().copyEmptyString());
        assertEquals("", dest.aaa);
    }

    /**
     * @throws Exception
     */
    public void testCopyBBForConverter() throws Exception {
        SrcBB src = new SrcBB();
        src.fff = "1,000";
        DestBB dest = new DestBB();
        BeanUtil.copy(src, dest, new CopyOptions().numberConverter("#,##0"));
        assertEquals(new Integer(1000), dest.fff);
    }

    /**
     * @throws Exception
     */
    public void testCopyBM() throws Exception {
        SrcBM src = new SrcBM();
        src.aaa = "111";
        Map<String, Object> dest = new HashMap<String, Object>();
        BeanUtil.copy(src, dest);
        assertEquals("111", dest.get("aaa"));
    }

    /**
     * @throws Exception
     */
    public void testCopyBMFromReadOnly() throws Exception {
        SrcBM src = new SrcBM();
        src.bbb = "111";
        Map<String, Object> dest = new HashMap<String, Object>();
        BeanUtil.copy(src, dest);
        assertEquals("111", dest.get("bbb"));
    }

    /**
     * @throws Exception
     */
    public void testCopyBMFromWriteOnly() throws Exception {
        SrcBM src = new SrcBM();
        src.ccc = "111";
        Map<String, Object> dest = new HashMap<String, Object>();
        BeanUtil.copy(src, dest);
        assertNull(dest.get("ccc"));
    }

    /**
     * @throws Exception
     */
    public void testCopyBMForInclude() throws Exception {
        SrcBM src = new SrcBM();
        src.aaa = "111";
        src.bbb = "222";
        Map<String, Object> dest = new HashMap<String, Object>();
        BeanUtil.copy(src, dest, new CopyOptions().include("aaa"));
        assertEquals("111", dest.get("aaa"));
        assertNull(dest.get("bbb"));
    }

    /**
     * @throws Exception
     */
    public void testCopyBMForExclude() throws Exception {
        SrcBM src = new SrcBM();
        src.aaa = "111";
        src.bbb = "222";
        Map<String, Object> dest = new HashMap<String, Object>();
        BeanUtil.copy(src, dest, new CopyOptions().exclude("bbb"));
        assertEquals("111", dest.get("aaa"));
        assertNull(dest.get("bbb"));
    }

    /**
     * @throws Exception
     */
    public void testCopyBMForNull() throws Exception {
        SrcBM src = new SrcBM();
        Map<String, Object> dest = new HashMap<String, Object>();
        dest.put("aaa", "111");
        BeanUtil.copy(src, dest);
        assertEquals("111", dest.get("aaa"));
    }

    /**
     * @throws Exception
     */
    public void testCopyBMForCopyNull() throws Exception {
        SrcBM src = new SrcBM();
        Map<String, Object> dest = new HashMap<String, Object>();
        dest.put("aaa", "111");
        BeanUtil.copy(src, dest, new CopyOptions().copyNull());
        assertNull(dest.get("aaa"));
    }

    /**
     * @throws Exception
     */
    public void testCopyBMForEmptyString() throws Exception {
        SrcBM src = new SrcBM();
        src.aaa = "";
        Map<String, Object> dest = new HashMap<String, Object>();
        dest.put("aaa", "111");
        BeanUtil.copy(src, dest);
        assertEquals("111", dest.get("aaa"));
    }

    /**
     * @throws Exception
     */
    public void testCopyBMForCopyEmptyString() throws Exception {
        SrcBM src = new SrcBM();
        src.aaa = "";
        Map<String, Object> dest = new HashMap<String, Object>();
        dest.put("aaa", "111");
        BeanUtil.copy(src, dest, new CopyOptions().copyEmptyString());
        assertEquals("", dest.get("aaa"));
    }

    /**
     * @throws Exception
     */
    public void testCopyBMForConverter() throws Exception {
        SrcBM src = new SrcBM();
        src.aaa = "1,000";
        Map<String, Object> dest = new HashMap<String, Object>();
        BeanUtil.copy(src, dest, new CopyOptions().numberConverter(
            "#,##0",
            "aaa"));
        assertEquals(new Long(1000), dest.get("aaa"));
    }

    /**
     * @throws Exception
     */
    public void testCopyMB() throws Exception {
        Map<String, Object> src = new HashMap<String, Object>();
        src.put("aaa", "111");
        DestMB dest = new DestMB();
        BeanUtil.copy(src, dest);
        assertEquals("111", dest.aaa);
    }

    /**
     * @throws Exception
     */
    public void testCopyMBToReadOnly() throws Exception {
        Map<String, Object> src = new HashMap<String, Object>();
        src.put("bbb", "111");
        DestMB dest = new DestMB();
        BeanUtil.copy(src, dest);
        assertNull(dest.bbb);
    }

    /**
     * @throws Exception
     */
    public void testCopyMBToWriteOnly() throws Exception {
        Map<String, Object> src = new HashMap<String, Object>();
        src.put("ccc", "111");
        DestMB dest = new DestMB();
        BeanUtil.copy(src, dest);
        assertEquals("111", dest.ccc);
    }

    /**
     * @throws Exception
     */
    public void testCopyMBForInclude() throws Exception {
        Map<String, Object> src = new HashMap<String, Object>();
        src.put("aaa", "111");
        src.put("ccc", "222");
        DestMB dest = new DestMB();
        BeanUtil.copy(src, dest, new CopyOptions().include("aaa"));
        assertEquals("111", dest.aaa);
        assertNull(dest.ccc);
    }

    /**
     * @throws Exception
     */
    public void testCopyMBForExclude() throws Exception {
        Map<String, Object> src = new HashMap<String, Object>();
        src.put("aaa", "111");
        src.put("ccc", "222");
        DestMB dest = new DestMB();
        BeanUtil.copy(src, dest, new CopyOptions().exclude("ccc"));
        assertEquals("111", dest.aaa);
        assertNull(dest.ccc);
    }

    /**
     * @throws Exception
     */
    public void testCopyMBForNull() throws Exception {
        Map<String, Object> src = new HashMap<String, Object>();
        DestMB dest = new DestMB();
        dest.aaa = "111";
        BeanUtil.copy(src, dest);
        assertEquals("111", dest.aaa);
    }

    /**
     * @throws Exception
     */
    public void testCopyMBForCopyNull() throws Exception {
        Map<String, Object> src = new HashMap<String, Object>();
        src.put("aaa", null);
        DestMB dest = new DestMB();
        dest.aaa = "111";
        BeanUtil.copy(src, dest, new CopyOptions().copyNull());
        assertNull(dest.aaa);
    }

    /**
     * @throws Exception
     */
    public void testCopyMBForEmptyString() throws Exception {
        Map<String, Object> src = new HashMap<String, Object>();
        src.put("aaa", "");
        DestMB dest = new DestMB();
        dest.aaa = "111";
        BeanUtil.copy(src, dest);
        assertEquals("111", dest.aaa);
    }

    /**
     * @throws Exception
     */
    public void testCopyMBForCopyEmptyString() throws Exception {
        Map<String, Object> src = new HashMap<String, Object>();
        src.put("aaa", "");
        DestMB dest = new DestMB();
        dest.aaa = "111";
        BeanUtil.copy(src, dest, new CopyOptions().copyEmptyString());
        assertEquals("", dest.aaa);
    }

    /**
     * @throws Exception
     */
    public void testCopyMBForConverter() throws Exception {
        Map<String, Object> src = new HashMap<String, Object>();
        src.put("ddd", "1,000");
        DestMB dest = new DestMB();
        BeanUtil.copy(src, dest, new CopyOptions().numberConverter("#,##0"));
        assertEquals(new Integer(1000), dest.ddd);
    }

    /**
     * @throws Exception
     */
    public void testCopyMM() throws Exception {
        Map<String, Object> src = new HashMap<String, Object>();
        src.put("aaa", "111");
        Map<String, Object> dest = new HashMap<String, Object>();
        BeanUtil.copy(src, dest);
        assertEquals("111", dest.get("aaa"));
    }

    /**
     * @throws Exception
     */
    public void testCopyMMForInclude() throws Exception {
        Map<String, Object> src = new HashMap<String, Object>();
        src.put("aaa", "111");
        src.put("bbb", "222");
        Map<String, Object> dest = new HashMap<String, Object>();
        BeanUtil.copy(src, dest, new CopyOptions().include("aaa"));
        assertEquals("111", dest.get("aaa"));
        assertNull(dest.get("bbb"));
    }

    /**
     * @throws Exception
     */
    public void testCopyMMForExclude() throws Exception {
        Map<String, Object> src = new HashMap<String, Object>();
        src.put("aaa", "111");
        src.put("bbb", "222");
        Map<String, Object> dest = new HashMap<String, Object>();
        BeanUtil.copy(src, dest, new CopyOptions().exclude("bbb"));
        assertEquals("111", dest.get("aaa"));
        assertNull(dest.get("bbb"));
    }

    /**
     * @throws Exception
     */
    public void testCopyMMForNull() throws Exception {
        Map<String, Object> src = new HashMap<String, Object>();
        src.put("aaa", null);
        Map<String, Object> dest = new HashMap<String, Object>();
        dest.put("aaa", "111");
        BeanUtil.copy(src, dest);
        assertEquals("111", dest.get("aaa"));
    }

    /**
     * @throws Exception
     */
    public void testCopyMMForCopyNull() throws Exception {
        Map<String, Object> src = new HashMap<String, Object>();
        src.put("aaa", null);
        Map<String, Object> dest = new HashMap<String, Object>();
        dest.put("aaa", "111");
        BeanUtil.copy(src, dest, new CopyOptions().copyNull());
        assertNull(dest.get("aaa"));
    }

    /**
     * @throws Exception
     */
    public void testCopyMMForEmptyString() throws Exception {
        Map<String, Object> src = new HashMap<String, Object>();
        src.put("aaa", "");
        Map<String, Object> dest = new HashMap<String, Object>();
        dest.put("aaa", "111");
        BeanUtil.copy(src, dest);
        assertEquals("111", dest.get("aaa"));
    }

    /**
     * @throws Exception
     */
    public void testCopyMMForCopyEmptyString() throws Exception {
        Map<String, Object> src = new HashMap<String, Object>();
        src.put("aaa", "");
        Map<String, Object> dest = new HashMap<String, Object>();
        dest.put("aaa", "111");
        BeanUtil.copy(src, dest, new CopyOptions().copyEmptyString());
        assertEquals("", dest.get("aaa"));
    }

    /**
     * @throws Exception
     */
    public void testCopyMMForConverter() throws Exception {
        Map<String, Object> src = new HashMap<String, Object>();
        src.put("aaa", "1,000");
        Map<String, Object> dest = new HashMap<String, Object>();
        BeanUtil.copy(src, dest, new CopyOptions().numberConverter(
            "#,##0",
            "aaa"));
        assertEquals(new Long(1000), dest.get("aaa"));
    }

    /**
     * 
     */
    private static class SrcBB {

        private String aaa;

        private String bbb;

        private String ccc;

        @SuppressWarnings("unused")
        private String ddd;

        @SuppressWarnings("unused")
        private String eee;

        private String fff;

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
         * @return the bbb
         */
        public String getBbb() {
            return bbb;
        }

        /**
         * @return the ccc
         */
        public String getCcc() {
            return ccc;
        }

        /**
         * @param ddd
         *            the ddd to set
         */
        public void setDdd(String ddd) {
            this.ddd = ddd;
        }

        /**
         * @param eee
         *            the eee to set
         */
        public void setEee(String eee) {
            this.eee = eee;
        }

        /**
         * @return the fff
         */
        public String getFff() {
            return fff;
        }

        /**
         * @param fff
         *            the fff to set
         */
        public void setFff(String fff) {
            this.fff = fff;
        }
    }

    /**
     * 
     */
    private static class DestBB {

        private String aaa;

        private String bbb;

        private String ccc;

        private String ddd;

        private String eee;

        private Integer fff;

        private String ggg;

        /**
         * @return the aaa
         */
        public String getAaa() {
            return aaa;
        }

        /**
         * @param aaa
         *            the aaa to set
         */
        public void setAaa(String aaa) {
            this.aaa = aaa;
        }

        /**
         * @param bbb
         *            the bbb to set
         */
        public void setBbb(String bbb) {
            this.bbb = bbb;
        }

        /**
         * @return the ccc
         */
        public String getCcc() {
            return ccc;
        }

        /**
         * @param ddd
         *            the ddd to set
         */
        public void setDdd(String ddd) {
            this.ddd = ddd;
        }

        /**
         * @return the eee
         */
        public String getEee() {
            return eee;
        }

        /**
         * @return the fff
         */
        public Integer getFff() {
            return fff;
        }

        /**
         * @param fff
         *            the fff to set
         */
        public void setFff(Integer fff) {
            this.fff = fff;
        }

        /**
         * @return the ggg
         */
        public String getGgg() {
            return ggg;
        }

        /**
         * @param ggg
         *            the ggg to set
         */
        public void setGgg(String ggg) {
            this.ggg = ggg;
        }
    }

    /**
     * 
     */
    private static class SrcBM {

        private String aaa;

        private String bbb;

        @SuppressWarnings("unused")
        private String ccc;

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
         * @return the bbb
         */
        public String getBbb() {
            return bbb;
        }

        /**
         * @param ccc
         *            the ccc to set
         */
        public void setCcc(String ccc) {
            this.ccc = ccc;
        }
    }

    /**
     * 
     */
    private static class DestMB {

        private String aaa;

        private String bbb;

        private String ccc;

        private Integer ddd;

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
         * @return the bbb
         */
        public String getBbb() {
            return bbb;
        }

        /**
         * @param ccc
         *            the ccc to set
         */
        public void setCcc(String ccc) {
            this.ccc = ccc;
        }

        /**
         * @return the ddd
         */
        public Integer getDdd() {
            return ddd;
        }

        /**
         * @param ddd
         *            the ddd to set
         */
        public void setDdd(Integer ddd) {
            this.ddd = ddd;
        }
    }
}