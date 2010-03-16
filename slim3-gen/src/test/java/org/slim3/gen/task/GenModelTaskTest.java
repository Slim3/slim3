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

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.slim3.gen.task.GenModelTask.ModelDef;

/**
 * @author taedium
 * 
 */
public class GenModelTaskTest {

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testParse() throws Exception {
        GenModelTask task = new GenModelTask();
        ModelDef parsedText = task.parse("Hoge extends Foo");
        assertThat(parsedText.modelRelativeClassName, is("Hoge"));
        assertThat(parsedText.modelRelativeSuperclassName, is("Foo"));
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testIllegalKeyword() throws Exception {
        GenModelTask task = new GenModelTask();
        try {
            task.parse("Hoge implements Foo");
            fail();
        } catch (RuntimeException expected) {
            System.out.println(expected.getMessage());
        }
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testEmptyToken() throws Exception {
        GenModelTask task = new GenModelTask();
        try {
            task.parse("");
            fail();
        } catch (RuntimeException expected) {
            System.out.println(expected.getMessage());
        }
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testTooManyTokens() throws Exception {
        GenModelTask task = new GenModelTask();
        try {
            task.parse("AAA BBB CCC DDD");
            fail();
        } catch (RuntimeException expected) {
            System.out.println(expected.getMessage());
        }
    }
}
