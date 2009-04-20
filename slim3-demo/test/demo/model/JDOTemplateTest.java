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
import org.slim3.gae.jdo.PM;
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
        List<Sample> ret = new JDOTemplate<List<Sample>>() {
            @Override
            public List<Sample> doExecute() {
                Sample s = new Sample();
                s.setName("hoge");
                s.setEnabled(true);
                s.setMyByte((byte) 1);
                s.setMyShort((short) 1);
                s.setMyInteger(1);
                s.setMyLong((long) 1);
                s.setMyFloat((float) 1);
                s.setMyDouble((double) 1);
                s.setMyDate(new java.util.Date(1));
                s.setMyEnum(IdentityType.APPLICATION);
                PM.getCurrent().makePersistent(s);
                SampleMeta sample = new SampleMeta();
                return from(sample).where(sample.id.eq(Long.valueOf(1)))
                        .getResultList();
            }
        }.execute();
        System.out.println(ret);
    }
}