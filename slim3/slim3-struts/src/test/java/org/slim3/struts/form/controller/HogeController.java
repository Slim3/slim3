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
package org.slim3.struts.form.controller;

import org.apache.struts.action.ActionForward;
import org.slim3.struts.annotation.Controller;
import org.slim3.struts.annotation.Execute;
import org.slim3.struts.annotation.Required;

/**
 * @author higa
 * 
 */
@Controller
public class HogeController {

    /**
     * 
     */
    public boolean reseted = false;

    /**
     * 
     */
    @Required
    public String aaa;

    /**
     * @return the result
     */
    @Execute(input = "index.jsp")
    public ActionForward index() {
        return null;
    }

    /**
     * 
     */
    public void reset() {
        reseted = true;
    }
}
