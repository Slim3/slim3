package org.slim3.datastore;

import java.util.List;

public class InverseModelListRef<M, O> extends AbstractInverseModelRef<M, O> {

    private static final long serialVersionUID = 1L;

    protected Sort[] defaultSorts;

    protected List<M> modelList;

    protected InverseModelListRef() {
    }

    public InverseModelListRef(Class<M> modelClass, CharSequence mappedPropertyName,
            O owner, Sort... defaultSorts) {
        super(modelClass, mappedPropertyName, owner);
        this.defaultSorts = defaultSorts;
    }

    public List<M> getModelList() {
        return modelList;
    }

    public ModelListQuery query() {
        throw new UnsupportedOperationException("This method is unsupported on GWT.");
    }
    
    public void clear() {
        modelList = null;
    }
    
    public class ModelListQuery {
    }
}