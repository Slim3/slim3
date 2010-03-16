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
import org.slim3.gen.desc.ServiceDesc;
import org.slim3.gen.printer.Printer;
import org.slim3.gen.util.ClassUtil;

/**
 * Generates a service test case java file.
 * 
 * @author taedium
 * 
 */
public class ServiceTestCaseGenerator implements Generator {

    /** the service description */
    protected final ServiceDesc serviceDesc;

    /**
     * Creates a new {@link ServiceTestCaseGenerator}.
     * 
     * @param serviceDesc
     *            the service description
     */
    public ServiceTestCaseGenerator(ServiceDesc serviceDesc) {
        if (serviceDesc == null) {
            throw new NullPointerException("The serviceDesc parameter is null.");
        }
        this.serviceDesc = serviceDesc;
    }

    public void generate(Printer p) {
        if (serviceDesc.getPackageName().length() != 0) {
            p.println("package %s;", serviceDesc.getPackageName());
            p.println();
        }
        if (!ClassConstants.Object.equals(serviceDesc
            .getTestCaseSuperclassName())) {
            p.println("import %s;", serviceDesc.getTestCaseSuperclassName());
        }
        p.println("import %s;", AnnotationConstants.Test);
        p.println("import static %s.*;", ClassConstants.Assert);
        p.println("import static %s.*;", ClassConstants.CoreMatchers);
        p.println();
        p.print(
            "public class %s%s",
            serviceDesc.getSimpleName(),
            Constants.TEST_SUFFIX);
        if (!ClassConstants.Object.equals(serviceDesc
            .getTestCaseSuperclassName())) {
            p.print(" extends %s", ClassUtil.getSimpleName(serviceDesc
                .getTestCaseSuperclassName()));
        }
        p.println(" {");
        p.println();
        p.println("    private %1$s service = new %1$s();", serviceDesc
            .getSimpleName());
        p.println();
        p.println("    @%s", ClassUtil.getSimpleName(AnnotationConstants.Test));
        p.println("    public void test() throws Exception {");
        p.println("        assertThat(service, is(notNullValue()));");
        p.println("    }");
        p.println("}");
    }
}