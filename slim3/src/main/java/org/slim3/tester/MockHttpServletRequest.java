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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slim3.util.ArrayMap;
import org.slim3.util.StringUtil;

/**
 * A mock implementation for {@link HttpServletRequest}.
 * 
 * @author higa
 * @since 1.0.0
 * 
 */
public class MockHttpServletRequest implements HttpServletRequest {

    /**
     * The servlet context.
     */
    protected ServletContext servletContext;

    /**
     * The servlet path.
     */
    protected String servletPath;

    /**
     * Authority type.
     */
    protected String authType;

    /**
     * The list for {@link Cookie}.
     */
    protected List<Cookie> cookieList = new ArrayList<Cookie>();

    /**
     * The map for the request header.
     */
    protected ArrayMap<String, List<String>> headerMap =
        new ArrayMap<String, List<String>>();

    /**
     * The method.
     */
    protected String method = "get";

    /**
     * The path information.
     */
    protected String pathInfo;

    /**
     * The translated path.
     */
    protected String pathTranslated;

    /**
     * The query string.
     */
    protected String queryString;

    /**
     * The session.
     */
    protected MockHttpSession session;

    /**
     * The request scheme.
     */
    protected String scheme = "http";

    /**
     * The server port.
     */
    protected int serverPort = 80;

    /**
     * The protocol.
     */
    protected String protocol = "HTTP/1.1";

    /**
     * The server name.
     */
    protected String serverName = "localhost";

    /**
     * The map for the attributes.
     */
    protected Map<String, Object> attributeMap = new HashMap<String, Object>();

    /**
     * The character encoding.
     */
    protected String characterEncoding;

    /**
     * The content length.
     */
    protected int contentLength;

    /**
     * The content type.
     */
    protected String contentType;

    /**
     * The map for the parameters.
     */
    protected Map<String, String[]> parameterMap =
        new HashMap<String, String[]>();

    /**
     * The requested session identifier.
     */
    protected String requestedSessionId;

    /**
     * Whether the requested session identifier comes from cookie.
     */
    protected boolean requestedSessionIdFromCookie = true;

    /**
     * The input stream.
     */
    protected ServletInputStream inputStream;

    /**
     * The buffered reader.
     */
    protected BufferedReader reader;

    /**
     * The remote address.
     */
    protected String remoteAddr;

    /**
     * The remote host.
     */
    protected String remoteHost;

    /**
     * The remote port.
     */
    protected int remotePort;

    /**
     * The local address.
     */
    protected String localAddr;

    /**
     * The local name.
     */
    protected String localName;

    /**
     * The local port.
     */
    protected int localPort;

    /**
     * The list for the locales.
     */
    protected List<Locale> localeList = new ArrayList<Locale>();

    /**
     * The secure flag.
     */
    protected boolean secure = false;

    /**
     * The user principal.
     */
    protected Principal userPrincipal;

    /**
     * Constructor.
     * 
     * @param servletContext
     *            the servlet context
     * @throws NullPointerException
     *             if the servletContext parameter is null
     */
    public MockHttpServletRequest(ServletContext servletContext)
            throws NullPointerException {
        if (servletContext == null) {
            throw new NullPointerException(
                "The servletContext parameter is null.");
        }
        this.servletContext = servletContext;
    }

    public String getAuthType() {
        return authType;
    }

    /**
     * Sets the authority type.
     * 
     * @param authType
     *            the authority type
     */
    public void setAuthType(String authType) {
        this.authType = authType;
    }

    public Cookie[] getCookies() {
        return cookieList.toArray(new Cookie[cookieList.size()]);
    }

    /**
     * Adds the cookie.
     * 
     * @param cookie
     *            the cookie
     */
    public void addCookie(Cookie cookie) {
        cookieList.add(cookie);
    }

    public String getHeader(String name) {
        List<String> values = getHeaderList(name);
        if (values != null) {
            return values.get(0);
        }
        return null;
    }

    public long getDateHeader(String name) {
        String value = getHeader(name);
        return HeaderUtil.convertStringToDate(value);
    }

