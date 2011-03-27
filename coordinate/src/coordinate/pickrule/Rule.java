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
package coordinate.pickrule;

import coordinate.model.Item;

/**
 * This interface represents a rule of item combination
 * 
 * @author higayasuo
 * @since 1.0.0
 * 
 */
public interface Rule {

    /**
     * Determines if the combination of first item and second one is suitable.
     * Returns true if its combination is suitable.
     * 
     * @param first
     *            the first item
     * @param second
     *            the second item
     * @return whether the combination of first item and second one is suitable.
     */
    boolean isSuitable(Item first, Item second);
}