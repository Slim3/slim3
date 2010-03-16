/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
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
package org.slim3.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

/**
 * {@link HttpServletRequest} acts as {link Map}.
 * 
 * @author higa
 * @since 1.0.0
 * 
 */
public class RequestMap extends HashMap<String, Object> {

    private static final long serialVersionUID = 1L;

    /**
     * The request.
     */
    protected HttpServletRequest request;

    /**
     * Constructor.
     * 
     * @param request
     *            the request
     */
    public RequestMap(HttpServletRequest request) {
        if (request == null) {
            throw new NullPointerException("The request parameter is null.");
        }
        this.request = request;
    }

    @Override
    public void clear() {
        List<String> names = new ArrayList<String>();
        for (Enumeration<?> e = request.getAttributeNames(); e
            .hasMoreElements();) {
            names.add(StringUtil.toString(e.nextElement()));
        }
        for (String name : names) {
            request.removeAttribute(name);
        }
    }

    @Override
    public boolean containsKey(Object key) {
        for (Enumeration<?> e = request.getAttributeNames(); e
            .hasMoreElements();) {
            String k = StringUtil.toString(e.nextElement());
            if (key == null && k == null || key != null && key.equals(k)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        for (Enumeration<?> e = request.getAttributeNames(); e
            .hasMoreElements();) {
            String k = StringUtil.toString(e.nextElement());
            Object v = request.getAttribute(k);
            if (value == null && v == null || value != null && value.equals(v)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Set<Entry<String, Object>> entrySet() {
        Set<Entry<String, Object>> set = new HashSet<Entry<String, Object>>();
        for (Enumeration<?> e = request.getAttributeNames(); e
            .hasMoreElements();) {
            String key = StringUtil.toString(e.nextElement());
            set.add(new EntryImpl(key, request.getAttribute(key)));
        }
        return set;
    }

    @Override
    public Object get(Object key) {
        return request.getAttribute(StringUtil.toString(key));
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public Set<String> keySet() {
        Set<String> set = new HashSet<String>();
        for (Enumeration<?> e = request.getAttributeNames(); e
            .hasMoreElements();) {
            set.add(StringUtil.toString(e.nextElement()));
        }
        return set;
    }

    @Override
    public Object put(String key, Object value) {
        Object old = request.getAttribute(key);
        request.setAttribute(key, value);
        return old;
    }

    @Override
    public void putAll(Map<? extends String, ? extends Object> m) {
        for (String key : m.keySet()) {
            request.setAttribute(key, m.get(key));
        }
    }

    @Override
    public Object remove(Object key) {
        String k = StringUtil.toString(key);
        Object old = request.getAttribute(k);
        request.removeAttribute(k);
        return old;
    }

    @Override
    public int size() {
        int size = 0;
        for (Enumeration<?> e = request.getAttributeNames(); e
            .hasMoreElements(); e.nextElement()) {
            size++;
        }
        return size;
    }

    @Override
    public Collection<Object> values() {
        List<Object> list = new ArrayList<Object>();
        for (Enumeration<?> e = request.getAttributeNames(); e
            .hasMoreElements();) {
            list
                .add(request.getAttribute(StringUtil.toString(e.nextElement())));
        }
        return list;
    }

    /**
     * An implementation for {@link Entry}.
     */
    protected static class EntryImpl implements Entry<String, Object>,
            Serializable {

        private static final long serialVersionUID = 1L;

        /**
         * The key.
         */
        protected String key;

        /**
         * The value.
         */
        protected Object value;

        /**
         * Constructor.
         * 
         * @param key
         *            the key
         * @param value
         *            the value
         */
        public EntryImpl(String key, Object value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public Object getValue() {
            return value;
        }

        public Object setValue(Object value) {
            Object old = value;
            this.value = value;
            return old;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof Entry<?, ?>)) {
                return false;
            }
            Entry<?, ?> other = (Entry<?, ?>) obj;
            if (key == null) {
                return other.getKey() == null;
            }
            return key.equals(other.getKey());
        }

        @Override
        public int hashCode() {
            if (key == null) {
                return 0;
            }
            return key.hashCode();
        }
    }
}