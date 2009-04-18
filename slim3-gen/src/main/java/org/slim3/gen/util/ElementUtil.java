package org.slim3.gen.util;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
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
import javax.lang.model.util.Types;

public final class ElementUtil {

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

    public static boolean isBindableField(VariableElement e) {
        if (e.getKind() == ElementKind.FIELD) {
            Set<Modifier> modifires = e.getModifiers();
            if (modifires.contains(Modifier.PUBLIC)
                    && !modifires.contains(Modifier.STATIC)) {
                return true;
            }
        }
        return false;
    }

    public static String toText(VariableElement e, Types types) {
        StringBuilder buf = new StringBuilder();
        e.asType().accept(new ToText(types), buf);
        return buf.toString();
    }

    protected static class ToText extends
            SimpleTypeVisitor6<Void, StringBuilder> {

        protected final Types types;

        public ToText(Types types) {
            this.types = types;
        }

        @Override
        public Void visitArray(ArrayType t, StringBuilder p) {
            t.getComponentType().accept(this, p);
            p.append("[]");
            return DEFAULT_VALUE;
        }

        @Override
        public Void visitPrimitive(PrimitiveType t, StringBuilder p) {
            types.boxedClass(t).asType().accept(this, p);
            return DEFAULT_VALUE;
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
            return DEFAULT_VALUE;
        }

        @Override
        public Void visitError(ErrorType t, StringBuilder p) {
            p.append("Object");
            return DEFAULT_VALUE;
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
            return DEFAULT_VALUE;
        }

        protected void appendName(DeclaredType t, StringBuilder p) {
            t.asElement().accept(
                    new SimpleElementVisitor6<Void, StringBuilder>() {
                        @Override
                        public Void visitType(TypeElement e, StringBuilder p) {
                            p.append(e.getSimpleName());
                            return DEFAULT_VALUE;
                        }
                    }, p);
        }
    }

    public static void collectTypeElement(VariableElement e,
            Collection<TypeElement> result) {
        e.asType().accept(new CollectTypeElement(), result);
    }

    protected static class CollectTypeElement extends
            SimpleTypeVisitor6<Void, Collection<TypeElement>> {
        @Override
        public Void visitArray(ArrayType t, Collection<TypeElement> p) {
            t.getComponentType().accept(this, p);
            return DEFAULT_VALUE;
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
            return DEFAULT_VALUE;
        }

        @Override
        public Void visitDeclared(DeclaredType t, Collection<TypeElement> p) {
            t.asElement().accept(
                    new SimpleElementVisitor6<Void, Collection<TypeElement>>() {
                        @Override
                        public Void visitType(TypeElement e,
                                Collection<TypeElement> p) {
                            p.add(e);
                            return DEFAULT_VALUE;
                        }
                    }, p);

            List<? extends TypeMirror> typeArgs = t.getTypeArguments();
            if (typeArgs.size() > 0) {
                for (TypeMirror arg : t.getTypeArguments()) {
                    arg.accept(this, p);
                }
            }
            return DEFAULT_VALUE;
        }
    }
}
