package com.google.appengine.api.datastore;

import java.io.Serializable;

public final class PostalAddress implements Serializable {

    public static final long serialVersionUID = 543579577L;
    
    private String address;
    
    public PostalAddress(String address) {
        if (address == null) {
            throw new NullPointerException("address must not be null");
        } else {
            this.address = address;
            return;
        }
    }

    private PostalAddress() {
        address = null;
    }

    public String getAddress() {
        return address;
    }

    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        PostalAddress that = (PostalAddress) o;
        return address.equals(that.address);
    }

    public int hashCode() {
        return address.hashCode();
    }

    public int compareTo(PostalAddress o) {
        return address.compareTo(o.address);
    }
}