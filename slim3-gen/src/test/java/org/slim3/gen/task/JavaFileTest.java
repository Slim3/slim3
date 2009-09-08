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

import org.slim3.gen.desc.ClassDesc;
import org.slim3.gen.util.ClassUtil;

/**
 * @author taedium
 * 
 */
public class JavaFileTest extends TestCase {

    /**
     * 
     * @throws Exception
     */
    public void test() throws Exception {
        ClassDesc classDesc = new SimpleClassDesc("aaa", "Bbb");
        JavaFile javaFile = new NoMkdirsJavaFile(new File("src"), classDesc);
        File expectedFile = new File(new File("src", "aaa"), "Bbb.java");
        assertEquals(expectedFile, javaFile.getFile());
        assertEquals("aaa.Bbb", javaFile.getClassName());
    }

    /**
     * 
     * @throws Exception
     */
    public void testDefaultPackage() throws Exception {
        ClassDesc classDesc = new SimpleClassDesc(null, "Bbb");
        JavaFile javaFile = new NoMkdirsJavaFile(new File("src"), classDesc);
        File expectedFile = new File("src", "Bbb.java");
        assertEquals(expectedFile, javaFile.getFile());
        assertEquals("Bbb", javaFile.getClassName());
    }

    /**
     * 
     * @throws Exception
     */
    public void testSuffix() throws Exception {
        ClassDesc classDesc = new SimpleClassDesc("aaa", "Bbb");
        JavaFile javaFile =
            new NoMkdirsJavaFile(new File("src"), classDesc, "Test");
        File expectedFile = new File(new File("src", "aaa"), "BbbTest.java");
        assertEquals(expectedFile, javaFile.getFile());
        assertEquals("aaa.BbbTest", javaFile.getClassName());
    }

    /**
     * 
     * @author taedium
     * 
     */
    protected static class NoMkdirsJavaFile extends JavaFile {

        /**
         * 
         * @param baseDir
         * @param classDesc
         */
        public NoMkdirsJavaFile(File baseDir, ClassDesc classDesc) {
            super(baseDir, classDesc);
        }

        /**
         * 
         * @param baseDir
         * @param classDesc
         * @param suffix
         */
        public NoMkdirsJavaFile(File baseDir, ClassDesc classDesc, String suffix) {
            super(baseDir, classDesc, suffix);
        }

        @Override
        protected void mkdirs(File dir) {
        }
    }

    /**
     * 
     * @author taedium
     * 
     */
    protected static class SimpleClassDesc implements ClassDesc {

        private String packageName;

        private String simpleName;

        /**
         * 
         * @param packageName
         * @param simpleName
         */
        public SimpleClassDesc(String packageName, String simpleName) {
            this.packageName = packageName;
            this.simpleName = simpleName;
        }

        public String getPackageName() {
            return packageName;
        }

        public String getSimpleName() {
            return simpleName;
        }

        public String getQualifiedName() {
            return ClassUtil.getQualifiedName(packageName, simpleName);
        }

    }
}
