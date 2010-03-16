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
package org.slim3.gen.desc;

import org.slim3.gen.Constants;
import org.slim3.gen.util.StringUtil;

/**
 * Creates a view description.
 * 
 * @author taedium
 * @since 1.0.0
 */
public class ViewDescFactory {

    /**
     * Creates a view description.
     * 
     * @param path
     *            the path to a view.
     * @return a view description.
     */
    public ViewDesc createViewDesc(String path) {
        if (path == null) {
            throw new NullPointerException("The path parameter is null.");
        }
        if (!path.startsWith("/")) {
            throw new IllegalArgumentException(
                "The path does not start with '/'.");
        }
        int pos = path.lastIndexOf("/");
        String dirName = path.substring(0, pos);
        String fileName = path.substring(pos + 1);
        if (fileName.length() == 0) {
            fileName = Constants.INDEX_VIEW;
        } else {
            fileName += Constants.VIEW_SUFFIX;
        }
        ViewDesc viewDesc = new ViewDesc();
        viewDesc.setDirName(dirName);
        viewDesc.setFileName(fileName);
        viewDesc.setRelativePath(toRelativePath(path));
        viewDesc.setTitle(toTitle(path));
        return viewDesc;
    }

    /**
     * Converts the path to a relative path.
     * 
     * @param path
     *            the path to a view.
     * @return a relative path.
     */
    protected String toRelativePath(String path) {
        int pos = path.lastIndexOf("/");
        return path.substring(pos + 1);
    }

    /**
     * Converts the path to a title.
     * 
     * @param path
     *            the path to a view.
     * @return a view title.
     */
    protected String toTitle(String path) {
        if (path.endsWith("/")) {
            return path.substring(1).replace("/", " ")
                + StringUtil.capitalize(Constants.INDEX);
        }
        int pos = path.lastIndexOf("/");
        if (pos == 0) {
            return StringUtil.capitalize(path.substring(1));
        } else if (pos > 0) {
            return path.substring(1, pos).replace("/", " ")
                + " "
                + StringUtil.capitalize(path.substring(pos + 1));
        }
        throw new IllegalArgumentException("path");
    }
}
