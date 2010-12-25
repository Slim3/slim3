package com.google.appengine.api.datastore;

import java.io.Serializable;

public final class Email implements Serializable {

    public static final long serialVersionUID = -546419898L;
    
    private String email;
    
    public Email(String email) {
        if (email == null) {
            throw new NullPointerException("email must not be null");
        } else {
            this.email = email;
            return;
        }
    }

    private Email() {
        email = null;
    }

    public String getEmail() {
        return email;
    }

    public int compareTo(Email e) {
        return email.compareTo(e.email);
    }

    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Email email1 = (Email) o;
        return email.equals(email1.email);
    }

    public int hashCode() {
        return email.hashCode();
    }
}