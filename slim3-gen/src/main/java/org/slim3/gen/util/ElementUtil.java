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
package org.slim3.gen.util;

import java.util.Collection;
import java.util.List;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
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
import javax.lang.model.util.Types;

/**
 * A utility class for operationg elements.
 * 
 * @author taedium
 * @since 3.0
 * 
 */
public final class ElementUtil {

    /**
     * Returns {@code true} if an element is annotated with a specified
     * annotation and {@code false} otherwise.
     * 
     * @param element
     *            the element object to be checked.
     * @param annotation
     *            the fully qualified name of an annotation.
     * @return
     */
    public static boolean isAnnotated(Element element, final String annotation) {
        for (AnnotationMirror mirror : element.getAnnotationMirrors()) {
            Element e = mirror.getAnnotationType().asElement();
            boolean annotated = e.accept(
                    new ElementKindVisitor6<Boolean, Void>() {
                        @Override
                        public Boolean visitTypeAsAnnotationType(TypeElement e,
                                Void p) {
                            return e.getQualifiedName().contentEquals(
                                    annotation);
                        }
                    }, null);
            if (annotated) {
                return true;
            }
        }
        return false;
    }

    /**
     * Converts a variable element to a string representation.
     * 
     * @param e
     *            the element object.
     * @param types
     *            the utility object.
     * @return a string representation of the element.
     * @see ToString
     */
    public static String toString(VariableElement e, Types types) {
        StringBuilder buf = new StringBuilder();
        e.asType().accept(new ToString(types), buf);
        return buf.toString();
    }

    /**
     * A visitor class to convert an variable element to a string
     * representation.
     * 
     * @author taedium
     * @since 3.0
     * 
     */
    protected static class ToString extends
            SimpleTypeVisitor6<Void, StringBuilder> {

        protected final Types types;

        public ToString(Types types) {
            this.types = types;
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

        protected void appendName(DeclaredType t, StringBuilder p) {
            t.asElement().accept(
                    new SimpleElementVisitor6<Void, StringBuilder>() {
                        @Override
                        public Void visitType(TypeElement e, StringBuilder p) {
                            p.append(e.getSimpleName());
                            return null;
                        }
                    }, p);
        }
    }

    /**
     * Collects type elements from an element.
     * 
     * @param e
     *            the element object.
     * @param result
     *            a collection of type elements.
     * @see CollectTypeElements
     */
    public static void collectTypeElements(VariableElement e,
            Collection<TypeElement> result) {
        e.asType().accept(new CollectTypeElements(), result);
    }

    /**
     * A visitor class to collect type elements from an element.
     * 
     * @author taedium
     * @since 3.0
     * 
     */
    protected static class CollectTypeElements extends
            SimpleTypeVisitor6<Void, Collection<TypeElement>> {
        @Override
        public Void visitArray(ArrayType t, Collection<TypeElement> p) {
            t.getComponentType().accept(this, p);
            return null;
        }

        @Override
        public Void visitWildcard(WildcardType t, Collection<TypeElement> p) {
            TypeMirror extendedBound = t.getExtendsBound();
            if (extendedBound != null) {
                extendedBound.accept(this, p);
            }
            TypeMirror superBound = t.getSuperBound();
            if (superBound != null) {
                superBound.accept(this, p);
            }
            return null;
        }

        @Override
        public Void visitDeclared(DeclaredType t, Collection<TypeElement> p) {
            t.asElement().accept(
                    new SimpleElementVisitor6<Void, Collection<TypeElement>>() {
                        @Override
                        public Void visitType(TypeElement e,
                                Collection<TypeElement> p) {
                            p.add(e);
                            return null;
                        }
                    }, p);

            List<? extends TypeMirror> typeArgs = t.getTypeArguments();
            if (typeArgs.size() > 0) {
                for (TypeMirror arg : t.getTypeArguments()) {
                    arg.accept(this, p);
                }
            }
            return null;
        }
    }
}
