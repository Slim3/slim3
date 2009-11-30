package org.slim3.datastore;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Transaction;

public class ModelRef<M> extends AbstractModelRef<M> {

    private static final long serialVersionUID = 1L;

    protected M model;

    protected Key key;

    protected ModelRef() {
    }

    public ModelRef(Class<M> modelClass) throws NullPointerException {
        super(modelClass);
    }

    public M getModel() {
        return model;
    }

    public M getModel(Transaction tx) {
        return model;
    }

    public void setModel(M model) throws IllegalArgumentException {
        this.model = model;
    }

    public Key getKey() {
        return key;
    }

    public void setKey(Key key) throws IllegalArgumentException {
        this.key = key;
    }
    public M refresh() {
        return null;
    }

    public M refresh(Transaction tx) {
        return null;
    }
}