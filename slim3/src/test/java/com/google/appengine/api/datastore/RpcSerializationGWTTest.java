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
package com.google.appengine.api.datastore;

import com.google.appengine.api.datastore.client.SerializationService;
import com.google.appengine.api.datastore.client.SerializationServiceAsync;
import com.google.appengine.api.datastore.shared.model.Bean;
import com.google.gwt.core.client.GWT;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * @author galdolber
 *
 */
public class RpcSerializationGWTTest extends GWTTestCase {
    

    @Override
    public String getModuleName() {
        return "com.google.appengine.api.datastore.Datastore";
    }

    /**
     * Test key serialization.
     */
    public void testKeySerialization() {
        SerializationServiceAsync service = GWT.create(SerializationService.class);
        final Key key = KeyFactory.createKey("Some", 3);
        service.getKey(key, new AsyncCallback<Key>() {
            public void onSuccess(Key e) {
                assertEquals(key, e);
                finishTest();
            }
            public void onFailure(Throwable caught) {
                caught.printStackTrace();
                fail();
            }
        });
        delayTestFinish(500);
    }
    
    /**
     * Test entity serialization.
     */
    public void testEntitySerialization() {
        SerializationServiceAsync service = GWT.create(SerializationService.class);
        Bean bean = new Bean();
        bean.setB(false);
        bean.setD(3.3);
        bean.setDa(new Date());
        bean.setF(23.4f);
        bean.setKey(KeyFactory.createKey("Bean", 3));
        ArrayList<String> list = new ArrayList<String>();
        list.add("1");
        list.add("2");
        list.add("3");
        bean.setList(list );
        bean.setS("String");
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("1", "1");
        map.put("2", "2");
        map.put("3", "3");
        bean.setMap(map );
        
        service.getEntity(bean, new AsyncCallback<Entity>() {
            public void onSuccess(Entity e) {
                finishTest();
            }
            public void onFailure(Throwable caught) {
                caught.printStackTrace();
                fail();
            }
        });
        delayTestFinish(500);
    }
}
