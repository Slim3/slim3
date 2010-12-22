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
package org.slim3.datastore;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.google.appengine.api.datastore.Transaction;
import com.google.apphosting.api.DeadlineExceededException;

/**
 * {@link Filter} for Datastore.
 * 
 * @author higa
 * @since 1.0.0
 * 
 */
public class DatastoreFilter implements Filter {

    private static final Logger logger =
        Logger.getLogger(DatastoreFilter.class.getName());

    public void init(FilterConfig config) throws ServletException {
    }

    public void destroy() {
    }

    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        try {
            GlobalTransaction.clearActiveTransactions();
            chain.doFilter(request, response);
        } catch (DeadlineExceededException dee) {
            for (GlobalTransaction tx : Datastore.getActiveGlobalTransactions()) {
                try {
                    tx.rollbackAsync();
                    logger.info("The global transaction("
                        + tx.getId()
                        + ") was rolled back asynchronously.");
                } catch (Throwable t) {
                    logger.log(Level.WARNING, t.getMessage(), t);
                }
            }
            throw dee;
        } finally {
            for (GlobalTransaction tx : Datastore.getActiveGlobalTransactions()) {
                try {
                    tx.rollback();
                    logger.info("The global transaction("
                        + tx.getId()
                        + ") was rolled back.");
                } catch (Throwable t) {
                    logger.log(Level.WARNING, t.getMessage(), t);
                }
            }
            for (Transaction tx : Datastore.getActiveTransactions()) {
                try {
                    tx.rollback();
                    logger.info("The local transaction("
                        + tx.getId()
                        + ") was rolled back.");
                } catch (Throwable t) {
                    logger.log(Level.WARNING, t.getMessage(), t);
                }
            }
        }
    }
}