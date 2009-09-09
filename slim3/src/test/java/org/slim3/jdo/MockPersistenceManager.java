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
package org.slim3.jdo;

import java.util.Collection;
import java.util.Date;
import java.util.EnumSet;
import java.util.Set;

import javax.jdo.Extent;
import javax.jdo.FetchGroup;
import javax.jdo.FetchPlan;
import javax.jdo.JDOException;
import javax.jdo.ObjectState;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;
import javax.jdo.Transaction;
import javax.jdo.datastore.JDOConnection;
import javax.jdo.datastore.Sequence;
import javax.jdo.listener.InstanceLifecycleListener;

/**
 * @author higa
 * 
 */
public class MockPersistenceManager implements PersistenceManager {

    /**
     * The current transaction.
     */
    protected Transaction tx = new MockTransaction(this);

    @SuppressWarnings("unchecked")
    public void addInstanceLifecycleListener(
            InstanceLifecycleListener listener, Class... classes) {
    }

    public void checkConsistency() {
    }

    public void close() {
    }

    public Transaction currentTransaction() {
        return tx;
    }

    public void deletePersistent(Object pc) {
    }

    public void deletePersistentAll(Object... pcs) {
    }

    @SuppressWarnings("unchecked")
    public void deletePersistentAll(Collection pcs) {
    }

    public <T> T detachCopy(T pc) {
        return null;
    }

    public <T> Collection<T> detachCopyAll(Collection<T> pcs) {
        return null;
    }

    public <T> T[] detachCopyAll(T... pcs) {
        return null;
    }

    public void evict(Object pc) {
    }

    public void evictAll() {
    }

    public void evictAll(Object... pcs) {
    }

    @SuppressWarnings("unchecked")
    public void evictAll(Collection pcs) {
    }

    @SuppressWarnings("unchecked")
    public void evictAll(boolean subclasses, Class pcClass) {
    }

    public void flush() {
    }

    public boolean getCopyOnAttach() {
        return false;
    }

    public JDOConnection getDataStoreConnection() {
        return null;
    }

    public boolean getDetachAllOnCommit() {
        return false;
    }

    public <T> Extent<T> getExtent(Class<T> persistenceCapableClass) {
        return null;
    }

    public <T> Extent<T> getExtent(Class<T> persistenceCapableClass,
            boolean subclasses) {
        return null;
    }

    @SuppressWarnings("unchecked")
    public FetchGroup getFetchGroup(Class cls, String name) {
        return null;
    }

    public FetchPlan getFetchPlan() {
        return null;
    }

    public boolean getIgnoreCache() {
        return false;
    }

    @SuppressWarnings("unchecked")
    public Set getManagedObjects() {
        return null;
    }

    @SuppressWarnings("unchecked")
    public Set getManagedObjects(EnumSet<ObjectState> states) {
        return null;
    }

    @SuppressWarnings("unchecked")
    public Set getManagedObjects(Class... classes) {
        return null;
    }

    @SuppressWarnings("unchecked")
    public Set getManagedObjects(EnumSet<ObjectState> states, Class... classes) {
        return null;
    }

    public boolean getMultithreaded() {
        return false;
    }

    public Object getObjectById(Object oid) {
        return null;
    }

    public Object getObjectById(Object oid, boolean validate) {
        return null;
    }

    public <T> T getObjectById(Class<T> cls, Object key) {
        return null;
    }

    public Object getObjectId(Object pc) {
        return null;
    }

    @SuppressWarnings("unchecked")
    public Class getObjectIdClass(Class cls) {
        return null;
    }

    @SuppressWarnings("unchecked")
    public Collection getObjectsById(Collection oids) {
        return null;
    }

    public Object[] getObjectsById(Object... oids) {
        return null;
    }

    @SuppressWarnings("unchecked")
    public Collection getObjectsById(Collection oids, boolean validate) {
        return null;
    }

    @SuppressWarnings("deprecation")
    public Object[] getObjectsById(Object[] oids, boolean validate) {
        return null;
    }

    public Object[] getObjectsById(boolean validate, Object... oids) {
        return null;
    }

    public PersistenceManagerFactory getPersistenceManagerFactory() {
        return null;
    }

    public Sequence getSequence(String name) {
        return null;
    }

    public Date getServerDate() {
        return null;
    }

