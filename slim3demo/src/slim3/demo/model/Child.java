package slim3.demo.model;

import java.io.Serializable;

import org.slim3.datastore.Model;
import org.slim3.datastore.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@Model
public class Child implements Serializable {

    private static final long serialVersionUID = 1L;

    @PrimaryKey
    private Key key;

    /**
     * Returns the key.
     * 
     * @return the key
     */
    public Key getKey() {
        return key;
    }

    /**
     * Sets the key.
     * 
     * @param key
     *            the key
     */
    public void setKey(Key key) {
        this.key = key;
    }
}
