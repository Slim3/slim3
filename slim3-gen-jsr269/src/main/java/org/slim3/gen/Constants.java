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
package org.slim3.gen;

import org.slim3.gen.util.StringUtil;

/**
 * The collection of Constants.
 * 
 * @author taedium
 * @since 1.0.0
 */
public final class Constants {

    /** the suffix of meta class */
    public static final String META_SUFFIX = "Meta";

    /** the suffix of meta class */
    public static final String REF_SUFFIX = "Ref";

    /** the suffix of test class */
    public static final String TEST_SUFFIX = "Test";

    /** the suffix of controller. */
    public static final String CONTROLLER_SUFFIX = "Controller";

    /** the suffix of dao. */
    public static final String DAO_SUFFIX = "Dao";

    /** the suffix of asynchronous class */
    public static final String ASYNC_SUFFIX = "Async";

    /** the suffix of implementation class */
    public static final String IMPL_SUFFIX = "Impl";

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
    public static final String CONTROLLER_PACKAGE = "controller";

    /** the package name for model */
    public static final String MODEL_PACKAGE = "model";

    /** the package name for dao */
    public static final String DAO_PACKAGE = "dao";

    /** the package name for client */
    public static final String CLIENT_PACKAGE = "client";

    /** the package name for server */
    public static final String SERVER_PACKAGE = "server";

    /** the package name for service */
    public static final String SERVICE_PACKAGE = "service";

    /** the package name for shared */
    public static final String SHARED_PACKAGE = "shared";

    /** the package name for shared */
    public static final String META_PACKAGE = "meta";

}
