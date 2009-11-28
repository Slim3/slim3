package com.google.appengine.api.datastore;

import java.io.Serializable;

public class AppIdNamespace implements Serializable, Comparable {

    private static final long serialVersionUID = 1L;
    
    private String appId;
    
    private String namespace;
    
    private AppIdNamespace() {
    }
    
    public AppIdNamespace(String appId, String namespace) {
        this.appId = appId;
        this.namespace = namespace;
    }
    
    public int compareTo(Object o) {
        return 0;
    }
}