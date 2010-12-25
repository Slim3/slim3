package com.google.appengine.api.datastore;

import java.io.Serializable;
import java.util.Arrays;

public final class ShortBlob implements Serializable {

    public static final long serialVersionUID = -234210296L;
    
    private byte bytes[];
    
    protected ShortBlob() {
        bytes = null;
    }

    public ShortBlob(byte bytes[]) {
        this.bytes = new byte[bytes.length];
        System.arraycopy(bytes, 0, this.bytes, 0, bytes.length);
    }

    public byte[] getBytes() {
        return bytes;
    }

    public int hashCode() {
        return Arrays.hashCode(bytes);
    }

    public boolean equals(Object object) {
        if (object instanceof ShortBlob) {
            ShortBlob other = (ShortBlob) object;
            return Arrays.equals(bytes, other.bytes);
        } else {
            return false;
        }
    }

    public String toString() {
        return (new StringBuilder())
            .append("<ShortBlob: ")
            .append(bytes.length)
            .append(" bytes>")
            .toString();
    }

    public int compareTo(ShortBlob other) {
        throw new RuntimeException("compareTo is not supported on gwt");
    }
}