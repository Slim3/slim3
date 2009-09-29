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
package org.slim3.gen.printer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.Formatter;

/**
 * Prints format strings to a file.
 * 
 * @author taedium
 * @since 3.0
 * 
 */
public class FilePrinter implements Printer {

    /** the indent space */
    protected static final String INDENT_SPACE = "    ";

    /** the indent */
    protected StringBuilder indent = new StringBuilder();

    /** the formatter object */
    protected final Formatter formatter;

    /**
     * Creates a new {@link FilePrinter}.
     * 
     * @param writer
     *            the writer
     * @throws IOException
     *             if an I/O error occurred
     */
    public FilePrinter(Writer writer) throws IOException {
        if (writer == null) {
            throw new NullPointerException("The writer parameter is null.");
        }
        formatter = new Formatter(new BufferedWriter(writer));
    }

    /**
     * Creates a new {@link FilePrinter}.
     * 
     * @param file
     *            the file
     * @param encoding
     *            the encoding
     * @throws IOException
     *             if an I/O error occurred
     */
    public FilePrinter(File file, String encoding) throws IOException {
        if (file == null) {
            throw new NullPointerException("The file parameter is null.");
        }
        if (encoding == null) {
            throw new NullPointerException("The encoding parameter is null.");
        }
        formatter = new Formatter(file, encoding);
    }

    public void print(String format, Object... args) {
        formatter.format(indent + format, args);
    }

    public void println(String format, Object... args) {
        formatter.format(indent + format, args);
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
}
