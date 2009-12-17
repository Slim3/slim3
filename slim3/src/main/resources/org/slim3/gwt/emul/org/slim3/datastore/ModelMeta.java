package org.slim3.datastore;

import java.util.List;

import com.google.appengine.api.datastore.Entity;

public abstract class ModelMeta<M> {

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