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

    @SuppressWarnings("unchecked")
    @Override
    public void addInstanceLifecycleListener(
            InstanceLifecycleListener listener, Class... classes) {
    }

    @Override
    public void checkConsistency() {
    }

    @Override
    public void close() {
    }

    @Override
    public Transaction currentTransaction() {
        return null;
    }

    @Override
    public void deletePersistent(Object pc) {
    }

    @Override
    public void deletePersistentAll(Object... pcs) {
    }

    @SuppressWarnings("unchecked")
    @Override
    public void deletePersistentAll(Collection pcs) {
    }

    @Override
    public <T> T detachCopy(T pc) {
        return null;
    }

    @Override
    public <T> Collection<T> detachCopyAll(Collection<T> pcs) {
        return null;
    }

    @Override
    public <T> T[] detachCopyAll(T... pcs) {
        return null;
    }

    @Override
    public void evict(Object pc) {
    }

    @Override
    public void evictAll() {
    }

    @Override
    public void evictAll(Object... pcs) {
    }

    @SuppressWarnings("unchecked")
    @Override
    public void evictAll(Collection pcs) {
    }

    @SuppressWarnings("unchecked")
    @Override
    public void evictAll(boolean subclasses, Class pcClass) {
    }

    @Override
    public void flush() {
    }

    @Override
    public boolean getCopyOnAttach() {
        return false;
    }

    @Override
    public JDOConnection getDataStoreConnection() {
        return null;
    }

    @Override
    public boolean getDetachAllOnCommit() {
        return false;
    }

    @Override
    public <T> Extent<T> getExtent(Class<T> persistenceCapableClass) {
        return null;
    }

    @Override
    public <T> Extent<T> getExtent(Class<T> persistenceCapableClass,
            boolean subclasses) {
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public FetchGroup getFetchGroup(Class cls, String name) {
        return null;
    }

    @Override
    public FetchPlan getFetchPlan() {
        return null;
    }

    @Override
    public boolean getIgnoreCache() {
        return false;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Set getManagedObjects() {
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Set getManagedObjects(EnumSet<ObjectState> states) {
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Set getManagedObjects(Class... classes) {
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Set getManagedObjects(EnumSet<ObjectState> states, Class... classes) {
        return null;
    }

    @Override
    public boolean getMultithreaded() {
        return false;
    }

    @Override
    public Object getObjectById(Object oid) {
        return null;
    }

    @Override
    public Object getObjectById(Object oid, boolean validate) {
        return null;
    }

    @Override
    public <T> T getObjectById(Class<T> cls, Object key) {
        return null;
    }

    @Override
    public Object getObjectId(Object pc) {
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Class getObjectIdClass(Class cls) {
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Collection getObjectsById(Collection oids) {
        return null;
    }

    @Override
    public Object[] getObjectsById(Object... oids) {
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Collection getObjectsById(Collection oids, boolean validate) {
        return null;
    }

    @SuppressWarnings("deprecation")
    @Override
    public Object[] getObjectsById(Object[] oids, boolean validate) {
        return null;
    }

    @Override
    public Object[] getObjectsById(boolean validate, Object... oids) {
        return null;
    }

    @Override
    public PersistenceManagerFactory getPersistenceManagerFactory() {
        return null;
    }

    @Override
    public Sequence getSequence(String name) {
        return null;
    }

    @Override
    public Date getServerDate() {
        return null;
    }

    @Override
    public Object getTransactionalObjectId(Object pc) {
        return null;
    }

    @Override
    public Object getUserObject() {
        return null;
    }

    @Override
    public Object getUserObject(Object key) {
        return null;
    }

    @Override
    public boolean isClosed() {
        return false;
    }

    @Override
    public void makeNontransactional(Object pc) {
    }

    @Override
    public void makeNontransactionalAll(Object... pcs) {
    }

    @SuppressWarnings("unchecked")
    @Override
    public void makeNontransactionalAll(Collection pcs) {
    }

    @Override
    public <T> T makePersistent(T pc) {
        return null;
    }

    @Override
    public <T> T[] makePersistentAll(T... pcs) {
        return null;
    }

    @Override
    public <T> Collection<T> makePersistentAll(Collection<T> pcs) {
        return null;
    }

    @Override
    public void makeTransactional(Object pc) {
    }

    @Override
    public void makeTransactionalAll(Object... pcs) {
    }

    @SuppressWarnings("unchecked")
    @Override
    public void makeTransactionalAll(Collection pcs) {
    }

    @Override
    public void makeTransient(Object pc) {
    }

    @Override
    public void makeTransient(Object pc, boolean useFetchPlan) {
    }

    @Override
    public void makeTransientAll(Object... pcs) {
    }

    @SuppressWarnings("unchecked")
    @Override
    public void makeTransientAll(Collection pcs) {
    }

    @SuppressWarnings("deprecation")
    @Override
    public void makeTransientAll(Object[] pcs, boolean useFetchPlan) {
    }

    @Override
    public void makeTransientAll(boolean useFetchPlan, Object... pcs) {
    }

    @SuppressWarnings("unchecked")
    @Override
    public void makeTransientAll(Collection pcs, boolean useFetchPlan) {
    }

    @Override
    public <T> T newInstance(Class<T> pcClass) {
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Query newNamedQuery(Class cls, String queryName) {
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object newObjectIdInstance(Class pcClass, Object key) {
        return null;
    }

    @Override
    public Query newQuery() {
        return null;
    }

    @Override
    public Query newQuery(Object compiled) {
        return null;
    }

    @Override
    public Query newQuery(String query) {
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Query newQuery(Class cls) {
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Query newQuery(Extent cln) {
        return null;
    }

    @Override
    public Query newQuery(String language, Object query) {
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Query newQuery(Class cls, Collection cln) {
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Query newQuery(Class cls, String filter) {
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Query newQuery(Extent cln, String filter) {
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Query newQuery(Class cls, Collection cln, String filter) {
        return null;
    }

    @Override
    public Object putUserObject(Object key, Object val) {
        return null;
    }

    @Override
    public void refresh(Object pc) {
    }

    @Override
    public void refreshAll() {
    }

    @Override
    public void refreshAll(Object... pcs) {
    }

    @SuppressWarnings("unchecked")
    @Override
    public void refreshAll(Collection pcs) {
    }

    @Override
    public void refreshAll(JDOException jdoe) {
    }

    @Override
    public void removeInstanceLifecycleListener(
            InstanceLifecycleListener listener) {
    }

    @Override
    public Object removeUserObject(Object key) {
        return null;
    }

    @Override
    public void retrieve(Object pc) {
    }

    @Override
    public void retrieve(Object pc, boolean useFetchPlan) {
    }

    @SuppressWarnings("unchecked")
    @Override
    public void retrieveAll(Collection pcs) {
    }

    @Override
    public void retrieveAll(Object... pcs) {
    }

    @SuppressWarnings("unchecked")
    @Override
    public void retrieveAll(Collection pcs, boolean useFetchPlan) {
    }

    @SuppressWarnings("deprecation")
    @Override
    public void retrieveAll(Object[] pcs, boolean useFetchPlan) {
    }

    @Override
    public void retrieveAll(boolean useFetchPlan, Object... pcs) {
    }

    @Override
    public void setCopyOnAttach(boolean flag) {
    }

    @Override
    public void setDetachAllOnCommit(boolean flag) {
    }

    @Override
    public void setIgnoreCache(boolean flag) {
    }

    @Override
    public void setMultithreaded(boolean flag) {
    }

    @Override
    public void setUserObject(Object o) {
    }
}