package com.google.appengine.api.datastore;

import java.io.Serializable;

public class Query {
    
    protected Query() {
    }

    public static enum SortDirection {
        ASCENDING,
        DESCENDING;
    }
}