    public Object getTransactionalObjectId(Object pc) {
        return null;
    }

    public Object getUserObject() {
        return null;
    }

    public Object getUserObject(Object key) {
        return null;
    }

    public boolean isClosed() {
        return false;
    }

    public void makeNontransactional(Object pc) {
    }

    public void makeNontransactionalAll(Object... pcs) {
    }

    @SuppressWarnings("unchecked")
    public void makeNontransactionalAll(Collection pcs) {
    }

    public <T> T makePersistent(T pc) {
        return null;
    }

    public <T> T[] makePersistentAll(T... pcs) {
        return null;
    }

    public <T> Collection<T> makePersistentAll(Collection<T> pcs) {
        return null;
    }

    public void makeTransactional(Object pc) {
    }

    public void makeTransactionalAll(Object... pcs) {
    }

    @SuppressWarnings("unchecked")
    public void makeTransactionalAll(Collection pcs) {
    }

    public void makeTransient(Object pc) {
    }

    public void makeTransient(Object pc, boolean useFetchPlan) {
    }

    public void makeTransientAll(Object... pcs) {
    }

    @SuppressWarnings("unchecked")
    public void makeTransientAll(Collection pcs) {
    }

    @SuppressWarnings("deprecation")
    public void makeTransientAll(Object[] pcs, boolean useFetchPlan) {
    }

    public void makeTransientAll(boolean useFetchPlan, Object... pcs) {
    }

    @SuppressWarnings("unchecked")
    public void makeTransientAll(Collection pcs, boolean useFetchPlan) {
    }

    public <T> T newInstance(Class<T> pcClass) {
        return null;
    }

    @SuppressWarnings("unchecked")
    public Query newNamedQuery(Class cls, String queryName) {
        return null;
    }

    @SuppressWarnings("unchecked")
    public Object newObjectIdInstance(Class pcClass, Object key) {
        return null;
    }

    public Query newQuery() {
        return null;
    }

    public Query newQuery(Object compiled) {
        return null;
    }

    public Query newQuery(String query) {
        return null;
    }

    @SuppressWarnings("unchecked")
    public Query newQuery(Class cls) {
        return null;
    }

    @SuppressWarnings("unchecked")
    public Query newQuery(Extent cln) {
        return null;
    }

    public Query newQuery(String language, Object query) {
        return null;
    }

    @SuppressWarnings("unchecked")
    public Query newQuery(Class cls, Collection cln) {
        return null;
    }

    @SuppressWarnings("unchecked")
    public Query newQuery(Class cls, String filter) {
        return null;
    }

    @SuppressWarnings("unchecked")
    public Query newQuery(Extent cln, String filter) {
        return null;
    }

    @SuppressWarnings("unchecked")
    public Query newQuery(Class cls, Collection cln, String filter) {
        return null;
    }

    public Object putUserObject(Object key, Object val) {
        return null;
    }

    public void refresh(Object pc) {
    }

    public void refreshAll() {
    }

    public void refreshAll(Object... pcs) {
    }

    @SuppressWarnings("unchecked")
    public void refreshAll(Collection pcs) {
    }

    public void refreshAll(JDOException jdoe) {
    }

    public void removeInstanceLifecycleListener(
            InstanceLifecycleListener listener) {
    }

    public Object removeUserObject(Object key) {
        return null;
    }

    public void retrieve(Object pc) {
    }

    public void retrieve(Object pc, boolean useFetchPlan) {
    }

    @SuppressWarnings("unchecked")
    public void retrieveAll(Collection pcs) {
    }

    public void retrieveAll(Object... pcs) {
    }

    @SuppressWarnings("unchecked")
    public void retrieveAll(Collection pcs, boolean useFetchPlan) {
    }

    @SuppressWarnings("deprecation")
    public void retrieveAll(Object[] pcs, boolean useFetchPlan) {
    }

    public void retrieveAll(boolean useFetchPlan, Object... pcs) {
    }

    public void setCopyOnAttach(boolean flag) {
    }

    public void setDetachAllOnCommit(boolean flag) {
    }

    public void setIgnoreCache(boolean flag) {
    }

    public void setMultithreaded(boolean flag) {
    }

    public void setUserObject(Object o) {
    }

    public Integer getQueryTimeoutMillis() {
        return null;
    }

    public void setQueryTimeoutMillis(Integer arg0) {
    }
}