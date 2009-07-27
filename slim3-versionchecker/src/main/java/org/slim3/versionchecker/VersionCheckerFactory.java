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
package org.slim3.versionchecker;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import com.sun.mirror.apt.AnnotationProcessor;
import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.apt.AnnotationProcessorFactory;
import com.sun.mirror.declaration.AnnotationTypeDeclaration;

/**
 * Creates a java version checker.
 * 
 * @author taedium
 * @since 3.0
 * 
 */
public class VersionCheckerFactory implements AnnotationProcessorFactory {

    private static final Collection<String> supportedAnnotations =
        Collections.unmodifiableCollection(Arrays
            .asList("javax.jdo.annotations.PersistenceCapable"));

    private static final Collection<String> supportedOptions =
        Collections.emptySet();

    public Collection<String> supportedAnnotationTypes() {
        return supportedAnnotations;
    }

    public Collection<String> supportedOptions() {
        return supportedOptions;
    }

    public AnnotationProcessor getProcessorFor(
            Set<AnnotationTypeDeclaration> atds,
            AnnotationProcessorEnvironment env) {
        return new VersionChecker(env);
    }

    private static class VersionChecker implements AnnotationProcessor {
        private final AnnotationProcessorEnvironment env;

        private static final String message =
            "In order to use Slim3 JDO, IDE requires greater than or equal to JDK 1.6. If your IDE is Eclipse, you can specify java version using -vm boot option.";

        private static Double version;

        static {
            version =
                Double
                    .valueOf(System.getProperty("java.specification.version"));
        }

        VersionChecker(AnnotationProcessorEnvironment env) {
            this.env = env;
        }

        public void process() {
            if (version >= 1.6) {
                return;
            }
            env.getMessager().printError(message);
        }
    }

}
