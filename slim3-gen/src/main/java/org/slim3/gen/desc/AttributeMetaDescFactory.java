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
package org.slim3.gen.desc;

import java.util.Collection;
import java.util.List;

import org.slim3.gen.AnnotationConstants;
import org.slim3.gen.ClassConstants;
import org.slim3.gen.Constants;
import org.slim3.gen.datastore.ArrayType;
import org.slim3.gen.datastore.CollectionType;
import org.slim3.gen.datastore.CoreReferenceType;
import org.slim3.gen.datastore.DataType;
import org.slim3.gen.datastore.DataTypeFactory;
import org.slim3.gen.datastore.InverseModelListRefType;
import org.slim3.gen.datastore.InverseModelRefType;
import org.slim3.gen.datastore.ModelRefType;
import org.slim3.gen.datastore.OtherReferenceType;
import org.slim3.gen.message.MessageCode;
import org.slim3.gen.processor.ValidationException;
import org.slim3.gen.util.AnnotationMirrorUtil;
import org.slim3.gen.util.DeclarationUtil;
import org.slim3.gen.util.StringUtil;
import org.slim3.gen.util.TypeUtil;

import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.declaration.AnnotationMirror;
import com.sun.mirror.declaration.ClassDeclaration;
import com.sun.mirror.declaration.FieldDeclaration;
import com.sun.mirror.declaration.MethodDeclaration;
import com.sun.mirror.declaration.TypeDeclaration;
import com.sun.mirror.type.ClassType;
import com.sun.mirror.type.DeclaredType;
import com.sun.mirror.type.TypeMirror;
import com.sun.mirror.type.PrimitiveType.Kind;
import com.sun.mirror.util.SimpleTypeVisitor;

/**
 * Represents an attribute meta description factory.
 * 
 * @author taedium
 * @since 3.0
 * 
 */
public class AttributeMetaDescFactory {

    /** the environment */
    protected final AnnotationProcessorEnvironment env;

    /**
     * Creates a new {@link AttributeMetaDescFactory}.
     * 
     * @param env
     *            the processing environment
     */
    public AttributeMetaDescFactory(AnnotationProcessorEnvironment env) {
        if (env == null) {
            throw new NullPointerException("The env parameter is null.");
        }
        this.env = env;
    }

    /**
     * Creates a new {@link AttributeMetaDesc}
     * 
     * @param classDeclaration
     *            the model class declaration
     * @param fieldDeclaration
     *            the field declaration
     * @param methodDeclarations
     *            the method declarations
     * @return an attribute meta description
     */
    public AttributeMetaDesc createAttributeMetaDesc(
            ClassDeclaration classDeclaration,
            FieldDeclaration fieldDeclaration,
            List<MethodDeclaration> methodDeclarations) {
        if (fieldDeclaration == null) {
            throw new NullPointerException(
                "The fieldDeclaration parameter is null.");
        }
        if (methodDeclarations == null) {
            throw new NullPointerException(
                "The methodDeclarations parameter is null.");
        }

        DataTypeFactory dataTypeFactory = creaetDataTypeFactory();
        String propertyName = fieldDeclaration.getSimpleName();
        AnnotationMirror attribute =
            DeclarationUtil.getAnnotationMirror(
                env,
                fieldDeclaration,
                AnnotationConstants.Attribute);
        if (attribute != null) {
            String value =
                AnnotationMirrorUtil.getElementValue(
                    attribute,
                    AnnotationConstants.name);
            if (value != null && value.length() > 0) {
                propertyName = value;
            }
        }
        DataType dataType =
            dataTypeFactory.createDataType(fieldDeclaration, fieldDeclaration
                .getType());
        AttributeMetaDesc attributeMetaDesc =
            new AttributeMetaDesc(
                fieldDeclaration.getSimpleName(),
                dataType,
                propertyName);
        handleField(
            attributeMetaDesc,
            classDeclaration,
            fieldDeclaration,
            attribute);
        handleMethod(
            attributeMetaDesc,
            classDeclaration,
            fieldDeclaration,
            methodDeclarations);
        return attributeMetaDesc;
    }

