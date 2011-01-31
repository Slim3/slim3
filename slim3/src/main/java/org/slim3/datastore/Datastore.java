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

import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import org.slim3.util.CipherFactory;
import org.slim3.util.ClassUtil;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyRange;
import com.google.appengine.api.datastore.Transaction;

/**
 * A class to access datastore.
 * 
 * @author higa
 * @since 1.0.0
 * 
 */
public final class Datastore {

    /**
     * The key of {@link DatastoreDelegate} class name.
     */
    public static final String DELEGATE_KEY = "slim3.datastoreDelegate";

    private static DatastoreDelegate defaultDelegate;

    private static Constructor<? extends DatastoreDelegate> delegateConstructor;

    static {
        initialize();
    }

    static void initialize() {
        Class<DatastoreDelegate> clazz =
            ClassUtil.forName(System.getProperty(
                DELEGATE_KEY,
                DatastoreDelegate.class.getName()));
        delegateClass(clazz);
    }

    /**
     * Sets the delegate class.
     * 
     * @param clazz
     *            the delegate class
     * @throws NullPointerException
     *             if the clazz parameter is null
     */
    public static void delegateClass(Class<? extends DatastoreDelegate> clazz)
            throws NullPointerException {
        if (clazz == null) {
            throw new NullPointerException(
                "The clazz parameter must not be null.");
        }
        try {
            delegateConstructor = clazz.getConstructor(Double.class);
        } catch (Throwable cause) {
            throw new IllegalStateException(cause);
        }
        defaultDelegate = ClassUtil.newInstance(clazz);
    }

    static DatastoreDelegate delegate() {
        return defaultDelegate;
    }

    static AsyncDatastoreDelegate asyncDelegate() {
        return defaultDelegate.asyncDelegate();
    }

    /**
     * Returns a {@link DatastoreDelegate}.
     * 
     * @param deadline
     *            the deadline
     * @return a {@link DatastoreDelegate}
     * @throws IllegalStateException
     *             if a {@link DatastoreDelegate} cannot be instantiated
     */
    static DatastoreDelegate delegate(Double deadline)
            throws IllegalStateException {
        try {
            return delegateConstructor.newInstance(deadline);
        } catch (Throwable cause) {
            throw new IllegalStateException(cause);
        }
    }

    /**
     * Returns a {@link DatastoreDelegate}.
     * 
     * @param deadline
     *            the deadline
     * @return a {@link DatastoreDelegate}
     * @throws IllegalStateException
     *             if a {@link DatastoreDelegate} cannot be instantiated
     */
    public static DatastoreDelegate deadline(Double deadline)
            throws IllegalStateException {
        return delegate(deadline);
    }

    /**
     * Begins a transaction.
     * 
     * @return a begun transaction
     */
    public static Transaction beginTransaction() {
        return delegate().beginTransaction();
    }

    /**
     * Use {@link Transaction#commit()} directly.
     * 
     * @param tx
     *            the transaction
     */
    @Deprecated
    public static void commit(Transaction tx) {
        tx.commit();
    }

    /**
     * Use {@link Transaction#rollback()} directly.
     * 
     * @param tx
     *            the transaction
     */
    @Deprecated
    public static void rollback(Transaction tx) {
        tx.rollback();
    }

    /**
     * Returns the active transactions.
     * 
     * @return the active transactions
     */
    public static Collection<Transaction> getActiveTransactions() {
        return delegate().getActiveTransactions();
    }

    /**
     * Returns the current transaction. Returns null if there is no transaction.
     * 
     * @return the current transaction
     */
    public static Transaction getCurrentTransaction() {
        return delegate().getCurrentTransaction();
    }

    /**
     * Begins a global transaction.
     * 
     * @return a begun global transaction
     */
    public static GlobalTransaction beginGlobalTransaction() {
        return delegate().beginGlobalTransaction();
    }

    /**
     * Returns the active global transactions.
     * 
     * @return the active global transactions
     */
    public static Collection<GlobalTransaction> getActiveGlobalTransactions() {
        return delegate().getActiveGlobalTransactions();
    }

    /**
     * Returns the current global transaction. Returns null if there is no
     * transaction.
     * 
     * @return the current global transaction
     */
    public static GlobalTransaction getCurrentGlobalTransaction() {
        return delegate().getCurrentGlobalTransaction();
    }

    /**
     * Allocates a key within a namespace defined by the kind.
     * 
     * @param kind
     *            the kind
     * @return a key within a namespace defined by the kind
     * @throws NullPointerException
     *             if the kind parameter is null
     */
    public static Key allocateId(String kind) throws NullPointerException {
        return delegate().allocateId(kind);
    }

    /**
     * Allocates a key within a namespace defined by the kind of the model.
     * 
     * @param modelClass
     *            the model class
     * @return a key within a namespace defined by the kind of the model
     * @throws NullPointerException
     *             if the modelClass parameter is null
     */
    public static Key allocateId(Class<?> modelClass)
            throws NullPointerException {
        return delegate().allocateId(modelClass);
    }

    /**
     * Allocates a key within a namespace defined by the kind of the model.
     * 
     * @param modelMeta
     *            the meta data of the model
     * @return a key within a namespace defined by the kind of the model
     * @throws NullPointerException
     *             if the modelMeta parameter is null
     */
    public static Key allocateId(ModelMeta<?> modelMeta)
            throws NullPointerException {
        return delegate().allocateId(modelMeta);
    }

    /**
     * Allocates a key within a namespace defined by the parent key and the
     * kind.
     * 
     * @param parentKey
     *            the parent key
     * @param kind
     *            the kind
     * @return a key within a namespace defined by the parent key and the kind
     * @throws NullPointerException
     *             if the parentKey parameter is null or if the kind parameter
     *             is null
     */
    public static Key allocateId(Key parentKey, String kind)
            throws NullPointerException {
        return delegate().allocateId(parentKey, kind);
    }

    /**
     * Allocates a key within a namespace defined by the parent key and the kind
     * of the model.
     * 
     * @param parentKey
     *            the parent key
     * @param modelClass
     *            the model class
     * @return a key within a namespace defined by the parent key and the kind
     *         of the model
     * @throws NullPointerException
     *             if the parentKey parameter is null or if the modelClass
     *             parameter is null
     */
    public static Key allocateId(Key parentKey, Class<?> modelClass)
            throws NullPointerException {
        return delegate().allocateId(parentKey, modelClass);
    }

    /**
     * Allocates a key within a namespace defined by the parent key and the kind
     * of the model.
     * 
     * @param parentKey
     *            the parent key
     * @param modelMeta
     *            the meta data of the model
     * @return a key within a namespace defined by the parent key and the kind
     *         of the model
     * @throws NullPointerException
     *             if the parentKey parameter is null or if the modelMeta
     *             parameter is null
     */
    public static Key allocateId(Key parentKey, ModelMeta<?> modelMeta)
            throws NullPointerException {
        return delegate().allocateId(parentKey, modelMeta);
    }

    /**
     * Allocates keys within a namespace defined by the kind.
     * 
     * @param kind
     *            the kind
     * @param num
     *            the number of allocated keys
     * @return keys within a namespace defined by the kind
     * @throws NullPointerException
     *             if the kind parameter is null
     */
    public static KeyRange allocateIds(String kind, long num)
            throws NullPointerException {
        return delegate().allocateIds(kind, num);
    }

    /**
     * Allocates keys within a namespace defined by the kind asynchronously.
     * 
     * @param kind
     *            the kind
     * @param num
     *            the number of allocated keys
     * @return keys represented as {@link Future}
     * @throws NullPointerException
     *             if the kind parameter is null
     */
    public static Future<KeyRange> allocateIdsAsync(String kind, long num)
            throws NullPointerException {
        return asyncDelegate().allocateIdsAsync(kind, num);
    }

    /**
     * Allocates keys within a namespace defined by the kind of the model.
     * 
     * @param modelClass
     *            the model class
     * @param num
     *            the number of allocated keys
     * @return keys within a namespace defined by the kind of the model
     * @throws NullPointerException
     *             if the modelClass parameter is null
     */
    public static KeyRange allocateIds(Class<?> modelClass, long num)
            throws NullPointerException {
        return delegate().allocateIds(modelClass, num);
    }

    /**
     * Allocates keys within a namespace defined by the kind of the model
     * asynchronously.
     * 
     * @param modelClass
     *            the model class
     * @param num
     *            the number of allocated keys
     * @return keys represented as {@link Future}
     * @throws NullPointerException
     *             if the modelClass parameter is null
     */
    public static Future<KeyRange> allocateIdsAsync(Class<?> modelClass,
            long num) throws NullPointerException {
        return asyncDelegate().allocateIdsAsync(modelClass, num);
    }

    /**
     * Allocates keys within a namespace defined by the kind of the model.
     * 
     * @param modelMeta
     *            the meta data of the model
     * @param num
     *            the number of allocated keys
     * @return keys within a namespace defined by the kind of the model
     */
    public static KeyRange allocateIds(ModelMeta<?> modelMeta, long num) {
        return delegate().allocateIds(modelMeta, num);
    }

    /**
     * Allocates keys within a namespace defined by the kind of the model
     * asynchronously.
     * 
     * @param modelMeta
     *            the meta data of the model
     * @param num
     *            the number of allocated keys
     * @return keys represented as {@link Future}
     */
    public static Future<KeyRange> allocateIdsAsync(ModelMeta<?> modelMeta,
            long num) {
        return asyncDelegate().allocateIdsAsync(modelMeta, num);
    }

    /**
     * Allocates keys within a namespace defined by the parent key and the kind.
     * 
     * @param parentKey
     *            the parent key
     * @param kind
     *            the kind
     * @param num
     * @return keys within a namespace defined by the parent key and the kind
     * @throws NullPointerException
     *             if the parentKey parameter is null or if the kind parameter
     *             is null
     */
    public static KeyRange allocateIds(Key parentKey, String kind, int num)
            throws NullPointerException {
        return delegate().allocateIds(parentKey, kind, num);
    }

    /**
     * Allocates keys within a namespace defined by the parent key and the kind
     * asynchronously.
     * 
     * @param parentKey
     *            the parent key
     * @param kind
     *            the kind
     * @param num
     * @return keys represented as {@link Future}
     * @throws NullPointerException
     *             if the parentKey parameter is null or if the kind parameter
     *             is null
     */
    public static Future<KeyRange> allocateIdsAsync(Key parentKey, String kind,
            int num) throws NullPointerException {
        return asyncDelegate().allocateIdsAsync(parentKey, kind, num);
    }

    /**
     * Allocates keys within a namespace defined by the parent key and the kind
     * of the model.
     * 
     * @param parentKey
     *            the parent key
     * @param modelClass
     *            the model class
     * @param num
     * @return keys within a namespace defined by the parent key and the kind of
     *         the model
     * @throws NullPointerException
     *             if the parentKey parameter is null or if the modelClass
     *             parameter is null
     */
    public static KeyRange allocateIds(Key parentKey, Class<?> modelClass,
            int num) throws NullPointerException {
        return delegate().allocateIds(parentKey, modelClass, num);
    }

    /**
     * Allocates keys within a namespace defined by the parent key and the kind
     * of the model asynchronously.
     * 
     * @param parentKey
     *            the parent key
     * @param modelClass
     *            the model class
     * @param num
     * @return keys represented as {@link Future}
     * @throws NullPointerException
     *             if the parentKey parameter is null or if the modelClass
     *             parameter is null
     */
    public static Future<KeyRange> allocateIdsAsync(Key parentKey,
            Class<?> modelClass, int num) throws NullPointerException {
        return asyncDelegate().allocateIdsAsync(parentKey, modelClass, num);
    }

    /**
     * Allocates keys within a namespace defined by the parent key and the kind
     * of the model.
     * 
     * @param parentKey
     *            the parent key
     * @param modelMeta
     *            the meta data of the model
     * @param num
     * @return keys within a namespace defined by the parent key and the kind of
     *         the model
     * @throws NullPointerException
     *             if the parentKey parameter is null or if the modelMeta
     *             parameter is null
     */
    public static KeyRange allocateIds(Key parentKey, ModelMeta<?> modelMeta,
            int num) throws NullPointerException {
        return delegate().allocateIds(parentKey, modelMeta, num);
    }

    /**
     * Allocates keys within a namespace defined by the parent key and the kind
     * of the model asynchronously.
     * 
     * @param parentKey
     *            the parent key
     * @param modelMeta
     *            the meta data of the model
     * @param num
     * @return keys represented as {@link Future}
     * @throws NullPointerException
     *             if the parentKey parameter is null or if the modelMeta
     *             parameter is null
     */
    public static Future<KeyRange> allocateIdsAsync(Key parentKey,
            ModelMeta<?> modelMeta, int num) throws NullPointerException {
        return asyncDelegate().allocateIdsAsync(parentKey, modelMeta, num);
    }

    /**
     * Creates a key.
     * 
     * @param kind
     *            the kind of entity
     * @param id
     *            the identifier
     * @return a key
     * @throws NullPointerException
     *             if the kind parameter is null
     */
    public static Key createKey(String kind, long id)
            throws NullPointerException {
        return delegate().createKey(kind, id);
    }

    /**
     * Creates a key.
     * 
     * @param modelClass
     *            the model class
     * @param id
     *            the identifier
     * @return a key
     * @throws NullPointerException
     *             if the modelClass parameter is null
     */
    public static Key createKey(Class<?> modelClass, long id)
            throws NullPointerException {
        return delegate().createKey(modelClass, id);
    }

    /**
     * Creates a key.
     * 
     * @param modelMeta
     *            the meta data of the model
     * @param id
     *            the identifier
     * @return a key
     * @throws NullPointerException
     *             if the modelMeta parameter is null
     */
    public static Key createKey(ModelMeta<?> modelMeta, long id)
            throws NullPointerException {
        return delegate().createKey(modelMeta, id);
    }

