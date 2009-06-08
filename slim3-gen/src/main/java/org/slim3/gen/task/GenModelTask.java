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
import java.io.IOException;

import javax.xml.xpath.XPathExpressionException;

import org.slim3.gen.ClassConstants;

/**
 * @author taedium
 * 
 */
public class GenModelTask extends AbstractTask {

    /** the source directory */
    protected File srcDir;

    /** the test source directory */
    protected File testDir;

    /** the war directory */
    protected File warDir;

    protected String packageName;

    protected String simpleName;

    /** the file encoding */
    protected String encoding = "UTF-8";

    /** the superclass name of testcase */
    protected String testCaseSuperclassName = ClassConstants.JDOTestCase;

    /**
     * Sets the srcDir.
     * 
     * @param srcDir
     *            the srcDir to set
     */
    public void setSrcDir(File srcDir) {
        this.srcDir = srcDir;
    }

    /**
     * Sets the testDir.
     * 
     * @param testDir
     *            the testDir to set
     */
    public void setTestDir(File testDir) {
        this.testDir = testDir;
    }

    /**
     * Sets the warDir.
     * 
     * @param warDir
     *            the warDir to set
     */
    public void setWarDir(File warDir) {
        this.warDir = warDir;
    }

    /**
     * Sets the packageName.
     * 
     * @param packageName
     *            the packageName to set
     */
    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    /**
     * Sets the simpleName.
     * 
     * @param simpleName
     *            the simpleName to set
     */
    public void setSimpleName(String simpleName) {
        this.simpleName = simpleName;
    }

    /**
     * Sets the encoding.
     * 
     * @param encoding
     *            the encoding to set
     */
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    /**
     * Sets the testCaseSuperclassName.
     * 
     * @param testCaseSuperclassName
     *            the testCaseSuperclassName to set
     */
    public void setTestCaseSuperclassName(String testCaseSuperclassName) {
        this.testCaseSuperclassName = testCaseSuperclassName;
    }

    @Override
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
        if (simpleName == null) {
            throw new IllegalStateException("The simpleName parameter is null.");
        }
        String modelPackageName = getModelPackageName();
        // ControllerDescFactory factory =
        // createControllerDescFactory(controllerPackageName);
        // ControllerDesc controllerDesc = factory.createControllerDesc(path);
        // generateController(controllerDesc);
        // generateControllerTestCase(controllerDesc);
    }

    protected String getModelPackageName() throws IOException,
            XPathExpressionException {
        if (packageName != null) {
            return packageName;
        }
        AppEngineConfig config = new AppEngineConfig(warDir);
        String controllerPackageName = config.getControllerPackageName();
        int pos = controllerPackageName.lastIndexOf(".");
        String rootPackageName =
            pos > 0 ? controllerPackageName.substring(0, pos) : null;
        if (rootPackageName != null) {
            return controllerPackageName + ".model";
        }
        return "model";
    }

}
