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
package org.slim3.controller.controller;

import java.util.List;
import java.util.Map;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

/**
 * @author higa
 * 
 */
public class HogeController extends Controller {

    /**
     * 
     */
    protected String aaa;

    /**
     * 
     */
    protected String bbb;

    /**
     * 
     */
    protected MyBean bean;

    /**
     * 
     */
    protected Map<String, Object> map;

    /**
     * 
     */
    protected String[] array;

    /**
     * 
     */
    protected List<String> list;

    /**
     * 
     */
    @SuppressWarnings("unchecked")
    protected List list2;

    /**
     * 
     */
    protected MyBean[] beanArray;

    /**
     * 
     */
    protected List<MyBean> beanList;

    /**
     * 
     */
    protected List<List<MyBean>> beanListList;

    /**
     * 
     */
    protected MyBean[][] beanArrayArray;

    /**
     * 
     */
    protected Map<String, Object>[][] mapArrayArray;

    /**
     * 
     */
    protected List<Map<String, Object>> mapList;

    /**
     * @return the aaa
     */
    public String getAaa() {
        return aaa;
    }

    /**
     * @param aaa
     *            the aaa to set
     */
    public void setAaa(String aaa) {
        this.aaa = aaa;
    }

    /**
     * @return the bbb
     */
    public String getBbb() {
        return bbb;
    }

    /**
     * @param bbb
     *            the bbb to set
     */
    public void setBbb(String bbb) {
        this.bbb = bbb;
    }

    /**
     * @return the myBean
     */
    public MyBean getBean() {
        return bean;
    }

    /**
     * @param myBean
     *            the myBean to set
     */
    public void setBean(MyBean myBean) {
        this.bean = myBean;
    }

    /**
     * @return the map
     */
    public Map<String, Object> getMap() {
        return map;
    }

    /**
     * @param map
     *            the map to set
     */
    public void setMap(Map<String, Object> map) {
        this.map = map;
    }

    /**
     * @return the array
     */
    public String[] getArray() {
        return array;
    }

    /**
     * @param array
     *            the array to set
     */
    public void setArray(String[] array) {
        this.array = array;
    }

    /**
     * @return the list
     */
    public List<String> getList() {
        return list;
    }

    /**
     * @param list
     *            the list to set
     */
    public void setList(List<String> list) {
        this.list = list;
    }

    /**
     * @return the list2
     */
    @SuppressWarnings("unchecked")
    public List getList2() {
        return list2;
    }

    /**
     * @param list2
     *            the list2 to set
     */
    @SuppressWarnings("unchecked")
    public void setList2(List list2) {
        this.list2 = list2;
    }

    /**
     * @return the myBeanArray
     */
    public MyBean[] getBeanArray() {
        return beanArray;
    }

    /**
     * @param myBeanArray
     *            the myBeanArray to set
     */
    public void setBeanArray(MyBean[] myBeanArray) {
        this.beanArray = myBeanArray;
    }

    /**
     * @return the myBeanList
     */
    public List<MyBean> getBeanList() {
        return beanList;
    }

    /**
     * @param myBeanList
     *            the myBeanList to set
     */
    public void setBeanList(List<MyBean> myBeanList) {
        this.beanList = myBeanList;
    }

    /**
     * @return the myBeanListList
     */
    public List<List<MyBean>> getBeanListList() {
        return beanListList;
    }

    /**
     * @param myBeanListList
     *            the myBeanListList to set
     */
    public void setBeanListList(List<List<MyBean>> myBeanListList) {
        this.beanListList = myBeanListList;
    }

    /**
     * @return the myBeanArrayArray
     */
    public MyBean[][] getBeanArrayArray() {
        return beanArrayArray;
    }

    /**
     * @param myBeanArrayArray
     *            the myBeanArrayArray to set
     */
    public void setBeanArrayArray(MyBean[][] myBeanArrayArray) {
        this.beanArrayArray = myBeanArrayArray;
    }

    /**
     * @return the mapArrayArray
     */
    public Map<String, Object>[][] getMapArrayArray() {
        return mapArrayArray;
    }

    /**
     * @param mapArrayArray
     *            the mapArrayArray to set
     */
    public void setMapArrayArray(Map<String, Object>[][] mapArrayArray) {
        this.mapArrayArray = mapArrayArray;
    }

    /**
     * @return the mapList
     */
    public List<Map<String, Object>> getMapList() {
        return mapList;
    }

    /**
     * @param mapList
     *            the mapList to set
     */
    public void setMapList(List<Map<String, Object>> mapList) {
        this.mapList = mapList;
    }

    @Override
    public Navigation execute() {
        return null;
    }
}