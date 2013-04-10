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

import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.tools.JavaFileObject;

import org.slim3.gen.desc.ClassDesc;
import org.slim3.gen.generator.Generator;
import org.slim3.gen.printer.FilePrinter;
import org.slim3.gen.printer.Printer;

/**
 * Represents the support class for generating java file.
 * 
 * @author taedium
 * @since 1.0.0
 * 
 */
public class GenerateSupport {

    /** the processing environment */
    protected final ProcessingEnvironment processingEnv;

    /**
     * Creates a new {@link GenerateSupport}.
     * 
     * @param processingEnv
     *            the environment
     */
    public GenerateSupport(ProcessingEnvironment processingEnv) {
        if (processingEnv == null) {
            throw new NullPointerException("The env parameter is null.");
        }
        this.processingEnv = processingEnv;
    }

    /**
     * Generates a java file.
     * 
     * @param generator
     *            the generator.
     * @param classDesc
     *            the class description.
     * @param classElement
     *            the class declaration.
     */
    public void generate(Generator generator, ClassDesc classDesc,
            Element classElement) {
        if (generator == null) {
            throw new NullPointerException("The generator parameter is null.");
        }
        if (classDesc == null) {
            throw new NullPointerException("The classDesc parameter is null.");
        }
        Filer filer = processingEnv.getFiler();
        Printer printer = null;
        try {
            JavaFileObject fileObject =
                filer.createSourceFile(
                    classDesc.getQualifiedName(),
                    classElement);
            printer = createPrinter(fileObject.openWriter());
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
