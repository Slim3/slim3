package org.slim3.datastore;

public abstract class AbstractInverseModelRef<M, O> extends AbstractModelRef<M> {

    private static final long serialVersionUID = 1L;

    protected String mappedPropertyName;

    protected O owner;

    protected AbstractInverseModelRef() {
    }

    public AbstractInverseModelRef(Class<M> modelClass,
            CharSequence mappedPropertyName, O owner) throws NullPointerException {
        super(modelClass);
        if (mappedPropertyName == null) {
            throw new NullPointerException(
                "The mappedPropertyName must not be null.");
        }
        if (owner == null) {
            throw new NullPointerException("The owner must not be null.");
        }
        this.mappedPropertyName = mappedPropertyName.toString();
        this.owner = owner;
    }
}