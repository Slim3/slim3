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
package org.slim3.gen.printer;

import java.io.Closeable;

/**
 * Prints format strings to a destination.
 * 
 * @author taedium
 * @since 1.0.0
 * 
 */
public interface Printer extends Closeable {

    /**
     * Prints a format string.
     * 
     * @param format
     *            the format string.
     * @param args
     *            arguments referenced by the format specifiers in the format
     *            string.
     */
    void print(String format, Object... args);

    /**
     * Prints a format string without indent.
     * 
     * @param format
     *            the format string.
     * @param args
     *            arguments referenced by the format specifiers in the format
     *            string.
     */
    void printWithoutIndent(String format, Object... args);

    /**
     * Prints a format string and terminates the line.
     * 
     * @param format
     *            the format string.
     * @param args
     *            arguments referenced by the format specifiers in the format
     *            string.
     */
    void println(String format, Object... args);

    /**
     * Prints a format string without indent and terminates the line.
     * 
     * @param format
     *            the format string.
     * @param args
     *            arguments referenced by the format specifiers in the format
     *            string.
     */
    void printlnWithoutIndent(String format, Object... args);

    /**
     * Terminates the current line by writing the line separator string.
     */
    void println();

    /**
     * Indents a line.
     */
    void indent();

    /**
     * Unindents a line.
     */
    void unindent();

    /**
     * Closes the stream and releases any system resources associated with it.
     */
    void close();
}
