package org.slim3.gen.option;

import javax.annotation.processing.ProcessingEnvironment;

public final class Options {

    public static final String DEBUG = "debug";

    public static boolean isDebugEnabled(ProcessingEnvironment env) {
        String debug = env.getOptions().get(Options.DEBUG);
        return Boolean.valueOf(debug).booleanValue();
    }

}