    /**
     * Creates a key.
     * 
     * @param kind
     *            the kind of entity
     * @param name
     *            the name
     * @return a key
     * @throws NullPointerException
     *             if the kind parameter is null or if the name parameter is
     *             null
     */
    public static Key createKey(String kind, String name)
            throws NullPointerException {
        return delegate().createKey(kind, name);
    }

    /**
     * Creates a key.
     * 
     * @param modelClass
     *            the model class
     * @param name
     *            the name
     * @return a key
     * @throws NullPointerException
     *             if the modeClass parameter is null or if the name parameter
     *             is null
     */
    public static Key createKey(Class<?> modelClass, String name)
            throws NullPointerException {
        return delegate().createKey(modelClass, name);
    }

    /**
     * Creates a key.
     * 
     * @param modelMeta
     *            the meta data of the model
     * @param name
     *            the name
     * @return a key
     * @throws NullPointerException
     *             if the modelMeta parameter is null or if the name parameter
     *             is null
     */
    public static Key createKey(ModelMeta<?> modelMeta, String name)
            throws NullPointerException {
        return delegate().createKey(modelMeta, name);
    }

    /**
     * Creates a key.
     * 
     * @param parentKey
     *            the parent key
     * @param kind
     *            the kind of entity
     * @param id
     *            the identifier
     * @return a key
     * @throws NullPointerException
     *             if the parentKey parameter is null or if the kind parameter
     *             is null
     */
    public static Key createKey(Key parentKey, String kind, long id)
            throws NullPointerException {
        return delegate().createKey(parentKey, kind, id);
    }

    /**
     * Creates a key.
     * 
     * @param parentKey
     *            the parent key
     * @param modelClass
     *            the model class
     * @param id
     *            the identifier
     * @return a key
     * @throws NullPointerException
     *             if the parentKey parameter is null or if the modelClass
     *             parameter is null
     */
    public static Key createKey(Key parentKey, Class<?> modelClass, long id)
            throws NullPointerException {
        return delegate().createKey(parentKey, modelClass, id);
    }

    /**
     * Creates a key.
     * 
     * @param parentKey
     *            the parent key
     * @param modelMeta
     *            the meta data of the model
     * @param id
     *            the identifier
     * @return a key
     * @throws NullPointerException
     *             if the parentKey parameter is null or if the modelMeta
     *             parameter is null
     */
    public static Key createKey(Key parentKey, ModelMeta<?> modelMeta, long id)
            throws NullPointerException {
        return delegate().createKey(parentKey, modelMeta, id);
    }

    /**
     * Creates a key.
     * 
     * @param parentKey
     *            the parent key
     * @param kind
     *            the kind of entity
     * @param name
     *            the name
     * @return a key
     * @throws NullPointerException
     *             if the parentKey parameter is null or if the kind parameter
     *             is null or if the name parameter is null
     */
    public static Key createKey(Key parentKey, String kind, String name)
            throws NullPointerException {
        return delegate().createKey(parentKey, kind, name);
    }

    /**
     * Creates a key.
     * 
     * @param parentKey
     *            the parent key
     * @param modelClass
     *            the model class
     * @param name
     *            the name
     * @return a key
     * @throws NullPointerException
     *             if the parentKey is null or if the modelClass parameter is
     *             null or if the name parameter is null
     */
    public static Key createKey(Key parentKey, Class<?> modelClass, String name)
            throws NullPointerException {
        return delegate().createKey(parentKey, modelClass, name);
    }

    /**
     * Creates a key.
     * 
     * @param parentKey
     *            the parent key
     * @param modelMeta
     *            the meta data of the model
     * @param name
     *            the name
     * @return a key
     * @throws NullPointerException
     *             if the parentKey is null or if the modelMeta parameter is
     *             null or if the name parameter is null
     */
    public static Key createKey(Key parentKey, ModelMeta<?> modelMeta,
            String name) throws NullPointerException {
        return delegate().createKey(parentKey, modelMeta, name);
    }

    /**
     * Converts the key to an encoded string.
     * 
     * @param key
     *            the key
     * @return an encoded string
     * @throws NullPointerException
     *             if the key parameter is null
     */
    public static String keyToString(Key key) throws NullPointerException {
        return delegate().keyToString(key);
    }

    /**
     * Converts the encoded string to a key.
     * 
     * @param encodedKey
     *            the encoded string
     * @return a key
     * @throws NullPointerException
     *             if the encodedKey parameter is null
     */
    public static Key stringToKey(String encodedKey)
            throws NullPointerException {
        return delegate().stringToKey(encodedKey);
    }

    /**
     * Puts the unique value.
     * 
     * @param uniqueIndexName
     *            the unique index name
     * @param value
     *            the unique value
     * @return whether the unique value is put
     * @throws NullPointerException
     *             if the uniqueIndexName parameter is null or if the value
     *             parameter is null
     */
    public static boolean putUniqueValue(String uniqueIndexName, String value)
            throws NullPointerException {
        return delegate().putUniqueValue(uniqueIndexName, value);
    }

    /**
     * Deletes the unique value.
     * 
     * @param uniqueIndexName
     *            the unique index name
     * @param value
     *            the unique value
     * @throws NullPointerException
     *             if the uniqueIndexName parameter is null or if the value
     *             parameter is null
     */
    public static void deleteUniqueValue(String uniqueIndexName, String value)
            throws NullPointerException {
        delegate().deleteUniqueValue(uniqueIndexName, value);
    }

    /**
     * Returns an entity specified by the key. If there is a current
     * transaction, this operation will execute within that transaction.
     * 
     * @param key
     *            the key
     * @return an entity specified by the key
     * @throws NullPointerException
     *             if the key parameter is null
     * @throws EntityNotFoundRuntimeException
     *             if no entity specified by the key could be found
     */
    public static Entity get(Key key) throws NullPointerException,
            EntityNotFoundRuntimeException {
        return delegate().get(key);
    }

    /**
     * Returns an entity specified by the key asynchronously. If there is a
     * current transaction, this operation will execute within that transaction.
     * 
     * @param key
     *            the key
     * @return an entity represented as {@link Future}
     * @throws NullPointerException
     *             if the key parameter is null
     */
    public static Future<Entity> getAsync(Key key) throws NullPointerException {
        return asyncDelegate().getAsync(key);
    }

    /**
     * Returns a model specified by the key. If there is a current transaction,
     * this operation will execute within that transaction.
     * 
     * @param <M>
     *            the model type
     * @param modelClass
     *            the model class
     * @param key
     *            the key
     * @return a model specified by the key
     * @throws NullPointerException
     *             if the modelClass parameter is null
     * @throws EntityNotFoundRuntimeException
     *             if no entity specified by the key could be found
     * @throws IllegalArgumentException
     *             if the kind of the key is different from the kind of the
     *             model or if the model class is not assignable from entity
     *             class
     */
    public static <M> M get(Class<M> modelClass, Key key)
            throws NullPointerException, EntityNotFoundRuntimeException,
            IllegalArgumentException {
        return delegate().get(modelClass, key);
    }

    /**
     * Returns a model specified by the key asynchronously. If there is a
     * current transaction, this operation will execute within that transaction.
     * 
     * @param <M>
     *            the model type
     * @param modelClass
     *            the model class
     * @param key
     *            the key
     * @return a model represented as {@link Future}
     * @throws NullPointerException
     *             if the modelClass parameter is null
     */
    public static <M> Future<M> getAsync(Class<M> modelClass, Key key)
            throws NullPointerException {
        return asyncDelegate().getAsync(modelClass, key);
    }

    /**
     * Returns a model specified by the key. If there is a current transaction,
     * this operation will execute within that transaction.
     * 
     * @param <M>
     *            the model type
     * @param modelMeta
     *            the meta data of model
     * @param key
     *            the key
     * @return a model specified by the key
     * @throws NullPointerException
     *             if the modelMeta parameter is null
     * @throws EntityNotFoundRuntimeException
     *             if no entity specified by the key could be found
     */
    public static <M> M get(ModelMeta<M> modelMeta, Key key)
            throws NullPointerException, EntityNotFoundRuntimeException {
        return delegate().get(modelMeta, key);
    }

    /**
     * Returns a model specified by the key asynchronously. If there is a
     * current transaction, this operation will execute within that transaction.
     * 
     * @param <M>
     *            the model type
     * @param modelMeta
     *            the meta data of model
     * @param key
     *            the key
     * @return a model represented as {@link Future}
     * @throws NullPointerException
     *             if the modelMeta parameter is null
     */
    public static <M> Future<M> getAsync(ModelMeta<M> modelMeta, Key key)
            throws NullPointerException {
        return asyncDelegate().getAsync(modelMeta, key);
    }

    /**
     * Returns an entity specified by the key. Returns null if no entity is
     * found. If there is a current transaction, this operation will execute
     * within that transaction.
     * 
     * @param key
     *            the key
     * @return an entity specified by the key
     * @throws NullPointerException
     *             if the key parameter is null
     */
    public static Entity getOrNull(Key key) throws NullPointerException {
        return delegate().getOrNull(key);
    }

    /**
     * Returns an entity specified by the key asynchronously. If there is a
     * current transaction, this operation will execute within that transaction.
     * 
     * @param key
     *            the key
     * @return an entity represented as {@link Future}
     * @throws NullPointerException
     *             if the key parameter is null
     */
    public static Future<Entity> getOrNullAsync(Key key)
            throws NullPointerException {
        return asyncDelegate().getOrNullAsync(key);
    }

    /**
     * Returns a model specified by the key. Returns null if no entity is found.
     * If there is a current transaction, this operation will execute within
     * that transaction.
     * 
     * @param <M>
     *            the model type
     * @param modelClass
     *            the model class
     * @param key
     *            the key
     * @return a model specified by the key
     * @throws NullPointerException
     *             if the modelClass parameter is null
     * @throws IllegalArgumentException
     *             if the kind of the key is different from the kind of the
     *             model or if the model class is not assignable from entity
     *             class
     */
    public static <M> M getOrNull(Class<M> modelClass, Key key)
            throws NullPointerException, IllegalArgumentException {
        return delegate().getOrNull(modelClass, key);
    }

    /**
     * Returns a model specified by the key asynchronously. If there is a
     * current transaction, this operation will execute within that transaction.
     * 
     * @param <M>
     *            the model type
     * @param modelClass
     *            the model class
     * @param key
     *            the key
     * @return a model represented as {@link Future}
     * @throws NullPointerException
     *             if the modelClass parameter is null
     */
    public static <M> Future<M> getOrNullAsync(Class<M> modelClass, Key key)
            throws NullPointerException {
        return asyncDelegate().getOrNullAsync(modelClass, key);
    }

    /**
     * Returns a model specified by the key. Returns null if no entity is found.
     * If there is a current transaction, this operation will execute within
     * that transaction.
     * 
     * @param <M>
     *            the model type
     * @param modelMeta
     *            the meta data of model
     * @param key
     *            the key
     * @return a model specified by the key
     * @throws NullPointerException
     *             if the modelMeta parameter is null
     * @throws IllegalArgumentException
     *             if the kind of the key is different from the kind of the
     *             model or if the model class is not assignable from entity
     *             class
     * 
     */
    public static <M> M getOrNull(ModelMeta<M> modelMeta, Key key)
            throws NullPointerException, IllegalArgumentException {
        return delegate().getOrNull(modelMeta, key);
    }

    /**
     * Returns a model specified by the key asynchronously. If there is a
     * current transaction, this operation will execute within that transaction.
     * 
     * @param <M>
     *            the model type
     * @param modelMeta
     *            the meta data of model
     * @param key
     *            the key
     * @return a model represented as {@link Future}
     * @throws NullPointerException
     *             if the modelMeta parameter is null
     * @throws IllegalArgumentException
     *             if the kind of the key is different from the kind of the
     *             model or if the model class is not assignable from entity
     *             class
     * 
     */
    public static <M> Future<M> getOrNullAsync(ModelMeta<M> modelMeta, Key key)
            throws NullPointerException, IllegalArgumentException {
        return asyncDelegate().getOrNullAsync(modelMeta, key);
    }

    /**
     * Returns an entity specified by the key without transaction.
     * 
     * @param key
     *            the key
     * @return an entity specified by the key
     * @throws NullPointerException
     *             if the key parameter is null
     * @throws EntityNotFoundRuntimeException
     *             if no entity specified by the key could be found
     */
    public static Entity getWithoutTx(Key key) throws NullPointerException,
            EntityNotFoundRuntimeException {
        return delegate().getWithoutTx(key);
    }

    /**
     * Returns an entity specified by the key without transaction
     * asynchronously.
     * 
     * @param key
     *            the key
     * @return an entity represented as {@link Future}
     * @throws NullPointerException
     *             if the key parameter is null
     */
    // TODO
    public static Future<Entity> getWithoutTxAsync(Key key)
            throws NullPointerException {
        return asyncDelegate().getWithoutTxAsync(key);
    }

    /**
     * Returns a model specified by the key without transaction.
     * 
     * @param <M>
     *            the model type
     * @param modelClass
     *            the model class
     * @param key
     *            the key
     * @return a model specified by the key
     * @throws NullPointerException
     *             if the modelClass parameter is null
     * @throws EntityNotFoundRuntimeException
     *             if no entity specified by the key could be found
     * @throws IllegalArgumentException
     *             if the kind of the key is different from the kind of the
     *             model or if the model class is not assignable from entity
     *             class
     */
    public static <M> M getWithoutTx(Class<M> modelClass, Key key)
            throws NullPointerException, EntityNotFoundRuntimeException,
            IllegalArgumentException {
        return delegate().getWithoutTx(modelClass, key);
    }

    /**
     * Returns a model specified by the key without transaction asynchronously.
     * 
     * @param <M>
     *            the model type
     * @param modelClass
     *            the model class
     * @param key
     *            the key
     * @return a model represented as {@link Future}
     * @throws NullPointerException
     *             if the modelClass parameter is null
     */
    public static <M> Future<M> getWithoutTxAsync(Class<M> modelClass, Key key)
            throws NullPointerException {
        return asyncDelegate().getWithoutTxAsync(modelClass, key);
    }

