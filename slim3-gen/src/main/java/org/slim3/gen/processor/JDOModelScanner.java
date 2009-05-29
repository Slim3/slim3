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
package org.slim3.gen.processor;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.NestingKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ErrorType;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.WildcardType;
import javax.lang.model.util.ElementKindVisitor6;
import javax.lang.model.util.ElementScanner6;
import javax.lang.model.util.SimpleElementVisitor6;
import javax.lang.model.util.SimpleTypeVisitor6;
import javax.lang.model.util.TypeKindVisitor6;

import org.slim3.gen.ClassConstants;
import org.slim3.gen.desc.AttributeDesc;
import org.slim3.gen.desc.ModelDesc;
import org.slim3.gen.util.ElementUtil;

/**
 * Scans a JDO model.
 * 
 * @author taedium
 * @since 3.0
 * 
 */
public class JDOModelScanner extends ElementScanner6<Void, ModelDesc> {

    /** the processing environment */
    protected final ProcessingEnvironment processingEnv;

    /**
     * Creates a new {@link JDOModelScanner}.
     * 
     * @param processingEnv
     *            the processing environment
     */
    public JDOModelScanner(ProcessingEnvironment processingEnv) {
        this.processingEnv = processingEnv;
    }

    @Override
    public Void visitType(TypeElement e, ModelDesc p) {
        if (e.getNestingKind() == NestingKind.TOP_LEVEL) {
            p.setTopLevel(true);
        } else {
            return null;
        }
        return super.visitType(e, p);
    }

    @Override
    public Void visitVariable(VariableElement attribute, ModelDesc p) {
        AnnotationMirror persistent = ElementUtil.getAnnotationMirror(
                attribute, ClassConstants.Persistent);
        if (persistent == null) {
            return null;
        }
        if (!isTopLevelType(attribute)) {
            return null;
        }
        AttributeDesc attributeDesc = new AttributeDesc();
        attributeDesc.setName(attribute.getSimpleName().toString());
        attributeDesc.setModelType(isModelType(attribute));
        Iterator<String> classNames = new ClassNameCollector(attribute.asType())
                .collect().iterator();
        if (classNames.hasNext()) {
            String className = classNames.next();
            attributeDesc.setClassName(className);
        }
        if (classNames.hasNext()) {
            String elementClassName = classNames.next();
            attributeDesc.setElementClassName(elementClassName);
        }
        p.addAttributeDesc(attributeDesc);
        return null;
    }

    /**
     * Returns {@code true} if the attribute type is a top level type.
     * 
     * @param attribute
     *            the element of an attribute
     * @return {@code true} if the attribute type is a top level type
     */
    protected boolean isTopLevelType(Element attribute) {
        Element e = processingEnv.getTypeUtils().asElement(attribute.asType());
        return e.accept(new ElementKindVisitor6<Boolean, Void>(false) {

            @Override
            public Boolean visitType(TypeElement e, Void p) {
                return e.getNestingKind() == NestingKind.TOP_LEVEL;
            }

        }, null);
    }

    /**
     * Returns {@code true} if the attribute type is a model type.
     * 
     * @param attribute
     *            the element of an attribute
     * @return {@code true} if the attribute type is a model type.
     */
    protected boolean isModelType(Element attribute) {
        Element e = processingEnv.getTypeUtils().asElement(attribute.asType());
        if (e != null
                && ElementUtil.getAnnotationMirror(e,
                        ClassConstants.PersistenceCapable) != null) {
            return true;
        }
        return false;
    }

    /**
     * Collects the collection of class name.
     * 
     * @author taedium
     * @since 3.0
     * 
     */
    protected class ClassNameCollector extends
            SimpleTypeVisitor6<Void, LinkedList<String>> {

        /** the target typeMirror */
        protected final TypeMirror typeMirror;

        /**
         * Creates a new {@link ClassNameCollector}
         * 
         * @param typeMirror
         *            the target typeMirror
         */
        public ClassNameCollector(TypeMirror typeMirror) {
            this.typeMirror = typeMirror;
        }

        public List<String> collect() {
            LinkedList<String> names = new LinkedList<String>();
            typeMirror.accept(this, names);
            return names;
        }

        @Override
        public Void visitArray(ArrayType t, LinkedList<String> p) {
            LinkedList<String> names = new LinkedList<String>();
            t.getComponentType().accept(this, names);
            p.add(names.getFirst() + "[]");
            p.add(names.getFirst());
            return null;
        }

        @Override
        public Void visitPrimitive(PrimitiveType t, LinkedList<String> p) {
            t.accept(new TypeKindVisitor6<Void, LinkedList<String>>() {

                @Override
                public Void visitPrimitiveAsBoolean(PrimitiveType t,
                        LinkedList<String> p) {
                    p.add(boolean.class.getSimpleName());
                    return null;
                }

                @Override
                public Void visitPrimitiveAsByte(PrimitiveType t,
                        LinkedList<String> p) {
                    p.add(byte.class.getSimpleName());
                    return null;
                }

                @Override
                public Void visitPrimitiveAsChar(PrimitiveType t,
                        LinkedList<String> p) {
                    p.add(char.class.getSimpleName());
                    return null;
                }

                @Override
                public Void visitPrimitiveAsDouble(PrimitiveType t,
                        LinkedList<String> p) {
                    p.add(double.class.getSimpleName());
                    return null;
                }

                @Override
                public Void visitPrimitiveAsFloat(PrimitiveType t,
                        LinkedList<String> p) {
                    p.add(float.class.getSimpleName());
                    return null;
                }

                @Override
                public Void visitPrimitiveAsInt(PrimitiveType t,
                        LinkedList<String> p) {
                    p.add(int.class.getSimpleName());
                    return null;
                }

                @Override
                public Void visitPrimitiveAsLong(PrimitiveType t,
                        LinkedList<String> p) {
                    p.add(long.class.getSimpleName());
                    return null;
                }

                @Override
                public Void visitPrimitiveAsShort(PrimitiveType t,
                        LinkedList<String> p) {
                    p.add(short.class.getSimpleName());
                    return null;
                }
            }, p);
            return null;
        }

        @Override
        public Void visitWildcard(WildcardType t, LinkedList<String> p) {
            TypeMirror extendedBound = t.getExtendsBound();
            if (extendedBound != null) {
                LinkedList<String> names = new LinkedList<String>();
                extendedBound.accept(this, names);
                p.add(names.getFirst());
            }
            TypeMirror superBound = t.getSuperBound();
            if (superBound != null) {
                LinkedList<String> names = new LinkedList<String>();
                superBound.accept(this, names);
                p.add(names.getFirst());
            }
            return null;
        }

        @Override
        public Void visitError(ErrorType t, LinkedList<String> p) {
            p.add("Object");
            return null;
        }

        @Override
        public Void visitDeclared(DeclaredType t, LinkedList<String> p) {
            t.asElement().accept(
                    new SimpleElementVisitor6<Void, LinkedList<String>>() {
                        @Override
                        public Void visitType(TypeElement e,
                                LinkedList<String> p) {
                            p.add(e.getQualifiedName().toString());
                            return null;
                        }
                    }, p);
            for (TypeMirror arg : t.getTypeArguments()) {
                LinkedList<String> names = new LinkedList<String>();
                arg.accept(this, names);
                p.add(names.getFirst());
            }
            return null;
        }
    }
}
