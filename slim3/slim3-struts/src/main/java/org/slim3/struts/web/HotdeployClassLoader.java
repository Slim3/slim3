/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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
package org.slim3.struts.web;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import javax.servlet.http.HttpServletRequest;

import org.slim3.commons.config.Configuration;
import org.slim3.commons.util.StringUtil;
import org.slim3.struts.S3StrutsGlobals;

/**
 * The {@link ClassLoader} for HOT deployment.
 * 
 * @author higa
 * @since 3.0
 */
public class HotdeployClassLoader extends ClassLoader {

    /**
     * The hot class loader key.
     */
    public static final String HOT_CLASS_LOADER_KEY = HotdeployClassLoader.class
            .getName();

    /**
     * Returns the current instance.
     * 
     * @return the current instance
     */
    public static HotdeployClassLoader getCurrentInstance() {
        HttpServletRequest request = WebLocator.getRequest();
        if (request == null) {
            return null;
        }
        return (HotdeployClassLoader) request
                .getAttribute(HOT_CLASS_LOADER_KEY);
    }

    /**
     * Sets the current instance
     * 
     * @param instance
     *            the current instance
     * @throws NullPointerException
     *             if the instance parameter is null
     * @throws IllegalStateException
     *             if the http servlet request is not found
     */
    public static void setCurrentInstance(HotdeployClassLoader instance)
            throws NullPointerException, IllegalStateException {
        if (instance == null) {
            throw new NullPointerException("The instance parameter is null.");
        }
        HttpServletRequest request = WebLocator.getRequest();
        if (request == null) {
            throw new IllegalStateException(
                    "The http servlet request is not found.");
        }
        request.setAttribute(HOT_CLASS_LOADER_KEY, instance);
    }

    /**
     * Remove the current instance.
     * 
     * @throws IllegalStateException
     *             if the http servlet request is not found
     */
    public static void removeCurrentInstance() throws IllegalStateException {
        HttpServletRequest request = WebLocator.getRequest();
        if (request == null) {
            throw new IllegalStateException(
                    "The http servlet request is not found.");
        }
        request.removeAttribute(HOT_CLASS_LOADER_KEY);
    }

    /**
     * Constructor
     * 
     * @param parentClassLoader
     *            the parent class loader.
     */
    public HotdeployClassLoader(ClassLoader parentClassLoader) {
        super(parentClassLoader);
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
                        definePackage(packageName, null, null, null, null,
                                null, null, null);
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
     * Returns the input stream
     * 
     * @param path
     *            the path
     * @return the input stream
     */
    protected InputStream getInputStream(String path) {
        try {
            URL url = Thread.currentThread().getContextClassLoader()
                    .getResource(path);
            if (url == null) {
                return null;
            }
            URLConnection connection = url.openConnection();
            connection.setUseCaches(false);
            return connection.getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
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
            throw new RuntimeException(e);
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
        String packageName = Configuration.getInstance().getValue(
                S3StrutsGlobals.CONTROLLER_PACKAGE_KEY);
        if (StringUtil.isEmpty(packageName)) {
            return false;
        }
        return className.startsWith(packageName + ".");
    }
}