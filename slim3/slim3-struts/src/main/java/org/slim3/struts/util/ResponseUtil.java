/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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
package org.slim3.struts.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.slim3.commons.exception.WrapRuntimeException;
import org.slim3.struts.web.WebLocator;

/**
 * A utility for {@link HttpServletResponse}.
 * 
 * @author higa
 * @since 3.0
 * 
 */
public final class ResponseUtil {

    private ResponseUtil() {
    }

    /**
     * Downloads the data.
     * 
     * @param fileName
     *            the file name
     * @param data
     *            the data
     */
    public static void download(String fileName, byte[] data) {
        HttpServletResponse response = WebLocator.getResponse();
        try {
            response.setContentType("application/octet-stream");
            response.setHeader("Content-disposition", "attachment; filename=\""
                    + fileName + "\"");
            OutputStream out = response.getOutputStream();
            try {
                out.write(data);
            } finally {
                out.close();
            }
        } catch (IOException e) {
            throw new WrapRuntimeException(e);
        }
    }

    /**
     * Downloads the data.
     * 
     * @param fileName
     *            the file name
     * @param in
     *            the input stream
     */
    public static void download(String fileName, InputStream in) {
        try {
            HttpServletResponse response = WebLocator.getResponse();
            response.setContentType("application/octet-stream");
            response.setHeader("Content-disposition", "attachment; filename=\""
                    + fileName + "\"");
            OutputStream out = response.getOutputStream();
            try {
                byte[] buf = new byte[8192];
                int n = 0;
                while ((n = in.read(buf, 0, buf.length)) != -1) {
                    out.write(buf, 0, n);
                }
                out.flush();
            } finally {
                out.close();
            }
        } catch (IOException e) {
            throw new WrapRuntimeException(e);
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                throw new WrapRuntimeException(e);
            }
        }
    }

    /**
     * Downloads the data.
     * 
     * @param fileName
     *            the file name
     * @param in
     *            the input stream
     * @param length
     *            content length
     */
    public static void download(String fileName, InputStream in, int length) {
        HttpServletResponse response = WebLocator.getResponse();
        response.setContentLength(length);
        download(fileName, in);
    }

    /**
     * Writes the text to response.
     * 
     * @param text
     *            the text
     */
    public static void write(String text) {
        write(text, null, null);
    }

    /**
     * Writes the text to response.
     * 
     * @param text
     *            the text
     * @param contentType
     *            the content type
     */
    public static void write(String text, String contentType) {
        write(text, contentType, null);
    }

    /**
     * Writes the text to response.
     * 
     * @param text
     *            the text
     * @param contentType
     *            the content type
     * @param encoding
     *            the encoding
     * 
     */
    public static void write(String text, String contentType, String encoding) {
        if (contentType == null) {
            contentType = "text/plain";
        }
        if (encoding == null) {
            encoding = WebLocator.getRequest().getCharacterEncoding();
            if (encoding == null) {
                encoding = "UTF-8";
            }
        }
        HttpServletResponse response = WebLocator.getResponse();
        response.setContentType(contentType + "; charset=" + encoding);
        try {
            PrintWriter out = null;
            try {
                out = new PrintWriter(new OutputStreamWriter(response
                        .getOutputStream(), encoding));
                out.print(text);
            } finally {
                if (out != null) {
                    out.close();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
