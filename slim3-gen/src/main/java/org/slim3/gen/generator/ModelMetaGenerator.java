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

import static org.slim3.gen.ClassConstants.*;

import java.util.Date;

import org.slim3.gen.ClassConstants;
import org.slim3.gen.ProductInfo;
import org.slim3.gen.datastore.ArrayType;
import org.slim3.gen.datastore.BlobType;
import org.slim3.gen.datastore.CollectionType;
import org.slim3.gen.datastore.CorePrimitiveType;
import org.slim3.gen.datastore.CoreReferenceType;
import org.slim3.gen.datastore.DataType;
import org.slim3.gen.datastore.EnumType;
import org.slim3.gen.datastore.FloatType;
import org.slim3.gen.datastore.IntegerType;
import org.slim3.gen.datastore.KeyType;
import org.slim3.gen.datastore.ListType;
import org.slim3.gen.datastore.LongType;
import org.slim3.gen.datastore.PrimitiveBooleanType;
import org.slim3.gen.datastore.PrimitiveByteType;
import org.slim3.gen.datastore.PrimitiveDoubleType;
import org.slim3.gen.datastore.PrimitiveFloatType;
import org.slim3.gen.datastore.PrimitiveIntType;
import org.slim3.gen.datastore.PrimitiveLongType;
import org.slim3.gen.datastore.PrimitiveShortType;
import org.slim3.gen.datastore.SetType;
import org.slim3.gen.datastore.ShortBlobType;
import org.slim3.gen.datastore.ShortType;
import org.slim3.gen.datastore.SimpleDataTypeVisitor;
import org.slim3.gen.datastore.SortedSetType;
import org.slim3.gen.datastore.StringType;
import org.slim3.gen.datastore.TextType;
import org.slim3.gen.desc.AttributeMetaDesc;
import org.slim3.gen.desc.ModelMetaDesc;
import org.slim3.gen.printer.Printer;
import org.slim3.gen.util.ClassUtil;

