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
package org.slim3.controller.router;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.slim3.util.WrapRuntimeException;

/**
 * A class to route a path.
 * 
 * @author higa
 * @since 1.0.0
 * 
 */
public class Routing {

    /**
     * The "from" path.
     */
    protected String from;

    /**
     * {@link Pattern} for "from".
     */
    protected Pattern fromPattern;

    /**
     * The list of place holders.
     */
    protected List<String> placeHolderList = new ArrayList<String>();

    /**
     * The "to" path.
     */
    protected String to;

    /**
     * The list of {@link ToFragment}s.
     */
    protected List<ToFragment> toFragmentList = new ArrayList<ToFragment>();

    /**
     * Constructor.
     * 
     * @param from
     *            the "from" path
     * @param to
     *            the "to" path
     * @throws NullPointerException
     *             if the from parameter is null or if the to parameter is null
     */
    public Routing(String from, String to) throws NullPointerException {
        setFrom(from);
        setTo(to);
    }

    /**
     * Sets the "from" path.
     * 
     * @param from
     *            the "from" path
     * @throws NullPointerException
     *             if the from parameter is null
     */
    protected void setFrom(String from) throws NullPointerException {
        if (from == null) {
            throw new NullPointerException("The from parameter is null.");
        }
        this.from = from;
        StringBuilder sb = new StringBuilder(50);
        char[] chars = from.toCharArray();
        int length = chars.length;
        int index = -1;
        boolean catchAll = false;
        for (int i = 0; i < length; i++) {
            if (chars[i] == '{') {
                if (index < 0) {
                    index = i;
                } else if (catchAll) {
                    throw new IllegalArgumentException("The from path("
                            + from
                            + ") is invalid, because of \"{\" after \"*\".");
                } else {
                    throw new IllegalArgumentException("The from path("
                        + from
                        + ") is invalid, because \"}\" is missing.");
                }
            } else if (chars[i] == '}') {
                if (index >= 0) {
                    sb.append("([^/]+)");
                    placeHolderList.add(from.substring(index + 1, i));
                    index = -1;
                } else if (catchAll) {
                    throw new IllegalArgumentException("The from path("
                            + from
                            + ") is invalid, because of \"}\" after \"*\".");
                } else {
                    throw new IllegalArgumentException("The from path("
                        + from
                        + ") is invalid, because \"{\" is missing.");
                }
            } else if (chars[i] == '*') {
                if (index < 0) {
                    index = i;
                    catchAll = true;
                } else if (catchAll) {
                    throw new IllegalArgumentException("The from path("
                            + from
                            + ") is invalid, because of duplicate \"*\".");
                } else {
                    throw new IllegalArgumentException("The from path("
                            + from
                            + ") is invalid, because \"}\" is missing.");
                }
            } else if (index < 0) {
                sb.append(chars[i]);
            }
        }
        if (catchAll) {
            sb.append("([^*]+)");
            placeHolderList.add(from.substring(index + 1));
            index = -1;
        } else if (index >= 0) {
            throw new IllegalArgumentException("The from path("
                + from
                + ") is invalid, because \"}\" is missing.");
        }
        fromPattern = Pattern.compile("^" + sb.toString() + "$");
    }

    /**
     * Sets the "to" path.
     * 
     * @param to
     *            the "to" path
     */
    protected void setTo(String to) {
        if (to == null) {
            throw new NullPointerException("The to parameter is null.");
        }
        this.to = to;
        StringBuilder sb = new StringBuilder(50);
        char[] chars = to.toCharArray();
        int length = chars.length;
        int index = -1;
        for (int i = 0; i < length; i++) {
            if (chars[i] == '{') {
                if (index < 0) {
                    toFragmentList.add(new StringFragment(sb.toString()));
                    sb.setLength(0);
                    index = i;
                } else {
                    throw new IllegalArgumentException("The to path("
                        + to
                        + ") is invalid, because \"}\" is missing.");
                }
            } else if (chars[i] == '}') {
                if (index >= 0) {
                    String name = to.substring(index + 1, i);
                    if (placeHolderList.indexOf(name) < 0) {
                        throw new IllegalArgumentException("The to path("
                            + to
                            + ") is invalid, because the name("
                            + name
                            + ") is not found in from path("
                            + from
                            + ").");
                    }
                    toFragmentList.add(new PlaceHolderFragment(name));
                    index = -1;
                } else {
                    throw new IllegalArgumentException("The to path("
                        + to
                        + ") is invalid, because \"{\" is missing.");
                }
            } else if (index < 0) {
                sb.append(chars[i]);
            }
        }
        if (index >= 0) {
            throw new IllegalArgumentException("The to path("
                + to
                + ") is invalid, because \"}\" is missing.");
        }
        if (sb.length() > 0) {
            toFragmentList.add(new StringFragment(sb.toString()));
        }
    }

