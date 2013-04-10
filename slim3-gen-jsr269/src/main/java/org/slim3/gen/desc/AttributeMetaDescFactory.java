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

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.SimpleTypeVisitor6;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.AttributeListener;
import org.slim3.datastore.Model;
import org.slim3.datastore.json.Json;
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
import org.slim3.gen.util.DeclarationUtil;
import org.slim3.gen.util.FieldDeclarationUtil;
import org.slim3.gen.util.TypeUtil;

/**
 * Represents an attribute meta description factory.
 * 
 * @author taedium
 * @author oyama
 * @since 1.0.0
 * 
 */
public class AttributeMetaDescFactory {

    final ProcessingEnvironment processingEnv;
    final RoundEnvironment roundEnv;

    /**
     * Creates a new {@link AttributeMetaDescFactory}.
     * 
     * @param processingEnv
     *            the processing environment
     * @param roundEnv
     *            the round environment
     */
    public AttributeMetaDescFactory(ProcessingEnvironment processingEnv,
            RoundEnvironment roundEnv) {
        if (processingEnv == null) {
            throw new NullPointerException(
                "The processingEnv parameter is null.");
        }
        if (roundEnv == null) {
            throw new NullPointerException("The roundEnv parameter is null.");
        }
        this.processingEnv = processingEnv;
        this.roundEnv = roundEnv;
    }

    /**
     * Creates a new {@link AttributeMetaDesc}
     * 
     * @param classElement
     *            the model class declaration
     * @param fieldElement
     *            the field declaration
     * @param methodDeclarations
     *            the method declarations
     * @return an attribute meta description
     */
    public AttributeMetaDesc createAttributeMetaDesc(TypeElement classElement,
            VariableElement fieldElement,
            List<ExecutableElement> methodDeclarations) {
        if (fieldElement == null) {
            throw new NullPointerException(
                "The fieldDeclaration parameter is null.");
        }
        if (methodDeclarations == null) {
            throw new NullPointerException(
                "The methodDeclarations parameter is null.");
        }
        String attributeName =
            FieldDeclarationUtil.getPropertyName(fieldElement);
        String name = attributeName;
        Attribute attribute = fieldElement.getAnnotation(Attribute.class);
        if (attribute != null) {
            String value = attribute.name();
            if (value != null && value.length() > 0) {
                name = value;
            }
        }
        DataTypeFactory dataTypeFactory = creaetDataTypeFactory();
        DataType dataType =
            dataTypeFactory.createDataType(fieldElement, fieldElement.asType());
        AttributeMetaDesc attributeMetaDesc =
            new AttributeMetaDesc(name, attributeName, dataType);
        handleField(attributeMetaDesc, classElement, fieldElement, attribute);
        handleMethod(
            attributeMetaDesc,
            classElement,
            fieldElement,
            methodDeclarations);
        return attributeMetaDesc;
    }

