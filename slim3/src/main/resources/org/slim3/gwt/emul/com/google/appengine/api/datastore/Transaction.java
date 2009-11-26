package com.google.appengine.api.datastore;

import java.io.Serializable;

public interface Transaction {

    public void commit();
    
    public void rollback();
    
    public String getId();
    
    public boolean isActive();
}