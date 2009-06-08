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
import org.slim3.gen.Constants;
import org.slim3.gen.desc.ControllerDesc;
import org.slim3.gen.desc.ControllerDescFactory;
import org.slim3.gen.generator.ControllerGenerator;
import org.slim3.gen.generator.ControllerTestCaseGenerator;
import org.slim3.gen.generator.Generator;
import org.slim3.gen.printer.FilePrinter;
import org.slim3.gen.printer.Printer;

/**
 * Represents a task to generate a controller java file.
 * 
 * @author taedium
 * @since 3.0
 * 
 */
public class GenControllerTask extends AbstractTask {

    /** the source directory */
    protected File srcDir;

    /** the test source directory */
    protected File testDir;

    /** the war directory */
    protected File warDir;

    /** the controller path */
    protected String controllerPath;

    /** the file encoding */
    protected String encoding = "UTF-8";

    /** the superclass name */
    protected String superclassName = ClassConstants.JDOController;

    /** the superclass name of testcase */
    protected String testCaseSuperclassName =
        ClassConstants.JDOControllerTestCase;

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
     * Sets the controllerPath.
     * 
     * @param controllerPath
     *            the controllerPath to set
     */
    public void setControllerPath(String controllerPath) {
        this.controllerPath = controllerPath;
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
     * Sets the superclassName.
     * 
     * @param superclassName
     *            the superclassName to set
     */
    public void setSuperclassName(String superclassName) {
        this.superclassName = superclassName;
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
        if (controllerPath == null) {
            throw new IllegalStateException(
                "The controllerPath parameter is null.");
        }
        String path =
            controllerPath.startsWith("/") ? controllerPath : "/"
                + controllerPath;
        AppEngineConfig config = new AppEngineConfig(warDir);
        String controllerPackageName = config.getControllerPackageName();
        ControllerDescFactory factory =
            createControllerDescFactory(controllerPackageName);
        ControllerDesc controllerDesc = factory.createControllerDesc(path);
        generateController(controllerDesc);
        generateControllerTestCase(controllerDesc);
    }

    /**
     * Creates a {@link ControllerDescFactory}.
     * 
     * @param controllerPackageName
     *            the base package name of controllers.
     * @return a factory
     */
    protected ControllerDescFactory createControllerDescFactory(
            String controllerPackageName) {
        return new ControllerDescFactory(
            controllerPackageName,
            superclassName,
            testCaseSuperclassName);
    }

    /**
     * Generates a controller.
     * 
     * @param controllerDesc
     *            the controller description
     * @throws IOException
     */
    protected void generateController(ControllerDesc controllerDesc)
            throws IOException {
        File packageDir =
            new File(srcDir, controllerDesc.getPackageName().replace(
                ".",
                File.separator));
        packageDir.mkdirs();
        File javaFile =
            new File(packageDir, controllerDesc.getSimpleName().replace(
                '.',
                '/')
                + ".java");
        String className =
            controllerDesc.getPackageName()
                + "."
                + controllerDesc.getSimpleName();
        Generator generator = careateControllerGenerator(controllerDesc);
        generate(generator, javaFile, className);
    }

    /**
     * Creates a {@link Generator}.
     * 
     * @param controllerDesc
     *            the controller description
     * @return a generator
     */
    protected Generator careateControllerGenerator(ControllerDesc controllerDesc) {
        return new ControllerGenerator(controllerDesc);
    }

    /**
     * Generates a controller test case.
     * 
     * @param controllerDesc
     *            the controller description
     * @throws IOException
     */
    protected void generateControllerTestCase(ControllerDesc controllerDesc)
            throws IOException {
        File packageDir =
            new File(testDir, controllerDesc.getPackageName().replace(
                ".",
                File.separator));
        packageDir.mkdirs();
        File javaFile =
            new File(packageDir, controllerDesc.getSimpleName()
                + Constants.TEST_SUFFIX
                + ".java");
        String className =
            controllerDesc.getPackageName()
                + "."
                + controllerDesc.getSimpleName()
                + Constants.TEST_SUFFIX;
        Generator generator =
            careateControllerTestCaseGenerator(controllerDesc);
        generate(generator, javaFile, className);
    }

    /**
     * Creates a {@link Generator}.
     * 
     * @param controllerDesc
     *            the controller description
     * @return a generator
     */
    protected Generator careateControllerTestCaseGenerator(
            ControllerDesc controllerDesc) {
        return new ControllerTestCaseGenerator(controllerDesc);
    }

    /**
     * Generates a file.
     * 
     * @param generator
     *            the generator
     * @param file
     *            the file to be generated
     * @param className
     *            the class name
     * @throws IOException
     */
    protected void generate(Generator generator, File file, String className)
            throws IOException {
        if (file.exists()) {
            log("Already exists. Skipped. (" + className + ".java:0)");
            return;
        }
        Printer printer = null;
        try {
            printer = createPrinter(file);
            generator.generate(printer);
        } finally {
            if (printer != null) {
                printer.close();
            }
        }
        log("Generated. (" + className + ".java:0)");
    }

    /**
     * Creates a printer.
     * 
     * @param file
     *            the file
     * @return a printer.
     * @throws IOException
     */
    protected Printer createPrinter(File file) throws IOException {
        return new FilePrinter(file, encoding);
    }
}
