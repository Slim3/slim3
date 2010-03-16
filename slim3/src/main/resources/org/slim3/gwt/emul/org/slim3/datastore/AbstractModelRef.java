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

import java.io.Serializable;

import org.slim3.datastore.Datastore;
import org.slim3.datastore.ModelMeta;
import org.slim3.util.ClassUtil;

public class AbstractModelRef<M> implements Serializable {

    private static final long serialVersionUID = 1L;
    
    protected String modelClassName;
    
    protected AbstractModelRef() {
    }
    
    public AbstractModelRef(Class<M> modelClass) throws NullPointerException {
        modelClassName = modelClass.getName();
    }
    
    public Class<M> getModelClass() {
        throw new UnsupportedOperationException("This method is unsupported on GWT.");
    }
    
    public ModelMeta<M> getModelMeta() {
        throw new UnsupportedOperationException("This method is unsupported on GWT.");
    }
}