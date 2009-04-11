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
package org.slim3.commons.util;

/**
 * A utility class for {@link Character}.
 * 
 * @author higa
 * @version 3.0
 */
public final class CharacterUtil {

    private CharacterUtil() {
    }

    /**
     * Converts the object to {@link Character}.
     * 
     * @param o
     *            the object
     * @return the converted value
     */
    public static Character toCharacter(Object o) {
        if (o == null) {
            return null;
        } else if (o instanceof Character) {
            return (Character) o;
        } else {
            String s = o.toString();
            if (s.length() == 0) {
                return null;
            }
            if (s.length() > 1) {
                throw new IllegalStateException("The length(" + s.length()
                        + ") of \"" + s + "\" should be 1.");
            }
            return Character.valueOf(s.charAt(0));
        }
    }

    /**
     * Converts the object to primitive char.
     * 
     * @param o
     *            the object
     * @return the converted value
     */
    public static char toPrimitiveChar(Object o) {
        Character c = toCharacter(o);
        if (c == null) {
            return 0;
        }
        return c.charValue();
    }
}