/**
 * Generates a model meta java file.
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

    public void generate(Printer printer) {
        if (printer == null) {
            throw new NullPointerException("The printer parameter is null.");
        }
        printPackage(printer);
        printClass(printer);
    }

    /**
     * Prints the package.
     * 
     * @param printer
     *            the printer
     */
    protected void printPackage(Printer printer) {
        if (modelMetaDesc.getPackageName().length() != 0) {
            printer.println("package %s;", modelMetaDesc.getPackageName());
            printer.println();
        }
    }

    /**
     * Prints the class.
     * 
     * @param printer
     */
    protected void printClass(Printer printer) {
        printer
            .println(
                "//@javax.annotation.Generated(value = { \"%s\", \"%s\" }, date = \"%tF %<tT\")",
                ProductInfo.getName(),
                ProductInfo.getVersion(),
                new Date());
        printer.println("public final class %s extends %s<%s> {", modelMetaDesc
            .getSimpleName(), ClassConstants.ModelMeta, modelMetaDesc
            .getModelClassName());
        printer.println();
        printer.indent();
        printConstructor(printer);
        printer.unindent();
        printer.println();
        printer.indent();
        printAttributeMetaFields(printer);
        printSetKeyMethod(printer);
        printGetVersion(printer);
        printIncrementVersionMethod(printer);
        printEntityToModelMethod(printer);
        printModelToEntityMethod(printer);
        printer.unindent();
        printer.print("}");
    }

    /**
     * Prints the constructor.
     * 
     * @param printer
     *            the printer
     */
    protected void printConstructor(Printer printer) {
        printer.println("public %s() {", modelMetaDesc.getSimpleName());
        printer.println("    super(\"%1$s\", %2$s.class);", modelMetaDesc
            .getKind(), modelMetaDesc.getModelClassName());
        printer.println("}");
    }

    /**
     * Generates attribute meta fields.
     * 
     * @param printer
     *            the printer
     */
    protected void printAttributeMetaFields(Printer printer) {
        AttributeMetaFieldsGenerator generator =
            new AttributeMetaFieldsGenerator(printer);
        generator.generate();
    }

    /**
     * Generates the {@code entityToModel} method.
     * 
     * @param printer
     *            the prnter
     */
    protected void printEntityToModelMethod(Printer printer) {
        EntityToModelMethodGenerator generator =
            new EntityToModelMethodGenerator(printer);
        generator.generate();
    }

    /**
     * Generates the {@code modelToEntity} method.
     * 
     * @param printer
     *            the prnter
     */
    protected void printModelToEntityMethod(Printer printer) {
        ModelToEntityMethodGenerator generator =
            new ModelToEntityMethodGenerator(printer);
        generator.generate();
    }

    /**
     * Generates the {@code getVersion} method.
     * 
     * @param printer
     *            the prnter
     */
    protected void printGetVersion(final Printer printer) {
        printer.println("@Override");
        printer.println("protected long getVersion(Object model) {");
        final AttributeMetaDesc attr =
            modelMetaDesc.getVersionAttributeMetaDesc();
        if (attr == null) {
            printer
                .println(
                    "    throw new IllegalStateException(\"The version property of the model(%1$s) is not defined.\");",
                    modelMetaDesc.getModelClassName());
        } else {
            printer.println("    %1$s m = (%1$s) model;", modelMetaDesc
                .getModelClassName());
            DataType dataType = attr.getDataType();
            dataType.accept(
                new SimpleDataTypeVisitor<Void, Void, RuntimeException>() {

                    @Override
                    protected Void defaultAction(DataType type, Void p)
                            throws RuntimeException {
                        printer
                            .println(
                                "    throw new IllegalStateException(\"The version property of the model(%1$s) is not defined.\");",
                                modelMetaDesc.getModelClassName());
                        return null;
                    }

                    @Override
                    public Void visitPrimitiveLongType(PrimitiveLongType type,
                            Void p) throws RuntimeException {
                        printer.println("    return m.%1$s();", attr
                            .getReadMethodName());
                        return null;
                    }

                    @Override
                    public Void visitLongType(LongType type, Void p)
                            throws RuntimeException {
                        printer
                            .println(
                                "    return m.%1$s() != null ? m.%1$s().longValue() : 0L;",
                                attr.getReadMethodName());
                        return null;
                    }

                },
                null);
        }
        printer.println("}");
        printer.println();
    }

    /**
     * Generates the {@code incrementVersion} method.
     * 
     * @param printer
     *            the prnter
     */
    protected void printIncrementVersionMethod(final Printer printer) {
        printer.println("@Override");
        printer.println("protected void incrementVersion(Object model) {");
        final AttributeMetaDesc attr =
            modelMetaDesc.getVersionAttributeMetaDesc();
        if (attr != null) {
            printer.println("    %1$s m = (%1$s) model;", modelMetaDesc
                .getModelClassName());
            DataType dataType = attr.getDataType();
            dataType.accept(
                new SimpleDataTypeVisitor<Void, Void, RuntimeException>() {

                    @Override
                    protected Void defaultAction(DataType type, Void p)
                            throws RuntimeException {
                        printer
                            .println(
                                "    throw new IllegalStateException(\"The version property of the model(%1$s) is not defined.\");",
                                modelMetaDesc.getModelClassName());
                        return null;
                    }

                    @Override
                    public Void visitPrimitiveLongType(PrimitiveLongType type,
                            Void p) throws RuntimeException {
                        printer.println("    m.%1$s(m.%2$s() + 1L);", attr
                            .getWriteMethodName(), attr.getReadMethodName());
                        return null;
                    }

                    @Override
                    public Void visitLongType(LongType type, Void p)
                            throws RuntimeException {
                        printer
                            .println(
                                "    long version = m.%1$s() != null ? m.%1$s().longValue() : 0L;",
                                attr.getReadMethodName());
                        printer.println(
                            "    m.%1$s(Long.valueOf(version + 1L));",
                            attr.getWriteMethodName());
                        return null;
                    }

                },
                null);
        }
        printer.println("}");
        printer.println();
    }

    /**
     * Generates the {@code setKey} method.
     * 
     * @param printer
     *            the prnter
     */
    protected void printSetKeyMethod(final Printer printer) {
        printer.println("@Override");
        printer
            .println("protected void setKey(Object model, com.google.appengine.api.datastore.Key key) {");
        final AttributeMetaDesc attr = modelMetaDesc.getKeyAttributeMetaDesc();
        if (attr == null) {
            printer
                .println(
                    "    throw new IllegalStateException(\"The key property of the model(%1$s) is not defined.\");",
                    modelMetaDesc.getModelClassName());
        } else {
            printer.println("    %1$s m = (%1$s) model;", modelMetaDesc
                .getModelClassName());
            printer.println("    m.%1$s(key);", attr.getWriteMethodName());
        }
        printer.println("}");
        printer.println();
    }

    /**
     * Represents attribute meta fields generator.
     * 
     * @author taedium
     * @since 3.0
     * 
     */
    protected class AttributeMetaFieldsGenerator extends
            SimpleDataTypeVisitor<Void, AttributeMetaDesc, RuntimeException> {

        /** the printer */
        protected final Printer printer;

        /**
         * Creates a new {@link AttributeMetaFieldsGenerator}.
         * 
         * @param printer
         *            the printer
         */
        protected AttributeMetaFieldsGenerator(Printer printer) {
            this.printer = printer;
        }

        /**
         * Generates attribute meta fields.
         */
        public void generate() {
            for (AttributeMetaDesc attr : modelMetaDesc
                .getAttributeMetaDescList()) {
                if (attr.isImpermanent()
                    || attr.isLob()
                    || attr.isUnindexed()
                    || attr.getDataType().isSerialized()) {
                    continue;
                }
                DataType dataType = attr.getDataType();
                dataType.accept(this, attr);
            }
        }

        @Override
        public Void visitCorePrimitiveType(CorePrimitiveType type,
                AttributeMetaDesc p) throws RuntimeException {
            printer
                .println(
                    "public %1$s<%2$s, %3$s> %4$s = new %1$s<%2$s, %3$s>(this, \"%5$s\", %6$s.class);",
                    CoreAttributeMeta,
                    modelMetaDesc.getModelClassName(),
                    type.getWrapperClassName(),
                    p.getName(),
                    p.getPropertyName(),
                    type.getClassName());
            printer.println();
            return null;
        }

        @Override
        public Void visitCoreReferenceType(CoreReferenceType type,
                AttributeMetaDesc p) throws RuntimeException {
            printer
                .println(
                    "public %1$s<%2$s, %3$s> %4$s = new %1$s<%2$s, %3$s>(this, \"%5$s\", %6$s.class);",
                    CoreAttributeMeta,
                    modelMetaDesc.getModelClassName(),
                    type.getTypeName(),
                    p.getName(),
                    p.getPropertyName(),
                    type.getClassName());
            printer.println();
            return null;
        }

        @Override
        public Void visitKeyType(KeyType type, AttributeMetaDesc p)
                throws RuntimeException {

            String propertyName = "__key__";
            if (!p.isPrimaryKey()) {
                propertyName = p.getPropertyName();
            }
            printer
                .println(
                    "public %1$s<%2$s, %3$s> %4$s = new %1$s<%2$s, %3$s>(this, \"%5$s\", %6$s.class);",
                    CoreAttributeMeta,
                    modelMetaDesc.getModelClassName(),
                    type.getTypeName(),
                    p.getName(),
                    propertyName,
                    type.getClassName());
            printer.println();
            return null;
        }

        @Override
        public Void visitStringType(StringType type, AttributeMetaDesc p)
                throws RuntimeException {
            printer.println(
                "public %1$s<%2$s> %3$s = new %1$s<%2$s>(this, \"%4$s\");",
                StringAttributeMeta,
                modelMetaDesc.getModelClassName(),
                p.getName(),
                p.getPropertyName());
            printer.println();
            return null;
        }

        @Override
        public Void visitShortBlobType(ShortBlobType type, AttributeMetaDesc p)
                throws RuntimeException {
            return null;
        }

        @Override
        public Void visitBlobType(BlobType type, AttributeMetaDesc p)
                throws RuntimeException {
            return null;
        }

        @Override
        public Void visitTextType(TextType type, AttributeMetaDesc p)
                throws RuntimeException {
            return null;
        }

        @Override
        public Void visitCollectionType(final CollectionType collectionType,
                final AttributeMetaDesc attr) throws RuntimeException {
            DataType elementType = collectionType.getElementType();
            elementType.accept(
                new SimpleDataTypeVisitor<Void, Void, RuntimeException>() {

                    @Override
                    public Void visitCoreReferenceType(
                            CoreReferenceType elementType, Void p)
                            throws RuntimeException {
                        printer
                            .println(
                                "public %1$s<%2$s, %3$s, %4$s> %5$s = new %1$s<%2$s, %3$s, %4$s>(this, \"%6$s\", %7$s.class);",
                                CollectionAttributeMeta,
                                modelMetaDesc.getModelClassName(),
                                collectionType.getTypeName(),
                                elementType.getTypeName(),
                                attr.getName(),
                                attr.getPropertyName(),
                                collectionType.getClassName());
                        printer.println();
                        return null;
                    }

                },
                null);
            return null;
        }
    }

    /**
     * Represents the {@code entityToModel} method generator.
     * 
     * @author taedium
     * @since 3.0
     * 
     */
    protected class EntityToModelMethodGenerator extends
            SimpleDataTypeVisitor<Void, AttributeMetaDesc, RuntimeException> {

        /** the printer */
        protected final Printer printer;

        /**
         * Creates a new {@link EntityToModelMethodGenerator}.
         * 
         * @param printer
         *            the printer
         */
        public EntityToModelMethodGenerator(Printer printer) {
            this.printer = printer;
        }

        /**
         * Generates the entityToModelMethod.
         */
        public void generate() {
            printer.println("@Override");
            printer.println(
                "protected %1$s entityToModel(%2$s entity) {",
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
        protected Void defaultAction(DataType type, AttributeMetaDesc p)
                throws RuntimeException {
            if (p.isLob()) {
                printer
                    .println(
                        "%1$s _%2$s = blobToSerializable((%3$s) entity.getProperty(\"%4$s\"));",
                        type.getTypeName(),
                        p.getName(),
                        Blob,
                        p.getPropertyName());
            } else {
                printer
                    .println(
                        "%1$s _%2$s = shortBlobToSerializable((%3$s) entity.getProperty(\"%4$s\"));",
                        type.getTypeName(),
                        p.getName(),
                        ShortBlob,
                        p.getPropertyName());
            }
            printer.println("model.%1$s(_%2$s);", p.getWriteMethodName(), p
                .getName());
            return null;
        }

        @Override
        public Void visitPrimitiveBooleanType(PrimitiveBooleanType type,
                AttributeMetaDesc p) throws RuntimeException {
            printer
                .println(
                    "model.%1$s(booleanToPrimitiveBoolean((%2$s) entity.getProperty(\"%3$s\")));",
                    p.getWriteMethodName(),
                    type.getWrapperClassName(),
                    p.getPropertyName());
            return null;
        }

        @Override
        public Void visitPrimitiveDoubleType(PrimitiveDoubleType type,
                AttributeMetaDesc p) throws RuntimeException {
            printer
                .println(
                    "model.%1$s(doubleToPrimitiveDouble((%2$s) entity.getProperty(\"%3$s\")));",
                    p.getWriteMethodName(),
                    type.getWrapperClassName(),
                    p.getPropertyName());
            return null;
        }

        @Override
        public Void visitPrimitiveFloatType(PrimitiveFloatType type,
                AttributeMetaDesc p) throws RuntimeException {
            printer
                .println(
                    "model.%1$s(doubleToPrimitiveFloat((%2$s) entity.getProperty(\"%3$s\")));",
                    p.getWriteMethodName(),
                    Double,
                    p.getPropertyName());
            return null;
        }

        @Override
        public Void visitPrimitiveIntType(PrimitiveIntType type,
                AttributeMetaDesc p) throws RuntimeException {
            printer
                .println(
                    "model.%1$s(longToPrimitiveInt((%2$s) entity.getProperty(\"%3$s\")));",
                    p.getWriteMethodName(),
                    Long,
                    p.getPropertyName());
            return null;
        }

        @Override
        public Void visitPrimitiveLongType(PrimitiveLongType type,
                AttributeMetaDesc p) throws RuntimeException {
            printer
                .println(
                    "model.%1$s(longToPrimitiveLong((%2$s) entity.getProperty(\"%3$s\")));",
                    p.getWriteMethodName(),
                    type.getWrapperClassName(),
                    p.getPropertyName());
            return null;
        }

        @Override
        public Void visitPrimitiveShortType(PrimitiveShortType type,
                AttributeMetaDesc p) throws RuntimeException {
            printer
                .println(
                    "model.%1$s(longToPrimitiveShort((%2$s) entity.getProperty(\"%3$s\")));",
                    p.getWriteMethodName(),
                    Long,
                    p.getPropertyName());
            return null;
        }

        @Override
        public Void visitCoreReferenceType(CoreReferenceType type,
                AttributeMetaDesc p) throws RuntimeException {
            printer.println(
                "model.%1$s((%2$s) entity.getProperty(\"%3$s\"));",
                p.getWriteMethodName(),
                type.getTypeName(),
                p.getPropertyName());
            return null;
        }

        @Override
        public Void visitFloatType(FloatType type, AttributeMetaDesc p)
                throws RuntimeException {
            printer
                .println(
                    "model.%1$s(doubleToFloat((%2$s) entity.getProperty(\"%3$s\")));",
                    p.getWriteMethodName(),
                    Double,
                    p.getPropertyName());
            return null;
        }

        @Override
        public Void visitIntegerType(IntegerType type, AttributeMetaDesc p)
                throws RuntimeException {
            printer
                .println(
                    "model.%1$s(longToInteger((%2$s) entity.getProperty(\"%3$s\")));",
                    p.getWriteMethodName(),
                    Long,
                    p.getPropertyName());
            return null;
        }

        @Override
        public Void visitShortType(ShortType type, AttributeMetaDesc p)
                throws RuntimeException {
            printer
                .println(
                    "model.%1$s(longToShort((%2$s) entity.getProperty(\"%3$s\")));",
                    p.getWriteMethodName(),
                    Long,
                    p.getPropertyName());
            return null;
        }

        @Override
        public Void visitStringType(StringType type, AttributeMetaDesc p)
                throws RuntimeException {
            if (p.isLob()) {
                printer
                    .println(
                        "model.%1$s(textToString((%2$s) entity.getProperty(\"%3$s\")));",
                        p.getWriteMethodName(),
                        Text,
                        p.getPropertyName());
                return null;
            }
            return super.visitStringType(type, p);
        }

        @Override
        public Void visitEnumType(EnumType type, AttributeMetaDesc p)
                throws RuntimeException {
            printer
                .println(
                    "model.%1$s(stringToEnum(%2$s.class, (%3$s) entity.getProperty(\"%4$s\")));",
                    p.getWriteMethodName(),
                    type.getTypeName(),
                    String,
                    p.getPropertyName());
            return null;
        }

        @Override
        public Void visitKeyType(KeyType type, AttributeMetaDesc p)
                throws RuntimeException {
            if (p.isPrimaryKey()) {
                printer.println("model.%1$s(entity.getKey());", p
                    .getWriteMethodName());
            } else {
                printer.println(
                    "model.%1$s((%2$s) entity.getProperty(\"%3$s\"));",
                    p.getWriteMethodName(),
                    type.getTypeName(),
                    p.getPropertyName());
            }
            return null;
        }

        @Override
        public Void visitArrayType(ArrayType type, final AttributeMetaDesc attr)
                throws RuntimeException {
            DataType componentType = type.getComponentType();
            componentType.accept(
                new SimpleDataTypeVisitor<Boolean, Void, RuntimeException>(
                    false) {

                    @Override
                    public Boolean visitPrimitiveByteType(
                            PrimitiveByteType type, Void p)
                            throws RuntimeException {
                        if (attr.isLob()) {
                            printer
                                .println(
                                    "model.%1$s(blobToBytes((%2$s) entity.getProperty(\"%3$s\")));",
                                    attr.getWriteMethodName(),
                                    Blob,
                                    attr.getPropertyName());
                        } else {
                            printer
                                .println(
                                    "model.%1$s(shortBlobToBytes((%2$s) entity.getProperty(\"%3$s\")));",
                                    attr.getWriteMethodName(),
                                    ShortBlob,
                                    attr.getPropertyName());
                        }
                        return true;
                    }

                },
                null);
            return null;
        }

        @Override
        public Void visitListType(final ListType collectionType,
                final AttributeMetaDesc attr) throws RuntimeException {
            DataType elementType = collectionType.getElementType();
            Boolean handled =
                elementType.accept(
                    new SimpleDataTypeVisitor<Boolean, Void, RuntimeException>(
                        false) {

                        @Override
                        public Boolean visitCoreReferenceType(
                                CoreReferenceType type, Void p)
                                throws RuntimeException {
                            printer
                                .println(
                                    "model.%1$s(toList(%2$s.class, entity.getProperty(\"%3$s\")));",
                                    attr.getWriteMethodName(),
                                    type.getClassName(),
                                    attr.getPropertyName());
                            return true;
                        }

                        @Override
                        public Boolean visitShortType(ShortType type, Void p)
                                throws RuntimeException {
                            printer
                                .println(
                                    "model.%1$s(longListToShortList(entity.getProperty(\"%2$s\")));",
                                    attr.getWriteMethodName(),
                                    attr.getPropertyName());
                            return true;
                        }

                        @Override
                        public Boolean visitIntegerType(IntegerType type, Void p)
                                throws RuntimeException {
                            printer
                                .println(
                                    "model.%1$s(longListToIntegerList(entity.getProperty(\"%2$s\")));",
                                    attr.getWriteMethodName(),
                                    attr.getPropertyName());
                            return true;
                        }

                        @Override
                        public Boolean visitFloatType(FloatType type, Void p)
                                throws RuntimeException {
                            printer
                                .println(
                                    "model.%1$s(doubleListToFloatList(entity.getProperty(\"%2$s\")));",
                                    attr.getWriteMethodName(),
                                    attr.getPropertyName());
                            return true;
                        }

                    },
                    null);
            return handled ? null : super.visitListType(collectionType, attr);
        }

        @Override
        public Void visitSetType(final SetType collectionType,
                final AttributeMetaDesc attr) throws RuntimeException {
            DataType elementType = collectionType.getElementType();
            Boolean handled =
                elementType.accept(
                    new SimpleDataTypeVisitor<Boolean, Void, RuntimeException>(
                        false) {

                        @Override
                        public Boolean visitCoreReferenceType(
                                CoreReferenceType type, Void p)
                                throws RuntimeException {
                            printer
                                .println(
                                    "model.%1$s(toSet(%2$s.class, entity.getProperty(\"%3$s\")));",
                                    attr.getWriteMethodName(),
                                    type.getClassName(),
                                    attr.getPropertyName());
                            return true;
                        }

                        @Override
                        public Boolean visitShortType(ShortType type, Void p)
                                throws RuntimeException {
                            printer
                                .println(
                                    "model.%1$s(longListToShortSet(entity.getProperty(\"%2$s\")));",
                                    attr.getWriteMethodName(),
                                    attr.getPropertyName());
                            return true;
                        }

                        @Override
                        public Boolean visitIntegerType(IntegerType type, Void p)
                                throws RuntimeException {
                            printer
                                .println(
                                    "model.%1$s(longListToIntegerSet(entity.getProperty(\"%2$s\")));",
                                    attr.getWriteMethodName(),
                                    attr.getPropertyName());
                            return true;
                        }

                        @Override
                        public Boolean visitFloatType(FloatType type, Void p)
                                throws RuntimeException {
                            printer
                                .println(
                                    "model.%1$s(doubleListToFloatSet(entity.getProperty(\"%2$s\")));",
                                    attr.getWriteMethodName(),
                                    attr.getPropertyName());
                            return true;
                        }

                    },
                    null);
            return handled ? null : super.visitSetType(collectionType, attr);
        }

        @Override
        public Void visitSortedSetType(final SortedSetType collectionType,
                final AttributeMetaDesc attr) throws RuntimeException {
            DataType elementType = collectionType.getElementType();
            Boolean handled =
                elementType.accept(
                    new SimpleDataTypeVisitor<Boolean, Void, RuntimeException>(
                        false) {

                        @Override
                        public Boolean visitCoreReferenceType(
                                CoreReferenceType type, Void p)
                                throws RuntimeException {
                            printer
                                .println(
                                    "model.%1$s(toSortedSet(%2$s.class, entity.getProperty(\"%3$s\")));",
                                    attr.getWriteMethodName(),
                                    type.getClassName(),
                                    attr.getPropertyName());
                            return true;
                        }

                        @Override
                        public Boolean visitShortType(ShortType type, Void p)
                                throws RuntimeException {
                            printer
                                .println(
                                    "model.%1$s(longListToShortSortedSet(entity.getProperty(\"%2$s\")));",
                                    attr.getWriteMethodName(),
                                    attr.getPropertyName());
                            return true;
                        }

                        @Override
                        public Boolean visitIntegerType(IntegerType type, Void p)
                                throws RuntimeException {
                            printer
                                .println(
                                    "model.%1$s(longListToIntegerSortedSet(entity.getProperty(\"%2$s\")));",
                                    attr.getWriteMethodName(),
                                    attr.getPropertyName());
                            return true;
                        }

                        @Override
                        public Boolean visitFloatType(FloatType type, Void p)
                                throws RuntimeException {
                            printer
                                .println(
                                    "model.%1$s(doubleListToFloatSortedSet(entity.getProperty(\"%2$s\")));",
                                    attr.getWriteMethodName(),
                                    attr.getPropertyName());
                            return true;
                        }

                    },
                    null);
            return handled ? null : super.visitSortedSetType(
                collectionType,
                attr);
        }
    }

    /**
     * Represents the {@code modelToMethod} method generator.
     * 
     * @author taedium
     * @since 3.0
     * 
     */
    protected class ModelToEntityMethodGenerator extends
            SimpleDataTypeVisitor<Void, AttributeMetaDesc, RuntimeException> {

        /** the printer */
        protected final Printer printer;

        /**
         * Creates a new {@link ModelToEntityMethodGenerator}.
         * 
         * @param printer
         *            the printer
         */
        public ModelToEntityMethodGenerator(Printer printer) {
            this.printer = printer;
        }

        /**
         * Generates the modelToMethod method.
         */
        public void generate() {
            printer.println("@Override");
            printer.println(
                "protected %1$s modelToEntity(%2$s model) {",
                Entity,
                Object);
            printer.indent();
            printer.println("%1$s m = (%1$s) model;", modelMetaDesc
                .getModelClassName());
            printer.println("%1$s entity = null;", Entity);
            printer.println("if (m.getKey() != null) {");
            printer.println("    entity = new %1$s(m.getKey());", Entity);
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
        protected Void defaultAction(DataType type, AttributeMetaDesc p)
                throws RuntimeException {
            if (p.isLob()) {
                printer
                    .println(
                        "entity.setUnindexedProperty(\"%1$s\", serializableToBlob(m.%2$s()));",
                        p.getPropertyName(),
                        p.getReadMethodName());
            } else {
                printer
                    .println(
                        "entity.setUnindexedProperty(\"%1$s\", serializableToShortBlob(m.%2$s()));",
                        p.getPropertyName(),
                        p.getReadMethodName());
            }
            return null;
        }

        @Override
        public Void visitCorePrimitiveType(CorePrimitiveType type,
                AttributeMetaDesc p) throws RuntimeException {
            if (type.isSerialized()) {
                return super.visitCorePrimitiveType(type, p);
            }
            if (p.isUnindexed()) {
                printer.println(
                    "entity.setUnindexedProperty(\"%1$s\", m.%2$s());",
                    p.getPropertyName(),
                    p.getReadMethodName());
            } else {
                printer.println("entity.setProperty(\"%1$s\", m.%2$s());", p
                    .getPropertyName(), p.getReadMethodName());
            }
            return null;
        }

        @Override
        public Void visitCoreReferenceType(CoreReferenceType type,
                AttributeMetaDesc p) throws RuntimeException {
            if (type.isSerialized()) {
                return super.visitCoreReferenceType(type, p);
            }
            if (p.isUnindexed()) {
                printer.println(
                    "entity.setUnindexedProperty(\"%1$s\", m.%2$s());",
                    p.getPropertyName(),
                    p.getReadMethodName());
            } else {
                printer.println("entity.setProperty(\"%1$s\", m.%2$s());", p
                    .getPropertyName(), p.getReadMethodName());
            }
            return null;
        }

        @Override
        public Void visitStringType(StringType type, AttributeMetaDesc p)
                throws RuntimeException {
            if (type.isSerialized()) {
                return super.visitStringType(type, p);
            }
            if (p.isLob()) {
                printer
                    .println(
                        "entity.setUnindexedProperty(\"%1$s\", stringToText(m.%2$s()));",
                        p.getPropertyName(),
                        p.getReadMethodName());
                return null;
            }
            return super.visitStringType(type, p);
        }

        @Override
        public Void visitEnumType(EnumType type, AttributeMetaDesc p)
                throws RuntimeException {
            if (type.isSerialized()) {
                return super.visitCoreReferenceType(type, p);
            }
            if (p.isUnindexed()) {
                printer
                    .println(
                        "entity.setUnindexedProperty(\"%1$s\", enumToString(m.%2$s()));",
                        p.getPropertyName(),
                        p.getReadMethodName());
            } else {
                printer.println(
                    "entity.setProperty(\"%1$s\", enumToString(m.%2$s()));",
                    p.getPropertyName(),
                    p.getReadMethodName());
            }
            return null;
        }

        @Override
        public Void visitTextType(TextType type, AttributeMetaDesc p)
                throws RuntimeException {
            printer.println(
                "entity.setUnindexedProperty(\"%1$s\", m.%2$s());",
                p.getPropertyName(),
                p.getReadMethodName());
            return null;
        }

        @Override
        public Void visitArrayType(ArrayType type, final AttributeMetaDesc attr)
                throws RuntimeException {
            if (type.isSerialized()) {
                return super.visitArrayType(type, attr);
            }
            DataType componentType = type.getComponentType();
            componentType.accept(
                new SimpleDataTypeVisitor<Void, Void, RuntimeException>() {

                    @Override
                    public Void visitPrimitiveByteType(PrimitiveByteType type,
                            Void p) throws RuntimeException {
                        if (attr.isLob()) {
                            printer
                                .println(
                                    "entity.setUnindexedProperty(\"%1$s\", bytesToBlob(m.%2$s()));",
                                    attr.getPropertyName(),
                                    attr.getReadMethodName());
                        } else {
                            printer
                                .println(
                                    "entity.setUnindexedProperty(\"%1$s\", bytesToShortBlob(m.%2$s()));",
                                    attr.getPropertyName(),
                                    attr.getReadMethodName());
                        }
                        return null;
                    }
                },
                null);
            return null;
        }

        @Override
        public Void visitCollectionType(CollectionType type, AttributeMetaDesc p)
                throws RuntimeException {
            if (type.isSerialized()) {
                return super.visitCollectionType(type, p);
            }
            printer.println("entity.setProperty(\"%1$s\", m.%2$s());", p
                .getPropertyName(), p.getReadMethodName());
            return null;
        }
    }

}
