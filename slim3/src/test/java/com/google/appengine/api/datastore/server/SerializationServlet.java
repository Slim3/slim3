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
package com.google.appengine.api.datastore.server;

import com.google.appengine.api.datastore.Category;
import com.google.appengine.api.datastore.Email;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.GeoPt;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Link;
import com.google.appengine.api.datastore.PhoneNumber;
import com.google.appengine.api.datastore.PostalAddress;
import com.google.appengine.api.datastore.Rating;
import com.google.appengine.api.datastore.ShortBlob;
import com.google.appengine.api.datastore.client.SerializationService;
import com.google.appengine.api.datastore.server.meta.BeanMeta;
import com.google.appengine.api.datastore.shared.model.Bean;
import com.google.appengine.api.users.User;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import org.slim3.tester.AppEngineTester;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author galdolber
 *
 */
public class SerializationServlet extends RemoteServiceServlet implements SerializationService {

    private static final long serialVersionUID = 272177304719622205L;

    /**
     * Tester.
     */
    protected AppEngineTester tester = new AppEngineTester();
    {
        try {
            tester.setUp();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    public Key getKey(Key key) {
        return key;
    }
    
    public Entity getEntity(Bean bean) {
        Entity entity = BeanMeta.get().modelToEntity(bean);
        byte[] bytes = new byte[]{1,2,3};
        entity.setProperty("shortBlob", new ShortBlob(bytes ));
        entity.setProperty("user", new User("3@3.com", "domain", "id", "identity"));
        entity.setProperty("category", new Category("category"));
        entity.setProperty("email", new Email("3@3.com"));
        entity.setProperty("geopt", new GeoPt(23.0f, 54.3f));
        entity.setProperty("link", new Link("http://www.3.com"));
        entity.setProperty("phoneNumber", new PhoneNumber("333-333-3333"));
        entity.setProperty("postalAddress", new PostalAddress("333 three 3¬∫"));
        entity.setProperty("rating", new Rating(33));
        return entity;
    }

    public ArrayList<String> forceListInclution() {
        return new ArrayList<String>();
    }

    public HashMap<String, String> forceMapInclution() {
        return new HashMap<String, String>();
    }
}
