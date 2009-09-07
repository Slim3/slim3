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
package org.slim3.gen.desc;

import org.slim3.gen.processor.TigerOptions;

import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.declaration.ClassDeclaration;
import com.sun.mirror.declaration.FieldDeclaration;
import com.sun.mirror.util.DeclarationFilter;

/**
 * Creates a model meta description.
 * 
 * @author taedium
 * @since 3.0
 * 
 */
public class ModelMetaDescFactory {

	/** the processing environment */
	protected final AnnotationProcessorEnvironment processingEnv;

	/** the attribute meta description factory */
	protected final AttributeMetaDescFactory attributeMetaDescFactory;

	/**
	 * Creates a new {@link ModelMetaDescFactory}.
	 * 
	 * @param processingEnv
	 *            the processing environment
	 * @param attributeMetaDescFactory
	 *            the attribute meta description factory
	 */
	public ModelMetaDescFactory(AnnotationProcessorEnvironment processingEnv,
			AttributeMetaDescFactory attributeMetaDescFactory) {
		if (processingEnv == null) {
			throw new NullPointerException(
					"The processingEnv parameter is null.");
		}
		if (attributeMetaDescFactory == null) {
			throw new NullPointerException(
					"The attributeMetaDescFactory parameter is null.");
		}
		this.processingEnv = processingEnv;
		this.attributeMetaDescFactory = attributeMetaDescFactory;
	}

	/**
	 * Creates a model meta description.
	 * 
	 * @param modelElement
	 *            the model element.
	 * @return a model description
	 */
	public ModelMetaDesc createModelMetaDesc(ClassDeclaration modelElement) {
		if (modelElement == null) {
			throw new NullPointerException(
					"The modelElement parameter is null.");
		}
		String modelClassName = modelElement.getQualifiedName().toString();
		ModelMetaClassName modelMetaClassName = createModelMetaClassName(modelClassName);
		ModelMetaDesc modelMetaDesc = new ModelMetaDesc();
		modelMetaDesc.setPackageName(modelMetaClassName.getPackageName());
		modelMetaDesc.setSimpleName(modelMetaClassName.getSimpleName());
		modelMetaDesc.setModelClassName(modelClassName);
		handleFields(modelElement, modelMetaDesc);
		return modelMetaDesc;
	}

	/**
	 * Creates a model meta class name.
	 * 
	 * @param modelClassName
	 *            a model class name
	 * @return a model meta class name
	 */
	protected ModelMetaClassName createModelMetaClassName(String modelClassName) {
		return new ModelMetaClassName(modelClassName, TigerOptions
				.getModelPackage(processingEnv), TigerOptions
				.getMetaPackage(processingEnv), TigerOptions
				.getSharedPackage(processingEnv), TigerOptions
				.getServerPackage(processingEnv));
	}

	/**
	 * Handles fields.
	 * 
	 * @param modelElement
	 *            the model element.
	 * @param modelMetaDesc
	 *            the model meta description
	 */
	protected void handleFields(ClassDeclaration modelElement,
			ModelMetaDesc modelMetaDesc) {
		for (FieldDeclaration fieldElement : DeclarationFilter.getFilter(
				FieldDeclaration.class).filter(modelElement.getNestedTypes(),
				FieldDeclaration.class)) {
			handleField(fieldElement, modelMetaDesc);
		}
	}

	/**
	 * Handles a field.
	 * 
	 * @param fieldElement
	 *            a field element.
	 * @param modelMetaDesc
	 *            the model meta description.
	 */
	protected void handleField(FieldDeclaration fieldElement,
			ModelMetaDesc modelMetaDesc) {
		AttributeMetaDesc attributeMetaDesc = attributeMetaDescFactory
				.createAttributeMetaDesc(fieldElement);
		if (attributeMetaDesc != null) {
			modelMetaDesc.addAttributeMetaDesc(attributeMetaDesc);
		}
	}

}
