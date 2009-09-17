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
import static org.slim3.gen.AnnotationConstants.Version;
import static org.slim3.gen.ClassConstants.Key;
import static org.slim3.gen.ClassConstants.Long;
import static org.slim3.gen.ClassConstants.String;
import static org.slim3.gen.ClassConstants.primitive_long;

import java.util.List;

import org.slim3.gen.datastore.DatastoreType;
import org.slim3.gen.datastore.DatastoreTypeFactory;
import org.slim3.gen.message.MessageCode;
import org.slim3.gen.processor.ValidationException;
import org.slim3.gen.util.StringUtil;
import org.slim3.gen.util.TypeUtil;

import com.sun.mirror.apt.AnnotationProcessorEnvironment;
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
     * @param attributeDeclaration
     *            the attribute declaration
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

        AttributeMetaDesc attributeMetaDesc =
            new AttributeMetaDesc(fieldDeclaration.getSimpleName());
        handleField(attributeMetaDesc, fieldDeclaration);
        handleMethod(attributeMetaDesc, fieldDeclaration, methodDeclarations);
        return attributeMetaDesc;
    }

    protected void handleField(AttributeMetaDesc attributeMetaDesc,
            FieldDeclaration fieldDeclaration) {
        DatastoreTypeFactory datastoreTypeFactory =
            creaetDatastoreTypeFactory();
        DatastoreType type =
            datastoreTypeFactory.createDatastoreType(
                fieldDeclaration,
                fieldDeclaration.getType());
        attributeMetaDesc.setTypeName(type.getTypeName());
        attributeMetaDesc.setDeclaredTypeName(type.getDeclaredTypeName());
        attributeMetaDesc.setElementTypeName(type.getElementTypeName());
        attributeMetaDesc.setImplicationTypeName(type.getImplicationTypeName());
        attributeMetaDesc.setCollection(type.isCollection());
        attributeMetaDesc.setArray(type.isArray());
        attributeMetaDesc.setPrimitive(type.isPrimitive());
        attributeMetaDesc.setUnindexed(type.isUnindex());
        if (type.isAnnotated(Impermanent)) {
            handleImpermanent(attributeMetaDesc, type);
        } else if (type.isAnnotated(PrimaryKey)) {
            handlePrimaryKey(attributeMetaDesc, type);
        } else if (type.isAnnotated(Version)) {
            handleVersion(attributeMetaDesc, type);
        } else if (type.isAnnotated(Text)) {
            handleText(attributeMetaDesc, type);
        } else if (type.isAnnotated(Blob)) {
            handleBlob(attributeMetaDesc, type);
        } else if (type.isSerializable()) {
            handleSerializable(attributeMetaDesc, type);
        }
    }

    protected void handleImpermanent(AttributeMetaDesc attributeMetaDesc,
            DatastoreType type) {
        validateAnnotationConsistency(
            type,
            Impermanent,
            PrimaryKey,
            Version,
            Text,
            Blob);
        attributeMetaDesc.setImpermanent(true);
    }

    protected void handlePrimaryKey(AttributeMetaDesc attributeMetaDesc,
            DatastoreType type) {
        validateAnnotationConsistency(type, PrimaryKey, Version, Text, Blob);
        if (!Key.equals(type.getTypeName())) {
            throw new ValidationException(MessageCode.SILM3GEN1007, env, type
                .getDeclaration());
        }
        attributeMetaDesc.setPrimaryKey(true);
    }

    protected void handleVersion(AttributeMetaDesc attributeMetaDesc,
            DatastoreType type) {
        validateAnnotationConsistency(type, Version, Text, Blob);
        if (!Long.equals(type.getTypeName())
            && primitive_long.equals(type.getTypeName())) {
            throw new ValidationException(MessageCode.SILM3GEN1008, env, type
                .getDeclaration());
        }
        attributeMetaDesc.setVersion(true);
    }

    protected void handleText(AttributeMetaDesc attributeMetaDesc,
            DatastoreType type) {
        validateAnnotationConsistency(type, Text, Blob);
        if (!String.equals(type.getTypeName())) {
            throw new ValidationException(MessageCode.SILM3GEN1009, env, type
                .getDeclaration());
        }
        attributeMetaDesc.setText(true);
        attributeMetaDesc.setUnindexed(true);
    }

    protected void handleBlob(AttributeMetaDesc attributeMetaDesc,
            DatastoreType type) {
        if (!type.isByteArray() && !type.isSerializable()) {
            throw new ValidationException(MessageCode.SILM3GEN1009, env, type
                .getDeclaration());
        }
        if (!type.isByteArray()) {
            attributeMetaDesc.setSerialized(true);
        }
        attributeMetaDesc.setBlob(true);
    }

    protected void handleSerializable(AttributeMetaDesc attributeMetaDesc,
            DatastoreType type) {
        if (!type.isByteArray()) {
            attributeMetaDesc.setSerialized(true);
        }
        attributeMetaDesc.setShortBlob(true);
    }

    protected void validateAnnotationConsistency(DatastoreType type,
            String targetAnnotation, String... annotations) {
        for (String annotation : annotations) {
            if (type.isAnnotated(annotation)) {
                throw new ValidationException(
                    MessageCode.SILM3GEN1006,
                    env,
                    type.getDeclaration(),
                    targetAnnotation,
                    annotation);
            }
        }
    }

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

    protected DatastoreTypeFactory creaetDatastoreTypeFactory() {
        return new DatastoreTypeFactory(env);
    }

}
