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
import java.net.URL;
import java.util.Enumeration;

import org.datanucleus.ClassLoaderResolver;
import org.datanucleus.exceptions.ClassNotResolvedException;

/**
 * The resolver of {@link ClassLoader} for Slim3. This class uses just only
 * current context class loader.
 * 
 * @author higa
 * @since 3.0
 * 
 */
public class S3ClassLoaderResolver implements ClassLoaderResolver {

    /**
     * Constructor.
     */
    public S3ClassLoaderResolver() {
    }

    /**
     * Constructor.
     * 
     * @param pmLoader
     *            the class loader initialized by the persistence manager
     *            creation
     */
    public S3ClassLoaderResolver(ClassLoader pmLoader) {
    }

    public Class<?> classForName(String name) {
        return classForName(name, false);
    }

    public Class<?> classForName(String name, ClassLoader primary) {
        return classForName(name);
    }

    public Class<?> classForName(String name, boolean initialize) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        try {
            return Class.forName(name, initialize, loader);
        } catch (ClassNotFoundException e) {
            throw new ClassNotResolvedException(e.getMessage(), e);
        }
    }

    public Class<?> classForName(String name, ClassLoader primary,
            boolean initialize) {
        return classForName(name, initialize);
    }

    public URL getResource(String resourceName, ClassLoader primary) {
        if (resourceName.startsWith("/")) {
            resourceName = resourceName.substring(1);
        }
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        return loader.getResource(resourceName);
    }

    public Enumeration<URL> getResources(String resourceName,
            ClassLoader primary) throws IOException {
        if (resourceName.startsWith("/")) {
            resourceName = resourceName.substring(1);
        }
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        return loader.getResources(resourceName);
    }

    @SuppressWarnings("unchecked")
    public boolean isAssignableFrom(String className_1, Class class_2) {
        if (className_1 == null || class_2 == null) {
            return false;
        }
        return classForName(className_1).isAssignableFrom(class_2);
    }

    @SuppressWarnings("unchecked")
    public boolean isAssignableFrom(Class class_1, String className_2) {
        if (class_1 == null || className_2 == null) {
            return false;
        }
        return class_1.isAssignableFrom(classForName(className_2));
    }

    public boolean isAssignableFrom(String className_1, String className_2) {
        if (className_1 == null || className_2 == null) {
            return false;
        }
        return classForName(className_1).isAssignableFrom(
            classForName(className_2));
    }

    public void registerUserClassLoader(ClassLoader loader) {
    }

    public void setPrimary(ClassLoader primary) {
    }

    public void setRuntimeClassLoader(ClassLoader loader) {
    }

    public void unsetPrimary() {
    }
}
