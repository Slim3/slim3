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
package org.slim3.tester;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * A mock implementation for {@link MockHttpServletResponse}.
 * 
 * @author higa
 * @since 1.0.0
 * 
 */
public class MockHttpServletResponse implements HttpServletResponse {

    /**
     * The list for {@link Cookie}.
     */
    protected List<Cookie> cookieList = new ArrayList<Cookie>();

    /**
     * The map for the response header.
     */
    protected Map<String, List<String>> headerMap =
        new HashMap<String, List<String>>();

    /**
     * The committed flag.
     */
    protected boolean committed = false;

    /**
     * The status.
     */
    protected int status = SC_OK;

    /**
     * The message.
     */
    protected String message;

    /**
     * The locale.
     */
    protected Locale locale;

    /**
     * The character encoding.
     */
    protected String characterEncoding = "UTF-8";

    /**
     * The source output.
     */
    protected ByteArrayOutputStream sourceOutput = new ByteArrayOutputStream();

    /**
     * The writer.
     */
    protected PrintWriter writer;

    /**
     * The output stream.
     */
    protected ServletOutputStream outputStream;

    /**
     * Whether getWriter method is called.
     */
    protected boolean getWriterCalled;

    /**
     * Whether getOutputStream method is called.
     */
    protected boolean getOutputStreamCalled;

    /**
     * The buffer size.
     */
    protected int bufferSize = 32;

    /**
     * The redirect path
     */
    protected String redirectPath;

    /**
     * Constructor.
     */
    public MockHttpServletResponse() {
    }

    /**
     * Returns the cookies.
     * 
     * @return the cookies
     */
    public Cookie[] getCookies() {
        return cookieList.toArray(new Cookie[cookieList.size()]);
    }

    public void addCookie(Cookie cookie) {
        cookieList.add(cookie);
    }

    public boolean containsHeader(String name) {
        return headerMap.containsKey(name);
    }

    public String encodeURL(String url) {
        return url;
    }

    public String encodeRedirectURL(String url) {
        return url;
    }

    public String encodeUrl(String url) {
        return encodeURL(url);
    }

    public String encodeRedirectUrl(String url) {
        return encodeRedirectUrl(url);
    }

    /**
     * Returns the status.
     * 
     * @return the status
     */
    public int getStatus() {
        return status;
    }

    public void setStatus(int status, String message) {
        assertNotCommitted();
        this.status = status;
        this.message = message;
        resetBuffer();
    }

    public void setStatus(int status) {
        setStatus(status, getResponseStatusMessage(status));
    }

