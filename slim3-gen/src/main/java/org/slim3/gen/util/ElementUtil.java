package org.slim3.gen.util;

import java.util.Set;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.ElementKindVisitor6;

public final class ElementUtil {

    public static boolean isAnnotated(Element element, final String annotation) {
        for (AnnotationMirror mirror : element.getAnnotationMirrors()) {
            Element e = mirror.getAnnotationType().asElement();
            return e.accept(new ElementKindVisitor6<Boolean, Void>() {
                @Override
                public Boolean visitTypeAsAnnotationType(TypeElement e, Void p) {
                    return e.getQualifiedName().toString().equals(annotation);
                }
            }, null);
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
}
