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

import org.junit.Test;

/**
 * @author higa
 * 
 */
public class StringUtilTest {

    /**
     * @throws Exception
     */
    @Test
    public void isEmptyForNull() throws Exception {
        assertThat(StringUtil.isEmpty(null), is(true));
    }

    /**
     * @throws Exception
     */
    @Test
    public void isEmptyForEmptyString() throws Exception {
        assertThat(StringUtil.isEmpty(""), is(true));
    }

    /**
     * @throws Exception
     */
    @Test
    public void isEmptyForNotEmptyString() throws Exception {
        assertThat(StringUtil.isEmpty("a"), is(false));
    }

    /**
     * @throws Exception
     */
    @Test
    public void decapitalize() throws Exception {
        assertThat(StringUtil.decapitalize("Aaa"), is("aaa"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void decapitalizeForOneCharacter() throws Exception {
        assertThat(StringUtil.decapitalize("A"), is("a"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void decapitalizeForEmptyString() throws Exception {
        assertThat(StringUtil.decapitalize(""), is(""));
    }

    /**
     * @throws Exception
     */
    @Test
    public void decapitalizeForAllUpperCase() throws Exception {
        assertThat(StringUtil.decapitalize("URL"), is("URL"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void capitalize() throws Exception {
        assertThat(StringUtil.capitalize("aaa"), is("Aaa"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void capitalizeForChapitalizedString() throws Exception {
        assertThat(StringUtil.capitalize("Aaa"), is("Aaa"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void capitalizeForEmptyString() throws Exception {
        assertThat(StringUtil.capitalize(""), is(""));
    }

    /**
     * @throws Exception
     */
    @Test
    public void split() throws Exception {
        String[] array = StringUtil.split("aaa\nbbb", "\n");
        assertThat(array.length, is(2));
        assertThat(array[0], is("aaa"));
        assertThat(array[1], is("bbb"));
    }

    /**
     * 
     */
    @Test
    public void splitIncludingBlankDelimiter() {
        String[] array = StringUtil.split("aaa, bbb", ", ");
        assertThat(array.length, is(2));
        assertThat(array[0], is("aaa"));
        assertThat(array[1], is("bbb"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void splitBySize() throws Exception {
        String[] array = StringUtil.split("abc", 2);
        assertThat(array.length, is(2));
        assertThat(array[0], is("ab"));
        assertThat(array[1], is("c"));
        array = StringUtil.split("abcd", 2);
        assertThat(array.length, is(2));
        assertThat(array[0], is("ab"));
        assertThat(array[1], is("cd"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void join() throws Exception {
        assertThat(StringUtil.join(new String[] { "ab", "c" }), is("abc"));
    }

    /**
     * 
     */
    @Test
    public void testToString() {
        assertThat(StringUtil.toString(2), is("2"));
        assertThat(StringUtil.toString(null), is(nullValue()));
    }
}