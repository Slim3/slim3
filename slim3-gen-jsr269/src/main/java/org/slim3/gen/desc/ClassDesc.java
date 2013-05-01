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
 * Represents a class description.
 * 
 * @author taedium
 * @since 1.0.0
 */
public interface ClassDesc {

    /**
     * Returns the packageName.
     * 
     * @return the packageName
     */
    public String getPackageName();

    /**
     * Returns the simpleName.
     * 
     * @return the simpleName
     */
    public String getSimpleName();

    /**
     * Returns the qualifiedName.
     * 
     * @return the qualifiedName.
     */
    public String getQualifiedName();
}
