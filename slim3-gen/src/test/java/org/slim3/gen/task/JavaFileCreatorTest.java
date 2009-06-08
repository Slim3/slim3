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

import junit.framework.TestCase;

/**
 * @author taedium
 * 
 */
public class JavaFileCreatorTest extends TestCase {

    /**
     * 
     * @throws Exception
     */
    public void testCreateJavaFile() throws Exception {
        JavaFileCreator creator =
            new JavaFileCreator(new File("src"), new File("test"), "aaa", "Bbb") {

                @Override
                protected void mkdirs(File dir) {
                }
            };
        File expected = new File(new File("src", "aaa"), "Bbb.java");
        assertEquals(expected, creator.createJavaFile());
    }

    /**
     * 
     * @throws Exception
     */
    public void testCreateJavaFile_defaultPackage() throws Exception {
        JavaFileCreator creator =
            new JavaFileCreator(new File("src"), new File("test"), null, "Bbb") {

                @Override
                protected void mkdirs(File dir) {
                }
            };
        File expected = new File("src", "Bbb.java");
        assertEquals(expected, creator.createJavaFile());
    }

    /**
     * 
     * @throws Exception
     */
    public void testCreateTestCaseJavaFile() throws Exception {
        JavaFileCreator creator =
            new JavaFileCreator(new File("src"), new File("test"), "aaa", "Bbb") {

                @Override
                protected void mkdirs(File dir) {
                }
            };
        File expected = new File(new File("test", "aaa"), "BbbTest.java");
        assertEquals(expected, creator.createTestCaseJavaFile());
    }

    /**
     * 
     * @throws Exception
     */
    public void testCreateTestCaseJavaFile_defaultPackage() throws Exception {
        JavaFileCreator creator =
            new JavaFileCreator(new File("src"), new File("test"), null, "Bbb") {

                @Override
                protected void mkdirs(File dir) {
                }
            };
        File expected = new File("test", "BbbTest.java");
        assertEquals(expected, creator.createTestCaseJavaFile());
    }
}
