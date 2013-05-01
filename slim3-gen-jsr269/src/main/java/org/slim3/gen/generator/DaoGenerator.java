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
import org.slim3.gen.desc.DaoDesc;
import org.slim3.gen.printer.Printer;
import org.slim3.gen.util.ClassUtil;

/**
 * Generates a dao java file.
 *
 * @author sue445
 * @since 1.0.12
 *
 */
public class DaoGenerator implements Generator {

    /** the dao description */
    protected final DaoDesc daoDesc;

    /**
     * Creates a new {@link DaoGenerator}.
     *
     * @param daoDesc
     *            the dao description
     */
    public DaoGenerator(DaoDesc daoDesc) {
        if (daoDesc == null) {
            throw new NullPointerException("The daoDesc parameter is null.");
        }
        this.daoDesc = daoDesc;
    }

    /**
     * {@inheritDoc}
     */
    public void generate(Printer p) {
        if (daoDesc.getPackageName().length() != 0) {
            p.println("package %s;", daoDesc.getPackageName());
            p.println();
        }
        p.println("import %s;", ClassConstants.DaoBase);
        p.println();
        p.println("import %s;", daoDesc.getModelClassName());
        p.println();
        String modelSimpleClassName = ClassUtil.getSimpleName(daoDesc.getModelClassName());
        p.println("public class %s extends %s<%s>{", daoDesc.getSimpleName(), ClassUtil.getSimpleName(ClassConstants.DaoBase), modelSimpleClassName);
        p.println();
        p.println("}");
    }
}