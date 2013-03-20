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

import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.apt.Messager;
import com.sun.mirror.declaration.Declaration;
import com.sun.mirror.util.SourcePosition;

/**
 * Logs messages.
 * 
 * @author taedium
 * @since 1.0.0
 * 
 */
@SuppressWarnings("deprecation")
public final class Logger {

    /**
     * Logs a debug message.
     * 
     * @param env
     *            the environment.
     * @param message
     *            the message.
     */
    public static void debug(AnnotationProcessorEnvironment env, String message) {
        Messager messager = env.getMessager();
        messager.printNotice(message);
    }

    /**
     * Logs a warning message.
     * 
     * @param env
     *            the environment.
     * @param declaration
     *            the declaration to use as a position hint
     * @param message
     *            the message.
     */
    public static void warning(AnnotationProcessorEnvironment env,
            Declaration declaration, String message) {
        Messager messager = env.getMessager();
        messager.printWarning(declaration.getPosition(), message);
    }

    /**
     * Logs an error message.
     * 
     * @param env
     *            the environment.
     * @param sourcePosition
     *            the source position.
     * @param message
     *            the message.
     */
    public static void error(AnnotationProcessorEnvironment env,
            SourcePosition sourcePosition, String message) {
        Messager messager = env.getMessager();
        messager.printError(sourcePosition, message);
    }

}
