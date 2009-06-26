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
 * Represents a Google App Engine configuration file.
 * 
 * @author taedium
 * @since 3.0
 */
public class AppEngineConfig {

    /** the war directory */
    protected final File warDir;

    /**
     * Creates a new {@link AppEngineConfig}.
     * 
     * @param warDir
     *            the war directory
     */
    public AppEngineConfig(File warDir) {
        if (warDir == null) {
            throw new NullPointerException("The warDir parameter is null.");
        }
        this.warDir = warDir;
    }

    /**
     * Returns a base package name of controllers.
     * 
     * @return a base package name of controlle
     * @throws FileNotFoundException
     * @throws XPathExpressionException
     */
    protected String getRootPackageName() throws FileNotFoundException,
            XPathExpressionException {
        XPathFactory factory = XPathFactory.newInstance();
        XPath xpath = factory.newXPath();
        xpath.setNamespaceContext(new NamespaceContext() {
            public String getNamespaceURI(String prefix) {
                if (prefix == null)
                    throw new NullPointerException(
                        "The parameter prefix is null.");
                else if ("pre".equals(prefix))
                    return "http://java.sun.com/xml/ns/javaee";
                else if ("xml".equals(prefix))
                    return XMLConstants.XML_NS_URI;
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
        try {
            String value =
                xpath.evaluate(
                    "/pre:web-app/pre:context-param[1]/pre:param-value",
                    new InputSource(inputStream));
            if (StringUtil.isEmpty(value)) {
                throw new RuntimeException(MessageFormatter
                    .getMessage(MessageCode.SILM3GEN0008));
            }
            return value;
        } finally {
            CloseableUtil.close(inputStream);
        }
    }

}
