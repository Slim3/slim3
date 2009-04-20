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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementVisitor;
import javax.lang.model.element.NestingKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ErrorType;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.WildcardType;
import javax.lang.model.util.ElementScanner6;
import javax.lang.model.util.Elements;
import javax.lang.model.util.SimpleElementVisitor6;
import javax.lang.model.util.SimpleTypeVisitor6;
import javax.lang.model.util.Types;

import org.slim3.gen.Annotations;
import org.slim3.gen.ProductInfo;
import org.slim3.gen.printer.Printer;
import org.slim3.gen.util.ElementUtil;
import org.slim3.gen.util.Logger;

/**
 * Generates source codes of a JDO model meta class.
 * 
 * @author taedium
 * @since 3.0
 * 
 */
public class JDOModelMetaGenerator implements Generator<Printer> {

    /** the document URL */
    protected static final String docURL = "http://code.google.com/intl/en/appengine/docs/java/datastore/dataclasses.html";

    /** the list of unsupported package names */
    protected static final List<String> unsupportedPackageNameList = new ArrayList<String>();
    static {
        unsupportedPackageNameList.add("java.math");
        unsupportedPackageNameList.add("java.sql");
    }

    /** the list of unsupported class names */
    protected static final List<String> unsupportedClassNameList = new ArrayList<String>();
    static {
        unsupportedClassNameList.add("java.util.Calendar");
        unsupportedClassNameList.add("java.lang.Character");
    }

    /** the processing environment */
    protected final ProcessingEnvironment processingEnv;

    /** the utility object of elements */
    protected final Elements elements;

    /** the utility object of types */
    protected final Types types;

    /** the package name */
    protected final String packageName;

    /** the collection of imported names */
    protected final ImportedNames importedNames;

    /** the collection of reserved names */
    protected final ReservedNames reservedNames;

    /** the attributeMeta map */
    protected final Map<String, String> attributeMetaMap = new LinkedHashMap<String, String>();

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
        this.elements = processingEnv.getElementUtils();
        this.types = processingEnv.getTypeUtils();
        int pos = qualifiedName.lastIndexOf('.');
        if (pos < 0) {
            this.packageName = "";
        } else {
            this.packageName = qualifiedName.substring(0, pos);
        }
        this.importedNames = new ImportedNames(packageName);
        this.reservedNames = new ReservedNames(element.getQualifiedName()
                .toString(), qualifiedName);

