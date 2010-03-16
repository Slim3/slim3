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

import org.slim3.gen.desc.GWTServiceDesc;
import org.slim3.gen.printer.Printer;

/**
 * Generates a GWT service java file.
 * 
 * @author taedium
 * 
 */
public class GWTServiceGenerator implements Generator {

    /** the service description */
    protected final GWTServiceDesc serviceDesc;

    /**
     * Creates a new {@link GWTServiceGenerator}.
     * 
     * @param serviceDesc
     *            the service description
     */
    public GWTServiceGenerator(GWTServiceDesc serviceDesc) {
        if (serviceDesc == null) {
            throw new NullPointerException("The serviceDesc parameter is null.");
        }
        this.serviceDesc = serviceDesc;
    }

    public void generate(Printer p) {
        if (serviceDesc.getPackageName().length() != 0) {
            p.println("package %s;", serviceDesc.getPackageName());
            p.println();
        }
        p.println("import com.google.gwt.user.client.rpc.RemoteService;");
        p
            .println("import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;");
        p.println();
        p.println("@RemoteServiceRelativePath(\"%s\")", serviceDesc
            .getRemoteServiceRelativePath());
        p.println("public interface %s extends RemoteService {", serviceDesc
            .getSimpleName());
        p.println();
        p.println("}");
    }
}