    /**
     * Handleds a field.
     * 
     * @param attributeMetaDesc
     *            the attribute meta description
     * @param classDeclaration
     *            the model class declaration
     * @param fieldDeclaration
     *            the field declaration
     * @param attribute
     *            the Attribute annotation.
     */
    protected void handleField(AttributeMetaDesc attributeMetaDesc,
            ClassDeclaration classDeclaration,
            FieldDeclaration fieldDeclaration, AnnotationMirror attribute) {
        if (AnnotationMirrorUtil.getElementValue(
            attribute,
            AnnotationConstants.primaryKey) == Boolean.TRUE) {
            handlePrimaryKey(
                attributeMetaDesc,
                classDeclaration,
                fieldDeclaration,
                attribute);
        }
        if (AnnotationMirrorUtil.getElementValue(
            attribute,
            AnnotationConstants.version) == Boolean.TRUE) {
            handleVersion(
                attributeMetaDesc,
                classDeclaration,
                fieldDeclaration,
                attribute);
        }
        if (AnnotationMirrorUtil.getElementValue(
            attribute,
            AnnotationConstants.lob) == Boolean.TRUE) {
            handleLob(
                attributeMetaDesc,
                classDeclaration,
                fieldDeclaration,
                attribute);
        }
        if (AnnotationMirrorUtil.getElementValue(
            attribute,
            AnnotationConstants.unindexed) == Boolean.TRUE) {
            handleUnindexed(
                attributeMetaDesc,
                classDeclaration,
                fieldDeclaration,
                attribute);
        }
        if (AnnotationMirrorUtil.getElementValue(
            attribute,
            AnnotationConstants.persistent) == Boolean.FALSE) {
            handleNotPersistent(attributeMetaDesc, fieldDeclaration);
        }
        if (attributeMetaDesc.isPersistent()) {
            DataType dataType = attributeMetaDesc.getDataType();
            if (dataType instanceof ModelRefType) {
                if (classDeclaration
                    .equals(fieldDeclaration.getDeclaringType())) {
                    throw new ValidationException(
                        MessageCode.SILM3GEN1043,
                        env,
                        fieldDeclaration.getPosition());
                }
                throw new ValidationException(
                    MessageCode.SILM3GEN1044,
                    env,
                    classDeclaration.getPosition(),
                    fieldDeclaration.getSimpleName(),
                    fieldDeclaration.getDeclaringType().getQualifiedName());
            }
            if (dataType instanceof InverseModelRefType) {
                if (classDeclaration
                    .equals(fieldDeclaration.getDeclaringType())) {
                    throw new ValidationException(
                        MessageCode.SILM3GEN1035,
                        env,
                        fieldDeclaration.getPosition());
                }
                throw new ValidationException(
                    MessageCode.SILM3GEN1036,
                    env,
                    classDeclaration.getPosition(),
                    fieldDeclaration.getSimpleName(),
                    fieldDeclaration.getDeclaringType().getQualifiedName());
            }
            if (dataType instanceof InverseModelListRefType) {
                if (classDeclaration
                    .equals(fieldDeclaration.getDeclaringType())) {
                    throw new ValidationException(
                        MessageCode.SILM3GEN1037,
                        env,
                        fieldDeclaration.getPosition());
                }
                throw new ValidationException(
                    MessageCode.SILM3GEN1038,
                    env,
                    classDeclaration.getPosition(),
                    fieldDeclaration.getSimpleName(),
                    fieldDeclaration.getDeclaringType().getQualifiedName());
            }
            if (!attributeMetaDesc.isLob()) {
                if (dataType instanceof OtherReferenceType) {
                    throwExceptionForNonCoreType(
                        classDeclaration,
                        fieldDeclaration,
                        attribute);
                }
                if (dataType instanceof CollectionType
                    && CollectionType.class.cast(dataType).getElementType() instanceof OtherReferenceType) {
                    throwExceptionForNonCoreType(
                        classDeclaration,
                        fieldDeclaration,
                        attribute);
                }
                if (dataType instanceof ArrayType) {
                    throwExceptionForNonCoreType(
                        classDeclaration,
                        fieldDeclaration,
                        attribute);
                }
            }
        }
        if (attributeMetaDesc.getDataType() instanceof ModelRefType) {
            validateModelRefTypeArgument(
                attributeMetaDesc,
                classDeclaration,
                fieldDeclaration);
        }
    }

