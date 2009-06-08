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

import org.slim3.gen.desc.ViewDesc;
import org.slim3.gen.desc.ViewDescFactory;
import org.slim3.gen.generator.Generator;
import org.slim3.gen.generator.ViewGenerator;
import org.slim3.gen.printer.FilePrinter;
import org.slim3.gen.printer.Printer;

/**
 * Represents a task to generate a view file.
 * 
 * @author taedium
 * @since 3.0
 */
public class GenViewTask extends AbstractTask {

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

    @Override
    public void doExecute() throws IOException {
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
        ViewDescFactory viewDescFactory = createViewDescFactory();
        ViewDesc viewDesc = viewDescFactory.createViewDesc(path);
        generateView(viewDesc);
    }

    /**
     * Creates a {@link ViewDescFactory}.
     * 
     * @return a factory
     */
    protected ViewDescFactory createViewDescFactory() {
        return new ViewDescFactory();
    }

    /***
     * Generates a view.
     * 
     * @param viewDesc
     *            the view description.
     * @throws IOException
     */
    protected void generateView(ViewDesc viewDesc) throws IOException {
        File viewDir = new File(warDir, viewDesc.getDirName());
        viewDir.mkdirs();
        File viewFile = new File(viewDir, viewDesc.getFileName());
        Generator generator = careateViewGenerator(viewDesc);
        generate(generator, viewFile);
    }

    /**
     * Creates a {@link Generator}.
     * 
     * @param viewDesc
     *            the view description.
     * @return a generator
     */
    protected Generator careateViewGenerator(ViewDesc viewDesc) {
        return new ViewGenerator(viewDesc);
    }

    /**
     * Generates a file.
     * 
     * @param generator
     * @param file
     * @throws IOException
     */
    protected void generate(Generator generator, File file) throws IOException {
        if (file.exists()) {
            log("Already exists. Skipped generation. ("
                + file.getAbsolutePath()
                + ")");
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
        log("Generated. (" + file.getAbsolutePath() + ")");
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
