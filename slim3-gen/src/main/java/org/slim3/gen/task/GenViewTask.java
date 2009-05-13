/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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

import org.slim3.gen.Constants;
import org.slim3.gen.generator.Generator;
import org.slim3.gen.generator.ViewGenerator;

/**
 * @author taedium
 * 
 */
public class GenViewTask extends AbstractTask {

    protected File warDir;

    protected String controllerPath;

    protected String encoding = "UTF-8";

    public void setWarDir(File warDir) {
        this.warDir = warDir;
    }

    public void setControllerPath(String controllerPath) {
        this.controllerPath = controllerPath;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

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
            generateView(path);
        } catch (Exception e) {
            StringWriter writer = new StringWriter();
            e.printStackTrace(new PrintWriter(writer));
            throw new RuntimeException(writer.toString());
        }
    }

    protected void generateView(String path) throws IOException {
        int pos = path.lastIndexOf("/");
        String dirName = path.substring(0, pos);
        String fileName = path.substring(pos + 1);
        if (fileName.length() == 0) {
            fileName = Constants.INDEX_VIEW;
        } else {
            fileName += Constants.VIEW_SUFFIX;
        }

        File viewDir = new File(warDir, dirName);
        viewDir.mkdirs();
        File viewFile = new File(viewDir, fileName);
        if (viewFile.exists()) {
            return;
        }
        Generator generator = careateViewGenerator();
        generate(generator, viewFile);
    }

    protected Generator careateViewGenerator() {
        return new ViewGenerator();
    }

}
