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

import static org.slim3.gen.AnnotationConstants.Model;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.sun.mirror.apt.AnnotationProcessor;
import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.apt.AnnotationProcessorFactory;
import com.sun.mirror.declaration.AnnotationTypeDeclaration;

/**
 * Represents a {@code ModelProcessor} factory.
 * 
 * @author taedium
 * @since 1.0.0
 * 
 */
@SuppressWarnings("deprecation")
public class ModelProcessorFactory implements AnnotationProcessorFactory {

    /** the supported annotations */
    protected static final Collection<String> supportedAnnotations =
        Collections.unmodifiableCollection(Arrays.asList(Model));

    /** the supported options */
    protected static final Collection<String> supportedOptions =
        createSupportedOptions();

    private static Collection<String> createSupportedOptions() {
        Set<String> supportedOptions = new HashSet<String>();
        supportedOptions.add(Options.DEBUG);
        supportedOptions.add(Options.JAVA_VERSION);
        supportedOptions.add(Options.META_PACKAGE);
        supportedOptions.add(Options.MODEL_PACKAGE);
        supportedOptions.add(Options.SERVER_PACKAGE);
        supportedOptions.add(Options.SHARED_PACKAGE);
        return Collections.unmodifiableCollection(supportedOptions);
    }

    public Collection<String> supportedAnnotationTypes() {
        return supportedAnnotations;
    }

    public Collection<String> supportedOptions() {
        return supportedOptions;
    }

    public AnnotationProcessor getProcessorFor(
            Set<AnnotationTypeDeclaration> annotationTypeDeclarations,
            AnnotationProcessorEnvironment env) {
        return new ModelProcessor(annotationTypeDeclarations, env);
    }
}
