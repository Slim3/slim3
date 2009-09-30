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

import static org.slim3.gen.AnnotationConstants.Blob;
import static org.slim3.gen.AnnotationConstants.Impermanent;
import static org.slim3.gen.AnnotationConstants.PrimaryKey;
import static org.slim3.gen.AnnotationConstants.Text;
import static org.slim3.gen.AnnotationConstants.Unindexed;
import static org.slim3.gen.AnnotationConstants.Version;
import static org.slim3.gen.ClassConstants.Key;
import static org.slim3.gen.ClassConstants.Long;
import static org.slim3.gen.ClassConstants.String;
import static org.slim3.gen.ClassConstants.primitive_byte_array;
import static org.slim3.gen.ClassConstants.primitive_long;

import java.util.List;

import org.slim3.gen.datastore.DataType;
import org.slim3.gen.datastore.DataTypeFactory;
import org.slim3.gen.message.MessageCode;
import org.slim3.gen.processor.ValidationException;
import org.slim3.gen.util.DeclarationUtil;
import org.slim3.gen.util.StringUtil;
import org.slim3.gen.util.TypeUtil;

import com.sun.mirror.apt.AnnotationProcessorEnvironment;
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
        DataType dataType =
            datastoreTypeFactory.createDataType(
                fieldDeclaration,
                fieldDeclaration.getType());
        AttributeMetaDesc attributeMetaDesc =
            new AttributeMetaDesc(fieldDeclaration.getSimpleName(), dataType);
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
        if (isAnnotated(Impermanent, fieldDeclaration)) {
            handleImpermanent(attributeMetaDesc, fieldDeclaration);
        } else if (isAnnotated(PrimaryKey, fieldDeclaration)) {
            handlePrimaryKey(attributeMetaDesc, fieldDeclaration);
        } else if (isAnnotated(Version, fieldDeclaration)) {
            handleVersion(attributeMetaDesc, fieldDeclaration);
        } else if (isAnnotated(Text, fieldDeclaration)) {
            handleText(attributeMetaDesc, fieldDeclaration);
        } else if (isAnnotated(Blob, fieldDeclaration)) {
            handleBlob(attributeMetaDesc, fieldDeclaration);
        } else if (isAnnotated(Unindexed, fieldDeclaration)) {
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
            Impermanent,
            PrimaryKey,
            Version,
            Text,
            Blob);
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
            PrimaryKey,
            Version,
            Text,
            Blob,
            Unindexed);
        if (!Key.equals(attributeMetaDesc.getDataType().getClassName())) {
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
            Version,
            Text,
            Blob,
            Unindexed);
        String className = attributeMetaDesc.getDataType().getClassName();
        if (!Long.equals(className) && !primitive_long.equals(className)) {
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
        validateAnnotationConsistency(fieldDeclaration, Text, Blob, Unindexed);
        if (!String.equals(attributeMetaDesc.getDataType().getClassName())) {
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
        validateAnnotationConsistency(fieldDeclaration, Blob, Unindexed);
        DataType dataType = attributeMetaDesc.getDataType();
        String className = dataType.getClassName();
        if (!primitive_byte_array.equals(className) && !dataType.isSerialized()) {
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
        TypeMirror fieldTypeMirror = fieldDeclaration.getType();
        for (MethodDeclaration m : methodDeclarations) {
            if (isReadMethod(m, attributeMetaDesc, fieldTypeMirror)) {
                attributeMetaDesc.setReadMethodName(m.getSimpleName());
                if (attributeMetaDesc.getWriteMethodName() != null) {
                    break;
                }
            } else if (isWriteMethod(m, attributeMetaDesc, fieldTypeMirror)) {
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
     * Retrun {@code true} if method is getter method.
     * 
     * @param m
     *            the method declaraion
     * @param attributeMetaDesc
     *            the attribute meta description
     * @param fieldTypeMirror
     *            the field type
     * @return {@code true} if method is getter method
     */
    protected boolean isReadMethod(MethodDeclaration m,
            AttributeMetaDesc attributeMetaDesc, TypeMirror fieldTypeMirror) {
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
        if (!propertyName.equals(attributeMetaDesc.getName())
            || TypeUtil.isVoid(m.getReturnType())
            || m.getParameters().size() != 0) {
            return false;
        }
        return m.getReturnType().equals(fieldTypeMirror);
    }

    /**
     * Retrun {@code true} if method is setter method.
     * 
     * @param m
     *            the method declaraion
     * @param attributeMetaDesc
     *            the attribute meta description
     * @param fieldTypeMirror
     *            the field type
     * @return {@code true} if method is setter method
     */
    protected boolean isWriteMethod(MethodDeclaration m,
            AttributeMetaDesc attributeMetaDesc, TypeMirror fieldTypeMirror) {
        if (!m.getSimpleName().startsWith("set")) {
            return false;
        }
        String propertyName =
            StringUtil.decapitalize(m.getSimpleName().substring(3));
        if (!propertyName.equals(attributeMetaDesc.getName())
            || !TypeUtil.isVoid(m.getReturnType())
            || m.getParameters().size() != 1) {
            return false;
        }
        TypeMirror parameterTypeMirror =
            m.getParameters().iterator().next().getType();
        return parameterTypeMirror.equals(fieldTypeMirror);
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
