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

import org.slim3.gen.Constants;
import org.slim3.gen.desc.DaoDesc;
import org.slim3.gen.printer.Printer;
import org.slim3.gen.util.ClassUtil;

/**
 * Generates a dao java file.
 * 
 * @author taedium
 * 
 */
public class DaoGenerator implements Generator {

    /** the dao description */
    protected final DaoDesc daoDesc;

    /**
     * Creates a new {@link DaoGenerator}.
     * 
     * @param daoDesc
     *            the dao description.
     */
    public DaoGenerator(DaoDesc daoDesc) {
        if (daoDesc == null) {
            throw new NullPointerException("The daoDesc parameter is null.");
        }
        this.daoDesc = daoDesc;
    }

    @Override
    public void generate(Printer p) {
        p.println("package %s;", daoDesc.getPackageName());
        p.println();
        p.println("import java.util.List;");
        p.println("import java.util.logging.Logger;");
        p.println("import javax.jdo.PersistenceManager;");
        p.println("import %s;", daoDesc.getModelClassName());
        p.println("import %s;", daoDesc.getModelClassName()
            + Constants.META_SUFFIX);
        p.println("import %s;", daoDesc.getSuperclassName());
        p.println();
        p.println(
            "public class %s extends %s<%s> {",
            daoDesc.getSimpleName(),
            ClassUtil.getSimpleName(daoDesc.getSuperclassName()),
            ClassUtil.getSimpleName(daoDesc.getModelClassName()));
        p.println();
        p
            .println(
                "    private static final %1$s m = new %1$s();",
                (ClassUtil.getSimpleName(daoDesc.getModelClassName()) + Constants.META_SUFFIX));
        p.println();
        p
            .println(
                "    private static final Logger logger = Logger.getLogger(%s.class.getName());",
                daoDesc.getSimpleName());
        p.println();
        p.println("    public %s(PersistenceManager pm) {", daoDesc
            .getSimpleName());
        p.println("        super(pm, %s.class);", ClassUtil
            .getSimpleName(daoDesc.getModelClassName()));
        p.println("    }");
        p.println();
        p.println("    public List<%s> findAll() {", ClassUtil
            .getSimpleName(daoDesc.getModelClassName()));
        p.println("        return from().getResultList();");
        p.println("    }");
        p.println();
        p.println("}");
    }
}
