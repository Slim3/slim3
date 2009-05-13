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
import java.io.PrintWriter;
import java.io.StringWriter;

import org.slim3.gen.generator.Generator;
import org.slim3.gen.printer.FilePrinter;
import org.slim3.gen.printer.Printer;

/**
 * An abstract class for Ant tasks.
 * 
 * @author taedium
 * @since 3.0
 * 
 */
public abstract class AbstractTask {

    /** the war directory */
    protected File warDir;

    /** the controller path */
    protected String controllerPath;

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
     * Executes this task.
     */
    public void execute() {
        try {
            doExecute();
        } catch (Exception e) {
            StringWriter writer = new StringWriter();
            e.printStackTrace(new PrintWriter(writer));
            throw new RuntimeException(writer.toString());
        }
    }

    /**
     * Executes this task.
     * 
     * @throws Exception
     */
    protected abstract void doExecute() throws Exception;

    /**
     * Generates a file.
     * 
     * @param generator
     * @param file
     * @throws IOException
     */
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
        System.out.println("generated. " + file.getAbsolutePath());
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