    /**
     * Returns a model specified by the key without transaction.
     * 
     * @param <M>
     *            the model type
     * @param modelMeta
     *            the meta data of model
     * @param key
     *            the key
     * @return a model specified by the key
     * @throws NullPointerException
     *             if the modelMeta parameter is null
     * @throws EntityNotFoundRuntimeException
     *             if no entity specified by the key could be found
     */
    public static <M> M getWithoutTx(ModelMeta<M> modelMeta, Key key)
            throws NullPointerException, EntityNotFoundRuntimeException {
        return delegate().getWithoutTx(modelMeta, key);
    }

    /**
     * Returns a model specified by the key without transaction asynchronously.
     * 
     * @param <M>
     *            the model type
     * @param modelMeta
     *            the meta data of model
     * @param key
     *            the key
     * @return a model represented as {@link Future}
     * @throws NullPointerException
     *             if the modelMeta parameter is null
     */
    public static <M> Future<M> getWithoutTxAsync(ModelMeta<M> modelMeta,
            Key key) throws NullPointerException {
        return asyncDelegate().getWithoutTxAsync(modelMeta, key);
    }

    /**
     * Returns an entity specified by the key without transaction. Returns null
     * if no entity is found.
     * 
     * @param key
     *            the key
     * @return an entity specified by the key
     * @throws NullPointerException
     *             if the key parameter is null
     */
    public static Entity getOrNullWithoutTx(Key key)
            throws NullPointerException {
        return delegate().getOrNullWithoutTx(key);
    }

    /**
     * Returns an entity specified by the key without transaction
     * asynchronously.
     * 
     * @param key
     *            the key
     * @return an entity represented as {@link Future}
     * @throws NullPointerException
     *             if the key parameter is null
     */
    public static Future<Entity> getOrNullWithoutTxAsync(Key key)
            throws NullPointerException {
        return asyncDelegate().getOrNullWithoutTxAsync(key);
    }

    /**
     * Returns a model specified by the key without transaction. Returns null if
     * no entity is found.
     * 
     * @param <M>
     *            the model type
     * @param modelClass
     *            the model class
     * @param key
     *            the key
     * @return a model specified by the key
     * @throws NullPointerException
     *             if the modelClass parameter is null
     * @throws IllegalArgumentException
     *             if the kind of the key is different from the kind of the
     *             model or if the model class is not assignable from entity
     *             class
     */
    public static <M> M getOrNullWithoutTx(Class<M> modelClass, Key key)
            throws NullPointerException, IllegalArgumentException {
        return delegate().getOrNullWithoutTx(modelClass, key);
    }

    /**
     * Returns a model specified by the key without transaction asynchronously.
     * 
     * @param <M>
     *            the model type
     * @param modelClass
     *            the model class
     * @param key
     *            the key
     * @return a model represented as {@link Future}
     * @throws NullPointerException
     *             if the modelClass parameter is null
     */
    public static <M> Future<M> getOrNullWithoutTxAsync(Class<M> modelClass,
            Key key) throws NullPointerException {
        return asyncDelegate().getOrNullWithoutTxAsync(modelClass, key);
    }

    /**
     * Returns a model specified by the key without transaction. Returns null if
     * no entity is found.
     * 
     * @param <M>
     *            the model type
     * @param modelMeta
     *            the meta data of model
     * @param key
     *            the key
     * @return a model specified by the key
     * @throws NullPointerException
     *             if the modelMeta parameter is null
     * @throws IllegalArgumentException
     *             if the kind of the key is different from the kind of the
     *             model or if the model class is not assignable from entity
     *             class
     */
    public static <M> M getOrNullWithoutTx(ModelMeta<M> modelMeta, Key key)
            throws NullPointerException, IllegalArgumentException {
        return delegate().getOrNullWithoutTx(modelMeta, key);
    }

    /**
     * Returns a model specified by the key without transaction asynchronously.
     * 
     * @param <M>
     *            the model type
     * @param modelMeta
     *            the meta data of model
     * @param key
     *            the key
     * @return a model represented as {@link Future}
     * @throws NullPointerException
     *             if the modelMeta parameter is null
     */
    public static <M> Future<M> getOrNullWithoutTxAsync(ModelMeta<M> modelMeta,
            Key key) throws NullPointerException {
        return asyncDelegate().getOrNullWithoutTxAsync(modelMeta, key);
    }

    /**
     * Returns a model specified by the key and checks the version. If there is
     * a current transaction, this operation will execute within that
     * transaction.
     * 
     * @param <M>
     *            the model type
     * @param modelClass
     *            the model class
     * @param key
     *            the key
     * @param version
     *            the version
     * @return a model specified by the key
     * @throws NullPointerException
     *             if the modelClass parameter is null or if the key parameter
     *             is null or if the version parameter is null
     * @throws EntityNotFoundRuntimeException
     *             if no entity specified by the key could be found
     * @throws IllegalArgumentException
     *             if the kind of the key is different from the kind of the
     *             model or if the model class is not assignable from entity
     *             class
     * @throws ConcurrentModificationException
     *             if the version of the model is updated
     */
    public static <M> M get(Class<M> modelClass, Key key, Long version)
            throws NullPointerException, EntityNotFoundRuntimeException,
            IllegalArgumentException, ConcurrentModificationException {
        return delegate().get(modelClass, key, version);
    }

    /**
     * Returns a model specified by the key and checks the version
     * asynchronously. If there is a current transaction, this operation will
     * execute within that transaction.
     * 
     * @param <M>
     *            the model type
     * @param modelClass
     *            the model class
     * @param key
     *            the key
     * @param version
     *            the version
     * @return a model represented as {@link Future}
     * @throws NullPointerException
     *             if the modelClass parameter is null or if the key parameter
     *             is null or if the version parameter is null
     */
    public static <M> Future<M> getAsync(Class<M> modelClass, Key key,
            Long version) throws NullPointerException {
        return asyncDelegate().getAsync(modelClass, key, version);
    }

    /**
     * Returns a model specified by the key and checks the version. If there is
     * a current transaction, this operation will execute within that
     * transaction.
     * 
     * @param <M>
     *            the model type
     * @param modelMeta
     *            the meta data of model
     * @param key
     *            the key
     * @param version
     *            the version
     * @return a model specified by the key
     * @throws NullPointerException
     *             if the modelMeta parameter is null or if the key parameter is
     *             null or if the version parameter is null
     * @throws EntityNotFoundRuntimeException
     *             if no entity specified by the key could be found
     * @throws IllegalArgumentException
     *             if the kind of the key is different from the kind of the
     *             model or if the model class is not assignable from entity
     *             class
     * @throws ConcurrentModificationException
     *             if the version of the model is updated
     */
    public static <M> M get(ModelMeta<M> modelMeta, Key key, Long version)
            throws NullPointerException, EntityNotFoundRuntimeException,
            IllegalArgumentException, ConcurrentModificationException {
        return delegate().get(modelMeta, key, version);
    }

    /**
     * Returns a model specified by the key and checks the version
     * asynchronously. If there is a current transaction, this operation will
     * execute within that transaction.
     * 
     * @param <M>
     *            the model type
     * @param modelMeta
     *            the meta data of model
     * @param key
     *            the key
     * @param version
     *            the version
     * @return a model represented as {@link Future}
     * @throws NullPointerException
     *             if the modelMeta parameter is null or if the key parameter is
     *             null or if the version parameter is null
     */
    public static <M> Future<M> getAsync(ModelMeta<M> modelMeta, Key key,
            Long version) throws NullPointerException {
        return asyncDelegate().getAsync(modelMeta, key, version);
    }

    /**
     * Returns an entity specified by the key within the provided transaction.
     * 
     * @param tx
     *            the transaction
     * @param key
     *            the key
     * @return an entity specified by the key
     * @throws NullPointerException
     *             if the key parameter is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     * @throws EntityNotFoundRuntimeException
     *             if no entity specified by the key is found
     */
    public static Entity get(Transaction tx, Key key)
            throws NullPointerException, IllegalStateException,
            EntityNotFoundRuntimeException {
        return delegate().get(tx, key);
    }

    /**
     * Returns an entity specified by the key within the provided transaction
     * asynchronously.
     * 
     * @param tx
     *            the transaction
     * @param key
     *            the key
     * @return an entity represented as {@link Future}
     * @throws NullPointerException
     *             if the key parameter is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     */
    public static Future<Entity> getAsync(Transaction tx, Key key)
            throws NullPointerException, IllegalStateException {
        return asyncDelegate().getAsync(tx, key);
    }

    /**
     * Returns a model specified by the key within the provided transaction.
     * 
     * @param <M>
     *            the model type
     * @param tx
     *            the transaction
     * @param modelClass
     *            the model class
     * @param key
     *            the key
     * @return a model specified by the key
     * @throws NullPointerException
     *             if the modelClass parameter is null or the key parameter is
     *             null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     * @throws EntityNotFoundRuntimeException
     *             if no entity specified by the key could be found
     * @throws IllegalArgumentException
     *             if the kind of the key is different from the kind of the
     *             model or if the model class is not assignable from entity
     *             class
     */
    public static <M> M get(Transaction tx, Class<M> modelClass, Key key)
            throws NullPointerException, IllegalStateException,
            EntityNotFoundRuntimeException, IllegalArgumentException {
        return delegate().get(tx, modelClass, key);
    }

    /**
     * Returns a model specified by the key within the provided transaction
     * asynchronously.
     * 
     * @param <M>
     *            the model type
     * @param tx
     *            the transaction
     * @param modelClass
     *            the model class
     * @param key
     *            the key
     * @return a model represented as {@link Future}
     * @throws NullPointerException
     *             if the modelClass parameter is null or the key parameter is
     *             null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     */
    public static <M> Future<M> getAsync(Transaction tx, Class<M> modelClass,
            Key key) throws NullPointerException, IllegalStateException {
        return asyncDelegate().getAsync(tx, modelClass, key);
    }

    /**
     * Returns a model specified by the key within the provided transaction.
     * 
     * @param <M>
     *            the model type
     * @param tx
     *            the transaction
     * @param modelMeta
     *            the meta data of model
     * @param key
     *            the key
     * @return a model specified by the key
     * @throws NullPointerException
     *             if the modelMeta parameter is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     * @throws EntityNotFoundRuntimeException
     *             if no entity specified by the key could be found
     * @throws IllegalArgumentException
     *             if the kind of the key is different from the kind of the
     *             model or if the model class is not assignable from entity
     *             class
     */
    public static <M> M get(Transaction tx, ModelMeta<M> modelMeta, Key key)
            throws NullPointerException, IllegalStateException,
            EntityNotFoundRuntimeException, IllegalArgumentException {
        return delegate().get(tx, modelMeta, key);
    }

    /**
     * Returns a model specified by the key within the provided transaction
     * asynchronously.
     * 
     * @param <M>
     *            the model type
     * @param tx
     *            the transaction
     * @param modelMeta
     *            the meta data of model
     * @param key
     *            the key
     * @return a model represented as {@link Future}
     * @throws NullPointerException
     *             if the modelMeta parameter is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     */
    public static <M> Future<M> getAsync(Transaction tx,
            ModelMeta<M> modelMeta, Key key) throws NullPointerException,
            IllegalStateException {
        return asyncDelegate().getAsync(tx, modelMeta, key);
    }

    /**
     * Returns an entity specified by the key within the provided transaction.
     * Returns null if no entity is found.
     * 
     * @param tx
     *            the transaction
     * @param key
     *            the key
     * @return an entity specified by the key
     * @throws NullPointerException
     *             if the key parameter is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     */
    public static Entity getOrNull(Transaction tx, Key key)
            throws NullPointerException, IllegalStateException {
        return delegate().getOrNull(tx, key);
    }

    /**
     * Returns an entity specified by the key within the provided transaction
     * asynchronously.
     * 
     * @param tx
     *            the transaction
     * @param key
     *            the key
     * @return an entity represented as {@link Future}
     * @throws NullPointerException
     *             if the key parameter is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     */
    public static Future<Entity> getOrNullAsync(Transaction tx, Key key)
            throws NullPointerException, IllegalStateException {
        return asyncDelegate().getOrNullAsync(tx, key);
    }

    /**
     * Returns a model specified by the key within the provided transaction.
     * Returns null if no entity is found.
     * 
     * @param <M>
     *            the model type
     * @param tx
     *            the transaction
     * @param modelClass
     *            the model class
     * @param key
     *            the key
     * @return a model specified by the key
     * @throws NullPointerException
     *             if the modelClass parameter is null or the key parameter is
     *             null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     * @throws IllegalArgumentException
     *             if the kind of the key is different from the kind of the
     *             model or if the model class is not assignable from entity
     *             class
     */
    public static <M> M getOrNull(Transaction tx, Class<M> modelClass, Key key)
            throws NullPointerException, IllegalStateException,
            IllegalArgumentException {
        return delegate().getOrNull(tx, modelClass, key);
    }

    /**
     * Returns a model specified by the key within the provided transaction
     * asynchronously.
     * 
     * @param <M>
     *            the model type
     * @param tx
     *            the transaction
     * @param modelClass
     *            the model class
     * @param key
     *            the key
     * @return a model represented as {@link Future}
     * @throws NullPointerException
     *             if the modelClass parameter is null or the key parameter is
     *             null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     */
    public static <M> Future<M> getOrNullAsync(Transaction tx,
            Class<M> modelClass, Key key) throws NullPointerException,
            IllegalStateException {
        return asyncDelegate().getOrNullAsync(tx, modelClass, key);
    }

