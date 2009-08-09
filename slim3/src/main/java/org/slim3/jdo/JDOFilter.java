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
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;
import javax.jdo.spi.JDOImplHelper;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.slim3.controller.FrontController;
import org.slim3.controller.HotReloadingClassLoader;

/**
 * {@link FrontController} for JDO.
 * 
 * @author higa
 * @since 3.0
 * 
 */
public class JDOFilter implements Filter {

    private static final Logger logger =
        Logger.getLogger(JDOFilter.class.getName());

    @Override
    public void init(FilterConfig arg0) throws ServletException {
    }

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        PersistenceManager pm = CurrentPersistenceManager.get();
        if (pm == null) {
            pm = PMF.get().getPersistenceManager();
            CurrentPersistenceManager.set(pm);
            try {
                chain.doFilter(request, response);
            } finally {
                try {
                    Transaction tx = pm.currentTransaction();
                    if (tx.isActive()) {
                        tx.rollback();
                    }
                } catch (Throwable e) {
                    logger.log(Level.WARNING, e.getMessage(), e);
                }
                try {
                    pm.close();
                } catch (Throwable e) {
                    logger.log(Level.WARNING, e.getMessage(), e);
                }
                CurrentPersistenceManager.set(null);
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