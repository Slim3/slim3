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

import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.declaration.TypeDeclaration;
import com.sun.mirror.type.DeclaredType;
import com.sun.mirror.type.PrimitiveType;
import com.sun.mirror.type.TypeMirror;
import com.sun.mirror.type.VoidType;
import com.sun.mirror.type.PrimitiveType.Kind;
import com.sun.mirror.util.SimpleTypeVisitor;

/**
 * @author taedium
 * 
 */
public final class TypeUtil {

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

    public static TypeDeclaration toTypeDeclaration(TypeMirror typeMirror) {
        class Visitor extends SimpleTypeVisitor {
            TypeDeclaration result;

            @Override
            public void visitDeclaredType(DeclaredType declaredtype) {
                result = declaredtype.getDeclaration();
            }
        }
        Visitor visitor = new Visitor();
        typeMirror.accept(visitor);
        return visitor.result;
    }

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

    public static boolean isSubtype(AnnotationProcessorEnvironment env,
            TypeMirror suptype, Class supertype) {
        TypeDeclaration collection =
            env.getTypeDeclaration(supertype.getName());
        return env.getTypeUtils().isSubtype(
            env.getTypeUtils().getErasure(suptype),
            env.getTypeUtils().getDeclaredType(collection));
    }
}
