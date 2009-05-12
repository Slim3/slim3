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
import java.io.IOException;

import org.slim3.gen.Constants;
import org.slim3.gen.desc.ControllerDesc;
import org.slim3.gen.desc.ControllerDescFactory;
import org.slim3.gen.generator.ControllerGenerator;
import org.slim3.gen.generator.ControllerJspGenerator;
import org.slim3.gen.generator.ControllerTestCaseGenerator;
import org.slim3.gen.generator.Generator;
import org.slim3.gen.printer.FilePrinter;
import org.slim3.gen.printer.Printer;

/**
 * @author taedium
 * 
 */
public class GenControllerTask {

    protected File srcDir;

    protected File testDir;

    protected File warDir;

    protected String controllerPath;

    protected String encoding = "UTF-8";

    public void setSrcDir(File srcDir) {
        this.srcDir = srcDir;
    }

    public File getTestDir() {
        return testDir;
    }

    public void setTestDir(File testDir) {
        this.testDir = testDir;
    }

    public void setWarDir(File warDir) {
        this.warDir = warDir;
    }

    public void setControllerPath(String controllerPath) {
        this.controllerPath = controllerPath;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public void execute() throws Exception {
        if (srcDir == null) {
            throw new IllegalStateException("The srcDir parameter is null on "
                    + getClass() + ".");
        }
        if (testDir == null) {
            throw new IllegalStateException("The testDir parameter is null on "
                    + getClass() + ".");
        }
        if (warDir == null) {
            throw new IllegalStateException("The warDir parameter isnull on "
                    + getClass() + ".");
        }
        if (controllerPath == null) {
            throw new IllegalStateException(
                    "The controllerPath parameter is null on " + getClass()
                            + ".");
        }
        String controllerPackageName = findControllerPackageName();
        ControllerDescFactory factory = new ControllerDescFactory(
                controllerPackageName);
        ControllerDesc controllerDesc = factory
                .createControllerDesc(controllerPath);
        generateController(controllerDesc);
        generateControllerTest(controllerDesc);
        generateControllerJsp(controllerDesc);
    }

    protected String findControllerPackageName() throws Exception {
        File file = new File(new File(warDir, "WEB-INF"), "appengine-web.xml");
        return "aaa.bbb";
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

    protected void generateControllerJsp(ControllerDesc controllerDesc)
            throws IOException {
        warDir.mkdirs();
        File jspFile = new File(warDir, controllerDesc.getJspName());
        if (jspFile.exists()) {
            return;
        }
        Generator generator = careateControllerJspGenerator(controllerDesc);
        generate(generator, jspFile);
    }

    protected Generator careateControllerJspGenerator(
            ControllerDesc controllerDesc) {
        return new ControllerJspGenerator(controllerDesc);
    }

    protected void generate(Generator generator, File file) throws IOException {
        Printer printer = null;
        try {
            printer = createPrinter(file);
            generator.generate(printer);
        } finally {
            if (printer != null) {
                printer.close();
            }
        }
    }

    protected Printer createPrinter(File file) throws IOException {
        return new FilePrinter(file, encoding);
    }
}
