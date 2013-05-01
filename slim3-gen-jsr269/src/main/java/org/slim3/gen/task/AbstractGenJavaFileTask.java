/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
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
import org.slim3.gen.desc.ClassDesc;
import org.slim3.gen.generator.Generator;
import org.slim3.gen.message.MessageCode;
import org.slim3.gen.message.MessageFormatter;
import org.slim3.gen.printer.Printer;

/**
 * Represents an abstract task to generate a java file.
 * 
 * @author taedium
 * @since 1.0.0
 */
public abstract class AbstractGenJavaFileTask extends AbstractGenFileTask {

    /** the source directory */
    protected File srcDir;

    /** the test source directory */
    protected File testDir;

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

    @Override
    protected void doExecute() throws Exception {
        super.doExecute();
        if (srcDir == null) {
            throw new IllegalStateException("The srcDir parameter is null.");
        }
        if (testDir == null) {
            throw new IllegalStateException("The testDir parameter is null.");
        }
    }

    /**
     * Creates a java file.
     * 
     * @param classDesc
     *            the class description
     * @return a java file.
     */
    protected JavaFile createJavaFile(ClassDesc classDesc) {
        return new JavaFile(srcDir, classDesc);
    }

    /**
     * Creates a java file of test case.
     * 
     * @param classDesc
     *            the class description
     * @return a java file.
     */
    protected JavaFile createTestCaseJavaFile(ClassDesc classDesc) {
        return new JavaFile(testDir, classDesc, Constants.TEST_SUFFIX);
    }

    /**
     * Generates a java file.
     * 
     * @param generator
     *            the generator
     * @param javaFile
     *            the java file to be generated
     * @throws IOException
     */
    protected void generateJavaFile(Generator generator, JavaFile javaFile)
            throws IOException {
        File file = javaFile.getFile();
        String className = javaFile.getClassName();
        if (file.exists()) {
            log(MessageFormatter.getSimpleMessage(
                MessageCode.SLIM3GEN0004,
                className));
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
        log(MessageFormatter.getSimpleMessage(
            MessageCode.SLIM3GEN0005,
            className));
    }

}
