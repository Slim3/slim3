package org.slim3.datastore;

import java.io.Serializable;

public class AbstractModelRef<M> implements Serializable {

    private static final long serialVersionUID = 1L;
    
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