    public int getIntHeader(String name) {
        String value = getHeader(name);
        return HeaderUtil.convertStringToInt(value);
    }

    public Enumeration<String> getHeaders(String name) {
        List<String> values = getHeaderList(name);
        if (values == null) {
            values = Collections.emptyList();
        }
        return Collections.enumeration(values);
    }

    public Enumeration<String> getHeaderNames() {
        return Collections.enumeration(headerMap.keySet());
    }

    /**
     * Sets the header.
     * 
     * @param name
     *            the name
     * @param value
     *            the value
     */
    public void setHeader(String name, String value) {
        List<String> values = new ArrayList<String>();
        values.add(value);
        headerMap.put(name.toLowerCase(), values);
    }

    /**
     * Sets the date header.
     * 
     * @param name
     *            the name
     * @param value
     *            the value
     */
    public void setDateHeader(String name, long value) {
        setHeader(name, HeaderUtil.convertDateToString(value));
    }

    /**
     * Sets the int header.
     * 
     * @param name
     *            the name
     * @param value
     *            the value
     */
    public void setIntHeader(String name, int value) {
        setHeader(name, HeaderUtil.convertIntToString(value));
    }

    /**
     * Adds the header.
     * 
     * @param name
     *            the name
     * @param value
     *            the value
     */
    public void addHeader(String name, String value) {
        List<String> values = getHeaderList(name);
        if (values == null) {
            values = new ArrayList<String>();
        }
        values.add(value);
        headerMap.put(name.toLowerCase(), values);
    }

    /**
     * Adds the date header.
     * 
     * @param name
     *            the name
     * @param value
     *            the value
     */
    public void addDateHeader(String name, long value) {
        addHeader(name, HeaderUtil.convertDateToString(value));
    }

    /**
     * Adds the int header.
     * 
     * @param name
     *            the name
     * @param value
     *            the value
     */
    public void addIntHeader(String name, int value) {
        addHeader(name, HeaderUtil.convertIntToString(value));
    }

    /**
     * Return the list for header.
     * 
     * @param name
     *            the name
     * @return the list for header
     */
    protected List<String> getHeaderList(String name) {
        return headerMap.get(name.toLowerCase());
    }

    public String getMethod() {
        return method;
    }

    /**
     * Sets the method.
     * 
     * @param method
     *            the method
     */
    public void setMethod(String method) {
        this.method = method;
    }

    public String getPathInfo() {
        return pathInfo;
    }

    /**
     * Sets the path information.
     * 
     * @param pathInfo
     *            the path information
     */
    public void setPathInfo(String pathInfo) {
        this.pathInfo = pathInfo;
    }

    public String getPathTranslated() {
        return pathTranslated;
    }

    /**
     * Sets the translated path.
     * 
     * @param pathTranslated
     *            the translated path
     */
    public void setPathTranslated(String pathTranslated) {
        this.pathTranslated = pathTranslated;
    }

    public String getContextPath() {
        return servletContext.getContextPath();
    }

    public String getQueryString() {
        return queryString;
    }

    /**
     * Sets the query string.
     * 
     * @param queryString
     *            the query string
     */
    public void setQueryString(String queryString) {
        this.queryString = queryString;
        if (queryString != null) {
            for (String param : StringUtil.split(queryString, "&")) {
                String[] keyValue = StringUtil.split(param, "=");
                if (keyValue.length == 1) {
                    addParameter(keyValue[0], "");
                } else {
                    addParameter(keyValue[0], keyValue[1]);
                }
            }
        }
    }

    public String getRemoteUser() {
        return System.getProperty("user.name");
    }

    public boolean isUserInRole(String role) {
        return true;
    }

    public Principal getUserPrincipal() {
        return userPrincipal;
    }

    /**
     * Sets the user principal.
     * 
     * @param userPrincipal
     *            the user principal
     */
    public void setUserPrincipal(Principal userPrincipal) {
        this.userPrincipal = userPrincipal;
    }

    public String getRequestedSessionId() {
        return requestedSessionId;
    }

