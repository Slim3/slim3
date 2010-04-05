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

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.slim3.tester.AppEngineTestCase;
import org.slim3.util.ByteUtil;

import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.repackaged.com.google.common.util.Base64;

/**
 * @author higa
 * 
 */
public class SpikeTest extends AppEngineTestCase {

    /**
     * @throws Exception
     */
    @Test
    public void spike() throws Exception {
        List<FilterPredicate> list = new ArrayList<FilterPredicate>();
        list.add(new FilterPredicate("aaa", FilterOperator.EQUAL, "111"));
        String encodedString = Base64.encode(ByteUtil.toByteArray(list));
        List<FilterPredicate> list2 =
            ByteUtil.toObject(Base64.decode(encodedString));
        assertThat(list2.size(), is(1));
        assertThat(list2.get(0).getPropertyName(), is("aaa"));
        assertThat(list2.get(0).getOperator(), is(FilterOperator.EQUAL));
        assertThat((String) list2.get(0).getValue(), is("111"));
    }
}