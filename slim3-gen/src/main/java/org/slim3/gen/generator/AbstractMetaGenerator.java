package org.slim3.gen.generator;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Generated;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementScanner6;
import javax.lang.model.util.Elements;
import javax.tools.JavaFileObject;

import org.slim3.gen.ProductInfo;
import org.slim3.gen.option.Options;
import org.slim3.gen.util.Logger;

public abstract class AbstractMetaGenerator extends
        ElementScanner6<Void, Printer> implements Generator<TypeElement> {

    protected final ProcessingEnvironment processingEnv;

    protected long startTime;

    public AbstractMetaGenerator(ProcessingEnvironment processingEnv) {
        this.processingEnv = processingEnv;
    }

    public void generate(TypeElement element) {
        Printer printer = null;
        String fileName = null;
        Filer filer = processingEnv.getFiler();
        try {
            JavaFileObject file = filer.createSourceFile(element
                    .getQualifiedName()
                    + getSuffix());
            fileName = file.getName();
            start(fileName);
            printer = new Printer(file);
            scan(element, printer);
        } catch (IOException e) {
            Logger.error(processingEnv, "[%s] Failed to handle element(%s).",
                    getClass().getName(), element.getQualifiedName());
            throw new RuntimeException(e);
        } finally {
            if (printer != null) {
                printer.close();
            }
        }
        end(fileName);
    }

    @Override
    public Void visitType(TypeElement e, Printer p) {
        Elements elements = processingEnv.getElementUtils();
        PackageElement packageElement = elements.getPackageOf(e);
        if (packageElement.getQualifiedName().length() > 0) {
            p.println("package %s ;", packageElement.getQualifiedName());
            p.println();
        }
        p.println("import %s;", Generated.class.getName());
        p.println();
        p.println("@%s(value = { \"%s\", \"%s\" }, date = \"%s\")",
                Generated.class.getSimpleName(), ProductInfo.getName(),
                ProductInfo.getVersion(), getDate());
        p.println("public final class %s%s {", e.getSimpleName(), getSuffix());
        p.println();
        scan(e.getEnclosedElements(), p);
        p.print("}");
        return DEFAULT_VALUE;
    }

    protected String getSuffix() {
        return Options.getMetaSuffix(processingEnv);
    }

    protected String getDate() {
        String pattern = Options.getDatepattern(processingEnv);
        SimpleDateFormat dateFormat = pattern != null ? new SimpleDateFormat(
                pattern) : new SimpleDateFormat();
        return dateFormat.format(new Date());
    }

    protected void start(String fileName) {
        if (Options.isDebugEnabled(processingEnv)) {
            Logger.debug(processingEnv, "[%s] File(%s) is generating.",
                    getClass().getName(), fileName);
            startTime = System.nanoTime();
        }
    }

    protected void end(String fileName) {
        if (Options.isDebugEnabled(processingEnv)) {
            Logger.debug(processingEnv,
                    "[%s] File(%s) is generated. elapsed=%10d(nano)",
                    getClass().getName(), fileName, System.nanoTime()
                            - startTime);
        }
    }

}