    /**
     * Handleds a field.
     * 
     * @param attributeMetaDesc
     *            the attribute meta description
     * @param classElement
     *            the model class declaration
     * @param fieldElement
     *            the field declaration
     * @param attribute
     *            the Attribute annotation.
     */
    protected void handleField(AttributeMetaDesc attributeMetaDesc,
            TypeElement classElement, VariableElement fieldElement,
            Attribute attribute) {
        if (attribute == null) {
            attribute = new Attribute() {

                public String name() {
                    return "";
                }

                public boolean persistent() {
                    return true;
                }

                public boolean primaryKey() {
                    return false;
                }

                public boolean version() {
                    return false;
                }

                public boolean lob() {
                    return false;
                }

                public boolean unindexed() {
                    return false;
                }

                public boolean cipher() {
                    return false;
                }

                public Class<?> listener() {
                    return AttributeListener.class;
                }

                public Class<? extends Annotation> annotationType() {
                    return null;
                }
            };
        }
        if (attribute.primaryKey()) {
            handlePrimaryKey(
                attributeMetaDesc,
                classElement,
                fieldElement,
                attribute);
        }
        if (attribute.version()) {
            handleVersion(
                attributeMetaDesc,
                classElement,
                fieldElement,
                attribute);
        }
        if (attribute.lob()) {
            handleLob(attributeMetaDesc, classElement, fieldElement, attribute);
        }
        if (attribute.unindexed()) {
            handleUnindexed(
                attributeMetaDesc,
                classElement,
                fieldElement,
                attribute);
        }
        if (!attribute.persistent()) {
            handleNotPersistent(attributeMetaDesc, fieldElement);
        }
        if (attribute.cipher()) {
            handleCipher(
                attributeMetaDesc,
                classElement,
                fieldElement,
                attribute);
        }
        handleJson(attributeMetaDesc, classElement, fieldElement, attribute);
        handleAttributeListener(
            attributeMetaDesc,
            classElement,
            fieldElement,
            attribute);
        if (attributeMetaDesc.isPersistent()) {
            DataType dataType = attributeMetaDesc.getDataType();
            if (dataType instanceof InverseModelRefType) {
                if (classElement.equals(fieldElement.getEnclosingElement())) {
                    throw new ValidationException(
                        MessageCode.SLIM3GEN1035,
                        fieldElement);
                }
                throw new ValidationException(
                    MessageCode.SLIM3GEN1036,
                    classElement,
                    fieldElement.getSimpleName(),
                    fieldElement.getEnclosingElement().toString());
            }
            if (!attributeMetaDesc.isLob()) {
                if (dataType instanceof OtherReferenceType) {
                    throwExceptionForNonCoreType(
                        classElement,
                        fieldElement,
                        attribute);
                }
                if (dataType instanceof CollectionType
                    && CollectionType.class.cast(dataType).getElementType() instanceof OtherReferenceType) {
                    throwExceptionForNonCoreType(
                        classElement,
                        fieldElement,
                        attribute);
                }
                if (dataType instanceof ArrayType
                    && !ArrayType.class
                        .cast(dataType)
                        .getComponentType()
                        .getClassName()
                        .equals(ClassConstants.primitive_byte)) {
                    throwExceptionForNonCoreType(
                        classElement,
                        fieldElement,
                        attribute);
                }
            }
        }
        if (attributeMetaDesc.getDataType() instanceof ModelRefType) {
            validateModelRefTypeArgument(
                attributeMetaDesc,
                classElement,
                fieldElement);
        }
    }

    /**
     * Handles not persistent.
     * 
     * @param attributeMetaDesc
     *            the attribute meta description
     * @param fieldElement
     *            the field declaration
     */
    protected void handleNotPersistent(AttributeMetaDesc attributeMetaDesc,
            VariableElement fieldElement) {
        attributeMetaDesc.setPersistent(false);
    }

    /**
     * Handles primary key.
     * 
     * @param attributeMetaDesc
     *            the attribute meta description
     * @param classElement
     *            the model class declaration
     * @param fieldElement
     *            the field declaration
     * @param attribute
     *            the Attribute annotation mirror
     */
    protected void handlePrimaryKey(AttributeMetaDesc attributeMetaDesc,
            TypeElement classElement, VariableElement fieldElement,
            Attribute attribute) {
        if (attribute.version()) {
            throwExceptionForConflictedElements(
                classElement,
                fieldElement,
                attribute,
                AnnotationConstants.primaryKey,
                AnnotationConstants.version);
        }
        if (attribute.lob()) {
            throwExceptionForConflictedElements(
                classElement,
                fieldElement,
                attribute,
                AnnotationConstants.primaryKey,
                AnnotationConstants.lob);
        }
        if (attribute.unindexed()) {
            throwExceptionForConflictedElements(
                classElement,
                fieldElement,
                attribute,
                AnnotationConstants.primaryKey,
                AnnotationConstants.unindexed);
        }
        if (!attribute.persistent()) {
            throwExceptionForConflictedElements(
                classElement,
                fieldElement,
                attribute,
                AnnotationConstants.primaryKey,
                AnnotationConstants.persistent + " = false");
        }
        if (!ClassConstants.Key.equals(attributeMetaDesc
            .getDataType()
            .getClassName())) {
            if (classElement.equals(fieldElement.getEnclosingElement())) {
                throw new ValidationException(
                    MessageCode.SLIM3GEN1007,
                    fieldElement);
            }
            throw new ValidationException(
                MessageCode.SLIM3GEN1029,
                classElement,
                fieldElement.getSimpleName(),
                fieldElement.getEnclosingElement().toString());
        }
        attributeMetaDesc.setPrimaryKey(true);
    }

