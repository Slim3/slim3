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
package org.slim3.util;

import com.google.appengine.api.datastore.Text;

/**
 * A converter for Text.
 * 
 * @author higa
 * @since 3.0
 * 
 */
public class TextConverter implements Converter {

    @Override
    public Object getAsObject(String value) {
        if (value == null) {
            return null;
        }
        return new Text(value);
    }

    @Override
    public String getAsString(Object value) {
        if (value == null) {
            return null;
        }
        return Text.class.cast(value).getValue();
    }

    @Override
    public boolean isTarget(Class<?> clazz) {
        return clazz == Text.class;
    }
}