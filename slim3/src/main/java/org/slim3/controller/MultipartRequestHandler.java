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

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;

import org.slim3.controller.upload.FileItem;
import org.slim3.controller.upload.FileItemIterator;
import org.slim3.controller.upload.FileItemStream;
import org.slim3.controller.upload.FileUpload;
import org.slim3.controller.upload.Streams;
import org.slim3.util.ArrayUtil;
import org.slim3.util.StringUtil;
import org.slim3.util.ThrowableUtil;

/**
 * {@link RequestHandler} for multipart request.
 * 
 * @author higa
 * @since 1.0.0
 * 
 */
public class MultipartRequestHandler extends RequestHandler {

    /**
     * The key of maximum allowed size of a complete request.
     */
    public static final String SIZE_MAX_KEY = "slim3.uploadSizeMax";

    /**
     * The key of maximum allowed size of a single uploaded file.
     */
    public static final String FILE_SIZE_MAX_KEY = "slim3.uploadFileSizeMax";

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
            FileUpload upload = createFileUpload();
            FileItemIterator iter = upload.getItemIterator(request);
            while (iter.hasNext()) {
                FileItemStream item = iter.next();
                String name = item.getFieldName();
                InputStream stream = item.openStream();
                if (item.isFormField()) {
                    String value =
                        normalizeValue(Streams.asString(stream, request
                            .getCharacterEncoding()));
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
            ThrowableUtil.wrapAndThrow(e);
        }
    }

    /**
     * Creates a {@link FileUpload}.
     * 
     * @return a {@link FileUpload}
     */
    protected FileUpload createFileUpload() {
        FileUpload upload = new FileUpload();
        upload.setHeaderEncoding(request.getCharacterEncoding());
        String sizeMax = System.getProperty(SIZE_MAX_KEY);
        if (!StringUtil.isEmpty(sizeMax)) {
            upload.setSizeMax(Long.valueOf(sizeMax));
        }
        String fileSizeMax = System.getProperty(FILE_SIZE_MAX_KEY);
        if (!StringUtil.isEmpty(fileSizeMax)) {
            upload.setFileSizeMax(Long.valueOf(fileSizeMax));
        }
        return upload;
    }
}