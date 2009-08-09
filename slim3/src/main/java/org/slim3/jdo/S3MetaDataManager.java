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

import org.datanucleus.ClassLoaderResolver;
import org.datanucleus.OMFContext;
import org.datanucleus.jdo.metadata.JDOMetaDataManager;
import org.datanucleus.metadata.AbstractClassMetaData;
import org.slim3.util.Cleanable;
import org.slim3.util.Cleaner;

/**
 * The meta data manager for Slim3.
 * 
 * @author higa
 * @since 3.0
 * 
 */
public class S3MetaDataManager extends JDOMetaDataManager {

    /**
     * Whether this class is initialized.
     */
    protected volatile boolean initialized = false;

    /**
     * Constructor.
     * 
     * @param ctxt
     *            the object manager factory context
     */
    public S3MetaDataManager(OMFContext ctxt) {
        super(ctxt);
        initialize();
    }

    @SuppressWarnings("unchecked")
    @Override
    public synchronized AbstractClassMetaData getMetaDataForClassInternal(
            Class c, ClassLoaderResolver clr) {
        if (!initialized) {
            initialize();
        }
        return super.getMetaDataForClassInternal(c, clr);
    }

    /**
     * Initializes this class.
     */
    protected void initialize() {
        Cleaner.add(new Cleanable() {
            @Override
            public void clean() {
                classMetaDataByClass.clear();
                classesWithoutPersistenceInfo.clear();
                classMetaDataByInterface.clear();
                initialized = false;
            }
        });
        initialized = true;
    }
}