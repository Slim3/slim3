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

import java.util.Iterator;
import java.util.List;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Transaction;

/**
 * An ancestor query for all kind.
 * 
 * @author higa
 * @since 1.0.0
 * 
 */
public class KindlessQuery extends AbstractQuery<KindlessQuery> {

    /**
     * Constructor.
     * 
     */
    public KindlessQuery() {
        super();
    }

    /**
     * Constructor.
     * 
     * @param ancestorKey
     *            the ancestor key
     * @throws NullPointerException
     *             if the ancestorKey parameter is null
     */
    public KindlessQuery(Key ancestorKey) throws NullPointerException {
        super(ancestorKey);
    }

    /**
     * Constructor.
     * 
     * @param tx
     *            the transaction
     * @param ancestorKey
     *            the ancestor key
     * @throws NullPointerException
     *             if the ancestorKey parameter is null
     */
    public KindlessQuery(Transaction tx, Key ancestorKey)
            throws NullPointerException {
        super(ancestorKey);
        setTx(tx);
    }

    /**
     * Returns entities as a list.
     * 
     * @return entities as a list
     */
    public List<Entity> asList() {
        return super.asEntityList();
    }

    @Override
    public Entity asSingleEntity() {
        return super.asSingleEntity();
    }

    /**
     * Returns entities as {@link Iterable}.
     * 
     * @return entities as {@link Iterable}
     */
    public Iterable<Entity> asIterable() {
        return super.asIterableEntities();
    }

    /**
     * Returns entities as {@link Iterator}.
     * 
     * @return entities as {@link Iterator}
     */
    public Iterator<Entity> asIterator() {
        return asIterable().iterator();
    }
}