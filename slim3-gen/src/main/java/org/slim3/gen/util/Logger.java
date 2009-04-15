package org.slim3.gen.util;

import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.tools.Diagnostic.Kind;

public final class Logger {

    public static void debug(ProcessingEnvironment env, String format,
            Object... args) {
        String msg = String.format(format, args);
        Messager messager = env.getMessager();
        messager.printMessage(Kind.OTHER, msg);
    }

    public static void warn(ProcessingEnvironment env, String format,
            Object... args) {
        String msg = String.format(format, args);
        Messager messager = env.getMessager();
        messager.printMessage(Kind.WARNING, msg);
    }

    public static void error(ProcessingEnvironment env, String format,
            Object... args) {
        String msg = String.format(format, args);
        Messager messager = env.getMessager();
        messager.printMessage(Kind.ERROR, msg);
    }
}
