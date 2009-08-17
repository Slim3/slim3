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
package org.slim3.gen.desc;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ErrorType;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.WildcardType;
import javax.lang.model.util.ElementKindVisitor6;
import javax.lang.model.util.SimpleElementVisitor6;
import javax.lang.model.util.SimpleTypeVisitor6;
import javax.lang.model.util.TypeKindVisitor6;

import org.slim3.gen.ClassConstants;
import org.slim3.gen.message.MessageCode;
import org.slim3.gen.message.MessageFormatter;
import org.slim3.gen.processor.Logger;
import org.slim3.gen.util.ElementUtil;

/**
 * Represents an attribute meta description factory.
 * 
 * @author taedium
 * @since 3.0
 * 
 */
public class AttributeMetaDescFactory {

    /** the processing environment */
    protected final ProcessingEnvironment processingEnv;

    /**
     * Creates a new {@link AttributeMetaDescFactory}.
     * 
     * @param processingEnv
     *            the processing environment
     */
    public AttributeMetaDescFactory(ProcessingEnvironment processingEnv) {
        if (processingEnv == null) {
            throw new NullPointerException(
                "The processingEnv parameter is null.");
        }
        this.processingEnv = processingEnv;
    }

    /**
     * Creates a new {@link AttributeMetaDesc}
     * 
     * @param fieldElement
     *            the field element
     * @return an attribute meta description
     */
    public AttributeMetaDesc createAttributeMetaDesc(
            VariableElement fieldElement) {
        if (fieldElement == null) {
            throw new NullPointerException(
                "The fieldElement parameter is null.");
        }
        if (!isPersistent(fieldElement)) {
            if (isInstanceVariable(fieldElement)
                && !isNotPersistent(fieldElement)) {
                Logger.warning(processingEnv, fieldElement, MessageFormatter
                    .getSimpleMessage(MessageCode.SILM3GEN0010));
            }
            return null;
        }
        if (isNestedType(fieldElement)) {
            return null;
        }

        AttributeMetaDesc attributeMetaDesc = new AttributeMetaDesc();
        attributeMetaDesc.setName(fieldElement.getSimpleName().toString());
        attributeMetaDesc.setEmbedded(isEmbedded(fieldElement));
        Iterator<String> classNames =
            new ClassNameCollector(fieldElement.asType()).collect().iterator();
        if (classNames.hasNext()) {
            String className = classNames.next();
            attributeMetaDesc.setAttributeClassName(className);
        }
        if (classNames.hasNext()) {
            String elementClassName = classNames.next();
            attributeMetaDesc.setAttributeElementClassName(elementClassName);
        }
        return attributeMetaDesc;
    }

    /**
     * Returns {@code true} if the attribute type is a nested type.
     * 
     * @param attributeElement
     *            the element of an attribute
     * @return {@code true} if the attribute type is a nested type
     */
    protected boolean isNestedType(Element attributeElement) {
        TypeMirror typeMirror =
            attributeElement.asType().accept(
                new TypeKindVisitor6<TypeMirror, Void>() {

                    @Override
                    public TypeMirror visitArray(ArrayType t, Void p) {
                        return t.getComponentType().accept(this, p);
                    }

                    @Override
                    protected TypeMirror defaultAction(TypeMirror e, Void p) {
                        return e;
                    }

                },
                null);
        Element e = processingEnv.getTypeUtils().asElement(typeMirror);
        if (e == null) {
            return false;
        }
        return e.accept(new ElementKindVisitor6<Boolean, Void>(false) {

            @Override
            public Boolean visitType(TypeElement e, Void p) {
                return e.getNestingKind().isNested();
            }

        }, null);
    }

    /**
     * Returns {@code true} if the attribute is persistent.
     * 
     * @param attributeElement
     *            the element of an attribute
     * @return {@code true} if the attribute is persistent.
     */
    protected boolean isPersistent(Element attributeElement) {
        return ElementUtil.getAnnotationMirror(
            attributeElement,
            ClassConstants.Persistent) != null;
    }

    /**
     * Returns {@code true} if the attribute is not persistent.
     * 
     * @param attributeElement
     *            the element of an attribute
     * @return {@code true} if the attribute is persistent.
     */
    protected boolean isNotPersistent(Element attributeElement) {
        return ElementUtil.getAnnotationMirror(
            attributeElement,
            ClassConstants.NotPersistent) != null;
    }

    /**
     * Returns {@code true} if the attribute is an instance variable.
     * 
     * @param attributeElement
     *            the element of an attribute
     * @return {@code true} if the attribute is an instance variable.
     */
    protected boolean isInstanceVariable(VariableElement attributeElement) {
        return !attributeElement.getModifiers().contains(Modifier.STATIC);
    }

    /**
     * Returns {@code true} if the attribute is embedded.
     * 
     * @param attributeElement
     *            the element of an attribute
     * @return {@code true} if the attribute is embedded.
     */
    protected boolean isEmbedded(Element attributeElement) {
        return ElementUtil.getAnnotationMirror(
            attributeElement,
            ClassConstants.Embedded) != null;
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

        /**
         * Collects the collection of class name.
         * 
         * @return the collection of class name
         */
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
                    public Void visitType(TypeElement e, LinkedList<String> p) {
                        p.add(e.getQualifiedName().toString());
                        return null;
                    }
                },
                p);
            for (TypeMirror arg : t.getTypeArguments()) {
                LinkedList<String> names = new LinkedList<String>();
                arg.accept(this, names);
                p.add(names.getFirst());
            }
            return null;
        }
    }
}
