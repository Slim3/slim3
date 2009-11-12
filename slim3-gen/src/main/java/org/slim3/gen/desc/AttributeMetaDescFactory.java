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

import java.util.List;

import org.slim3.gen.AnnotationConstants;
import org.slim3.gen.ClassConstants;
import org.slim3.gen.datastore.ArrayType;
import org.slim3.gen.datastore.CollectionType;
import org.slim3.gen.datastore.CoreReferenceType;
import org.slim3.gen.datastore.DataType;
import org.slim3.gen.datastore.DataTypeFactory;
import org.slim3.gen.datastore.OtherReferenceType;
import org.slim3.gen.message.MessageCode;
import org.slim3.gen.processor.ValidationException;
import org.slim3.gen.util.AnnotationMirrorUtil;
import org.slim3.gen.util.DeclarationUtil;
import org.slim3.gen.util.StringUtil;
import org.slim3.gen.util.TypeUtil;

import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.declaration.AnnotationMirror;
import com.sun.mirror.declaration.FieldDeclaration;
import com.sun.mirror.declaration.MethodDeclaration;
import com.sun.mirror.type.TypeMirror;
import com.sun.mirror.type.PrimitiveType.Kind;

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
     * @param fieldDeclaration
     *            the field declaration
     * @param methodDeclarations
     *            the method declarations
     * @return an attribute meta description
     */
    public AttributeMetaDesc createAttributeMetaDesc(
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

        DataTypeFactory datastoreTypeFactory = creaetDatastoreTypeFactory();
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
            datastoreTypeFactory.createDataType(
                fieldDeclaration,
                fieldDeclaration.getType());
        AttributeMetaDesc attributeMetaDesc =
            new AttributeMetaDesc(
                fieldDeclaration.getSimpleName(),
                dataType,
                propertyName);
        handleField(attributeMetaDesc, fieldDeclaration, attribute);
        handleMethod(attributeMetaDesc, fieldDeclaration, methodDeclarations);
        return attributeMetaDesc;
    }

    /**
     * Handleds a field.
     * 
     * @param attributeMetaDesc
     *            the attribute meta description
     * @param fieldDeclaration
     *            the field declaration
     * @param attribute
     *            the Attribute annotation.
     */
    protected void handleField(AttributeMetaDesc attributeMetaDesc,
            FieldDeclaration fieldDeclaration, AnnotationMirror attribute) {
        if (AnnotationMirrorUtil.getElementValue(
            attribute,
            AnnotationConstants.primaryKey) == Boolean.TRUE) {
            handlePrimaryKey(attributeMetaDesc, fieldDeclaration, attribute);
        }
        if (AnnotationMirrorUtil.getElementValue(
            attribute,
            AnnotationConstants.version) == Boolean.TRUE) {
            handleVersion(attributeMetaDesc, fieldDeclaration, attribute);
        }
        if (AnnotationMirrorUtil.getElementValue(
            attribute,
            AnnotationConstants.lob) == Boolean.TRUE) {
            handleLob(attributeMetaDesc, fieldDeclaration, attribute);
        }
        if (AnnotationMirrorUtil.getElementValue(
            attribute,
            AnnotationConstants.unindexed) == Boolean.TRUE) {
            handleUnindexed(attributeMetaDesc, fieldDeclaration, attribute);
        }
        if (AnnotationMirrorUtil.getElementValue(
            attribute,
            AnnotationConstants.persistent) == Boolean.FALSE) {
            handleNotPersistent(attributeMetaDesc, fieldDeclaration);
        }
        if (attributeMetaDesc.isPersistent() && !attributeMetaDesc.isLob()) {
            DataType dataType = attributeMetaDesc.getDataType();
            if (dataType instanceof OtherReferenceType) {
                throw new ValidationException(
                    MessageCode.SILM3GEN1005,
                    env,
                    fieldDeclaration.getPosition());
            }
            if (dataType instanceof CollectionType
                && CollectionType.class.cast(dataType).getElementType() instanceof OtherReferenceType) {
                throw new ValidationException(
                    MessageCode.SILM3GEN1005,
                    env,
                    fieldDeclaration.getPosition());
            }
            if (dataType instanceof ArrayType) {
                throw new ValidationException(
                    MessageCode.SILM3GEN1005,
                    env,
                    fieldDeclaration.getPosition());
            }
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
     * @param fieldDeclaration
     *            the field declaration
     * @param attribute
     *            the Attribute annotation mirror
     */
    protected void handlePrimaryKey(AttributeMetaDesc attributeMetaDesc,
            FieldDeclaration fieldDeclaration, AnnotationMirror attribute) {
        if (AnnotationMirrorUtil.getElementValue(
            attribute,
            AnnotationConstants.version) == Boolean.TRUE) {
            throw new ValidationException(
                MessageCode.SILM3GEN1021,
                env,
                attribute.getPosition(),
                AnnotationConstants.primaryKey,
                AnnotationConstants.version);
        }
        if (AnnotationMirrorUtil.getElementValue(
            attribute,
            AnnotationConstants.lob) == Boolean.TRUE) {
            throw new ValidationException(
                MessageCode.SILM3GEN1021,
                env,
                fieldDeclaration.getPosition(),
                AnnotationConstants.primaryKey,
                AnnotationConstants.lob);
        }
        if (AnnotationMirrorUtil.getElementValue(
            attribute,
            AnnotationConstants.unindexed) == Boolean.TRUE) {
            throw new ValidationException(
                MessageCode.SILM3GEN1021,
                env,
                fieldDeclaration.getPosition(),
                AnnotationConstants.primaryKey,
                AnnotationConstants.unindexed);
        }
        if (AnnotationMirrorUtil.getElementValue(
            attribute,
            AnnotationConstants.persistent) == Boolean.FALSE) {
            throw new ValidationException(
                MessageCode.SILM3GEN1021,
                env,
                fieldDeclaration.getPosition(),
                AnnotationConstants.primaryKey,
                AnnotationConstants.persistent + " = false");
        }
        if (!ClassConstants.Key.equals(attributeMetaDesc
            .getDataType()
            .getClassName())) {
            throw new ValidationException(
                MessageCode.SILM3GEN1007,
                env,
                fieldDeclaration.getPosition());
        }
        attributeMetaDesc.setPrimaryKey(true);
    }

    /**
     * Handles version.
     * 
     * @param attributeMetaDesc
     *            the attribute meta description
     * @param fieldDeclaration
     *            the field declaration
     * @param attribute
     *            the attribute annotation mirror
     */
    protected void handleVersion(AttributeMetaDesc attributeMetaDesc,
            FieldDeclaration fieldDeclaration, AnnotationMirror attribute) {
        if (AnnotationMirrorUtil.getElementValue(
            attribute,
            AnnotationConstants.lob) == Boolean.TRUE) {
            throw new ValidationException(
                MessageCode.SILM3GEN1021,
                env,
                attribute.getPosition(),
                AnnotationConstants.version,
                AnnotationConstants.lob);
        }
        if (AnnotationMirrorUtil.getElementValue(
            attribute,
            AnnotationConstants.persistent) == Boolean.FALSE) {
            throw new ValidationException(
                MessageCode.SILM3GEN1021,
                env,
                fieldDeclaration.getPosition(),
                AnnotationConstants.version,
                AnnotationConstants.persistent + " = false");
        }
        String className = attributeMetaDesc.getDataType().getClassName();
        if (!ClassConstants.Long.equals(className)
            && !ClassConstants.primitive_long.equals(className)) {
            throw new ValidationException(
                MessageCode.SILM3GEN1008,
                env,
                fieldDeclaration.getPosition());
        }
        attributeMetaDesc.setVersion(true);
    }

    /**
     * Handles a large object.
     * 
     * @param attributeMetaDesc
     *            the attribute meta description
     * @param fieldDeclaration
     *            the field declaration
     * @param attribute
     *            the Attribute annotation mirror
     */
    protected void handleLob(AttributeMetaDesc attributeMetaDesc,
            FieldDeclaration fieldDeclaration, AnnotationMirror attribute) {
        if (AnnotationMirrorUtil.getElementValue(
            attribute,
            AnnotationConstants.persistent) == Boolean.FALSE) {
            throw new ValidationException(
                MessageCode.SILM3GEN1021,
                env,
                attribute.getPosition(),
                AnnotationConstants.lob,
                AnnotationConstants.persistent + " = false");
        }
        if (AnnotationMirrorUtil.getElementValue(
            attribute,
            AnnotationConstants.unindexed) == Boolean.FALSE) {
            throw new ValidationException(
                MessageCode.SILM3GEN1021,
                env,
                fieldDeclaration.getPosition(),
                AnnotationConstants.lob,
                AnnotationConstants.unindexed + " = false");
        }
        DataType dataType = attributeMetaDesc.getDataType();
        if (dataType instanceof CoreReferenceType
            && !ClassConstants.String.equals(dataType.getClassName())) {
            throw new ValidationException(
                MessageCode.SILM3GEN1009,
                env,
                fieldDeclaration.getPosition());
        }
        if (dataType instanceof CollectionType
            && CollectionType.class.cast(dataType).getElementType() instanceof CoreReferenceType) {
            throw new ValidationException(
                MessageCode.SILM3GEN1009,
                env,
                fieldDeclaration.getPosition());
        }
        attributeMetaDesc.setLob(true);
    }

    /**
     * Handles unindexed.
     * 
     * @param attributeMetaDesc
     *            the attribute meta description
     * @param fieldDeclaration
     *            the field declaration
     * @param attribute
     *            the annotation mirror for Attribute
     */
    protected void handleUnindexed(AttributeMetaDesc attributeMetaDesc,
            FieldDeclaration fieldDeclaration, AnnotationMirror attribute) {
        if (AnnotationMirrorUtil.getElementValue(
            attribute,
            AnnotationConstants.persistent) == Boolean.FALSE) {
            throw new ValidationException(
                MessageCode.SILM3GEN1021,
                env,
                attribute.getPosition(),
                AnnotationConstants.unindexed,
                AnnotationConstants.persistent + " = false");
        }
        attributeMetaDesc.setUnindexed(true);
    }

    /**
     * Handles a method.
     * 
     * @param attributeMetaDesc
     *            the attribute meta description
     * @param fieldDeclaration
     *            the field declaration
     * @param methodDeclarations
     *            the method declaration
     */
    protected void handleMethod(AttributeMetaDesc attributeMetaDesc,
            FieldDeclaration fieldDeclaration,
            List<MethodDeclaration> methodDeclarations) {
        for (MethodDeclaration m : methodDeclarations) {
            if (isReadMethod(m, attributeMetaDesc, fieldDeclaration)) {
                attributeMetaDesc.setReadMethodName(m.getSimpleName());
                if (attributeMetaDesc.getWriteMethodName() != null) {
                    break;
                }
            } else if (isWriteMethod(m, attributeMetaDesc, fieldDeclaration)) {
                attributeMetaDesc.setWriteMethodName(m.getSimpleName());
                if (attributeMetaDesc.getReadMethodName() != null) {
                    break;
                }
            }
        }
        if (attributeMetaDesc.isPersistent()) {
            validateGetterAndSetterMethods(attributeMetaDesc, fieldDeclaration);
        }
    }

    /**
     * Validates the getter method and the setter method.
     * 
     * @param attributeMetaDesc
     *            the attribute mete description
     * @param fieldDeclaration
     *            the field declaration
     */
    protected void validateGetterAndSetterMethods(
            AttributeMetaDesc attributeMetaDesc,
            FieldDeclaration fieldDeclaration) {
        if (attributeMetaDesc.getReadMethodName() == null) {
            throw new ValidationException(
                MessageCode.SILM3GEN1011,
                env,
                fieldDeclaration.getPosition(),
                fieldDeclaration.getSimpleName());
        }
        if (attributeMetaDesc.getWriteMethodName() == null) {
            throw new ValidationException(
                MessageCode.SILM3GEN1012,
                env,
                fieldDeclaration.getPosition(),
                fieldDeclaration.getSimpleName());
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
     * Creates a datastore type factory.
     * 
     * @return a datastore type factory
     */
    protected DataTypeFactory creaetDatastoreTypeFactory() {
        return new DataTypeFactory(env);
    }

}
