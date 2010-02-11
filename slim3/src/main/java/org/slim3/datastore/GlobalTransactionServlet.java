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

import org.slim3.util.StringUtil;

import com.google.appengine.api.datastore.Key;

/**
 * {@link HttpServlet} for Global Transaction.
 * 
 * @author higa
 * @since 3.0
 * 
 */
public class GlobalTransactionServlet extends HttpServlet {

    /**
     * The servelt path.
     */
    public static final String SERVLET_PATH = "/slim3/gtx";

    /**
     * The name of "command" parameter.
     */
    public static final String COMMAND_NAME = "command";

    /**
     * The name of "key" parameter.
     */
    public static final String KEY_NAME = "key";

    /**
     * The name of "version" parameter.
     */
    public static final String VERSION_NAME = "version";

    /**
     * The name of "rollforward" command.
     */
    public static final String ROLLFORWARD_COMMAND = "rollforward";

    /**
     * The name of "rollback" command.
     */
    public static final String ROLLBACK_COMMAND = "rollback";

    /**
     * The name of "cleanup" command.
     */
    public static final String CLEANUP_COMMAND = "cleanup";

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
        String command = req.getParameter(COMMAND_NAME);
        if (ROLLFORWARD_COMMAND.equalsIgnoreCase(command)) {
            String keyStr = req.getParameter(KEY_NAME);
            if (StringUtil.isEmpty(keyStr)) {
                throw new ServletException(
                    "The key parameter must not be null.");
            }
            String versionStr = req.getParameter(VERSION_NAME);
            if (StringUtil.isEmpty(versionStr)) {
                throw new ServletException(
                    "The version parameter must not be null.");
            }
            Key key = Datastore.stringToKey(keyStr);
            long version = Long.valueOf(versionStr);
            GlobalTransaction.rollForward(key, version);
        } else if (ROLLBACK_COMMAND.equalsIgnoreCase(command)) {
            String keyStr = req.getParameter(KEY_NAME);
            if (StringUtil.isEmpty(keyStr)) {
                throw new ServletException(
                    "The key parameter must not be null.");
            }
            Key key = Datastore.stringToKey(keyStr);
            GlobalTransaction.rollback(key);
        } else if (CLEANUP_COMMAND.equalsIgnoreCase(command)) {
            GlobalTransaction.cleanUp();
        } else {
            throw new ServletException("The command("
                + command
                + ") is unknown.");
        }
    }
}