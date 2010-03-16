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
public class BooleanUtilTest {

    /**
     * @throws Exception
     */
    @Test
    public void toBooleanForNull() throws Exception {
        assertThat(BooleanUtil.toBoolean(null), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void toBooleanForEmptyString() throws Exception {
        assertThat(BooleanUtil.toBoolean(""), is(false));
    }

    /**
     * @throws Exception
     */
    @Test
    public void toBooleanForBoolean() throws Exception {
        assertThat(BooleanUtil.toBoolean(Boolean.TRUE), is(true));
    }

    /**
     * @throws Exception
     */
    @Test
    public void toBooleanForString() throws Exception {
        assertThat(BooleanUtil.toBoolean("1"), is(true));
        assertThat(BooleanUtil.toBoolean("0"), is(false));
        assertThat(BooleanUtil.toBoolean("true"), is(true));
        assertThat(BooleanUtil.toBoolean("false"), is(false));
        assertThat(BooleanUtil.toBoolean("on"), is(true));
    }

    /**
     * @throws Exception
     */
    @Test
    public void toBooleanForObject() throws Exception {
        assertThat(BooleanUtil.toBoolean(new Object()), is(true));
    }

    /**
     * @throws Exception
     */
    @Test
    public void toPrimitiveBooleanForNull() throws Exception {
        assertThat(BooleanUtil.toPrimitiveBoolean(null), is(false));
    }

    /**
     * @throws Exception
     */
    @Test
    public void toPrimitiveBooleanForEmptyString() throws Exception {
        assertThat(BooleanUtil.toPrimitiveBoolean(""), is(false));
    }

    /**
     * @throws Exception
     */
    @Test
    public void toPrimitiveBooleanForBoolean() throws Exception {
        assertThat(BooleanUtil.toPrimitiveBoolean(Boolean.TRUE), is(true));
    }

    /**
     * @throws Exception
     */
    @Test
    public void toPrimitiveInt() throws Exception {
        assertThat(BooleanUtil.toPrimitiveInt(true), is(1));
        assertThat(BooleanUtil.toPrimitiveInt(false), is(0));
    }
}