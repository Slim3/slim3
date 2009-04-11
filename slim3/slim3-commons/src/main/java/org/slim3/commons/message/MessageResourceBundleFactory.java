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
package org.slim3.commons.message;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.slim3.commons.config.Configuration;

/**
 * A factory class for {@link MessageResourceBundle}.
 * 
 * @author higa
 * @since 3.0
 */
public final class MessageResourceBundleFactory {

    private static Map<String, MessageResourceBundleCache> bundleCaches = new ConcurrentHashMap<String, MessageResourceBundleCache>();

    private MessageResourceBundleFactory() {
    }

    /**
     * Returns the message bundle. If message bundle is not found, returns null.
     * 
     * @param locale
     *            the locale
     * @param messageBundleName
     *            the message bundle name such as
     *            aaa.foo(WEB-INF/classes/aaa/foo.properties)
     * @return a message bundle
     * @throws NullPointerException
     *             if the locale parameter is null or if the messageBundleName
     *             parameter is null
     */
    public static MessageResourceBundle getBundle(Locale locale,
            String messageBundleName) throws NullPointerException {
        if (locale == null) {
            throw new NullPointerException("The locale parameter is null.");
        }
        if (messageBundleName == null) {
            throw new NullPointerException(
                    "The messageBundleName parameter is null.");
        }
        MessageResourceBundle parentBundle = getBundle(messageBundleName);
        MessageResourceBundle bundle = getBundle(messageBundleName + "_"
                + locale.getLanguage());
        if (bundle != null) {
            bundle.setParent(parentBundle);
            return bundle;
        }
        return parentBundle;
    }

    /**
     * Returns the message bundle.
     * 
     * @param messageBundleName
     *            the message bundle name
     * @return the message bundle
     */
    protected static MessageResourceBundle getBundle(String messageBundleName) {
        String path = messageBundleName.replace('.', '/') + ".properties";
        MessageResourceBundleCache cache = bundleCaches.get(messageBundleName);
        if (cache != null) {
            if (cache.isModified()) {
                cache.setBundle(createBundle(path));
            }
            return cache.getBundle();
        }
        cache = new MessageResourceBundleCache(createBundle(path),
                getFile(path));
        bundleCaches.put(messageBundleName, cache);
        return cache.getBundle();
    }

    /**
     * Returns a file for the path.
     * 
     * @param path
     *            the path
     * @return a file for the path
     */
    protected static File getFile(String path) {
        if (!Configuration.getInstance().isHot()) {
            return null;
        }
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        URL url = loader.getResource(path);
        if (url == null) {
            return null;
        }
        try {
            return new File(URLDecoder.decode(url.getPath(), "UTF-8"))
                    .getAbsoluteFile();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates a message bundle.
     * 
     * @param path
     *            the path
     * @return a message bundle
     */
    protected static MessageResourceBundle createBundle(String path) {
        try {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            URL url = loader.getResource(path);
            if (url == null) {
                return null;
            }
            Properties p = new Properties();
            URLConnection connection = url.openConnection();
            connection.setUseCaches(false);
            InputStream in = connection.getInputStream();
            try {
                p.load(in);
            } finally {
                in.close();
            }
            return new MessageResourceBundle(p);
        } catch (IOException e) {
            throw new IllegalArgumentException(path, e);
        }
    }
}