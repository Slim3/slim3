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
package org.slim3.datastore;

import java.util.Comparator;

import com.google.appengine.api.datastore.Key;

/**
 * {@link Comparator} for {@link Key}.
 * 
 * @author higa
 * @since 1.0.0
 * 
 */
public final class KeyDescComparator implements Comparator<Key> {

    /**
     * The instance.
     */
    public static final Comparator<Key> INSTANCE = new KeyDescComparator();

    private KeyDescComparator() {
    }

    public int compare(Key o1, Key o2) {
        return -1 * o1.compareTo(o2);
    }
}