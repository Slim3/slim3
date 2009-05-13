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
package org.slim3.gen.task;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.slim3.gen.desc.ViewDesc;
import org.slim3.gen.desc.ViewDescFactory;
import org.slim3.gen.generator.Generator;
import org.slim3.gen.generator.ViewGenerator;

/**
 * Represents a task to generate a view file.
 * 
 * @author taedium
 * @since 3.0
 */
public class GenViewTask extends AbstractTask {

    public void doExecute() {
        try {
            if (warDir == null) {
                throw new IllegalStateException("The warDir parameter is null.");
            }
            if (controllerPath == null) {
                throw new IllegalStateException(
                        "The controllerPath parameter is null.");
            }
            String path = controllerPath.startsWith("/") ? controllerPath : "/"
                    + controllerPath;
            ViewDescFactory viewDescFactory = createViewDescFactory();
            ViewDesc viewDesc = viewDescFactory.createViewDesc(path);
            generateView(viewDesc);
        } catch (Exception e) {
            StringWriter writer = new StringWriter();
            e.printStackTrace(new PrintWriter(writer));
            throw new RuntimeException(writer.toString());
        }
    }

    /**
     * Creates a {@link ViewDescFactory}.
     * 
     * @return a factory
     */
    protected ViewDescFactory createViewDescFactory() {
        return new ViewDescFactory();
    }

    /***
     * Generates a view.
     * 
     * @param viewDesc
     *            the view description.
     * @throws IOException
     */
    protected void generateView(ViewDesc viewDesc) throws IOException {
        File viewDir = new File(warDir, viewDesc.getDirName());
        viewDir.mkdirs();
        File viewFile = new File(viewDir, viewDesc.getFileName());
        if (viewFile.exists()) {
            return;
        }
        Generator generator = careateViewGenerator();
        generate(generator, viewFile);
    }

    /**
     * Creates a {@link Generator}.
     * 
     * @return a generator
     */
    protected Generator careateViewGenerator() {
        return new ViewGenerator();
    }

}
