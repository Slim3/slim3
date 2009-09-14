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
package org.slim3.jdo;

import java.io.IOException;

import javax.jdo.PersistenceManager;
import javax.jdo.spi.JDOImplHelper;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slim3.controller.HotReloadingClassLoader;
import org.slim3.util.RequestUtil;

/**
 * {@link Filter} for JDO.
 * 
 * @author higa
 * @since 3.0
 * 
 */
public class JDOFilter implements Filter {

    public void init(FilterConfig config) throws ServletException {
    }

    public void destroy() {
    }

    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        doFilter(
            (HttpServletRequest) request,
            (HttpServletResponse) response,
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
        String path = RequestUtil.getPath(request);
        String ext = RequestUtil.getExtension(path);
        PersistenceManager pm = CurrentPersistenceManager.get();
        if ((ext == null || ext.startsWith("s3")) && pm == null) {
            pm = PMF.get().getPersistenceManager();
            CurrentPersistenceManager.set(pm);
            try {
                chain.doFilter(request, response);
            } finally {
                CurrentPersistenceManager.close();
                ClassLoader loader =
                    Thread.currentThread().getContextClassLoader();
                if (loader instanceof HotReloadingClassLoader) {
                    JDOImplHelper.getInstance().unregisterClasses(loader);
                }
            }
        } else {
            chain.doFilter(request, response);
        }
    }
}