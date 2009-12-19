package com.google.appengine.api.datastore;

import java.io.Serializable;

public class Query {
    
    protected Query() {
    }

    public static enum SortDirection {
        ASCENDING,
        DESCENDING;
    }
    
    public static class SortPredicate implements Seriliazable {
        
        private static final long serialVersionUID = 1L;
        
        private String propertyName;
        
        private SortDirection direction;
        
        protected SortPredicate() {
        }
        
        public String getPropertyName() {
            return propertyName;
        }
        
        public SortDirection getDirection() {
            return direction;
        }
    }
}