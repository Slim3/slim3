/*
 * Copyright 2004-2009 the original author or authors.
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

import java.util.ArrayList;
import java.util.Arrays;

import junit.framework.TestCase;

import org.slim3.datastore.meta.HogeMeta;

import com.google.appengine.api.datastore.Query;

/**
 * @author higa
 * 
 */
public class AbstFilterCriterionTest extends TestCase {

    private HogeMeta meta = new HogeMeta();

    /**
     * @throws Exception
     * 
     */
    public void testGetFirstElement() throws Exception {
        MyCriterion c = new MyCriterion(meta.myIntegerList);
        assertNull(c.getFirstElement(new ArrayList<Integer>()));
        assertEquals(1, c.getFirstElement(Arrays.asList(1, 2)));
    }

    private static class MyCriterion extends AbstractSortCriterion {

        /**
         * @param attributeMeta
         * @throws NullPointerException
         */
        public MyCriterion(AbstractAttributeMeta<?, ?> attributeMeta)
                throws NullPointerException {
            super(attributeMeta);
        }

        public void apply(Query query) {
        }

        public int compare(Object model1, Object model2)
                throws IllegalStateException {
            return 0;
        }

    }
}