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
import org.slim3.gen.datastore.DataType;
import org.slim3.gen.datastore.DataTypeFactory;
import org.slim3.gen.message.MessageCode;
import org.slim3.gen.processor.ValidationException;
import org.slim3.gen.util.AnnotationMirrorUtil;
import org.slim3.gen.util.DeclarationUtil;
import org.slim3.gen.util.StringUtil;
import org.slim3.gen.util.TypeUtil;

import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.declaration.AnnotationMirror;
import com.sun.mirror.declaration.Declaration;
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
        handleField(attributeMetaDesc, fieldDeclaration);
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
     */
    protected void handleField(AttributeMetaDesc attributeMetaDesc,
            FieldDeclaration fieldDeclaration) {
        if (isAnnotated(AnnotationConstants.Impermanent, fieldDeclaration)) {
            handleImpermanent(attributeMetaDesc, fieldDeclaration);
        } else if (isAnnotated(AnnotationConstants.PrimaryKey, fieldDeclaration)) {
            handlePrimaryKey(attributeMetaDesc, fieldDeclaration);
        } else if (isAnnotated(AnnotationConstants.Version, fieldDeclaration)) {
            handleVersion(attributeMetaDesc, fieldDeclaration);
        } else if (isAnnotated(AnnotationConstants.Text, fieldDeclaration)) {
            handleText(attributeMetaDesc, fieldDeclaration);
        } else if (isAnnotated(AnnotationConstants.Blob, fieldDeclaration)) {
            handleBlob(attributeMetaDesc, fieldDeclaration);
        } else if (isAnnotated(AnnotationConstants.Unindexed, fieldDeclaration)) {
            handleUnindexed(attributeMetaDesc, fieldDeclaration);
        }
    }

    /**
     * Handles impermanent.
     * 
     * @param attributeMetaDesc
     *            the attribute meta description
     * @param fieldDeclaration
     *            the field declaration
     */
    protected void handleImpermanent(AttributeMetaDesc attributeMetaDesc,
            FieldDeclaration fieldDeclaration) {
        validateAnnotationConsistency(
            fieldDeclaration,
            AnnotationConstants.Impermanent,
            AnnotationConstants.PrimaryKey,
            AnnotationConstants.Version,
            AnnotationConstants.Text,
            AnnotationConstants.Blob);
        attributeMetaDesc.setImpermanent(true);
    }

    /**
     * Handles primary key.
     * 
     * @param attributeMetaDesc
     *            the attribute meta description
     * @param fieldDeclaration
     *            the field declaration
     */
    protected void handlePrimaryKey(AttributeMetaDesc attributeMetaDesc,
            FieldDeclaration fieldDeclaration) {
        validateAnnotationConsistency(
            fieldDeclaration,
            AnnotationConstants.PrimaryKey,
            AnnotationConstants.Version,
            AnnotationConstants.Text,
            AnnotationConstants.Blob,
            AnnotationConstants.Unindexed);
        if (!ClassConstants.Key.equals(attributeMetaDesc
            .getDataType()
            .getClassName())) {
            throw new ValidationException(
                MessageCode.SILM3GEN1007,
                env,
                fieldDeclaration);
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
     */
    protected void handleVersion(AttributeMetaDesc attributeMetaDesc,
            FieldDeclaration fieldDeclaration) {
        validateAnnotationConsistency(
            fieldDeclaration,
            AnnotationConstants.Version,
            AnnotationConstants.Text,
            AnnotationConstants.Blob,
            AnnotationConstants.Unindexed);
        String className = attributeMetaDesc.getDataType().getClassName();
        if (!ClassConstants.Long.equals(className)
            && !ClassConstants.primitive_long.equals(className)) {
            throw new ValidationException(
                MessageCode.SILM3GEN1008,
                env,
                fieldDeclaration);
        }
        attributeMetaDesc.setVersion(true);
    }

    /**
     * Handles text.
     * 
     * @param attributeMetaDesc
     *            the attribute meta description
     * @param fieldDeclaration
     *            the field declaration
     */
    protected void handleText(AttributeMetaDesc attributeMetaDesc,
            FieldDeclaration fieldDeclaration) {
        validateAnnotationConsistency(
            fieldDeclaration,
            AnnotationConstants.Text,
            AnnotationConstants.Blob,
            AnnotationConstants.Unindexed);
        if (!ClassConstants.String.equals(attributeMetaDesc
            .getDataType()
            .getClassName())) {
            throw new ValidationException(
                MessageCode.SILM3GEN1009,
                env,
                fieldDeclaration);
        }
        attributeMetaDesc.setText(true);
    }

    /**
     * Handles blob.
     * 
     * @param attributeMetaDesc
     *            the attribute meta description
     * @param fieldDeclaration
     *            the field declaration
     */
    protected void handleBlob(AttributeMetaDesc attributeMetaDesc,
            FieldDeclaration fieldDeclaration) {
        validateAnnotationConsistency(
            fieldDeclaration,
            AnnotationConstants.Blob,
            AnnotationConstants.Unindexed);
        DataType dataType = attributeMetaDesc.getDataType();
        String className = dataType.getClassName();
        if (!ClassConstants.primitive_byte_array.equals(className)
            && !dataType.isSerialized()) {
            throw new ValidationException(
                MessageCode.SILM3GEN1010,
                env,
                fieldDeclaration);
        }
        attributeMetaDesc.setBlob(true);
    }

    /**
     * Handles unindexed.
     * 
     * @param attributeMetaDesc
     *            the attribute meta description
     * @param fieldDeclaration
     *            the field declaration
     */
    protected void handleUnindexed(AttributeMetaDesc attributeMetaDesc,
            FieldDeclaration fieldDeclaration) {
        attributeMetaDesc.setUnindexed(true);
    }

    /**
     * Validates annotation consistency.
     * 
     * @param fieldDeclaration
     *            the field declaration
     * @param targetAnnotation
     *            the validation target
     * @param annotations
     *            annotations
     */
    protected void validateAnnotationConsistency(
            FieldDeclaration fieldDeclaration, String targetAnnotation,
            String... annotations) {
        for (String annotation : annotations) {
            if (isAnnotated(annotation, fieldDeclaration)) {
                throw new ValidationException(
                    MessageCode.SILM3GEN1006,
                    env,
                    fieldDeclaration,
                    targetAnnotation,
                    annotation);
            }
        }
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
        if (!attributeMetaDesc.isImpermanent()) {
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
                fieldDeclaration,
                fieldDeclaration.getSimpleName());
        }
        if (attributeMetaDesc.getWriteMethodName() == null) {
            throw new ValidationException(
                MessageCode.SILM3GEN1012,
                env,
                fieldDeclaration,
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
     * Returns {@code true} if annotation is present on the declaration.
     * 
     * @param annotation
     *            the annotation
     * @param declaration
     *            the declaration
     * @return {@code true} if annotation is present on the declaration
     */
    protected boolean isAnnotated(String annotation, Declaration declaration) {
        return DeclarationUtil
            .getAnnotationMirror(env, declaration, annotation) != null;
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
