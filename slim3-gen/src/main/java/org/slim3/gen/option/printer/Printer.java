package org.slim3.gen.option.printer;

public interface Printer {

    void print(String format, Object... args);

    void println(String format, Object... args);

    public void println();

    public void close();
}