    /**
     * Returns a model specified by the key within the provided transaction.
     * Returns null if no entity is found.
     * 
     * @param <M>
     *            the model type
     * @param tx
     *            the transaction
     * @param modelMeta
     *            the meta data of model
     * @param key
     *            the key
     * @return a model specified by the key
     * @throws NullPointerException
     *             if the modelMeta parameter is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     * @throws IllegalArgumentException
     *             if the kind of the key is different from the kind of the
     *             model or if the model class is not assignable from entity
     *             class
     */
    public static <M> M getOrNull(Transaction tx, ModelMeta<M> modelMeta,
            Key key) throws NullPointerException, IllegalStateException,
            IllegalArgumentException {
        return delegate().getOrNull(tx, modelMeta, key);
    }

    /**
     * Returns a model specified by the key within the provided transaction
     * asynchronously.
     * 
     * @param <M>
     *            the model type
     * @param tx
     *            the transaction
     * @param modelMeta
     *            the meta data of model
     * @param key
     *            the key
     * @return a model represented as {@link Future}
     * @throws NullPointerException
     *             if the modelMeta parameter is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     */
    public static <M> Future<M> getOrNullAsync(Transaction tx,
            ModelMeta<M> modelMeta, Key key) throws NullPointerException,
            IllegalStateException {
        return asyncDelegate().getOrNullAsync(tx, modelMeta, key);
    }

    /**
     * Returns a model specified by the key within the provided transaction.
     * 
     * @param <M>
     *            the model type
     * @param tx
     *            the transaction
     * @param modelClass
     *            the model class
     * @param key
     *            the key
     * @param version
     *            the version
     * @return a model specified by the key
     * @throws NullPointerException
     *             if the modelClass parameter is null or if the key parameter
     *             is null or if the version parameter is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     * @throws EntityNotFoundRuntimeException
     *             if no entity specified by the key could be found
     * @throws IllegalArgumentException
     *             if the kind of the key is different from the kind of the
     *             model or if the model class is not assignable from entity
     *             class
     * @throws ConcurrentModificationException
     *             if the version of the model is updated
     */
    public static <M> M get(Transaction tx, Class<M> modelClass, Key key,
            Long version) throws NullPointerException, IllegalStateException,
            EntityNotFoundRuntimeException, IllegalArgumentException,
            ConcurrentModificationException {
        return delegate().get(tx, modelClass, key, version);
    }

    /**
     * Returns a model specified by the key within the provided transaction
     * asynchronously.
     * 
     * @param <M>
     *            the model type
     * @param tx
     *            the transaction
     * @param modelClass
     *            the model class
     * @param key
     *            the key
     * @param version
     *            the version
     * @return a model represented as {@link Future}
     * @throws NullPointerException
     *             if the modelClass parameter is null or if the key parameter
     *             is null or if the version parameter is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     */
    public static <M> Future<M> getAsync(Transaction tx, Class<M> modelClass,
            Key key, Long version) throws NullPointerException,
            IllegalStateException {
        return asyncDelegate().getAsync(tx, modelClass, key, version);
    }

    /**
     * Returns a model specified by the key within the provided transaction.
     * 
     * @param <M>
     *            the model type
     * @param tx
     *            the transaction
     * @param modelMeta
     *            the meta data of model
     * @param key
     *            the key
     * @param version
     *            the version
     * @return a model specified by the key
     * @throws NullPointerException
     *             if the modelMeta parameter is null or if the key parameter is
     *             null or if the version parameter is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     * @throws EntityNotFoundRuntimeException
     *             if no entity specified by the key could be found
     * @throws IllegalArgumentException
     *             if the kind of the key is different from the kind of the
     *             model or if the model class is not assignable from entity
     *             class
     * @throws ConcurrentModificationException
     *             if the version of the model is updated
     */
    public static <M> M get(Transaction tx, ModelMeta<M> modelMeta, Key key,
            Long version) throws NullPointerException, IllegalStateException,
            EntityNotFoundRuntimeException, IllegalArgumentException,
            ConcurrentModificationException {
        return delegate().get(tx, modelMeta, key, version);
    }

    /**
     * Returns a model specified by the key within the provided transaction
     * asynchronously.
     * 
     * @param <M>
     *            the model type
     * @param tx
     *            the transaction
     * @param modelMeta
     *            the meta data of model
     * @param key
     *            the key
     * @param version
     *            the version
     * @return a model represented as {@link Future}
     * @throws NullPointerException
     *             if the modelMeta parameter is null or if the key parameter is
     *             null or if the version parameter is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     */
    public static <M> Future<M> getAsync(Transaction tx,
            ModelMeta<M> modelMeta, Key key, Long version)
            throws NullPointerException, IllegalStateException {
        return asyncDelegate().getAsync(tx, modelMeta, key, version);
    }

    /**
     * Returns entities specified by the keys. If there is a current
     * transaction, this operation will execute within that transaction.
     * 
     * @param keys
     *            the keys
     * @return entities specified by the key
     * @throws NullPointerException
     *             if the keys parameter is null
     * @throws EntityNotFoundRuntimeException
     *             if no entity specified by the key could be found
     */
    public static List<Entity> get(Iterable<Key> keys)
            throws NullPointerException, EntityNotFoundRuntimeException {
        return delegate().get(keys);
    }

    /**
     * Returns entities specified by the keys asynchronously. If there is a
     * current transaction, this operation will execute within that transaction.
     * 
     * @param keys
     *            the keys
     * @return entities represented as {@link Future}
     * @throws NullPointerException
     *             if the keys parameter is null
     */
    public static Future<List<Entity>> getAsync(Iterable<Key> keys)
            throws NullPointerException {
        return asyncDelegate().getAsync(keys);
    }

    /**
     * Returns entities specified by the keys. If there is a current
     * transaction, this operation will execute within that transaction.
     * 
     * @param keys
     *            the keys
     * @return entities specified by the key
     * @throws EntityNotFoundRuntimeException
     *             if no entity specified by the key could be found
     */
    public static List<Entity> get(Key... keys)
            throws EntityNotFoundRuntimeException {
        return delegate().get(keys);
    }

    /**
     * Returns entities specified by the keys asynchronously. If there is a
     * current transaction, this operation will execute within that transaction.
     * 
     * @param keys
     *            the keys
     * @return entities represented as {@link Future}
     */
    public static Future<List<Entity>> getAsync(Key... keys) {
        return asyncDelegate().getAsync(keys);
    }

    /**
     * Returns models specified by the keys. If there is a current transaction,
     * this operation will execute within that transaction.
     * 
     * @param <M>
     *            the model type
     * @param modelClass
     *            the model class
     * @param keys
     *            the keys
     * @return models specified by the keys
     * @throws NullPointerException
     *             if the modelClass parameter is null of if the keys parameter
     *             is null
     * @throws EntityNotFoundRuntimeException
     *             if no entity specified by the key could be found
     * @throws IllegalArgumentException
     *             if the kind of the key is different from the kind of the
     *             model or if the model class is not assignable from entity
     *             class
     */
    public static <M> List<M> get(Class<M> modelClass, Iterable<Key> keys)
            throws NullPointerException, EntityNotFoundRuntimeException,
            IllegalArgumentException {
        return delegate().get(modelClass, keys);
    }

    /**
     * Returns models specified by the keys asynchronously. If there is a
     * current transaction, this operation will execute within that transaction.
     * 
     * @param <M>
     *            the model type
     * @param modelClass
     *            the model class
     * @param keys
     *            the keys
     * @return models represented as {@link Future}
     * @throws NullPointerException
     *             if the modelClass parameter is null of if the keys parameter
     *             is null
     */
    public static <M> Future<List<M>> getAsync(Class<M> modelClass,
            Iterable<Key> keys) throws NullPointerException {
        return asyncDelegate().getAsync(modelClass, keys);
    }

    /**
     * Returns models specified by the keys. If there is a current transaction,
     * this operation will execute within that transaction.
     * 
     * @param <M>
     *            the model type
     * @param modelMeta
     *            the meta data of model
     * @param keys
     *            the keys
     * @return models specified by the keys
     * @throws NullPointerException
     *             if the modelMeta parameter is null of if the keys parameter
     *             is null
     * @throws EntityNotFoundRuntimeException
     *             if no entity specified by the key could be found
     * @throws IllegalArgumentException
     *             if the kind of the key is different from the kind of the
     *             model or if the model class is not assignable from entity
     *             class
     */
    public static <M> List<M> get(ModelMeta<M> modelMeta, Iterable<Key> keys)
            throws NullPointerException, EntityNotFoundRuntimeException,
            IllegalArgumentException {
        return delegate().get(modelMeta, keys);

    }

    /**
     * Returns models specified by the keys asynchronously. If there is a
     * current transaction, this operation will execute within that transaction.
     * 
     * @param <M>
     *            the model type
     * @param modelMeta
     *            the meta data of model
     * @param keys
     *            the keys
     * @return models represented as {@link Future}
     * @throws NullPointerException
     *             if the modelMeta parameter is null of if the keys parameter
     *             is null
     */
    public static <M> Future<List<M>> getAsync(ModelMeta<M> modelMeta,
            Iterable<Key> keys) throws NullPointerException {
        return asyncDelegate().getAsync(modelMeta, keys);

    }

    /**
     * Returns models specified by the keys. If there is a current transaction,
     * this operation will execute within that transaction.
     * 
     * @param <M>
     *            the model type
     * @param modelClass
     *            the model class
     * @param keys
     *            the keys
     * @return models specified by the keys
     * @throws NullPointerException
     *             if the modelClass parameter is null
     * @throws EntityNotFoundRuntimeException
     *             if no entity specified by the key could be found
     * @throws IllegalArgumentException
     *             if the kind of the key is different from the kind of the
     *             model or if the model class is not assignable from entity
     *             class
     */
    public static <M> List<M> get(Class<M> modelClass, Key... keys)
            throws NullPointerException, EntityNotFoundRuntimeException,
            IllegalArgumentException {
        return delegate().get(modelClass, keys);
    }

    /**
     * Returns models specified by the keys asynchronously. If there is a
     * current transaction, this operation will execute within that transaction.
     * 
     * @param <M>
     *            the model type
     * @param modelClass
     *            the model class
     * @param keys
     *            the keys
     * @return models represented as {@link Future}
     * @throws NullPointerException
     *             if the modelClass parameter is null
     */
    public static <M> Future<List<M>> getAsync(Class<M> modelClass, Key... keys)
            throws NullPointerException {
        return asyncDelegate().getAsync(modelClass, keys);
    }

    /**
     * Returns models specified by the keys. If there is a current transaction,
     * this operation will execute within that transaction.
     * 
     * @param <M>
     *            the model type
     * @param modelMeta
     *            the meta data of model
     * @param keys
     *            the keys
     * @return models specified by the keys
     * @throws NullPointerException
     *             if the modelMeta parameter is null or if the keys parameter
     *             is null
     * @throws EntityNotFoundRuntimeException
     *             if no entity specified by the key could be found
     * @throws IllegalArgumentException
     *             if the kind of the key is different from the kind of the
     *             model or if the model class is not assignable from entity
     *             class
     */
    public static <M> List<M> get(ModelMeta<M> modelMeta, Key... keys)
            throws NullPointerException, EntityNotFoundRuntimeException,
            IllegalArgumentException {
        return delegate().get(modelMeta, keys);
    }

    /**
     * Returns models specified by the keys asynchronously. If there is a
     * current transaction, this operation will execute within that transaction.
     * 
     * @param <M>
     *            the model type
     * @param modelMeta
     *            the meta data of model
     * @param keys
     *            the keys
     * @return models represented as {@link Future}
     * @throws NullPointerException
     *             if the modelMeta parameter is null or if the keys parameter
     *             is null
     */
    public static <M> Future<List<M>> getAsync(ModelMeta<M> modelMeta,
            Key... keys) throws NullPointerException {
        return asyncDelegate().getAsync(modelMeta, keys);
    }

    /**
     * Returns entities specified by the keys without transaction.
     * 
     * @param keys
     *            the keys
     * @return entities specified by the key
     * @throws NullPointerException
     *             if the keys parameter is null
     * @throws EntityNotFoundRuntimeException
     *             if no entity specified by the key could be found
     */
    public static List<Entity> getWithoutTx(Iterable<Key> keys)
            throws NullPointerException, EntityNotFoundRuntimeException {
        return delegate().getWithoutTx(keys);
    }

    /**
     * Returns entities specified by the keys without transaction
     * asynchronously.
     * 
     * @param keys
     *            the keys
     * @return entities represented as {@link Future}
     * @throws NullPointerException
     *             if the keys parameter is null
     */
    public static Future<List<Entity>> getWithoutTxAsync(Iterable<Key> keys)
            throws NullPointerException {
        return asyncDelegate().getWithoutTxAsync(keys);
    }

    /**
     * Returns entities specified by the keys. If there is a current
     * transaction, this operation will execute within that transaction.
     * 
     * @param keys
     *            the keys
     * @return entities specified by the key
     * @throws EntityNotFoundRuntimeException
     *             if no entity specified by the key could be found
     */
    public static List<Entity> getWithoutTx(Key... keys)
            throws EntityNotFoundRuntimeException {
        return delegate().getWithoutTx(keys);
    }

    /**
     * Returns entities specified by the keys asynchronously. If there is a
     * current transaction, this operation will execute within that transaction.
     * 
     * @param keys
     *            the keys
     * @return entities represented as {@link Future}
     */
    public static Future<List<Entity>> getWithoutTxAsync(Key... keys) {
        return asyncDelegate().getWithoutTxAsync(keys);
    }

    /**
     * Returns models specified by the keys without transaction.
     * 
     * @param <M>
     *            the model type
     * @param modelClass
     *            the model class
     * @param keys
     *            the keys
     * @return models specified by the keys
     * @throws NullPointerException
     *             if the modelClass parameter is null of if the keys parameter
     *             is null
     * @throws EntityNotFoundRuntimeException
     *             if no entity specified by the key could be found
     * @throws IllegalArgumentException
     *             if the kind of the key is different from the kind of the
     *             model or if the model class is not assignable from entity
     *             class
     */
    public static <M> List<M> getWithoutTx(Class<M> modelClass,
            Iterable<Key> keys) throws NullPointerException,
            EntityNotFoundRuntimeException, IllegalArgumentException {
        return delegate().getWithoutTx(modelClass, keys);
    }

