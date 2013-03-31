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
package org.slim3.test.model;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.InverseModelRef;
import org.slim3.datastore.Model;
import org.slim3.datastore.ModelRef;

import com.google.appengine.api.datastore.Key;

/**
 * @author vvakame
 */
@Model
public class RefBModel {
    @Attribute(primaryKey = true)
    Key primaryKey;

    @Attribute(persistent = false)
    InverseModelRef<RefAModel, RefBModel> inverseA =
        new InverseModelRef<RefAModel, RefBModel>(
            RefAModel.class,
            "inverseB",
            this);

    ModelRef<RefAModel> refA = new ModelRef<RefAModel>(RefAModel.class);

    /**
     * @return the primaryKey
     */
    public Key getPrimaryKey() {
        return primaryKey;
    }

    /**
     * @param primaryKey
     *            the primaryKey to set
     */
    public void setPrimaryKey(Key primaryKey) {
        this.primaryKey = primaryKey;
    }

    /**
     * @return the inverseA
     */
    public InverseModelRef<RefAModel, RefBModel> getInverseA() {
        return inverseA;
    }

    /**
     * @return the refA
     */
    public ModelRef<RefAModel> getRefA() {
        return refA;
    }
}
