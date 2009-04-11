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

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import org.apache.struts.taglib.html.FormTag;
import org.apache.struts.taglib.html.SubmitTag;

/**
 * {@link SubmitTag} of Slim3.
 * 
 * @author higa
 * @since 3.0
 * 
 */
public class S3SubmitTag extends SubmitTag {

    /**
     * Whether this class writes javascript for validation.
     */
    protected boolean jsValidate = false;

    private static final long serialVersionUID = 1L;

    /**
     * Determines if this class writes javascript for validation.
     * 
     * @return whether this class writes javascript for validation
     */
    public boolean isJsValidate() {
        return jsValidate;
    }

    /**
     * Sets javascript validation flag.
     * 
     * @param jsValidate
     *            whether this class writes javascript for validation
     */
    public void setJsValidate(boolean jsValidate) {
        this.jsValidate = jsValidate;
    }

    @Override
    public void release() {
        super.release();
        jsValidate = false;
    }

    @Override
    public int doEndTag() throws JspException {
        if (jsValidate) {
            FormTag formTag = (FormTag) findAncestorWithClass(this,
                    FormTag.class);
            if (formTag == null) {
                throw new JspTagException("FormTag not found.");
            }
            String actionFormName = formTag.getBeanName();
            StringBuilder sb = new StringBuilder();
            sb.append("var myForm = document.forms['").append(actionFormName)
                    .append("'];");
            sb.append("myForm.id='").append(actionFormName).append("_").append(
                    property).append("'; ");
            sb.append("return validate").append(
                    actionFormName.substring(0, 1).toUpperCase()).append(
                    actionFormName.substring(1)).append("_").append(property)
                    .append("(myForm);");
            String originalOnclick = getOnclick();
            if (originalOnclick == null
                    || originalOnclick.startsWith("var myForm")) {
                setOnclick(sb.toString());
            } else {
                setOnclick(originalOnclick + ";" + sb.toString());
            }
        }
        return super.doEndTag();
    }
}