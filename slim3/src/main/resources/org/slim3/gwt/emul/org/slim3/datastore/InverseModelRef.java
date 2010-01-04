package org.slim3.datastore;

public class InverseModelRef<M, O> extends AbstractInverseModelRef<M, O> {

    private static final long serialVersionUID = 1L;

    protected M model;

    protected InverseModelRef() {
    }

    public InverseModelRef(Class<M> modelClass, String mappedPropertyName,
            O owner) {
        super(modelClass, mappedPropertyName, owner);
    }
 
    public M getModel() {
        return model;
    }

    public M refresh() {
        throw new UnsupportedOperationException("This method is unsupported on GWT.");
    }
    
    public void clear() {
        model = null;
    }
}