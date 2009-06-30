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
package org.slim3.controller;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;

import org.slim3.controller.upload.FileItem;
import org.slim3.controller.upload.FileItemIterator;
import org.slim3.controller.upload.FileItemStream;
import org.slim3.controller.upload.FileUpload;
import org.slim3.controller.upload.Streams;
import org.slim3.util.ArrayUtil;
import org.slim3.util.RuntimeExceptionUtil;

/**
 * {@link RequestHandler} for multipart request.
 * 
 * @author higa
 * @since 3.0
 * 
 */
public class MultipartRequestHandler extends RequestHandler {

    private static final int BYTE_ARRAY_SIZE = 8 * 1024;

    /**
     * Constructor.
     * 
     * @param request
     *            the request
     */
    public MultipartRequestHandler(HttpServletRequest request) {
        super(request);
    }

    @Override
    public void handle() {
        try {
            FileUpload upload = new FileUpload();
            upload.setHeaderEncoding(request.getCharacterEncoding());
            FileItemIterator iter = upload.getItemIterator(request);
            while (iter.hasNext()) {
                FileItemStream item = iter.next();
                String name = item.getFieldName();
                InputStream stream = item.openStream();
                if (item.isFormField()) {
                    String value = normalize(Streams.asString(stream));
                    if (name.endsWith(ARRAY_SUFFIX)) {
                        String[] array = (String[]) request.getAttribute(name);
                        if (array == null) {
                            array = new String[0];
                        }
                        request.setAttribute(name, ArrayUtil.add(array, value));
                    } else {
                        request.setAttribute(name, value);
                    }
                } else {
                    ByteArrayOutputStream baos =
                        new ByteArrayOutputStream(BYTE_ARRAY_SIZE);
                    Streams.copy(stream, baos, true);
                    byte[] data = baos.toByteArray();
                    FileItem value =
                        data.length > 0 ? new FileItem(item.getFileName(), item
                            .getContentType(), data) : null;
                    if (name.endsWith(ARRAY_SUFFIX)) {
                        FileItem[] array =
                            (FileItem[]) request.getAttribute(name);
                        if (array == null) {
                            array = new FileItem[0];
                        }
                        request.setAttribute(name, ArrayUtil.add(array, value));
                    } else {
                        request.setAttribute(name, value);
                    }
                }
            }
        } catch (Exception e) {
            RuntimeExceptionUtil.wrapAndThrow(e);
        }
    }
}