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

import org.slim3.gen.desc.ViewDesc;
import org.slim3.gen.printer.Printer;

/**
 * Generates a view file.
 * 
 * @author taedium
 * @since 1.0.0
 * 
 */
public class ViewGenerator implements Generator {

    /** the view description */
    protected final ViewDesc viewDesc;

    /**
     * Creates a new {@link ViewGenerator}.
     * 
     * @param viewDesc
     *            the view description
     */
    public ViewGenerator(ViewDesc viewDesc) {
        this.viewDesc = viewDesc;
    }

    public void generate(Printer p) {
        p
            .println("<%%@page pageEncoding=\"UTF-8\" isELIgnored=\"false\" session=\"false\"%%>");
        p
            .println("<%%@taglib prefix=\"c\" uri=\"http://java.sun.com/jsp/jstl/core\"%%>");
        p
            .println("<%%@taglib prefix=\"fn\" uri=\"http://java.sun.com/jsp/jstl/functions\"%%>");
        p
            .println("<%%@taglib prefix=\"f\" uri=\"http://www.slim3.org/functions\"%%>");
        p.println();
        p.println("<html>");
        p.println("<head>");
        p
            .println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />");
        p.println("<title>%s</title>", viewDesc.getTitle());
        p.println("</head>");
        p.println("<body>");
        p.println("<p>Hello %s !!!</p>", viewDesc.getTitle());
        p.println("</body>");
        p.println("</html>");
    }
}
