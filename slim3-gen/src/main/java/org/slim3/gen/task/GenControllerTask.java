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
package org.slim3.gen.task;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.slim3.gen.Constants;
import org.slim3.gen.desc.ControllerDesc;
import org.slim3.gen.desc.ControllerDescFactory;
import org.slim3.gen.generator.ControllerGenerator;
import org.slim3.gen.generator.ControllerTestCaseGenerator;
import org.slim3.gen.generator.Generator;
import org.slim3.gen.util.CloseableUtil;
import org.xml.sax.InputSource;

/**
 * @author taedium
 * 
 */
public class GenControllerTask extends AbstractTask {

    protected File srcDir;

    protected File testDir;

    public void setSrcDir(File srcDir) {
        this.srcDir = srcDir;
    }

    public File getTestDir() {
        return testDir;
    }

    public void setTestDir(File testDir) {
        this.testDir = testDir;
    }

    public void doExecute() throws IOException, XPathExpressionException {
        if (srcDir == null) {
            throw new IllegalStateException("The srcDir parameter is null.");
        }
        if (testDir == null) {
            throw new IllegalStateException("The testDir parameter is null.");
        }
        if (warDir == null) {
            throw new IllegalStateException("The warDir parameter is null.");
        }
        if (controllerPath == null) {
            throw new IllegalStateException(
                    "The controllerPath parameter is null.");
        }
        String path = controllerPath.startsWith("/") ? controllerPath : "/"
                + controllerPath;
        String controllerPackageName = findControllerPackageName();
        ControllerDescFactory factory = createControllerDescFactory(controllerPackageName);
        ControllerDesc controllerDesc = factory.createControllerDesc(path);
        generateController(controllerDesc);
        generateControllerTest(controllerDesc);
    }

    protected ControllerDescFactory createControllerDescFactory(
            String controllerPackageName) {
        return new ControllerDescFactory(controllerPackageName);
    }

    protected String findControllerPackageName() throws FileNotFoundException,
            XPathExpressionException {
        XPathFactory factory = XPathFactory.newInstance();
        XPath xpath = factory.newXPath();
        xpath.setNamespaceContext(new NamespaceContext() {
            public String getNamespaceURI(String prefix) {
                if (prefix == null)
                    throw new NullPointerException(
                            "The parameter prefix is null.");
                else if ("pre".equals(prefix))
                    return "http://appengine.google.com/ns/1.0";
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
        File file = new File(new File(warDir, "WEB-INF"), "appengine-web.xml");
        InputStream inputStream = new FileInputStream(file);
        try {
            String value = xpath
                    .evaluate(
                            "/pre:appengine-web-app/pre:system-properties/pre:property[@name='slim3.controllerPackage']/@value",
                            new InputSource(inputStream));
            if (value == null) {
                throw new RuntimeException(
                        "The system-property 'slim3.controllerPackage' is not found in appengine-web.xml.");
            }
            return value;
        } finally {
            CloseableUtil.close(inputStream);
        }
    }

    protected void generateController(ControllerDesc controllerDesc)
            throws IOException {
        File packageDir = new File(srcDir, controllerDesc.getPackageName()
                .replace(".", File.separator));
        packageDir.mkdirs();
        File javaFile = new File(packageDir, controllerDesc.getSimpleName()
                .replace('.', '/')
                + ".java");
        if (javaFile.exists()) {
            return;
        }
        Generator generator = careateControllerGenerator(controllerDesc);
        generate(generator, javaFile);
    }

    protected Generator careateControllerGenerator(ControllerDesc controllerDesc) {
        return new ControllerGenerator(controllerDesc);
    }

    protected void generateControllerTest(ControllerDesc controllerDesc)
            throws IOException {
        File packageDir = new File(testDir, controllerDesc.getPackageName()
                .replace(".", File.separator));
        packageDir.mkdirs();
        File javaFile = new File(packageDir, controllerDesc.getSimpleName()
                + Constants.TEST_SUFFIX + ".java");
        if (javaFile.exists()) {
            return;
        }
        Generator generator = careateControllerTestCaseGenerator(controllerDesc);
        generate(generator, javaFile);
    }

    protected Generator careateControllerTestCaseGenerator(
            ControllerDesc controllerDesc) {
        return new ControllerTestCaseGenerator(controllerDesc);
    }

}
