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
package org.slim3.gen.generator;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Generated;

import org.slim3.gen.ClassConstants;
import org.slim3.gen.ProductInfo;
import org.slim3.gen.desc.GWTServiceAsyncDesc;
import org.slim3.gen.desc.GWTServiceAsyncMethodDesc;
import org.slim3.gen.printer.Printer;

/**
 * Generates a GWT asynchronous service java file.
 * 
 * @author taedium
 * @since 3.0
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

    @Override
    public void generate(Printer p) {
        if (!serviceAsyncDesc.getPackageName().isEmpty()) {
            p.println("package %s;", serviceAsyncDesc.getPackageName());
            p.println();
        }
        p.println(
            "@%s(value = { \"%s\", \"%s\" }, date = \"%tF %<tT\")",
            Generated.class.getName(),
            ProductInfo.getName(),
            ProductInfo.getVersion(),
            new Date());
        p.print("public interface %s", serviceAsyncDesc.getSimpleName());
        if (!serviceAsyncDesc.getTypeParameterNames().isEmpty()) {
            p.print("<");
        }
        for (Iterator<String> it =
            serviceAsyncDesc.getTypeParameterNames().iterator(); it.hasNext();) {
            p.print("%s", it.next());
            if (it.hasNext()) {
                p.print(", ");
            }
        }
        if (!serviceAsyncDesc.getTypeParameterNames().isEmpty()) {
            p.print(">");
        }
        p.println(" {");
        p.println();
        for (GWTServiceAsyncMethodDesc methodDesc : serviceAsyncDesc
            .getServiceAsyncMethodDescs()) {
            p.print("    ");
            if (!methodDesc.getTypeParameterNames().isEmpty()) {
                p.print("<");
            }
            for (Iterator<String> it =
                methodDesc.getTypeParameterNames().iterator(); it.hasNext();) {
                p.print("%s", it.next());
                if (it.hasNext()) {
                    p.print(", ");
                }
            }
            if (!methodDesc.getTypeParameterNames().isEmpty()) {
                p.print("> ");
            }
            p.print("void %s(", methodDesc.getName());
            for (Iterator<Map.Entry<String, String>> it =
                methodDesc.getParameterNames().entrySet().iterator(); it
                .hasNext();) {
                Entry<String, String> entry = it.next();
                p.print("%s %s, ", entry.getValue(), entry.getKey());
            }
            p.print(
                "%s<%s> callback)",
                ClassConstants.AsyncCallback,
                methodDesc.getReturnTypeName());
            if (!methodDesc.getThrownTypeNames().isEmpty()) {
                p.print(" throws ");
            }
            for (Iterator<String> it =
                methodDesc.getThrownTypeNames().iterator(); it.hasNext();) {
                p.print("%s", it.next());
                if (it.hasNext()) {
                    p.print(", ");
                }
            }
            p.println(";");
            p.println();
        }
        p.print("}");
    }

}
