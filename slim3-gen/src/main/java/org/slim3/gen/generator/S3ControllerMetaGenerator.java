package org.slim3.gen.generator;

import java.util.Formatter;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;

import org.slim3.gen.annotation.Annotations;
import org.slim3.gen.util.ElementUtil;
import org.slim3.gen.util.Logger;

public class S3ControllerMetaGenerator extends AbstractMetaGenerator {

    protected final Set<String> names = new HashSet<String>();

    public S3ControllerMetaGenerator(ProcessingEnvironment processingEnv) {
        super(processingEnv);
    }

    @Override
    public Void visitVariable(VariableElement e, Formatter p) {
        if (ElementUtil.isBindableField(e)) {
            printProperty(e, p);
        }
        return DEFAULT_VALUE;
    }

    @Override
    public Void visitExecutable(ExecutableElement e, Formatter p) {
        if (ElementUtil.isAnnotated(e, Annotations.Execute)) {
            printProperty(e, p);
        }
        return DEFAULT_VALUE;
    }

    protected void printProperty(Element element, Formatter p) {
        String name = element.getSimpleName().toString();
        if (names.contains(name)) {
            Logger.warn(processingEnv, "[%s] property(%s) duplicated.",
                    getClass().getName(), name);
            return;
        }
        names.add(name);
        p.format("    public static final String %s = \"%s\";\n", name, name);
        p.format("\n");
    }

}