    /**
     * Handles not persistent.
     * 
     * @param attributeMetaDesc
     *            the attribute meta description
     * @param fieldDeclaration
     *            the field declaration
     */
    protected void handleNotPersistent(AttributeMetaDesc attributeMetaDesc,
            FieldDeclaration fieldDeclaration) {
        attributeMetaDesc.setPersistent(false);
    }

    /**
     * Handles primary key.
     * 
     * @param attributeMetaDesc
     *            the attribute meta description
     * @param classDeclaration
     *            the model class declaration
     * @param fieldDeclaration
     *            the field declaration
     * @param attribute
     *            the Attribute annotation mirror
     */
    protected void handlePrimaryKey(AttributeMetaDesc attributeMetaDesc,
            ClassDeclaration classDeclaration,
            FieldDeclaration fieldDeclaration, AnnotationMirror attribute) {
        if (AnnotationMirrorUtil.getElementValue(
            attribute,
            AnnotationConstants.version) == Boolean.TRUE) {
            throwExceptionForConflictedElements(
                classDeclaration,
                fieldDeclaration,
                attribute,
                AnnotationConstants.primaryKey,
                AnnotationConstants.version);
        }
        if (AnnotationMirrorUtil.getElementValue(
            attribute,
            AnnotationConstants.lob) == Boolean.TRUE) {
            throwExceptionForConflictedElements(
                classDeclaration,
                fieldDeclaration,
                attribute,
                AnnotationConstants.primaryKey,
                AnnotationConstants.lob);
        }
        if (AnnotationMirrorUtil.getElementValue(
            attribute,
            AnnotationConstants.unindexed) == Boolean.TRUE) {
            throwExceptionForConflictedElements(
                classDeclaration,
                fieldDeclaration,
                attribute,
                AnnotationConstants.primaryKey,
                AnnotationConstants.unindexed);
        }
        if (AnnotationMirrorUtil.getElementValue(
            attribute,
            AnnotationConstants.persistent) == Boolean.FALSE) {
            throwExceptionForConflictedElements(
                classDeclaration,
                fieldDeclaration,
                attribute,
                AnnotationConstants.primaryKey,
                AnnotationConstants.persistent + " = false");
        }
        if (!ClassConstants.Key.equals(attributeMetaDesc
            .getDataType()
            .getClassName())) {
            if (classDeclaration.equals(fieldDeclaration.getDeclaringType())) {
                throw new ValidationException(
                    MessageCode.SILM3GEN1007,
                    env,
                    fieldDeclaration.getPosition());
            }
            throw new ValidationException(
                MessageCode.SILM3GEN1029,
                env,
                classDeclaration.getPosition(),
                fieldDeclaration.getSimpleName(),
                fieldDeclaration.getDeclaringType().getQualifiedName());
        }
        attributeMetaDesc.setPrimaryKey(true);
    }

