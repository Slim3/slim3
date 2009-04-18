package org.slim3.gen.generator;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.NestingKind;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.ElementScanner6;
import javax.lang.model.util.Elements;
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
        for (String importName : getImportName(e)) {
            p.println("import %s;", importName);
        }
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

    protected Set<String> getImportName(TypeElement e) {
        Set<TypeElement> typeElements = new HashSet<TypeElement>();
        new ElementScanner6<Void, Set<TypeElement>>() {
            @Override
            public Void visitVariable(VariableElement e, Set<TypeElement> p) {
                if (ElementUtil.isAnnotated(e, Annotations.Persistent)) {
                    ElementUtil.collectTypeElement(e, p);
                }
                return DEFAULT_VALUE;
            }
        }.scan(e, typeElements);
        Set<String> result = new TreeSet<String>();
        for (Iterator<TypeElement> i = typeElements.iterator(); i.hasNext();) {
            String name = i.next().toString();
            if (!name.startsWith("java.lang.")) {
                result.add(name);
            }
        }
        result.add("javax.annotation.Generated");
        result.add("org.slim3.gae.jdo.AttributeMeta");
        result.add("org.slim3.gae.jdo.ModelMeta");
        return result;
    }

    @Override
    public Void visitVariable(VariableElement e, Printer p) {
        if (ElementUtil.isAnnotated(e, Annotations.Persistent)) {
            p
                    .println(
                            "    public AttributeMeta<%1$s> %2$s = new AttributeMeta<%1$s>(\"%2$s\");",
                            ElementUtil.toText(e, types), e.getSimpleName());
            p.println();
        }
        return DEFAULT_VALUE;
    }
}
