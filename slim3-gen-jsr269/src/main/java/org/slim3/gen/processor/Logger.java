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

import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic;

/**
 * Logs messages.
 * 
 * @author taedium
 * @since 1.0.0
 * 
 */
public final class Logger {

    static Messager messager = null;

    /**
     * Initialization.
     * 
     * @param messager
     * @author vvakame
     */
    public static void init(Messager messager) {
        Logger.messager = messager;
    }

    /**
     * Logs a debug message.
     * 
     * @param message
     *            the message.
     */
    public static void debug(String message) {
        messager.printMessage(Diagnostic.Kind.NOTE, message);
    }

    /**
     * Logs a warning message.
     * 
     * @param element
     *            the element.
     * @param message
     *            the message.
     */
    public static void warning(Element element, String message) {
        messager.printMessage(Diagnostic.Kind.WARNING, message, element);
    }

    /**
     * Logs an error message.
     * 
     * @param element
     *            the element.
     * @param message
     *            the message.
     */
    public static void error(Element element, String message) {
        messager.printMessage(Diagnostic.Kind.ERROR, message, element);
    }

}
