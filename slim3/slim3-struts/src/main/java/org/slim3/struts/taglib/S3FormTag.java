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
package org.slim3.struts.taglib;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.config.FormBeanConfig;
import org.apache.struts.taglib.TagUtils;
import org.apache.struts.taglib.html.Constants;
import org.apache.struts.taglib.html.FormTag;
import org.apache.struts.util.RequestUtils;
import org.slim3.struts.util.ActionUtil;

/**
 * {@link FormTag} of Slim3.
 * 
 * @author higa
 * @since 3.0
 * 
 */
public class S3FormTag extends FormTag {

    private static final long serialVersionUID = 1L;

    /**
     * The onkeypress event.
     */
    protected String onkeypress;

    /**
     * The onkeyup event.
     */
    protected String onkeyup;

    /**
     * The onkeydown event.
     */
    protected String onkeydown;

    /**
     * Returns the onkeypress event.
     * 
     * @return the onkeypress event
     */
    public String getOnkeypress() {
        return onkeypress;
    }

    /**
     * Sets the onkeypress event.
     * 
     * @param onkeypress
     *            the onkeypress event
     */
    public void setOnkeypress(String onkeypress) {
        this.onkeypress = onkeypress;
    }

    /**
     * Returns the onkeyup event
     * 
     * @return the onkeyup event
     */
    public String getOnkeyup() {
        return onkeyup;
    }

    /**
     * Sets the onkeyup event
     * 
     * @param onkeyup
     *            the onkeyup event
     */
    public void setOnkeyup(String onkeyup) {
        this.onkeyup = onkeyup;
    }

    /**
     * Returns the onkeydown event
     * 
     * @return the onkeydown event
     */
    public String getOnkeydown() {
        return onkeydown;
    }

    /**
     * Sets the onkeydown event.
     * 
     * @param onkeydown
     *            the onkeydown event
     */
    public void setOnkeydown(String onkeydown) {
        this.onkeydown = onkeydown;
    }

    @Override
    protected void lookup() throws JspException {
        moduleConfig = TagUtils.getInstance().getModuleConfig(pageContext);
        if (moduleConfig == null) {
            JspException e = new JspException(messages
                    .getMessage("formTag.collections"));
            pageContext.setAttribute(Globals.EXCEPTION_KEY, e,
                    PageContext.REQUEST_SCOPE);
            throw e;
        }
        servlet = (ActionServlet) pageContext.getServletContext().getAttribute(
                Globals.ACTION_SERVLET_KEY);
        if (action == null) {
            action = ActionUtil.getActionPath();
        } else if (!action.startsWith("/")) {
            action = ActionUtil.getActionPath() + action;
        }
        String actionPath = action;
        if (actionPath.endsWith("/")) {
            actionPath = actionPath.substring(0, actionPath.length() - 1);
        }
        mapping = (ActionMapping) moduleConfig.findActionConfig(actionPath);
        if (mapping == null) {
            JspException e = new JspException(messages.getMessage(
                    "formTag.mapping", action));
            pageContext.setAttribute(Globals.EXCEPTION_KEY, e,
                    PageContext.REQUEST_SCOPE);
            throw e;
        }
        FormBeanConfig formBeanConfig = moduleConfig.findFormBeanConfig(mapping
                .getName());
        if (formBeanConfig == null) {
            JspException e = new JspException(messages.getMessage(
                    "formTag.formBean", mapping.getName(), action));
            pageContext.setAttribute(Globals.EXCEPTION_KEY, e,
                    PageContext.REQUEST_SCOPE);
            throw e;
        }
        beanName = mapping.getAttribute();
        beanScope = mapping.getScope();
        beanType = formBeanConfig.getType();
    }

    @Override
    protected void initFormBean() throws JspException {
        int scope = PageContext.SESSION_SCOPE;
        if ("request".equalsIgnoreCase(beanScope)) {
            scope = PageContext.REQUEST_SCOPE;
        }

        Object bean = pageContext.getAttribute(beanName, scope);
        if (bean == null) {
            bean = RequestUtils.createActionForm(
                    (HttpServletRequest) pageContext.getRequest(), mapping,
                    moduleConfig, servlet);
            if (bean == null) {
                throw new JspException(messages.getMessage("formTag.create",
                        beanType));
            }
            pageContext.setAttribute(beanName, bean, scope);
        }
        pageContext.setAttribute(Constants.BEAN_KEY, bean,
                PageContext.REQUEST_SCOPE);
    }

    @Override
    protected void renderAction(StringBuffer results) {
        HttpServletRequest request = (HttpServletRequest) pageContext
                .getRequest();
        HttpServletResponse response = (HttpServletResponse) pageContext
                .getResponse();
        results.append(" action=\"");
        String contextPath = request.getContextPath();
        StringBuffer value = new StringBuffer();
        if (contextPath.length() > 1) {
            value.append(contextPath);
        }
        value.append(action);
        results.append(response.encodeURL(value.toString()));
        results.append("\"");
    }

    @Override
    public void release() {
        super.release();
        onkeypress = null;
        onkeyup = null;
        onkeydown = null;
    }

    @Override
    protected void renderOtherAttributes(StringBuffer results) {
        super.renderOtherAttributes(results);
        renderAttribute(results, "onkeypress", onkeypress);
        renderAttribute(results, "onkeyup", onkeyup);
        renderAttribute(results, "onkeydown", onkeydown);
    }

}