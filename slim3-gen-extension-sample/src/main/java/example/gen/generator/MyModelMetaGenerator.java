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
package example.gen.generator;

import org.slim3.gen.desc.ModelMetaDesc;
import org.slim3.gen.generator.ModelMetaGenerator;
import org.slim3.gen.printer.Printer;

/**
 * @author higayasuo
 * 
 */
public class MyModelMetaGenerator extends ModelMetaGenerator {

	/**
	 * @param modelMetaDesc
	 */
	public MyModelMetaGenerator(ModelMetaDesc modelMetaDesc) {
		super(modelMetaDesc);
	}

	@Override
	protected void printClass(Printer printer) {
		printer.println("// This is a test.");
		super.printClass(printer);
	}

}
