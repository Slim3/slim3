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

import java.util.Formatter;

/**
 * Prints format strings to a console.
 *
 * @author sue445
 * @since 1.0.12
 *
 */
public class ConsolePrinter implements Printer {

    /** the indent space */
    protected static final String INDENT_SPACE = "    ";

    /** the indent */
    protected StringBuilder indent = new StringBuilder();

    /** the formatter object */
    protected final Formatter formatter;

    /**
     * Creates a new {@link ConsolePrinter}.
     */
    public ConsolePrinter() {
        formatter = new Formatter(System.out);
    }

    public void print(String format, Object... args) {
        formatter.format(indent + format, escapse(args));
    }

    public void printWithoutIndent(String format, Object... args) {
        formatter.format(format, escapse(args));
    }

    public void println(String format, Object... args) {
        formatter.format(indent + format, escapse(args));
        formatter.format("%n");
    }

    public void printlnWithoutIndent(String format, Object... args) {
        formatter.format(format, escapse(args));
        formatter.format("%n");
    }

    public void println() {
        formatter.format("%n");
    }

    public void indent() {
        indent.append(INDENT_SPACE);
    }

    public void unindent() {
        if (indent.length() >= INDENT_SPACE.length()) {
            indent.setLength(indent.length() - INDENT_SPACE.length());
        }
    }

    public void close() {
        formatter.close();
    }

    /**
     * Escapes arguments.
     *
     * @param args
     *            arguments
     * @return the escaped arguments
     */
    protected Object[] escapse(Object... args) {
        if (args.length == 0) {
            return args;
        }
        Object[] results = new Object[args.length];
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof String) {
                String text = (String) args[i];
                results[i] = text.replaceAll("\"", "\\\\\"");
            } else {
                results[i] = args[i];
            }
        }
        return results;
    }
}
