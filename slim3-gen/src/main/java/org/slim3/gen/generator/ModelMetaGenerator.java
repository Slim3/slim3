/*
 * Copyright 2004-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.slim3.gen.generator;

import java.util.Date;

import org.slim3.gen.ClassConstants;
import org.slim3.gen.ProductInfo;
import org.slim3.gen.datastore.ArrayType;
import org.slim3.gen.datastore.CollectionType;
import org.slim3.gen.datastore.CoreType;
import org.slim3.gen.datastore.DataType;
import org.slim3.gen.datastore.KeyType;
import org.slim3.gen.datastore.PrimitiveType;
import org.slim3.gen.datastore.SimpleDataTypeVisitor;
import org.slim3.gen.datastore.StringType;
import org.slim3.gen.desc.AttributeMetaDesc;
import org.slim3.gen.desc.ModelMetaDesc;
import org.slim3.gen.printer.Printer;

/**
 * Generates a JDO model meta java file.
 * 
 * @author taedium
 * @since 3.0
 * 
 */
public class ModelMetaGenerator implements Generator {

    /** the model meta description */
    protected final ModelMetaDesc modelMetaDesc;

    /**
     * Creates a new {@link ModelMetaGenerator}.
     * 
     * @param modelMetaDesc
     *            the model meta description
     */
    public ModelMetaGenerator(ModelMetaDesc modelMetaDesc) {
        if (modelMetaDesc == null) {
            throw new NullPointerException(
                "The modelMetaDesc parameter is null.");
        }
        this.modelMetaDesc = modelMetaDesc;
    }

    public void generate(Printer p) {
        printPackage(p);
        printClass(p);
    }

    protected void printPackage(Printer p) {
        if (modelMetaDesc.getPackageName().length() != 0) {
            p.println("package %s;", modelMetaDesc.getPackageName());
            p.println();
        }
    }

    protected void printClass(Printer p) {
        p
            .println(
                "//@javax.annotation.Generated(value = { \"%s\", \"%s\" }, date = \"%tF %<tT\")",
                ProductInfo.getName(),
                ProductInfo.getVersion(),
                new Date());
        p.println("public final class %s extends %s<%s> {", modelMetaDesc
            .getSimpleName(), ClassConstants.ModelMeta, modelMetaDesc
            .getModelClassName());
        p.println();
        printConstructor(p);
        p.println();
        printAttributeMetaFields(p);
        printEntityToModelMethod(p);
        printModelToEntityMethod(p);
        p.print("}");
    }

    protected void printConstructor(Printer p) {
        p.println("    public %s() {", modelMetaDesc.getSimpleName());
        p
            .println("        super(%s.class);", modelMetaDesc
                .getModelClassName());
        p.println("    }");
    }

    protected void printAttributeMetaFields(Printer p) {
        AttributeMetaFieldPrintingVisitor visitor =
            new AttributeMetaFieldPrintingVisitor(p);
        for (AttributeMetaDesc attr : modelMetaDesc.getAttributeMetaDescList()) {
            if (attr.isUnindexed()) {
                continue;
            }
            DataType dataType = attr.getDataType();
            dataType.accept(visitor, attr);
            p.println();
        }
    }

    protected void printEntityToModelMethod(Printer p) {
        p.println("    @Override");
        p.println("    public %1$s entityToModel(%2$s entity) {", modelMetaDesc
            .getModelClassName(), ClassConstants.Entity);
        p.println("        %1$s model = new %1$s();", modelMetaDesc
            .getModelClassName());
        EntityToModelMethodPrintingVisitor visitor =
            new EntityToModelMethodPrintingVisitor(p);
        for (AttributeMetaDesc attr : modelMetaDesc.getAttributeMetaDescList()) {
            DataType dataType = attr.getDataType();
            dataType.accept(visitor, attr);

            // if (attr.isPrimaryKey()) {
            // p.println("        model.%1$s(entity.getKey());", attr
            // .getWriteMethodName());
            // } else if (attr.isSerialized()) {
            // if (attr.isBlob()) {
            // printConversionStatement(
            // p,
            // attr,
            // "toSerializable",
            // ClassConstants.Blob,
            // attr.getTypeName());
            // } else {
            // printConversionStatement(
            // p,
            // attr,
            // "toSerializable",
            // ClassConstants.ShortBlob,
            // attr.getTypeName());
            // }
            // } else if (attr.isCollection()) {
            // if (ClassConstants.Short.equals(attr.getElementTypeName())) {
            // printConversionStatement(
            // p,
            // attr,
            // "toShortList",
            // ClassConstants.List + "<" + ClassConstants.Long + ">",
            // ClassConstants.List
            // + "<"
            // + attr.getElementTypeName()
            // + ">");
            // }
            // } else if (attr.isText()) {
            // printConversionStatement(
            // p,
            // attr,
            // "toString",
            // ClassConstants.Text);
            // } else if (attr.isArray()
            // && ClassConstants.primitve_byte.equals(attr
            // .getElementTypeName())) {
            // if (attr.isBlob()) {
            // printConversionStatement(
            // p,
            // attr,
            // "toBytes",
            // ClassConstants.Blob);
            // } else {
            // printConversionStatement(
            // p,
            // attr,
            // "toBytes",
            // ClassConstants.ShortBlob);
            // }
            // } else if (attr.isPrimitive()) {
            // if (ClassConstants.primitve_short.equals(attr.getTypeName())) {
            // printConversionStatement(
            // p,
            // attr,
            // "toPrimitiveShort",
            // ClassConstants.Long);
            // }
            // } else {
            // if (ClassConstants.Short.equals(attr.getTypeName())) {
            // printConversionStatement(
            // p,
            // attr,
            // "toShort",
            // ClassConstants.Long);
            // }
            // }
        }
        p.println("        return model;");
        p.println("   }");
        p.println();
    }

