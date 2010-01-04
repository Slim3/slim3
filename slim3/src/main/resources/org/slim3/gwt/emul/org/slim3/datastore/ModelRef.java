package org.slim3.datastore;

import org.slim3.datastore.Datastore;

import com.google.appengine.api.datastore.Key;

public class ModelRef<M> extends AbstractModelRef<M> {

    private static final long serialVersionUID = 1L;

    protected M model;

    protected Key key;

    protected ModelRef() {
    }

    public ModelRef(Class<M> modelClass) {
        super(modelClass);
    }

    public M getModel() {
        return model;
    }

    public void setModel(M model) {
        if (model == null) {
            this.model = null;
            this.key = null;
        } else {
            this.model = model;
        }
    }
    
    public Key getKey() {
        return key;
    }
    
    public void setKey(Key key) {
        throw new UnsupportedOperationException("This method is unsupported on GWT.");
    }

    public M refresh() {
        throw new UnsupportedOperationException("This method is unsupported on GWT.");
    }
    
    public void clear() {
        model = null;
        key = null;
    }
}