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
package org.slim3.gen.processor;

import org.slim3.gen.message.MessageCode;

import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.declaration.AnnotationMirror;
import com.sun.mirror.declaration.Declaration;
import com.sun.mirror.type.TypeMirror;

/**
 * Thrown when unknown declaration object is found.
 * 
 * @author taedium
 * @since 1.0.0
 * 
 */
@SuppressWarnings("deprecation")
public class UnknownDeclarationException extends AptException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a new {@link ValidationException}.
     * 
     * @param env
     *            the environment
     * @param declaration
     *            the send target
     * @param typeMirror
     *            the typemirror corresponding to unknown declaration
     */
    public UnknownDeclarationException(AnnotationProcessorEnvironment env,
            Declaration declaration, TypeMirror typeMirror) {
        super(
            MessageCode.SLIM3GEN1001,
            env,
            declaration.getPosition(),
            typeMirror);
    }

    /**
     * Creates a new {@link ValidationException}.
     * 
     * @param env
     *            the environment
     * @param declaration
     *            the send target
     * @param annotationMirror
     *            the annotationmirror corresponding to unknown declaration
     */
    public UnknownDeclarationException(AnnotationProcessorEnvironment env,
            Declaration declaration, AnnotationMirror annotationMirror) {
        super(
            MessageCode.SLIM3GEN1001,
            env,
            declaration.getPosition(),
            annotationMirror);
    }
}
