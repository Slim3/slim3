package org.slim3.gen.generator;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.Formatter;

import javax.tools.JavaFileObject;

public class Printer {

    protected final Formatter formatter;

    public Printer(JavaFileObject javaFileObject) throws IOException {
        this.formatter = new Formatter(new BufferedOutputStream(javaFileObject
                .openOutputStream()));
    }

    public void print(String format, Object... args) {
        formatter.format(format, args);
    }

    public void println(String format, Object... args) {
        formatter.format(format, args);
        formatter.format("\n");
    }

    public void println() {
        formatter.format("\n");
    }

    public void close() {
        formatter.close();
    }
}
