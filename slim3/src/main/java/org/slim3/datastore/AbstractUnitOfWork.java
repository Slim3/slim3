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
package org.slim3.datastore;

import java.util.logging.Logger;

/**
 * An abstract class for Unit of Work
 * 
 * @author higayasuo
 * @since 1.0.12
 * 
 */
public abstract class AbstractUnitOfWork {

    public final static int MAX_RETRIES = 5;

    private static final Logger logger =
        Logger.getLogger(AbstractUnitOfWork.class.getName());

    public final void run(int maxRetries) {
        /*
         * while (true) { beginTransaction(); try { doRun(); commit(); break; }
         * catch (ConcurrentModificationException e) {
         * log.warning("concurrent modification for predicate:" + p + " err:" +
         * e.getMessage()); if (retries <= 0) { throw e; } } catch (Exception e)
         * { log.warning("unable to process predicate:" + p + " err:" +
         * e.getMessage()); throw e; } finally { rollback(); } int min = 200; //
         * min sleep ms int max = 2000; // max sleep ms int sleep = min + (int)
         * (Math.random() * ((max - min) + 1)); log.warning("sleep: " + sleep +
         * " ms then retrying predicate:" + p); Thread.sleep(sleep); retries--;
         * 
         * }
         */
    }

    protected abstract void beginTransaction();

    protected abstract void doRun();

    protected abstract void commit();

    protected abstract void rollback();
}