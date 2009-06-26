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
package org.slim3.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.slim3.exception.WrapRuntimeException;

/**
 * The {@link ClassLoader} for hot reloading.
 * 
 * @author higa
 * @since 3.0
 */
public class HotReloadingClassLoader extends ClassLoader {

    /**
     * The root package name.
     */
    protected String rootPackageName;

    /**
     * The static package names.
     */
    protected String[] staticPackageNames;

    /**
     * Constructor
     * 
     * @param parentClassLoader
     *            the parent class loader.
     * @param rootPackageName
     *            the root package name
     * @param staticPackageNames
     *            the static package names
     * @throws NullPointerException
     *             if the rootPackageName parameter is null or if the
     *             staticPackageNames parameter is null
     */
    public HotReloadingClassLoader(ClassLoader parentClassLoader,
            String rootPackageName, String[] staticPackageNames)
            throws NullPointerException {
        super(parentClassLoader);
        if (rootPackageName == null) {
            throw new NullPointerException(
                "The rootPackageName parameter is null.");
        }
        if (staticPackageNames == null) {
            throw new NullPointerException(
                "The staticPackageNames parameter is null.");
        }
        this.rootPackageName = rootPackageName;
        this.staticPackageNames = staticPackageNames;
    }

    @Override
    public Class<?> loadClass(String className, boolean resolve)
            throws ClassNotFoundException {
        if (isTarget(className)) {
            Class<?> clazz = findLoadedClass(className);
            if (clazz != null) {
                return clazz;
            }
            int index = className.lastIndexOf('.');
            if (index >= 0) {
                String packageName = className.substring(0, index);
                if (getPackage(packageName) == null) {
                    try {
                        definePackage(
                            packageName,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null);
                    } catch (IllegalArgumentException ignore) {
                    }
                }
            }
            clazz = defineClass(className, resolve);
            if (clazz != null) {
                return clazz;
            }
        }
        return super.loadClass(className, resolve);
    }

    /**
     * Defines the class.
     * 
     * @param className
     *            the class name
     * @param resolve
     *            whether the class is resolved
     * @return the class
     */
    protected Class<?> defineClass(String className, boolean resolve) {
        Class<?> clazz;
        String path = className.replace('.', '/') + ".class";
        InputStream is = getInputStream(path);
        if (is != null) {
            clazz = defineClass(className, is);
            if (resolve) {
                resolveClass(clazz);
            }
            return clazz;
        }
        return null;
    }

    /**
     * Defines the class.
     * 
     * @param className
     *            the class name
     * @param is
     *            the input stream
     * @return the class
     */
    protected Class<?> defineClass(String className, InputStream is) {
        return defineClass(className, getBytes(is));
    }

    /**
     * Defines the class.
     * 
     * @param className
     *            the class name
     * @param bytes
     *            the array of bytes.
     * @return the class
     */
    protected Class<?> defineClass(String className, byte[] bytes) {
        return defineClass(className, bytes, 0, bytes.length);
    }

    /**
     * Returns the input stream.
     * 
     * @param path
     *            the path
     * @return the input stream
     */
    protected InputStream getInputStream(String path) {
        try {
            URL url = getResource(path);
            if (url == null) {
                return null;
            }
            URLConnection connection = url.openConnection();
            connection.setUseCaches(false);
            return connection.getInputStream();
        } catch (IOException ignore) {
            return null;
        }
    }

    /**
     * Returns input stream data as the array of bytes.
     * 
     * @param is
     *            the input stream
     * @return the array of bytes
     * @throws RuntimeException
     *             if {@link IOException} is encountered
     */
    protected byte[] getBytes(InputStream is) throws RuntimeException {
        byte[] bytes = null;
        byte[] buf = new byte[8192];
        try {
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                int n = 0;
                while ((n = is.read(buf, 0, buf.length)) != -1) {
                    baos.write(buf, 0, n);
                }
                bytes = baos.toByteArray();
            } finally {
                if (is != null) {
                    is.close();
                }
            }
        } catch (IOException e) {
            throw new WrapRuntimeException(e);
        }
        return bytes;
    }

    /**
     * Determines if the class is the target of hot deployment.
     * 
     * @param className
     *            the class name
     * @return whether the class is the target of hot deployment
     */
    protected boolean isTarget(String className) {
        if (!className.startsWith(rootPackageName + ".")) {
            return false;
        }
        for (String p : staticPackageNames) {
            if (className.startsWith(rootPackageName + "." + p + ".")) {
                return false;
            }
        }
        return true;
    }
}