    /**
     * Handles version.
     * 
     * @param attributeMetaDesc
     *            the attribute meta description
     * @param classDeclaration
     *            the model class declaration
     * @param fieldDeclaration
     *            the field declaration
     * @param attribute
     *            the attribute annotation mirror
     */
    protected void handleVersion(AttributeMetaDesc attributeMetaDesc,
            ClassDeclaration classDeclaration,
            FieldDeclaration fieldDeclaration, AnnotationMirror attribute) {
        if (AnnotationMirrorUtil.getElementValue(
            attribute,
            AnnotationConstants.lob) == Boolean.TRUE) {
            throwExceptionForConflictedElements(
                classDeclaration,
                fieldDeclaration,
                attribute,
                AnnotationConstants.version,
                AnnotationConstants.lob);
        }
        if (AnnotationMirrorUtil.getElementValue(
            attribute,
            AnnotationConstants.persistent) == Boolean.FALSE) {
            throwExceptionForConflictedElements(
                classDeclaration,
                fieldDeclaration,
                attribute,
                AnnotationConstants.version,
                AnnotationConstants.persistent + " = false");
        }
        String className = attributeMetaDesc.getDataType().getClassName();
        if (!ClassConstants.Long.equals(className)
            && !ClassConstants.primitive_long.equals(className)) {
            if (classDeclaration.equals(fieldDeclaration.getDeclaringType())) {
                throw new ValidationException(
                    MessageCode.SILM3GEN1008,
                    env,
                    fieldDeclaration.getPosition());
            }
            throw new ValidationException(
                MessageCode.SILM3GEN1030,
                env,
                classDeclaration.getPosition(),
                fieldDeclaration.getSimpleName(),
                fieldDeclaration.getDeclaringType().getQualifiedName());
        }
        attributeMetaDesc.setVersion(true);
    }

    /**
     * Handles a large object.
     * 
     * @param attributeMetaDesc
     *            the attribute meta description
     * @param classDeclaration
     *            the model class declaration
     * @param fieldDeclaration
     *            the field declaration
     * @param attribute
     *            the Attribute annotation mirror
     */
    protected void handleLob(AttributeMetaDesc attributeMetaDesc,
            ClassDeclaration classDeclaration,
            FieldDeclaration fieldDeclaration, AnnotationMirror attribute) {
        if (AnnotationMirrorUtil.getElementValue(
            attribute,
            AnnotationConstants.persistent) == Boolean.FALSE) {
            throwExceptionForConflictedElements(
                classDeclaration,
                fieldDeclaration,
                attribute,
                AnnotationConstants.lob,
                AnnotationConstants.persistent + " = false");
        }
        if (AnnotationMirrorUtil.getElementValue(
            attribute,
            AnnotationConstants.unindexed) == Boolean.FALSE) {
            throwExceptionForConflictedElements(
                classDeclaration,
                fieldDeclaration,
                attribute,
                AnnotationConstants.lob,
                AnnotationConstants.unindexed + " = false");
        }
        DataType dataType = attributeMetaDesc.getDataType();
        if (dataType instanceof CoreReferenceType
            && !ClassConstants.String.equals(dataType.getClassName())) {
            throwExceptionForLobUnsupportedType(
                classDeclaration,
                fieldDeclaration,
                attribute);
        }
        if (dataType instanceof ModelRefType) {
            throwExceptionForLobUnsupportedType(
                classDeclaration,
                fieldDeclaration,
                attribute);
        }
        if (dataType instanceof CollectionType
            && CollectionType.class.cast(dataType).getElementType() instanceof CoreReferenceType) {
            throwExceptionForLobUnsupportedType(
                classDeclaration,
                fieldDeclaration,
                attribute);
        }
        attributeMetaDesc.setLob(true);
    }

    /**
     * Handles unindexed.
     * 
     * @param attributeMetaDesc
     *            the attribute meta description
     * @param classDeclaration
     *            the model class declaration
     * @param fieldDeclaration
     *            the field declaration
     * @param attribute
     *            the annotation mirror for Attribute
     */
    protected void handleUnindexed(AttributeMetaDesc attributeMetaDesc,
            ClassDeclaration classDeclaration,
            FieldDeclaration fieldDeclaration, AnnotationMirror attribute) {
        if (AnnotationMirrorUtil.getElementValue(
            attribute,
            AnnotationConstants.persistent) == Boolean.FALSE) {
            throwExceptionForConflictedElements(
                classDeclaration,
                fieldDeclaration,
                attribute,
                AnnotationConstants.unindexed,
                AnnotationConstants.persistent + " = false");
        }
        attributeMetaDesc.setUnindexed(true);
    }

