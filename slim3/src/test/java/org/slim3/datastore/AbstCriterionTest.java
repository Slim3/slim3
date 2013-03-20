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
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.hasItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.slim3.datastore.meta.BbbMeta;
import org.slim3.datastore.meta.HogeMeta;
import org.slim3.datastore.model.Bbb;
import org.slim3.tester.AppEngineTestCase;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Query.SortDirection;

/**
 * @author higa
 * 
 */
public class AbstCriterionTest extends AppEngineTestCase {

    private HogeMeta meta = new HogeMeta();

    /**
     * @throws Exception
     * 
     */
    @SuppressWarnings({ "rawtypes" })
    @Test
    public void constructor() throws Exception {
        MyCriterion criterion = new MyCriterion(meta.myInteger);
        assertThat(
            (CoreAttributeMeta) criterion.attributeMeta,
            is(sameInstance((CoreAttributeMeta) meta.myInteger)));
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

    /**
     * @throws Exception
     * 
     */
    @Test
    public void convertValueForDatastoreForNoConvertedValue() throws Exception {
        MyCriterion criterion = new MyCriterion(meta.myString);
        assertThat(
            (String) criterion.convertValueForDatastore("ASCENDING"),
            is("ASCENDING"));
        assertThat(criterion.convertValueForDatastore(null), is(nullValue()));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void convertValueForDatastoreForEnum() throws Exception {
        MyCriterion criterion = new MyCriterion(meta.myEnum);
        assertThat((String) criterion
            .convertValueForDatastore(SortDirection.ASCENDING), is("ASCENDING"));
    }

    /**
     * @throws Exception
     * 
     */
    @SuppressWarnings("unchecked")
    @Test
    public void convertValueForDatastoreForEnumList() throws Exception {
        MyCriterion criterion = new MyCriterion(meta.myEnumList);
        assertThat((List<String>) criterion.convertValueForDatastore(Arrays
            .asList(SortDirection.ASCENDING)), hasItem("ASCENDING"));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void convertValueForDatastoreForNullIterable() throws Exception {
        MyCriterion criterion = new MyCriterion(meta.myString);
        assertThat(
            criterion.convertValueForDatastore((Iterable<?>) null),
            is(nullValue()));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void convertValueForDatastoreForEmptyIterable() throws Exception {
        MyCriterion criterion = new MyCriterion(meta.myString);
        assertThat(criterion
            .convertValueForDatastore(new ArrayList<String>())
            .size(), is(0));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void convertValueForDatastoreForModelRef() throws Exception {
        BbbMeta bbbMeta = BbbMeta.get();
        MyCriterion criterion = new MyCriterion(bbbMeta.hogeRef);
        Bbb bbb = new Bbb();
        Key key = Datastore.createKey("Hoge", 1);
        bbb.getHogeRef().setKey(key);
        assertThat(
            (Key) criterion.convertValueForDatastore(bbb.getHogeRef()),
            is(key));
    }

    private static class MyCriterion extends AbstractCriterion {

        /**
         * @param attributeMeta
         * @throws NullPointerException
         */
        public MyCriterion(AttributeMeta<?, ?> attributeMeta)
                throws NullPointerException {
            super(attributeMeta);
        }

    }
}