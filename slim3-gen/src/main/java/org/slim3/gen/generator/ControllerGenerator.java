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
package org.slim3.gen.generator;

import org.slim3.gen.ClassConstants;
import org.slim3.gen.desc.ControllerDesc;
import org.slim3.gen.printer.Printer;
import org.slim3.gen.util.ClassUtil;

/**
 * Generates a controller java file.
 * 
 * @author taedium
 * @since 1.0.0
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

    public void generate(Printer p) {
        if (controllerDesc.getPackageName().length() != 0) {
            p.println("package %s;", controllerDesc.getPackageName());
            p.println();
        }
        p.println("import %s;", controllerDesc.getSuperclassName());
        p.println("import %s;", ClassConstants.Navigation);
        p.println();
        p.println("public class %s extends %s {", controllerDesc
            .getSimpleName(), ClassUtil.getSimpleName(controllerDesc
            .getSuperclassName()));
        p.println();
        p.println("    @Override");
        p.println("    public %s run() throws Exception {", ClassUtil
            .getSimpleName(ClassConstants.Navigation));
        if (controllerDesc.isUseView()) {
            p.println("        return forward(\"%s\");", controllerDesc
                .getSimpleViewName());
        } else {
            p.println("        return null;");
        }
        p.println("    }");
        p.println("}");
    }
}
