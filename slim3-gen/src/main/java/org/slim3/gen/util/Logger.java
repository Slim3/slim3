package org.slim3.gen.util;

import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic.Kind;

public final class Logger {

    public static void debug(ProcessingEnvironment env, String format,
            Object... args) {
        String msg = String.format(format, args);
        Messager messager = env.getMessager();
        messager.printMessage(Kind.NOTE, msg);
    }

    public static void warn(ProcessingEnvironment env, Element element,
            String format, Object... args) {
        String msg = String.format(format, args);
        Messager messager = env.getMessager();
        messager.printMessage(Kind.WARNING, msg, element);
    }

    public static void error(ProcessingEnvironment env, Element element,
            String format, Object... args) {
        String msg = String.format(format, args);
        Messager messager = env.getMessager();
        messager.printMessage(Kind.ERROR, msg, element);
    }
}
