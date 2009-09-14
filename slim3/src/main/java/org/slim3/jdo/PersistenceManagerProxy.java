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
 * The proxy of persistence manager.
 * 
 * @author higa
 * @since 3.0
 * 
 */
public class PersistenceManagerProxy implements PersistenceManager {

    @SuppressWarnings("unchecked")
    public void addInstanceLifecycleListener(
            InstanceLifecycleListener listener, Class... classes) {
        CurrentPersistenceManager
            .getAndCheckPresence()
            .addInstanceLifecycleListener(listener, classes);

    }

    public void checkConsistency() {
        CurrentPersistenceManager.getAndCheckPresence().checkConsistency();
    }

    public void close() {
        CurrentPersistenceManager.close();
    }

    public Transaction currentTransaction() {
        return CurrentPersistenceManager
            .getAndCheckPresence()
            .currentTransaction();
    }

    public void deletePersistent(Object pc) {
        CurrentPersistenceManager.getAndCheckPresence().deletePersistent(pc);
    }

    public void deletePersistentAll(Object... pcs) {
        CurrentPersistenceManager
            .getAndCheckPresence()
            .deletePersistentAll(pcs);
    }

    @SuppressWarnings("unchecked")
    public void deletePersistentAll(Collection pcs) {
        CurrentPersistenceManager
            .getAndCheckPresence()
            .deletePersistentAll(pcs);
    }

    public <T> T detachCopy(T pc) {
        return CurrentPersistenceManager.getAndCheckPresence().detachCopy(pc);
    }

    public <T> Collection<T> detachCopyAll(Collection<T> pcs) {
        return CurrentPersistenceManager.getAndCheckPresence().detachCopyAll(
            pcs);
    }

    public <T> T[] detachCopyAll(T... pcs) {
        return CurrentPersistenceManager.getAndCheckPresence().detachCopyAll(
            pcs);
    }

    public void evict(Object pc) {
        CurrentPersistenceManager.getAndCheckPresence().evict(pc);
    }

    public void evictAll() {
        CurrentPersistenceManager.getAndCheckPresence().evictAll();
    }

    public void evictAll(Object... pcs) {
        CurrentPersistenceManager.getAndCheckPresence().evictAll(pcs);
    }

    @SuppressWarnings("unchecked")
    public void evictAll(Collection pcs) {
        CurrentPersistenceManager.getAndCheckPresence().evictAll(pcs);

    }

    @SuppressWarnings("unchecked")
    public void evictAll(boolean subclasses, Class pcClass) {
        CurrentPersistenceManager.getAndCheckPresence().evictAll(
            subclasses,
            pcClass);

    }

    public void flush() {
        CurrentPersistenceManager.getAndCheckPresence().flush();
    }

    public boolean getCopyOnAttach() {
        return CurrentPersistenceManager
            .getAndCheckPresence()
            .getCopyOnAttach();
    }

    public JDOConnection getDataStoreConnection() {
        return CurrentPersistenceManager
            .getAndCheckPresence()
            .getDataStoreConnection();
    }

    public boolean getDetachAllOnCommit() {
        return CurrentPersistenceManager
            .getAndCheckPresence()
            .getDetachAllOnCommit();
    }

    public <T> Extent<T> getExtent(Class<T> persistenceCapableClass) {
        return CurrentPersistenceManager.getAndCheckPresence().getExtent(
            persistenceCapableClass);
    }

    public <T> Extent<T> getExtent(Class<T> persistenceCapableClass,
            boolean subclasses) {
        return CurrentPersistenceManager.getAndCheckPresence().getExtent(
            persistenceCapableClass,
            subclasses);
    }

    @SuppressWarnings("unchecked")
    public FetchGroup getFetchGroup(Class cls, String name) {
        return CurrentPersistenceManager.getAndCheckPresence().getFetchGroup(
            cls,
            name);
    }

    public FetchPlan getFetchPlan() {
        return CurrentPersistenceManager.getAndCheckPresence().getFetchPlan();
    }

    public boolean getIgnoreCache() {
        return CurrentPersistenceManager.getAndCheckPresence().getIgnoreCache();
    }