    /**
     * Validates ModelRef type argument.
     * 
     * @param attributeMetaDesc
     *            the attribute meta description
     * @param classDeclaration
     *            the model class declaration
     * @param fieldDeclaration
     *            the field declaration
     */
    protected void validateModelRefTypeArgument(
            AttributeMetaDesc attributeMetaDesc,
            final ClassDeclaration classDeclaration,
            final FieldDeclaration fieldDeclaration) {
        fieldDeclaration.getType().accept(new SimpleTypeVisitor() {
            @Override
            public void visitClassType(ClassType classType) {
                if (ClassConstants.ModelRef.equals(classType
                    .getDeclaration()
                    .getQualifiedName())) {
                    Collection<TypeMirror> typeArgs =
                        classType.getActualTypeArguments();
                    if (typeArgs.isEmpty()) {
                        if (classDeclaration.equals(fieldDeclaration
                            .getDeclaringType())) {
                            throw new ValidationException(
                                MessageCode.SILM3GEN1033,
                                env,
                                fieldDeclaration.getPosition());
                        }
                        throw new ValidationException(
                            MessageCode.SILM3GEN1034,
                            env,
                            classDeclaration.getPosition(),
                            fieldDeclaration.getSimpleName(),
                            fieldDeclaration
                                .getDeclaringType()
                                .getQualifiedName());
                    }
                    DeclaredType declaredType =
                        TypeUtil.toDeclaredType(typeArgs.iterator().next());
                    if (declaredType == null) {
                        throwExceptionForModelRefTypeArgument(
                            classDeclaration,
                            fieldDeclaration);
                    }
                    TypeDeclaration typeDeclaration =
                        declaredType.getDeclaration();
                    if (typeDeclaration == null) {
                        throwExceptionForModelRefTypeArgument(
                            classDeclaration,
                            fieldDeclaration);
                    }
                    AnnotationMirror annotationMirror =
                        DeclarationUtil.getAnnotationMirror(
                            env,
                            typeDeclaration,
                            AnnotationConstants.Model);
                    if (annotationMirror == null) {
                        throwExceptionForModelRefTypeArgument(
                            classDeclaration,
                            fieldDeclaration);
                    }
                }
                ClassType superclassType = classType.getSuperclass();
                if (superclassType != null) {
                    superclassType.accept(this);
                }
            }

        });
    }

    /**
     * Handles a method.
     * 
     * @param attributeMetaDesc
     *            the attribute meta description
     * @param classDeclaration
     *            the class declaration
     * @param fieldDeclaration
     *            the field declaration
     * @param methodDeclarations
     *            the method declaration
     */
    protected void handleMethod(AttributeMetaDesc attributeMetaDesc,
            ClassDeclaration classDeclaration,
            FieldDeclaration fieldDeclaration,
            List<MethodDeclaration> methodDeclarations) {
        MethodDeclaration readMethodDeclaration = null;
        MethodDeclaration wirteMethodDeclaration = null;
        for (MethodDeclaration m : methodDeclarations) {
            if (isReadMethod(m, attributeMetaDesc, fieldDeclaration)) {
                readMethodDeclaration = m;
                attributeMetaDesc.setReadMethodName(m.getSimpleName());
                if (attributeMetaDesc.getWriteMethodName() != null) {
                    break;
                }
            } else if (isWriteMethod(m, attributeMetaDesc, fieldDeclaration)) {
                wirteMethodDeclaration = m;
                attributeMetaDesc.setWriteMethodName(m.getSimpleName());
                if (attributeMetaDesc.getReadMethodName() != null) {
                    break;
                }
            }
        }
        if (attributeMetaDesc.isPersistent()) {
            validateReadAndWriteMethods(
                attributeMetaDesc,
                classDeclaration,
                fieldDeclaration,
                readMethodDeclaration,
                wirteMethodDeclaration);
        }
        DataType dataType = attributeMetaDesc.getDataType();
        if (dataType instanceof ModelRefType) {
            validateReadMethodOnly(
                attributeMetaDesc,
                (ModelRefType) dataType,
                classDeclaration,
                fieldDeclaration,
                readMethodDeclaration,
                wirteMethodDeclaration);
        } else if (dataType instanceof InverseModelRefType) {
            validateReadMethodOnly(
                attributeMetaDesc,
                (InverseModelRefType) dataType,
                classDeclaration,
                fieldDeclaration,
                readMethodDeclaration,
                wirteMethodDeclaration);
        }
    }

