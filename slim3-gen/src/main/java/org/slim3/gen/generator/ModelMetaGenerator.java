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

import static org.slim3.gen.ClassConstants.CollectionAttributeMeta;
import static org.slim3.gen.ClassConstants.CoreAttributeMeta;
import static org.slim3.gen.ClassConstants.Entity;
import static org.slim3.gen.ClassConstants.Object;
import static org.slim3.gen.ClassConstants.StringAttributeMeta;

import java.util.Date;

import org.slim3.gen.ClassConstants;
import org.slim3.gen.ProductInfo;
import org.slim3.gen.datastore.ArrayType;
import org.slim3.gen.datastore.CollectionType;
import org.slim3.gen.datastore.CorePrimitiveType;
import org.slim3.gen.datastore.CoreReferenceType;
import org.slim3.gen.datastore.DataType;
import org.slim3.gen.datastore.KeyType;
import org.slim3.gen.datastore.SimpleDataTypeVisitor;
import org.slim3.gen.datastore.StringType;
import org.slim3.gen.desc.AttributeMetaDesc;
import org.slim3.gen.desc.ModelMetaDesc;
import org.slim3.gen.printer.Printer;
import org.slim3.gen.util.ClassUtil;

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
        p.indent();
        printConstructor(p);
        p.unindent();
        p.println();
        p.indent();
        printAttributeMetaFields(p);
        printEntityToModelMethod(p);
        printModelToEntityMethod(p);
        p.unindent();
        p.print("}");
    }

    protected void printConstructor(Printer p) {
        p.println("public %s() {", modelMetaDesc.getSimpleName());
        p.println("    super(%s.class);", modelMetaDesc.getModelClassName());
        p.println("}");
    }

    protected void printAttributeMetaFields(Printer p) {
        AttributeMetaFieldsGenerator generator =
            new AttributeMetaFieldsGenerator(p);
        generator.generate();
    }

    protected void printEntityToModelMethod(Printer p) {
        EntityToModelMethodGenerator generator =
            new EntityToModelMethodGenerator(p);
        generator.generate();
    }

    protected void printModelToEntityMethod(Printer p) {
        ModelToEntityMethodGenerator generator =
            new ModelToEntityMethodGenerator(p);
        generator.generate();
    }

    protected class AttributeMetaFieldsGenerator extends
            SimpleDataTypeVisitor<Void, AttributeMetaDesc, RuntimeException> {

        protected final Printer printer;

        protected AttributeMetaFieldsGenerator(Printer printer) {
            this.printer = printer;
        }

        public void generate() {
            for (AttributeMetaDesc attr : modelMetaDesc
                .getAttributeMetaDescList()) {
                if (attr.isImpermanent()) {
                    continue;
                }
                DataType dataType = attr.getDataType();
                dataType.accept(this, attr);
                printer.println();
            }
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
        public Void visitCoreReferenceType(CoreReferenceType type,
                AttributeMetaDesc p) throws RuntimeException {
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
        public Void visitCorePrimitiveType(CorePrimitiveType type,
                AttributeMetaDesc p) throws RuntimeException {
            printCoreAttributeMetaField(p.getName(), type.getClassName(), type
                .getTypeName());
            return null;
        }

        protected void printCoreAttributeMetaField(String fieldName,
                String attributeClassName, String attributeTypeName) {
            printer
                .println(
                    "public %1$s<%2$s, %3$s> %4$s = new %1$s<%2$s, %3$s>(this, \"%4$s\", %5$s.class);",
                    CoreAttributeMeta,
                    modelMetaDesc.getModelClassName(),
                    attributeTypeName,
                    fieldName,
                    attributeClassName);
        }

        protected void printKeyAttributeMetaField(String fieldName,
                String attributeClassName, String attributeTypeName) {
            printer
                .println(
                    "public %1$s<%2$s, %3$s> %4$s = new %1$s<%2$s, %3$s>(this, \"__key__\", %5$s.class);",
                    CoreAttributeMeta,
                    modelMetaDesc.getModelClassName(),
                    attributeTypeName,
                    fieldName,
                    attributeClassName);
        }

        protected void printStringAttributeMetaField(String fieldName) {
            printer.println(
                "public %1$s<%2$s> %3$s = new %1$s<%2$s>(this, \"%3$s\");",
                StringAttributeMeta,
                modelMetaDesc.getModelClassName(),
                fieldName);
        }

        protected void printCollectionAttributeMetaField(String fieldName,
                String attributeClassName, String attributeTypeName,
                String elementTypeName) {
            printer
                .println(
                    "public %1$s<%2$s, %3$s, %4$s> %5$s = new %1$s<%2$s, %3$s, %4$s>(this, \"%5$s\", %6$s.class);",
                    CollectionAttributeMeta,
                    modelMetaDesc.getModelClassName(),
                    attributeTypeName,
                    elementTypeName,
                    fieldName,
                    attributeClassName);
        }

    }

    protected class EntityToModelMethodGenerator extends
            SimpleDataTypeVisitor<Void, AttributeMetaDesc, RuntimeException> {

        protected final Printer printer;

        /**
         * @param defaultValue
         */
        public EntityToModelMethodGenerator(Printer printer) {
            this.printer = printer;
        }

        public void generate() {
            printer.println("@Override");
            printer.println(
                "public %1$s entityToModel(%2$s entity) {",
                modelMetaDesc.getModelClassName(),
                Entity);
            printer.indent();
            printer.println("%1$s model = new %1$s();", modelMetaDesc
                .getModelClassName());
            for (AttributeMetaDesc attr : modelMetaDesc
                .getAttributeMetaDescList()) {
                if (attr.isImpermanent()) {
                    continue;
                }
                DataType dataType = attr.getDataType();
                dataType.accept(this, attr);
            }
            printer.println("return model;");
            printer.unindent();
            printer.println("}");
            printer.println();
        }

        @Override
        public Void visitKeyType(KeyType type, AttributeMetaDesc p)
                throws RuntimeException {
            printer.println("model.%1$s(entity.getKey());", p
                .getWriteMethodName());
            return null;
        }

        @Override
        public Void visitStringType(StringType type, AttributeMetaDesc p)
                throws RuntimeException {
            printer.println(
                "model.%1$s((%2$s) entity.getProperty(\"%3$s\"));",
                p.getWriteMethodName(),
                type.getTypeName(),
                p.getName());
            return null;
        }

    }

    protected class ModelToEntityMethodGenerator extends
            SimpleDataTypeVisitor<Void, AttributeMetaDesc, RuntimeException> {

        protected final Printer printer;

        public ModelToEntityMethodGenerator(Printer printer) {
            this.printer = printer;
        }

        public void generate() {
            printer.println("@Override");
            printer.println(
                "public %1$s modelToEntity(%2$s m) {",
                Entity,
                Object);
            printer.indent();
            printer.println("%1$s model = (%1$s) m;", modelMetaDesc
                .getModelClassName());
            printer.println("%1$s entity = null;", Entity);
            printer.println("if (model.getKey() != null) {");
            printer.println("    entity = new %1$s(model.getKey());", Entity);
            printer.println("} else {");
            printer.println(
                "    entity = new %1$s(\"%2$s\");",
                Entity,
                ClassUtil.getSimpleName(modelMetaDesc.getModelClassName()));
            printer.println("}");
            for (AttributeMetaDesc attr : modelMetaDesc
                .getAttributeMetaDescList()) {
                if (attr.isImpermanent() || attr.isPrimaryKey()) {
                    continue;
                }
                DataType dataType = attr.getDataType();
                dataType.accept(this, attr);
            }
            printer.println("return entity;");
            printer.unindent();
            printer.println("}");
        }

        @Override
        public Void visitCoreReferenceType(CoreReferenceType type,
                AttributeMetaDesc p) throws RuntimeException {
            printer.println("entity.setProperty(\"%1$s\", model.%2$s());", p
                .getName(), p.getReadMethodName());
            return super.visitCoreReferenceType(type, p);
        }

    }

}
