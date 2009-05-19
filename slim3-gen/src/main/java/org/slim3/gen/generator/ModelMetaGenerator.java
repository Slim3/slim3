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

import javax.annotation.Generated;

import org.slim3.gen.ClassConstants;
import org.slim3.gen.Constants;
import org.slim3.gen.ProductInfo;
import org.slim3.gen.desc.AttributeDesc;
import org.slim3.gen.desc.ModelDesc;
import org.slim3.gen.printer.Printer;

/**
 * Generates source codes of a JDO model meta class.
 * 
 * @author taedium
 * @since 3.0
 * 
 */
public class ModelMetaGenerator implements Generator {

    /** the model description */
    protected final ModelDesc modelDesc;

    /**
     * Creates a new {@link ModelMetaGenerator}.
     * 
     * @param the
     *            model description
     */
    public ModelMetaGenerator(ModelDesc modelDesc) {
        if (modelDesc == null) {
            throw new NullPointerException("The modelDesc parameter is null.");
        }
        this.modelDesc = modelDesc;
    }

    @Override
    public void generate(Printer p) {
        if (!modelDesc.getPackageName().isEmpty()) {
            p.println("package %s;", modelDesc.getPackageName());
            p.println();
        }
        p.println();
        p.println("@%s(value = { \"%s\", \"%s\" }, date = \"%tF %<tT\")",
                Generated.class.getName(), ProductInfo.getName(), ProductInfo
                        .getVersion(), new Date());
        p.println("public final class %s extends %s<%s> {", modelDesc
                .getSimpleName()
                + Constants.META_SUFFIX, ClassConstants.ModelMeta, modelDesc
                .getSimpleName());
        p.println();
        p.println("    public %s() {", modelDesc.getSimpleName()
                + Constants.META_SUFFIX);
        p.println("        super(%s.class);", modelDesc.getSimpleName());
        p.println("    }");
        p.println();
        for (AttributeDesc desc : modelDesc.getAttributeDescList()) {
            if (desc.isModelType()) {
                p.println("    public %1$s%2$s %3$s = new %1$s%2$s();", desc
                        .getClassName(), Constants.META_SUFFIX, desc.getName());
            } else {
                if (desc.getElementClassName() == null) {
                    p
                            .println(
                                    "    public %1$s %2$s = new %1$s(\"%2$s\", %3$s.class);",
                                    ClassConstants.AttributeMeta, desc
                                            .getName(), desc.getClassName());
                } else {
                    p
                            .println(
                                    "    public %1$s %2$s = new %1$s(\"%2$s\", %3$s.class, %4$s.class);",
                                    ClassConstants.AttributeMeta, desc
                                            .getName(), desc.getClassName(),
                                    desc.getElementClassName());
                }
            }
            p.println();
        }
        p.print("}");
    }
}
