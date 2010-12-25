package com.google.appengine.api.datastore.client;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.shared.model.Bean;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Service.
 */
@RemoteServiceRelativePath("test")
public interface SerializationService extends RemoteService {
    
    /**
     * @param key
     * @return Key
     */
    Key getKey(Key key);
    
    /**
     * @param entity
     * @return Entity
     */
    Entity getEntity(Bean entity);
    
    /**
     * Force gwt to include {@link ArrayList} into the serialization policy.
     * @return List
     */
    ArrayList<String> forceListInclution();
    
    /**
     * Force gwt to include {@link HashMap} into the serialization policy.
     * @return Map
     */
    HashMap<String, String> forceMapInclution();
}