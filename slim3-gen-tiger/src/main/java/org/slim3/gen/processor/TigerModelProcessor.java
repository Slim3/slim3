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

import java.util.Set;

import org.slim3.gen.ClassConstants;
import org.slim3.gen.desc.AttributeMetaDescFactory;
import org.slim3.gen.desc.ModelMetaDesc;
import org.slim3.gen.desc.ModelMetaDescFactory;
import org.slim3.gen.generator.Generator;
import org.slim3.gen.generator.ModelMetaGenerator;
import org.slim3.gen.message.MessageCode;
import org.slim3.gen.message.MessageFormatter;

import com.sun.mirror.apt.AnnotationProcessor;
import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.declaration.AnnotationTypeDeclaration;
import com.sun.mirror.declaration.ClassDeclaration;
import com.sun.mirror.util.DeclarationFilter;

/**
 * @author taedium
 * 
 */
public class TigerModelProcessor implements AnnotationProcessor {

	protected final Set<AnnotationTypeDeclaration> atds;

	protected final AnnotationProcessorEnvironment processingEnv;

	/** the support for generating */
	protected final GenerateSupport generateSupport;

	public TigerModelProcessor(Set<AnnotationTypeDeclaration> atds,
			AnnotationProcessorEnvironment processingEnv) {
		this.atds = atds;
		this.processingEnv = processingEnv;
		this.generateSupport = new GenerateSupport(processingEnv);
	}

	public void process() {
		for (AnnotationTypeDeclaration annotation : atds) {
			for (ClassDeclaration element : DeclarationFilter.getFilter(
					ClassDeclaration.class).filter(
					processingEnv.getDeclarationsAnnotatedWith(annotation),
					ClassDeclaration.class)) {
				try {
					handleTypeElement(element);
				} catch (RuntimeException e) {
					TigerLogger.error(processingEnv, element, MessageFormatter
							.getMessage(MessageCode.SILM3GEN0001,
									ClassConstants.PersistenceCapable));
					throw e;
				}
			}
		}
	}

	/**
	 * Handles a type element represents a JDO model class.
	 * 
	 * @param element
	 *            the element represents a JDO model class.
	 */
	protected void handleTypeElement(ClassDeclaration element) {
		if (TigerOptions.isDebugEnabled(processingEnv)) {
			TigerLogger.debug(processingEnv, MessageFormatter.getMessage(
					MessageCode.SILM3GEN0002, element));
		}
		if (element.getDeclaringType() == null) {
			AttributeMetaDescFactory attributeMetaDescFactory = createAttributeMetaDescFactory();
			ModelMetaDescFactory modelMetaDescFactory = createModelMetaDescFactory(attributeMetaDescFactory);
			ModelMetaDesc modelMetaDesc = modelMetaDescFactory
					.createModelMetaDesc(element);
			Generator generator = createGenerator(modelMetaDesc);
			generateSupport.generate(generator, modelMetaDesc, element);
		}
		// if (Options.isDebugEnabled(processingEnv)) {
		// Logger.debug(processingEnv, MessageFormatter.getMessage(
		// MessageCode.SILM3GEN0003, element));
		// }
	}

	/**
	 * Creates an attribute meta description factory.
	 * 
	 * @return an attribute meta description factory
	 */
	protected AttributeMetaDescFactory createAttributeMetaDescFactory() {
		return new AttributeMetaDescFactory(processingEnv);
	}

	/**
	 * Creates a model meta description factory.
	 * 
	 * @param attributeMetaDescFactory
	 *            the attribute meta description factory.
	 * @return a model meta description factory
	 */
	protected ModelMetaDescFactory createModelMetaDescFactory(
			AttributeMetaDescFactory attributeMetaDescFactory) {
		return new ModelMetaDescFactory(processingEnv, attributeMetaDescFactory);
	}

	/**
	 * Creates a generator object.
	 * 
	 * @param modelMetaDesc
	 *            the model description.
	 * @return a generator object.
	 */
	protected Generator createGenerator(ModelMetaDesc modelMetaDesc) {
		return new ModelMetaGenerator(modelMetaDesc);
	}
}
