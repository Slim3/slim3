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
package org.slim3.gen.task;

import java.io.File;
import java.io.IOException;

import org.slim3.gen.desc.ViewDesc;
import org.slim3.gen.desc.ViewDescFactory;
import org.slim3.gen.generator.Generator;
import org.slim3.gen.generator.ViewGenerator;

/**
 * Represents a task to generate a view file.
 * 
 * @author taedium
 * @since 1.0.0
 */
public class GenViewTask extends AbstractGenFileTask {

    /** the controller path */
    protected String controllerPath;

    /**
     * Sets the controllerPath.
     * 
     * @param controllerPath
     *            the controllerPath to set
     */
    public void setControllerPath(String controllerPath) {
        this.controllerPath = controllerPath;
    }

    @Override
    public void doExecute() throws Exception {
        super.doExecute();
        if (controllerPath == null) {
            throw new IllegalStateException(
                "The controllerPath parameter is null.");
        }
        ViewDesc viewDesc = createViewDesc();
        generateView(viewDesc);
    }

    /**
     * Creates a view description.
     * 
     * @return a view description
     */
    private ViewDesc createViewDesc() {
        String path =
            controllerPath.startsWith("/") ? controllerPath : "/"
                + controllerPath;
        ViewDescFactory viewDescFactory = createViewDescFactory();
        ViewDesc viewDesc = viewDescFactory.createViewDesc(path);
        return viewDesc;
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
        Generator generator = careateViewGenerator(viewDesc);
        generateFile(generator, viewFile);
    }

    /**
     * Creates a {@link Generator}.
     * 
     * @param viewDesc
     *            the view description.
     * @return a generator
     */
    protected Generator careateViewGenerator(ViewDesc viewDesc) {
        return new ViewGenerator(viewDesc);
    }

}
