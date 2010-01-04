package org.slim3.datastore;

import java.io.Serializable;

import org.slim3.datastore.Datastore;
import org.slim3.datastore.ModelMeta;
import org.slim3.util.ClassUtil;

public class AbstractModelRef<M> implements Serializable {

    private static final long serialVersionUID = 1L;
    
    protected String modelClassName;
    
    protected AbstractModelRef() {
    }
    
    public AbstractModelRef(Class<M> modelClass) throws NullPointerException {
        modelClassName = modelClass.getName();
    }
    
    public Class<M> getModelClass() {
        throw new UnsupportedOperationException("This method is unsupported on GWT.");
    }
    
    public ModelMeta<M> getModelMeta() {
        throw new UnsupportedOperationException("This method is unsupported on GWT.");
    }
}