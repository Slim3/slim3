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

import java.util.concurrent.ExecutionException;

import org.junit.Test;

/**
 * @author higa
 * 
 */
public class FutureUtilTest {

    /**
     * @throws Exception
     */
    @Test
    public void get() throws Exception {
        String value = "hoge";
        FakeFuture<String> future = new FakeFuture<String>(value);
        assertThat(FutureUtil.get(future), is(value));
    }

    /**
     * @throws Exception
     */
    @Test(expected = Exception.class)
    public void getWhenExceptionOccurred() throws Exception {
        String value = "hoge";
        FakeFuture<String> future = new FakeFuture<String>(value) {

            @Override
            public String get() throws InterruptedException, ExecutionException {
                throw new ExecutionException(new Exception("Hoge"));
            }

        };
        FutureUtil.get(future);
    }

    /**
     * @throws Exception
     */
    @Test(expected = RuntimeException.class)
    public void getWhenRuntimeExceptionOccurred() throws Exception {
        String value = "hoge";
        FakeFuture<String> future = new FakeFuture<String>(value) {

            @Override
            public String get() throws InterruptedException, ExecutionException {
                throw new ExecutionException(new RuntimeException("Hoge"));
            }

        };
        FutureUtil.get(future);
    }

    /**
     * @throws Exception
     */
    @Test(expected = Error.class)
    public void getWhenErrorOccurred() throws Exception {
        String value = "hoge";
        FakeFuture<String> future = new FakeFuture<String>(value) {

            @Override
            public String get() throws InterruptedException, ExecutionException {
                throw new ExecutionException(new Error("Hoge"));
            }

        };
        FutureUtil.get(future);
    }

    /**
     * @throws Exception
     */
    @Test
    public void getQuietly() throws Exception {
        String value = "hoge";
        FakeFuture<String> future = new FakeFuture<String>(value);
        assertThat(FutureUtil.getQuietly(future), is(value));
    }

    /**
     * @throws Exception
     */
    @Test(expected = WrapRuntimeException.class)
    public void getQuietlyWhenExceptionOccurred() throws Exception {
        String value = "hoge";
        FakeFuture<String> future = new FakeFuture<String>(value) {

            @Override
            public String get() throws InterruptedException, ExecutionException {
                throw new ExecutionException(new Exception("Hoge"));
            }

        };
        FutureUtil.getQuietly(future);
    }

    /**
     * @throws Exception
     */
    @Test(expected = RuntimeException.class)
    public void getQuietlyWhenRuntimeExceptionOccurred() throws Exception {
        String value = "hoge";
        FakeFuture<String> future = new FakeFuture<String>(value) {

            @Override
            public String get() throws InterruptedException, ExecutionException {
                throw new ExecutionException(new RuntimeException("Hoge"));
            }

        };
        FutureUtil.getQuietly(future);
    }

    /**
     * @throws Exception
     */
    @Test(expected = Error.class)
    public void getQuietlyWhenErrorOccurred() throws Exception {
        String value = "hoge";
        FakeFuture<String> future = new FakeFuture<String>(value) {

            @Override
            public String get() throws InterruptedException, ExecutionException {
                throw new ExecutionException(new Error("Hoge"));
            }

        };
        FutureUtil.getQuietly(future);
    }
}