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
import org.slim3.gen.desc.ModelDesc;
import org.slim3.gen.printer.Printer;
import org.slim3.gen.util.ClassUtil;

/**
 * Generates a model test case java file.
 * 
 * @author taedium
 * 
 */
public class ModelTestCaseGenerator implements Generator {

    /** the model description */
    protected final ModelDesc modelDesc;

    /**
     * Creates a new {@link ModelTestCaseGenerator}.
     * 
     * @param modelDesc
     *            the model description
     */
    public ModelTestCaseGenerator(ModelDesc modelDesc) {
        if (modelDesc == null) {
            throw new NullPointerException("The modelDesc parameter is null.");
        }
        this.modelDesc = modelDesc;
    }

    public void generate(Printer p) {
        if (modelDesc.getPackageName().length() != 0) {
            p.println("package %s;", modelDesc.getPackageName());
            p.println();
        }
        if (!ClassConstants.Object
            .equals(modelDesc.getTestCaseSuperclassName())) {
            p.println("import %s;", modelDesc.getTestCaseSuperclassName());
        }
        p.println("import %s;", AnnotationConstants.Test);
        p.println("import static %s.*;", ClassConstants.Assert);
        p.println("import static %s.*;", ClassConstants.CoreMatchers);
        p.println();
        p.print(
            "public class %s%s",
            modelDesc.getSimpleName(),
            Constants.TEST_SUFFIX);
        if (!ClassConstants.Object
            .equals(modelDesc.getTestCaseSuperclassName())) {
            p.print(" extends %s", ClassUtil.getSimpleName(modelDesc
                .getTestCaseSuperclassName()));
        }
        p.println(" {");
        p.println();
        p.println("    private %1$s model = new %1$s();", modelDesc
            .getSimpleName());
        p.println();
        p.println("    @%s", ClassUtil.getSimpleName(AnnotationConstants.Test));
        p.println("    public void test() throws Exception {");
        p.println("        assertThat(model, is(notNullValue()));");
        p.println("    }");
        p.println("}");
    }

}