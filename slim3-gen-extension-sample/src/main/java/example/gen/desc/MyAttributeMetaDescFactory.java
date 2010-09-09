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
package example.gen.desc;

import org.slim3.gen.desc.AttributeMetaDesc;
import org.slim3.gen.desc.AttributeMetaDescFactory;
import org.slim3.gen.util.AnnotationMirrorUtil;
import org.slim3.gen.util.DeclarationUtil;

import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.declaration.AnnotationMirror;
import com.sun.mirror.declaration.ClassDeclaration;
import com.sun.mirror.declaration.FieldDeclaration;

import example.gen.MyConstants;

/**
 * @author higayasuo
 * 
 */
public class MyAttributeMetaDescFactory extends AttributeMetaDescFactory {

	/**
	 * @param env
	 */
	public MyAttributeMetaDescFactory(AnnotationProcessorEnvironment env) {
		super(env);
	}

	@Override
	protected void handleField(AttributeMetaDesc attributeMetaDesc,
			ClassDeclaration classDeclaration,
			FieldDeclaration fieldDeclaration, AnnotationMirror attribute) {
		super.handleField(attributeMetaDesc, classDeclaration,
				fieldDeclaration, attribute);
		AnnotationMirror sync = DeclarationUtil.getAnnotationMirror(env,
				fieldDeclaration, MyConstants.Sync);
		Boolean b = false;
		if (sync != null) {
			Boolean b2 = AnnotationMirrorUtil.getElementValue(sync,
					MyConstants.value);
			if (b2 != null) {
				b = b2.booleanValue();
			}
		}
		attributeMetaDesc.setData(MyConstants.Sync, b);
	}
}