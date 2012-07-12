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

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.slim3.datastore.meta.HogeMeta;

import com.google.appengine.api.datastore.Query.CompositeFilter;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;

/**
 * @author higa
 * 
 */
public class CompositeCriterionTest {

    private HogeMeta meta = new HogeMeta();

    /**
     * @throws Exception
     */
    @Test
    public void getOperator() throws Exception {
        CompositeFilterOperator operator = CompositeFilterOperator.AND;
        FilterCriterion[] criteria = new FilterCriterion[0];
        CompositeCriterion criterion =
            new CompositeCriterion(meta, operator, criteria);
        assertThat(criterion.getOperator(), is(operator));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getSubFilters() throws Exception {
        CompositeFilterOperator operator = CompositeFilterOperator.AND;
        FilterCriterion[] criteria = new FilterCriterion[0];
        CompositeCriterion criterion =
            new CompositeCriterion(meta, operator, criteria);
        assertThat(criterion.getCriteria(), is(criteria));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getFiltersForZeroFilter() throws Exception {
        CompositeFilterOperator operator = CompositeFilterOperator.AND;
        FilterCriterion[] criteria = new FilterCriterion[0];
        CompositeCriterion criterion =
            new CompositeCriterion(meta, operator, criteria);
        assertThat(criterion.getFilters().length, is(0));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getFiltersForOneFilter() throws Exception {
        CompositeFilterOperator operator = CompositeFilterOperator.AND;
        FilterCriterion[] criteria =
            new FilterCriterion[] { meta.myString.equal("aaa") };
        CompositeCriterion criterion =
            new CompositeCriterion(meta, operator, criteria);
        assertThat(criterion.getFilters().length, is(1));
        assertThat(criterion.getFilters()[0], is(criteria[0].getFilters()[0]));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getFiltersForMultipleFilters() throws Exception {
        CompositeFilterOperator operator = CompositeFilterOperator.AND;
        FilterCriterion[] criteria =
            new FilterCriterion[] {
                meta.myString.equal("aaa"),
                meta.myInteger.equal(1) };
        CompositeCriterion criterion =
            new CompositeCriterion(meta, operator, criteria);
        assertThat(criterion.getFilters().length, is(1));
        assertThat(criterion.getFilters()[0], is(CompositeFilter.class));
        CompositeFilter cf = (CompositeFilter) criterion.getFilters()[0];
        assertThat(cf.getOperator(), is(operator));
        assertThat(cf.getSubFilters().size(), is(2));

    }
}