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
package org.slim3.datastore.model;

import java.io.Serializable;

import org.slim3.datastore.Model;
import org.slim3.datastore.ModelRef;

/**
 * @author higa
 * 
 */
@Model
public class Bbb extends Aaa implements Serializable {

    private static final long serialVersionUID = 1L;

    private ModelRef<Hoge> hogeRef = new ModelRef<Hoge>(Hoge.class);

    private ModelRef<Hoge> hoge2Ref = new ModelRef<Hoge>(Hoge.class);

    /**
     * @return the hogeRef
     */
    public ModelRef<Hoge> getHogeRef() {
        return hogeRef;
    }

    /**
     * @return the hogeRef
     */
    public ModelRef<Hoge> getHoge2Ref() {
        return hoge2Ref;
    }
}