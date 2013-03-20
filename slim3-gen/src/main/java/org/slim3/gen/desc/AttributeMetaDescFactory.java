/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
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
import java.util.Map;

import org.slim3.gen.AnnotationConstants;
import org.slim3.gen.ClassConstants;
import org.slim3.gen.datastore.ArrayType;
import org.slim3.gen.datastore.CollectionType;
import org.slim3.gen.datastore.CoreReferenceType;
import org.slim3.gen.datastore.DataType;
import org.slim3.gen.datastore.DataTypeFactory;
import org.slim3.gen.datastore.InverseModelRefType;
import org.slim3.gen.datastore.ModelRefType;
import org.slim3.gen.datastore.OtherReferenceType;
import org.slim3.gen.message.MessageCode;
import org.slim3.gen.processor.UnknownDeclarationException;
import org.slim3.gen.processor.ValidationException;
import org.slim3.gen.util.AnnotationMirrorUtil;
import org.slim3.gen.util.DeclarationUtil;
import org.slim3.gen.util.FieldDeclarationUtil;
import org.slim3.gen.util.TypeUtil;

import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.declaration.AnnotationMirror;
import com.sun.mirror.declaration.AnnotationTypeElementDeclaration;
import com.sun.mirror.declaration.AnnotationValue;
import com.sun.mirror.declaration.ClassDeclaration;
import com.sun.mirror.declaration.FieldDeclaration;
import com.sun.mirror.declaration.MethodDeclaration;
import com.sun.mirror.declaration.TypeDeclaration;
import com.sun.mirror.type.ClassType;
import com.sun.mirror.type.DeclaredType;
import com.sun.mirror.type.InterfaceType;
import com.sun.mirror.type.TypeMirror;
import com.sun.mirror.util.SimpleTypeVisitor;

/**
 * Represents an attribute meta description factory.
 * 
 * @author taedium
 * @author oyama
 * @since 1.0.0
 * 
 */
