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
package org.slim3.tester;

import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.dev.LocalDatastoreService;
import com.google.appengine.tools.development.ApiProxyLocalImpl;
import com.google.apphosting.api.ApiProxy;

/**
 * A tester for local data store.
 * 
 * @author higa
 * @since 3.0
 * 
 */
public class DatastoreTester extends ServiceTester {

    /**
     * The API proxy.
     */
    protected ApiProxyLocalImpl apiProxy;

    /**
     * The data store service.
     */
    protected LocalDatastoreService datastoreService;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        apiProxy = (ApiProxyLocalImpl) ApiProxy.getDelegate();
        apiProxy.setProperty(
            LocalDatastoreService.NO_STORAGE_PROPERTY,
            Boolean.TRUE.toString());
        datastoreService =
            (LocalDatastoreService) apiProxy.getService("datastore_v3");
    }

    @Override
    public void tearDown() throws Exception {
        datastoreService.clearProfiles();
        super.tearDown();
    }

    /**
     * Counts the number of the models.
     * 
     * @param modelClass
     *            the model class
     * @return the number of the models
     * @throws NullPointerException
     *             if the modelClass parameter is null
     */
    public int count(Class<?> modelClass) throws NullPointerException {
        if (modelClass == null) {
            throw new NullPointerException("The modelClass parameter is null.");
        }
        Query query = new Query(modelClass.getSimpleName());
        return DatastoreServiceFactory
            .getDatastoreService()
            .prepare(query)
            .countEntities();
    }
}