    /**
     * Validates the read method and the write method.
     * 
     * @param attributeMetaDesc
     *            the attribute mete description
     * @param classDeclaration
     *            the model declaration
     * @param fieldDeclaration
     *            the field declaration
     * @param readMethodDeclaration
     *            the read method declaration
     * @param writeMethodDeclaration
     *            the write method declaration
     */
    protected void validateReadAndWriteMethods(
            AttributeMetaDesc attributeMetaDesc,
            ClassDeclaration classDeclaration,
            FieldDeclaration fieldDeclaration,
            MethodDeclaration readMethodDeclaration,
            MethodDeclaration writeMethodDeclaration) {
        if (readMethodDeclaration == null) {
            if (classDeclaration.equals(fieldDeclaration.getDeclaringType())) {
                throw new ValidationException(
                    MessageCode.SILM3GEN1011,
                    env,
                    fieldDeclaration.getPosition());
            }
            throw new ValidationException(
                MessageCode.SILM3GEN1024,
                env,
                classDeclaration.getPosition(),
                fieldDeclaration.getSimpleName(),
                fieldDeclaration.getDeclaringType().getQualifiedName());
        }
        if (writeMethodDeclaration == null) {
            if (classDeclaration.equals(fieldDeclaration.getDeclaringType())) {
                throw new ValidationException(
                    MessageCode.SILM3GEN1012,
                    env,
                    fieldDeclaration.getPosition());
            }
            throw new ValidationException(
                MessageCode.SILM3GEN1025,
                env,
                classDeclaration.getPosition(),
                fieldDeclaration.getSimpleName(),
                fieldDeclaration.getDeclaringType().getQualifiedName());
        }
    }

    /**
     * Validates that the read method is inexistent and the write method is
     * existent.
     * 
     * @param attributeMetaDesc
     *            the attribute mete description
     * @param modelRefType
     *            the model reference type
     * @param classDeclaration
     *            the model declaration
     * @param fieldDeclaration
     *            the field declaration
     * @param readMethodDeclaration
     *            the read method declaration
     * @param writeMethodDeclaration
     *            the write method declaration
     */
    protected void validateReadMethodOnly(AttributeMetaDesc attributeMetaDesc,
            ModelRefType modelRefType, ClassDeclaration classDeclaration,
            FieldDeclaration fieldDeclaration,
            MethodDeclaration readMethodDeclaration,
            MethodDeclaration writeMethodDeclaration) {
        if (readMethodDeclaration == null) {
            if (classDeclaration.equals(fieldDeclaration.getDeclaringType())) {
                throw new ValidationException(
                    MessageCode.SILM3GEN1011,
                    env,
                    fieldDeclaration.getPosition());
            }
            throw new ValidationException(
                MessageCode.SILM3GEN1024,
                env,
                classDeclaration.getPosition(),
                fieldDeclaration.getSimpleName(),
                fieldDeclaration.getDeclaringType().getQualifiedName());
        }
        if (writeMethodDeclaration != null) {
            String fieldDefinition =
                String.format(
                    "%1$s %2$s = new %1$s(%3$s%4$s.get());",
                    fieldDeclaration.getType(),
                    fieldDeclaration.getSimpleName(),
                    modelRefType.getReferenceModelClassName(),
                    Constants.META_SUFFIX);
            if (classDeclaration.equals(fieldDeclaration.getDeclaringType())) {
                throw new ValidationException(
                    MessageCode.SILM3GEN1041,
                    env,
                    fieldDeclaration.getPosition(),
                    fieldDefinition);
            }
            throw new ValidationException(
                MessageCode.SILM3GEN1042,
                env,
                classDeclaration.getPosition(),
                fieldDeclaration.getSimpleName(),
                fieldDeclaration.getDeclaringType().getQualifiedName(),
                fieldDefinition);
        }
    }

