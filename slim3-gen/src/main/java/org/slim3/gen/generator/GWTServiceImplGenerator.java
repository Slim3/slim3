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
import org.slim3.gen.desc.GWTServiceImplDesc;
import org.slim3.gen.printer.Printer;
import org.slim3.gen.util.ClassUtil;

/**
 * Generates a GWT service implemetation java file.
 * 
 * @author taedium
 * 
 */
public class GWTServiceImplGenerator implements Generator {

    /** the service implementation description */
    protected final GWTServiceImplDesc serviceImplDesc;

    /**
     * Creates a new {@link GWTServiceImplGenerator}.
     * 
     * @param serviceImplDesc
     *            the service implementation description
     */
    public GWTServiceImplGenerator(GWTServiceImplDesc serviceImplDesc) {
        if (serviceImplDesc == null) {
            throw new NullPointerException(
                "The serviceImplDesc parameter is null.");
        }
        this.serviceImplDesc = serviceImplDesc;
    }

    public void generate(Printer p) {
        if (serviceImplDesc.getPackageName().length() != 0) {
            p.println("package %s;", serviceImplDesc.getPackageName());
            p.println();
        }
        p.println("import %s;", serviceImplDesc.getServiceClassName());
        if (!ClassConstants.Object.equals(serviceImplDesc.getSuperclassName())) {
            p.println("import %s;", serviceImplDesc.getSuperclassName());
        }
        p.println();
        p.print("public class %s", serviceImplDesc.getSimpleName());
        if (!ClassConstants.Object.equals(serviceImplDesc.getSuperclassName())) {
            p.print(" extends %s", serviceImplDesc.getSuperclassName());
        }
        p.println(" implements %s {", ClassUtil.getSimpleName(serviceImplDesc
            .getServiceClassName()));
        p.println();
        p.println("}");
    }
}