    @SuppressWarnings("unchecked")
    public Set getManagedObjects() {
        return CurrentPersistenceManager
            .getAndCheckPresence()
            .getManagedObjects();
    }

    @SuppressWarnings("unchecked")
    public Set getManagedObjects(EnumSet<ObjectState> states) {
        return CurrentPersistenceManager
            .getAndCheckPresence()
            .getManagedObjects(states);
    }

    @SuppressWarnings("unchecked")
    public Set getManagedObjects(Class... classes) {
        return CurrentPersistenceManager
            .getAndCheckPresence()
            .getManagedObjects(classes);
    }

    @SuppressWarnings("unchecked")
    public Set getManagedObjects(EnumSet<ObjectState> states, Class... classes) {
        return CurrentPersistenceManager
            .getAndCheckPresence()
            .getManagedObjects(states, classes);
    }

    public boolean getMultithreaded() {
        return CurrentPersistenceManager
            .getAndCheckPresence()
            .getMultithreaded();
    }

    public Object getObjectById(Object oid) {
        return CurrentPersistenceManager.getAndCheckPresence().getObjectById(
            oid);
    }

    public Object getObjectById(Object oid, boolean validate) {
        return CurrentPersistenceManager.getAndCheckPresence().getObjectById(
            oid,
            validate);
    }

    public <T> T getObjectById(Class<T> cls, Object key) {
        return CurrentPersistenceManager.getAndCheckPresence().getObjectById(
            cls,
            key);
    }

    public Object getObjectId(Object pc) {
        return CurrentPersistenceManager.getAndCheckPresence().getObjectId(pc);
    }

    @SuppressWarnings("unchecked")
    public Class getObjectIdClass(Class cls) {
        return CurrentPersistenceManager
            .getAndCheckPresence()
            .getObjectIdClass(cls);
    }

    @SuppressWarnings("unchecked")
    public Collection getObjectsById(Collection oids) {
        return CurrentPersistenceManager.getAndCheckPresence().getObjectsById(
            oids);
    }

    public Object[] getObjectsById(Object... oids) {
        return CurrentPersistenceManager.getAndCheckPresence().getObjectsById(
            oids);
    }

    @SuppressWarnings("unchecked")
    public Collection getObjectsById(Collection oids, boolean validate) {
        return CurrentPersistenceManager.getAndCheckPresence().getObjectsById(
            oids,
            validate);
    }

    @SuppressWarnings("deprecation")
    public Object[] getObjectsById(Object[] oids, boolean validate) {
        return CurrentPersistenceManager.getAndCheckPresence().getObjectsById(
            oids,
            validate);
    }

    public Object[] getObjectsById(boolean validate, Object... oids) {
        return CurrentPersistenceManager.getAndCheckPresence().getObjectsById(
            validate,
            oids);
    }

    public PersistenceManagerFactory getPersistenceManagerFactory() {
        return CurrentPersistenceManager
            .getAndCheckPresence()
            .getPersistenceManagerFactory();
    }

    public Integer getQueryTimeoutMillis() {
        return CurrentPersistenceManager
            .getAndCheckPresence()
            .getQueryTimeoutMillis();
    }

    public Sequence getSequence(String name) {
        return CurrentPersistenceManager
            .getAndCheckPresence()
            .getSequence(name);
    }

    public Date getServerDate() {
        return CurrentPersistenceManager.getAndCheckPresence().getServerDate();
    }

    public Object getTransactionalObjectId(Object pc) {
        return CurrentPersistenceManager
            .getAndCheckPresence()
            .getTransactionalObjectId(pc);
    }

    public Object getUserObject() {
        return CurrentPersistenceManager.getAndCheckPresence().getUserObject();
    }

    public Object getUserObject(Object key) {
        return CurrentPersistenceManager.getAndCheckPresence().getUserObject(
            key);
    }

    public boolean isClosed() {
        return CurrentPersistenceManager.getAndCheckPresence().isClosed();
    }

    public void makeNontransactional(Object pc) {
        CurrentPersistenceManager
            .getAndCheckPresence()
            .makeNontransactional(pc);
    }

    public void makeNontransactionalAll(Object... pcs) {
        CurrentPersistenceManager
            .getAndCheckPresence()
            .makeNontransactionalAll(pcs);

    }

