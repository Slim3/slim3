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

/**
 * Represents a view description.
 * 
 * @author taedium
 * @since 1.0.0
 */
public class ViewDesc {

    /** the dirName */
    protected String dirName;

    /** the fileName */
    protected String fileName;

    /** the relativePath */
    protected String relativePath;

    /** the title */
    protected String title;

    /**
     * Returns the dirName.
     * 
     * @return the dirName
     */
    public String getDirName() {
        return dirName;
    }

    /**
     * Sets the the dirName.
     * 
     * @param dirName
     *            the dirName to set
     */
    public void setDirName(String dirName) {
        this.dirName = dirName;
    }

    /**
     * Returns the fileName.
     * 
     * @return the fileName
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Sets the fileName.
     * 
     * @param fileName
     *            the fileName to set
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Returns the relativePath.
     * 
     * @return the relativePath
     */
    public String getRelativePath() {
        return relativePath;
    }

    /**
     * Sets the relativePath.
     * 
     * @param relativePath
     *            the relativePath to set
     */
    public void setRelativePath(String relativePath) {
        this.relativePath = relativePath;
    }

    /**
     * Returns the title.
     * 
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns the title.
     * 
     * @param title
     *            the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

}
