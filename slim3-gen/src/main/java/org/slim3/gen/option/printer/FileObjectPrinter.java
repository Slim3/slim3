package org.slim3.gen.option.printer;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Formatter;

import javax.tools.FileObject;

public class FileObjectPrinter implements Printer {

    protected final Formatter formatter;

    public FileObjectPrinter(FileObject fileObject) throws IOException {
        formatter = new Formatter(new BufferedWriter(fileObject.openWriter()));
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