    /**
     * Returns models specified by the keys without transaction asynchronously.
     * 
     * @param <M>
     *            the model type
     * @param modelClass
     *            the model class
     * @param keys
     *            the keys
     * @return models represented as {@link Future}
     * @throws NullPointerException
     *             if the modelClass parameter is null of if the keys parameter
     *             is null
     */
    public static <M> Future<List<M>> getWithoutTxAsync(Class<M> modelClass,
            Iterable<Key> keys) throws NullPointerException {
        return asyncDelegate().getWithoutTxAsync(modelClass, keys);
    }

    /**
     * Returns models specified by the keys without transaction.
     * 
     * @param <M>
     *            the model type
     * @param modelMeta
     *            the meta data of model
     * @param keys
     *            the keys
     * @return models specified by the keys
     * @throws NullPointerException
     *             if the modelMeta parameter is null of if the keys parameter
     *             is null
     * @throws EntityNotFoundRuntimeException
     *             if no entity specified by the key could be found
     * @throws IllegalArgumentException
     *             if the kind of the key is different from the kind of the
     *             model or if the model class is not assignable from entity
     *             class
     */
    public static <M> List<M> getWithoutTx(ModelMeta<M> modelMeta,
            Iterable<Key> keys) throws NullPointerException,
            EntityNotFoundRuntimeException, IllegalArgumentException {
        return delegate().getWithoutTx(modelMeta, keys);

    }

    /**
     * Returns models specified by the keys without transaction asynchronously.
     * 
     * @param <M>
     *            the model type
     * @param modelMeta
     *            the meta data of model
     * @param keys
     *            the keys
     * @return models represented as {@link Future}
     * @throws NullPointerException
     *             if the modelMeta parameter is null of if the keys parameter
     *             is null
     */
    public static <M> Future<List<M>> getWithoutTxAsync(ModelMeta<M> modelMeta,
            Iterable<Key> keys) throws NullPointerException {
        return asyncDelegate().getWithoutTxAsync(modelMeta, keys);

    }

    /**
     * Returns models specified by the keys without transaction.
     * 
     * @param <M>
     *            the model type
     * @param modelClass
     *            the model class
     * @param keys
     *            the keys
     * @return models specified by the keys
     * @throws NullPointerException
     *             if the modelClass parameter is null
     * @throws EntityNotFoundRuntimeException
     *             if no entity specified by the key could be found
     * @throws IllegalArgumentException
     *             if the kind of the key is different from the kind of the
     *             model or if the model class is not assignable from entity
     *             class
     */
    public static <M> List<M> getWithoutTx(Class<M> modelClass, Key... keys)
            throws NullPointerException, EntityNotFoundRuntimeException,
            IllegalArgumentException {
        return delegate().getWithoutTx(modelClass, keys);
    }

    /**
     * Returns models specified by the keys without transaction asynchronously.
     * 
     * @param <M>
     *            the model type
     * @param modelClass
     *            the model class
     * @param keys
     *            the keys
     * @return models represented as {@link Future}
     * @throws NullPointerException
     *             if the modelClass parameter is null
     */
    public static <M> Future<List<M>> getWithoutTxAsync(Class<M> modelClass,
            Key... keys) throws NullPointerException {
        return asyncDelegate().getWithoutTxAsync(modelClass, keys);
    }

    /**
     * Returns models specified by the keys without transaction.
     * 
     * @param <M>
     *            the model type
     * @param modelMeta
     *            the meta data of model
     * @param keys
     *            the keys
     * @return models specified by the keys
     * @throws NullPointerException
     *             if the modelMeta parameter is null or if the keys parameter
     *             is null
     * @throws EntityNotFoundRuntimeException
     *             if no entity specified by the key could be found
     * @throws IllegalArgumentException
     *             if the kind of the key is different from the kind of the
     *             model or if the model class is not assignable from entity
     *             class
     */
    public static <M> List<M> getWithoutTx(ModelMeta<M> modelMeta, Key... keys)
            throws NullPointerException, EntityNotFoundRuntimeException,
            IllegalArgumentException {
        return delegate().getWithoutTx(modelMeta, keys);
    }

    /**
     * Returns models specified by the keys without transaction asynchronously.
     * 
     * @param <M>
     *            the model type
     * @param modelMeta
     *            the meta data of model
     * @param keys
     *            the keys
     * @return models represented as {@link Future}
     * @throws NullPointerException
     *             if the modelMeta parameter is null or if the keys parameter
     *             is null
     */
    public static <M> Future<List<M>> getWithoutTxAsync(ModelMeta<M> modelMeta,
            Key... keys) throws NullPointerException {
        return asyncDelegate().getWithoutTxAsync(modelMeta, keys);
    }

    /**
     * Returns entities specified by the keys within the provided transaction.
     * 
     * @param tx
     *            the transaction
     * @param keys
     *            the keys
     * @return entities specified by the key
     * @throws NullPointerException
     *             if the keys parameter is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     * @throws EntityNotFoundRuntimeException
     *             if no entity specified by the key could be found
     */
    public static List<Entity> get(Transaction tx, Iterable<Key> keys)
            throws NullPointerException, IllegalStateException,
            EntityNotFoundRuntimeException {
        return delegate().get(tx, keys);
    }

    /**
     * Returns entities specified by the keys within the provided transaction
     * asynchronously.
     * 
     * @param tx
     *            the transaction
     * @param keys
     *            the keys
     * @return entities represented as {@link Future}
     * @throws NullPointerException
     *             if the keys parameter is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     */
    public static Future<List<Entity>> getAsync(Transaction tx,
            Iterable<Key> keys) throws NullPointerException,
            IllegalStateException {
        return asyncDelegate().getAsync(tx, keys);
    }

    /**
     * Returns entities specified by the keys within the provided transaction.
     * 
     * @param tx
     *            the transaction
     * @param keys
     *            the keys
     * @return entities specified by the key
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     * @throws EntityNotFoundRuntimeException
     *             if no entity specified by the key could be found
     */
    public static List<Entity> get(Transaction tx, Key... keys)
            throws IllegalStateException, EntityNotFoundRuntimeException {
        return delegate().get(tx, keys);
    }

    /**
     * Returns entities specified by the keys within the provided transaction
     * asynchronously.
     * 
     * @param tx
     *            the transaction
     * @param keys
     *            the keys
     * @return entities represented as {@link Future}
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     */
    public static Future<List<Entity>> getAsync(Transaction tx, Key... keys)
            throws IllegalStateException {
        return asyncDelegate().getAsync(tx, keys);
    }

    /**
     * Returns models specified by the keys within the provided transaction.
     * 
     * @param <M>
     *            the model type
     * @param tx
     *            the transaction
     * @param modelClass
     *            the model class
     * @param keys
     *            the keys
     * @return models specified by the keys
     * @throws NullPointerException
     *             if the modelClass parameter is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     * @throws EntityNotFoundRuntimeException
     *             if no entity specified by the key could be found
     * @throws IllegalArgumentException
     *             if the kind of the key is different from the kind of the
     *             model or if the model class is not assignable from entity
     *             class
     */
    public static <M> List<M> get(Transaction tx, Class<M> modelClass,
            Iterable<Key> keys) throws NullPointerException,
            IllegalStateException, EntityNotFoundRuntimeException,
            IllegalArgumentException {
        return delegate().get(tx, modelClass, keys);
    }

    /**
     * Returns models specified by the keys within the provided transaction
     * asynchronously.
     * 
     * @param <M>
     *            the model type
     * @param tx
     *            the transaction
     * @param modelClass
     *            the model class
     * @param keys
     *            the keys
     * @return models represented as {@link Future}
     * @throws NullPointerException
     *             if the modelClass parameter is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     */
    public static <M> Future<List<M>> getAsync(Transaction tx,
            Class<M> modelClass, Iterable<Key> keys)
            throws NullPointerException, IllegalStateException {
        return asyncDelegate().getAsync(tx, modelClass, keys);
    }

    /**
     * Returns models specified by the keys within the provided transaction.
     * 
     * @param <M>
     *            the model type
     * @param tx
     *            the transaction
     * @param modelMeta
     *            the meta data of model
     * @param keys
     *            the keys
     * @return models specified by the keys
     * @throws NullPointerException
     *             if the modelMeta parameter is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     * @throws EntityNotFoundRuntimeException
     *             if no entity specified by the key could be found
     * @throws IllegalArgumentException
     *             if the kind of the key is different from the kind of the
     *             model or if the model class is not assignable from entity
     *             class
     */
    public static <M> List<M> get(Transaction tx, ModelMeta<M> modelMeta,
            Iterable<Key> keys) throws NullPointerException,
            IllegalStateException, EntityNotFoundRuntimeException,
            IllegalArgumentException {
        return delegate().get(tx, modelMeta, keys);
    }

    /**
     * Returns models specified by the keys within the provided transaction
     * asynchronously.
     * 
     * @param <M>
     *            the model type
     * @param tx
     *            the transaction
     * @param modelMeta
     *            the meta data of model
     * @param keys
     *            the keys
     * @return models represented as {@link Future}
     * @throws NullPointerException
     *             if the modelMeta parameter is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     */
    public static <M> Future<List<M>> getAsync(Transaction tx,
            ModelMeta<M> modelMeta, Iterable<Key> keys)
            throws NullPointerException, IllegalStateException {
        return asyncDelegate().getAsync(tx, modelMeta, keys);
    }

    /**
     * Returns models specified by the keys within the provided transaction.
     * 
     * @param <M>
     *            the model type
     * @param tx
     *            the transaction
     * @param modelClass
     *            the model class
     * @param keys
     *            the keys
     * @return models specified by the keys
     * @throws NullPointerException
     *             if the modelClass parameter is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     * @throws EntityNotFoundRuntimeException
     *             if no entity specified by the key could be found
     * @throws IllegalArgumentException
     *             if the kind of the key is different from the kind of the
     *             model or if the model class is not assignable from entity
     *             class
     */
    public static <M> List<M> get(Transaction tx, Class<M> modelClass,
            Key... keys) throws NullPointerException, IllegalStateException,
            EntityNotFoundRuntimeException, IllegalArgumentException {
        return delegate().get(tx, modelClass, keys);
    }

    /**
     * Returns models specified by the keys within the provided transaction
     * asynchronously.
     * 
     * @param <M>
     *            the model type
     * @param tx
     *            the transaction
     * @param modelClass
     *            the model class
     * @param keys
     *            the keys
     * @return models represented as {@link Future}
     * @throws NullPointerException
     *             if the modelClass parameter is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     */
    public static <M> Future<List<M>> getAsync(Transaction tx,
            Class<M> modelClass, Key... keys) throws NullPointerException,
            IllegalStateException {
        return asyncDelegate().getAsync(tx, modelClass, keys);
    }

    /**
     * Returns models specified by the keys within the provided transaction.
     * 
     * @param <M>
     *            the model type
     * @param tx
     *            the transaction
     * @param modelMeta
     *            the meta data of model
     * @param keys
     *            the keys
     * @return models specified by the keys
     * @throws NullPointerException
     *             if the modelMeta parameter is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     * @throws EntityNotFoundRuntimeException
     *             if no entity specified by the key could be found
     * @throws IllegalArgumentException
     *             if the kind of the key is different from the kind of the
     *             model or if the model class is not assignable from entity
     *             class
     */
    public static <M> List<M> get(Transaction tx, ModelMeta<M> modelMeta,
            Key... keys) throws NullPointerException, IllegalStateException,
            EntityNotFoundRuntimeException, IllegalArgumentException {
        return delegate().get(tx, modelMeta, keys);
    }

    /**
     * Returns models specified by the keys within the provided transaction
     * asynchronously.
     * 
     * @param <M>
     *            the model type
     * @param tx
     *            the transaction
     * @param modelMeta
     *            the meta data of model
     * @param keys
     *            the keys
     * @return models represented as {@link Future}
     * @throws NullPointerException
     *             if the modelMeta parameter is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     */
    public static <M> Future<List<M>> getAsync(Transaction tx,
            ModelMeta<M> modelMeta, Key... keys) throws NullPointerException,
            IllegalStateException {
        return asyncDelegate().getAsync(tx, modelMeta, keys);
    }

    /**
     * Returns entities specified by the keys. If there is a current
     * transaction, this operation will execute within that transaction.
     * 
     * @param keys
     *            the keys
     * @return entities specified by the keys
     * @throws NullPointerException
     *             if the keys parameter is null
     */
    public static Map<Key, Entity> getAsMap(Iterable<Key> keys)
            throws NullPointerException {
        return delegate().getAsMap(keys);
    }

    /**
     * Returns entities specified by the keys asynchronously. If there is a
     * current transaction, this operation will execute within that transaction.
     * 
     * @param keys
     *            the keys
     * @return entities represented as {@link Future}
     * @throws NullPointerException
     *             if the keys parameter is null
     */
    public static Future<Map<Key, Entity>> getAsMapAsync(Iterable<Key> keys)
            throws NullPointerException {
        return asyncDelegate().getAsMapAsync(keys);
    }

    /**
     * Returns entities specified by the keys. If there is a current
     * transaction, this operation will execute within that transaction.
     * 
     * @param keys
     *            the keys
     * @return entities specified by the keys
     */
    public static Map<Key, Entity> getAsMap(Key... keys) {
        return delegate().getAsMap(keys);
    }

    /**
     * Returns entities specified by the keys asynchronously. If there is a
     * current transaction, this operation will execute within that transaction.
     * 
     * @param keys
     *            the keys
     * @return entities represented as {@link Future}
     */
    public static Future<Map<Key, Entity>> getAsMapAsync(Key... keys) {
        return asyncDelegate().getAsMapAsync(keys);
    }

