/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Generated;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;

import org.slim3.gen.ClassConstants;
import org.slim3.gen.Constants;
import org.slim3.gen.ProductInfo;
import org.slim3.gen.desc.AttributeDesc;
import org.slim3.gen.desc.AttributeDescCollector;
import org.slim3.gen.printer.Printer;

/**
 * Generates source codes of a JDO model meta class.
 * 
 * @author taedium
 * @since 3.0
 * 
 */
public class JDOModelMetaGenerator implements Generator {

    /** the processing environment */
    protected final ProcessingEnvironment processingEnv;

    /** the package name */
    protected final String packageName;

    /** the collection of imported names */
    protected final ImportedNames importedNames;

    /** the collection of reserved names */
    protected final ReservedNames reservedNames;

    /** the list of attribute description */
    protected final List<AttributeDesc> attributeDescList;

    /**
     * Creates a new {@link JDOModelMetaGenerator}.
     * 
     * @param processingEnv
     *            the processing environment.
     * @param element
     *            the element object of JDO model.
     * @param qualifiedName
     *            qualified name of the class to be generated.
     */
    public JDOModelMetaGenerator(ProcessingEnvironment processingEnv,
            TypeElement element, String qualifiedName) {
        if (processingEnv == null) {
            throw new NullPointerException(
                    "The processingEnv parameter is null.");
        }
        if (element == null) {
            throw new NullPointerException("The element parameter is null.");
        }
        if (qualifiedName == null) {
            throw new NullPointerException(
                    "The qualifiedName parameter is null.");
        }
        this.processingEnv = processingEnv;
        int pos = qualifiedName.lastIndexOf('.');
        if (pos < 0) {
            this.packageName = "";
        } else {
            this.packageName = qualifiedName.substring(0, pos);
        }
        this.importedNames = new ImportedNames(packageName);
        this.reservedNames = new ReservedNames(element.getQualifiedName()
                .toString(), qualifiedName);

        AttributeDescCollector collector = new AttributeDescCollector(
                new ArrayList<AttributeDesc>(), importedNames, processingEnv);
        attributeDescList = collector.scan(element);
    }

    @Override
    public void generate(Printer p) {
        if (!packageName.isEmpty()) {
            p.println("package %s;", packageName);
            p.println();
        }
        for (String importedName : importedNames) {
            p.println("import %s;", importedName);
        }
        p.println();
        p.println("@%s(value = { \"%s\", \"%s\" }, date = \"%tF %<tT\")",
                reservedNames.generated, ProductInfo.getName(), ProductInfo
                        .getVersion(), new Date());
        p.println("public final class %s extends %s<%s> {",
                reservedNames.modelMeta, reservedNames.s3modelMeta,
                reservedNames.model);
        p.println();
        p.println("    public %s() {", reservedNames.modelMeta);
        p.println("        super(%s.class);", reservedNames.model);
        p.println("    }");
        p.println();
        for (AttributeDesc desc : attributeDescList) {
            if (desc.isModelType()) {
                p.println("    public %1$s%2$s %3$s = new %1$s%2$s();", desc
                        .getTypeName(), Constants.META_SUFFIX, desc.getName());
            } else {
                p
                        .println(
                                "    public %1$s<%2$s> %3$s = new %1$s<%2$s>(\"%3$s\");",
                                reservedNames.s3attributeMeta, desc
                                        .getTypeName(), desc.getName());
            }
            p.println();
        }
        p.print("}");
    }

    /**
     * Represents reserved class names.
     * 
     * @author taedium
     * @since 3.0
     * 
     */
    protected class ReservedNames {

        /** name of model class */
        protected final String model;

        /** name of model meta class */
        protected final String modelMeta;

        /** name of {@code javax.annotation.Generated} class */
        protected final String generated;

        /** name of {@code org.slim3.jdo.ModelMeta} class */
        protected final String s3modelMeta;

        /** name of {@code org.slim3.jdo.AttributeMeta} class */
        protected final String s3attributeMeta;

        /**
         * Creates a new {@link ReservedNames}.
         * 
         * @param qualifiedModelName
         *            the qualified class name of model.
         * @param qualifiedModelMetaName
         *            the the qualified class name of model meta.
         */
        public ReservedNames(String qualifiedModelName,
                String qualifiedModelMetaName) {
            model = importedNames.add(qualifiedModelName);
            modelMeta = importedNames.add(qualifiedModelMetaName);
            generated = importedNames.add(Generated.class.getName());
            s3modelMeta = importedNames.add(ClassConstants.ModelMeta);
            s3attributeMeta = importedNames.add(ClassConstants.AttributeMeta);
        }
    }
}
