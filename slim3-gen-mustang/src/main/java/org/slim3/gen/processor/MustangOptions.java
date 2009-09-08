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
package org.slim3.gen.processor;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;

import org.slim3.gen.Constants;

/**
 * Represents options for {@link Processor}.
 * 
 * @author taedium
 * @since 3.0
 * @see Processor#getSupportedOptions()
 */
public final class MustangOptions {

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
     *            the processing environment.
     * @return {@code true} if the debug option enabled otherwirse {@code false}
     *         .
     */
    public static boolean isDebugEnabled(ProcessingEnvironment env) {
        String debug = env.getOptions().get(MustangOptions.DEBUG);
        if (debug == null) {
            return false;
        }
        return Boolean.valueOf(debug).booleanValue();
    }

    /**
     * Returns java version.
     * 
     * @param env
     *            the processing environment.
     * @return the java version
     */
    public static double getJavaVersion(ProcessingEnvironment env) {
        String version = env.getOptions().get(MustangOptions.JAVA_VERSION);
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
     *            the processing environment.
     * @return the model package.
     */
    public static String getModelPackage(ProcessingEnvironment env) {
        String modelPackage =
            env.getOptions().get(MustangOptions.MODEL_PACKAGE);
        return modelPackage != null ? modelPackage : Constants.MODEL_PACKAGE;
    }

    /**
     * Returns the meta package.
     * 
     * @param env
     *            the processing environment.
     * @return the meta package.
     */
    public static String getMetaPackage(ProcessingEnvironment env) {
        String metaPackage = env.getOptions().get(MustangOptions.META_PACKAGE);
        return metaPackage != null ? metaPackage : Constants.META_PACKAGE;
    }

    /**
     * Returns the shared package.
     * 
     * @param env
     *            the processing environment.
     * @return the shared package.
     */
    public static String getSharedPackage(ProcessingEnvironment env) {
        String sharedPackage =
            env.getOptions().get(MustangOptions.SHARED_PACKAGE);
        return sharedPackage != null ? sharedPackage : Constants.SHARED_PACKAGE;
    }

    /**
     * Returns the server package.
     * 
     * @param env
     *            the processing environment.
     * @return the server package.
     */
    public static String getServerPackage(ProcessingEnvironment env) {
        String serverPackage =
            env.getOptions().get(MustangOptions.SERVER_PACKAGE);
        return serverPackage != null ? serverPackage : Constants.SERVER_PACKAGE;
    }
}