        new ClassElementScanner().scan(element);
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
        for (Map.Entry<String, String> entry : attributeMetaMap.entrySet()) {
            p.println("    public %1$s<%2$s> %3$s = new %1$s<%2$s>(\"%3$s\");",
                    reservedNames.s3attributeMeta, entry.getValue(), entry
                            .getKey());
            p.println();
        }
        p.print("}");
    }

    /**
     * Adds a package name to the list of unsupported package names.
     * 
     * @param packageName
     *            the package name.
     */
    public static void addUnsupportedPackageName(String packageName) {
        unsupportedPackageNameList.add(packageName);
    }

    /**
     * Removes a package name from the list of unsupported package names.
     * 
     * @param packageName
     *            the package name.
     */
    public static void removeUnsupportedPackageName(String packageName) {
        unsupportedPackageNameList.remove(packageName);
    }

    /**
     * Adds a class name to the list of unsupported class names.
     * 
     * @param className
     *            the class name.
     */
    public static void addUnsupportedClassName(String className) {
        unsupportedClassNameList.add(className);
    }

    /**
     * Removes a class name from the list of unsupported class names.
     * 
     * @param className
     *            the class name.
     */
    public static void removeUnsupportedClassName(String className) {
        unsupportedClassNameList.remove(className);
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

        /** name of {@code org.slim3.gae.jdo.ModelMeta} class */
        protected final String s3modelMeta;

        /** name of {@code org.slim3.gae.jdo.AttributeMeta} class */
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
            generated = importedNames.add("javax.annotation.Generated");
            s3modelMeta = importedNames.add("org.slim3.gae.jdo.ModelMeta");
            s3attributeMeta = importedNames
                    .add("org.slim3.gae.jdo.AttributeMeta");
        }
    }

    /**
     * Scans a JDM model class element.
     * 
     * @author taedium
     * @since 3.0
     * 
     */
    protected class ClassElementScanner extends ElementScanner6<Void, Void> {

        @Override
        public Void visitType(TypeElement e, Void p) {
            if (e.getNestingKind() != NestingKind.TOP_LEVEL) {
                return null;
            }
            return super.visitType(e, p);
        }

        @Override
        public Void visitVariable(VariableElement e, Void p) {
            if (ElementUtil.isAnnotated(e, Annotations.Persistent)) {
                String fieldName = e.getSimpleName().toString();
                StringBuilder buf = new StringBuilder();
                e.asType().accept(new TypeParameterBuilder(e), buf);
                String typeParameter = buf.toString();
                attributeMetaMap.put(fieldName, typeParameter);
            }
            return null;
        }
    }

    /**
     * Builds a string of a actual type parameter.
     * 
     * @author taedium
     * @since 3.0
     * 
     */
    protected class TypeParameterBuilder extends
            SimpleTypeVisitor6<Void, StringBuilder> {

        /** the element which a message notified */
        protected Element notifiedElement;

        public TypeParameterBuilder(Element notifiedElement) {
            this.notifiedElement = notifiedElement;
        }

        @Override
        public Void visitArray(ArrayType t, StringBuilder p) {
            t.getComponentType().accept(this, p);
            p.append("[]");
            return null;
        }

        @Override
        public Void visitPrimitive(PrimitiveType t, StringBuilder p) {
            types.boxedClass(t).asType().accept(this, p);
            return null;
        }

        @Override
        public Void visitWildcard(WildcardType t, StringBuilder p) {
            p.append("?");
            TypeMirror extendedBound = t.getExtendsBound();
            if (extendedBound != null) {
                p.append(" extends ");
                extendedBound.accept(this, p);
            }
            TypeMirror superBound = t.getSuperBound();
            if (superBound != null) {
                p.append(" super ");
                superBound.accept(this, p);
            }
            return null;
        }

        @Override
        public Void visitError(ErrorType t, StringBuilder p) {
            p.append("Object");
            return null;
        }

        @Override
        public Void visitDeclared(DeclaredType t, StringBuilder p) {
            appendName(t, p);
            List<? extends TypeMirror> typeArgs = t.getTypeArguments();
            if (typeArgs.size() > 0) {
                p.append("<");
                for (TypeMirror arg : typeArgs) {
                    arg.accept(this, p);
                    p.append(", ");
                }
                p.setLength(p.length() - 2);
                p.append(">");
            }
            return null;
        }

        /**
         * Appends a type name to {@code p}.
         * 
         * @param t
         *            the type.
         * @param p
         *            the object to which the type name is appended.
         */
        protected void appendName(DeclaredType t, StringBuilder p) {
            ElementVisitor<Void, StringBuilder> visitor = new SimpleElementVisitor6<Void, StringBuilder>() {
                @Override
                public Void visitType(TypeElement e, StringBuilder p) {
                    checkName(e);
                    String qualifiedName = e.getQualifiedName().toString();
                    p.append(importedNames.add(qualifiedName));
                    return null;
                }

                protected void checkName(TypeElement e) {
                    String packageName = elements.getPackageOf(e)
                            .getQualifiedName().toString();
                    String qualifiedName = e.getQualifiedName().toString();
                    if (unsupportedPackageNameList.contains(packageName)) {
                        Logger
                                .error(
                                        processingEnv,
                                        notifiedElement,
                                        "[%s] Package(%s) is not supported on Google App Engine. See %s",
                                        JDOModelMetaGenerator.class.getName(),
                                        packageName, docURL);
                    }
                    if (unsupportedClassNameList.contains(qualifiedName)) {
                        Logger
                                .error(
                                        processingEnv,
                                        notifiedElement,
                                        "[%s] Class(%s) is not supported on Google App Engine. See %s",
                                        JDOModelMetaGenerator.class.getName(),
                                        qualifiedName, docURL);
                    }
                }
            };
            t.asElement().accept(visitor, p);
        }
    }
}
