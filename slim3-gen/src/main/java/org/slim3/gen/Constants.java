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
package org.slim3.gen;

import org.slim3.gen.util.StringUtil;

/**
 * The collection of Constants.
 * 
 * @author taedium
 * @since 3.0
 */
public final class Constants {

    /** the suffix of meta class */
    public static final String META_SUFFIX = "Meta";

    /** the suffix of test class */
    public static final String TEST_SUFFIX = "Test";

    /** the suffix of controller. */
    public static final String CONTROLLER_SUFFIX = "Controller";

    /** the suffix of dao. */
    public static final String DAO_SUFFIX = "Dao";

    /** the suffix of async class */
    public static final String ASYNC_SUFFIX = "Async";

    /** the suffix of view. */
    public static final String VIEW_SUFFIX = ".jsp";

    /** the index */
    public static final String INDEX = "index";

    /** the index controller. */
    public static final String INDEX_CONTROLLER =
        StringUtil.capitalize(INDEX) + CONTROLLER_SUFFIX;

    /** the index view. */
    public static final String INDEX_VIEW = INDEX + VIEW_SUFFIX;

    /** the sub package name for controller */
    public static final String CONTROLLER_SUB_PACKAGE = "controller";

    /** the sub package name for model */
    public static final String MODEL_SUB_PACKAGE = "model";

    /** the sub package name for dao */
    public static final String DAO_SUB_PACKAGE = "dao";

}