    /**
     * Sets the requested session identifier.
     * 
     * @param requestedSessionId
     *            the requested session identifier
     */
    public void setRequestedSessionId(String requestedSessionId) {
        this.requestedSessionId = requestedSessionId;
    }

    public String getRequestURI() {
        String contextPath = getContextPath();
        if (contextPath.equals("/")) {
            return servletPath;
        }
        return contextPath + servletPath;
    }

    public StringBuffer getRequestURL() {
        StringBuffer url = new StringBuffer();
        url.append(scheme);
        url.append("://");
        url.append(serverName);
        if ((scheme.equals("http") && (serverPort != 80))
            || (scheme.equals("https") && (serverPort != 443))) {

            url.append(':');
            url.append(serverPort);
        }
        url.append(getRequestURI());
        return url;
    }

    public String getServletPath() {
        return servletPath;
    }

    /**
     * Sets the servlet path.
     * 
     * @param servletPath
     *            the servlet path
     */
    public void setServletPath(String servletPath) {
        this.servletPath = servletPath;
    }

    public HttpSession getSession(boolean create) {
        if (session != null) {
            session.updateLastAccessedTime();
            return session;
        }
        if (create) {
            session = new MockHttpSession(servletContext);
        }
        return session;
    }

    public HttpSession getSession() {
        return getSession(true);
    }

    public boolean isRequestedSessionIdValid() {
        if (session != null) {
            return session.isValid();
        }
        return false;
    }

    public boolean isRequestedSessionIdFromCookie() {
        return requestedSessionIdFromCookie;
    }

    public boolean isRequestedSessionIdFromURL() {
        return !isRequestedSessionIdFromCookie();
    }

    public boolean isRequestedSessionIdFromUrl() {
        return isRequestedSessionIdFromURL();
    }

    public Object getAttribute(String name) {
        return attributeMap.get(name);
    }

    public Enumeration<String> getAttributeNames() {
        return Collections.enumeration(attributeMap.keySet());
    }

    public void setAttribute(String name, Object value) {
        attributeMap.put(name, value);
    }

    public void removeAttribute(String name) {
        attributeMap.remove(name);
    }

    public String getCharacterEncoding() {
        return characterEncoding;
    }

    public void setCharacterEncoding(String characterEncoding)
            throws UnsupportedEncodingException {
        this.characterEncoding = characterEncoding;
    }

    public int getContentLength() {
        return contentLength;
    }

    /**
     * Sets the content length.
     * 
     * @param contentLength
     *            the content length
     */
    public void setContentLength(int contentLength) {
        this.contentLength = contentLength;
    }

    public String getContentType() {
        return contentType;
    }

    /**
     * Sets the content type.
     * 
     * @param contentType
     */
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public ServletInputStream getInputStream() throws IOException {
        return inputStream;
    }

    /**
     * Sets the input stream.
     * 
     * @param inputStream
     *            the input stream
     */
    public void setInputStream(ServletInputStream inputStream) {
        this.inputStream = inputStream;
    }

    public String getParameter(String name) {
        String[] values = parameterMap.get(name);
        if (values == null || values.length == 0) {
            return null;
        }
        return values[0];
    }

    public Enumeration<String> getParameterNames() {
        return Collections.enumeration(parameterMap.keySet());
    }

    public String[] getParameterValues(String name) {
        return parameterMap.get(name);
    }

    public Map<String, String[]> getParameterMap() {
        return parameterMap;
    }

    /**
     * Adds the request parameter.
     * 
     * @param name
     *            the name
     * @param value
     *            the value
     */
    public void addParameter(String name, String value) {
        String[] values = getParameterValues(name);
        if (values == null) {
            setParameter(name, value);
        } else {
            String[] newArray = new String[values.length + 1];
            System.arraycopy(values, 0, newArray, 0, values.length);
            newArray[newArray.length - 1] = value;
            parameterMap.put(name, newArray);
        }
    }

