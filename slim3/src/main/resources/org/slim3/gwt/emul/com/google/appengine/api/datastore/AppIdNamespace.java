package com.google.appengine.api.datastore;

import java.io.Serializable;

public class AppIdNamespace implements Serializable, Comparable {

    private static final long serialVersionUID = 1L;
    
    private AppIdNamespace() {
    }
    
    public int compareTo(Object o) {
        return 0;
    }
}