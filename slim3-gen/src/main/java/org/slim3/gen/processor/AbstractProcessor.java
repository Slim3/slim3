package org.slim3.gen.processor;

import java.util.Set;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;

import org.slim3.gen.option.Options;
import org.slim3.gen.util.Logger;

public abstract class AbstractProcessor extends
        javax.annotation.processing.AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations,
            RoundEnvironment roundEnv) {
        long startTime = 0L;
        if (Options.isDebugEnabled(processingEnv)) {
            Logger.debug(processingEnv, "[%s] Started.", getClass().getName());
            startTime = System.nanoTime();
        }
        for (TypeElement annotation : annotations) {
            for (TypeElement element : ElementFilter.typesIn(roundEnv
                    .getElementsAnnotatedWith(annotation))) {
                handleTypeElement(element);
            }
        }
        if (Options.isDebugEnabled(processingEnv)) {
            Logger.debug(processingEnv, "[%s] Ended. elapsed=%d(nano)",
                    getClass().getName(), System.nanoTime() - startTime);
        }
        return true;
    }

    protected abstract void handleTypeElement(TypeElement element);

}
