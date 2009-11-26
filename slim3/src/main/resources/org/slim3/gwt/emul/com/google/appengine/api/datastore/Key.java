package com.google.appengine.api.datastore;

import java.io.Serializable;

public class Key implements Serializable, Comparable {

    private static final long serialVersionUID = 1L;

    private String appId;

    private long id;

    private String name;
        
    private Key() {
    }

    public int compareTo(Object o) {
        throw new UnsupportedOperationException();
    }
}