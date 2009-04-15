package org.slim3.gen.generator;

import java.util.Formatter;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.VariableElement;

import org.slim3.gen.annotation.Annotations;
import org.slim3.gen.util.ElementUtil;

public class JDOEntityMetaGenerator extends AbstractMetaGenerator {

    public JDOEntityMetaGenerator(ProcessingEnvironment processingEnv) {
        super(processingEnv);
    }

    @Override
    public Void visitVariable(VariableElement e, Formatter p) {
        if (ElementUtil.isAnnotated(e, Annotations.Persistent)) {
            p.format("    public static final String %s = \"%s\";\n", e
                    .getSimpleName(), e.getSimpleName());
            p.format("\n");
        }
        return DEFAULT_VALUE;
    }

}
