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
package org.slim3.struts.action.controller;

import java.util.List;
import java.util.Map;

import org.apache.struts.action.ActionForward;
import org.slim3.struts.action.MyBean;
import org.slim3.struts.annotation.Execute;
import org.slim3.struts.annotation.SessionScope;

/**
 * @author higa
 * 
 */
public class HogeController {

    /**
     * 
     */
    @SessionScope
    public String aaa;

    /**
     * 
     */
    public String bbb;

    /**
     * 
     */
    public MyBean myBean;

    /**
     * 
     */
    public Map<String, Object> map;

    /**
     * 
     */
    public String[] array;

    /**
     * 
     */
    public List<String> list;

    /**
     * 
     */
    @SuppressWarnings("unchecked")
    public List list2;

    /**
     * 
     */
    public MyBean[] myBeanArray;

    /**
     * 
     */
    public List<MyBean> myBeanList;

    /**
     * 
     */
    public List<List<MyBean>> myBeanListList;

    /**
     * 
     */
    public MyBean[][] myBeanArrayArray;

    /**
     * 
     */
    public Map<String, Object>[][] mapArrayArray;

    /**
     * 
     */
    public List<Map<String, Object>> mapList;

    /**
     * @return the result
     */
    @Execute(validate = false)
    public ActionForward index() {
        return new ActionForward("index.jsp");
    }

    /**
     * @return the result
     */
    @Execute(input = "index.jsp", roles = { "role1", "role2" })
    public ActionForward submit() {
        return new ActionForward("index.jsp");
    }
}
