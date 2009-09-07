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

import org.slim3.gen.Constants;
import org.slim3.gen.desc.ModelDesc;
import org.slim3.gen.printer.Printer;
import org.slim3.gen.util.ClassUtil;
import org.slim3.gen.util.StringUtil;

/**
 * Generates a JDO model test case java file.
 * 
 * @author taedium
 * @since 3.0
 * 
 */
public class ModelTestCaseGenerator implements Generator {

	/** the model description */
	protected final ModelDesc modelDesc;

	/**
	 * Creates a new {@link ModelTestCaseGenerator}.
	 * 
	 * @param modelDesc
	 *            the model description
	 */
	public ModelTestCaseGenerator(ModelDesc modelDesc) {
		if (modelDesc == null) {
			throw new NullPointerException("The modelDesc parameter is null.");
		}
		this.modelDesc = modelDesc;
	}

	public void generate(Printer p) {
		if (modelDesc.getPackageName().length() != 0) {
			p.println("package %s;", modelDesc.getPackageName());
			p.println();
		}
		p.println("import %s;", modelDesc.getTestCaseSuperclassName());
		p.println();
		p.println("public class %s%s extends %s {", modelDesc.getSimpleName(),
				Constants.TEST_SUFFIX, ClassUtil.getSimpleName(modelDesc
						.getTestCaseSuperclassName()));
		p.println();
		p.println("    public void test() throws Exception {");
		p.println("        %1$s %2$s = new %1$s();", modelDesc.getSimpleName(),
				StringUtil.decapitalize(modelDesc.getSimpleName()));
		p.println("        assertNotNull(%s);", StringUtil
				.decapitalize(modelDesc.getSimpleName()));
		p.println("    }");
		p.println("}");
	}
}
