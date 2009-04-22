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

import java.util.List;

import javax.jdo.annotations.IdentityType;

import org.slim3.gae.jdo.JDOTemplate;
import org.slim3.gae.unit.LocalJDOTestCase;

/**
 * @author higa
 * 
 */
public class JDOTemplateTest extends LocalJDOTestCase {

    /**
     * @throws Exception
     */
    public void test() throws Exception {
        Sample sample = new Sample();
        sample.setName("hoge");
        sample.setMyBoolean(true);
        sample.setMyByte(toByte(1));
        sample.setMyShort(toShort(1));
        sample.setMyInteger(1);
        sample.setMyLong(toLong(1));
        sample.setMyFloat(toFloat(1));
        sample.setMyDouble(toDouble(1));
        sample.setMyDate(new java.util.Date(1));
        sample.setMyEnum(IdentityType.APPLICATION);
        pm.makePersistent(sample);
        List<Sample> ret = new JDOTemplate<List<Sample>>() {
            @Override
            public List<Sample> doExecute() {
                SampleMeta s = new SampleMeta();
                return from(s)
                    .where(s.id.eq(toLong(1)), s.name.eq("hoge"))
                    .orderBy(s.id.asc(), s.name.desc())
                    .getResultList();
            }
        }.execute();
        System.out.println(ret);
    }
}