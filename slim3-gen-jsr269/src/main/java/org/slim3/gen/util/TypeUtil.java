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

import java.util.HashSet;
import java.util.Set;

import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.declaration.TypeDeclaration;
import com.sun.mirror.type.ClassType;
import com.sun.mirror.type.DeclaredType;
import com.sun.mirror.type.InterfaceType;
import com.sun.mirror.type.PrimitiveType;
import com.sun.mirror.type.TypeMirror;
import com.sun.mirror.type.VoidType;
import com.sun.mirror.type.PrimitiveType.Kind;
import com.sun.mirror.util.SimpleTypeVisitor;

/**
 * A utility class for operationg typemirrors.
 * 
 * @author taedium
 * @since 1.0.0
 * 
 */
@SuppressWarnings("deprecation")
public final class TypeUtil {

    /**
     * Returns {@code true} if a typeMirror represents void.
     * 
     * @param typeMirror
     *            the typemirror
     * @return {@code true} if a typeMirror represents void, otherwise {@code
     *         false}.
     */
    public static boolean isVoid(TypeMirror typeMirror) {
        class Visitor extends SimpleTypeVisitor {
            boolean result;

            @Override
            public void visitVoidType(VoidType voidtype) {
                result = true;
            }
        }
        Visitor visitor = new Visitor();
        typeMirror.accept(visitor);
        return visitor.result;
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
    public static boolean isPrimitive(TypeMirror typeMirror, final Kind kind) {
        class Visitor extends SimpleTypeVisitor {
            boolean result;

            @Override
            public void visitPrimitiveType(PrimitiveType primitivetype) {
                result = primitivetype.getKind() == kind;
            }
        }
        Visitor visitor = new Visitor();
        typeMirror.accept(visitor);
        return visitor.result;
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
        class Visitor extends SimpleTypeVisitor {
            DeclaredType result;

            @Override
            public void visitDeclaredType(DeclaredType declaredtype) {
                result = declaredtype;
            }
        }
        Visitor visitor = new Visitor();
        typeMirror.accept(visitor);
        return visitor.result;
    }

    /**
     * Returns {@code true} if a typeMirror represents {@link ClassType}.
     * 
     * @param typeMirror
     *            the typemirror
     * @return {@code true} if a typeMirror represents {@link ClassType},
     *         otherwise {@code null}.
     */
    public static ClassType toClassType(TypeMirror typeMirror) {
        class Visitor extends SimpleTypeVisitor {
            ClassType result;

            @Override
            public void visitClassType(ClassType classtype) {
                result = classtype;
            }

        }
        Visitor visitor = new Visitor();
        typeMirror.accept(visitor);
        return visitor.result;
    }

    /**
     * Returns {@code true} if a {@code subtype} is subtype of {@code supertype}
     * .
     * 
     * @param env
     *            the environment
     * @param subtype
     *            the typemirror of subtype
     * @param supertype
     *            the supertype
     * @return {@code true} if a {@code subtype} is subtype of {@code
     *         superclass}, otherwise {@code false}.
     */
    public static boolean isSubtype(AnnotationProcessorEnvironment env,
            TypeMirror subtype, Class<?> supertype) {
        TypeDeclaration supertypeDeclaration =
            env.getTypeDeclaration(supertype.getName());
        if (supertypeDeclaration == null) {
            return false;
        }
        TypeMirror t1 = env.getTypeUtils().getErasure(subtype);
        TypeMirror t2 =
            env.getTypeUtils().getDeclaredType(supertypeDeclaration);
        return env.getTypeUtils().isSubtype(t1, t2);
    }

    /**
     * Returns {@code true} if a {@code subtype} is subtype of {@code supertype}
     * .
     * 
     * @param env
     *            the environment
     * @param subtype
     *            the typemirror of subtype
     * @param supertype
     *            the supertype
     * @return {@code true} if a {@code subtype} is subtype of {@code
     *         superclass}, otherwise {@code false}.
     */
    public static boolean isSubtype(AnnotationProcessorEnvironment env,
            TypeMirror subtype, String supertype) {
        return getSuperDeclaredType(env, subtype, supertype) != null;
    }

    /**
     * Returns super {@link DeclaredType} of {@code subtype}.
     * 
     * @param env
     *            the environment
     * @param subtype
     *            the typemirror of subtype
     * @param supertype
     *            the supertype
     * @return super {@link DeclaredType} of {@code subtype}
     */
    public static DeclaredType getSuperDeclaredType(
            AnnotationProcessorEnvironment env, TypeMirror subtype,
            String supertype) {
        TypeDeclaration supertypeDeclaration =
            env.getTypeDeclaration(supertype);
        if (supertypeDeclaration == null) {
            return null;
        }

        class Visitor extends SimpleTypeVisitor {

            Set<DeclaredType> supertypes = new HashSet<DeclaredType>();

            @Override
            public void visitDeclaredType(DeclaredType declaredType) {
                supertypes.add(declaredType);
                for (InterfaceType supertype : declaredType
                    .getSuperinterfaces()) {
                    supertype.accept(this);
                }
            }

            @Override
            public void visitClassType(ClassType classType) {
                supertypes.add(classType);
                ClassType supertype = classType.getSuperclass();
                if (supertype != null) {
                    supertype.accept(this);
                    super.visitClassType(classType);
                }
            }

        }
        Visitor visitor = new Visitor();
        subtype.accept(visitor);
        for (DeclaredType type : visitor.supertypes) {
            TypeDeclaration subtypeDeclaration = type.getDeclaration();
            if (subtypeDeclaration.equals(supertypeDeclaration)) {
                return type;
            }
        }
        return null;
    }
}
