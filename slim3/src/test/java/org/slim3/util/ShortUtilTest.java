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
public class ShortUtilTest {

    /**
     * @throws Exception
     */
    @Test
    public void toShortForNull() throws Exception {
        assertThat(ShortUtil.toShort(null), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void toShortForEmptyString() throws Exception {
        assertThat(ShortUtil.toShort(""), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void toShortForShort() throws Exception {
        Short value = Short.valueOf("1");
        assertThat(ShortUtil.toShort(value), is(value));
    }

    /**
     * @throws Exception
     */
    @Test
    public void toShortForNumber() throws Exception {
        assertThat(ShortUtil.toShort(1).shortValue(), is((short) 1));
    }

    /**
     * @throws Exception
     */
    @Test
    public void toShortForString() throws Exception {
        assertThat(ShortUtil.toShort("1").shortValue(), is((short) 1));
    }

    /**
     * @throws Exception
     */
    @Test
    public void toShortForTrue() throws Exception {
        assertThat(ShortUtil.toShort(Boolean.TRUE).shortValue(), is((short) 1));
    }

    /**
     * @throws Exception
     */
    @Test
    public void toShortForFalse() throws Exception {
        assertThat(ShortUtil.toShort(Boolean.FALSE).shortValue(), is((short) 0));
    }

    /**
     * @throws Exception
     */
    @Test(expected = NumberFormatException.class)
    public void toShortForIllegalArgument() throws Exception {
        ShortUtil.toShort("xx");
    }

    /**
     * @throws Exception
     */
    @Test
    public void toPrimitiveShortForNull() throws Exception {
        assertThat(ShortUtil.toPrimitiveShort(null), is((short) 0));
    }

    /**
     * @throws Exception
     */
    @Test
    public void toPrimitiveShortForEmptyString() throws Exception {
        assertThat(ShortUtil.toPrimitiveShort(""), is((short) 0));
    }

    /**
     * @throws Exception
     */
    @Test
    public void toPrimitiveShortForNumber() throws Exception {
        assertThat(ShortUtil.toPrimitiveShort(1), is((short) 1));
    }

    /**
     * @throws Exception
     */
    @Test
    public void toPrimitiveShortForString() throws Exception {
        assertThat(ShortUtil.toPrimitiveShort("1"), is((short) 1));
    }

    /**
     * @throws Exception
     */
    @Test
    public void toPrimitiveShortForTrue() throws Exception {
        assertThat(ShortUtil.toPrimitiveShort(Boolean.TRUE), is((short) 1));
    }

    /**
     * @throws Exception
     */
    @Test
    public void toPrimitiveShortForFalse() throws Exception {
        assertThat(ShortUtil.toPrimitiveShort(Boolean.FALSE), is((short) 0));
    }

    /**
     * @throws Exception
     */
    @Test(expected = NumberFormatException.class)
    public void toPrimitiveShortForIllegalArgument() throws Exception {
        ShortUtil.toPrimitiveShort("xx");
    }
}