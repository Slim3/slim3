package com.google.appengine.api.datastore.client;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.shared.model.Bean;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Async service.
 */
public interface SerializationServiceAsync {

    /**
     * @param key
     * @param callback
     */
    void getKey(Key key, AsyncCallback<Key> callback);
    
    /**
     * @param bean
     * @param callback
     */
    void getEntity(Bean bean, AsyncCallback<Entity> callback);

    /**
     * @param callback
     */
    void forceListInclution(AsyncCallback<ArrayList<String>> callback);

    /**
     * @param callback
     */
    void forceMapInclution(AsyncCallback<HashMap<String, String>> callback);
}