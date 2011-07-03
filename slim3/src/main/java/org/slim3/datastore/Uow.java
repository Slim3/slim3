/*
 * Copyright 2004-2011 the original author or authors.
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

import java.util.ConcurrentModificationException;
import java.util.logging.Logger;

/**
 * Unit of Work template.
 * 
 * @author higayasuo
 * @since 1.0.12
 * 
 */
public final class Uow {

    /**
     * The default max retries.
     */
    public final static int DEFAULT_MAX_RETRIES = 5;

    /**
     * The default sleep time.
     */
    public final static long DEFAULT_SLEEP_TIME = 100;

    private static final Logger logger = Logger.getLogger(Uow.class.getName());

    /**
     * Runs the unit of work template.
     * 
     * @param uow
     *            the unit of work
     * @param <T>
     *            return type
     * @return a result
     * @throws NullPointerException
     *             if the uow parameter is null
     */
    @SuppressWarnings("unchecked")
    public static final <T> T run(AbstractUow uow) throws NullPointerException {
        return (T) run(uow, DEFAULT_MAX_RETRIES, DEFAULT_SLEEP_TIME);
    }

    /**
     * Runs the unit of work template.
     * 
     * @param uow
     *            the unit of work
     * @param maxRetries
     *            the max retries
     * @param <T>
     *            return type
     * @return a result
     * @throws NullPointerException
     *             if the uow parameter is null
     */
    @SuppressWarnings("unchecked")
    public static final <T> T run(AbstractUow uow, int maxRetries)
            throws NullPointerException {
        return (T) run(uow, maxRetries, DEFAULT_SLEEP_TIME);
    }

    /**
     * Runs the unit of work template.
     * 
     * @param uow
     *            the unit of work
     * @param maxRetries
     *            the max retries
     * @param sleepTime
     *            the sleep time
     * @param <T>
     *            return type
     * @return a result
     * @throws NullPointerException
     *             if the uow parameter is null
     */
    @SuppressWarnings("unchecked")
    public static final <T> T run(AbstractUow uow, int maxRetries,
            long sleepTime) throws NullPointerException {
        if (uow == null) {
            throw new NullPointerException(
                "The uow parameter must not be null.");
        }
        int retries = 0;
        T ret;
        while (true) {
            uow.beginTransaction();
            try {
                ret = (T) uow.run();
                uow.commit();
                return ret;
            } catch (ConcurrentModificationException e) {
                if (retries < maxRetries) {
                    retries++;
                    logger.warning("Concurrent modification exception: "
                        + e.getMessage());
                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException ignore) {
                    }
                    continue;
                }
                throw e;
            } finally {
                uow.rollback();
            }
        }
    }

    private Uow() {
    }
}