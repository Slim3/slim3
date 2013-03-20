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
package org.slim3.jsr269.processor;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Test;
import org.seasar.aptina.unit.AptinaTestCase;
import org.slim3.test.model.BasicModel;

/**
 * @author vvakame
 * 
 */
@Ignore("Not implemented")
public class ModelProcessorTest extends AptinaTestCase {

    /**
     * Test for generate Meta class of {@link BasicModel}.
     * 
     * @throws Exception
     */
    @Test
    public void testForBasic() throws Exception {
        ModelProcessor processor = new ModelProcessor();
        addProcessor(processor);

        addCompilationUnit(BasicModel.class);

        compile();
        {
            @SuppressWarnings("unused")
            String source =
                getGeneratedSource(BasicModel.class.getName() + "Meta");
        }
        assertThat(getCompiledResult(), is(true));
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        addSourcePath("src/test/java");
        setCharset("utf-8");
    }
}
