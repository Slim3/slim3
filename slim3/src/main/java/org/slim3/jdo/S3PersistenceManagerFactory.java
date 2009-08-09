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

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManagerFactory;

import org.datanucleus.jdo.JDOPersistenceManagerFactory;

/**
 * The persistence manager factory for Slim3.
 * 
 * @author higa
 * @since 3.0
 * 
 */
public class S3PersistenceManagerFactory extends JDOPersistenceManagerFactory {

    /**
     * Keeps track of the number of instances we've allocated per PMF in this
     * class loader. We do this to try and detect when users are allocating
     * these over and over when they should just be allocating one and reusing
     * it.
     */
    protected static final ConcurrentHashMap<String, AtomicInteger> NUM_INSTANCES_PER_PERSISTENCE_UNIT =
        new ConcurrentHashMap<String, AtomicInteger>();

    private static final long serialVersionUID = 1L;

    /**
     * @param props
     */
    @SuppressWarnings("unchecked")
    public S3PersistenceManagerFactory(Map props) {
        super(props);
    }

    /**
     * Return a new PersistenceManagerFactoryImpl with options set according to
     * the given Properties. Largely based on the parent class implementation of
     * this method.
     * 
     * @param overridingProps
     *            The Map of properties to initialize the
     *            PersistenceManagerFactory with.
     * @return A PersistenceManagerFactory with options set according to the
     *         given Properties.
     * @see JDOHelper#getPersistenceManagerFactory(Map)
     */
    @SuppressWarnings( { "unchecked" })
    public synchronized static PersistenceManagerFactory getPersistenceManagerFactory(
            Map overridingProps) {
        // Extract the properties into a Map allowing for a Properties object
        // being used
        Map overridingMap = null;
        if (overridingProps instanceof Properties) {
            // Make sure we handle default properties too (SUN Properties class
            // oddness)
            overridingMap = new HashMap();
            for (Enumeration e = ((Properties) overridingProps).propertyNames(); e
                .hasMoreElements();) {
                String param = (String) e.nextElement();
                overridingMap.put(param, ((Properties) overridingProps)
                    .getProperty(param));
            }
        } else {
            overridingMap = overridingProps;
        }
        // Create the PMF and freeze it (JDO spec $11.7)
        S3PersistenceManagerFactory pmf =
            new S3PersistenceManagerFactory(overridingMap);
        pmf.freezeConfiguration();
        return pmf;
    }

    @Override
    protected void initialiseOMFContext() {
        omfContext = new S3OMFContext(this);
    }
}