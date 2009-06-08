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

import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

/**
 * An abstract class for Ant tasks.
 * 
 * @author taedium
 * @since 3.0
 * 
 */
public abstract class AbstractTask extends Task {

    /**
     * Executes this task.
     */
    @Override
    public void execute() {
        try {
            doExecute();
        } catch (Exception e) {
            StringWriter writer = new StringWriter();
            e.printStackTrace(new PrintWriter(writer));
            throw new BuildException(writer.toString());
        }
    }

    /**
     * Executes this task.
     * 
     * @throws Exception
     */
    protected abstract void doExecute() throws Exception;

}