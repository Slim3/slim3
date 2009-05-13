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

/**
 * Represents options for {@link Processor}.
 * 
 * @author taedium
 * @since 3.0
 * @see Processor#getSupportedOptions()
 */
public final class Options {

    /** debug option */
    public static final String DEBUG = "debug";

    /** validation option */
    public static final String VALIDATION = "validation";

    /**
     * Returns {@code true} if debug enabled otherwirse {@code false}.
     * 
     * @param env
     *            the processing environment.
     * @return {@code true} if the debug option enabled otherwirse {@code false}
     *         .
     */
    public static boolean isDebugEnabled(ProcessingEnvironment env) {
        String debug = env.getOptions().get(Options.DEBUG);
        if (debug == null) {
            return false;
        }
        return Boolean.valueOf(debug).booleanValue();
    }

    /**
     * Returns {@code true} if validation enabled otherwirse {@code false}.
     * 
     * @param env
     *            the processing environment.
     * @return {@code true} if the validation option enabled otherwirse {@code
     *         false} .
     */
    public static boolean isValidationEnabled(ProcessingEnvironment env) {
        String validation = env.getOptions().get(Options.VALIDATION);
        if (validation == null) {
            return true;
        }
        return Boolean.valueOf(validation).booleanValue();
    }

}