    /**
     * Validates that the read method is inexistent and the write method is
     * existent.
     * 
     * @param attributeMetaDesc
     *            the attribute mete description
     * @param inverseModelRefType
     *            the inverse model ref type
     * @param classDeclaration
     *            the model declaration
     * @param fieldDeclaration
     *            the field declaration
     * @param readMethodDeclaration
     *            the read method declaration
     * @param writeMethodDeclaration
     *            the write method declaration
     */
    protected void validateReadMethodOnly(AttributeMetaDesc attributeMetaDesc,
            InverseModelRefType inverseModelRefType,
            ClassDeclaration classDeclaration,
            FieldDeclaration fieldDeclaration,
            MethodDeclaration readMethodDeclaration,
            MethodDeclaration writeMethodDeclaration) {
        if (readMethodDeclaration == null) {
            if (classDeclaration.equals(fieldDeclaration.getDeclaringType())) {
                throw new ValidationException(
                    MessageCode.SILM3GEN1011,
                    env,
                    fieldDeclaration.getPosition());
            }
            throw new ValidationException(
                MessageCode.SILM3GEN1024,
                env,
                classDeclaration.getPosition(),
                fieldDeclaration.getSimpleName(),
                fieldDeclaration.getDeclaringType().getQualifiedName());
        }
        if (writeMethodDeclaration != null) {
            String fieldDefinition =
                String.format(
                    "%1$s %2$s = new %1$s(%3$s%4$s.get().xxx, this);",
                    fieldDeclaration.getType(),
                    fieldDeclaration.getSimpleName(),
                    inverseModelRefType.getReferenceModelClassName(),
                    Constants.META_SUFFIX);
            if (classDeclaration.equals(fieldDeclaration.getDeclaringType())) {
                throw new ValidationException(
                    MessageCode.SILM3GEN1039,
                    env,
                    writeMethodDeclaration.getPosition(),
                    fieldDeclaration.getSimpleName(),
                    fieldDefinition);
            }
            throw new ValidationException(
                MessageCode.SILM3GEN1040,
                env,
                classDeclaration.getPosition(),
                fieldDeclaration.getSimpleName(),
                fieldDeclaration.getDeclaringType().getQualifiedName(),
                fieldDefinition);
        }
    }

    /**
     * Return {@code true} if method is getter method.
     * 
     * @param m
     *            the method declaration
     * @param attributeMetaDesc
     *            the attribute meta description
     * @param fieldDeclaration
     *            the field declaration
     * @return {@code true} if method is getter method
     */
    protected boolean isReadMethod(MethodDeclaration m,
            AttributeMetaDesc attributeMetaDesc,
            FieldDeclaration fieldDeclaration) {
        String propertyName = null;
        if (m.getSimpleName().startsWith("get")) {
            propertyName =
                StringUtil.decapitalize(m.getSimpleName().substring(3));
        } else if (m.getSimpleName().startsWith("is")) {
            if (!TypeUtil.isPrimitive(m.getReturnType(), Kind.BOOLEAN)) {
                return false;
            }
            propertyName =
                StringUtil.decapitalize(m.getSimpleName().substring(2));
        } else {
            return false;
        }
        if (!propertyName.equals(fieldDeclaration.getSimpleName())
            || TypeUtil.isVoid(m.getReturnType())
            || m.getParameters().size() != 0) {
            return false;
        }
        return m.getReturnType().equals(fieldDeclaration.getType());
    }

    /**
     * Return {@code true} if method is setter method.
     * 
     * @param m
     *            the method declaration
     * @param attributeMetaDesc
     *            the attribute meta description
     * @param fieldDeclaration
     *            the field type
     * @return {@code true} if method is setter method
     */
    protected boolean isWriteMethod(MethodDeclaration m,
            AttributeMetaDesc attributeMetaDesc,
            FieldDeclaration fieldDeclaration) {
        if (!m.getSimpleName().startsWith("set")) {
            return false;
        }
        String propertyName =
            StringUtil.decapitalize(m.getSimpleName().substring(3));
        if (!propertyName.equals(fieldDeclaration.getSimpleName())
            || !TypeUtil.isVoid(m.getReturnType())
            || m.getParameters().size() != 1) {
            return false;
        }
        TypeMirror parameterTypeMirror =
            m.getParameters().iterator().next().getType();
        return parameterTypeMirror.equals(fieldDeclaration.getType());
    }