@SuppressWarnings("deprecation")
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
        String attributeName =
            FieldDeclarationUtil.getPropertyName(fieldDeclaration);
        String name = attributeName;
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
                name = value;
            }
        }
        DataTypeFactory dataTypeFactory = creaetDataTypeFactory();
        DataType dataType =
            dataTypeFactory.createDataType(fieldDeclaration, fieldDeclaration
                .getType());
        AttributeMetaDesc attributeMetaDesc =
            new AttributeMetaDesc(name, attributeName, dataType);
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
        if (AnnotationMirrorUtil.getElementValue(
                attribute,
                AnnotationConstants.cipher) == Boolean.TRUE) {
                handleCipher(
                    attributeMetaDesc,
                    classDeclaration,
                    fieldDeclaration,
                    attribute);
            }
        handleJson(
            attributeMetaDesc,
            classDeclaration,
            fieldDeclaration,
            attribute);
        handleAttributeListener(
            attributeMetaDesc,
            classDeclaration,
            fieldDeclaration,
            attribute);
        if (attributeMetaDesc.isPersistent()) {
            DataType dataType = attributeMetaDesc.getDataType();
            if (dataType instanceof InverseModelRefType) {
                if (classDeclaration
                    .equals(fieldDeclaration.getDeclaringType())) {
                    throw new ValidationException(
                        MessageCode.SLIM3GEN1035,
                        env,
                        fieldDeclaration.getPosition());
                }
                throw new ValidationException(
                    MessageCode.SLIM3GEN1036,
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
                if (dataType instanceof ArrayType
                    && !ArrayType.class
                        .cast(dataType)
                        .getComponentType()
                        .getClassName()
                        .equals(ClassConstants.primitive_byte)) {
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
                    MessageCode.SLIM3GEN1007,
                    env,
                    fieldDeclaration.getPosition());
            }
            throw new ValidationException(
                MessageCode.SLIM3GEN1029,
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
                    MessageCode.SLIM3GEN1008,
                    env,
                    fieldDeclaration.getPosition());
            }
            throw new ValidationException(
                MessageCode.SLIM3GEN1030,
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
        if (dataType instanceof CollectionType
            && CollectionType.class.cast(dataType).getElementType() instanceof CoreReferenceType) {
            throwExceptionForLobUnsupportedType(
                classDeclaration,
                fieldDeclaration,
                attribute);
        }
        if (dataType instanceof ModelRefType) {
            if (classDeclaration.equals(fieldDeclaration.getDeclaringType())) {
                throw new ValidationException(
                    MessageCode.SLIM3GEN1009,
                    env,
                    fieldDeclaration.getPosition());
            }
            throw new ValidationException(
                MessageCode.SLIM3GEN1028,
                env,
                classDeclaration.getPosition(),
                fieldDeclaration.getSimpleName(),
                fieldDeclaration.getDeclaringType().getQualifiedName());
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
     * Handles cipher.
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
    protected void handleCipher(AttributeMetaDesc attributeMetaDesc,
            ClassDeclaration classDeclaration,
            FieldDeclaration fieldDeclaration, AnnotationMirror attribute) {
        if (AnnotationMirrorUtil.getElementValue(
            attribute,
            AnnotationConstants.persistent) == Boolean.FALSE) {
            throwExceptionForConflictedElements(
                classDeclaration,
                fieldDeclaration,
                attribute,
                AnnotationConstants.cipher,
                AnnotationConstants.persistent + " = false");
        }
        String type = attributeMetaDesc.getDataType().getTypeName();
        if (!type.equals(ClassConstants.String) && !type.equals(ClassConstants.Text)) {
            throw new ValidationException(
                MessageCode.SLIM3GEN1053,
                env,
                attribute.getPosition());
        }
        attributeMetaDesc.setCipher(true);
    }

    /**
     * Handles the json.
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
    protected void handleJson(AttributeMetaDesc attributeMetaDesc,
            ClassDeclaration classDeclaration,
            FieldDeclaration fieldDeclaration, AnnotationMirror attribute) {
        JsonAnnotation anno = new JsonAnnotation();
        attributeMetaDesc.setJson(anno);
        AnnotationMirror json =
                DeclarationUtil.getAnnotationMirror(
                    env,
                    fieldDeclaration,
                    AnnotationConstants.Json);
        if(json == null){
            return;
        }
        for(Map.Entry<AnnotationTypeElementDeclaration, AnnotationValue> entry
                : json.getElementValues().entrySet()){
            String sn = entry.getKey().getSimpleName();
            if(sn.equals(AnnotationConstants.ignore)) {
                anno.setIgnore(entry.getValue() != null ?
                    (Boolean)entry.getValue().getValue()
                    : false);
            } else if(sn.equals(AnnotationConstants.ignoreNull)){
                anno.setIgnoreNull(entry.getValue() != null ?
                    (Boolean)entry.getValue().getValue()
                    : false);
            } else if(sn.equals(AnnotationConstants.alias)){
                anno.setAlias(entry.getValue() != null ?
                    (String)entry.getValue().getValue()
                    : "");
            } else if(sn.equals(AnnotationConstants.coder)){
                anno.setCoderClassName(getClassNameOfClassParameter(
                    entry.getKey(), entry.getValue()
                    ));
            } else if(sn.equals(AnnotationConstants.order)){
                anno.setOrder(entry.getValue() != null ?
                    (Integer)entry.getValue().getValue()
                    : Integer.MAX_VALUE);
            }
        }
    }

    /**
     * Gets the class name of the class parameter.
     * @param declaration the declaration
     * @param value the value
     * @return the class name
     */
    protected String getClassNameOfClassParameter(
            AnnotationTypeElementDeclaration declaration, AnnotationValue value){
        String className = TypeUtil.toClassType(
            (TypeMirror)declaration.getDefaultValue().getValue()
            ).getDeclaration().getQualifiedName();
        TypeMirror mirror = (TypeMirror)value.getValue();
        if(mirror == null){
            return className;
        }
        if (value instanceof InterfaceType) {
            throw new ValidationException(
                MessageCode.SLIM3GEN1055,
                env,
                declaration.getPosition());
        }
        ClassType coderClassType = TypeUtil.toClassType(mirror);
        if(coderClassType == null) return className;
        ClassDeclaration coderClassDeclaration =
            coderClassType.getDeclaration();
        if (coderClassDeclaration == null) {
            throw new UnknownDeclarationException(
                env,
                coderClassDeclaration,
                coderClassType);
        }
        className = coderClassDeclaration.getQualifiedName();
        if (!DeclarationUtil
                .hasPublicDefaultConstructor(coderClassDeclaration)) {
                throw new ValidationException(
                    MessageCode.SLIM3GEN1054,
                    env,
                    declaration.getPosition(),
                    className);
        }
        return className;
    }
    
    /**
     * Handles the attribute listener.
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
    protected void handleAttributeListener(AttributeMetaDesc attributeMetaDesc,
            ClassDeclaration classDeclaration,
            FieldDeclaration fieldDeclaration, AnnotationMirror attribute) {
        Object listener =
            AnnotationMirrorUtil.getElementValue(
                attribute,
                AnnotationConstants.listener);
        if (listener == null) {
            return;
        }
        if (listener instanceof InterfaceType) {
            throw new ValidationException(
                MessageCode.SLIM3GEN1052,
                env,
                fieldDeclaration.getPosition());
        }
        ClassType listenerClassType =
            TypeUtil.toClassType((TypeMirror) listener);
        if (listenerClassType == null) {
            return;
        }
        ClassDeclaration listenerClassDeclaration =
            listenerClassType.getDeclaration();
        if (listenerClassDeclaration == null) {
            throw new UnknownDeclarationException(
                env,
                listenerClassDeclaration,
                listenerClassType);
        }
        if (!validateAttributeListenerParameter(
            attributeMetaDesc,
            classDeclaration,
            fieldDeclaration,
            listenerClassType)) {
            throw new ValidationException(
                MessageCode.SLIM3GEN1051,
                env,
                fieldDeclaration.getPosition(),
                listenerClassDeclaration.getQualifiedName(),
                TypeUtil
                    .toClassType(fieldDeclaration.getType())
                    .getDeclaration()
                    .getQualifiedName());
        }
        if (!DeclarationUtil
            .hasPublicDefaultConstructor(listenerClassDeclaration)) {
            if (classDeclaration.equals(fieldDeclaration.getDeclaringType())) {
                throw new ValidationException(
                    MessageCode.SLIM3GEN1050,
                    env,
                    fieldDeclaration.getPosition(),
                    listenerClassDeclaration.getQualifiedName());
            }
            throw new ValidationException(
                MessageCode.SLIM3GEN1045,
                env,
                classDeclaration.getPosition(),
                listenerClassDeclaration.getQualifiedName());
        }
        attributeMetaDesc
            .setAttributeListenerClassName(listenerClassDeclaration
                .getQualifiedName());
    }

    /**
     * Handles the attribute listener generics parameter.
     * 
     * @param attributeMetaDesc
     *            the attribute meta description
     * @param classDeclaration
     *            the model class declaration
     * @param fieldDeclaration
     *            the field declaration
     * @param classType
     *            the class type
     * @return whether the validation is OK
     */
    protected boolean validateAttributeListenerParameter(
            AttributeMetaDesc attributeMetaDesc,
            ClassDeclaration classDeclaration,
            FieldDeclaration fieldDeclaration, ClassType classType) {
        if (classType.getActualTypeArguments().size() == 1
            && classType.getActualTypeArguments().contains(
                fieldDeclaration.getType())) {
            return true;
        }
        ClassType superclass = classType.getSuperclass();
        if (superclass != null) {
            if (validateAttributeListenerParameter(
                attributeMetaDesc,
                classDeclaration,
                fieldDeclaration,
                superclass)) {
                return true;
            }
        }
        for (InterfaceType superInterfaceType : classType.getSuperinterfaces()) {
            if (validateAttributeListenerParameter(
                attributeMetaDesc,
                classDeclaration,
                fieldDeclaration,
                superInterfaceType)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Handles the attribute listener generics parameter.
     * 
     * @param attributeMetaDesc
     *            the attribute meta description
     * @param classDeclaration
     *            the model class declaration
     * @param fieldDeclaration
     *            the field declaration
     * @param interfaceType
     *            the interface type
     * @return whether the validation is OK
     */
    protected boolean validateAttributeListenerParameter(
            AttributeMetaDesc attributeMetaDesc,
            ClassDeclaration classDeclaration,
            FieldDeclaration fieldDeclaration, InterfaceType interfaceType) {
        if (interfaceType.getActualTypeArguments().size() == 1
            && interfaceType.getActualTypeArguments().contains(
                fieldDeclaration.getType())) {
            return true;
        }
        for (InterfaceType superInterfaceType : interfaceType
            .getSuperinterfaces()) {
            if (validateAttributeListenerParameter(
                attributeMetaDesc,
                classDeclaration,
                fieldDeclaration,
                superInterfaceType)) {
                return true;
            }
        }
        return false;

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
                                MessageCode.SLIM3GEN1033,
                                env,
                                fieldDeclaration.getPosition());
                        }
                        throw new ValidationException(
                            MessageCode.SLIM3GEN1034,
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
        DataType dataType = attributeMetaDesc.getDataType();
        if (attributeMetaDesc.isPersistent()) {
            if (dataType instanceof ModelRefType) {
                validateReadMethodOnly(
                    attributeMetaDesc,
                    (ModelRefType) dataType,
                    classDeclaration,
                    fieldDeclaration,
                    readMethodDeclaration,
                    wirteMethodDeclaration);
            } else {
                validateReadAndWriteMethods(
                    attributeMetaDesc,
                    classDeclaration,
                    fieldDeclaration,
                    readMethodDeclaration,
                    wirteMethodDeclaration);
            }
        }
        if (dataType instanceof InverseModelRefType) {
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
            String expectedReadMethodName =
                FieldDeclarationUtil.getReadMethodName(fieldDeclaration);
            if (classDeclaration.equals(fieldDeclaration.getDeclaringType())) {
                throw new ValidationException(
                    MessageCode.SLIM3GEN1011,
                    env,
                    fieldDeclaration.getPosition(),
                    expectedReadMethodName);
            }
            throw new ValidationException(
                MessageCode.SLIM3GEN1024,
                env,
                classDeclaration.getPosition(),
                expectedReadMethodName,
                fieldDeclaration.getSimpleName(),
                fieldDeclaration.getDeclaringType().getQualifiedName());
        }
        if (writeMethodDeclaration == null) {
            String expectedWriteMethodName =
                FieldDeclarationUtil.getWriteMethodName(fieldDeclaration);
            if (classDeclaration.equals(fieldDeclaration.getDeclaringType())) {
                throw new ValidationException(
                    MessageCode.SLIM3GEN1012,
                    env,
                    fieldDeclaration.getPosition(),
                    expectedWriteMethodName);
            }
            throw new ValidationException(
                MessageCode.SLIM3GEN1025,
                env,
                classDeclaration.getPosition(),
                expectedWriteMethodName,
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
            String expectedReadMethodName =
                FieldDeclarationUtil.getReadMethodName(fieldDeclaration);
            if (classDeclaration.equals(fieldDeclaration.getDeclaringType())) {
                throw new ValidationException(
                    MessageCode.SLIM3GEN1011,
                    env,
                    fieldDeclaration.getPosition(),
                    expectedReadMethodName);
            }
            throw new ValidationException(
                MessageCode.SLIM3GEN1024,
                env,
                classDeclaration.getPosition(),
                expectedReadMethodName,
                fieldDeclaration.getSimpleName(),
                fieldDeclaration.getDeclaringType().getQualifiedName());
        }
        if (writeMethodDeclaration != null) {
            String fieldDefinition =
                String.format(
                    "%1$s %2$s = new %1$s(%3$s.class);",
                    fieldDeclaration.getType(),
                    fieldDeclaration.getSimpleName(),
                    modelRefType.getReferenceModelClassName());
            if (classDeclaration.equals(fieldDeclaration.getDeclaringType())) {
                throw new ValidationException(
                    MessageCode.SLIM3GEN1041,
                    env,
                    writeMethodDeclaration.getPosition(),
                    fieldDeclaration.getSimpleName(),
                    fieldDefinition);
            }
            throw new ValidationException(
                MessageCode.SLIM3GEN1042,
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
            String expectedReadMethodName =
                FieldDeclarationUtil.getReadMethodName(fieldDeclaration);
            if (classDeclaration.equals(fieldDeclaration.getDeclaringType())) {
                throw new ValidationException(
                    MessageCode.SLIM3GEN1011,
                    env,
                    fieldDeclaration.getPosition(),
                    expectedReadMethodName);
            }
            throw new ValidationException(
                MessageCode.SLIM3GEN1024,
                env,
                classDeclaration.getPosition(),
                expectedReadMethodName,
                fieldDeclaration.getSimpleName(),
                fieldDeclaration.getDeclaringType().getQualifiedName());
        }
        if (writeMethodDeclaration != null) {
            String fieldDefinition =
                String.format(
                    "%1$s %2$s = new %1$s(%3$s.class, \"xxx\", this);",
                    fieldDeclaration.getType(),
                    fieldDeclaration.getSimpleName(),
                    inverseModelRefType.getReferenceModelClassName());
            if (classDeclaration.equals(fieldDeclaration.getDeclaringType())) {
                throw new ValidationException(
                    MessageCode.SLIM3GEN1039,
                    env,
                    writeMethodDeclaration.getPosition(),
                    fieldDeclaration.getSimpleName(),
                    fieldDefinition,
                    classDeclaration.getSimpleName(),
                    inverseModelRefType.getReferenceModelClassName());
            }
            throw new ValidationException(
                MessageCode.SLIM3GEN1040,
                env,
                classDeclaration.getPosition(),
                fieldDeclaration.getSimpleName(),
                fieldDeclaration.getDeclaringType().getQualifiedName(),
                fieldDefinition,
                classDeclaration.getSimpleName(),
                inverseModelRefType.getReferenceModelClassName());
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
        if (TypeUtil.isVoid(m.getReturnType())
            || m.getParameters().size() != 0
            || !m.getReturnType().equals(fieldDeclaration.getType())) {
            return false;
        }
        String methodName = m.getSimpleName();
        for (String candidateReadMethodName : FieldDeclarationUtil
            .getReadMethodNames(fieldDeclaration)) {
            if (methodName.equals(candidateReadMethodName)) {
                return true;
            }
        }
        return false;
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
        String methodName = m.getSimpleName();
        if (!methodName.startsWith("set")
            || !TypeUtil.isVoid(m.getReturnType())
            || m.getParameters().size() != 1) {
            return false;
        }
        TypeMirror parameterTypeMirror =
            m.getParameters().iterator().next().getType();
        if (!parameterTypeMirror.equals(fieldDeclaration.getType())) {
            return false;
        }
        return methodName.equals(FieldDeclarationUtil
            .getWriteMethodName(fieldDeclaration));
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
                MessageCode.SLIM3GEN1005,
                env,
                fieldDeclaration.getPosition());
        }
        throw new ValidationException(
            MessageCode.SLIM3GEN1026,
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
                MessageCode.SLIM3GEN1021,
                env,
                attribute.getPosition(),
                element1,
                element2);
        }
        throw new ValidationException(
            MessageCode.SLIM3GEN1027,
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
                MessageCode.SLIM3GEN1045,
                env,
                fieldDeclaration.getPosition());
        }
        throw new ValidationException(
            MessageCode.SLIM3GEN1046,
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
                MessageCode.SLIM3GEN1031,
                env,
                fieldDeclaration.getPosition());
        }
        throw new ValidationException(
            MessageCode.SLIM3GEN1032,
            env,
            classDeclaration.getPosition(),
            fieldDeclaration.getSimpleName(),
            fieldDeclaration.getDeclaringType().getQualifiedName());
    }

}
