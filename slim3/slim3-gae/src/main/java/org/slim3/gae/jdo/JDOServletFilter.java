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
package org.slim3.gae.jdo;

import java.io.IOException;

import javax.jdo.PersistenceManager;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * {@link Filter} for JDO.
 * 
 * @author higa
 * @since 3.0
 * 
 */
public class JDOServletFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        if (PM.getCurrent() != null) {
            throw new ServletException(
                    "The persistence manager is already attached to the current thread.");
        }
        PersistenceManager pm = PMF.getPersistenceManagerFactory()
                .getPersistenceManager();
        PM.setCurrent(pm);
        try {
            chain.doFilter(request, response);
        } finally {
            if (pm != PM.getCurrent()) {
                throw new ServletException(
                        "The current persistence manager is not same as the first persistence manager attached to the current thread.");
            }
            PM.setCurrent(null);
            pm.close();
        }
    }

}
