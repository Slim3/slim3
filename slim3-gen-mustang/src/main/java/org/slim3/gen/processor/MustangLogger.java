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

import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic.Kind;

/**
 * Logs messages.
 * 
 * @author taedium
 * @since 3.0
 * 
 */
public final class MustangLogger {

    /**
     * Logs a debug message.
     * 
     * @param env
     *            the processing environment.
     * @param message
     *            the message.
     */
    public static void debug(ProcessingEnvironment env, String message) {
        Messager messager = env.getMessager();
        messager.printMessage(Kind.NOTE, message);
    }

    /**
     * Logs a warning message.
     * 
     * @param env
     *            the processing environment.
     * @param element
     *            the element to use as a position hint
     * @param message
     *            the message.
     */
    public static void warning(ProcessingEnvironment env, Element element,
            String message) {
        Messager messager = env.getMessager();
        messager.printMessage(Kind.WARNING, message, element);
    }

    /**
     * Logs an error message.
     * 
     * @param env
     *            the processing environment.
     * @param element
     *            the element to use as a position hint
     * @param message
     *            the message.
     */
    public static void error(ProcessingEnvironment env, Element element,
            String message) {
        Messager messager = env.getMessager();
        messager.printMessage(Kind.ERROR, message, element);
    }

}
