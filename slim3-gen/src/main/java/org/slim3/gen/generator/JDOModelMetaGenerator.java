package org.slim3.gen.generator;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.VariableElement;

import org.slim3.gen.annotation.Annotations;
import org.slim3.gen.util.ElementUtil;

public class JDOModelMetaGenerator extends AbstractMetaGenerator {

    public JDOModelMetaGenerator(ProcessingEnvironment processingEnv) {
        super(processingEnv);
    }

    @Override
    public Void visitVariable(VariableElement e, Printer p) {
        if (ElementUtil.isAnnotated(e, Annotations.Persistent)) {
            p.println("    public static final String %s = \"%s\";", e
                    .getSimpleName(), e.getSimpleName());
            p.println();
        }
        return DEFAULT_VALUE;
    }

}
