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

import java.io.File;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import org.slim3.util.ThrowableUtil;
import org.slim3.util.WrapRuntimeException;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.api.datastore.dev.LocalDatastoreService;
import com.google.appengine.tools.development.ApiProxyLocalImpl;
import com.google.apphosting.api.ApiProxy;

/**
 * A tester for local services.
 * 
 * @author higa
 * @since 3.0
 * 
 */
public class LocalServiceTester {

    /**
     * The name of ApiProxyLocalImpl class.
     */
    protected static final String API_PROXY_LOCAL_IMPL_CLASS_NAME =
        "com.google.appengine.tools.development.ApiProxyLocalImpl";
    /**
     * The name of LocalDatastoreService class.
     */
    protected static final String LOCAL_DATASTORE_SERVICE_CLASS_NAME =
        "com.google.appengine.api.datastore.dev.LocalDatastoreService";

    /**
     * The name of impl directory.
     */
    protected static final String IMPL_DIR_NAME = "impl";

    /**
     * The name of appengine-api-stubs library.
     */
    protected static final String API_STUBS_LIB_NAME =
        "appengine-api-stubs.jar";

    /**
     * The name of appengine-local-runtime library.
     */
    protected static final String LOCAL_RUNTIME_LIB_NAME =
        "appengine-local-runtime.jar";

    /**
     * The name of {@link LocalDatastoreService}.
     */
    protected static final String LOCAL_DETASTORE_SERVICE_NAME = "datastore_v3";
    /**
     * The base path for api proxy local implementation.
     */
    protected String basePath = "build";

    static {
        loadLibraries();
    }

    /**
     * Loads appengine libraries.
     */
    protected static void loadLibraries() {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        if (!(loader instanceof URLClassLoader)) {
            return;
        }
        File libDir = getLibDir();
        File implDir = new File(libDir, IMPL_DIR_NAME);
        List<URL> urls = new ArrayList<URL>();
        try {
            loader.loadClass(API_PROXY_LOCAL_IMPL_CLASS_NAME);
        } catch (ClassNotFoundException e) {
            urls.add(getLibraryURL(implDir, LOCAL_RUNTIME_LIB_NAME));
        }
        try {
            loader.loadClass(LOCAL_DATASTORE_SERVICE_CLASS_NAME);
        } catch (ClassNotFoundException e) {
            urls.add(getLibraryURL(implDir, API_STUBS_LIB_NAME));
        }
        loadLibraries(loader, urls);
    }

    /**
     * Loads the libraries.
     * 
     * @param loader
     *            the {@link ClassLoader}
     * @param urls
     *            the list of {@link URL}s.
     */
    protected static void loadLibraries(ClassLoader loader, List<URL> urls) {
        if (urls.size() > 0) {
            try {
                Method m =
                    URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
                m.setAccessible(true);
                for (URL url : urls) {
                    m.invoke(loader, url);
                }
            } catch (Throwable cause) {
                ThrowableUtil.wrapAndThrow(cause);
            }
        }
    }

    /**
     * Returns a lib directory.
     * 
     * @return a lib directory
     */
    protected static File getLibDir() {
        return new File(ApiProxy.class
            .getProtectionDomain()
            .getCodeSource()
            .getLocation()
            .getFile()).getParentFile().getParentFile();

    }

    /**
     * Gets the library {@link URL}.
     * 
     * @param dir
     *            the directory
     * @param libName
     *            the name of the library
     * @return the library {@link URL}
     */
    protected static URL getLibraryURL(File dir, String libName) {
        File file = new File(dir, libName);
        if (!file.exists()) {
            throw new IllegalArgumentException("The library("
                + libName
                + ") is not found in the directory("
                + dir.getAbsolutePath()
                + ").");
        }
        try {
            return file.toURI().toURL();
        } catch (MalformedURLException e) {
            throw new WrapRuntimeException(e);
        }
    }

    /**
     * Returns the base path.
     * 
     * @return the base path
     */
    public String getBasePath() {
        return basePath;
    }

    /**
     * Sets the base path.
     * 
     * @param basePath
     *            the base path
     */
    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    /**
     * Sets up this tester.
     * 
     * @throws Exception
     *             if an exception has occurred
     * 
     */
    public void setUp() throws Exception {
        ApiProxy.setEnvironmentForCurrentThread(new TestEnvironment());
        ApiProxyLocalImpl delegate = new ApiProxyLocalImpl(new File(basePath)) {
        };
        ApiProxy.setDelegate(delegate);
        delegate.setProperty(
            LocalDatastoreService.NO_STORAGE_PROPERTY,
            Boolean.TRUE.toString());
    }

    /**
     * Tears down this tester.
     * 
     * @throws Exception
     *             if an exception has occurred
     */
    public void tearDown() throws Exception {
        ApiProxyLocalImpl delegate = (ApiProxyLocalImpl) ApiProxy.getDelegate();
        LocalDatastoreService datastoreService =
            (LocalDatastoreService) delegate
                .getService(LOCAL_DETASTORE_SERVICE_NAME);
        datastoreService.clearProfiles();
        for (Transaction tx : DatastoreServiceFactory
            .getDatastoreService()
            .getActiveTransactions()) {
            tx.rollback();
        }
        ApiProxy.setDelegate(null);
        ApiProxy.setEnvironmentForCurrentThread(null);
    }

    /**
     * Counts the number of the model.
     * 
     * @param modelClass
     *            the model class
     * @return the number of the model
     * @throws NullPointerException
     *             if the modelClass parameter is null
     */
    public int count(Class<?> modelClass) throws NullPointerException {
        if (modelClass == null) {
            throw new NullPointerException("The modelClass parameter is null.");
        }
        return count(modelClass.getSimpleName());
    }

    /**
     * Counts the number of the entity.
     * 
     * @param kind
     *            the kind
     * @return the number of the model
     * @throws NullPointerException
     *             if the kind parameter is null
     */
    public int count(String kind) throws NullPointerException {
        if (kind == null) {
            throw new NullPointerException("The kind parameter is null.");
        }
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        return ds.prepare(new Query(kind)).countEntities();
    }
}