    /**
     * Creates a data type factory.
     * 
     * @return a data type factory
     */
    protected DataTypeFactory creaetDataTypeFactory() {
        return new DataTypeFactory(env);
    }

    /**
     * Throws {@link ValidationException} for non core type.
     * 
     * @param classDeclaration
     *            the model class declaration
     * @param fieldDeclaration
     *            the field declaration
     * @param attribute
     *            the annotation mirror for Attribute
     */
    protected void throwExceptionForNonCoreType(
            ClassDeclaration classDeclaration,
            FieldDeclaration fieldDeclaration, AnnotationMirror attribute) {
        if (classDeclaration.equals(fieldDeclaration.getDeclaringType())) {
            throw new ValidationException(
                MessageCode.SILM3GEN1005,
                env,
                fieldDeclaration.getPosition());
        }
        throw new ValidationException(
            MessageCode.SILM3GEN1026,
            env,
            classDeclaration.getPosition(),
            fieldDeclaration.getSimpleName(),
            fieldDeclaration.getDeclaringType().getQualifiedName());
    }

    /**
     * Throws {@link ValidationException} for conflicted annotation elements.
     * 
     * @param classDeclaration
     *            the model class declaration
     * @param fieldDeclaration
     *            the field declaration
     * @param attribute
     *            the annotation mirror for Attribute
     * @param element1
     *            conflicted element
     * @param element2
     *            conflicted element
     */
    protected void throwExceptionForConflictedElements(
            ClassDeclaration classDeclaration,
            FieldDeclaration fieldDeclaration, AnnotationMirror attribute,
            String element1, String element2) {
        if (classDeclaration.equals(fieldDeclaration.getDeclaringType())) {
            throw new ValidationException(
                MessageCode.SILM3GEN1021,
                env,
                attribute.getPosition(),
                element1,
                element2);
        }
        throw new ValidationException(
            MessageCode.SILM3GEN1027,
            env,
            classDeclaration.getPosition(),
            element1,
            element2,
            fieldDeclaration.getSimpleName(),
            fieldDeclaration.getDeclaringType().getQualifiedName());
    }

    /**
     * Throws {@link ValidationException} for Lob unsupported type.
     * 
     * @param classDeclaration
     *            the model class declaration
     * @param fieldDeclaration
     *            the field declaration
     * @param attribute
     *            the annotation mirror for Attribute
     */
    protected void throwExceptionForLobUnsupportedType(
            ClassDeclaration classDeclaration,
            FieldDeclaration fieldDeclaration, AnnotationMirror attribute) {
        if (classDeclaration.equals(fieldDeclaration.getDeclaringType())) {
            throw new ValidationException(
                MessageCode.SILM3GEN1009,
                env,
                fieldDeclaration.getPosition());
        }
        throw new ValidationException(
            MessageCode.SILM3GEN1028,
            env,
            classDeclaration.getPosition(),
            fieldDeclaration.getSimpleName(),
            fieldDeclaration.getDeclaringType().getQualifiedName());
    }

    /**
     * hrows {@link ValidationException} for ModelRef type argument.
     * 
     * @param classDeclaration
     *            the model class declaration
     * @param fieldDeclaration
     *            the field declaration
     */
    protected void throwExceptionForModelRefTypeArgument(
            ClassDeclaration classDeclaration, FieldDeclaration fieldDeclaration) {
        if (classDeclaration.equals(fieldDeclaration.getDeclaringType())) {
            throw new ValidationException(
                MessageCode.SILM3GEN1031,
                env,
                fieldDeclaration.getPosition());
        }
        throw new ValidationException(
            MessageCode.SILM3GEN1032,
            env,
            classDeclaration.getPosition(),
            fieldDeclaration.getSimpleName(),
            fieldDeclaration.getDeclaringType().getQualifiedName());
    }
}