    protected void printModelToEntityMethod(Printer p) {
        p.println("    @Override");
        p.println(
            "    public %1$s modelToEntity(%2$s model) {",
            ClassConstants.Entity,
            ClassConstants.Object);
        p.println("        return null;");
        p.println("   }");
    }

    public class AttributeMetaFieldPrintingVisitor extends
            SimpleDataTypeVisitor<Void, AttributeMetaDesc, RuntimeException> {

        protected final Printer printer;

        public AttributeMetaFieldPrintingVisitor(Printer printer) {
            this.printer = printer;
        }

        @Override
        public Void visitArrayType(ArrayType type, AttributeMetaDesc p)
                throws RuntimeException {
            DataType componentType = type.getComponentType();
            printCollectionAttributeMetaField(
                p.getName(),
                type.getClassName(),
                type.getTypeName(),
                componentType.getTypeName());
            return null;
        }

        @Override
        public Void visitCollectionType(CollectionType type, AttributeMetaDesc p)
                throws RuntimeException {
            DataType elementType = type.getElementType();
            printCollectionAttributeMetaField(
                p.getName(),
                type.getClassName(),
                type.getTypeName(),
                elementType.getTypeName());
            return super.visitCollectionType(type, p);
        }

        @Override
        public Void visitCoreType(CoreType type, AttributeMetaDesc p)
                throws RuntimeException {
            printCoreAttributeMetaField(p.getName(), type.getClassName(), type
                .getTypeName());
            return null;
        }

        @Override
        public Void visitKeyType(KeyType type, AttributeMetaDesc p)
                throws RuntimeException {
            printCoreAttributeMetaField("__key__", type.getClassName(), type
                .getTypeName());
            return null;
        }

        @Override
        public Void visitStringType(StringType type, AttributeMetaDesc p)
                throws RuntimeException {
            printStringAttributeMetaField(p.getName());
            return null;
        }

        @Override
        public Void visitPrimitiveType(PrimitiveType type, AttributeMetaDesc p)
                throws RuntimeException {
            printCoreAttributeMetaField(p.getName(), type.getClassName(), type
                .getTypeName());
            return null;
        }

        protected void printCoreAttributeMetaField(String fieldName,
                String attributeClassName, String attributeTypeName) {
            printer
                .println(
                    "    public %1$s<%2$s, %3$s> %4$s = new %1$s<%2$s, %3$s>(this, \"%4$s\", %5$s.class);",
                    ClassConstants.CoreAttributeMeta,
                    modelMetaDesc.getModelClassName(),
                    attributeTypeName,
                    fieldName,
                    attributeClassName);
        }

        protected void printStringAttributeMetaField(String fieldName) {
            printer.println(
                "    public %1$s<%2$s> %3$s = new %1$s<%2$s>(this, \"%3$s\");",
                ClassConstants.StringAttributeMeta,
                modelMetaDesc.getModelClassName(),
                fieldName);
        }

        protected void printCollectionAttributeMetaField(String fieldName,
                String attributeClassName, String attributeTypeName,
                String elementTypeName) {
            printer
                .println(
                    "    public %1$s<%2$s, %3$s, %4$s> %5$s = new %1$s<%2$s, %3$s, %4$s>(this, \"%2$s\", %5$s.class);",
                    ClassConstants.CollectionAttributeMeta,
                    modelMetaDesc.getModelClassName(),
                    attributeTypeName,
                    elementTypeName,
                    fieldName,
                    attributeClassName);
        }

    }

    public class EntityToModelMethodPrintingVisitor extends
            SimpleDataTypeVisitor<Void, AttributeMetaDesc, RuntimeException> {

        protected Printer printer;

        /**
         * @param defaultValue
         */
        public EntityToModelMethodPrintingVisitor(Printer printer) {
            this.printer = printer;
        }

        @Override
        public Void visitKeyType(KeyType type, AttributeMetaDesc p)
                throws RuntimeException {
            printer.println("        model.%1$s(entity.getKey());", p
                .getWriteMethodName());
            return null;
        }

        protected void printConversionStatement(AttributeMetaDesc attr,
                String conversionMethodName, String srcTypeName) {
            printer
                .println(
                    "        model.%2$s(%3$s((%4$s) entity.getProperty(\"%1$s\")));",
                    attr.getName(),
                    attr.getWriteMethodName(),
                    conversionMethodName,
                    srcTypeName);
        }

        protected void printConversionStatement(AttributeMetaDesc attr,
                String conversionMethodName, String srcTypeName,
                String destTypeName) {
            printer
                .println(
                    "        model.%2$s((%5$s) %3$s((%4$s) entity.getProperty(\"%1$s\")));",
                    attr.getName(),
                    attr.getWriteMethodName(),
                    conversionMethodName,
                    srcTypeName,
                    destTypeName);
        }

    }

}
