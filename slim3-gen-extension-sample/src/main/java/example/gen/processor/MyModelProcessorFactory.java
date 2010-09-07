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
package example.gen.processor;

import java.util.Set;

import org.slim3.gen.processor.ModelProcessorFactory;

import com.sun.mirror.apt.AnnotationProcessor;
import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.declaration.AnnotationTypeDeclaration;

/**
 * @author higayasuo
 * 
 */
public class MyModelProcessorFactory extends ModelProcessorFactory {

	/**
	 * 
	 */
	public MyModelProcessorFactory() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.slim3.gen.processor.ModelProcessorFactory#getProcessorFor(java.util
	 * .Set, com.sun.mirror.apt.AnnotationProcessorEnvironment)
	 */
	@Override
	public AnnotationProcessor getProcessorFor(
			Set<AnnotationTypeDeclaration> annotationTypeDeclarations,
			AnnotationProcessorEnvironment env) {
		return new MyModelProcessor(annotationTypeDeclarations, env);
	}
}