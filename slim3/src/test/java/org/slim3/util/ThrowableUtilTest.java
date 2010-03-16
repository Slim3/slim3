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
public class ThrowableUtilTest {

    /**
     * @throws Exception
     */
    @Test(expected = Error.class)
    public void wrapForError() throws Exception {
        ThrowableUtil.wrap(new Error("Error"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void wrapForRuntimeException() throws Exception {
        RuntimeException ex = new RuntimeException("RuntimeException");
        assertThat(ThrowableUtil.wrap(ex), is(sameInstance(ex)));
    }

    /**
     * @throws Exception
     */
    @Test
    public void wrapForException() throws Exception {
        Exception ex = new Exception("Exception");
        RuntimeException wrapped = ThrowableUtil.wrap(ex);
        assertThat(wrapped, is(WrapRuntimeException.class));
        assertThat(wrapped.getCause(), is(sameInstance((Throwable) ex)));
    }

    /**
     * @throws Exception
     */
    @Test(expected = Error.class)
    public void wrapAndThrow() throws Exception {
        ThrowableUtil.wrapAndThrow(new Error("Error"));
    }
}