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
import java.io.PrintWriter;
import java.io.StringWriter;

import org.slim3.gen.generator.Generator;
import org.slim3.gen.printer.FilePrinter;
import org.slim3.gen.printer.Printer;

/**
 * @author taedium
 * 
 */
public abstract class AbstractTask {

    protected File warDir;

    protected String controllerPath;

    protected String encoding = "UTF-8";

    public void setWarDir(File warDir) {
        this.warDir = warDir;
    }

    public void setControllerPath(String controllerPath) {
        this.controllerPath = controllerPath;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public void execute() {
        try {
            doExecute();
        } catch (Exception e) {
            StringWriter writer = new StringWriter();
            e.printStackTrace(new PrintWriter(writer));
            throw new RuntimeException(writer.toString());
        }
    }

    protected abstract void doExecute() throws Exception;

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
