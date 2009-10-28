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

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.slim3.datastore.meta.HogeMeta;
import org.slim3.datastore.model.Hoge;

/**
 * @author higa
 * 
 */
public class AbstCriterionTest {

    private HogeMeta meta = new HogeMeta();

    /**
     * @throws Exception
     * 
     */
    @SuppressWarnings("unchecked")
    @Test
    public void constructor() throws Exception {
        MyCriterion criterion = new MyCriterion(meta.myInteger);
        assertThat(
            (CoreAttributeMeta) criterion.attributeMeta,
            is(sameInstance(meta.myInteger)));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void compareValue() throws Exception {
        MyCriterion criterion = new MyCriterion(meta.myInteger);
        assertThat(criterion.compareValue(1, 2), is(-1));
        assertThat(criterion.compareValue(1, 1), is(0));
        assertThat(criterion.compareValue(2, 1), is(1));
        assertThat(criterion.compareValue(null, 1), is(-1));
        assertThat(criterion.compareValue(null, null), is(0));
        assertThat(criterion.compareValue(1, null), is(1));
    }

    private static class MyCriterion extends AbstractCriterion {

        /**
         * @param attributeMeta
         * @throws NullPointerException
         */
        public MyCriterion(AbstractAttributeMeta<Hoge, Integer> attributeMeta)
                throws NullPointerException {
            super(attributeMeta);
        }

    }
}