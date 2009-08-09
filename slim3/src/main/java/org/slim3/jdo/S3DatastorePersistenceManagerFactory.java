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

import javax.jdo.JDOFatalUserException;
import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManagerFactory;

import org.datanucleus.store.appengine.ConcurrentHashMapHelper;
import org.datanucleus.store.appengine.Utils;
import org.datanucleus.store.appengine.jdo.DatastoreJDOPersistenceManagerFactory;

/**
 * The persistence manager factory for Slim3.
 * 
 * @author higa
 * @since 3.0
 * 
 */
public class S3DatastorePersistenceManagerFactory extends
        DatastoreJDOPersistenceManagerFactory {

    /**
     * Keeps track of the number of instances we've allocated per PMF in this
     * class loader. We do this to try and detect when users are allocating
     * these over and over when they should just be allocating one and reusing
     * it.
     */
    protected static final ConcurrentHashMap<String, AtomicInteger> NUM_INSTANCES_PER_PERSISTENCE_UNIT =
        new ConcurrentHashMap<String, AtomicInteger>();

    /**
     * The error message.
     */
    protected static final String DUPLICATE_PMF_ERROR_FORMAT =
        "Application code attempted to create a PersistenceManagerFactory named %s, but "
            + "one with this name already exists!  Instances of PersistenceManagerFactory are extremely slow "
            + "to create and it is usually not necessary to create one with a given name more than once.  "
            + "Instead, create a singleton and share it throughout your code.  If you really do need "
            + "to create a duplicate PersistenceManagerFactory (such as for a unittest suite), set the "
            + DISABLE_DUPLICATE_PMF_EXCEPTION_PROPERTY
            + " system property to avoid this error.";

    private static final long serialVersionUID = 1L;

    /**
     * @param props
     */
    @SuppressWarnings("unchecked")
    public S3DatastorePersistenceManagerFactory(Map props) {
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
     * @return A PersistenceManagerFactoryImpl with options set according to the
     *         given Properties.
     * @throws JDOFatalUserException
     *             When the user allocates more than one
     *             {@link PersistenceManagerFactory} with the same name, unless
     *             the user has added the
     *             {@link #DISABLE_DUPLICATE_PMF_EXCEPTION_PROPERTY} system
     *             property.
     * @see JDOHelper#getPersistenceManagerFactory(Map)
     */
    @SuppressWarnings( { "unchecked", "finally" })
    public synchronized static PersistenceManagerFactory getPersistenceManagerFactory(
            Map overridingProps) {
        // Extract the properties into a Map allowing for a Properties object
        // being used
        Map<String, Object> overridingMap;
        if (overridingProps instanceof Properties) {
            // Make sure we handle default properties too (SUN Properties class
            // oddness)
            overridingMap = new HashMap<String, Object>();
            for (Enumeration e = ((Properties) overridingProps).propertyNames(); e
                .hasMoreElements();) {
                String param = (String) e.nextElement();
                overridingMap.put(param, ((Properties) overridingProps)
                    .getProperty(param));
            }
        } else {
            overridingMap = overridingProps;
        }

        if (overridingMap == null) {
            overridingMap = Utils.newHashMap();
        }
        // This is an unfortunate way to do things, but I'm not aware of another
        // way to provide a default value for a specific persistence property
        // that already has a default value in the core plugin.xml.
        // plugin.xml in core assigns a default value of UPPERCASE for this
        // property, but we want a different default value for the app engine
        // pluging. If I add this is a persistence property to our own
        // plugin.xml it doesn't always get honored because the plugin
        // persistence properties are not always read in the same order (there's
        // a Hashmap buried in there), and the default value is whichever one
        // gets read first. So, in order to provide a plugin-specific default
        // value that conflicts with the default value for another plugin,
        // we set the property to the default value unless the user has
        // explicitly provided their own value in their config file.
        if (!overridingMap.containsKey("datanucleus.identifier.case")) {
            overridingMap.put("datanucleus.identifier.case", "PreserveCase");
        }
        // Create the PMF and freeze it (JDO spec $11.7)
        final S3DatastorePersistenceManagerFactory pmf =
            new S3DatastorePersistenceManagerFactory(overridingMap);
        pmf.freezeConfiguration();

        if (alreadyAllocated(pmf.getName())) {
            try {
                pmf.close();
            } finally {
                // this exception is more important than any exception that
                // might be
                // raised by close()
                throw new JDOFatalUserException(String.format(
                    DUPLICATE_PMF_ERROR_FORMAT,
                    pmf.getName()));
            }
        }
        return pmf;
    }

    /**
     * Determines if the persistence manager factory specified by the name has
     * been already allocated.
     * 
     * @param name
     *            the persistence manager factory name
     * 
     * @return {@code true} if the user has already allocated a
     *         {@link PersistenceManagerFactory} with the provided name, {@code
     *         false} otherwise.
     */
    protected static boolean alreadyAllocated(String name) {
        // Not all PMFs have names (like those created by Spring), and since we
        // do
        // our duplicate detection based on name we just have to assume that if
        // there isn't a name it isn't a duplicate. We have to short-circuit
        // here
        // because ConcurrentHashMap throws NPE if you pass it a null key.
        if (name == null) {
            return false;
        }
        AtomicInteger count =
            ConcurrentHashMapHelper.getCounter(
                NUM_INSTANCES_PER_PERSISTENCE_UNIT,
                name);
        return count.incrementAndGet() > 1
            && !System.getProperties().containsKey(
                DISABLE_DUPLICATE_PMF_EXCEPTION_PROPERTY);
    }

    @Override
    protected void initialiseOMFContext() {
        omfContext = new S3OMFContext(this);
    }
}