    /**
     * Returns models specified by the keys. If there is a current transaction,
     * this operation will execute within that transaction.
     * 
     * @param <M>
     *            the model type
     * @param modelClass
     *            the model class
     * @param keys
     *            the keys
     * @return models specified by the keys
     * @throws NullPointerException
     *             if the modelClass parameter is null or if the keys parameter
     *             is null
     * @throws IllegalArgumentException
     *             if the kind of the key is different from the kind of the
     *             model or if the model class is not assignable from entity
     *             class
     */
    public static <M> Map<Key, M> getAsMap(Class<M> modelClass,
            Iterable<Key> keys) throws NullPointerException,
            IllegalArgumentException {
        return delegate().getAsMap(modelClass, keys);
    }

    /**
     * Returns models specified by the keys asynchronously. If there is a
     * current transaction, this operation will execute within that transaction.
     * 
     * @param <M>
     *            the model type
     * @param modelClass
     *            the model class
     * @param keys
     *            the keys
     * @return models represented as {@link Future}
     * @throws NullPointerException
     *             if the modelClass parameter is null or if the keys parameter
     *             is null
     */
    public static <M> Future<Map<Key, M>> getAsMapAsync(Class<M> modelClass,
            Iterable<Key> keys) throws NullPointerException {
        return asyncDelegate().getAsMapAsync(modelClass, keys);
    }

    /**
     * Returns models specified by the keys. If there is a current transaction,
     * this operation will execute within that transaction.
     * 
     * @param <M>
     *            the model type
     * @param modelMeta
     *            the meta data of model
     * @param keys
     *            the keys
     * @return models specified by the keys
     * @throws NullPointerException
     *             if the modelMeta parameter is null or if the keys parameter
     *             is null
     * @throws IllegalArgumentException
     *             if the kind of the key is different from the kind of the
     *             model or if the model class is not assignable from entity
     *             class
     */
    public static <M> Map<Key, M> getAsMap(ModelMeta<M> modelMeta,
            Iterable<Key> keys) throws NullPointerException,
            IllegalArgumentException {
        return delegate().getAsMap(modelMeta, keys);
    }

    /**
     * Returns models specified by the keys asynchronously. If there is a
     * current transaction, this operation will execute within that transaction.
     * 
     * @param <M>
     *            the model type
     * @param modelMeta
     *            the meta data of model
     * @param keys
     *            the keys
     * @return models represented as {@link Future}
     * @throws NullPointerException
     *             if the modelMeta parameter is null or if the keys parameter
     *             is null
     */
    public static <M> Future<Map<Key, M>> getAsMapAsync(ModelMeta<M> modelMeta,
            Iterable<Key> keys) throws NullPointerException {
        return asyncDelegate().getAsMapAsync(modelMeta, keys);
    }

    /**
     * Returns models specified by the keys. If there is a current transaction,
     * this operation will execute within that transaction.
     * 
     * @param <M>
     *            the model type
     * @param modelClass
     *            the model class
     * @param keys
     *            the keys
     * @return models specified by the keys
     * @throws NullPointerException
     *             if the modelClass parameter is null
     * @throws IllegalArgumentException
     *             if the kind of the key is different from the kind of the
     *             model or if the model class is not assignable from entity
     *             class
     */
    public static <M> Map<Key, M> getAsMap(Class<M> modelClass, Key... keys)
            throws NullPointerException, IllegalArgumentException {
        return delegate().getAsMap(modelClass, keys);
    }

    /**
     * Returns models specified by the keys asynchronously. If there is a
     * current transaction, this operation will execute within that transaction.
     * 
     * @param <M>
     *            the model type
     * @param modelClass
     *            the model class
     * @param keys
     *            the keys
     * @return models represented as {@link Future}
     * @throws NullPointerException
     *             if the modelClass parameter is null
     */
    public static <M> Future<Map<Key, M>> getAsMapAsync(Class<M> modelClass,
            Key... keys) throws NullPointerException {
        return asyncDelegate().getAsMapAsync(modelClass, keys);
    }

    /**
     * Returns models specified by the keys. If there is a current transaction,
     * this operation will execute within that transaction.
     * 
     * @param <M>
     *            the model type
     * @param modelMeta
     *            the meta data of model
     * @param keys
     *            the keys
     * @return models specified by the keys
     * @throws NullPointerException
     *             if the modelMeta parameter is null
     * @throws IllegalArgumentException
     *             if the kind of the key is different from the kind of the
     *             model or if the model class is not assignable from entity
     *             class
     */
    public static <M> Map<Key, M> getAsMap(ModelMeta<M> modelMeta, Key... keys)
            throws NullPointerException, IllegalArgumentException {
        return delegate().getAsMap(modelMeta, keys);
    }

    /**
     * Returns models specified by the keys asynchronously. If there is a
     * current transaction, this operation will execute within that transaction.
     * 
     * @param <M>
     *            the model type
     * @param modelMeta
     *            the meta data of model
     * @param keys
     *            the keys
     * @return models represented as {@link Future}
     * @throws NullPointerException
     *             if the modelMeta parameter is null
     */
    public static <M> Future<Map<Key, M>> getAsMapAsync(ModelMeta<M> modelMeta,
            Key... keys) throws NullPointerException {
        return asyncDelegate().getAsMapAsync(modelMeta, keys);
    }

    /**
     * Returns entities specified by the keys without transaction.
     * 
     * @param keys
     *            the keys
     * @return entities specified by the keys
     * @throws NullPointerException
     *             if the keys parameter is null
     */
    public static Map<Key, Entity> getAsMapWithoutTx(Iterable<Key> keys)
            throws NullPointerException {
        return delegate().getAsMapWithoutTx(keys);
    }

    /**
     * Returns entities specified by the keys without transaction
     * asynchronously.
     * 
     * @param keys
     *            the keys
     * @return entities represented as {@link Future}
     * @throws NullPointerException
     *             if the keys parameter is null
     */
    public static Future<Map<Key, Entity>> getAsMapWithoutTxAsync(
            Iterable<Key> keys) throws NullPointerException {
        return asyncDelegate().getAsMapWithoutTxAsync(keys);
    }

    /**
     * Returns entities specified by the keys without transaction.
     * 
     * @param keys
     *            the keys
     * @return entities specified by the keys
     */
    public static Map<Key, Entity> getAsMapWithoutTx(Key... keys) {
        return delegate().getAsMapWithoutTx(keys);
    }

    /**
     * Returns entities specified by the keys without transaction
     * asynchronously.
     * 
     * @param keys
     *            the keys
     * @return entities represented as {@link Future}
     */
    public static Future<Map<Key, Entity>> getAsMapWithoutTxAsync(Key... keys) {
        return asyncDelegate().getAsMapWithoutTxAsync(keys);
    }

    /**
     * Returns models specified by the keys without transaction.
     * 
     * @param <M>
     *            the model type
     * @param modelClass
     *            the model class
     * @param keys
     *            the keys
     * @return models specified by the keys
     * @throws NullPointerException
     *             if the modelClass parameter is null or if the keys parameter
     *             is null
     * @throws IllegalArgumentException
     *             if the kind of the key is different from the kind of the
     *             model or if the model class is not assignable from entity
     *             class
     */
    public static <M> Map<Key, M> getAsMapWithoutTx(Class<M> modelClass,
            Iterable<Key> keys) throws NullPointerException,
            IllegalArgumentException {
        return delegate().getAsMapWithoutTx(modelClass, keys);
    }

    /**
     * Returns models specified by the keys without transaction asynchronously.
     * 
     * @param <M>
     *            the model type
     * @param modelClass
     *            the model class
     * @param keys
     *            the keys
     * @return models represented as {@link Future}
     * @throws NullPointerException
     *             if the modelClass parameter is null or if the keys parameter
     *             is null
     */
    public static <M> Future<Map<Key, M>> getAsMapWithoutTxAsync(
            Class<M> modelClass, Iterable<Key> keys)
            throws NullPointerException {
        return asyncDelegate().getAsMapWithoutTxAsync(modelClass, keys);
    }

    /**
     * Returns models specified by the keys without transaction.
     * 
     * @param <M>
     *            the model type
     * @param modelMeta
     *            the meta data of model
     * @param keys
     *            the keys
     * @return models specified by the keys
     * @throws NullPointerException
     *             if the modelMeta parameter is null or if the keys parameter
     *             is null
     * @throws IllegalArgumentException
     *             if the kind of the key is different from the kind of the
     *             model or if the model class is not assignable from entity
     *             class
     */
    public static <M> Map<Key, M> getAsMapWithoutTx(ModelMeta<M> modelMeta,
            Iterable<Key> keys) throws NullPointerException,
            IllegalArgumentException {
        return delegate().getAsMapWithoutTx(modelMeta, keys);
    }

    /**
     * Returns models specified by the keys without transaction asynchronously.
     * 
     * @param <M>
     *            the model type
     * @param modelMeta
     *            the meta data of model
     * @param keys
     *            the keys
     * @return models represented as {@link Future}
     * @throws NullPointerException
     *             if the modelMeta parameter is null or if the keys parameter
     *             is null
     */
    public static <M> Future<Map<Key, M>> getAsMapWithoutTxAsync(
            ModelMeta<M> modelMeta, Iterable<Key> keys)
            throws NullPointerException {
        return asyncDelegate().getAsMapWithoutTxAsync(modelMeta, keys);
    }

    /**
     * Returns models specified by the keys without transaction.
     * 
     * @param <M>
     *            the model type
     * @param modelClass
     *            the model class
     * @param keys
     *            the keys
     * @return models specified by the keys
     * @throws NullPointerException
     *             if the modelClass parameter is null
     * @throws IllegalArgumentException
     *             if the kind of the key is different from the kind of the
     *             model or if the model class is not assignable from entity
     *             class
     */
    public static <M> Map<Key, M> getAsMapWithoutTx(Class<M> modelClass,
            Key... keys) throws NullPointerException, IllegalArgumentException {
        return delegate().getAsMapWithoutTx(modelClass, keys);
    }

    /**
     * Returns models specified by the keys without transaction asynchronously.
     * 
     * @param <M>
     *            the model type
     * @param modelClass
     *            the model class
     * @param keys
     *            the keys
     * @return models represented as {@link Future}
     * @throws NullPointerException
     *             if the modelClass parameter is null
     */
    public static <M> Future<Map<Key, M>> getAsMapWithoutTxAsync(
            Class<M> modelClass, Key... keys) throws NullPointerException {
        return asyncDelegate().getAsMapWithoutTxAsync(modelClass, keys);
    }

    /**
     * Returns models specified by the keys without transaction.
     * 
     * @param <M>
     *            the model type
     * @param modelMeta
     *            the meta data of model
     * @param keys
     *            the keys
     * @return models specified by the keys
     * @throws NullPointerException
     *             if the modelMeta parameter is null
     * @throws IllegalArgumentException
     *             if the kind of the key is different from the kind of the
     *             model or if the model class is not assignable from entity
     *             class
     */
    public static <M> Map<Key, M> getAsMapWithoutTx(ModelMeta<M> modelMeta,
            Key... keys) throws NullPointerException, IllegalArgumentException {
        return delegate().getAsMapWithoutTx(modelMeta, keys);
    }

    /**
     * Returns models specified by the keys without transaction asynchronously.
     * 
     * @param <M>
     *            the model type
     * @param modelMeta
     *            the meta data of model
     * @param keys
     *            the keys
     * @return models represented as {@link Future}
     * @throws NullPointerException
     *             if the modelMeta parameter is null
     */
    public static <M> Future<Map<Key, M>> getAsMapWithoutTxAsync(
            ModelMeta<M> modelMeta, Key... keys) throws NullPointerException {
        return asyncDelegate().getAsMapWithoutTxAsync(modelMeta, keys);
    }

    /**
     * Returns entities specified by the keys within the provided transaction.
     * 
     * @param tx
     *            the transaction
     * @param keys
     *            the keys
     * @return entities specified by the keys
     * @throws NullPointerException
     *             if the keys parameter is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     */
    public static Map<Key, Entity> getAsMap(Transaction tx, Iterable<Key> keys)
            throws NullPointerException, IllegalStateException {
        return delegate().getAsMap(tx, keys);
    }

    /**
     * Returns entities specified by the keys within the provided transaction
     * asynchronously.
     * 
     * @param tx
     *            the transaction
     * @param keys
     *            the keys
     * @return entities represented as {@link Future}
     * @throws NullPointerException
     *             if the keys parameter is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     */
    public static Future<Map<Key, Entity>> getAsMapAsync(Transaction tx,
            Iterable<Key> keys) throws NullPointerException,
            IllegalStateException {
        return asyncDelegate().getAsMapAsync(tx, keys);
    }

    /**
     * Returns entities specified by the keys within the provided transaction.
     * 
     * @param tx
     *            the transaction
     * @param keys
     *            the keys
     * @return entities specified by the keys
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     */
    public static Map<Key, Entity> getAsMap(Transaction tx, Key... keys)
            throws IllegalStateException {
        return delegate().getAsMap(tx, keys);
    }

    /**
     * Returns entities specified by the keys within the provided transaction
     * asynchronously.
     * 
     * @param tx
     *            the transaction
     * @param keys
     *            the keys
     * @return entities represented as {@link Future}
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     */
    public static Future<Map<Key, Entity>> getAsMapAsync(Transaction tx,
            Key... keys) throws IllegalStateException {
        return asyncDelegate().getAsMapAsync(tx, keys);
    }

    /**
     * Returns models specified by the keys within the provided transaction.
     * 
     * @param <M>
     *            the model type
     * @param tx
     *            the transaction
     * @param modelClass
     *            the model class
     * @param keys
     *            the keys
     * @return entities specified by the key
     * @throws NullPointerException
     *             if the modelClass parameter is null or if the keys parameter
     *             is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     * @throws IllegalArgumentException
     *             if the kind of the key is different from the kind of the
     *             model or if the model class is not assignable from entity
     *             class
     */
    public static <M> Map<Key, M> getAsMap(Transaction tx, Class<M> modelClass,
            Iterable<Key> keys) throws NullPointerException,
            IllegalStateException, IllegalArgumentException {
        return delegate().getAsMap(tx, modelClass, keys);
    }

