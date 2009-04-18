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

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.NestingKind;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.ElementScanner6;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

import org.slim3.gen.ProductInfo;
import org.slim3.gen.annotation.Annotations;
import org.slim3.gen.printer.Printer;
import org.slim3.gen.util.ElementUtil;

/**
 * Generates source codes of a JDO model meta class.
 * 
 * @author taedium
 * @since 3.0
 * 
 */
public class JDOModelMetaGenerator extends ElementScanner6<Void, Printer>
        implements Generator<Void, TypeElement, Printer> {

    /** the processing environment */
    protected final ProcessingEnvironment processingEnv;

    /** the utility object of elements */
    protected final Elements elements;

    /** the utility object of types */
    protected final Types types;

    /** simple class name */
    protected final String simpleName;

    /**
     * Creates a new {@link JDOModelMetaGenerator}.
     * 
     * @param processingEnv
     *            the processing environment
     * @param simpleName
     *            simple class name
     */
    public JDOModelMetaGenerator(ProcessingEnvironment processingEnv,
            String simpleName) {
        this.processingEnv = processingEnv;
        this.elements = processingEnv.getElementUtils();
        this.types = processingEnv.getTypeUtils();
        this.simpleName = simpleName;
    }

    @Override
    public Void generate(TypeElement e, Printer p) {
        return scan(e, p);
    }

    @Override
    public Void visitType(TypeElement e, Printer p) {
        if (e.getNestingKind() != NestingKind.TOP_LEVEL) {
            return DEFAULT_VALUE;
        }
        PackageElement packageElement = elements.getPackageOf(e);
        if (!packageElement.isUnnamed()) {
            p.println("package %s;", packageElement.getQualifiedName());
            p.println();
        }
        for (String importName : getImportNames(e)) {
            p.println("import %s;", importName);
        }
        p.println();
        p.println(
                "@Generated(value = { \"%s\", \"%s\" }, date = \"%tF %<tT\")",
                ProductInfo.getName(), ProductInfo.getVersion(), new Date());
        p.println("public final class %s extends ModelMeta<%s> {", simpleName,
                e.getSimpleName());
        p.println();
        p.println("    public %s() {", simpleName);
        p.println("        super(%s.class);", e.getSimpleName());
        p.println("    }");
        p.println();
        scan(e.getEnclosedElements(), p);
        p.print("}");
        return DEFAULT_VALUE;
    }

    /**
     * Returns a set of import names.
     * 
     * @param e
     *            the type element object.
     * @return a set of import names.
     */
    protected Set<String> getImportNames(TypeElement e) {
        Set<TypeElement> typeElements = new HashSet<TypeElement>();
        new ElementScanner6<Void, Set<TypeElement>>() {
            @Override
            public Void visitVariable(VariableElement e, Set<TypeElement> p) {
                if (ElementUtil.isAnnotated(e, Annotations.Persistent)) {
                    ElementUtil.collectTypeElements(e, p);
                }
                return DEFAULT_VALUE;
            }
        }.scan(e, typeElements);
        Set<String> result = new TreeSet<String>();
        for (Iterator<TypeElement> i = typeElements.iterator(); i.hasNext();) {
            String name = i.next().getQualifiedName().toString();
            if (!name.startsWith("java.lang.")) {
                result.add(name);
            }
        }
        result.add("javax.annotation.Generated");
        result.add("org.slim3.gae.jdo.AttributeMeta");
        result.add("org.slim3.gae.jdo.ModelMeta");
        return result;
    }

    @Override
    public Void visitVariable(VariableElement e, Printer p) {
        if (ElementUtil.isAnnotated(e, Annotations.Persistent)) {
            p
                    .println(
                            "    public AttributeMeta<%1$s> %2$s = new AttributeMeta<%1$s>(\"%2$s\");",
                            ElementUtil.toString(e, types), e.getSimpleName());
            p.println();
        }
        return DEFAULT_VALUE;
    }
}
