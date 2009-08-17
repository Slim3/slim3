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
package org.slim3.gen.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.NoType;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.type.WildcardType;
import javax.lang.model.util.SimpleElementVisitor6;
import javax.lang.model.util.SimpleTypeVisitor6;
import javax.lang.model.util.TypeKindVisitor6;

/**
 * A utility class for {@link TypeMirror}.
 * 
 * @author taedium
 * @since 3.0
 * 
 */
public final class TypeUtil {

    /**
     * Converts a {@link TypeMirror} to a {@link TypeElement}.
     * 
     * @param typeMirror
     *            the target
     * @param env
     *            the processing environment
     * @return a converted {@link TypeElement}
     */
    public static TypeElement toTypeElement(TypeMirror typeMirror,
            ProcessingEnvironment env) {
        if (typeMirror == null) {
            throw new NullPointerException("The typeMirror parameter is null.");
        }
        if (env == null) {
            throw new NullPointerException("The env parameter is null.");
        }
        Element e = env.getTypeUtils().asElement(typeMirror);
        if (e == null) {
            return null;
        }
        return e.accept(new SimpleElementVisitor6<TypeElement, Void>() {

            @Override
            public TypeElement visitType(TypeElement e, Void p) {
                return e;
            }
        }, null);
    }

    /**
     * Converts a {@link TypeMirror} to a {@link DeclaredType}.
     * 
     * @param typeMirror
     *            the target
     * @param env
     *            the processing environment
     * @return a converted {@link DeclaredType}
     */
    public static DeclaredType toDeclaredType(TypeMirror typeMirror,
            ProcessingEnvironment env) {
        if (typeMirror == null) {
            throw new NullPointerException("The typeMirror parameter is null.");
        }
        if (env == null) {
            throw new NullPointerException("The env parameter is null.");
        }
        return typeMirror.accept(new SimpleTypeVisitor6<DeclaredType, Void>() {

            @Override
            public DeclaredType visitDeclared(DeclaredType t, Void p) {
                return t;
            }
        }, null);
    }

    /**
     * Returns the qualified type name which may contain type parameters.
     * 
     * @param typeMirror
     *            the target
     * @param typeParameterMap
     *            the type parameter map which maps a formal parameter and an
     *            actual parameter.
     * @param env
     *            the processing environment
     * @return a qualified type name
     */
    public static String getTypeName(TypeMirror typeMirror,
            final Map<TypeMirror, TypeMirror> typeParameterMap,
            final ProcessingEnvironment env) {
        if (typeMirror == null) {
            throw new NullPointerException("The typeMirror parameter is null.");
        }
        if (typeParameterMap == null) {
            throw new NullPointerException(
                "The typeParameterMap parameter is null.");
        }
        if (env == null) {
            throw new NullPointerException("The env parameter is null.");
        }
        StringBuilder p = new StringBuilder();
        typeMirror.accept(new TypeKindVisitor6<Void, StringBuilder>() {

            @Override
            public Void visitNoTypeAsVoid(NoType t, StringBuilder p) {
                p.append("void");
                return null;
            }

            @Override
            public Void visitPrimitive(PrimitiveType t, StringBuilder p) {
                if (p.length() == 0) {
                    p.append(t);
                } else {
                    TypeElement e = env.getTypeUtils().boxedClass(t);
                    p.append(e.getSimpleName());
                }
                return null;
            }

            @Override
            public Void visitArray(ArrayType t, StringBuilder p) {
                t.getComponentType().accept(this, p);
                p.append("[]");
                return null;
            }

            @Override
            public Void visitDeclared(DeclaredType t, StringBuilder p) {
                TypeElement e = toTypeElement(t, env);
                if (e != null) {
                    p.append(e.getQualifiedName());
                }
                if (!t.getTypeArguments().isEmpty()) {
                    p.append("<");
                    for (TypeMirror arg : t.getTypeArguments()) {
                        arg.accept(this, p);
                        p.append(", ");
                    }
                    p.setLength(p.length() - 2);
                    p.append(">");
                }
                return null;
            }

            @Override
            public Void visitTypeVariable(TypeVariable t, StringBuilder p) {
                p.append(resolveTypeVariable(t));
                TypeMirror upperBound = t.getUpperBound();
                String upperBoundName =
                    TypeUtil.getTypeName(upperBound, typeParameterMap, env);
                if (!Object.class.getName().equals(upperBoundName)) {
                    p.append(" extends ");
                    upperBound.accept(this, p);
                } else {
                    TypeMirror lowerBound = t.getLowerBound();
                    if (lowerBound.getKind() != TypeKind.NULL) {
                        p.append(" super ");
                        lowerBound.accept(this, p);
                    }
                }
                return null;
            }

            protected TypeMirror resolveTypeVariable(TypeVariable t) {
                if (typeParameterMap.containsKey(t)) {
                    return typeParameterMap.get(t);
                }
                return t;
            }

            @Override
            public Void visitWildcard(WildcardType t, StringBuilder p) {
                TypeMirror extendsBound = t.getExtendsBound();
                if (extendsBound != null) {
                    p.append("? extends ");
                    extendsBound.accept(this, p);
                }
                TypeMirror superBound = t.getSuperBound();
                if (superBound != null) {
                    p.append("? super ");
                    superBound.accept(this, p);
                }
                return null;
            }

            @Override
            protected Void defaultAction(TypeMirror e, StringBuilder p) {
                p.append(e);
                throw new IllegalArgumentException(p.toString());
            }

        }, p);

        return p.toString();
    }

