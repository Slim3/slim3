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
package org.slim3.struts.util;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionMessages;
import org.slim3.struts.web.WebLocator;

/**
 * A utility for {@link ActionMessages}.
 * 
 * @author higa
 * @since 3.0
 */
public final class ActionMessagesUtil {

    private ActionMessagesUtil() {
    }

    /**
     * Saves the error messages into request.
     * 
     * @param errors
     *            the error messages
     */
    public static void saveErrorsIntoRequest(ActionMessages errors) {
        if ((errors == null) || errors.isEmpty()) {
            WebLocator.getRequest().removeAttribute(Globals.ERROR_KEY);
            return;
        }
        WebLocator.getRequest().setAttribute(Globals.ERROR_KEY, errors);
    }

    /**
     * Saves the error messages in session.
     * 
     * @param errors
     *            the error messages
     */
    public static void saveErrorsIntoSession(ActionMessages errors) {
        if ((errors == null) || errors.isEmpty()) {
            WebLocator.getRequest().getSession().removeAttribute(
                    Globals.ERROR_KEY);
            return;
        }
        WebLocator.getRequest().getSession().setAttribute(Globals.ERROR_KEY,
                errors);
    }

    /**
     * Saves the messages into request.
     * 
     * @param messages
     *            the messages
     */
    public static void saveMessagesIntoRequest(ActionMessages messages) {
        if ((messages == null) || messages.isEmpty()) {
            WebLocator.getRequest().removeAttribute(Globals.MESSAGE_KEY);
            return;
        }
        WebLocator.getRequest().setAttribute(Globals.MESSAGE_KEY, messages);
    }

    /**
     * Saves the messages into session.
     * 
     * @param messages
     *            the messages
     */
    public static void saveMessagesIntoSession(ActionMessages messages) {
        if ((messages == null) || messages.isEmpty()) {
            WebLocator.getRequest().getSession().removeAttribute(
                    Globals.MESSAGE_KEY);
            return;
        }
        WebLocator.getRequest().getSession().setAttribute(Globals.MESSAGE_KEY,
                messages);
    }

    /**
     * Adds the error messages into request.
     * 
     * @param errors
     *            the error messages
     */
    public static void addErrorsIntoRequest(ActionMessages errors) {
        if (errors == null) {
            return;
        }
        ActionMessages requestErrors = (ActionMessages) WebLocator.getRequest()
                .getAttribute(Globals.ERROR_KEY);
        if (requestErrors == null) {
            requestErrors = new ActionMessages();
        }
        requestErrors.add(errors);
        saveErrorsIntoRequest(requestErrors);
    }

    /**
     * Adds the error messages into session
     * 
     * @param errors
     *            エラーメッセージ
     * @since 1.0.2
     */
    public static void addErrorsIntoSession(ActionMessages errors) {
        if (errors == null) {
            return;
        }
        ActionMessages sessionErrors = (ActionMessages) WebLocator.getRequest()
                .getSession().getAttribute(Globals.ERROR_KEY);
        if (sessionErrors == null) {
            sessionErrors = new ActionMessages();
        }
        sessionErrors.add(errors);
        saveErrorsIntoSession(sessionErrors);
    }

    /**
     * Adds the messages into request
     * 
     * @param messages
     *            the messages
     */
    public static void addMessagesIntoRequest(ActionMessages messages) {
        if (messages == null) {
            return;
        }
        ActionMessages requestMessages = (ActionMessages) WebLocator
                .getRequest().getAttribute(Globals.MESSAGE_KEY);
        if (requestMessages == null) {
            requestMessages = new ActionMessages();
        }
        requestMessages.add(messages);
        saveMessagesIntoRequest(requestMessages);
    }

    /**
     * Adds the messages into session.
     * 
     * @param messages
     *            the messages
     */
    public static void addMessagesIntoSession(ActionMessages messages) {
        if (messages == null) {
            return;
        }
        ActionMessages sessionMessages = (ActionMessages) WebLocator
                .getRequest().getSession().getAttribute(Globals.MESSAGE_KEY);
        if (sessionMessages == null) {
            sessionMessages = new ActionMessages();
        }
        sessionMessages.add(messages);
        saveMessagesIntoSession(sessionMessages);
    }

    /**
     * Determines if there are some error messages in request.
     * 
     * @return whether there are some error messages in request
     */
    public static boolean hasErrorsInRequest() {
        ActionMessages errors = (ActionMessages) WebLocator.getRequest()
                .getAttribute(Globals.ERROR_KEY);
        if (errors != null && !errors.isEmpty()) {
            return true;
        }
        return false;
    }

    /**
     * Determines if there are some error messages in session.
     * 
     * @return whether there are some error messages in session
     */
    public static boolean hasErrorsInSession() {
        ActionMessages errors = (ActionMessages) WebLocator.getRequest()
                .getSession().getAttribute(Globals.ERROR_KEY);
        if (errors != null && !errors.isEmpty()) {
            return true;
        }
        return false;
    }

}