    @SuppressWarnings("unchecked")
    public void makeNontransactionalAll(Collection pcs) {
        CurrentPersistenceManager
            .getAndCheckPresence()
            .makeNontransactionalAll(pcs);
    }

    public <T> T makePersistent(T pc) {
        return CurrentPersistenceManager.getAndCheckPresence().makePersistent(
            pc);
    }

    public <T> T[] makePersistentAll(T... pcs) {
        return CurrentPersistenceManager
            .getAndCheckPresence()
            .makePersistentAll(pcs);
    }

    public <T> Collection<T> makePersistentAll(Collection<T> pcs) {
        return CurrentPersistenceManager
            .getAndCheckPresence()
            .makePersistentAll(pcs);
    }

    public void makeTransactional(Object pc) {
        CurrentPersistenceManager.getAndCheckPresence().makeTransactional(pc);
    }

    public void makeTransactionalAll(Object... pcs) {
        CurrentPersistenceManager.getAndCheckPresence().makeTransactionalAll(
            pcs);
    }

    @SuppressWarnings("unchecked")
    public void makeTransactionalAll(Collection pcs) {
        CurrentPersistenceManager.getAndCheckPresence().makeTransactionalAll(
            pcs);
    }

    public void makeTransient(Object pc) {
        CurrentPersistenceManager.getAndCheckPresence().makeTransient(pc);
    }

    public void makeTransient(Object pc, boolean useFetchPlan) {
        CurrentPersistenceManager.getAndCheckPresence().makeTransient(
            pc,
            useFetchPlan);
    }

    public void makeTransientAll(Object... pcs) {
        CurrentPersistenceManager.getAndCheckPresence().makeTransientAll(pcs);
    }

    @SuppressWarnings("unchecked")
    public void makeTransientAll(Collection pcs) {
        CurrentPersistenceManager.getAndCheckPresence().makeTransientAll(pcs);

    }

    @SuppressWarnings("deprecation")
    public void makeTransientAll(Object[] pcs, boolean useFetchPlan) {
        CurrentPersistenceManager.getAndCheckPresence().makeTransientAll(
            pcs,
            useFetchPlan);
    }

    public void makeTransientAll(boolean useFetchPlan, Object... pcs) {
        CurrentPersistenceManager.getAndCheckPresence().makeTransientAll(
            useFetchPlan,
            pcs);
    }

    @SuppressWarnings("unchecked")
    public void makeTransientAll(Collection pcs, boolean useFetchPlan) {
        CurrentPersistenceManager.getAndCheckPresence().makeTransientAll(
            pcs,
            useFetchPlan);
    }

    public <T> T newInstance(Class<T> pcClass) {
        return CurrentPersistenceManager.getAndCheckPresence().newInstance(
            pcClass);
    }

    @SuppressWarnings("unchecked")
    public Query newNamedQuery(Class cls, String queryName) {
        return CurrentPersistenceManager.getAndCheckPresence().newNamedQuery(
            cls,
            queryName);
    }

    @SuppressWarnings("unchecked")
    public Object newObjectIdInstance(Class pcClass, Object key) {
        return CurrentPersistenceManager
            .getAndCheckPresence()
            .newObjectIdInstance(pcClass, key);
    }

    public Query newQuery() {
        return CurrentPersistenceManager.getAndCheckPresence().newQuery();
    }

    public Query newQuery(Object compiled) {
        return CurrentPersistenceManager.getAndCheckPresence().newQuery(
            compiled);
    }

    public Query newQuery(String query) {
        return CurrentPersistenceManager.getAndCheckPresence().newQuery(query);
    }

    @SuppressWarnings("unchecked")
    public Query newQuery(Class cls) {
        return CurrentPersistenceManager.getAndCheckPresence().newQuery(cls);
    }

    @SuppressWarnings("unchecked")
    public Query newQuery(Extent cln) {
        return CurrentPersistenceManager.getAndCheckPresence().newQuery(cln);
    }

    public Query newQuery(String language, Object query) {
        return CurrentPersistenceManager.getAndCheckPresence().newQuery(
            language,
            query);
    }

    @SuppressWarnings("unchecked")
    public Query newQuery(Class cls, Collection cln) {
        return CurrentPersistenceManager.getAndCheckPresence().newQuery(
            cls,
            cln);
    }

