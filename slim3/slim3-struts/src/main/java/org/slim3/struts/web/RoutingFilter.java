/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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
package org.slim3.struts.web;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.slim3.struts.S3StrutsGlobals;
import org.slim3.struts.config.S3ActionMapping;
import org.slim3.struts.config.S3ExecuteConfig;
import org.slim3.struts.util.RequestUtil;
import org.slim3.struts.util.S3ExecuteConfigUtil;
import org.slim3.struts.util.S3ModuleConfigUtil;

/**
 * A filter for routing.
 * 
 * @author higa
 * @since 3.0
 */
public class RoutingFilter implements Filter {

    /**
     * Assembles action path
     * 
     * @param path
     *            the path
     * @return action path
     */
    public static String assembleActionPath(String path) {
        if (path.indexOf('.') < 0) {
            String methodName = null;
            String actionPath = path;
            if (actionPath.endsWith("/")) {
                actionPath = actionPath.substring(0, actionPath.length() - 1);
            } else {
                int index = actionPath.lastIndexOf("/");
                methodName = actionPath.substring(index + 1);
                actionPath = actionPath.substring(0, index);
            }
            if (actionPath.equals("/") || actionPath.length() == 0) {
                actionPath = "/index";
            }
            S3ActionMapping actionMapping = (S3ActionMapping) S3ModuleConfigUtil
                    .getModuleConfig().findActionConfig(actionPath);
            if (actionMapping != null) {
                S3ExecuteConfig executeConfig = null;
                if (methodName != null) {
                    executeConfig = actionMapping.getExecuteConfig(methodName);
                    if (executeConfig != null) {
                        path = actionPath + S3StrutsGlobals.ACTION_EXTENSION;
                    }
                } else {
                    path = actionPath + S3StrutsGlobals.ACTION_EXTENSION;
                    S3ExecuteConfig previousExecuteConfig = S3ExecuteConfigUtil
                            .getExecuteConfig();
                    if (previousExecuteConfig != null) {
                        path = path + "?"
                                + previousExecuteConfig.getMethod().getName()
                                + "=";
                    }
                }
                S3ExecuteConfigUtil.setExecuteConfig(executeConfig);
            }
        }
        return path;
    }

    /**
     * The suffix of action.
     */
    public void init(FilterConfig config) throws ServletException {
    }

    public void destroy() {
    }

    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        String path = RequestUtil.getPath();
        path = assembleActionPath(path);
        if (path.endsWith(S3StrutsGlobals.ACTION_EXTENSION)) {
            request.getRequestDispatcher(path).forward(request, response);
        } else {
            chain.doFilter(request, response);
        }
    }
}