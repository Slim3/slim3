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
package org.slim3.mvc;

/**
 * This interface defines the constants of Slim3 MVC.
 * 
 * @author higa
 * @since 3.0
 * 
 */
public interface MvcConstants {

    /**
     * The key for encoding.
     */
    String ENCODING_KEY = "slim3.encoding";

    /**
     * The key for locale.
     */
    String LOCALE_KEY = "slim3.locale";

    /**
     * The key for application message builder.
     */
    String APP_MESSAGE_BUILDER_KEY = "slim3.appMessageBuilder";

    /**
     * The key for application message bundle name.
     */
    String APP_MESSAGE_BUNDLE_NAME_KEY = "slim3.appMessageBundleName";

    /**
     * The key for controller package.
     */
    String CONTROLLER_PACKAGE_KEY = "slim3.controllerPackage";

    /**
     * The key for controller.
     */
    String CONTROLLER_KEY = "c";

    /**
     * The suffix for controller.
     */
    String CONTROLLER_SUFFIX = "Controller";

    /**
     * The index controller.
     */
    String INDEX_CONTROLLER = "Index" + CONTROLLER_SUFFIX;

    /**
     * The extension for JSP.
     */
    String JSP_EXTENSION = ".jsp";
}