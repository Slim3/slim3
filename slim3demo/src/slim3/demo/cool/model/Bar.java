package slim3.demo.cool.model;

import java.io.Serializable;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Model;

import com.google.appengine.api.datastore.Key;

@Model
public class Bar implements Serializable {

    private static final long serialVersionUID = 1L;

    @Attribute(primaryKey = true)
    private Key key;

    private String sortValue;

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

    /**
     * @return the sortValue
     */
    public String getSortValue() {
        return sortValue;
    }

    /**
     * @param sortValue
     *            the sortValue to set
     */
    public void setSortValue(String sortValue) {
        this.sortValue = sortValue;
    }
}