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
package org.slim3.gen.util;

import static javax.lang.model.util.ElementFilter.*;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.NoType;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.SimpleTypeVisitor6;

/**
 * A utility class for operationg typemirrors.
 * 
 * @author taedium
 * @since 1.0.0
 * 
 */
public final class TypeUtil {

    private static ProcessingEnvironment processingEnv;

    /**
     * @param processingEnv
     */
    public static void init(ProcessingEnvironment processingEnv) {
        TypeUtil.processingEnv = processingEnv;
    }

    /**
     * Returns {@code true} if a typeMirror represents void.
     * 
     * @param typeMirror
     *            the typemirror
     * @return {@code true} if a typeMirror represents void, otherwise
     *         {@code false}.
     */
    public static boolean isVoid(TypeMirror typeMirror) {
        class Visitor extends SimpleTypeVisitor6<Boolean, Void> {
            @Override
            public Boolean visitNoType(NoType t, Void p) {
                return t.getKind() == TypeKind.VOID;
            }
        }
        Visitor visitor = new Visitor();
        Boolean result = typeMirror.accept(visitor, null);
        return result != null ? result : false;
    }

    /**
     * Returns {@code true} if a typeMirror represents primitive.
     * 
     * @param typeMirror
     *            the typemirror
     * @param kind
     *            the kind of primitive
     * @return {@code true} if a typeMirror represents primitive, otherwise
     *         {@code false}.
     */
    public static boolean isPrimitive(TypeMirror typeMirror, final TypeKind kind) {
        class Visitor extends SimpleTypeVisitor6<Boolean, TypeKind> {
            @Override
            public Boolean visitPrimitive(PrimitiveType t, TypeKind kind) {
                return t.getKind() == kind;
            }
        }
        Visitor visitor = new Visitor();
        Boolean result = typeMirror.accept(visitor, kind);
        return result != null ? result : false;
    }

    /**
     * Returns {@code true} if a typeMirror represents {@link DeclaredType}.
     * 
     * @param typeMirror
     *            the typemirror
     * @return {@code true} if a typeMirror represents {@link DeclaredType},
     *         otherwise {@code null}.
     */
    public static DeclaredType toDeclaredType(TypeMirror typeMirror) {
        class Visitor extends SimpleTypeVisitor6<DeclaredType, Void> {
            @Override
            public DeclaredType visitDeclared(DeclaredType t, Void p) {
                return t;
            }
        }
        Visitor visitor = new Visitor();
        return typeMirror.accept(visitor, null);
    }

    /**
     * Returns {@code true} if a typeMirror represents {@link DeclaredType}.
     * 
     * @param typeMirror
     *            the typemirror
     * @return {@code true} if a typeMirror represents {@link DeclaredType},
     *         otherwise {@code null}.
     */
    public static DeclaredType toClassType(TypeMirror typeMirror) {
        class Visitor extends SimpleTypeVisitor6<DeclaredType, Void> {
            @Override
            public DeclaredType visitDeclared(DeclaredType t, Void p) {
                if (t.asElement().getKind() == ElementKind.CLASS) {
                    return t;
                }
                return null;
            }
        }
        Visitor visitor = new Visitor();
        return typeMirror.accept(visitor, null);
    }

    /**
     * Returns {@code true} if a {@code subtype} is subtype of {@code supertype}
     * .
     * 
     * @param subtype
     *            the typemirror of subtype
     * @param supertype
     *            the supertype
     * @return {@code true} if a {@code subtype} is subtype of
     *         {@code superclass}, otherwise {@code false}.
     */
    public static boolean isSubtype(TypeMirror subtype, Class<?> supertype) {

        TypeElement supertypeDeclaration =
            processingEnv.getElementUtils().getTypeElement(supertype.getName());
        if (supertypeDeclaration == null) {
            return false;
        }
        TypeMirror t1 = processingEnv.getTypeUtils().erasure(subtype);
        DeclaredType t2 =
            processingEnv.getTypeUtils().getDeclaredType(supertypeDeclaration);
        return processingEnv.getTypeUtils().isSubtype(t1, t2);
    }

    /**
     * Returns {@code true} if a {@code subtype} is subtype of {@code supertype}
     * .
     * 
     * @param subtype
     *            the typemirror of subtype
     * @param supertype
     *            the supertype
     * @return {@code true} if a {@code subtype} is subtype of
     *         {@code superclass}, otherwise {@code false}.
     */
    public static boolean isSubtype(TypeMirror subtype, String supertype) {
        return getSuperDeclaredType(subtype, supertype) != null;
    }

    /**
     * Returns super {@link DeclaredType} of {@code subtype}.
     * 
     * @param subtype
     *            the typemirror of subtype
     * @param supertype
     *            the supertype
     * @return super {@link DeclaredType} of {@code subtype}
     */
    public static DeclaredType getSuperDeclaredType(TypeMirror subtype,
            String supertype) {

        TypeElement supertypeDeclaration =
            processingEnv.getElementUtils().getTypeElement(supertype);

        class Visitor extends SimpleTypeVisitor6<Void, Void> {

            Set<DeclaredType> supertypes = new HashSet<DeclaredType>();

            @Override
            public Void visitDeclared(DeclaredType declaredType, Void p) {
                supertypes.add(declaredType);
                for (TypeElement superElement : typesIn(declaredType
                    .asElement()
                    .getEnclosedElements())) {
                    if (superElement.getKind() == ElementKind.INTERFACE) {
                        superElement.asType().accept(this, null);
                    } else if (superElement.getKind() == ElementKind.CLASS) {
                        superElement.asType().accept(this, null);
                    }
                }
                return null;
            }
        }
        Visitor visitor = new Visitor();
        subtype.accept(visitor, null);
        for (DeclaredType type : visitor.supertypes) {
            if (processingEnv.getTypeUtils().isSameType(
                processingEnv.getTypeUtils().erasure(type),
                processingEnv.getTypeUtils().erasure(
                    supertypeDeclaration.asType()))) {
                return type;
            }
        }
        return null;
    }
}
