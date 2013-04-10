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
package org.slim3.gen.processor;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.seasar.aptina.unit.AptinaTestCase;
import org.seasar.aptina.unit.SourceNotGeneratedException;
import org.slim3.gen.processor.ModelProcessor;
import org.slim3.test.model.AttributeNotSupportedSampleModel;
import org.slim3.test.model.AttributeParameterSampleModel;
import org.slim3.test.model.BasicModel;
import org.slim3.test.model.AttributeSampleModel;
import org.slim3.test.model.RefAModel;
import org.slim3.test.model.RefBModel;

/**
 * @author vvakame
 * 
 */
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
            String sourceName = "org.slim3.test.meta.BasicModelMeta";
            @SuppressWarnings("unused")
            String source = getGeneratedSource(sourceName);
        }
        assertThat(getCompiledResult(), is(true));
    }

    /**
     * Test for generate Meta class of {@link AttributeSampleModel}.
     * 
     * @throws Exception
     */
    @Test
    public void testForAttributePrimitive() throws Exception {
        ModelProcessor processor = new ModelProcessor();
        addProcessor(processor);

        addCompilationUnit(AttributeSampleModel.class);

        compile();
        {
            String sourceName = "org.slim3.test.meta.AttributeSampleModelMeta";
            @SuppressWarnings("unused")
            String source = getGeneratedSource(sourceName);
        }
        assertThat(getCompiledResult(), is(true));
    }

    /**
     * Test for generate Meta class of {@link AttributeSampleModel}.
     * 
     * @throws Exception
     */
    @Test
    public void testForAttributeNotSupported() throws Exception {
        ModelProcessor processor = new ModelProcessor();
        addProcessor(processor);

        addCompilationUnit(AttributeNotSupportedSampleModel.class);

        compile();
        {
            String sourceName =
                "org.slim3.test.meta.AttributeNotSupportedSampleModelMeta";
            try {
                @SuppressWarnings("unused")
                String source = getGeneratedSource(sourceName);
                fail();
            } catch (SourceNotGeneratedException e) {
            }
        }
    }

    /**
     * Test for generate Meta class of {@link RefAModel} and {@link RefBModel}.
     * 
     * @throws Exception
     */
    @Test
    public void testForRefs() throws Exception {
        ModelProcessor processor = new ModelProcessor();
        addProcessor(processor);

        addCompilationUnit(RefAModel.class);
        addCompilationUnit(RefBModel.class);

        compile();
        {
            String sourceName = "org.slim3.test.meta.RefAModelMeta";
            @SuppressWarnings("unused")
            String source = getGeneratedSource(sourceName);
        }
        {
            String sourceName = "org.slim3.test.meta.RefBModelMeta";
            @SuppressWarnings("unused")
            String source = getGeneratedSource(sourceName);
        }
    }

    /**
     * Test for generate Meta class of {@link AttributeParameterSampleModel}.
     * 
     * @throws Exception
     */
    @Test
    public void testForAttributeParametered() throws Exception {
        ModelProcessor processor = new ModelProcessor();
        addProcessor(processor);

        addCompilationUnit(AttributeParameterSampleModel.class);

        compile();
        {
            String sourceName =
                "org.slim3.test.meta.AttributeParameterSampleModelMeta";
            @SuppressWarnings("unused")
            String source = getGeneratedSource(sourceName);
        }
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        addSourcePath("src/test/java");
        setCharset("utf-8");
    }
}
