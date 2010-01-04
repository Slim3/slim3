package org.slim3.datastore;

import java.io.Serializable;
import java.util.List;

import com.google.appengine.api.datastore.Entity;

public abstract class ModelMeta<M> {    

    protected ModelMeta() {
    }

    public String getKind() {
        throw new UnsupportedOperationException("This method is unsupported on GWT.");
    }

    public Class<M> getModelClass() {
        throw new UnsupportedOperationException("This method is unsupported on GWT.");
    }

    public List<String> getClassHierarchyList() {
        throw new UnsupportedOperationException("This method is unsupported on GWT.");
    }
    
    public M entityToModel(Entity entity) {
        throw new UnsupportedOperationException("This method is unsupported on GWT.");
    }

    public Entity modelToEntity(Object model) {
        throw new UnsupportedOperationException("This method is unsupported on GWT.");
    }
}