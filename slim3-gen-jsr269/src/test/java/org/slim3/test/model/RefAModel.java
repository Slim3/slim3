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
import org.slim3.datastore.InverseModelListRef;
import org.slim3.datastore.InverseModelRef;
import org.slim3.datastore.Model;
import org.slim3.datastore.ModelRef;

import com.google.appengine.api.datastore.Key;

/**
 * @author vvakame
 */
@Model
public class RefAModel {
    @Attribute(primaryKey = true)
    Key primaryKey;

    ModelRef<BasicModel> modelRef = new ModelRef<BasicModel>(BasicModel.class);

    @Attribute(persistent = false)
    InverseModelRef<RefBModel, RefAModel> inverseB =
        new InverseModelRef<RefBModel, RefAModel>(
            RefBModel.class,
            "inverseA",
            this);

    @Attribute(persistent = false)
    InverseModelListRef<RefBModel, RefAModel> inverseListB =
        new InverseModelListRef<RefBModel, RefAModel>(
            RefBModel.class,
            "refA",
            this);

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
     * @return the modelRef
     */
    public ModelRef<BasicModel> getModelRef() {
        return modelRef;
    }

    /**
     * @return the inverseB
     */
    public InverseModelRef<RefBModel, RefAModel> getInverseB() {
        return inverseB;
    }

    /**
     * @return the inverseListB
     */
    public InverseModelListRef<RefBModel, RefAModel> getInverseListB() {
        return inverseListB;
    }
}
