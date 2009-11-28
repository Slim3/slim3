package com.google.appengine.api.datastore;

import java.io.Serializable;

public class Key implements Serializable, Comparable {

    private static final long serialVersionUID = 1L;

    private Key parentKey;
    
    private String kind;
    
    private String appId;    
    
    private long id;

    private String name;
    
    private transient AppIdNamespace appIdNamespace;
        
    private Key() {
    }

    public String getKind() {
        return kind;
    }
    
    public Key getParent() {
        return parentKey;
    }
    
    public long getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
}