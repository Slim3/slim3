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
package org.slim3.gen.generator;

import org.slim3.gen.desc.GWTServiceAsyncDesc;
import org.slim3.gen.printer.Printer;

/**
 * Generates a GWT service async java file.
 * 
 * @author taedium
 * @since 1.0.0
 * 
 */
public class GWTServiceAsyncGenerator implements Generator {

    /** the service async description */
    protected final GWTServiceAsyncDesc serviceAsyncDesc;

    /**
     * Creates a new {@link GWTServiceAsyncGenerator}.
     * 
     * @param serviceAsyncDesc
     *            the service async description
     */
    public GWTServiceAsyncGenerator(GWTServiceAsyncDesc serviceAsyncDesc) {
        if (serviceAsyncDesc == null) {
            throw new NullPointerException(
                "The serviceAsyncDesc parameter is null.");
        }
        this.serviceAsyncDesc = serviceAsyncDesc;
    }

    public void generate(Printer p) {
        if (serviceAsyncDesc.getPackageName().length() != 0) {
            p.println("package %s;", serviceAsyncDesc.getPackageName());
            p.println();
        }
        p.println("public interface %s {", serviceAsyncDesc.getSimpleName());
        p.println();
        p.println("}");
    }

}
