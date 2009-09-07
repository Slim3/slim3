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

import com.sun.mirror.declaration.AnnotationMirror;
import com.sun.mirror.declaration.AnnotationTypeDeclaration;
import com.sun.mirror.declaration.Declaration;
import com.sun.mirror.util.SimpleDeclarationVisitor;

/**
 * A utility class for operationg elements.
 * 
 * @author taedium
 * @since 3.0
 * 
 */
public final class ElementUtil {

	/**
	 * Returns {@code AnnotationMirror} if an element is annotated with a
	 * specified annotation and {@code null} otherwise.
	 * 
	 * @param element
	 *            the element object to be checked.
	 * @param annotation
	 *            the fully qualified name of an annotation.
	 * @return {@code AnnotationMirror} if an element is annotated with a
	 *         specified annotation and {@code null} otherwise.
	 */
	public static AnnotationMirror getAnnotationMirror(Declaration element,
			final String annotation) {
		if (element == null) {
			throw new NullPointerException("The element parameter is null.");
		}
		for (AnnotationMirror mirror : element.getAnnotationMirrors()) {
			class Visitor extends SimpleDeclarationVisitor {

				boolean result;

				@Override
				public void visitAnnotationTypeDeclaration(
						AnnotationTypeDeclaration d) {
					result = d.getQualifiedName().equals("annotation");
				}
			}
			Visitor visitor = new Visitor();
			mirror.getAnnotationType().getDeclaration().accept(visitor);
			return visitor.result ? mirror : null;
		}
		return null;
	}
}
