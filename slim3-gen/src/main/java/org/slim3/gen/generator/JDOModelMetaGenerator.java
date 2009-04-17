package org.slim3.gen.generator;

import java.util.Date;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.NestingKind;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementScanner6;
import javax.lang.model.util.Elements;
import javax.lang.model.util.SimpleTypeVisitor6;
import javax.lang.model.util.Types;

import org.slim3.gen.ProductInfo;
import org.slim3.gen.annotation.Annotations;
import org.slim3.gen.printer.Printer;
import org.slim3.gen.util.ElementUtil;

public class JDOModelMetaGenerator extends ElementScanner6<Void, Printer>
        implements Generator<Void, TypeElement, Printer> {

    protected final ProcessingEnvironment processingEnv;

    protected final Elements elements;

    protected final Types types;

    protected final String simpleName;

    public JDOModelMetaGenerator(ProcessingEnvironment processingEnv,
            String simpleName) {
        this.processingEnv = processingEnv;
        this.elements = processingEnv.getElementUtils();
        this.types = processingEnv.getTypeUtils();
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
        p.println(
                "@Generated(value = { \"%s\", \"%s\" }, date = \"%tF %<tT\")",
                ProductInfo.getName(), ProductInfo.getVersion(), new Date());
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
                            "    public AttributeMeta<%1$s> %2$s = new AttributeMeta<%1$s>(\"%2$s\");",
                            getTypeText(e), e.getSimpleName());
            p.println();
        }
        return DEFAULT_VALUE;
    }

    protected String getTypeText(VariableElement e) {
        String text = e.asType().accept(new SimpleTypeVisitor6<String, Void>() {
            @Override
            protected String defaultAction(TypeMirror e, Void p) {
                return e.toString();
            }

            @Override
            public String visitPrimitive(PrimitiveType t, Void p) {
                return types.boxedClass(t).asType().toString();
            }
        }, null);

        if (text.startsWith("java.lang.")) {
            return text.substring(10);
        }
        return text;
    }
}
