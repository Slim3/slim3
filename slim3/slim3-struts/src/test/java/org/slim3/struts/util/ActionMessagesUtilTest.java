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

import junit.framework.TestCase;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.slim3.struts.unit.MockHttpServletRequest;
import org.slim3.struts.unit.MockServletContext;
import org.slim3.struts.web.WebLocator;

/**
 * @author higa
 * 
 */
public class ActionMessagesUtilTest extends TestCase {

    private MockServletContext servletContext = new MockServletContext();

    private MockHttpServletRequest request = new MockHttpServletRequest(
            servletContext, "/hoge.do");

    @Override
    protected void setUp() throws Exception {
        WebLocator.setRequest(request);
    }

    @Override
    protected void tearDown() throws Exception {
        WebLocator.setRequest(null);
    }

    /**
     * @throws Exception
     */
    public void testSaveErrorsIntoRequest() throws Exception {
        ActionMessages errors = new ActionMessages();
        errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("hoge",
                false));
        ActionMessagesUtil.saveErrorsIntoRequest(errors);
        assertFalse(((ActionMessages) request.getAttribute(Globals.ERROR_KEY))
                .isEmpty());
    }

    /**
     * @throws Exception
     */
    public void testSaveErrorsIntoSession() throws Exception {
        ActionMessages errors = new ActionMessages();
        errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("hoge",
                false));
        ActionMessagesUtil.saveErrorsIntoSession(errors);
        assertFalse(((ActionMessages) request.getSession().getAttribute(
                Globals.ERROR_KEY)).isEmpty());
    }

    /**
     * @throws Exception
     */
    public void testSaveMessagesIntoRequest() throws Exception {
        ActionMessages messages = new ActionMessages();
        messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("hoge",
                false));
        ActionMessagesUtil.saveMessagesIntoRequest(messages);
        assertFalse(((ActionMessages) request.getAttribute(Globals.MESSAGE_KEY))
                .isEmpty());
    }

    /**
     * @throws Exception
     */
    public void testSaveMessagesIntoSession() throws Exception {
        ActionMessages messages = new ActionMessages();
        messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("hoge",
                false));
        ActionMessagesUtil.saveMessagesIntoSession(messages);
        assertFalse(((ActionMessages) request.getSession().getAttribute(
                Globals.MESSAGE_KEY)).isEmpty());
    }

    /**
     * @throws Exception
     */
    public void testAddErrorsIntoRequest() throws Exception {
        ActionMessages errors = new ActionMessages();
        errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("hoge",
                false));
        ActionMessagesUtil.addErrorsIntoRequest(errors);
        assertFalse(((ActionMessages) request.getAttribute(Globals.ERROR_KEY))
                .isEmpty());
    }

    /**
     * @throws Exception
     */
    public void testAddErrorsIntoSession() throws Exception {
        ActionMessages errors = new ActionMessages();
        errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("hoge",
                false));
        ActionMessagesUtil.addErrorsIntoSession(errors);
        assertFalse(((ActionMessages) request.getSession().getAttribute(
                Globals.ERROR_KEY)).isEmpty());
    }

    /**
     * @throws Exception
     */
    public void testAddMessagesIntoRequest() throws Exception {
        ActionMessages messages = new ActionMessages();
        messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("hoge",
                false));
        ActionMessagesUtil.addMessagesIntoRequest(messages);
        assertFalse(((ActionMessages) request.getAttribute(Globals.MESSAGE_KEY))
                .isEmpty());
    }

    /**
     * @throws Exception
     */
    public void testAddMessagesIntoSession() throws Exception {
        ActionMessages messages = new ActionMessages();
        messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("hoge",
                false));
        ActionMessagesUtil.addMessagesIntoSession(messages);
        assertFalse(((ActionMessages) request.getSession().getAttribute(
                Globals.MESSAGE_KEY)).isEmpty());
    }

    /**
     * @throws Exception
     */
    public void testHasErrorsInRequest() throws Exception {
        ActionMessages errors = new ActionMessages();
        errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("hoge",
                false));
        request.setAttribute(Globals.ERROR_KEY, errors);
        assertTrue(ActionMessagesUtil.hasErrorsInRequest());
    }

    /**
     * @throws Exception
     */
    public void testHasErrorsInSession() throws Exception {
        ActionMessages errors = new ActionMessages();
        errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("hoge",
                false));
        request.getSession().setAttribute(Globals.ERROR_KEY, errors);
        assertTrue(ActionMessagesUtil.hasErrorsInSession());
    }
}