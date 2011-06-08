package com.google.appengine.api.users;

import java.io.Serializable;

public final class User implements Serializable {


    static final long serialVersionUID = -144411264L;
    private String email;
    private String authDomain;
    private String userId;
    private String federatedIdentity;
    
    private User() {
    }

    public User(String email, String authDomain) {
        this(email, authDomain, null);
    }

    public User(String email, String authDomain, String userId) {
        this(email, authDomain, null, null);
    }

    public User(String email, String authDomain, String userId,
            String federatedIdentity) {
        if (authDomain == null)
            this.authDomain = "";
        else
            this.authDomain = authDomain;
        if (userId == null)
            this.userId = "";
        else
            this.userId = userId;
        this.email = email;
        this.federatedIdentity = federatedIdentity;
    }

    public String getNickname() {
        int indexOfDomain =
            email.indexOf((new StringBuilder())
                .append("@")
                .append(authDomain)
                .toString());
        if (indexOfDomain == -1)
            return email;
        else
            return email.substring(0, indexOfDomain);
    }

    public String getAuthDomain() {
        return authDomain;
    }

    public String getEmail() {
        return email;
    }

    public String getUserId() {
        return userId;
    }

    public String getFederatedIdentity() {
        return federatedIdentity;
    }

    public String toString() {
        return email;
    }

    public boolean equals(Object object) {
        if (!(object instanceof User))
            return false;
        User user = (User) object;
        if (federatedIdentity != null && !federatedIdentity.equals(""))
            return user.federatedIdentity.equals(federatedIdentity);
        else
            return user.email.equals(email)
                && user.authDomain.equals(authDomain);
    }

    public int hashCode() {
        return 17 * email.hashCode() + authDomain.hashCode();
    }

    public int compareTo(User user) {
        return email.compareTo(user.email);
    }
}
