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
import org.slim3.gen.desc.ModelDesc;
import org.slim3.gen.printer.Printer;
import org.slim3.gen.util.ClassUtil;

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
        if (ClassConstants.Object.equals(modelDesc.getSuperclassName())) {
            p.println("import com.google.appengine.api.datastore.Key;");
            p.println();
            p.println("import org.slim3.datastore.Attribute;");
            p.println("import org.slim3.datastore.Model;");
        } else {
            p.println("import org.slim3.datastore.Model;");
            p.println();
            p.println("import %s;", modelDesc.getSuperclassName());
        }
        p.println();
        p.println("@Model(%1$s = 1)", AnnotationConstants.schemaVersion);
        if (ClassConstants.Object.equals(modelDesc.getSuperclassName())) {
            p.println("public class %s implements Serializable {", modelDesc
                .getSimpleName());
        } else {
            p.println(
                "public class %s extends %s implements Serializable {",
                modelDesc.getSimpleName(),
                ClassUtil.getSimpleName(modelDesc.getSuperclassName()));
        }
        p.println();
        p.println("    private static final long serialVersionUID = 1L;");
        p.println();
        if (ClassConstants.Object.equals(modelDesc.getSuperclassName())) {
            p.println("    @Attribute(primaryKey = true)");
            p.println("    private Key key;");
            p.println();
            p.println("    @Attribute(version = true)");
            p.println("    private Long version;");
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
            p.println("    @Override");
            p.println("    public int hashCode() {");
            p.println("        final int prime = 31;");
            p.println("        int result = 1;");
            p
                .println("        result = prime * result + ((key == null) ? 0 : key.hashCode());");
            p.println("        return result;");
            p.println("    }");
            p.println();
            p.println("    @Override");
            p.println("    public boolean equals(Object obj) {");
            p.println("        if (this == obj) {");
            p.println("            return true;");
            p.println("        }");
            p.println("        if (obj == null) {");
            p.println("            return false;");
            p.println("        }");
            p.println("        if (getClass() != obj.getClass()) {");
            p.println("            return false;");
            p.println("        }");
            p.println("        %1$s other = (%1$s) obj;", modelDesc
                .getSimpleName());
            p.println("        if (key == null) {");
            p.println("            if (other.key != null) {");
            p.println("                return false;");
            p.println("            }");
            p.println("        } else if (!key.equals(other.key)) {");
            p.println("            return false;");
            p.println("        }");
            p.println("        return true;");
            p.println("    }");
        }
        p.println("}");
    }
}