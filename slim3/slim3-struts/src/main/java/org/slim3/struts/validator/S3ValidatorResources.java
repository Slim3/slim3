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
package org.slim3.struts.validator;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.digester.Digester;
import org.apache.commons.digester.xmlrules.DigesterLoader;
import org.apache.commons.validator.Form;
import org.apache.commons.validator.FormSet;
import org.apache.commons.validator.ValidatorResources;
import org.slim3.commons.cleaner.Cleanable;
import org.slim3.commons.cleaner.Cleaner;
import org.xml.sax.SAXException;

/**
 * {@link ValidatorResources} for Slim3.
 * 
 * @author higa
 * @since 3.0
 * 
 */
public class S3ValidatorResources extends ValidatorResources {

    private static final long serialVersionUID = 1L;

    /**
     * The place where DTD is registered.
     */
    protected static final String REGISTRATIONS[] = {
            "-//Apache Software Foundation//DTD Commons Validator Rules Configuration 1.0//EN",
            "/org/apache/commons/validator/resources/validator_1_0.dtd",
            "-//Apache Software Foundation//DTD Commons Validator Rules Configuration 1.0.1//EN",
            "/org/apache/commons/validator/resources/validator_1_0_1.dtd",
            "-//Apache Software Foundation//DTD Commons Validator Rules Configuration 1.1//EN",
            "/org/apache/commons/validator/resources/validator_1_1.dtd",
            "-//Apache Software Foundation//DTD Commons Validator Rules Configuration 1.1.3//EN",
            "/org/apache/commons/validator/resources/validator_1_1_3.dtd",
            "-//Apache Software Foundation//DTD Commons Validator Rules Configuration 1.2.0//EN",
            "/org/apache/commons/validator/resources/validator_1_2_0.dtd",
            "-//Apache Software Foundation//DTD Commons Validator Rules Configuration 1.3.0//EN",
            "/org/apache/commons/validator/resources/validator_1_3_0.dtd" };

    /**
     * Whether this class is initialized.
     */
    protected volatile boolean initialized = false;

    /**
     * The map for {@link Form}.
     */
    protected Map<String, Form> forms = new ConcurrentHashMap<String, Form>();

    /**
     * Constructor.
     */
    public S3ValidatorResources() {
    }

    /**
     * Constructor.
     * 
     * @param streams
     *            the array of {@link InputStream}.
     * @throws IOException
     *             if {@link IOException} is encountered
     * @throws SAXException
     *             if {@link SAXException} is encountered
     */
    public S3ValidatorResources(InputStream[] streams) throws IOException,
            SAXException {
        URL rulesUrl = ValidatorResources.class
                .getResource("digester-rules.xml");
        Digester digester = DigesterLoader.createDigester(rulesUrl);
        digester.setNamespaceAware(true);
        digester.setValidating(true);
        digester.setUseContextClassLoader(true);
        for (int i = 0; i < REGISTRATIONS.length; i += 2) {
            URL url = ValidatorResources.class
                    .getResource(REGISTRATIONS[i + 1]);
            if (url != null) {
                digester.register(REGISTRATIONS[i], url.toString());
            }
        }
        for (int i = 0; i < streams.length; i++) {
            digester.push(this);
            digester.parse(streams[i]);
        }
        initialize();
    }

    /**
     * Initializes this class.
     */
    protected void initialize() {
        Cleaner.add(new Cleanable() {
            public void clean() {
                forms.clear();
                initialized = false;
            }
        });
        initialized = true;
    }

    @Override
    public Form getForm(Locale locale, String name) {
        if (!initialized) {
            initialize();
        }
        return forms.get(name);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void addFormSet(FormSet formSet) {
        Map<String, Form> formMap = formSet.getForms();
        for (Iterator<String> i = formMap.keySet().iterator(); i.hasNext();) {
            String name = i.next();
            forms.put(name, formMap.get(name));
        }
    }
}