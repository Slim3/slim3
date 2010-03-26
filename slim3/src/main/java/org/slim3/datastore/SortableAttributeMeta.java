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

/**
 * An meta data of attribute.
 * 
 * @author higa
 * @param <M>
 *            the model type
 * @param <A>
 *            the attribute type
 * @since 1.0.1
 * 
 */
public class SortableAttributeMeta<M, A> extends AttributeMeta<M, A> {

    /**
     * The "ascending" sort criterion
     */
    public final AscCriterion asc;

    /**
     * The "descending" sort criterion
     */
    public final DescCriterion desc;

    /**
     * Constructor.
     * 
     * @param modelMeta
     *            the meta data of model
     * @param name
     *            the name
     * @param fieldName
     *            the field name
     * @param attributeClass
     *            the attribute class
     * 
     * @throws NullPointerException
     *             if the modelMeta parameter is null or if the name parameter
     *             is null or if the attributeClass parameter is null or if the
     *             fieldName parameter is null
     */
    public SortableAttributeMeta(ModelMeta<M> modelMeta, String name,
            String fieldName, Class<? super A> attributeClass) {
        super(modelMeta, name, fieldName, attributeClass);
        asc = new AscCriterion(this);
        desc = new DescCriterion(this);
    }
}