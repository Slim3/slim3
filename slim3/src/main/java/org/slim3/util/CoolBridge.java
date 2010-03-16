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

/**
 * A bridge class between a cool object and a hot reloadable object.
 * 
 * @author higa
 * @since 1.0.0
 * 
 */
public final class CoolBridge {

    /**
     * Creates a HOT reloadable object.
     * 
     * @param <T>
     *            type
     * @param interfaceClass
     *            the interface class
     * @return a HOT reloable object
     * @throws NullPointerException
     *             if the interfaceClass parameter is null
     * @throws IllegalArgumentException
     *             if the class is not an interface or if the class is not under
     *             cool package
     * @throws WrapRuntimeException
     *             if the implementation class is not found
     */
    @SuppressWarnings("unchecked")
    public static <T> T create(Class<T> interfaceClass)
            throws NullPointerException, IllegalArgumentException,
            WrapRuntimeException {
        if (interfaceClass == null) {
            throw new NullPointerException(
                "The interfaceClass must not be null.");
        }
        String interfaceName = interfaceClass.getName();
        if (!interfaceClass.isInterface()) {
            throw new IllegalArgumentException("The class("
                + interfaceName
                + ") must be interface.");
        }
        if (!interfaceName.contains(".cool.")) {
            throw new IllegalArgumentException("The class("
                + interfaceName
                + ") must be under cool package.");
        }
        String implName = interfaceName.replace(".cool.", ".") + "Impl";
        Class<T> clazz = ClassUtil.forName(implName);
        if (!interfaceClass.isAssignableFrom(clazz)) {
            throw new IllegalArgumentException("The class("
                + implName
                + ") does not implement "
                + interfaceName
                + ".");
        }
        return (T) ClassUtil.newInstance(clazz);
    }

    private CoolBridge() {
    }
}