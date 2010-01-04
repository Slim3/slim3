package com.google.appengine.api.datastore;

import java.io.Serializable;

public class Query implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    protected Query() {
    }

    public static enum SortDirection {
        ASCENDING,
        DESCENDING;
    }
    
    public static class SortPredicate implements Serializable {
        
        private static final long serialVersionUID = 1L;
        
        private String propertyName;
        
        private SortDirection direction;
        
        protected SortPredicate() {
        }
        
        public SortPredicate(String propertyName, SortDirection direction) {
            this.propertyName = propertyName;
            this.direction = direction;
        }
        
        public String getPropertyName() {
            return propertyName;
        }
        
        public SortDirection getDirection() {
            return direction;
        }
    }
}