    /**
     * Returns the response status message.
     * 
     * @param status
     *            the status
     * @return the response status message
     */
    protected String getResponseStatusMessage(int status) {
        switch (status) {
        case HttpServletResponse.SC_OK:
            return "OK";
        case HttpServletResponse.SC_ACCEPTED:
            return "Accepted";
        case HttpServletResponse.SC_BAD_GATEWAY:
            return "Bad Gateway";
        case HttpServletResponse.SC_BAD_REQUEST:
            return "Bad Request";
        case HttpServletResponse.SC_CONFLICT:
            return "Conflict";
        case HttpServletResponse.SC_CONTINUE:
            return "Continue";
        case HttpServletResponse.SC_CREATED:
            return "Created";
        case HttpServletResponse.SC_EXPECTATION_FAILED:
            return "Expectation Failed";
        case HttpServletResponse.SC_FORBIDDEN:
            return "Forbidden";
        case HttpServletResponse.SC_GATEWAY_TIMEOUT:
            return "Gateway Timeout";
        case HttpServletResponse.SC_GONE:
            return "Gone";
        case HttpServletResponse.SC_HTTP_VERSION_NOT_SUPPORTED:
            return "HTTP Version Not Supported";
        case HttpServletResponse.SC_INTERNAL_SERVER_ERROR:
            return "Internal Server Error";
        case HttpServletResponse.SC_LENGTH_REQUIRED:
            return "Length Required";
        case HttpServletResponse.SC_METHOD_NOT_ALLOWED:
            return "Method Not Allowed";
        case HttpServletResponse.SC_MOVED_PERMANENTLY:
            return "Moved Permanently";
        case HttpServletResponse.SC_MOVED_TEMPORARILY:
            return "Moved Temporarily";
        case HttpServletResponse.SC_MULTIPLE_CHOICES:
            return "Multiple Choices";
        case HttpServletResponse.SC_NO_CONTENT:
            return "No Content";
        case HttpServletResponse.SC_NON_AUTHORITATIVE_INFORMATION:
            return "Non-Authoritative Information";
        case HttpServletResponse.SC_NOT_ACCEPTABLE:
            return "Not Acceptable";
        case HttpServletResponse.SC_NOT_FOUND:
            return "Not Found";
        case HttpServletResponse.SC_NOT_IMPLEMENTED:
            return "Not Implemented";
        case HttpServletResponse.SC_NOT_MODIFIED:
            return "Not Modified";
        case HttpServletResponse.SC_PARTIAL_CONTENT:
            return "Partial Content";
        case HttpServletResponse.SC_PAYMENT_REQUIRED:
            return "Payment Required";
        case HttpServletResponse.SC_PRECONDITION_FAILED:
            return "Precondition Failed";
        case HttpServletResponse.SC_PROXY_AUTHENTICATION_REQUIRED:
            return "Proxy Authentication Required";
        case HttpServletResponse.SC_REQUEST_ENTITY_TOO_LARGE:
            return "Request Entity Too Large";
        case HttpServletResponse.SC_REQUEST_TIMEOUT:
            return "Request Timeout";
        case HttpServletResponse.SC_REQUEST_URI_TOO_LONG:
            return "Request URI Too Long";
        case HttpServletResponse.SC_REQUESTED_RANGE_NOT_SATISFIABLE:
            return "Requested Range Not Satisfiable";
        case HttpServletResponse.SC_RESET_CONTENT:
            return "Reset Content";
        case HttpServletResponse.SC_SEE_OTHER:
            return "See Other";
        case HttpServletResponse.SC_SERVICE_UNAVAILABLE:
            return "Service Unavailable";
        case HttpServletResponse.SC_SWITCHING_PROTOCOLS:
            return "Switching Protocols";
        case HttpServletResponse.SC_UNAUTHORIZED:
            return "Unauthorized";
        case HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE:
            return "Unsupported Media Type";
        case HttpServletResponse.SC_USE_PROXY:
            return "Use Proxy";
        case 207:
            return "Multi-Status";
        case 422:
            return "Unprocessable Entity";
        case 423:
            return "Locked";
        case 507:
            return "Insufficient Storage";
        default:
            return "HTTP Response Status " + status;
        }
    }

    /**
     * Asserts this response is not committed.
     */
    protected void assertNotCommitted() {
        if (isCommitted()) {
            throw new IllegalStateException("Already committed");
        }
    }

    /**
     * Returns the message.
     * 
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    public void sendError(int status, String message) throws IOException {
        setStatus(status, message);
    }

    public void sendError(int status) throws IOException {
        setStatus(status);
    }

    public void sendRedirect(String path) throws IOException {
        setStatus(SC_MOVED_TEMPORARILY);
        redirectPath = path;
    }

    /**
     * Returns the redirect path.
     * 
     * @return the redirect path
     */
    public String getRedirectPath() {
        return redirectPath;
    }

    /**
     * Returns the header as {@link Enumeration}.
     * 
     * @param name
     *            the name
     * @return the header
     */
    public Enumeration<String> getHeaders(String name) {
        List<String> values = getHeaderList(name);
        if (values == null) {
            values = Collections.emptyList();
        }
        return Collections.enumeration(values);
    }

    /**
     * Returns the header as string.
     * 
     * @param name
     *            the name
     * @return the header
     */
    public String getHeader(String name) {
        List<String> values = getHeaderList(name);
        if (values != null) {
            return values.get(0);
        }
        return null;
    }

    /**
     * Returns the header as date.
     * 
     * @param name
     *            the name
     * @return the header
     */
    public long getDateHeader(String name) {
        String value = getHeader(name);
        return HeaderUtil.convertStringToDate(value);
    }