    /**
     * Creates a type parameter map which maps a formal parameter and an actual
     * parameter.
     * 
     * @param typeElement
     *            the element which may contain formal parameters
     * @param typeMirror
     *            the type which may contain actual parameters
     * @param env
     *            the processing environment
     * @return a type parameter map
     */
    public static Map<TypeMirror, TypeMirror> createTypeParameterMap(
            TypeElement typeElement, TypeMirror typeMirror,
            ProcessingEnvironment env) {
        if (typeElement == null) {
            throw new NullPointerException("The typeElement parameter is null.");
        }
        if (typeMirror == null) {
            throw new NullPointerException("The typeMirror parameter is null.");
        }
        if (env == null) {
            throw new NullPointerException("The env parameter is null.");
        }
        Map<TypeMirror, TypeMirror> typeParameterMap =
            new HashMap<TypeMirror, TypeMirror>();
        Iterator<? extends TypeParameterElement> formalParams =
            typeElement.getTypeParameters().iterator();
        DeclaredType declaredType = TypeUtil.toDeclaredType(typeMirror, env);
        Iterator<? extends TypeMirror> actualParams =
            declaredType.getTypeArguments().iterator();
        for (; formalParams.hasNext() && actualParams.hasNext();) {
            TypeMirror key = formalParams.next().asType();
            TypeMirror value = actualParams.next();
            typeParameterMap.put(key, value);
        }
        return typeParameterMap;
    }

    /**
     * Converts a primitive type to a coresponding wrapper type.
     * 
     * @param typeMirror
     *            the target
     * @param env
     *            the processing environment
     * @return a wrapper type
     */
    public static TypeMirror toWrapperTypeIfPrimitive(TypeMirror typeMirror,
            final ProcessingEnvironment env) {
        return typeMirror.accept(new TypeKindVisitor6<TypeMirror, Void>() {

            @Override
            public TypeMirror visitPrimitive(PrimitiveType t, Void p) {
                return env.getTypeUtils().boxedClass(t).asType();
            }

            @Override
            public TypeMirror visitNoTypeAsVoid(NoType t, Void p) {
                return env.getElementUtils().getTypeElement(
                    Void.class.getName()).asType();
            }

            @Override
            protected TypeMirror defaultAction(TypeMirror e, Void p) {
                return e;
            }

        }, null);
    }
}
