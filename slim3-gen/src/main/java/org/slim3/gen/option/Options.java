package org.slim3.gen.option;

import javax.annotation.processing.ProcessingEnvironment;

public final class Options {

    public static final String DEBUG = "debug";

    public static final String DATEPATTERN = "datepattern";

    public static final String META_SUFFIX = "meta.suffix";

    public static boolean isDebugEnabled(ProcessingEnvironment env) {
        String debug = env.getOptions().get(Options.DEBUG);
        return Boolean.valueOf(debug).booleanValue();
    }

    public static String getDatepattern(ProcessingEnvironment env) {
        return env.getOptions().get(Options.DATEPATTERN);
    }

    public static String getMetaSuffix(ProcessingEnvironment env) {
        String suffix = env.getOptions().get(Options.META_SUFFIX);
        return suffix != null ? suffix : "Meta";
    }

}
