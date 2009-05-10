/*
 * Copyright the original author or authors.
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

import java.util.Map;

/**
 * A class to copy a bean to an another bean.
 * 
 * @author higa
 * @since 3.0
 * 
 */
public class Copy extends AbstractCopy<Copy> {

    /**
     * The source.
     */
    protected Object src;

    /**
     * The destination.
     */
    protected Object dest;

    /**
     * Constructor.
     * 
     * @param src
     *            the source
     * @param dest
     *            the destination
     * @throws NullPointerException
     *             if the src parameter is null or if the dest parameter is null
     * @throws IllegalArgumentException
     *             if the src parameter is map or if the dest parameter is map
     */
    public Copy(Object src, Object dest) throws NullPointerException,
            IllegalArgumentException {
        if (src == null) {
            throw new NullPointerException("The src parameter is null.");
        }
        if (dest == null) {
            throw new NullPointerException("The dest parameter is null.");
        }
        this.src = src;
        this.dest = dest;
    }

    /**
     * Executes copy process.
     */
    @SuppressWarnings("unchecked")
    public void execute() {
        if (src instanceof Map && dest instanceof Map) {
            copyMapToMap((Map) src, (Map) dest);
        } else if (src instanceof Map) {
            copyMapToBean((Map) src, dest);
        } else if (dest instanceof Map) {
            copyBeanToMap(src, (Map) dest);
        } else {
            copyBeanToBean(src, dest);
        }
    }
}