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

import org.slim3.gen.Constants;

import com.sun.mirror.apt.AnnotationProcessorEnvironment;

/**
 * Represents options for {@link Processor}.
 * 
 * @author taedium
 * @since 3.0
 * @see Processor#getSupportedOptions()
 */
public final class TigerOptions {

	/** debug option */
	public static final String DEBUG = "debug";

	/** the model package */
	public static final String MODEL_PACKAGE = "modelPackage";

	/** the meta package */
	public static final String META_PACKAGE = "metaPackage";

	/** the shared package */
	public static final String SHARED_PACKAGE = "sharedPackage";

	/** the server package */
	public static final String SERVER_PACKAGE = "serverPackage";

	/**
	 * Returns {@code true} if debug enabled otherwirse {@code false}.
	 * 
	 * @param env
	 *            the processing environment.
	 * @return {@code true} if the debug option enabled otherwirse {@code false}
	 *         .
	 */
	public static boolean isDebugEnabled(AnnotationProcessorEnvironment env) {
		String debug = env.getOptions().get(TigerOptions.DEBUG);
		if (debug == null) {
			return false;
		}
		return Boolean.valueOf(debug).booleanValue();
	}

	/**
	 * Returns the model package.
	 * 
	 * @param env
	 *            the processing environment.
	 * @return the model package.
	 */
	public static String getModelPackage(AnnotationProcessorEnvironment env) {
		String modelPackage = env.getOptions().get(TigerOptions.MODEL_PACKAGE);
		return modelPackage != null ? modelPackage : Constants.MODEL_PACKAGE;
	}

	/**
	 * Returns the meta package.
	 * 
	 * @param env
	 *            the processing environment.
	 * @return the meta package.
	 */
	public static String getMetaPackage(AnnotationProcessorEnvironment env) {
		String metaPackage = env.getOptions().get(TigerOptions.META_PACKAGE);
		return metaPackage != null ? metaPackage : Constants.META_PACKAGE;
	}

	/**
	 * Returns the shared package.
	 * 
	 * @param env
	 *            the processing environment.
	 * @return the shared package.
	 */
	public static String getSharedPackage(AnnotationProcessorEnvironment env) {
		String sharedPackage = env.getOptions()
				.get(TigerOptions.SHARED_PACKAGE);
		return sharedPackage != null ? sharedPackage : Constants.SHARED_PACKAGE;
	}

	/**
	 * Returns the server package.
	 * 
	 * @param env
	 *            the processing environment.
	 * @return the server package.
	 */
	public static String getServerPackage(AnnotationProcessorEnvironment env) {
		String serverPackage = env.getOptions()
				.get(TigerOptions.SERVER_PACKAGE);
		return serverPackage != null ? serverPackage : Constants.SERVER_PACKAGE;
	}
}
