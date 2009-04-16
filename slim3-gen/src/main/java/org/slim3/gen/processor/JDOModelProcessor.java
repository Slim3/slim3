package org.slim3.gen.processor;

import java.io.IOException;

import javax.annotation.processing.Filer;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.tools.FileObject;

import org.slim3.gen.annotation.Annotations;
import org.slim3.gen.generator.Generator;
import org.slim3.gen.generator.JDOModelMetaGenerator;
import org.slim3.gen.option.Options;
import org.slim3.gen.option.printer.FileObjectPrinter;
import org.slim3.gen.option.printer.Printer;
import org.slim3.gen.util.Logger;

@SupportedSourceVersion(SourceVersion.RELEASE_6)
@SupportedAnnotationTypes(Annotations.PersistenceCapable)
@SupportedOptions( { Options.DEBUG })
public class JDOModelProcessor extends AbstractProcessor {

    protected static final String suffix = "Meta";

    protected void handleTypeElement(TypeElement element) {
        if (Options.isDebugEnabled(processingEnv)) {
            Logger.debug(processingEnv, "[%s] Element(%s) is handling.",
                    getClass().getName(), element.getQualifiedName());
        }
        Filer filer = processingEnv.getFiler();
        String name = element.getQualifiedName() + suffix;
        String simpleName = element.getSimpleName() + suffix;
        Printer printer = null;
        try {
            printer = createPrinter(filer.createSourceFile(name, element));
            Generator<Void, TypeElement, Printer> generator = createGenerator(simpleName);
            generator.generate(element, printer);
        } catch (IOException e) {
            Logger.error(processingEnv, element, "[%s] Failed to generate.",
                    getClass().getName());
            throw new RuntimeException(e);
        } finally {
            if (printer != null) {
                printer.close();
            }
        }
        if (Options.isDebugEnabled(processingEnv)) {
            Logger.debug(processingEnv, "[%s] Element(%s) is handled.",
                    getClass().getName(), element.getQualifiedName());
        }
    }

    protected Printer createPrinter(FileObject file) throws IOException {
        return new FileObjectPrinter(file);
    }

    protected Generator<Void, TypeElement, Printer> createGenerator(
            String simpleName) {
        return new JDOModelMetaGenerator(processingEnv, simpleName);
    }
}
