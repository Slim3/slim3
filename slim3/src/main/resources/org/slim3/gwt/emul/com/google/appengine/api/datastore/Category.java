package com.google.appengine.api.datastore;

import java.io.Serializable;

public final class Category implements Serializable {

    public static final long serialVersionUID = -1510792739L;
    private String category;
    
    public Category(String category) {
        if (category == null) {
            throw new NullPointerException("category must not be null");
        } else {
            this.category = category;
            return;
        }
    }

    private Category() {
        category = null;
    }

    public String getCategory() {
        return category;
    }

    public int compareTo(Category o) {
        return category.compareTo(o.category);
    }

    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        return category.equals(((Category) o).category);
    }

    public int hashCode() {
        return category.hashCode();
    }
}