    /**
     * Returns models specified by the keys within the provided transaction
     * asynchronously.
     * 
     * @param <M>
     *            the model type
     * @param tx
     *            the transaction
     * @param modelClass
     *            the model class
     * @param keys
     *            the keys
     * @return entities represented as {@link Future}
     * @throws NullPointerException
     *             if the modelClass parameter is null or if the keys parameter
     *             is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     */
    public static <M> Future<Map<Key, M>> getAsMapAsync(Transaction tx,
            Class<M> modelClass, Iterable<Key> keys)
            throws NullPointerException, IllegalStateException {
        return asyncDelegate().getAsMapAsync(tx, modelClass, keys);
    }

    /**
     * Returns models specified by the keys within the provided transaction.
     * 
     * @param <M>
     *            the model type
     * @param tx
     *            the transaction
     * @param modelMeta
     *            the meta data of model
     * @param keys
     *            the keys
     * @return entities specified by the key
     * @throws NullPointerException
     *             if the modelMeta parameter is null or if the keys parameter
     *             is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     * @throws IllegalArgumentException
     *             if the kind of the key is different from the kind of the
     *             model or if the model class is not assignable from entity
     *             class
     */
    public static <M> Map<Key, M> getAsMap(Transaction tx,
            ModelMeta<M> modelMeta, Iterable<Key> keys)
            throws NullPointerException, IllegalStateException,
            IllegalArgumentException {
        return delegate().getAsMap(tx, modelMeta, keys);

    }

    /**
     * Returns models specified by the keys within the provided transaction
     * asynchronously.
     * 
     * @param <M>
     *            the model type
     * @param tx
     *            the transaction
     * @param modelMeta
     *            the meta data of model
     * @param keys
     *            the keys
     * @return entities represented as {@link Future}
     * @throws NullPointerException
     *             if the modelMeta parameter is null or if the keys parameter
     *             is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     */
    public static <M> Future<Map<Key, M>> getAsMapAsync(Transaction tx,
            ModelMeta<M> modelMeta, Iterable<Key> keys)
            throws NullPointerException, IllegalStateException {
        return asyncDelegate().getAsMapAsync(tx, modelMeta, keys);

    }

    /**
     * Returns models specified by the keys within the provided transaction.
     * 
     * @param <M>
     *            the model type
     * @param tx
     *            the transaction
     * @param modelClass
     *            the model class
     * @param keys
     *            the keys
     * @return entities specified by the key
     * @throws NullPointerException
     *             if the modelClass parameter is null or if the keys parameter
     *             is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     * @throws IllegalArgumentException
     *             if the kind of the key is different from the kind of the
     *             model or if the model class is not assignable from entity
     *             class
     */
    public static <M> Map<Key, M> getAsMap(Transaction tx, Class<M> modelClass,
            Key... keys) throws NullPointerException, IllegalStateException,
            IllegalArgumentException {
        return delegate().getAsMap(tx, modelClass, keys);
    }

    /**
     * Returns models specified by the keys within the provided transaction
     * asynchronously.
     * 
     * @param <M>
     *            the model type
     * @param tx
     *            the transaction
     * @param modelClass
     *            the model class
     * @param keys
     *            the keys
     * @return entities represented as {@link Future}
     * @throws NullPointerException
     *             if the modelClass parameter is null or if the keys parameter
     *             is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     */
    public static <M> Future<Map<Key, M>> getAsMapAsync(Transaction tx,
            Class<M> modelClass, Key... keys) throws NullPointerException,
            IllegalStateException {
        return asyncDelegate().getAsMapAsync(tx, modelClass, keys);
    }

    /**
     * Returns models specified by the keys within the provided transaction.
     * 
     * @param <M>
     *            the model type
     * @param tx
     *            the transaction
     * @param modelMeta
     *            the meta data of model
     * @param keys
     *            the keys
     * @return entities specified by the key
     * @throws NullPointerException
     *             if the modelMeta parameter is null or if the keys parameter
     *             is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     * @throws IllegalArgumentException
     *             if the kind of the key is different from the kind of the
     *             model or if the model class is not assignable from entity
     *             class
     */
    public static <M> Map<Key, M> getAsMap(Transaction tx,
            ModelMeta<M> modelMeta, Key... keys) throws NullPointerException,
            IllegalStateException, IllegalArgumentException {
        return delegate().getAsMap(tx, modelMeta, keys);
    }

    /**
     * Returns models specified by the keys within the provided transaction
     * asynchronously.
     * 
     * @param <M>
     *            the model type
     * @param tx
     *            the transaction
     * @param modelMeta
     *            the meta data of model
     * @param keys
     *            the keys
     * @return entities represented as {@link Future}
     * @throws NullPointerException
     *             if the modelMeta parameter is null or if the keys parameter
     *             is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     */
    public static <M> Future<Map<Key, M>> getAsMapAsync(Transaction tx,
            ModelMeta<M> modelMeta, Key... keys) throws NullPointerException,
            IllegalStateException {
        return asyncDelegate().getAsMapAsync(tx, modelMeta, keys);
    }

    /**
     * Puts the entity to datastore. If there is a current transaction, this
     * operation will execute within that transaction.
     * 
     * @param entity
     *            the entity
     * @return a key
     * @throws NullPointerException
     *             if the entity parameter is null
     */
    public static Key put(Entity entity) throws NullPointerException {
        return delegate().put(entity);
    }

    /**
     * Puts the entity to datastore asynchronously. If there is a current
     * transaction, this operation will execute within that transaction.
     * 
     * @param entity
     *            the entity
     * @return a key represented as {@link Future}
     * @throws NullPointerException
     *             if the entity parameter is null
     */
    public static Future<Key> putAsync(Entity entity)
            throws NullPointerException {
        return asyncDelegate().putAsync(entity);
    }

    /**
     * Puts the entity to datastore without transaction.
     * 
     * @param entity
     *            the entity
     * @return a key
     * @throws NullPointerException
     *             if the entity parameter is null
     */
    public static Key putWithoutTx(Entity entity) throws NullPointerException {
        return delegate().putWithoutTx(entity);
    }

    /**
     * Puts the entity to datastore without transaction asynchronously.
     * 
     * @param entity
     *            the entity
     * @return a key represented as {@link Future}
     * @throws NullPointerException
     *             if the entity parameter is null
     */
    public static Future<Key> putWithoutTxAsync(Entity entity)
            throws NullPointerException {
        return asyncDelegate().putWithoutTxAsync(entity);
    }

    /**
     * Puts the model to datastore. If there is a current transaction, this
     * operation will execute within that transaction.
     * 
     * @param model
     *            the model
     * @return a key
     * @throws NullPointerException
     *             if the model parameter is null
     */
    public static Key put(Object model) throws NullPointerException {
        return delegate().put(model);
    }

    /**
     * Puts the model to datastore asynchronously. If there is a current
     * transaction, this operation will execute within that transaction.
     * 
     * @param model
     *            the model
     * @return a key represented as {@link Future}
     * @throws NullPointerException
     *             if the model parameter is null
     */
    public static Future<Key> putAsync(Object model)
            throws NullPointerException {
        return asyncDelegate().putAsync(model);
    }

    /**
     * Puts the model to datastore without transaction.
     * 
     * @param model
     *            the model
     * @return a key
     * @throws NullPointerException
     *             if the model parameter is null
     */
    public static Key putWithoutTx(Object model) throws NullPointerException {
        return delegate().putWithoutTx(model);
    }

    /**
     * Puts the model to datastore without transaction asynchronously.
     * 
     * @param model
     *            the model
     * @return a key represented as {@link Future}
     * @throws NullPointerException
     *             if the model parameter is null
     */
    public static Future<Key> putWithoutTxAsync(Object model)
            throws NullPointerException {
        return asyncDelegate().putWithoutTxAsync(model);
    }

    /**
     * Puts the entity to datastore within the provided transaction.
     * 
     * @param tx
     *            the transaction
     * @param entity
     *            the entity
     * @return a key
     * @throws NullPointerException
     *             if the entity parameter is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     */
    public static Key put(Transaction tx, Entity entity)
            throws NullPointerException, IllegalStateException {
        return delegate().put(tx, entity);
    }

    /**
     * Puts the entity to datastore within the provided transaction
     * asynchronously.
     * 
     * @param tx
     *            the transaction
     * @param entity
     *            the entity
     * @return a key represented as {@link Future}
     * @throws NullPointerException
     *             if the entity parameter is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     */
    public static Future<Key> putAsync(Transaction tx, Entity entity)
            throws NullPointerException, IllegalStateException {
        return asyncDelegate().putAsync(tx, entity);
    }

    /**
     * Puts the model to datastore within the provided transaction.
     * 
     * @param tx
     *            the transaction
     * @param model
     *            the model
     * @return a key
     * @throws NullPointerException
     *             if the model parameter is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     */
    public static Key put(Transaction tx, Object model)
            throws NullPointerException, IllegalStateException {
        return delegate().put(tx, model);
    }

    /**
     * Puts the model to datastore within the provided transaction
     * asynchronously.
     * 
     * @param tx
     *            the transaction
     * @param model
     *            the model
     * @return a key represented as {@link Future}
     * @throws NullPointerException
     *             if the model parameter is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     */
    public static Future<Key> putAsync(Transaction tx, Object model)
            throws NullPointerException, IllegalStateException {
        return asyncDelegate().putAsync(tx, model);
    }

    /**
     * Puts the models or entities to datastore. If there is a current
     * transaction, this operation will execute within that transaction.
     * 
     * @param models
     *            the models or entities
     * @return a list of keys
     * @throws NullPointerException
     *             if the models parameter is null
     */
    public static List<Key> put(Iterable<?> models) throws NullPointerException {
        return delegate().put(models);
    }

    /**
     * Puts the models or entities to datastore asynchronously. If there is a
     * current transaction, this operation will execute within that transaction.
     * 
     * @param models
     *            the models or entities
     * @return a list of keys represented as {@link Future}
     * @throws NullPointerException
     *             if the models parameter is null
     */
    public static Future<List<Key>> putAsync(Iterable<?> models)
            throws NullPointerException {
        return asyncDelegate().putAsync(models);
    }

    /**
     * Puts the models or entities to datastore without transaction.
     * 
     * @param models
     *            the models or entities
     * @return a list of keys
     * @throws NullPointerException
     *             if the models parameter is null
     */
    public static List<Key> putWithoutTx(Iterable<?> models)
            throws NullPointerException {
        return delegate().putWithoutTx(models);
    }

    /**
     * Puts the models or entities to datastore without transaction
     * asynchronously.
     * 
     * @param models
     *            the models or entities
     * @return a list of keys represented as {@link Future}
     * @throws NullPointerException
     *             if the models parameter is null
     */
    public static Future<List<Key>> putWithoutTxAsync(Iterable<?> models)
            throws NullPointerException {
        return asyncDelegate().putWithoutTxAsync(models);
    }

    /**
     * Puts the models or entities to datastore. If there is a current
     * transaction, this operation will execute within that transaction.
     * 
     * @param models
     *            the models or entities
     * @return a list of keys
     */
    public static List<Key> put(Object... models) {
        return delegate().put(models);
    }

    /**
     * Puts the models or entities to datastore asynchronously. If there is a
     * current transaction, this operation will execute within that transaction.
     * 
     * @param models
     *            the models or entities
     * @return a list of keys represented as {@link Future}
     */
    public static Future<List<Key>> putAsync(Object... models) {
        return asyncDelegate().putAsync(models);
    }

    /**
     * Puts the models or entities to datastore without transaction.
     * 
     * @param models
     *            the models or entities
     * @return a list of keys
     */
    public static List<Key> putWithoutTx(Object... models) {
        return delegate().putWithoutTx(models);
    }

    /**
     * Puts the models or entities to datastore without transaction
     * asynchronously.
     * 
     * @param models
     *            the models or entities
     * @return a list of keys represented as {@link Future}
     */
    public static Future<List<Key>> putWithoutTxAsync(Object... models) {
        return asyncDelegate().putWithoutTxAsync(models);
    }

    /**
     * Puts the models or entities to datastore within the provided transaction.
     * 
     * @param tx
     *            the transaction
     * @param models
     *            the models or entities
     * @return a list of keys
     * @throws NullPointerException
     *             if the models parameter is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     */
    public static List<Key> put(Transaction tx, Iterable<?> models)
            throws NullPointerException, IllegalStateException {
        return delegate().put(tx, models);
    }

    /**
     * Puts the models or entities to datastore within the provided transaction
     * asynchronously.
     * 
     * @param tx
     *            the transaction
     * @param models
     *            the models or entities
     * @return a list of keys represented as {@link Future}
     * @throws NullPointerException
     *             if the models parameter is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     */
    public static Future<List<Key>> putAsync(Transaction tx, Iterable<?> models)
            throws NullPointerException, IllegalStateException {
        return asyncDelegate().putAsync(tx, models);
    }

    /**
     * Puts the models or entities to datastore within the provided transaction.
     * 
     * @param tx
     *            the transaction
     * @param models
     *            the models or entities
     * @return a list of keys
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     */
    public static List<Key> put(Transaction tx, Object... models)
            throws IllegalStateException {
        return delegate().put(tx, models);
    }

    /**
     * Puts the models or entities to datastore within the provided transaction
     * asynchronously.
     * 
     * @param tx
     *            the transaction
     * @param models
     *            the models or entities
     * @return a list of keys represented as {@link Future}
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     */
    public static Future<List<Key>> putAsync(Transaction tx, Object... models)
            throws IllegalStateException {
        return asyncDelegate().putAsync(tx, models);
    }

