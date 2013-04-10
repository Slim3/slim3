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
package org.slim3.gen.util;

import java.util.regex.Pattern;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;


/**
 * A utility class for {@link VariableElement}.
 * 
 * @author taedium
 * 
 */
public final class FieldDeclarationUtil {

    private static Pattern isPrefixedFieldPattern = Pattern
        .compile("^is[A-Z].*");

    /** the processing environment */
    @SuppressWarnings("unused")
    private static ProcessingEnvironment processingEnv;

    /**
     * @param processingEnv
     */
    public static void init(ProcessingEnvironment processingEnv) {
        FieldDeclarationUtil.processingEnv = processingEnv;
    }

    /**
     * Get the read method name.
     * 
     * @param fieldElement
     *            the field declaration
     * @return the read method name
     */
    public static String getReadMethodName(VariableElement fieldElement) {
        if (fieldElement == null) {
            throw new NullPointerException(
                "The fieldDeclaration parameter is null.");
        }
        return getReadMethodNames(fieldElement)[0];
    }

    /**
     * Get the read method names.
     * 
     * @param fieldElement
     *            the field declaration
     * @return the read method names
     */
    public static String[] getReadMethodNames(VariableElement fieldElement) {
        if (fieldElement == null) {
            throw new NullPointerException(
                "The fieldDeclaration parameter is null.");
        }
        String fieldName = fieldElement.getSimpleName().toString();
        if (TypeUtil.isPrimitive(fieldElement.asType(), TypeKind.BOOLEAN)) {
            if (isPrefixedFieldPattern.matcher(fieldName).matches()) {
                return new String[] { fieldName };
            }
            String capitalized = StringUtil.capitalize(fieldName);
            return new String[] { "is" + capitalized, "get" + capitalized };
        }
        return new String[] { "get" + StringUtil.capitalize(fieldName) };
    }

    /**
     * Get the write method name.
     * 
     * @param fieldElement
     *            the field declaration
     * @return the write method name
     */
    public static String getWriteMethodName(VariableElement fieldElement) {
        if (fieldElement == null) {
            throw new NullPointerException(
                "The fieldDeclaration parameter is null.");
        }
        String fieldName = fieldElement.getSimpleName().toString();
        if (TypeUtil.isPrimitive(fieldElement.asType(), TypeKind.BOOLEAN)) {
            if (isPrefixedFieldPattern.matcher(fieldName).matches()) {
                return "set" + fieldName.substring(2);
            }
        }
        return "set" + StringUtil.capitalize(fieldName);
    }

    /**
     * Returns JavaBeans property name.
     * 
     * @param fieldElement
     *            the field declaration
     * @return JavaBeans property name
     */
    public static String getPropertyName(VariableElement fieldElement) {
        if (fieldElement == null) {
            throw new NullPointerException(
                "The fieldDeclaration parameter is null.");
        }
        String fieldName = fieldElement.getSimpleName().toString();
        if (TypeUtil.isPrimitive(fieldElement.asType(), TypeKind.BOOLEAN)) {
            if (isPrefixedFieldPattern.matcher(fieldName).matches()) {
                return StringUtil.decapitalize(fieldName.substring(2));
            }
        }
        return fieldName;
    }
}
