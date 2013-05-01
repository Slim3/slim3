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
package org.slim3.gen.generator;

import org.junit.Before;
import org.junit.Test;
import org.slim3.gen.ClassConstants;
import org.slim3.gen.desc.DaoDesc;
import org.slim3.gen.printer.ConsolePrinter;
import org.slim3.gen.printer.Printer;


/**
 * @author sue445
 *
 */
public class DaoTestCaseGeneratorTest {

    private DaoTestCaseGenerator generator;

    private Printer printer;


    /**
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception{
        DaoDesc desc = new DaoDesc();
        desc.setPackageName("slim3.dao");
        desc.setSimpleName("SampleDao");
        desc.setModelClassName("slim3.model.Sample");
        desc.setTestCaseSuperclassName(ClassConstants.AppEngineTestCase);

        generator = new DaoTestCaseGenerator(desc);
        printer = new ConsolePrinter();
    }

    /**
     * @throws Exception
     *
     */
    @Test
    public void generate() throws Exception{
        generator.generate(printer);
    }
}
