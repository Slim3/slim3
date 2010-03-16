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

import org.slim3.gen.AnnotationConstants;
import org.slim3.gen.ClassConstants;
import org.slim3.gen.Constants;
import org.slim3.gen.desc.GWTServiceImplDesc;
import org.slim3.gen.printer.Printer;
import org.slim3.gen.util.ClassUtil;

/**
 * Generates a GWT service implemetation test case java file.
 * 
 * @author taedium
 * 
 */
public class GWTServiceImplTestCaseGenerator implements Generator {

    /** the service implementation description */
    protected final GWTServiceImplDesc serviceImplDesc;

    /**
     * Creates a new {@link GWTServiceImplTestCaseGenerator}.
     * 
     * @param serviceImplDesc
     *            the service implementation description
     */
    public GWTServiceImplTestCaseGenerator(GWTServiceImplDesc serviceImplDesc) {
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
        if (!ClassConstants.Object.equals(serviceImplDesc
            .getTestCaseSuperclassName())) {
            p
                .println("import %s;", serviceImplDesc
                    .getTestCaseSuperclassName());
        }
        p.println("import %s;", AnnotationConstants.Test);
        p.println("import static %s.*;", ClassConstants.Assert);
        p.println("import static %s.*;", ClassConstants.CoreMatchers);
        p.println();
        p.print(
            "public class %s%s",
            serviceImplDesc.getSimpleName(),
            Constants.TEST_SUFFIX);
        if (!ClassConstants.Object.equals(serviceImplDesc
            .getTestCaseSuperclassName())) {
            p.print(" extends %s", ClassUtil.getSimpleName(serviceImplDesc
                .getTestCaseSuperclassName()));
        }
        p.println(" {");
        p.println();
        p.println("    private %1$s service = new %1$s();", serviceImplDesc
            .getSimpleName());
        p.println();
        p.println("    @%s", ClassUtil.getSimpleName(AnnotationConstants.Test));
        p.println("    public void test() throws Exception {");
        p.println("        assertThat(service, is(notNullValue()));");
        p.println("    }");
        p.println("}");
    }
}