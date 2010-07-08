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

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

/**
 * This listener sets the email address of the current user when a model is
 * updated.
 * 
 * @author higa
 * @since 1.0.5
 * 
 */
public class ModificationEmail implements AttributeListener<String> {

    public String prePut(String value) {
        UserService us = UserServiceFactory.getUserService();
        User user = us.getCurrentUser();
        if (user != null) {
            return user.getEmail();
        }
        return null;
    }

}