    /**
     * Handles version.
     * 
     * @param attributeMetaDesc
     *            the attribute meta description
     * @param classElement
     *            the model class declaration
     * @param fieldElement
     *            the field declaration
     * @param attribute
     *            the attribute annotation mirror
     */
    protected void handleVersion(AttributeMetaDesc attributeMetaDesc,
            TypeElement classElement, VariableElement fieldElement,
            Attribute attribute) {
        if (attribute.lob()) {
            throwExceptionForConflictedElements(
                classElement,
                fieldElement,
                attribute,
                AnnotationConstants.version,
                AnnotationConstants.lob);
        }
        if (!attribute.persistent()) {
            throwExceptionForConflictedElements(
                classElement,
                fieldElement,
                attribute,
                AnnotationConstants.version,
                AnnotationConstants.persistent + " = false");
        }
        String className = attributeMetaDesc.getDataType().getClassName();
        if (!ClassConstants.Long.equals(className)
            && !ClassConstants.primitive_long.equals(className)) {
            if (classElement.equals(fieldElement.getEnclosingElement())) {
                throw new ValidationException(
                    MessageCode.SLIM3GEN1008,
                    fieldElement);
            }
            throw new ValidationException(
                MessageCode.SLIM3GEN1030,
                classElement,
                fieldElement.getSimpleName(),
                fieldElement.getEnclosingElement().toString());
        }
        attributeMetaDesc.setVersion(true);
    }

    /**
     * Handles a large object.
     * 
     * @param attributeMetaDesc
     *            the attribute meta description
     * @param classElement
     *            the model class declaration
     * @param fieldElement
     *            the field declaration
     * @param attribute
     *            the Attribute annotation mirror
     */
    protected void handleLob(AttributeMetaDesc attributeMetaDesc,
            TypeElement classElement, VariableElement fieldElement,
            Attribute attribute) {
        if (!attribute.persistent()) {
            throwExceptionForConflictedElements(
                classElement,
                fieldElement,
                attribute,
                AnnotationConstants.lob,
                AnnotationConstants.persistent + " = false");
        }
        // memo: remove conflict check with "unindeded".
        // JSR269 can't detect true value from 'set' or 'default'.

        DataType dataType = attributeMetaDesc.getDataType();
        if (dataType instanceof CoreReferenceType
            && !ClassConstants.String.equals(dataType.getClassName())) {
            throwExceptionForLobUnsupportedType(
                classElement,
                fieldElement,
                attribute);
        }
        if (dataType instanceof CollectionType
            && CollectionType.class.cast(dataType).getElementType() instanceof CoreReferenceType) {
            throwExceptionForLobUnsupportedType(
                classElement,
                fieldElement,
                attribute);
        }
        if (dataType instanceof ModelRefType) {
            if (classElement.equals(fieldElement.getEnclosingElement())) {
                throw new ValidationException(
                    MessageCode.SLIM3GEN1009,
                    fieldElement);
            }
            throw new ValidationException(
                MessageCode.SLIM3GEN1028,
                classElement,
                fieldElement.getSimpleName(),
                fieldElement.getEnclosingElement().toString());
        }
        attributeMetaDesc.setLob(true);
    }

