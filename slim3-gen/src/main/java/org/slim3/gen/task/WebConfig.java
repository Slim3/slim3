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
package org.slim3.gen.task;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Iterator;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.slim3.gen.message.MessageCode;
import org.slim3.gen.message.MessageFormatter;
import org.slim3.gen.util.CloseableUtil;
import org.slim3.gen.util.StringUtil;
import org.xml.sax.InputSource;

/**
 * Represents a web configuration file.
 * 
 * @author taedium
 * @since 3.0
 */
public class WebConfig {

    /** the war directory */
    protected final File warDir;

    /**
     * Creates a new {@link WebConfig}.
     * 
     * @param warDir
     *            the war directory
     */
    public WebConfig(File warDir) {
        if (warDir == null) {
            throw new NullPointerException("The warDir parameter is null.");
        }
        this.warDir = warDir;
    }

    /**
     * Returns a root package name.
     * 
     * @return a root package name
     * @throws FileNotFoundException
     * @throws XPathExpressionException
     */
    protected String getRootPackageName() throws FileNotFoundException,
            XPathExpressionException {
        XPathFactory factory = XPathFactory.newInstance();
        XPath xpath = factory.newXPath();
        xpath.setNamespaceContext(new NamespaceContext() {
            public String getNamespaceURI(String prefix) {
                if (prefix == null) {
                    throw new NullPointerException(
                        "The parameter prefix is null.");
                } else if ("javaee".equals(prefix)) {
                    return "http://java.sun.com/xml/ns/javaee";
                } else if ("j2ee".equals(prefix)) {
                    return "http://java.sun.com/xml/ns/j2ee";
                } else if ("xml".equals(prefix)) {
                    return XMLConstants.XML_NS_URI;
                }
                return XMLConstants.NULL_NS_URI;
            }

            public String getPrefix(String uri) {
                throw new UnsupportedOperationException("getPrefix");
            }

            public Iterator<?> getPrefixes(String uri) {
                throw new UnsupportedOperationException("getPrefixes");
            }
        });
        File file = new File(new File(warDir, "WEB-INF"), "web.xml");
        InputStream inputStream = new FileInputStream(file);
        String value = null;
        try {
            value =
                xpath
                    .evaluate(
                        "/javaee:web-app/javaee:context-param[1]/javaee:param-value",
                        new InputSource(inputStream));
        } finally {
            CloseableUtil.close(inputStream);
        }
        if (!StringUtil.isEmpty(value)) {
            return value;
        }
        inputStream = new FileInputStream(file);
        try {
            value =
                xpath.evaluate(
                    "/j2ee:web-app/j2ee:context-param[1]/j2ee:param-value",
                    new InputSource(inputStream));
        } finally {
            CloseableUtil.close(inputStream);
        }
        if (!StringUtil.isEmpty(value)) {
            return value;
        }
        throw new RuntimeException(MessageFormatter
            .getMessage(MessageCode.SILM3GEN0008));

    }

}
