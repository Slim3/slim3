package org.slim3.gen.generator;

import java.util.Date;

import javax.annotation.Generated;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.NestingKind;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.ElementScanner6;
import javax.lang.model.util.Elements;

import org.slim3.gen.ProductInfo;
import org.slim3.gen.annotation.Annotations;
import org.slim3.gen.option.printer.Printer;
import org.slim3.gen.util.ElementUtil;

public class JDOModelMetaGenerator extends ElementScanner6<Void, Printer>
        implements Generator<Void, TypeElement, Printer> {

    protected final ProcessingEnvironment processingEnv;

    protected final String simpleName;

    public JDOModelMetaGenerator(ProcessingEnvironment processingEnv,
            String simpleName) {
        this.processingEnv = processingEnv;
        this.simpleName = simpleName;
    }

    @Override
    public Void generate(TypeElement e, Printer p) {
        return scan(e, p);
    }

    @Override
    public Void visitType(TypeElement e, Printer p) {
        if (e.getNestingKind() != NestingKind.TOP_LEVEL) {
            return DEFAULT_VALUE;
        }
        Elements elements = processingEnv.getElementUtils();
        PackageElement packageElement = elements.getPackageOf(e);
        if (packageElement.getQualifiedName().length() > 0) {
            p.println("package %s;", packageElement.getQualifiedName());
            p.println();
        }
        p.println("import javax.annotation.Generated;");
        p.println();
        p.println("import org.slim3.gae.jdo.AttributeMeta;");
        p.println("import org.slim3.gae.jdo.ModelMeta;");
        p.println();
        p.println("@%s(value = { \"%s\", \"%s\" }, date = \"%tF %<tT\")",
                Generated.class.getSimpleName(), ProductInfo.getName(),
                ProductInfo.getVersion(), new Date());
        p.println("public final class %s extends ModelMeta<%s> {", simpleName,
                e.getSimpleName());
        p.println();
        p.println("    public %s() {", simpleName);
        p.println("        super(%s.class);", e.getSimpleName());
        p.println("    }");
        p.println();
        scan(e.getEnclosedElements(), p);
        p.print("}");
        return DEFAULT_VALUE;
    }

    @Override
    public Void visitVariable(VariableElement e, Printer p) {
        if (ElementUtil.isAnnotated(e, Annotations.Persistent)) {
            p
                    .println(
                            "    public AttributeMeta %s = new AttributeMeta(\"%<s\");",
                            e.getSimpleName());
            p.println();
        }
        return DEFAULT_VALUE;
    }

}