    /**
     * Handles unindexed.
     * 
     * @param attributeMetaDesc
     *            the attribute meta description
     * @param classElement
     *            the model class declaration
     * @param fieldElement
     *            the field declaration
     * @param attribute
     *            the annotation mirror for Attribute
     */
    protected void handleUnindexed(AttributeMetaDesc attributeMetaDesc,
            TypeElement classElement, VariableElement fieldElement,
            Attribute attribute) {
        if (!attribute.persistent()) {
            throwExceptionForConflictedElements(
                classElement,
                fieldElement,
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
     * @param classElement
     *            the model class declaration
     * @param fieldElement
     *            the field declaration
     * @param attribute
     *            the annotation mirror for Attribute
     */
    protected void handleCipher(AttributeMetaDesc attributeMetaDesc,
            TypeElement classElement, VariableElement fieldElement,
            Attribute attribute) {
        if (!attribute.persistent()) {
            throwExceptionForConflictedElements(
                classElement,
                fieldElement,
                attribute,
                AnnotationConstants.cipher,
                AnnotationConstants.persistent + " = false");
        }
        String type = attributeMetaDesc.getDataType().getTypeName();
        if (!type.equals(ClassConstants.String)
            && !type.equals(ClassConstants.Text)) {
            throw new ValidationException(
                MessageCode.SLIM3GEN1053,
                fieldElement);
        }
        attributeMetaDesc.setCipher(true);
    }

    /**
     * Handles the json.
     * 
     * @param attributeMetaDesc
     *            the attribute meta description
     * @param classElement
     *            the model class declaration
     * @param fieldElement
     *            the field declaration
     * @param attribute
     *            the annotation mirror for Attribute
     */
    protected void handleJson(AttributeMetaDesc attributeMetaDesc,
            TypeElement classElement, VariableElement fieldElement,
            Attribute attribute) {
        JsonAnnotation anno = new JsonAnnotation();
        attributeMetaDesc.setJson(anno);
        Json json = fieldElement.getAnnotation(Json.class);
        if (json == null) {
            return;
        }
        anno.setIgnore(json.ignore());
        anno.setIgnoreNull(json.ignoreNull());
        anno.setAlias(json.alias());
        ExecutableElement coderMethod = null;
        AnnotationValue coder = null;
        for (AnnotationMirror mirror : fieldElement.getAnnotationMirrors()) {
            Map<? extends ExecutableElement, ? extends AnnotationValue> elementValues =
                mirror.getElementValues();
            for (ExecutableElement e : elementValues.keySet()) {
                if (AnnotationConstants.coder.equals(e
                    .getSimpleName()
                    .toString())) {
                    coderMethod = e;
                    coder = elementValues.get(e);
                }
            }
        }
        if (coder == null) {
            return;
        }
        String classNameOfClassParameter =
            getClassNameOfClassParameter(coderMethod, coder);
        anno.setCoderClassName(classNameOfClassParameter);

        anno.setOrder(json.order());
    }

    /**
     * Gets the class name of the class parameter.
     * 
     * @param declaration
     *            the declaration
     * @param value
     *            the value
     * @return the class name
     */
    protected String getClassNameOfClassParameter(
            ExecutableElement declaration, AnnotationValue value) {
        String className =
            TypeUtil
                .toClassType(
                    (TypeMirror) declaration.getDefaultValue().getValue())
                .getEnclosingType()
                .toString();
        TypeMirror mirror = (TypeMirror) value.getValue();
        if (mirror == null) {
            return className;
        }
        if (mirror.getKind() == TypeKind.DECLARED) {

        }
        if (processingEnv.getTypeUtils().asElement(mirror).getKind() == ElementKind.INTERFACE) {
            throw new ValidationException(MessageCode.SLIM3GEN1055, declaration);
        }
        DeclaredType coderClassType = TypeUtil.toClassType(mirror);
        if (coderClassType == null)
            return className;
        TypeElement coderClassDeclaration =
            (TypeElement) coderClassType.asElement();
        if (coderClassDeclaration == null) {
            throw new UnknownDeclarationException(
                coderClassDeclaration,
                coderClassType.asElement());
        }
        className = coderClassDeclaration.getQualifiedName().toString();
        if (!DeclarationUtil.hasPublicDefaultConstructor(coderClassDeclaration)) {
            throw new ValidationException(
                MessageCode.SLIM3GEN1054,
                declaration,
                className);
        }
        return className;
    }

    /**
     * Handles the attribute listener.
     * 
     * @param attributeMetaDesc
     *            the attribute meta description
     * @param classElement
     *            the model class declaration
     * @param fieldElement
     *            the field declaration
     * @param attribute
     *            the annotation mirror for Attribute
     */
    protected void handleAttributeListener(AttributeMetaDesc attributeMetaDesc,
            TypeElement classElement, VariableElement fieldElement,
            Attribute attribute) {
        AnnotationValue listener = null;
        for (AnnotationMirror mirror : fieldElement.getAnnotationMirrors()) {
            Map<? extends ExecutableElement, ? extends AnnotationValue> elementValues =
                mirror.getElementValues();
            for (ExecutableElement e : elementValues.keySet()) {
                if (AnnotationConstants.listener.equals(e
                    .getSimpleName()
                    .toString())) {
                    listener = elementValues.get(e);
                }
            }
        }
        if (listener == null) {
            return;
        }
        TypeElement listenerClassType =
            processingEnv.getElementUtils().getTypeElement(
                listener.getValue().toString());
        if (listenerClassType == null) {
            return;
        }
        if (listenerClassType.getKind() == ElementKind.INTERFACE) {
            throw new ValidationException(
                MessageCode.SLIM3GEN1052,
                fieldElement);
        }

        TypeElement listenerClassDeclaration = listenerClassType;
        if (!validateAttributeListenerParameter(
            attributeMetaDesc,
            classElement,
            fieldElement,
            listenerClassType)) {
            throw new ValidationException(
                MessageCode.SLIM3GEN1051,
                fieldElement,
                listenerClassDeclaration.getQualifiedName(),
                TypeUtil
                    .toClassType(fieldElement.asType())
                    .getEnclosingType()
                    .toString());
        }
        if (!DeclarationUtil
            .hasPublicDefaultConstructor(listenerClassDeclaration)) {
            if (classElement.equals(fieldElement.getEnclosingElement())) {
                throw new ValidationException(
                    MessageCode.SLIM3GEN1050,
                    fieldElement,
                    listenerClassDeclaration.getQualifiedName());
            }
            throw new ValidationException(
                MessageCode.SLIM3GEN1045,
                classElement,
                listenerClassDeclaration.getQualifiedName());
        }
        attributeMetaDesc
            .setAttributeListenerClassName(listenerClassDeclaration
                .getQualifiedName()
                .toString());
    }

    /**
     * Handles the attribute listener generics parameter.
     * 
     * @param attributeMetaDesc
     *            the attribute meta description
     * @param classElement
     *            the model class declaration
     * @param fieldElement
     *            the field declaration
     * @param listenerClassType
     * 
     *            the class type
     * @return whether the validation is OK
     */
    protected boolean validateAttributeListenerParameter(
            AttributeMetaDesc attributeMetaDesc, TypeElement classElement,
            VariableElement fieldElement, TypeElement listenerClassType) {
        if (listenerClassType.getTypeParameters().size() == 1
            && listenerClassType.getTypeParameters().contains(
                fieldElement.asType())) {
            return true;
        }
        TypeElement superclass =
            (TypeElement) processingEnv.getTypeUtils().asElement(
                listenerClassType.getSuperclass());
        if (superclass != null) {
            if (validateAttributeListenerParameter(
                attributeMetaDesc,
                classElement,
                fieldElement,
                superclass)) {
                return true;
            }
        }
        for (TypeMirror superInterfaceType : listenerClassType.getInterfaces()) {
            if (superInterfaceType.getKind() == TypeKind.DECLARED) {
                DeclaredType declaredType = (DeclaredType) superInterfaceType;
                if (validateAttributeListenerParameter(
                    attributeMetaDesc,
                    classElement,
                    fieldElement,
                    declaredType)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Handles the attribute listener generics parameter.
     * 
     * @param attributeMetaDesc
     *            the attribute meta description
     * @param classElement
     *            the model class declaration
     * @param fieldElement
     *            the field declaration
     * @param interfaceType
     *            the interface type
     * @return whether the validation is OK
     */
    protected boolean validateAttributeListenerParameter(
            AttributeMetaDesc attributeMetaDesc, TypeElement classElement,
            VariableElement fieldElement, DeclaredType interfaceType) {
        if (interfaceType.getTypeArguments().size() == 1
            && processingEnv.getTypeUtils().isSameType(
                interfaceType.getTypeArguments().get(0),
                fieldElement.asType())) {
            return true;
        }
        TypeElement interfaceElement = (TypeElement) interfaceType.asElement();
        for (TypeMirror superInterfaceType : interfaceElement.getInterfaces()) {
            if (superInterfaceType.getKind() == TypeKind.DECLARED) {
                if (validateAttributeListenerParameter(
                    attributeMetaDesc,
                    classElement,
                    fieldElement,
                    (DeclaredType) superInterfaceType)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Validates ModelRef type argument.
     * 
     * @param attributeMetaDesc
     *            the attribute meta description
     * @param classElement
     *            the model class declaration
     * @param fieldElement
     *            the field declaration
     */
    protected void validateModelRefTypeArgument(
            AttributeMetaDesc attributeMetaDesc,
            final TypeElement classElement, final VariableElement fieldElement) {
        fieldElement.asType().accept(new SimpleTypeVisitor6<Void, Void>() {

            @Override
            public Void visitDeclared(DeclaredType classType, Void p) {
                if (classType.getKind() != TypeKind.DECLARED) {
                    return super.visitDeclared(classType, p);
                }

                if (ClassConstants.ModelRef.equals(classType
                    .asElement()
                    .toString())) {
                    Collection<? extends TypeMirror> typeArgs =
                        classType.getTypeArguments();
                    if (typeArgs.isEmpty()) {
                        if (classElement.equals(fieldElement
                            .getEnclosingElement())) {
                            throw new ValidationException(
                                MessageCode.SLIM3GEN1033,
                                fieldElement);
                        }
                        throw new ValidationException(
                            MessageCode.SLIM3GEN1034,
                            classElement,
                            fieldElement.getSimpleName(),
                            fieldElement.getEnclosingElement().toString());
                    }
                    DeclaredType declaredType =
                        TypeUtil.toDeclaredType(typeArgs.iterator().next());
                    if (declaredType == null) {
                        throwExceptionForModelRefTypeArgument(
                            classElement,
                            fieldElement);
                    }
                    Model model =
                        processingEnv
                            .getTypeUtils()
                            .asElement(declaredType)
                            .getAnnotation(Model.class);
                    if (model == null) {
                        throwExceptionForModelRefTypeArgument(
                            classElement,
                            fieldElement);
                    }
                }
                TypeMirror superclassType = classType.getEnclosingType();
                if (superclassType != null) {
                    superclassType.accept(this, null);
                }
                return null;
            }
        },
            null);
    }

    /**
     * Handles a method.
     * 
     * @param attributeMetaDesc
     *            the attribute meta description
     * @param classElement
     *            the class declaration
     * @param fieldElement
     *            the field declaration
     * @param methodDeclarations
     *            the method declaration
     */
    protected void handleMethod(AttributeMetaDesc attributeMetaDesc,
            TypeElement classElement, VariableElement fieldElement,
            List<ExecutableElement> methodDeclarations) {
        ExecutableElement readMethodDeclaration = null;
        ExecutableElement wirteMethodDeclaration = null;
        for (ExecutableElement m : methodDeclarations) {
            if (isReadMethod(m, attributeMetaDesc, fieldElement)) {
                readMethodDeclaration = m;
                attributeMetaDesc.setReadMethodName(m
                    .getSimpleName()
                    .toString());
                if (attributeMetaDesc.getWriteMethodName() != null) {
                    break;
                }
            } else if (isWriteMethod(m, attributeMetaDesc, fieldElement)) {
                wirteMethodDeclaration = m;
                attributeMetaDesc.setWriteMethodName(m
                    .getSimpleName()
                    .toString());
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
                    classElement,
                    fieldElement,
                    readMethodDeclaration,
                    wirteMethodDeclaration);
            } else {
                validateReadAndWriteMethods(
                    attributeMetaDesc,
                    classElement,
                    fieldElement,
                    readMethodDeclaration,
                    wirteMethodDeclaration);
            }
        }
        if (dataType instanceof InverseModelRefType) {
            validateReadMethodOnly(
                attributeMetaDesc,
                (InverseModelRefType) dataType,
                classElement,
                fieldElement,
                readMethodDeclaration,
                wirteMethodDeclaration);
        }
    }

    /**
     * Validates the read method and the write method.
     * 
     * @param attributeMetaDesc
     *            the attribute mete description
     * @param classElement
     *            the model declaration
     * @param fieldElement
     *            the field declaration
     * @param readMethodDeclaration
     *            the read method declaration
     * @param wirteMethodDeclaration
     *            the write method declaration
     */
    protected void validateReadAndWriteMethods(
            AttributeMetaDesc attributeMetaDesc, TypeElement classElement,
            VariableElement fieldElement,
            ExecutableElement readMethodDeclaration,
            ExecutableElement wirteMethodDeclaration) {
        if (readMethodDeclaration == null) {
            String expectedReadMethodName =
                FieldDeclarationUtil.getReadMethodName(fieldElement);
            if (classElement.equals(fieldElement.getEnclosingElement())) {
                throw new ValidationException(
                    MessageCode.SLIM3GEN1011,
                    fieldElement,
                    expectedReadMethodName);
            }
            throw new ValidationException(
                MessageCode.SLIM3GEN1024,
                classElement,
                expectedReadMethodName,
                fieldElement.getSimpleName(),
                fieldElement.getEnclosingElement().toString());
        }
        if (wirteMethodDeclaration == null) {
            String expectedWriteMethodName =
                FieldDeclarationUtil.getWriteMethodName(fieldElement);
            if (classElement.equals(fieldElement.getEnclosingElement())) {
                throw new ValidationException(
                    MessageCode.SLIM3GEN1012,
                    fieldElement,
                    expectedWriteMethodName);
            }
            throw new ValidationException(
                MessageCode.SLIM3GEN1025,
                classElement,
                expectedWriteMethodName,
                fieldElement.getSimpleName(),
                fieldElement.getEnclosingElement().toString());
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
     * @param classElement
     *            the model declaration
     * @param fieldElement
     *            the field declaration
     * @param readMethodDeclaration
     *            the read method declaration
     * @param writeMethodDeclaration
     *            the write method declaration
     */
    protected void validateReadMethodOnly(AttributeMetaDesc attributeMetaDesc,
            ModelRefType modelRefType, TypeElement classElement,
            VariableElement fieldElement,
            ExecutableElement readMethodDeclaration,
            ExecutableElement writeMethodDeclaration) {
        if (readMethodDeclaration == null) {
            String expectedReadMethodName =
                FieldDeclarationUtil.getReadMethodName(fieldElement);
            if (classElement.equals(fieldElement.getEnclosingElement())) {
                throw new ValidationException(
                    MessageCode.SLIM3GEN1011,
                    fieldElement,
                    expectedReadMethodName);
            }
            throw new ValidationException(
                MessageCode.SLIM3GEN1024,
                classElement,
                expectedReadMethodName,
                fieldElement.getSimpleName(),
                fieldElement.getEnclosingElement().toString());
        }
        if (writeMethodDeclaration != null) {
            String fieldDefinition =
                String.format(
                    "%1$s %2$s = new %1$s(%3$s.class);",
                    fieldElement.asType(),
                    fieldElement.getSimpleName(),
                    modelRefType.getReferenceModelClassName());
            if (classElement.equals(fieldElement.getEnclosingElement())) {
                throw new ValidationException(
                    MessageCode.SLIM3GEN1041,
                    writeMethodDeclaration,
                    fieldElement.getSimpleName(),
                    fieldDefinition);
            }
            throw new ValidationException(
                MessageCode.SLIM3GEN1042,
                classElement,
                fieldElement.getSimpleName(),
                fieldElement.getEnclosingElement().toString(),
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
     * @param classElement
     *            the model declaration
     * @param fieldElement
     *            the field declaration
     * @param readMethodDeclaration
     *            the read method declaration
     * @param writeMethodDeclaration
     *            the write method declaration
     */
    protected void validateReadMethodOnly(AttributeMetaDesc attributeMetaDesc,
            InverseModelRefType inverseModelRefType, TypeElement classElement,
            VariableElement fieldElement,
            ExecutableElement readMethodDeclaration,
            ExecutableElement writeMethodDeclaration) {
        if (readMethodDeclaration == null) {
            String expectedReadMethodName =
                FieldDeclarationUtil.getReadMethodName(fieldElement);
            if (classElement.equals(fieldElement.getEnclosingElement())) {
                throw new ValidationException(
                    MessageCode.SLIM3GEN1011,
                    fieldElement,
                    expectedReadMethodName);
            }
            throw new ValidationException(
                MessageCode.SLIM3GEN1024,
                classElement,
                expectedReadMethodName,
                fieldElement.getSimpleName(),
                fieldElement.getEnclosingElement().toString());
        }
        if (writeMethodDeclaration != null) {
            String fieldDefinition =
                String.format(
                    "%1$s %2$s = new %1$s(%3$s.class, \"xxx\", this);",
                    fieldElement.asType(),
                    fieldElement.getSimpleName(),
                    inverseModelRefType.getReferenceModelClassName());
            if (classElement.equals(fieldElement.getEnclosingElement())) {
                throw new ValidationException(
                    MessageCode.SLIM3GEN1039,
                    writeMethodDeclaration,
                    fieldElement.getSimpleName(),
                    fieldDefinition,
                    classElement.getSimpleName(),
                    inverseModelRefType.getReferenceModelClassName());
            }
            throw new ValidationException(
                MessageCode.SLIM3GEN1040,
                classElement,
                fieldElement.getSimpleName(),
                fieldElement.getEnclosingElement().toString(),
                fieldDefinition,
                classElement.getSimpleName(),
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
     * @param fieldElement
     *            the field declaration
     * @return {@code true} if method is getter method
     */
    protected boolean isReadMethod(ExecutableElement m,
            AttributeMetaDesc attributeMetaDesc, VariableElement fieldElement) {
        if (TypeUtil.isVoid(m.getReturnType())
            || m.getParameters().size() != 0
            || !processingEnv.getTypeUtils().isSameType(
                m.getReturnType(),
                fieldElement.asType())) {
            return false;
        }
        String methodName = m.getSimpleName().toString();
        for (String candidateReadMethodName : FieldDeclarationUtil
            .getReadMethodNames(fieldElement)) {
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
     * @param fieldElement
     *            the field type
     * @return {@code true} if method is setter method
     */
    protected boolean isWriteMethod(ExecutableElement m,
            AttributeMetaDesc attributeMetaDesc, VariableElement fieldElement) {
        String methodName = m.getSimpleName().toString();
        if (!methodName.startsWith("set")
            || !TypeUtil.isVoid(m.getReturnType())
            || m.getParameters().size() != 1) {
            return false;
        }
        TypeMirror parameterTypeMirror =
            m.getParameters().iterator().next().asType();
        if (!processingEnv.getTypeUtils().isSameType(
            parameterTypeMirror,
            fieldElement.asType())) {
            return false;
        }
        return methodName.equals(FieldDeclarationUtil
            .getWriteMethodName(fieldElement));
    }

    /**
     * Creates a data type factory.
     * 
     * @return a data type factory
     */
    protected DataTypeFactory creaetDataTypeFactory() {
        return new DataTypeFactory(processingEnv, roundEnv);
    }

    /**
     * Throws {@link ValidationException} for non core type.
     * 
     * @param classElement
     *            the model class declaration
     * @param fieldElement
     *            the field declaration
     * @param attribute
     *            the annotation mirror for Attribute
     */
    protected void throwExceptionForNonCoreType(TypeElement classElement,
            VariableElement fieldElement, Attribute attribute) {
        if (classElement.equals(fieldElement.getEnclosingElement())) {
            throw new ValidationException(
                MessageCode.SLIM3GEN1005,
                fieldElement);
        }
        throw new ValidationException(
            MessageCode.SLIM3GEN1026,
            classElement,
            fieldElement.getSimpleName(),
            fieldElement.getEnclosingElement().toString());
    }

    /**
     * Throws {@link ValidationException} for conflicted annotation elements.
     * 
     * @param classElement
     *            the model class declaration
     * @param fieldElement
     *            the field declaration
     * @param attribute
     *            the annotation mirror for Attribute
     * @param element1
     *            conflicted element
     * @param element2
     *            conflicted element
     */
    protected void throwExceptionForConflictedElements(
            TypeElement classElement, VariableElement fieldElement,
            Attribute attribute, String element1, String element2) {
        if (classElement.equals(fieldElement.getEnclosingElement())) {
            throw new ValidationException(
                MessageCode.SLIM3GEN1021,
                fieldElement,
                element1,
                element2);
        }
        throw new ValidationException(
            MessageCode.SLIM3GEN1027,
            classElement,
            element1,
            element2,
            fieldElement.getSimpleName(),
            fieldElement.getEnclosingElement().toString());
    }

    /**
     * Throws {@link ValidationException} for Lob unsupported type.
     * 
     * @param classElement
     *            the model class declaration
     * @param fieldElement
     *            the field declaration
     * @param attribute
     *            the annotation mirror for Attribute
     */
    protected void throwExceptionForLobUnsupportedType(
            TypeElement classElement, VariableElement fieldElement,
            Attribute attribute) {
        if (classElement.equals(fieldElement.getEnclosingElement())) {
            throw new ValidationException(
                MessageCode.SLIM3GEN1045,
                fieldElement);
        }
        throw new ValidationException(
            MessageCode.SLIM3GEN1046,
            classElement,
            fieldElement.getSimpleName(),
            fieldElement.getEnclosingElement().toString());
    }

    /**
     * hrows {@link ValidationException} for ModelRef type argument.
     * 
     * @param classElement
     *            the model class declaration
     * @param fieldElement
     *            the field declaration
     */
    protected void throwExceptionForModelRefTypeArgument(
            TypeElement classElement, VariableElement fieldElement) {
        if (classElement.equals(fieldElement.getEnclosingElement())) {
            throw new ValidationException(
                MessageCode.SLIM3GEN1031,
                fieldElement);
        }
        throw new ValidationException(
            MessageCode.SLIM3GEN1032,
            classElement,
            fieldElement.getSimpleName(),
            fieldElement.getEnclosingElement().toString());
    }
}
