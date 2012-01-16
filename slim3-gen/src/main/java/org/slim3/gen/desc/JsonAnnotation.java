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
package org.slim3.gen.desc;

/**
 * @author nakaguchi
 * @author oyama
 */
public class JsonAnnotation {
    private boolean ignore;
    private boolean hasIgnore;
    private boolean ignoreNull = true;
    private String alias = "";
    private String coderClassName = "org.slim3.datastore.json.Default";
    private int order = Integer.MAX_VALUE;

    /**
     * @return the ignore
     */
    public boolean isIgnore() {
        return ignore;
    }

    /**
     * @param ignore the ignore to set
     */
    public void setIgnore(boolean ignore) {
        this.ignore = ignore;
        this.hasIgnore = true;
    }

    /**
     * @return true if ignore set(true or false).
     */
    public boolean hasIgnore(){
        return hasIgnore;
    }

    /**
     * @return the ignoreNull
     */
    public boolean isIgnoreNull() {
        return ignoreNull;
    }

    /**
     * @param ignoreNull the ignoreNull to set
     */
    public void setIgnoreNull(boolean ignoreNull) {
        this.ignoreNull = ignoreNull;
    }

    /**
     * @return the alias
     */
    public String getAlias() {
        return alias;
    }

    /**
     * @param alias the alias to set
     */
    public void setAlias(String alias) {
        this.alias = alias;
    }

    /**
     * @return the coderClassName
     */
    public String getCoderClassName() {
        return coderClassName;
    }

    /**
     * @param coderClassName the coderClassName to set
     */
    public void setCoderClassName(String coderClassName) {
        this.coderClassName = coderClassName;
    }

    /**
     * @return the order
     */
    public int getOrder() {
        return order;
    }

    /**
     * @param order the order to set
     */
    public void setOrder(int order) {
        this.order = order;
    }
}