    /**
     * Deletes entities specified by the keys. If there is a current
     * transaction, this operation will execute within that transaction.
     * 
     * @param keys
     *            the keys
     * @throws NullPointerException
     *             if the keys parameter is null
     */
    public static void delete(Iterable<Key> keys) throws NullPointerException {
        delegate().delete(keys);
    }

    /**
     * Deletes entities specified by the keys asynchronously. If there is a
     * current transaction, this operation will execute within that transaction.
     * 
     * @param keys
     *            the keys
     * @return a {@link Void} represented as {@link Future}
     * @throws NullPointerException
     *             if the keys parameter is null
     */
    public static Future<Void> deleteAsync(Iterable<Key> keys)
            throws NullPointerException {
        return asyncDelegate().deleteAsync(keys);
    }

    /**
     * Deletes entities specified by the keys without transaction.
     * 
     * @param keys
     *            the keys
     * @throws NullPointerException
     *             if the keys parameter is null
     */
    public static void deleteWithoutTx(Iterable<Key> keys)
            throws NullPointerException {
        delegate().deleteWithoutTx(keys);
    }

    /**
     * Deletes entities specified by the keys without transaction
     * asynchronously.
     * 
     * @param keys
     *            the keys
     * @return a {@link Void} represented as {@link Future}
     * @throws NullPointerException
     *             if the keys parameter is null
     */
    public static Future<Void> deleteWithoutTxAsync(Iterable<Key> keys)
            throws NullPointerException {
        return asyncDelegate().deleteWithoutTxAsync(keys);
    }

    /**
     * Deletes entities specified by the keys. If there is a current
     * transaction, this operation will execute within that transaction.
     * 
     * @param keys
     *            the keys
     */
    public static void delete(Key... keys) {
        delegate().delete(keys);
    }

    /**
     * Deletes entities specified by the keys asynchronously. If there is a
     * current transaction, this operation will execute within that transaction.
     * 
     * @param keys
     *            the keys
     * @return a {@link Void} represented as {@link Future}
     */
    public static Future<Void> deleteAsync(Key... keys) {
        return asyncDelegate().deleteAsync(keys);
    }

    /**
     * Deletes entities specified by the keys without transaction.
     * 
     * @param keys
     *            the keys
     */
    public static void deleteWithoutTx(Key... keys) {
        delegate().deleteWithoutTx(keys);
    }

    /**
     * Deletes entities specified by the keys without transaction
     * asynchronously.
     * 
     * @param keys
     *            the keys
     * @return a {@link Void} represented as {@link Future}
     */
    public static Future<Void> deleteWithoutTxAsync(Key... keys) {
        return asyncDelegate().deleteWithoutTxAsync(keys);
    }

    /**
     * Deletes entities specified by the keys within the provided transaction.
     * 
     * @param tx
     *            the transaction
     * @param keys
     *            the keys
     * @throws NullPointerException
     *             if the keys parameter is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     */
    public static void delete(Transaction tx, Iterable<Key> keys)
            throws NullPointerException, IllegalStateException {
        delegate().delete(tx, keys);
    }

    /**
     * Deletes entities specified by the keys within the provided transaction
     * asynchronously.
     * 
     * @param tx
     *            the transaction
     * @param keys
     *            the keys
     * @return a {@link Void} represented as {@link Future}
     * @throws NullPointerException
     *             if the keys parameter is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     */
    public static Future<Void> deleteAsync(Transaction tx, Iterable<Key> keys)
            throws NullPointerException, IllegalStateException {
        return asyncDelegate().deleteAsync(tx, keys);
    }

    /**
     * Deletes entities specified by the keys within the provided transaction.
     * 
     * @param tx
     *            the transaction
     * @param keys
     *            the keys
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     */
    public static void delete(Transaction tx, Key... keys)
            throws IllegalStateException {
        delegate().delete(tx, keys);
    }

    /**
     * Deletes entities specified by the keys within the provided transaction
     * asynchronously.
     * 
     * @param tx
     *            the transaction
     * @param keys
     *            the keys
     * @return a {@link Void} represented as {@link Future}
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     */
    public static Future<Void> deleteAsync(Transaction tx, Key... keys)
            throws IllegalStateException {
        return asyncDelegate().deleteAsync(tx, keys);
    }

    /**
     * Deletes all descendant entities.
     * 
     * @param ancestorKey
     *            the ancestor key
     * @throws NullPointerException
     *             if the ancestorKey parameter is null
     */
    public static void deleteAll(Key ancestorKey) throws NullPointerException {
        delegate().deleteAll(ancestorKey);
    }

    /**
     * Deletes all descendant entities asynchronously.
     * 
     * @param ancestorKey
     *            the ancestor key
     * @return a {@link Void} represented as {@link Future}
     * @throws NullPointerException
     *             if the ancestorKey parameter is null
     */
    public static Future<Void> deleteAllAsync(Key ancestorKey)
            throws NullPointerException {
        return asyncDelegate().deleteAllAsync(ancestorKey);
    }

    /**
     * Deletes all descendant entities within the provided transaction.
     * 
     * @param tx
     *            the transaction
     * @param ancestorKey
     *            the ancestor key
     * @throws NullPointerException
     *             if the ancestorKey parameter is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     */
    public static void deleteAll(Transaction tx, Key ancestorKey)
            throws NullPointerException, IllegalStateException {
        delegate().deleteAll(tx, ancestorKey);
    }

    /**
     * Deletes all descendant entities within the provided transaction
     * asynchronously.
     * 
     * @param tx
     *            the transaction
     * @param ancestorKey
     *            the ancestor key
     * @return a {@link Void} represented as {@link Future}
     * @throws NullPointerException
     *             if the ancestorKey parameter is null
     * @throws IllegalStateException
     *             if the transaction is not null and the transaction is not
     *             active
     */
    public static Future<Void> deleteAllAsync(Transaction tx, Key ancestorKey)
            throws NullPointerException, IllegalStateException {
        return asyncDelegate().deleteAllAsync(tx, ancestorKey);
    }

    /**
     * Deletes all descendant entities without transaction.
     * 
     * @param ancestorKey
     *            the ancestor key
     * @throws NullPointerException
     *             if the ancestorKey parameter is null
     */
    public static void deleteAllWithoutTx(Key ancestorKey)
            throws NullPointerException {
        delegate().deleteAllWithoutTx(ancestorKey);
    }

    /**
     * Deletes all descendant entities without transaction asynchronously.
     * 
     * @param ancestorKey
     *            the ancestor key
     * @return a {@link Void} represented as {@link Future}
     * @throws NullPointerException
     *             if the ancestorKey parameter is null
     */
    public static Future<Void> deleteAllWithoutTxAsync(Key ancestorKey)
            throws NullPointerException {
        return asyncDelegate().deleteAllWithoutTxAsync(ancestorKey);
    }

    /**
     * Returns a {@link ModelQuery}.
     * 
     * @param <M>
     *            the model type
     * @param modelClass
     *            the model class
     * @return a {@link ModelQuery}
     * @throws NullPointerException
     *             if the modelClass parameter is null
     */
    public static <M> ModelQuery<M> query(Class<M> modelClass)
            throws NullPointerException {
        return delegate().query(modelClass);
    }

    /**
     * Returns a {@link ModelQuery}.
     * 
     * @param <M>
     *            the model type
     * @param modelMeta
     *            the meta data of model
     * @return a {@link ModelQuery}
     * @throws NullPointerException
     *             if the modelMeta parameter is null
     */
    public static <M> ModelQuery<M> query(ModelMeta<M> modelMeta)
            throws NullPointerException {
        return delegate().query(modelMeta);
    }

    /**
     * Returns a {@link ModelQuery}.
     * 
     * @param <M>
     *            the model type
     * @param modelClass
     *            the model class
     * @param ancestorKey
     *            the ancestor key
     * @return a {@link ModelQuery}
     * @throws NullPointerException
     *             if the modelClass parameter is null or if the ancestorKey
     *             parameter is null
     */
    public static <M> ModelQuery<M> query(Class<M> modelClass, Key ancestorKey)
            throws NullPointerException {
        return delegate().query(modelClass, ancestorKey);
    }

    /**
     * Returns a {@link ModelQuery}.
     * 
     * @param <M>
     *            the model type
     * @param modelMeta
     *            the meta data of model
     * @param ancestorKey
     *            the ancestor key
     * @return a {@link ModelQuery}
     * @throws NullPointerException
     *             if the modelMeta parameter is null or if the ancestorKey
     *             parameter is null
     */
    public static <M> ModelQuery<M> query(ModelMeta<M> modelMeta,
            Key ancestorKey) throws NullPointerException {
        return delegate().query(modelMeta, ancestorKey);
    }

    /**
     * Returns a {@link ModelQuery}.
     * 
     * @param <M>
     *            the model type
     * @param tx
     *            the transaction
     * @param modelClass
     *            the model class
     * @param ancestorKey
     *            the ancestor key
     * @return a {@link ModelQuery}
     * @throws NullPointerException
     *             if the modelClass parameter is null or if the ancestorKey
     *             parameter is null
     */
    public static <M> ModelQuery<M> query(Transaction tx, Class<M> modelClass,
            Key ancestorKey) throws NullPointerException {
        return delegate().query(tx, modelClass, ancestorKey);
    }

    /**
     * Returns a {@link ModelQuery}.
     * 
     * @param <M>
     *            the model type
     * @param tx
     *            the transaction
     * @param modelMeta
     *            the meta data of model
     * @param ancestorKey
     *            the ancestor key
     * @return a {@link ModelQuery}
     * @throws NullPointerException
     *             if the modelMeta parameter is null or if the ancestorKey
     *             parameter is null
     */
    public static <M> ModelQuery<M> query(Transaction tx,
            ModelMeta<M> modelMeta, Key ancestorKey)
            throws NullPointerException {
        return delegate().query(tx, modelMeta, ancestorKey);
    }

    /**
     * Returns an {@link EntityQuery}.
     * 
     * @param kind
     *            the kind
     * @return an {@link EntityQuery}
     * @throws NullPointerException
     *             if the kind parameter is null
     */
    public static EntityQuery query(String kind) throws NullPointerException {
        return delegate().query(kind);
    }

    /**
     * Returns an {@link EntityQuery}.
     * 
     * @param kind
     *            the kind
     * @param ancestorKey
     *            the ancestor key
     * @return an {@link EntityQuery}
     * @throws NullPointerException
     *             if the kind parameter is null or if the ancestorKey parameter
     *             is null
     */
    public static EntityQuery query(String kind, Key ancestorKey)
            throws NullPointerException {
        return delegate().query(kind, ancestorKey);
    }

    /**
     * Returns an {@link EntityQuery}.
     * 
     * @param tx
     *            the transaction
     * @param kind
     *            the kind
     * @param ancestorKey
     *            the ancestor key
     * @return an {@link EntityQuery}
     * @throws NullPointerException
     *             if the kind parameter is null or if the ancestorKey parameter
     *             is null
     */
    public static EntityQuery query(Transaction tx, String kind, Key ancestorKey)
            throws NullPointerException {
        return delegate().query(tx, kind, ancestorKey);
    }

    /**
     * Returns a {@link KindlessQuery}.
     * 
     * @return a {@link KindlessQuery}
     */
    public static KindlessQuery query() {
        return delegate().query();
    }

    /**
     * Returns a {@link KindlessQuery}.
     * 
     * @param ancestorKey
     *            the ancestor key
     * @return a {@link KindlessQuery}
     * @throws NullPointerException
     *             if the ancestorKey parameter is null
     */
    public static KindlessQuery query(Key ancestorKey)
            throws NullPointerException {
        return delegate().query(ancestorKey);
    }

    /**
     * Returns a {@link KindlessQuery}.
     * 
     * @param tx
     *            the transaction
     * @param ancestorKey
     *            the ancestor key
     * @return a {@link KindlessQuery}
     * @throws NullPointerException
     *             if the ancestorKey parameter is null
     */
    public static KindlessQuery query(Transaction tx, Key ancestorKey)
            throws NullPointerException {
        return delegate().query(tx, ancestorKey);
    }

    /**
     * Returns a meta data of the model
     * 
     * @param <M>
     *            the model type
     * @param modelClass
     *            the model class
     * @return a meta data of the model
     * @throws NullPointerException
     *             if the modelClass parameter is null
     */
    public static <M> ModelMeta<M> getModelMeta(Class<M> modelClass)
            throws NullPointerException {
        return delegate().getModelMeta(modelClass);
    }

    /**
     * Filters the list in memory.
     * 
     * @param <M>
     *            the model type
     * @param list
     *            the model list
     * @param criteria
     *            the filter criteria
     * @return the filtered list.
     * @throws NullPointerException
     *             if the list parameter is null or if the model of the list is
     *             null
     */
    public static <M> List<M> filterInMemory(List<M> list,
            InMemoryFilterCriterion... criteria) throws NullPointerException {
        return delegate().filterInMemory(list, criteria);
    }

    /**
     * Sorts the list.
     * 
     * @param <M>
     *            the model type
     * @param list
     *            the model list
     * @param criteria
     *            criteria to sort
     * @return the sorted list
     * @throws NullPointerException
     *             if the list parameter is null or if the criteria parameter is
     *             null
     */
    public static <M> List<M> sortInMemory(List<M> list,
            InMemorySortCriterion... criteria) throws NullPointerException {
        return delegate().sortInMemory(list, criteria);
    }

    /**
     * Sets the limited key for cipher to the current thread.
     * 
     * @param key
     *            the key
     * @since 1.0.6
     */
    public static void setLimitedCipherKey(String key) {
        CipherFactory.getFactory().setLimitedKey(key);
    }

    /**
     * Sets the global key for cipher.
     * 
     * @param key
     *            the key
     * @since 1.0.6
     */
    public static void setGlobalCipherKey(String key) {
        CipherFactory.getFactory().setGlobalKey(key);
    }

    private Datastore() {
    }
}