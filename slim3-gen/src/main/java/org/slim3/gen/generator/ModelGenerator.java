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

import org.slim3.gen.desc.ModelDesc;
import org.slim3.gen.printer.Printer;

/**
 * Generates a JDO model java file.
 * 
 * @author taedium
 * 
 */
public class ModelGenerator implements Generator {

    /** the model description */
    protected final ModelDesc modelDesc;

    /**
     * Creates a new {@link ModelGenerator}.
     * 
     * @param modelDesc
     *            the model description
     */
    public ModelGenerator(ModelDesc modelDesc) {
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
        p.println("import java.io.Serializable;");
        p.println();
        p.println("import javax.jdo.annotations.Extension;");
        p.println("import javax.jdo.annotations.IdGeneratorStrategy;");
        p.println("import javax.jdo.annotations.IdentityType;");
        p.println("import javax.jdo.annotations.PersistenceCapable;");
        p.println("import javax.jdo.annotations.Persistent;");
        p.println("import javax.jdo.annotations.PrimaryKey;");
        p.println("import javax.jdo.annotations.Version;");
        p.println("import javax.jdo.annotations.VersionStrategy;");
        p.println();
        p
            .println("@PersistenceCapable(identityType = IdentityType.APPLICATION)");
        p
            .println("@Version(strategy = VersionStrategy.VERSION_NUMBER, column = \"version\")");
        p.println("public class %s implements Serializable {", modelDesc
            .getSimpleName());
        p.println();
        p.println("    private static final long serialVersionUID = 1L;");
        p.println();
        p.println("    @PrimaryKey");
        p
            .println("    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)");
        p
            .println("    @Extension(vendorName = \"datanucleus\", key = \"gae.encoded-pk\", value = \"true\")");
        p.println("    private String key;");
        p.println();
        p.println("    @Persistent");
        p.println("    private Long version = 1L;");
        p.println();
        p.println("    /**");
        p.println("     * Returns the key.");
        p.println("     *");
        p.println("     * @return the key");
        p.println("     */");
        p.println("    public String getKey() {");
        p.println("        return key;");
        p.println("    }");
        p.println();
        p.println("    /**");
        p.println("     * Sets the key.");
        p.println("     *");
        p.println("     * @param key");
        p.println("     *            the key");
        p.println("     */");
        p.println("    public void setKey(String key) {");
        p.println("        this.key = key;");
        p.println("    }");
        p.println();
        p.println("    /**");
        p.println("     * Returns the version.");
        p.println("     *");
        p.println("     * @return the version");
        p.println("     */");
        p.println("    public Long getVersion() {");
        p.println("        return version;");
        p.println("    }");
        p.println("}");
    }
}