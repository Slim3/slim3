package com.google.appengine.api.datastore;

import java.io.Serializable;

public final class Rating implements Serializable {

    public static final long serialVersionUID = 1483752963L;
    public static final int MIN_VALUE = 0;
    public static final int MAX_VALUE = 100;
    private int rating;

    public Rating(int rating) {
        if (rating < 0 || rating > 100) {
            throw new IllegalArgumentException(
                "rating must be no smaller than 0 and no greater than 100 (received "
                    + rating
                    + ")");
        } else {
            this.rating = rating;
            return;
        }
    }

    private Rating() {
        this(0);
    }

    public int getRating() {
        return rating;
    }

    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Rating rating1 = (Rating) o;
        return rating == rating1.rating;
    }

    public int hashCode() {
        return rating;
    }

    public int compareTo(Rating o) {
        return Integer.valueOf(rating).compareTo(Integer.valueOf(o.rating));
    }
}