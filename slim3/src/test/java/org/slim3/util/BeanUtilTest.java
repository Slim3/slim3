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
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.slim3.tester.MockHttpServletRequest;
import org.slim3.tester.MockServletContext;

/**
 * @author higa
 * 
 */
public class BeanUtilTest {

    /**
     * @throws Exception
     */
    @Test
    public void getBeanDesc() throws Exception {
        BeanDesc beanDesc = BeanUtil.getBeanDesc(getClass());
        assertThat(beanDesc, is(notNullValue()));
        assertThat(BeanUtil.getBeanDesc(getClass()), is(sameInstance(beanDesc)));
    }

    /**
     * @throws Exception
     */
    @Test
    public void copyBB() throws Exception {
        SrcBB src = new SrcBB();
        src.aaa = "111";
        DestBB dest = new DestBB();
        BeanUtil.copy(src, dest);
        assertThat(dest.aaa, is("111"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void copyBBFromReadOnlyToWriteOnly() throws Exception {
        SrcBB src = new SrcBB();
        src.bbb = "111";
        DestBB dest = new DestBB();
        BeanUtil.copy(src, dest);
        assertThat(dest.bbb, is("111"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void copyBBFromReadOnlyToReadOnly() throws Exception {
        SrcBB src = new SrcBB();
        src.ccc = "111";
        DestBB dest = new DestBB();
        BeanUtil.copy(src, dest);
        assertThat(dest.ccc, is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void copyBBFromWriteOnlyToWriteOnly() throws Exception {
        SrcBB src = new SrcBB();
        src.ddd = "111";
        DestBB dest = new DestBB();
        BeanUtil.copy(src, dest);
        assertThat(dest.ddd, is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void copyBBFromWriteOnlyToReadOnly() throws Exception {
        SrcBB src = new SrcBB();
        src.eee = "111";
        DestBB dest = new DestBB();
        BeanUtil.copy(src, dest);
        assertThat(dest.eee, is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void copyBBForInclude() throws Exception {
        SrcBB src = new SrcBB();
        src.aaa = "111";
        src.bbb = "222";
        DestBB dest = new DestBB();
        BeanUtil.copy(src, dest, new CopyOptions().include("aaa"));
        assertThat(dest.aaa, is("111"));
        assertThat(dest.bbb, is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void copyBBForExclude() throws Exception {
        SrcBB src = new SrcBB();
        src.aaa = "111";
        src.bbb = "222";
        DestBB dest = new DestBB();
        BeanUtil.copy(src, dest, new CopyOptions().exclude("bbb"));
        assertThat(dest.aaa, is("111"));
        assertThat(dest.bbb, is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void copyBBForNull() throws Exception {
        SrcBB src = new SrcBB();
        DestBB dest = new DestBB();
        dest.aaa = "111";
        BeanUtil.copy(src, dest);
        assertThat(dest.aaa, is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void copyBBForExcludeNull() throws Exception {
        SrcBB src = new SrcBB();
        DestBB dest = new DestBB();
        dest.aaa = "111";
        BeanUtil.copy(src, dest, new CopyOptions().excludeNull());
        assertThat(dest.aaa, is("111"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void copyBBForEmptyString() throws Exception {
        SrcBB src = new SrcBB();
        src.aaa = "";
        DestBB dest = new DestBB();
        dest.aaa = "111";
        BeanUtil.copy(src, dest);
        assertThat(dest.aaa, is(""));
    }

    /**
     * @throws Exception
     */
    @Test
    public void copyBBForExcludeEmptyString() throws Exception {
        SrcBB src = new SrcBB();
        src.aaa = "";
        DestBB dest = new DestBB();
        dest.aaa = "111";
        BeanUtil.copy(src, dest, new CopyOptions().excludeEmptyString());
        assertThat(dest.aaa, is("111"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void copyBBForConverter() throws Exception {
        SrcBB src = new SrcBB();
        src.fff = "1,000";
        DestBB dest = new DestBB();
        BeanUtil.copy(src, dest, new CopyOptions().numberConverter("#,##0"));
        assertThat(dest.fff, is(1000));
    }

    /**
     * @throws Exception
     */
    @Test
    public void copyBM() throws Exception {
        SrcBM src = new SrcBM();
        src.aaa = "111";
        Map<String, Object> dest = new HashMap<String, Object>();
        BeanUtil.copy(src, dest);
        assertThat((String) dest.get("aaa"), is("111"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void copyBMFromReadOnly() throws Exception {
        SrcBM src = new SrcBM();
        src.bbb = "111";
        Map<String, Object> dest = new HashMap<String, Object>();
        BeanUtil.copy(src, dest);
        assertThat((String) dest.get("bbb"), is("111"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void copyBMFromWriteOnly() throws Exception {
        SrcBM src = new SrcBM();
        src.ccc = "111";
        Map<String, Object> dest = new HashMap<String, Object>();
        BeanUtil.copy(src, dest);
        assertThat(dest.get("ccc"), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void copyBMForInclude() throws Exception {
        SrcBM src = new SrcBM();
        src.aaa = "111";
        src.bbb = "222";
        Map<String, Object> dest = new HashMap<String, Object>();
        BeanUtil.copy(src, dest, new CopyOptions().include("aaa"));
        assertThat((String) dest.get("aaa"), is("111"));
        assertThat(dest.get("bbb"), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void copyBMForExclude() throws Exception {
        SrcBM src = new SrcBM();
        src.aaa = "111";
        src.bbb = "222";
        Map<String, Object> dest = new HashMap<String, Object>();
        BeanUtil.copy(src, dest, new CopyOptions().exclude("bbb"));
        assertThat((String) dest.get("aaa"), is("111"));
        assertThat(dest.get("bbb"), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void copyBMForNull() throws Exception {
        SrcBM src = new SrcBM();
        Map<String, Object> dest = new HashMap<String, Object>();
        dest.put("aaa", "111");
        BeanUtil.copy(src, dest);
        assertThat(dest.get("aaa"), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void copyBMForExcludeNull() throws Exception {
        SrcBM src = new SrcBM();
        Map<String, Object> dest = new HashMap<String, Object>();
        dest.put("aaa", "111");
        BeanUtil.copy(src, dest, new CopyOptions().excludeNull());
        assertThat((String) dest.get("aaa"), is("111"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void copyBMForEmptyString() throws Exception {
        SrcBM src = new SrcBM();
        src.aaa = "";
        Map<String, Object> dest = new HashMap<String, Object>();
        dest.put("aaa", "111");
        BeanUtil.copy(src, dest);
        assertThat((String) dest.get("aaa"), is(""));
    }

    /**
     * @throws Exception
     */
    @Test
    public void copyBMForExcludeEmptyString() throws Exception {
        SrcBM src = new SrcBM();
        src.aaa = "";
        Map<String, Object> dest = new HashMap<String, Object>();
        dest.put("aaa", "111");
        BeanUtil.copy(src, dest, new CopyOptions().excludeEmptyString());
        assertThat((String) dest.get("aaa"), is("111"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void copyBMForConverter() throws Exception {
        SrcBM src = new SrcBM();
        src.aaa = "1,000";
        Map<String, Object> dest = new HashMap<String, Object>();
        BeanUtil.copy(src, dest, new CopyOptions().numberConverter(
            "#,##0",
            "aaa"));
        assertThat((Long) dest.get("aaa"), is(1000L));
    }

    /**
     * @throws Exception
     */
    @Test
    public void copyMB() throws Exception {
        Map<String, Object> src = new HashMap<String, Object>();
        src.put("aaa", "111");
        DestMB dest = new DestMB();
        BeanUtil.copy(src, dest);
        assertThat(dest.aaa, is("111"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void copyMBToReadOnly() throws Exception {
        Map<String, Object> src = new HashMap<String, Object>();
        src.put("bbb", "111");
        DestMB dest = new DestMB();
        BeanUtil.copy(src, dest);
        assertThat(dest.bbb, is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void copyMBToWriteOnly() throws Exception {
        Map<String, Object> src = new HashMap<String, Object>();
        src.put("ccc", "111");
        DestMB dest = new DestMB();
        BeanUtil.copy(src, dest);
        assertThat(dest.ccc, is("111"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void copyMBForInclude() throws Exception {
        Map<String, Object> src = new HashMap<String, Object>();
        src.put("aaa", "111");
        src.put("ccc", "222");
        DestMB dest = new DestMB();
        BeanUtil.copy(src, dest, new CopyOptions().include("aaa"));
        assertThat(dest.aaa, is("111"));
        assertThat(dest.ccc, is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void copyMBForExclude() throws Exception {
        Map<String, Object> src = new HashMap<String, Object>();
        src.put("aaa", "111");
        src.put("ccc", "222");
        DestMB dest = new DestMB();
        BeanUtil.copy(src, dest, new CopyOptions().exclude("ccc"));
        assertThat(dest.aaa, is("111"));
        assertThat(dest.ccc, is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void copyMBForNull() throws Exception {
        Map<String, Object> src = new HashMap<String, Object>();
        DestMB dest = new DestMB();
        dest.aaa = "111";
        BeanUtil.copy(src, dest);
        assertThat(dest.aaa, is("111"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void copyMBForExcludeNull() throws Exception {
        Map<String, Object> src = new HashMap<String, Object>();
        src.put("aaa", null);
        DestMB dest = new DestMB();
        dest.aaa = "111";
        BeanUtil.copy(src, dest, new CopyOptions().excludeNull());
        assertThat(dest.aaa, is("111"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void copyMBForEmptyString() throws Exception {
        Map<String, Object> src = new HashMap<String, Object>();
        src.put("aaa", "");
        DestMB dest = new DestMB();
        dest.aaa = "111";
        BeanUtil.copy(src, dest);
        assertThat(dest.aaa, is(""));
    }

    /**
     * @throws Exception
     */
    @Test
    public void copyMBForExcludeEmptyString() throws Exception {
        Map<String, Object> src = new HashMap<String, Object>();
        src.put("aaa", "");
        DestMB dest = new DestMB();
        dest.aaa = "111";
        BeanUtil.copy(src, dest, new CopyOptions().excludeEmptyString());
        assertThat(dest.aaa, is("111"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void copyMBForConverter() throws Exception {
        Map<String, Object> src = new HashMap<String, Object>();
        src.put("ddd", "1,000");
        DestMB dest = new DestMB();
        BeanUtil.copy(src, dest, new CopyOptions().numberConverter("#,##0"));
        assertThat(dest.ddd, is(1000));
    }

    /**
     * @throws Exception
     */
    @Test
    public void copyMM() throws Exception {
        Map<String, Object> src = new HashMap<String, Object>();
        src.put("aaa", "111");
        Date date = new Date();
        src.put("bbb", date);
        Map<String, Object> dest = new HashMap<String, Object>();
        BeanUtil.copy(src, dest);
        assertThat((String) dest.get("aaa"), is("111"));
        assertThat((Date) dest.get("bbb"), is(date));
    }

    /**
     * @throws Exception
     */
    @Test
    public void copyMMForInclude() throws Exception {
        Map<String, Object> src = new HashMap<String, Object>();
        src.put("aaa", "111");
        src.put("bbb", "222");
        Map<String, Object> dest = new HashMap<String, Object>();
        BeanUtil.copy(src, dest, new CopyOptions().include("aaa"));
        assertThat((String) dest.get("aaa"), is("111"));
        assertThat(dest.get("bbb"), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void copyMMForExclude() throws Exception {
        Map<String, Object> src = new HashMap<String, Object>();
        src.put("aaa", "111");
        src.put("bbb", "222");
        Map<String, Object> dest = new HashMap<String, Object>();
        BeanUtil.copy(src, dest, new CopyOptions().exclude("bbb"));
        assertThat((String) dest.get("aaa"), is("111"));
        assertThat(dest.get("bbb"), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void copyMMForNull() throws Exception {
        Map<String, Object> src = new HashMap<String, Object>();
        src.put("aaa", null);
        Map<String, Object> dest = new HashMap<String, Object>();
        dest.put("aaa", "111");
        BeanUtil.copy(src, dest);
        assertThat(dest.get("aaa"), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void copyMMForExcludeNull() throws Exception {
        Map<String, Object> src = new HashMap<String, Object>();
        src.put("aaa", null);
        Map<String, Object> dest = new HashMap<String, Object>();
        dest.put("aaa", "111");
        BeanUtil.copy(src, dest, new CopyOptions().excludeNull());
        assertThat((String) dest.get("aaa"), is("111"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void copyMMForEmptyString() throws Exception {
        Map<String, Object> src = new HashMap<String, Object>();
        src.put("aaa", "");
        Map<String, Object> dest = new HashMap<String, Object>();
        dest.put("aaa", "111");
        BeanUtil.copy(src, dest);
        assertThat((String) dest.get("aaa"), is(""));
    }

    /**
     * @throws Exception
     */
    @Test
    public void copyMMForExcludeEmptyString() throws Exception {
        Map<String, Object> src = new HashMap<String, Object>();
        src.put("aaa", "");
        Map<String, Object> dest = new HashMap<String, Object>();
        dest.put("aaa", "111");
        BeanUtil.copy(src, dest, new CopyOptions().excludeEmptyString());
        assertThat((String) dest.get("aaa"), is("111"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void copyMMForConverter() throws Exception {
        Map<String, Object> src = new HashMap<String, Object>();
        src.put("aaa", "1,000");
        Map<String, Object> dest = new HashMap<String, Object>();
        BeanUtil.copy(src, dest, new CopyOptions().numberConverter(
            "#,##0",
            "aaa"));
        assertThat((Long) dest.get("aaa"), is(1000L));
    }

    /**
     * @throws Exception
     */
    @Test
    public void copyRB() throws Exception {
        MockServletContext servletContext = new MockServletContext();
        MockHttpServletRequest src = new MockHttpServletRequest(servletContext);
        src.setAttribute("aaa", "111");
        DestRB dest = new DestRB();
        BeanUtil.copy(src, dest);
        assertThat(dest.aaa, is("111"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void copyRBToReadOnly() throws Exception {
        MockServletContext servletContext = new MockServletContext();
        MockHttpServletRequest src = new MockHttpServletRequest(servletContext);
        src.setAttribute("bbb", "111");
        DestRB dest = new DestRB();
        BeanUtil.copy(src, dest);
        assertThat(dest.bbb, is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void copyRBToWriteOnly() throws Exception {
        MockServletContext servletContext = new MockServletContext();
        MockHttpServletRequest src = new MockHttpServletRequest(servletContext);
        src.setAttribute("ccc", "111");
        DestRB dest = new DestRB();
        BeanUtil.copy(src, dest);
        assertThat(dest.ccc, is("111"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void copyRBForInclude() throws Exception {
        MockServletContext servletContext = new MockServletContext();
        MockHttpServletRequest src = new MockHttpServletRequest(servletContext);
        src.setAttribute("aaa", "111");
        src.setAttribute("ccc", "222");
        DestRB dest = new DestRB();
        BeanUtil.copy(src, dest, new CopyOptions().include("aaa"));
        assertThat(dest.aaa, is("111"));
        assertThat(dest.ccc, is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void copyRBForExclude() throws Exception {
        MockServletContext servletContext = new MockServletContext();
        MockHttpServletRequest src = new MockHttpServletRequest(servletContext);
        src.setAttribute("aaa", "111");
        src.setAttribute("ccc", "222");
        DestRB dest = new DestRB();
        BeanUtil.copy(src, dest, new CopyOptions().exclude("ccc"));
        assertThat(dest.aaa, is("111"));
        assertThat(dest.ccc, is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void copyRBForNull() throws Exception {
        MockServletContext servletContext = new MockServletContext();
        MockHttpServletRequest src = new MockHttpServletRequest(servletContext);
        DestRB dest = new DestRB();
        dest.aaa = "111";
        BeanUtil.copy(src, dest);
        assertThat(dest.aaa, is("111"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void copyRBForExcludeNull() throws Exception {
        MockServletContext servletContext = new MockServletContext();
        MockHttpServletRequest src = new MockHttpServletRequest(servletContext);
        src.setAttribute("aaa", null);
        DestRB dest = new DestRB();
        dest.aaa = "111";
        BeanUtil.copy(src, dest, new CopyOptions().excludeNull());
        assertThat(dest.aaa, is("111"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void copyRBForEmptyString() throws Exception {
        MockServletContext servletContext = new MockServletContext();
        MockHttpServletRequest src = new MockHttpServletRequest(servletContext);
        src.setAttribute("aaa", "");
        DestRB dest = new DestRB();
        dest.aaa = "111";
        BeanUtil.copy(src, dest);
        assertThat(dest.aaa, is(""));
    }

    /**
     * @throws Exception
     */
    @Test
    public void copyRBForExcludeEmptyString() throws Exception {
        MockServletContext servletContext = new MockServletContext();
        MockHttpServletRequest src = new MockHttpServletRequest(servletContext);
        src.setAttribute("aaa", "");
        DestRB dest = new DestRB();
        dest.aaa = "111";
        BeanUtil.copy(src, dest, new CopyOptions().excludeEmptyString());
        assertThat(dest.aaa, is("111"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void copyRBForExcludeNullAndCopyEmptyString() throws Exception {
        MockServletContext servletContext = new MockServletContext();
        MockHttpServletRequest src = new MockHttpServletRequest(servletContext);
        src.setAttribute("aaa", "");
        DestRB dest = new DestRB();
        dest.aaa = "111";
        BeanUtil.copy(src, dest, new CopyOptions()
            .excludeNull()
            .excludeEmptyString());
        assertThat(dest.aaa, is("111"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void copyRBForConverter() throws Exception {
        MockServletContext servletContext = new MockServletContext();
        MockHttpServletRequest src = new MockHttpServletRequest(servletContext);
        src.setAttribute("ddd", "1,000");
        DestRB dest = new DestRB();
        BeanUtil.copy(src, dest, new CopyOptions().numberConverter("#,##0"));
        assertThat(dest.ddd, is(1000));
    }

    /**
     * @throws Exception
     */
    @Test
    public void copyBR() throws Exception {
        SrcBR src = new SrcBR();
        src.aaa = "111";
        MockServletContext servletContext = new MockServletContext();
        MockHttpServletRequest dest =
            new MockHttpServletRequest(servletContext);
        BeanUtil.copy(src, dest);
        assertThat((String) dest.getAttribute("aaa"), is("111"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void copyBRFromReadOnly() throws Exception {
        SrcBR src = new SrcBR();
        src.bbb = "111";
        MockServletContext servletContext = new MockServletContext();
        MockHttpServletRequest dest =
            new MockHttpServletRequest(servletContext);
        BeanUtil.copy(src, dest);
        assertThat((String) dest.getAttribute("bbb"), is("111"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void copyBRFromWriteOnly() throws Exception {
        SrcBR src = new SrcBR();
        src.ccc = "111";
        MockServletContext servletContext = new MockServletContext();
        MockHttpServletRequest dest =
            new MockHttpServletRequest(servletContext);
        BeanUtil.copy(src, dest);
        assertThat(dest.getAttribute("ccc"), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void copyBRForInclude() throws Exception {
        SrcBR src = new SrcBR();
        src.aaa = "111";
        src.bbb = "222";
        MockServletContext servletContext = new MockServletContext();
        MockHttpServletRequest dest =
            new MockHttpServletRequest(servletContext);
        BeanUtil.copy(src, dest, new CopyOptions().include("aaa"));
        assertThat((String) dest.getAttribute("aaa"), is("111"));
        assertThat(dest.getAttribute("bbb"), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void copyBRForExclude() throws Exception {
        SrcBR src = new SrcBR();
        src.aaa = "111";
        src.bbb = "222";
        MockServletContext servletContext = new MockServletContext();
        MockHttpServletRequest dest =
            new MockHttpServletRequest(servletContext);
        BeanUtil.copy(src, dest, new CopyOptions().exclude("bbb"));
        assertThat((String) dest.getAttribute("aaa"), is("111"));
        assertThat(dest.getAttribute("bbb"), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void copyBRForNull() throws Exception {
        SrcBR src = new SrcBR();
        MockServletContext servletContext = new MockServletContext();
        MockHttpServletRequest dest =
            new MockHttpServletRequest(servletContext);
        dest.setAttribute("aaa", "111");
        BeanUtil.copy(src, dest);
        assertThat(dest.getAttribute("aaa"), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void copyBRForExcludeNull() throws Exception {
        SrcBR src = new SrcBR();
        MockServletContext servletContext = new MockServletContext();
        MockHttpServletRequest dest =
            new MockHttpServletRequest(servletContext);
        dest.setAttribute("aaa", "111");
        BeanUtil.copy(src, dest, new CopyOptions().excludeNull());
        assertThat((String) dest.getAttribute("aaa"), is("111"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void copyBRForEmptyString() throws Exception {
        SrcBR src = new SrcBR();
        src.aaa = "";
        MockServletContext servletContext = new MockServletContext();
        MockHttpServletRequest dest =
            new MockHttpServletRequest(servletContext);
        dest.setAttribute("aaa", "111");
        BeanUtil.copy(src, dest);
        assertThat((String) dest.getAttribute("aaa"), is(""));
    }

    /**
     * @throws Exception
     */
    @Test
    public void copyBRForExcludeEmptyString() throws Exception {
        SrcBR src = new SrcBR();
        src.aaa = "";
        MockServletContext servletContext = new MockServletContext();
        MockHttpServletRequest dest =
            new MockHttpServletRequest(servletContext);
        dest.setAttribute("aaa", "111");
        BeanUtil.copy(src, dest, new CopyOptions().excludeEmptyString());
        assertThat((String) dest.getAttribute("aaa"), is("111"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void copyBRForConverter() throws Exception {
        SrcBR src = new SrcBR();
        src.aaa = "1,000";
        MockServletContext servletContext = new MockServletContext();
        MockHttpServletRequest dest =
            new MockHttpServletRequest(servletContext);
        BeanUtil.copy(src, dest, new CopyOptions().numberConverter(
            "#,##0",
            "aaa"));
        assertThat((Long) dest.getAttribute("aaa"), is(1000L));
    }

    /**
     * 
     */
    @SuppressWarnings("unused")
    private static class SrcBB {

        private String aaa;

        private String bbb;

        private String ccc;

        private String ddd;

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
    @SuppressWarnings("unused")
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
    @SuppressWarnings("unused")
    private static class SrcBM {

        private String aaa;

        private String bbb;

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
    @SuppressWarnings("unused")
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

    /**
     * 
     */
    @SuppressWarnings("unused")
    private static class DestRB {

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

    /**
     * 
     */
    @SuppressWarnings("unused")
    private static class SrcBR {

        private String aaa;

        private String bbb;

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
}