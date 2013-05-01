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
import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.slim3.gen.generator.Generator;
import org.slim3.gen.message.MessageCode;
import org.slim3.gen.message.MessageFormatter;
import org.slim3.gen.printer.FilePrinter;
import org.slim3.gen.printer.Printer;

/**
 * Represents an abstract task to generate a file.
 * 
 * @author taedium
 * @since 1.0.0
 */
public abstract class AbstractGenFileTask extends Task {

    /** the war directory */
    protected File warDir;

    /** the file encoding */
    protected String encoding = "UTF-8";

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
     * Sets the encoding.
     * 
     * @param encoding
     *            the encoding to set
     */
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    /**
     * Executes this task.
     */
    @Override
    public void execute() {
        try {
            doExecute();
        } catch (Exception e) {
            StringWriter writer = new StringWriter();
            e.printStackTrace(new PrintWriter(writer));
            throw new BuildException(writer.toString());
        }
    }

    /**
     * Executes this task.
     * 
     * @throws Exception
     */
    protected void doExecute() throws Exception {
        if (warDir == null) {
            throw new IllegalStateException("The warDir parameter is null.");
        }
    }

    /**
     * Generates a file.
     * 
     * @param generator
     * @param file
     * @throws IOException
     */
    protected void generateFile(Generator generator, File file)
            throws IOException {
        if (file.exists()) {
            log(MessageFormatter.getSimpleMessage(
                MessageCode.SLIM3GEN0006,
                file.getAbsolutePath()));
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
        log(MessageFormatter.getSimpleMessage(MessageCode.SLIM3GEN0007, file
            .getAbsolutePath()));
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

    /**
     * Creates a web config.
     * 
     * @return a web config
     */
    protected WebConfig createWebConfig() {
        return new WebConfig(warDir);
    }
}