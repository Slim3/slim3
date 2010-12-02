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
package org.slim3.controller;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Test;
import org.slim3.controller.upload.FileUpload;
import org.slim3.tester.MockHttpServletRequest;
import org.slim3.tester.MockServletContext;

/**
 * @author higa
 * 
 */
public class MultipartRequestHandlerTest {

    private MockServletContext servletContext = new MockServletContext();

    private MockHttpServletRequest request =
        new MockHttpServletRequest(servletContext);

    /**
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        System.clearProperty(MultipartRequestHandler.SIZE_MAX_KEY);
        System.clearProperty(MultipartRequestHandler.FILE_SIZE_MAX_KEY);
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void createFileUploadForEncoding() throws Exception {
        String encoding = "UTF-8";
        request.setCharacterEncoding(encoding);
        MultipartRequestHandler handler = new MultipartRequestHandler(request);
        FileUpload upload = handler.createFileUpload();
        assertThat(upload.getHeaderEncoding(), is(encoding));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void createFileUploadForSizeMax() throws Exception {
        long sizeMax = 1000;
        System.setProperty(MultipartRequestHandler.SIZE_MAX_KEY, String
            .valueOf(sizeMax));
        MultipartRequestHandler handler = new MultipartRequestHandler(request);
        FileUpload upload = handler.createFileUpload();
        assertThat(upload.getSizeMax(), is(sizeMax));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void createFileUploadForFileSizeMax() throws Exception {
        long fileSizeMax = 100;
        System.setProperty(MultipartRequestHandler.FILE_SIZE_MAX_KEY, String
            .valueOf(fileSizeMax));
        MultipartRequestHandler handler = new MultipartRequestHandler(request);
        FileUpload upload = handler.createFileUpload();
        assertThat(upload.getFileSizeMax(), is(fileSizeMax));
    }
}