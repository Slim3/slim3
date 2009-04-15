package org.slim3.gen.processor;

import java.util.Set;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;

import org.slim3.gen.generator.Generator;
import org.slim3.gen.option.Options;
import org.slim3.gen.util.Logger;

public abstract class AbstractProcessor extends
        javax.annotation.processing.AbstractProcessor {

    protected int count;

    protected long startTime;

    @Override
    public boolean process(Set<? extends TypeElement> annotations,
            RoundEnvironment roundEnv) {
        start();
        for (TypeElement annotation : annotations) {
            for (TypeElement element : ElementFilter.typesIn(roundEnv
                    .getElementsAnnotatedWith(annotation))) {
                Generator<TypeElement> generator = createGenerator();
                generator.generate(element);
                end(true);
                return true;
            }
        }
        end(false);
        return false;
    }

    protected abstract Generator<TypeElement> createGenerator();

    protected void start() {
        if (Options.isDebugEnabled(processingEnv)) {
            Logger.debug(processingEnv, "[%s] process started. count=%d.",
                    getClass().getName(), count);
            startTime = System.nanoTime();
        }
    }

    protected void end(boolean claimed) {
        if (Options.isDebugEnabled(processingEnv)) {
            Logger
                    .debug(
                            processingEnv,
                            "[%s] process ended. claimed=%5b count=%d. elapsed=%10d(nano)",
                            getClass().getName(), claimed, count++, System
                                    .nanoTime()
                                    - startTime);
        }
    }
}
