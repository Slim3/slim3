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

import java.math.BigDecimal;

/**
 * A utility class for {@link BigDecimal}.
 * 
 * @author higa
 * @version 3.0
 */
public final class BigDecimalUtil {

    private BigDecimalUtil() {
    }

    /**
     * Converts the object to {@link BigDecimal}.
     * 
     * @param o
     *            the object
     * @return the converted value
     */
    public static BigDecimal toBigDecimal(Object o) {
        if (o == null) {
            return null;
        } else if (o instanceof BigDecimal) {
            return (BigDecimal) o;
        } else if (o.getClass() == String.class) {
            String s = (String) o;
            if (StringUtil.isEmpty(s)) {
                return null;
            }
            return new BigDecimal(s);
        } else {
            return new BigDecimal(o.toString());
        }
    }
}