    /**
     * Adds the request parameter.
     * 
     * @param name
     *            the name
     * @param values
     *            the value
     */
    public void addParameter(String name, String[] values) {
        if (values == null) {
            setParameter(name, (String) null);
            return;
        }
        String[] vals = getParameterValues(name);
        if (vals == null) {
            setParameter(name, values);
        } else {
            String[] newArray = new String[vals.length + values.length];
            System.arraycopy(vals, 0, newArray, 0, vals.length);
            System.arraycopy(values, 0, newArray, vals.length, values.length);
            parameterMap.put(name, newArray);
        }
    }

    /**
     * Sets the request parameter.
     * 
     * @param name
     *            the name
     * @param value
     *            the value
     */
    public void setParameter(String name, String value) {
        parameterMap.put(name, new String[] { value });
    }

    /**
     * Sets the request parameter.
     * 
     * @param name
     *            the name
     * @param values
     *            the value
     */
    public void setParameter(String name, String[] values) {
        parameterMap.put(name, values);
    }

    /**
     * Removes the request parameter.
     * 
     * @param name
     *            the name
     * 
     */
    public void removeParameter(String name) {
        parameterMap.remove(name);
    }

    public String getProtocol() {
        return protocol;
    }

    /**
     * Sets the protocol.
     * 
     * @param protocol
     *            the protocol
     */
    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getScheme() {
        return scheme;
    }

    /**
     * Sets the scheme.
     * 
     * @param scheme
     *            the scheme
     */
    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public String getServerName() {
        return serverName;
    }

    /**
     * Sets the server name.
     * 
     * @param serverName
     *            the server name
     */
    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public int getServerPort() {
        return serverPort;
    }

    /**
     * Sets the server port.
     * 
     * @param serverPort
     *            the server port
     */
    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public BufferedReader getReader() throws IOException {
        return reader;
    }

    /**
     * Sets the buffered reader.
     * 
     * @param reader
     *            the buffered reader
     */
    public void setReader(BufferedReader reader) {
        this.reader = reader;
    }

    public String getRemoteAddr() {
        return remoteAddr;
    }

    /**
     * Sets the remote address.
     * 
     * @param remoteAddr
     *            the remote address
     */
    public void setRemoteAddr(String remoteAddr) {
        this.remoteAddr = remoteAddr;
    }

    public String getRemoteHost() {
        return remoteHost;
    }

    /**
     * Sets the remote host.
     * 
     * @param remoteHost
     *            the remote host
     */
    public void setRemoteHost(String remoteHost) {
        this.remoteHost = remoteHost;
    }

    public String getLocalAddr() {
        return localAddr;
    }

    /**
     * Sets the local address.
     * 
     * @param localAddr
     *            the local address
     */
    public void setLocalAddr(String localAddr) {
        this.localAddr = localAddr;
    }

    public String getLocalName() {
        return localName;
    }

    /**
     * Sets the local name.
     * 
     * @param localName
     *            the local name
     */
    public void setLocalName(String localName) {
        this.localName = localName;
    }

    public int getLocalPort() {
        return localPort;
    }

    /**
     * Sets the local port.
     * 
     * @param localPort
     *            the local port
     */
    public void setLocalPort(int localPort) {
        this.localPort = localPort;
    }

    public int getRemotePort() {
        return remotePort;
    }

    /**
     * Sets the remote port.
     * 
     * @param remotePort
     *            the remote port
     */
    public void setRemotePort(int remotePort) {
        this.remotePort = remotePort;
    }

    public Locale getLocale() {
        if (localeList.isEmpty()) {
            return null;
        }
        return localeList.get(0);
    }

    /**
     * Adds the locale.
     * 
     * @param locale
     *            the locale
     */
    public void addLocale(Locale locale) {
        localeList.add(locale);
    }

    public Enumeration<Locale> getLocales() {
        return Collections.enumeration(localeList);
    }

    public boolean isSecure() {
        return secure;
    }

    /**
     * Sets the secure flag.
     * 
     * @param secure
     *            the secure flag
     */
    public void setSecure(boolean secure) {
        this.secure = secure;
    }

    public RequestDispatcher getRequestDispatcher(String path) {
        return servletContext.getRequestDispatcher(path);
    }

    public String getRealPath(String path) {
        return servletContext.getRealPath(path);
    }
}