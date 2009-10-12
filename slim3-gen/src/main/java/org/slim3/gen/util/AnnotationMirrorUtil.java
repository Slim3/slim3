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

import java.util.Iterator;
import java.util.Map;

import com.sun.mirror.declaration.AnnotationMirror;
import com.sun.mirror.declaration.AnnotationTypeElementDeclaration;
import com.sun.mirror.declaration.AnnotationValue;

/**
 * A utility class for annotation mirror.
 * 
 * @author higa
 * @since 3.0
 * 
 */
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
}