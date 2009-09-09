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

import java.lang.reflect.Field;

import org.datanucleus.OMFContext;
import org.datanucleus.PersistenceConfiguration;
import org.datanucleus.metadata.MetaDataManager;
import org.datanucleus.store.AbstractStoreManager;
import org.datanucleus.store.StoreDataManager;
import org.datanucleus.store.StoreManager;
import org.slim3.exception.WrapRuntimeException;
import org.slim3.util.Cleanable;
import org.slim3.util.Cleaner;

/**
 * The object manager context for Slim3.
 * 
 * @author higa
 * @since 3.0
 * 
 */
public class S3OMFContext extends OMFContext {

    /**
     * MetaDataManager for handling the MetaData for this PMF.
     */
    protected MetaDataManager metaDataManager = new S3MetaDataManager(this);

    /**
     * Whether this class is initialized.
     */
    protected volatile boolean initialized = false;

    /**
     * Manager for the data definition in the datastore.
     */
    protected StoreDataManager storeDataMgr;

    /**
     * Constructor.
     * 
     * @param persistenceConfig
     *            the persistence configuration
     */
    public S3OMFContext(PersistenceConfiguration persistenceConfig) {
        super(persistenceConfig);
    }

    /**
     * Constructor.
     * 
     * @param persistenceConfig
     *            the persistence configuration
     * @param context
     *            the context
     */
    public S3OMFContext(PersistenceConfiguration persistenceConfig, int context) {
        super(persistenceConfig, context);
        initialize();
    }

    @Override
    public MetaDataManager getMetaDataManager() {
        return metaDataManager;
    }

    @Override
    public StoreManager getStoreManager() {
        if (!initialized) {
            initialize();
        }
        return super.getStoreManager();
    }

    @Override
    public void setStoreManager(StoreManager storeMgr) {
        super.setStoreManager(storeMgr);
        try {
            Field field =
                AbstractStoreManager.class.getDeclaredField("storeDataMgr");
            field.setAccessible(true);
            storeDataMgr = (StoreDataManager) field.get(storeMgr);
        } catch (NoSuchFieldException e) {
            throw new WrapRuntimeException(e);
        } catch (IllegalArgumentException e) {
            throw new WrapRuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new WrapRuntimeException(e);
        }
    }

    /**
     * Initializes this class.
     */
    protected void initialize() {
        Cleaner.add(new Cleanable() {
            public void clean() {
                if (storeDataMgr != null) {
                    storeDataMgr.clear();
                }
                initialized = false;
            }
        });
        initialized = true;
    }
}