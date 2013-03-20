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
package org.slim3.gen.processor;

import java.io.IOException;
import java.io.Writer;

import org.slim3.gen.desc.ClassDesc;
import org.slim3.gen.generator.Generator;
import org.slim3.gen.printer.FilePrinter;
import org.slim3.gen.printer.Printer;

import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.apt.Filer;

/**
 * Represents the support class for generating java file.
 * 
 * @author taedium
 * @since 1.0.0
 * 
 */
@SuppressWarnings("deprecation")
public class GenerateSupport {

    /** the environment */
    protected final AnnotationProcessorEnvironment env;

    /**
     * Creates a new {@link GenerateSupport}.
     * 
     * @param env
     *            the environment
     */
    public GenerateSupport(AnnotationProcessorEnvironment env) {
        if (env == null) {
            throw new NullPointerException("The env parameter is null.");
        }
        this.env = env;
    }

    /**
     * Generates a java file.
     * 
     * @param generator
     *            the generator.
     * @param classDesc
     *            the class description.
     */
    public void generate(Generator generator, ClassDesc classDesc) {
        if (generator == null) {
            throw new NullPointerException("The generator parameter is null.");
        }
        if (classDesc == null) {
            throw new NullPointerException("The classDesc parameter is null.");
        }
        Filer filer = env.getFiler();
        Printer printer = null;
        try {
            printer =
                createPrinter(filer.createSourceFile(classDesc
                    .getQualifiedName()));
            generator.generate(printer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (printer != null) {
                printer.close();
            }
        }
    }

    /**
     * Creates a {@link Printer}.
     * 
     * @param writer
     *            the writer object.
     * @return a printer object.
     * @throws IOException
     *             if an I/O error occurred
     */
    protected Printer createPrinter(Writer writer) throws IOException {
        return new FilePrinter(writer);
    }
}
