package org.slim3.datastore;

import java.io.Serializable;

public class AbstractModelRef<M> implements Serializable {

    protected AbstractModelRef() {
    }
    
    public AbstractModelRef(Class<M> modelClass) throws NullPointerException {
    }

    public Class<M> getModelClass() {
        return null;
    }

    public ModelMeta<M> getModelMeta() {
        return null;
    }
}