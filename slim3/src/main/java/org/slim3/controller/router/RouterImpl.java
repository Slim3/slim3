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

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slim3.util.RequestUtil;

/**
 * An implementation class for {@link Router}.
 * 
 * @author higa
 * @since 1.0.0
 * 
 */
public class RouterImpl implements Router {

    /**
     * The list of {@link Routing}s.
     */
    protected List<Routing> routingList = new ArrayList<Routing>();

    /**
     * Adds {@link Routing}.
     * 
     * @param from
     *            the "from" path
     * @param to
     *            the "to" path
     * @throws NullPointerException
     *             if the from parameter is null or if the to parameter is null
     */
    public void addRouting(String from, String to) throws NullPointerException {
        routingList.add(new Routing(from, to));
    }

    public boolean isStatic(String path) throws NullPointerException {
        if (path == null) {
            throw new NullPointerException("The path parameter is null.");
        }
        if (path.startsWith("/_ah/")) {
            return false;
        }
        String extension = RequestUtil.getExtension(path);
        return extension != null && !extension.startsWith("s3");
    }

    public String route(HttpServletRequest request, String path) {
        if (request == null) {
            throw new NullPointerException("The request parameter is null.");
        }
        if (path == null) {
            throw new NullPointerException("The path parameter is null.");
        }
        for (Routing r : routingList) {
            String to = r.route(request, path);
            if (to != null) {
                return to;
            }
        }
        return null;
    }

}
