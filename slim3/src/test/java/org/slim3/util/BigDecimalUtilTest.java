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

import org.junit.Test;

/**
 * @author higa
 * 
 */
public class BigDecimalUtilTest {

    /**
     * @throws Exception
     */
    @Test
    public void toBigDecimalForNull() throws Exception {
        assertThat(BigDecimalUtil.toBigDecimal(null), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void toBigDecimalForBigDecimal() throws Exception {
        BigDecimal value = new BigDecimal(1);
        assertThat(BigDecimalUtil.toBigDecimal(value), is(value));
    }

    /**
     * @throws Exception
     */
    @Test
    public void toBigDecimalForString() throws Exception {
        BigDecimal value = new BigDecimal("1");
        assertThat(BigDecimalUtil.toBigDecimal("1"), is(value));
    }

    /**
     * @throws Exception
     */
    @Test
    public void toBigDecimalForEmptyString() throws Exception {
        assertThat(BigDecimalUtil.toBigDecimal(""), is(nullValue()));
    }
}