    /**
     * Routes the path.
     * 
     * @param request
     *            the request
     * @param path
     *            the path
     * @return a routed path
     * @throws NullPointerException
     *             if the request parameter is null or if the path parameter is
     *             null
     */
    public String route(HttpServletRequest request, String path)
            throws NullPointerException {
        if (request == null) {
            throw new NullPointerException("The request parameter is null.");
        }
        if (path == null) {
            throw new NullPointerException("The path parameter is null.");
        }
        Matcher matcher = fromPattern.matcher(path);
        if (!matcher.find()) {
            return null;
        }
        Map<String, String> placeHolderValues = new HashMap<String, String>();
        int index = 1;
        for (String name : placeHolderList) {
            placeHolderValues.put(name, matcher.group(index++));
        }
        StringBuilder to = new StringBuilder(50);
        for (ToFragment f : toFragmentList) {
            f.append(request, to, placeHolderValues);
        }
        return to.toString();
    }

    /**
     * A fragment of "to" path.
     * 
     */
    protected static interface ToFragment {

        /**
         * Appends a part of "to" path.
         * 
         * @param request
         *            the request
         * @param to
         *            the "to" path
         * @param placeHolderValues
         *            the map of place holders
         */
        void append(HttpServletRequest request, StringBuilder to,
                Map<String, String> placeHolderValues);
    }

    /**
     * {@link ToFragment} for String.
     * 
     */
    protected static class StringFragment implements ToFragment {

        /**
         * The value.
         */
        protected String value;

        /**
         * Constructor.
         * 
         * @param value
         *            the value
         * @throws NullPointerException
         *             if the value parameter is null
         */
        public StringFragment(String value) throws NullPointerException {
            if (value == null) {
                throw new NullPointerException("The value parameter is null");
            }
            this.value = value;
        }

        public void append(HttpServletRequest request, StringBuilder to,
                Map<String, String> placeHolderValues) {
            to.append(value);
        }
    }

    /**
     * {@link ToFragment} for String.
     * 
     */
    protected static class PlaceHolderFragment implements ToFragment {

        /**
         * The name.
         */
        protected String name;

        /**
         * Constructor.
         * 
         * @param name
         *            the value
         * @throws NullPointerException
         *             if the name parameter is null
         */
        public PlaceHolderFragment(String name) throws NullPointerException {
            if (name == null) {
                throw new NullPointerException("The name parameter is null");
            }
            this.name = name;
        }

        public void append(HttpServletRequest request, StringBuilder to,
                Map<String, String> placeHolderValues) {
            to.append(encode(placeHolderValues.get(name), request
                .getCharacterEncoding()));
        }

        /**
         * Encodes the path as a part of {@link URL}.
         * 
         * @param path
         *            the path
         * @param encoding
         *            the encoding
         * @return an encoded path
         * @throws WrapRuntimeException
         *             if {@link UnsupportedEncodingException} occurred
         */
        protected String encode(String path, String encoding)
                throws WrapRuntimeException {
            if (path == null) {
                return "";
            }
            if (encoding == null) {
                encoding = "UTF-8";
            }
            try {
                return URLEncoder.encode(path, encoding);
            } catch (UnsupportedEncodingException e) {
                throw new WrapRuntimeException(e);
            }
        }
    }
}