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

import java.util.Iterator;
import java.util.LinkedList;

import org.slim3.gen.ClassConstants;
import org.slim3.gen.processor.TigerOptions;
import org.slim3.gen.util.ElementUtil;

import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.declaration.FieldDeclaration;
import com.sun.mirror.declaration.Modifier;
import com.sun.mirror.type.ArrayType;
import com.sun.mirror.type.ClassType;
import com.sun.mirror.type.DeclaredType;
import com.sun.mirror.type.EnumType;
import com.sun.mirror.type.InterfaceType;
import com.sun.mirror.type.PrimitiveType;
import com.sun.mirror.type.ReferenceType;
import com.sun.mirror.type.TypeMirror;
import com.sun.mirror.type.WildcardType;
import com.sun.mirror.util.SimpleTypeVisitor;

/**
 * Represents an attribute meta description factory.
 * 
 * @author taedium
 * @since 3.0
 * 
 */
public class AttributeMetaDescFactory {

	/** the processing environment */
	protected final AnnotationProcessorEnvironment processingEnv;

	/**
	 * Creates a new {@link AttributeMetaDescFactory}.
	 * 
	 * @param processingEnv
	 *            the processing environment
	 */
	public AttributeMetaDescFactory(AnnotationProcessorEnvironment processingEnv) {
		if (processingEnv == null) {
			throw new NullPointerException(
					"The processingEnv parameter is null.");
		}
		this.processingEnv = processingEnv;
	}

	/**
	 * Creates a new {@link AttributeMetaDesc}
	 * 
	 * @param fieldElement
	 *            the field element
	 * @return an attribute meta description
	 */
	public AttributeMetaDesc createAttributeMetaDesc(
			FieldDeclaration fieldElement) {
		if (fieldElement == null) {
			throw new NullPointerException(
					"The fieldElement parameter is null.");
		}
		if (!isPersistent(fieldElement)) {
			if (isInstanceVariable(fieldElement)
					&& !isNotPersistent(fieldElement)) {
				// Logger.warning(processingEnv, fieldElement, MessageFormatter
				// .getSimpleMessage(MessageCode.SILM3GEN0010));
			}
			return null;
		}
		if (isNestedType(fieldElement)) {
			return null;
		}
		Iterator<String> classNames = new ClassNameCollector2(fieldElement
				.getType()).collect().iterator();
		AttributeMetaDesc attributeMetaDesc = new AttributeMetaDesc();
		attributeMetaDesc.setName(fieldElement.getSimpleName().toString());
		if (isEmbedded(fieldElement)) {
			attributeMetaDesc.setEmbedded(true);
			if (classNames.hasNext()) {
				String modelClassName = classNames.next();
				ModelMetaClassName modelMetaClassName = createModelMetaClassName(modelClassName);
				attributeMetaDesc
						.setEmbeddedModelMetaClassName(modelMetaClassName
								.getQualifiedName());
			}
		} else {
			if (classNames.hasNext()) {
				String className = classNames.next();
				attributeMetaDesc.setAttributeClassName(className);
			}
			if (classNames.hasNext()) {
				String elementClassName = classNames.next();
				attributeMetaDesc
						.setAttributeElementClassName(elementClassName);
			}
		}
		return attributeMetaDesc;
	}

	/**
	 * Returns {@code true} if the attribute type is a nested type.
	 * 
	 * @param attributeElement
	 *            the element of an attribute
	 * @return {@code true} if the attribute type is a nested type
	 */
	protected boolean isNestedType(FieldDeclaration attributeElement) {
		class GetTypeMirror extends SimpleTypeVisitor {

			TypeMirror result;

			GetTypeMirror(TypeMirror typeMirror) {
				result = typeMirror;
			}

			@Override
			public void visitArrayType(ArrayType t) {
				GetTypeMirror visitor = new GetTypeMirror(result);
				t.getComponentType().accept(visitor);
				this.result = visitor.result;
			}
		}
		GetTypeMirror getTypeMirrorVisitor = new GetTypeMirror(attributeElement
				.getType());
		attributeElement.getType().accept(getTypeMirrorVisitor);
		TypeMirror typeMirror = getTypeMirrorVisitor.result;

		class GetDeclaredType extends SimpleTypeVisitor {
			DeclaredType result;

			@Override
			public void visitClassType(ClassType t) {
				result = t;
			}

			@Override
			public void visitDeclaredType(DeclaredType t) {
				result = t;
			}

			@Override
			public void visitEnumType(EnumType t) {
				result = t;
			}

			@Override
			public void visitInterfaceType(InterfaceType t) {
				result = t;
			}
		}
		GetDeclaredType getDeclaredTypeVisitor = new GetDeclaredType();
		typeMirror.accept(getDeclaredTypeVisitor);
		DeclaredType declaredType = getDeclaredTypeVisitor.result;
		if (declaredType == null) {
			return false;
		}
		return declaredType.getDeclaration().getDeclaringType() != null;
	}

