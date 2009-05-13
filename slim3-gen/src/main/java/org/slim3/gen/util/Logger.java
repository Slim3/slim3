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

import java.util.Formatter;

import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic.Kind;

/**
 * Logs messages.
 * 
 * @author taedium
 * @since 3.0
 * 
 */
public final class Logger {

    /**
     * Logs a debug message.
     * 
     * @param env
     *            the processing environment.
     * @param format
     *            the format string.
     * @param args
     *            arguments referenced by the format specifiers in the format
     *            string.
     * @see Formatter
     */
    public static void debug(ProcessingEnvironment env, String format,
            Object... args) {
        String msg = String.format(format, args);
        Messager messager = env.getMessager();
        messager.printMessage(Kind.NOTE, msg);
    }

    /**
     * Logs a warning message.
     * 
     * @param env
     *            the processing environment.
     * @param element
     *            the element to use as a position hint
     * @param format
     *            the format string.
     * @param args
     *            arguments referenced by the format specifiers in the format
     *            string.
     * @see Formatter
     */
    public static void warn(ProcessingEnvironment env, Element element,
            String format, Object... args) {
        String msg = String.format(format, args);
        Messager messager = env.getMessager();
        messager.printMessage(Kind.WARNING, msg, element);
    }

    /**
     * Logs an error message.
     * 
     * @param env
     *            the processing environment.
     * @param element
     *            the element to use as a position hint
     * @param format
     *            the format string.
     * @param args
     *            arguments referenced by the format specifiers in the format
     *            string.
     * @see Formatter
     */
    public static void error(ProcessingEnvironment env, Element element,
            String format, Object... args) {
        String msg = String.format(format, args);
        Messager messager = env.getMessager();
        messager.printMessage(Kind.ERROR, msg, element);
    }

    /**
     * Logs an error message.
     * 
     * @param env
     *            the processing environment.
     * @param element
     *            the annotated element
     * @param annotationMirror
     *            the annotation to use as a position hint
     * @param format
     *            the format string.
     * @param args
     *            arguments referenced by the format specifiers in the format
     *            string.
     * @see Formatter
     */
    public static void error(ProcessingEnvironment env, Element element,
            AnnotationMirror annotationMirror, String format, Object... args) {
        String msg = String.format(format, args);
        Messager messager = env.getMessager();
        messager.printMessage(Kind.ERROR, msg, element, annotationMirror);
    }

    /**
     * Logs an error message.
     * 
     * @param env
     *            the processing environment.
     * @param element
     *            the annotated element
     * @param annotationMirror
     *            the annotation containing the annotation value
     * @param annotationValue
     *            the annotation value to use as a position hint
     * @param format
     *            the format string.
     * @param args
     *            arguments referenced by the format specifiers in the format
     *            string.
     * @see Formatter
     */
    public static void error(ProcessingEnvironment env, Element element,
            AnnotationMirror annotationMirror, AnnotationValue annotationValue,
            String format, Object... args) {
        String msg = String.format(format, args);
        Messager messager = env.getMessager();
        messager.printMessage(Kind.ERROR, msg, element, annotationMirror,
                annotationValue);
    }
}
