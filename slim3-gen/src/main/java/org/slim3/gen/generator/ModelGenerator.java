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
 * Generates a model java file.
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

    public void generate(Printer p) {
        if (modelDesc.getPackageName().length() != 0) {
            p.println("package %s;", modelDesc.getPackageName());
            p.println();
        }
        p.println("import java.io.Serializable;");
        p.println();
        p.println("import com.google.appengine.api.datastore.Key;");
        p.println();
        p.println("import org.slim3.datastore.Attribute;");
        p.println("import org.slim3.datastore.Model;");
        p.println();
        p.println("@Model");
        p.println("public class %s implements Serializable {", modelDesc
            .getSimpleName());
        p.println();
        p.println("    private static final long serialVersionUID = 1L;");
        p.println();
        p.println("    @Attribute(primaryKey = true)");
        p.println("    private Key key;");
        p.println();
        p.println("    @Attribute(version = true)");
        p.println("    private Long version;");
        p.println();
        p.println("    private Integer schemaVersion;");
        p.println();
        p.println("    /**");
        p.println("     * Returns the key.");
        p.println("     *");
        p.println("     * @return the key");
        p.println("     */");
        p.println("    public Key getKey() {");
        p.println("        return key;");
        p.println("    }");
        p.println();
        p.println("    /**");
        p.println("     * Sets the key.");
        p.println("     *");
        p.println("     * @param key");
        p.println("     *            the key");
        p.println("     */");
        p.println("    public void setKey(Key key) {");
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
        p.println();
        p.println("    /**");
        p.println("     * Sets the version.");
        p.println("     *");
        p.println("     * @param version");
        p.println("     *            the version");
        p.println("     */");
        p.println("    public void setVersion(Long version) {");
        p.println("        this.version = version;");
        p.println("    }");
        p.println();
        p.println("    /**");
        p.println("     * Returns the schema version.");
        p.println("     *");
        p.println("     * @return the schema version");
        p.println("     */");
        p.println("    public Integer getSchemaVersion() {");
        p.println("        return schemaVersion;");
        p.println("    }");
        p.println();
        p.println("    /**");
        p.println("     * Sets the schema version.");
        p.println("     *");
        p.println("     * @param schemaVersion");
        p.println("     *            the schema version");
        p.println("     */");
        p.println("    public void setSchemaVersion(Integer schemaVersion) {");
        p.println("        this.schemaVersion = schemaVersion;");
        p.println("    }");
        p.println("}");
    }
}