	/**
	 * Returns {@code true} if the attribute is persistent.
	 * 
	 * @param attributeElement
	 *            the element of an attribute
	 * @return {@code true} if the attribute is persistent.
	 */
	protected boolean isPersistent(FieldDeclaration attributeElement) {
		return ElementUtil.getAnnotationMirror(attributeElement,
				ClassConstants.Persistent) != null;
	}

	/**
	 * Returns {@code true} if the attribute is not persistent.
	 * 
	 * @param attributeElement
	 *            the element of an attribute
	 * @return {@code true} if the attribute is persistent.
	 */
	protected boolean isNotPersistent(FieldDeclaration attributeElement) {
		return ElementUtil.getAnnotationMirror(attributeElement,
				ClassConstants.NotPersistent) != null;
	}

	/**
	 * Returns {@code true} if the attribute is an instance variable.
	 * 
	 * @param attributeElement
	 *            the element of an attribute
	 * @return {@code true} if the attribute is an instance variable.
	 */
	protected boolean isInstanceVariable(FieldDeclaration attributeElement) {
		return !attributeElement.getModifiers().contains(Modifier.STATIC);
	}

	/**
	 * Returns {@code true} if the attribute is embedded.
	 * 
	 * @param attributeElement
	 *            the element of an attribute
	 * @return {@code true} if the attribute is embedded.
	 */
	protected boolean isEmbedded(FieldDeclaration attributeElement) {
		return ElementUtil.getAnnotationMirror(attributeElement,
				ClassConstants.Embedded) != null;
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
	 * Collects the collection of class name.
	 * 
	 * @author taedium
	 * @since 3.0
	 * 
	 */
	protected static class ClassNameCollector2 extends SimpleTypeVisitor {

		LinkedList<String> names = new LinkedList<String>();

		/** the target typeMirror */
		protected final TypeMirror typeMirror;

		/**
		 * Creates a new {@link ClassNameCollector}
		 * 
		 * @param typeMirror
		 *            the target typeMirror
		 */
		public ClassNameCollector2(TypeMirror typeMirror) {
			this.typeMirror = typeMirror;
		}

		/**
		 * Collects the collection of class name.
		 * 
		 * @return the collection of class name
		 */
		public LinkedList<String> collect() {
			typeMirror.accept(this);
			return names;
		}

		@Override
		public void visitArrayType(ArrayType arg0) {
			ClassNameCollector2 collector2 = new ClassNameCollector2(arg0);
			LinkedList<String> names = collector2.collect();
			arg0.getComponentType().accept(collector2);
			this.names.add(names.getFirst() + "[]");
			this.names.add(names.getFirst());
		}

		@Override
		public void visitDeclaredType(DeclaredType arg0) {
			names.add(arg0.getDeclaration().getQualifiedName());
			for (TypeMirror arg : arg0.getActualTypeArguments()) {
				ClassNameCollector2 collector2 = new ClassNameCollector2(arg);
				LinkedList<String> names = collector2.collect();
				this.names.add(names.getFirst());
			}
		}

		@Override
		public void visitPrimitiveType(PrimitiveType arg0) {
			switch (arg0.getKind()) {
			case BOOLEAN: {
				names.add(boolean.class.getName());
				break;
			}
			case BYTE: {
				names.add(byte.class.getName());
				break;
			}
			case CHAR: {
				names.add(char.class.getName());
				break;
			}
			case DOUBLE: {
				names.add(double.class.getName());
				break;
			}
			case FLOAT: {
				names.add(float.class.getName());
				break;
			}
			case INT: {
				names.add(int.class.getName());
				break;
			}
			case LONG: {
				names.add(long.class.getName());
				break;
			}
			case SHORT: {
				names.add(short.class.getName());
				break;
			}
			default: {
				throw new IllegalArgumentException(arg0.getKind().name());
			}
			}
		}

		@Override
		public void visitWildcardType(WildcardType arg0) {
			for (ReferenceType referenceType : arg0.getUpperBounds()) {
				ClassNameCollector2 collector2 = new ClassNameCollector2(
						referenceType);
				LinkedList<String> names = collector2.collect();
				this.names.add(names.getFirst());
			}
			for (ReferenceType referenceType : arg0.getLowerBounds()) {
				ClassNameCollector2 collector2 = new ClassNameCollector2(
						referenceType);
				LinkedList<String> names = collector2.collect();
				this.names.add(names.getFirst());
			}
		}

	}
}
