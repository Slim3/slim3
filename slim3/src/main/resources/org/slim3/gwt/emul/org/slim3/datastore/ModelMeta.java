package org.slim3.datastore;

public abstract class ModelMeta<M> {

    protected String kind;

    protected Class<M> modelClass;

    protected String simpleClassName;

    protected ModelMeta() {
    }

    public String getKind() {
        return kind;
    }

    public Class<M> getModelClass() {
        return modelClass;
    }

    public String getSimpleClassName() {
        return simpleClassName;
    }
}