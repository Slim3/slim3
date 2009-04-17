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
package demo.model;

import java.util.Collection;

import javax.jdo.Query;

import org.slim3.gae.jdo.JDOTemplate;
import org.slim3.gae.jdo.PM;
import org.slim3.gae.unit.LocalJDOTestCase;

/**
 * @author higa
 * 
 */
public class QueryTest extends LocalJDOTestCase {

    /**
     * @throws Exception
     */
    public void testEq() throws Exception {
        Collection<Sample> ret = new JDOTemplate<Collection<Sample>>() {
            @SuppressWarnings("unchecked")
            @Override
            public Collection<Sample> doExecute() {
                Sample s = new Sample();
                s.setName("hoge");
                s.setEnabled(true);
                s.setMyByte((byte) 1);
                s.setMyShort((short) 1);
                s.setMyInteger(1);
                s.setMyLong((long) 1);
                s.setMyFloat((float) 1);
                s.setMyDouble((double) 1);
                PM.getCurrent().makePersistent(s);
                Query query = PM.getCurrent().newQuery(
                        "select from " + Sample.class.getName()
                                + " where myDouble == myDoubleParam");
                query.declareParameters("Double myDoubleParam");
                return (Collection<Sample>) query.execute((double) 1);
            }
        }.execute();
        System.out.println(ret);
    }
}