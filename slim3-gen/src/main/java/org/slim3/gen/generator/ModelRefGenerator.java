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
package org.slim3.gen.generator;

import java.util.Date;

import org.slim3.gen.ClassConstants;
import org.slim3.gen.ProductInfo;
import org.slim3.gen.desc.ModelRefDesc;
import org.slim3.gen.printer.Printer;

/**
 * Generates a model meta java file.
 * 
 * @author taedium
 * @since 3.0
 * 
 */
public class ModelRefGenerator implements Generator {

    /** the model ref description */
    protected final ModelRefDesc modelRefDesc;

    /**
     * Creates a new {@link ModelRefGenerator}.
     * 
     * @param modelRefDesc
     *            the model ref description
     */
    public ModelRefGenerator(ModelRefDesc modelRefDesc) {
        if (modelRefDesc == null) {
            throw new NullPointerException(
                "The modelRefDesc parameter is null.");
        }
        this.modelRefDesc = modelRefDesc;
    }

    public void generate(Printer printer) {
        if (printer == null) {
            throw new NullPointerException("The printer parameter is null.");
        }
        if (modelRefDesc.getPackageName().length() != 0) {
            printer.println("package %s;", modelRefDesc.getPackageName());
            printer.println();
        }
        printer
            .println(
                "//@javax.annotation.Generated(value = { \"%s\", \"%s\" }, date = \"%tF %<tT\")",
                ProductInfo.getName(),
                ProductInfo.getVersion(),
                new Date());
        printer.println("/** */");
        printer.println("public final class %s extends %s<%s> {", modelRefDesc
            .getSimpleName(), ClassConstants.ModelRef, modelRefDesc
            .getModelClassName());
        printer.println();
        printer.println("    private static final long serialVersionUID = 1L;");
        printer.println();
        printer.println("    /** */");
        printer.println("    public %s() {", modelRefDesc.getSimpleName());
        printer.println("        super(%1$s.class);", modelRefDesc
            .getModelClassName());
        printer.println("    }");
        printer.println();
        printer.print("}");
    }

}
