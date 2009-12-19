package org.slim3.datastore;

import java.io.Serializable;
import java.util.List;

import com.google.appengine.api.datastore.Entity;

public abstract class ModelMeta<M> implements Serializable {

    private static final long serialVersionUID = 1L;
    
    protected String kind;

    protected Class<M> modelClass;

    protected List<String> classHierarchyList;

    protected ModelMeta() {
    }

    public String getKind() {
        return kind;
    }

    public Class<M> getModelClass() {
        return modelClass;
    }

    public List<String> getClassHierarchyList() {
        return classHierarchyList;
    }
    
    public M entityToModel(Entity entity) {
        return null;
    }

    public Entity modelToEntity(Object model) {
        return null;
    }
}