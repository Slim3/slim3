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
package org.slim3.controller.router;

import javax.servlet.ServletContext;

import org.slim3.controller.ControllerConstants;
import org.slim3.util.ClassUtil;
import org.slim3.util.Cleanable;
import org.slim3.util.Cleaner;
import org.slim3.util.ServletContextLocator;
import org.slim3.util.StringUtil;

/**
 * A factory for {@link Router}.
 * 
 * @author higa
 * @since 1.0.0
 * 
 */
public final class RouterFactory {

    /**
     * The key of {@link Router}.
     */
    public static final String ROUTER_KEY = "slim3.router";

    private static Router defaultRouter = new RouterImpl();

    /**
     * Returns a router.
     * 
     * @return a router
     */
    public static synchronized Router getRouter() {
        ServletContext servletContext = getServletContext();
        Router router = (Router) servletContext.getAttribute(ROUTER_KEY);
        if (router == null) {
            router = createRouter(servletContext);
            servletContext.setAttribute(ROUTER_KEY, router);
            Cleaner.add(new Cleanable() {
                public void clean() {
                    getServletContext().removeAttribute(ROUTER_KEY);
                }
            });
        }
        return router;
    }

    private static ServletContext getServletContext() {
        ServletContext servletContext = ServletContextLocator.get();
        if (servletContext == null) {
            throw new IllegalStateException("The servletContext is not found.");
        }
        return servletContext;
    }

    private static Router createRouter(ServletContext servletContext) {
        String rootPackageName =
            servletContext
                .getInitParameter(ControllerConstants.ROOT_PACKAGE_KEY);
        if (StringUtil.isEmpty(rootPackageName)) {
            throw new IllegalStateException("The context-param("
                + ControllerConstants.ROOT_PACKAGE_KEY
                + ") is not found in web.xml.");
        }
        String contollerPackageName =
            (String) servletContext
                .getAttribute(ControllerConstants.CONTROLLER_PACKAGE_KEY);
        if (contollerPackageName == null) {
            contollerPackageName =
                ControllerConstants.DEFAULT_CONTROLLER_PACKAGE;
        }
        try {
            String className =
                rootPackageName + "." + contollerPackageName + ".AppRouter";
            Class<?> clazz = Class.forName(className);
            return ClassUtil.newInstance(clazz);
        } catch (ClassNotFoundException e) {
            return defaultRouter;
        }
    }

    private RouterFactory() {
    }
}
