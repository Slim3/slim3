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
package org.slim3.controller;

/**
 * This interface defines the constants of Slim3 Controller.
 * 
 * @author higa
 * @since 3.0
 * 
 */
public interface ControllerConstants {

    /**
     * The key of this application's UUID.
     */
    String UUID_KEY = "slim3.uuid";

    /**
     * The key of hot reloading.
     */
    String HOT_RELOADING_KEY = "slim3.hotReloading";

    /**
     * The key of configuration setting for request character set.
     */
    String REQUEST_CHARSET_KEY = "javax.servlet.jsp.jstl.fmt.request.charset";

    /**
     * The default request character set.
     */
    String DEFAULT_REQUEST_CHARSET = "UTF-8";

    /**
     * The key of configuration setting for i18n localization context.
     */
    String LOCALIZATION_CONTEXT_KEY =
        "javax.servlet.jsp.jstl.fmt.localizationContext";

    /**
     * The default value of configuration setting for i18n localization context.
     */
    String DEFAULT_LOCALIZATION_CONTEXT = "application";

    /**
     * The key of configuration setting for application based preferred locale.
     */
    String LOCALE_KEY = "javax.servlet.jsp.jstl.fmt.locale";

    /**
     * The key of localization setting for time zone.
     */
    String TIME_ZONE_KEY = "javax.servlet.jsp.jstl.fmt.timeZone";

    /**
     * The key of controller package.
     */
    String CONTROLLER_PACKAGE_KEY = "slim3.controllerPackage";

    /**
     * The key of root package.
     */
    String ROOT_PACKAGE_KEY = "slim3.rootPackage";

    /**
     * The key of cool package.
     */
    String COOL_PACKAGE_KEY = "slim3.coolPackage";

    /**
     * The default cool package.
     */
    String DEFAULT_COOL_PACKAGE = "cool";

    /**
     * The key of controller.
     */
    String CONTROLLER_KEY = "slim3.controller";

    /**
     * The key of errors.
     */
    String ERRORS_KEY = "errors";

    /**
     * The suffix of controller.
     */
    String CONTROLLER_SUFFIX = "Controller";

    /**
     * The index controller.
     */
    String INDEX_CONTROLLER = "Index" + CONTROLLER_SUFFIX;

    /**
     * The default controller package name.
     */
    String DEFAULT_CONTROLLER_PACKAGE = "controller";

    /**
     * The server controller package name.
     */
    String SERVER_CONTROLLER_PACKAGE = "server.controller";
}
