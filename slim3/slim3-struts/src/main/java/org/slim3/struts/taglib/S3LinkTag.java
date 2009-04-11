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

import org.apache.struts.taglib.html.LinkTag;

/**
 * {@link LinkTag} of Slim3.
 * 
 * @author higa
 * @since 3.0
 * 
 */
public class S3LinkTag extends LinkTag {

    private static final long serialVersionUID = 1L;

    @Override
    protected String calculateURL() throws JspException {
        if (href != null) {
            int index = href.indexOf(':');
            if (index > -1) {
                return super.calculateURL();
            }
            return S3Functions.url(href);
        }
        return super.calculateURL();
    }
}