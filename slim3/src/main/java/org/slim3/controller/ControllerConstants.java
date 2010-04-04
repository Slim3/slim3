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
package org.slim3.controller;

/**
 * This interface defines the constants of Slim3 Controller.
 * 
 * @author higa
 * @since 1.0.0
 * 
 */
public final class ControllerConstants {

    /**
     * The key of this application's UUID.
     */
    public static String UUID_KEY = "slim3.uuid";

    /**
     * The key of hot reloading.
     */
    public static String HOT_RELOADING_KEY = "slim3.hotReloading";

    /**
     * The key of configuration setting for request character set.
     */
    public static String REQUEST_CHARSET_KEY =
        "javax.servlet.jsp.jstl.fmt.request.charset";

    /**
     * The default request character set.
     */
    public static String DEFAULT_REQUEST_CHARSET = "UTF-8";

    /**
     * The key of configuration setting for i18n localization context.
     */
    public static String LOCALIZATION_CONTEXT_KEY =
        "javax.servlet.jsp.jstl.fmt.localizationContext";

    /**
     * The default value of configuration setting for i18n localization context.
     */
    public static String DEFAULT_LOCALIZATION_CONTEXT = "application";

    /**
     * The key of configuration setting for application based preferred locale.
     */
    public static String LOCALE_KEY = "javax.servlet.jsp.jstl.fmt.locale";

    /**
     * The key of localization setting for time zone.
     */
    public static String TIME_ZONE_KEY = "javax.servlet.jsp.jstl.fmt.timeZone";

    /**
     * The key of controller package.
     */
    public static String CONTROLLER_PACKAGE_KEY = "slim3.controllerPackage";

    /**
     * The key of root package.
     */
    public static String ROOT_PACKAGE_KEY = "slim3.rootPackage";

    /**
     * The key of cool package.
     */
    public static String COOL_PACKAGE_KEY = "slim3.coolPackage";

    /**
     * The default cool package.
     */
    public static String DEFAULT_COOL_PACKAGE = "cool";

    /**
     * The key of controller.
     */
    public static String CONTROLLER_KEY = "slim3.controller";

    /**
     * The key of base path.
     */
    public static String BASE_PATH_KEY = "slim3.basePath";

    /**
     * The key of errors.
     */
    public static String ERRORS_KEY = "errors";

    /**
     * The suffix of controller.
     */
    public static String CONTROLLER_SUFFIX = "Controller";

    /**
     * The index controller.
     */
    public static String INDEX_CONTROLLER = "Index" + CONTROLLER_SUFFIX;

    /**
     * The default controller package name.
     */
    public static String DEFAULT_CONTROLLER_PACKAGE = "controller";

    /**
     * The server controller package name.
     */
    public static String SERVER_CONTROLLER_PACKAGE = "server.controller";

    /**
     * The key whether the path is routed.
     */
    public static String ROUTED_KEY = "slim3.routed";

    /**
     * The path before forwarding.
     */
    public static String FORWARD_SERVLET_PATH_KEY =
        "javax.servlet.forward.servlet_path";
}