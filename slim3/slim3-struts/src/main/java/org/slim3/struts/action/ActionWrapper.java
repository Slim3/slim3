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
package org.slim3.struts.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.slim3.commons.util.MethodUtil;
import org.slim3.struts.config.S3ExecuteConfig;
import org.slim3.struts.util.S3ExecuteConfigUtil;

/**
 * A wrapper class of {@link Action}.
 * 
 * @author higa
 * @since 3.0
 * 
 */
public class ActionWrapper extends Action {

    /**
     * The action.
     */
    protected Object action;

    /**
     * Constructor.
     * 
     * @param action
     *            the action
     */
    public ActionWrapper(Object action) {
        if (action == null) {
            throw new NullPointerException("The action parameter is null.");
        }
        this.action = action;
    }

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        S3ExecuteConfig executeConfig = S3ExecuteConfigUtil.getExecuteConfig();
        if (executeConfig != null) {
            return (ActionForward) MethodUtil.invoke(executeConfig.getMethod(),
                    action);
        }
        return null;
    }
}