    /**
     * Returns the header as int.
     * 
     * @param name
     *            the name
     * @return the header
     */
    public int getIntHeader(String name) {
        String value = getHeader(name);
        return HeaderUtil.convertStringToInt(value);
    }

    /**
     * Returns the header as list.
     * 
     * @param name
     *            the name
     * @return the header
     */
    protected List<String> getHeaderList(String name) {
        return headerMap.get(name.toLowerCase());
    }

    /**
     * Returns the header names.
     * 
     * @return the header names
     */
    public Enumeration<String> getHeaderNames() {
        return Collections.enumeration(headerMap.keySet());
    }

    public void setHeader(String name, String value) {
        List<String> values = new ArrayList<String>();
        values.add(value);
        headerMap.put(name.toLowerCase(), values);
    }

    public void addHeader(String name, String value) {
        List<String> values = getHeaderList(name);
        if (values == null) {
            values = new ArrayList<String>();
        }
        values.add(value);
        headerMap.put(name.toLowerCase(), values);
    }

    public void setDateHeader(String name, long value) {
        setHeader(name, HeaderUtil.convertDateToString(value));
    }

    public void addDateHeader(String name, long value) {
        addHeader(name, HeaderUtil.convertDateToString(value));
    }

    public void setIntHeader(String name, int value) {
        setHeader(name, HeaderUtil.convertIntToString(value));
    }

    public void addIntHeader(String name, int value) {
        addHeader(name, HeaderUtil.convertIntToString(value));
    }

    /**
     * Returns the content length.
     * 
     * @return the content length
     */
    public int getContentLength() {
        return getIntHeader("content-length");
    }

    public void setContentLength(int contentLength) {
        setIntHeader("content-length", contentLength);
    }

    public String getContentType() {
        return getHeader("content-type");
    }

    public void setContentType(String contentType) {
        setHeader("content-type", contentType);
    }

    public String getCharacterEncoding() {
        return characterEncoding;
    }

    public void setCharacterEncoding(String characterEncoding) {
        this.characterEncoding = characterEncoding;
    }

    public ServletOutputStream getOutputStream() throws IOException {
        if (getWriterCalled) {
            throw new IllegalStateException(
                "The getWriter method is already called.");
        }
        if (!getOutputStreamCalled) {
            getOutputStreamCalled = true;
            outputStream = new MockServletOutputStream(sourceOutput);
        }
        return outputStream;
    }

    public PrintWriter getWriter() throws IOException {
        if (getOutputStreamCalled) {
            throw new IllegalStateException(
                "The getOutputStream method is already called.");
        }
        if (!getWriterCalled) {
            getWriterCalled = true;
            writer =
                new PrintWriter(new OutputStreamWriter(
                    sourceOutput,
                    characterEncoding));
        }
        return writer;
    }

    /**
     * Returns the output as array of bytes.
     * 
     * @return the output
     * @throws IOException
     *             if {@link IOException} is encountered
     */
    public byte[] getOutputAsByteArray() throws IOException {
        flushBuffer();
        return sourceOutput.toByteArray();
    }

    /**
     * Returns the output as string.
     * 
     * @return the output
     * @throws IOException
     *             if {@link IOException} is encountered
     */
    public String getOutputAsString() throws IOException {
        return new String(getOutputAsByteArray(), characterEncoding);
    }

    public int getBufferSize() {
        return bufferSize;
    }

    public void setBufferSize(int bufferSize) {
        if (getOutputStreamCalled) {
            throw new IllegalStateException(
                "The getOutputStream method is already called.");
        }
        if (getWriterCalled) {
            throw new IllegalStateException(
                "The getWriter method is already called.");
        }
        this.bufferSize = bufferSize;
        sourceOutput = new ByteArrayOutputStream(bufferSize);
    }

    public void flushBuffer() throws IOException {
        if (outputStream != null) {
            outputStream.flush();
        } else if (writer != null) {
            writer.flush();
        }
    }

    public void resetBuffer() {
    }

    public boolean isCommitted() {
        return committed;
    }

    public void reset() {
        committed = false;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public Locale getLocale() {
        return locale;
    }
}
