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
package org.slim3.gen.util;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

/**
 * @author taedium
 * 
 */
public class CollectionUtilTest {

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testJoin() throws Exception {
        List<String> list = Arrays.asList("aaa", "bbb", "ccc");
        assertThat(CollectionUtil.join(list, ", "), is("aaa, bbb, ccc"));
    }
}
