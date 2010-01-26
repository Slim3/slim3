package org.slim3.datastore;

import java.io.Serializable;

import com.google.appengine.api.datastore.Query.SortDirection;

public class Sort implements Serializable {

    private static final long serialVersionUID = 1L;

    protected String propertyName;

    protected SortDirection direction;

    protected Sort() {
    }

    public Sort(CharSequence propertyName) throws NullPointerException {
        this(propertyName, SortDirection.ASCENDING);
    }

    public Sort(CharSequence propertyName, SortDirection direction)
            throws NullPointerException {
        if (propertyName == null) {
            throw new NullPointerException(
                "The propertyName parameter must not be null.");
        }
        if (direction == null) {
            throw new NullPointerException(
                "The direction parameter must not be null.");
        }
        this.propertyName = propertyName.toString();
        this.direction = direction;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public SortDirection getDirection() {
        return direction;
    }
}