    @SuppressWarnings("unchecked")
    public Query newQuery(Class cls, String filter) {
        return CurrentPersistenceManager.getAndCheckPresence().newQuery(
            cls,
            filter);
    }

    @SuppressWarnings("unchecked")
    public Query newQuery(Extent cln, String filter) {
        return CurrentPersistenceManager.getAndCheckPresence().newQuery(
            cln,
            filter);
    }

    @SuppressWarnings("unchecked")
    public Query newQuery(Class cls, Collection cln, String filter) {
        return CurrentPersistenceManager.getAndCheckPresence().newQuery(
            cls,
            cln,
            filter);
    }

    public Object putUserObject(Object key, Object val) {
        return CurrentPersistenceManager.getAndCheckPresence().putUserObject(
            key,
            val);
    }

    public void refresh(Object pc) {
        CurrentPersistenceManager.getAndCheckPresence().refresh(pc);
    }

    public void refreshAll() {
        CurrentPersistenceManager.getAndCheckPresence().refreshAll();
    }

    public void refreshAll(Object... pcs) {
        CurrentPersistenceManager.getAndCheckPresence().refreshAll(pcs);
    }

    @SuppressWarnings("unchecked")
    public void refreshAll(Collection pcs) {
        CurrentPersistenceManager.getAndCheckPresence().refreshAll(pcs);
    }

    public void refreshAll(JDOException jdoe) {
        CurrentPersistenceManager.getAndCheckPresence().refreshAll(jdoe);
    }

    public void removeInstanceLifecycleListener(
            InstanceLifecycleListener listener) {
        CurrentPersistenceManager
            .getAndCheckPresence()
            .removeInstanceLifecycleListener(listener);
    }

    public Object removeUserObject(Object key) {
        return CurrentPersistenceManager
            .getAndCheckPresence()
            .removeUserObject(key);
    }

    public void retrieve(Object pc) {
        CurrentPersistenceManager.getAndCheckPresence().retrieve(pc);
    }

    public void retrieve(Object pc, boolean useFetchPlan) {
        CurrentPersistenceManager.getAndCheckPresence().retrieve(
            pc,
            useFetchPlan);
    }

    @SuppressWarnings("unchecked")
    public void retrieveAll(Collection pcs) {
        CurrentPersistenceManager.getAndCheckPresence().retrieveAll(pcs);
    }

    public void retrieveAll(Object... pcs) {
        CurrentPersistenceManager.getAndCheckPresence().retrieveAll(pcs);
    }

    @SuppressWarnings("unchecked")
    public void retrieveAll(Collection pcs, boolean useFetchPlan) {
        CurrentPersistenceManager.getAndCheckPresence().retrieveAll(
            pcs,
            useFetchPlan);
    }

    @SuppressWarnings("deprecation")
    public void retrieveAll(Object[] pcs, boolean useFetchPlan) {
        CurrentPersistenceManager.getAndCheckPresence().retrieveAll(
            pcs,
            useFetchPlan);
    }

    public void retrieveAll(boolean useFetchPlan, Object... pcs) {
        CurrentPersistenceManager.getAndCheckPresence().retrieveAll(
            useFetchPlan,
            pcs);
    }

    public void setCopyOnAttach(boolean flag) {
        CurrentPersistenceManager.getAndCheckPresence().setCopyOnAttach(flag);
    }

    public void setDetachAllOnCommit(boolean flag) {
        CurrentPersistenceManager.getAndCheckPresence().setDetachAllOnCommit(
            flag);
    }

    public void setIgnoreCache(boolean flag) {
        CurrentPersistenceManager.getAndCheckPresence().setIgnoreCache(flag);
    }

    public void setMultithreaded(boolean flag) {
        CurrentPersistenceManager.getAndCheckPresence().setMultithreaded(flag);
    }

    public void setQueryTimeoutMillis(Integer timeout) {
        CurrentPersistenceManager.getAndCheckPresence().setQueryTimeoutMillis(
            timeout);
    }

    public void setUserObject(Object o) {
        CurrentPersistenceManager.getAndCheckPresence().setUserObject(o);
    }
}