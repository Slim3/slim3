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
package org.slim3.gen.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.sun.mirror.declaration.AnnotationMirror;
import com.sun.mirror.declaration.AnnotationTypeDeclaration;
import com.sun.mirror.declaration.AnnotationTypeElementDeclaration;
import com.sun.mirror.declaration.AnnotationValue;
import com.sun.mirror.type.AnnotationType;

/**
 * A utility class for annotation mirror.
 * 
 * @author higa
 * @since 1.0.0
 * 
 */
@SuppressWarnings("deprecation")
public final class AnnotationMirrorUtil {

    /**
     * Returns a value of the element.
     * 
     * @param <T>
     *            the value type
     * @param anno
     *            the annotation mirror
     * @param name
     *            the element name
     * 
     * @return a value of the element
     */
    @SuppressWarnings("unchecked")
    public static <T> T getElementValue(AnnotationMirror anno, String name) {
        if (anno == null) {
            return null;
        }
        if (name == null) {
            return null;
        }
        Map<AnnotationTypeElementDeclaration, AnnotationValue> elementValues =
            anno.getElementValues();
        for (Iterator<AnnotationTypeElementDeclaration> i =
            elementValues.keySet().iterator(); i.hasNext();) {
            AnnotationTypeElementDeclaration element = i.next();
            if (element.getSimpleName().equals(name)) {
                AnnotationValue v = elementValues.get(element);
                return (T) (v != null ? v.getValue() : null);
            }
        }
        return null;
    }

    /**
     * Returns a value of the element including default.
     * 
     * @param <T>
     *            the value type
     * @param anno
     *            the annotation mirror
     * @param name
     *            the element name
     * 
     * @return a value of the element
     */
    @SuppressWarnings("unchecked")
    public static <T> T getElementValueWithDefault(AnnotationMirror anno,
            String name) {
        if (anno == null) {
            return null;
        }
        if (name == null) {
            return null;
        }
        Map<String, AnnotationValue> elementValues =
            getElementValuesWithDefault(anno);
        for (Iterator<Map.Entry<String, AnnotationValue>> i =
            elementValues.entrySet().iterator(); i.hasNext();) {
            Entry<String, AnnotationValue> entry = i.next();
            String elementName = entry.getKey();
            AnnotationValue value = entry.getValue();
            if (elementName.equals(name)) {
                return (T) (value != null ? value.getValue() : null);
            }
        }
        return null;
    }

    private static Map<String, AnnotationValue> getElementValuesWithDefault(
            AnnotationMirror anno) {
        Map<String, AnnotationValue> results =
            new HashMap<String, AnnotationValue>(getElementDefaultValues(anno));
        for (Iterator<Map.Entry<AnnotationTypeElementDeclaration, AnnotationValue>> i =
            anno.getElementValues().entrySet().iterator(); i.hasNext();) {
            Entry<AnnotationTypeElementDeclaration, AnnotationValue> entry =
                i.next();
            results.put(entry.getKey().getSimpleName(), entry.getValue());
        }
        return results;
    }

    private static Map<String, AnnotationValue> getElementDefaultValues(
            AnnotationMirror anno) {
        Map<String, AnnotationValue> results =
            new HashMap<String, AnnotationValue>();
        AnnotationType annoType = anno.getAnnotationType();
        if (annoType == null) {
            return results;
        }
        AnnotationTypeDeclaration annoTypeDeclaration =
            annoType.getDeclaration();
        if (annoTypeDeclaration == null) {
            return results;
        }
        for (AnnotationTypeElementDeclaration element : annoTypeDeclaration
            .getMethods()) {
            results.put(element.getSimpleName(), element.getDefaultValue());
        }
        return results;
    }
}