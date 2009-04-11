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
package org.slim3.struts.web;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slim3.commons.cleaner.Cleaner;
import org.slim3.commons.config.Configuration;
import org.slim3.struts.S3StrutsGlobals;

/**
 * {@link Filter} for Slim3.
 * 
 * @author higa
 * @since 3.0
 * 
 */
public class S3StrutsFilter implements Filter {

    /**
     * The default encoding
     */
    protected static final String DEFAULT_ENCODING = "UTF-8";

    /**
     * The encoding.
     */
    protected String encoding;

    /**
     * Constructor.
     */
    public S3StrutsFilter() {
    }

    public void init(FilterConfig config) throws ServletException {
        encoding = Configuration.getInstance().getValue(
                S3StrutsGlobals.ENCODING_KEY);
        if (encoding == null) {
            encoding = DEFAULT_ENCODING;
        }
        WebLocator.setServletContext(config.getServletContext());
    }

    public void destroy() {
    }

    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        doFilter((HttpServletRequest) request, (HttpServletResponse) response,
                chain);
    }

    /**
     * Executes filtering process.
     * 
     * @param request
     *            the request
     * @param response
     *            the response
     * @param chain
     *            the filter chain
     * @throws IOException
     *             if {@link IOException} is encountered
     * @throws ServletException
     *             if {@link ServletException} is encountered
     */
    protected void doFilter(HttpServletRequest request,
            HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (request.getCharacterEncoding() == null) {
            request.setCharacterEncoding(encoding);
        }
        boolean hot = Configuration.getInstance().isHot();
        WebLocator.setRequest(request);
        WebLocator.setResponse(response);
        if (!hot) {
            chain.doFilter(request, response);
            return;
        }
        HotdeployClassLoader hotClassLoader = HotdeployClassLoader
                .getCurrentInstance();
        if (hotClassLoader == null) {
            synchronized (S3StrutsFilter.class) {
                ClassLoader originalClassLoader = Thread.currentThread()
                        .getContextClassLoader();
                hotClassLoader = new HotdeployClassLoader(originalClassLoader);
                try {
                    HotdeployClassLoader.setCurrentInstance(hotClassLoader);
                    chain.doFilter(request, response);
                } finally {
                    HotdeployClassLoader.removeCurrentInstance();
                    Cleaner.cleanAll();
                }
            }
        } else {
            chain.doFilter(request, response);
        }
    }
}