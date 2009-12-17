package org.slim3.datastore;

public class InverseModelRef<M> extends AbstractModelRef<M> {

    private static final long serialVersionUID = 1L;

    protected String mappedPropertyName;

    protected Object owner;

    protected M model;

    protected InverseModelRef() {
    }

    public M getModel() {
        return model;
    }

    public M refresh() {
        return null;
    }

    public void clear() {
        model = null;
    }
}