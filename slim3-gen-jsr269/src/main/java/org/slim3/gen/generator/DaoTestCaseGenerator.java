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
import org.slim3.gen.desc.DaoDesc;
import org.slim3.gen.printer.Printer;
import org.slim3.gen.util.ClassUtil;

/**
 * Generates a dao test case java file.
 *
 * @author sue445
 * @since 1.0.12
 *
 */
public class DaoTestCaseGenerator implements Generator {

    /** the dao description */
    protected final DaoDesc daoDesc;

    /**
     * Creates a new {@link DaoTestCaseGenerator}.
     *
     * @param daoDesc
     *            the dao description
     */
    public DaoTestCaseGenerator(DaoDesc daoDesc) {
        if (daoDesc == null) {
            throw new NullPointerException("The daoDesc parameter is null.");
        }
        this.daoDesc = daoDesc;
    }

    public void generate(Printer p) {
        if (daoDesc.getPackageName().length() != 0) {
            p.println("package %s;", daoDesc.getPackageName());
            p.println();
        }
        if (!ClassConstants.Object
            .equals(daoDesc.getTestCaseSuperclassName())) {
            p.println("import %s;", daoDesc.getTestCaseSuperclassName());
        }
        p.println("import %s;", AnnotationConstants.Test);
        p.println("import static %s.*;", ClassConstants.Assert);
        p.println("import static %s.*;", ClassConstants.CoreMatchers);
        p.println();
        p.print(
            "public class %s%s",
            daoDesc.getSimpleName(),
            Constants.TEST_SUFFIX);
        if (!ClassConstants.Object
            .equals(daoDesc.getTestCaseSuperclassName())) {
            p.print(" extends %s", ClassUtil.getSimpleName(daoDesc
                .getTestCaseSuperclassName()));
        }
        p.println(" {");
        p.println();
        p.println("    private %1$s dao = new %1$s();", daoDesc
            .getSimpleName());
        p.println();
        p.println("    @%s", ClassUtil.getSimpleName(AnnotationConstants.Test));
        p.println("    public void test() throws Exception {");
        p.println("        assertThat(dao, is(notNullValue()));");
        p.println("    }");
        p.println("}");
    }

}