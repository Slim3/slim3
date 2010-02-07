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
package org.slim3.datastore;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slim3.util.RequestUtil;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.repackaged.com.google.common.base.StringUtil;

/**
 * {@link HttpServlet} for Global Transaction.
 * 
 * @author higa
 * @since 3.0
 * 
 */
public class GlobalTransactionServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        process(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        process(req, resp);
    }

    /**
     * Processes this request.
     * 
     * @param req
     *            the request
     * @param resp
     *            the response
     * @throws ServletException
     *             if the path is illegal
     * @throws IOException
     *             if {@link IOException} occurred
     */
    protected void process(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String path = RequestUtil.getPath(req);
        String[] args = StringUtil.split(path, "/");
        if (args.length < 2
            || !args[0].equals("slim3")
            || !args[1].equals("gtx")) {
            throw new ServletException("The path("
                + path
                + ") must start with /slim3/gtx/.");
        }
        if (args[2].equalsIgnoreCase("rollforward")) {
            if (args.length != 5) {
                throw new ServletException("The path("
                    + path
                    + ") must be /slim3/gtx/rollforward/encodedKey/version.");
            }
            Key key = Datastore.stringToKey(args[3]);
            rollForward(key, Long.valueOf(args[4]));
        } else if (args[2].equalsIgnoreCase("rollback")) {
            if (args.length != 4) {
                throw new ServletException("The path("
                    + path
                    + ") must be /slim3/gtx/rollback/encodedKey.");
            }
            Key key = Datastore.stringToKey(args[3]);
            rollback(key);
        }
    }

    /**
     * Rolls forward the global transaction specified by the key.
     * 
     * @param globalTransactionKey
     *            the global transaction key
     * @param version
     *            the version
     */
    protected void rollForward(Key globalTransactionKey, long version) {
        GlobalTransaction.rollForward(globalTransactionKey, version);
    }

    /**
     * Rolls back the global transaction specified by the key.
     * 
     * @param globalTransactionKey
     *            the global transaction key
     */
    protected void rollback(Key globalTransactionKey) {
        GlobalTransaction.rollback(globalTransactionKey);
    }
}