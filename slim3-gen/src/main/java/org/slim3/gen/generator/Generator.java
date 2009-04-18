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
package org.slim3.gen.generator;

import javax.lang.model.element.Element;

/**
 * Generates something from an element.
 * 
 * @author taedium
 * @since 3.0
 * 
 * @param <R>
 *            the return type of the {@link #generate(Element, Object)} method.
 * @param <E>
 *            the element type.
 * @param <P>
 *            the type of the additional parameter to the
 *            {@link #generate(Element, Object)} method.
 */
public interface Generator<R, E extends Element, P> {

    /**
     * Generates something from an element.
     * 
     * @param e
     *            the element object.
     * @param p
     *            the additional parameter.
     * @return the result.
     */
    R generate(E e, P p);
}
