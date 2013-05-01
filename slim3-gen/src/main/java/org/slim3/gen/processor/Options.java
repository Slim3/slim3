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

import org.slim3.gen.Constants;

import com.sun.mirror.apt.AnnotationProcessor;
import com.sun.mirror.apt.AnnotationProcessorEnvironment;

/**
 * Represents options for {@link AnnotationProcessor}.
 * 
 * @author taedium
 * @since 1.0.0
 */
@SuppressWarnings("deprecation")
public final class Options {

    private static final double javaVersion = getJavaVersion();

    /** debug option */
    public static final String DEBUG = "debug";

    /** debug option */
    public static final String JAVA_VERSION = "java.version";

    /** the model package */
    public static final String MODEL_PACKAGE = "model.package";

    /** the meta package */
    public static final String META_PACKAGE = "meta.package";

    /** the shared package */
    public static final String SHARED_PACKAGE = "shared.package";

    /** the server package */
    public static final String SERVER_PACKAGE = "server.package";

    private static double getJavaVersion() {
        return Double.valueOf(System.getProperty("java.specification.version"));
    }

    /**
     * Returns {@code true} if debug enabled otherwirse {@code false}.
     * 
     * @param env
     *            the environment.
     * @return {@code true} if the debug option enabled otherwirse {@code false}
     *         .
     */
    public static boolean isDebugEnabled(AnnotationProcessorEnvironment env) {
        String debug = env.getOptions().get(Options.DEBUG);
        if (debug == null) {
            return false;
        }
        return Boolean.valueOf(debug).booleanValue();
    }

    /**
     * Returns the java version.
     * 
     * @param env
     *            the environment.
     * @return java version
     */
    public static double getJavaVersion(AnnotationProcessorEnvironment env) {
        String version = env.getOptions().get(Options.JAVA_VERSION);
        if (version != null) {
            try {
                return Double.valueOf(version);
            } catch (NumberFormatException ignored) {
            }
        }
        return javaVersion;
    }

    /**
     * Returns the model package.
     * 
     * @param env
     *            the environment.
     * @return the model package.
     */
    public static String getModelPackage(AnnotationProcessorEnvironment env) {
        String modelPackage = env.getOptions().get(Options.MODEL_PACKAGE);
        return modelPackage != null ? modelPackage : Constants.MODEL_PACKAGE;
    }

    /**
     * Returns the meta package.
     * 
     * @param env
     *            the environment.
     * @return the meta package.
     */
    public static String getMetaPackage(AnnotationProcessorEnvironment env) {
        String metaPackage = env.getOptions().get(Options.META_PACKAGE);
        return metaPackage != null ? metaPackage : Constants.META_PACKAGE;
    }

    /**
     * Returns the shared package.
     * 
     * @param env
     *            the environment.
     * @return the shared package.
     */
    public static String getSharedPackage(AnnotationProcessorEnvironment env) {
        String sharedPackage = env.getOptions().get(Options.SHARED_PACKAGE);
        return sharedPackage != null ? sharedPackage : Constants.SHARED_PACKAGE;
    }

    /**
     * Returns the server package.
     * 
     * @param env
     *            the environment.
     * @return the server package.
     */
    public static String getServerPackage(AnnotationProcessorEnvironment env) {
        String serverPackage = env.getOptions().get(Options.SERVER_PACKAGE);
        return serverPackage != null ? serverPackage : Constants.SERVER_PACKAGE;
    }
}
