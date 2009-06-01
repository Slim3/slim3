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

import org.slim3.gen.ClassConstants;
import org.slim3.gen.desc.ControllerDesc;
import org.slim3.gen.printer.Printer;
import org.slim3.gen.util.ClassUtil;

/**
 * Generates source codes of a controller java file.
 * 
 * @author taedium
 * @since 3.0
 * 
 */
public class ControllerGenerator implements Generator {

    /** the controller description */
    protected final ControllerDesc controllerDesc;

    /**
     * Creates a new {@link ControllerGenerator}.
     * 
     * @param controllerDesc
     *            the controller description
     */
    public ControllerGenerator(ControllerDesc controllerDesc) {
        if (controllerDesc == null) {
            throw new NullPointerException(
                "The controllerDesc parameter is null.");
        }
        this.controllerDesc = controllerDesc;
    }

    @Override
    public void generate(Printer p) {
        p.println("package %s;", controllerDesc.getPackageName());
        p.println();
        p.println("import %s;", controllerDesc.getSuperclassName());
        p.println("import %s;", ClassConstants.Navigation);
        p.println();
        p.println("public class %s extends %s {", controllerDesc
            .getSimpleName(), ClassUtil.getSimpleName(controllerDesc
            .getSuperclassName()));
        p.println();
        p.println("    @Override");
        p.println("    public %s run() {", ClassUtil
            .getSimpleName(ClassConstants.Navigation));
        p.println("        return forward(\"%s\");", controllerDesc
            .getSimpleViewName());
        p.println("    }");
        p.println("}");
    }
}
