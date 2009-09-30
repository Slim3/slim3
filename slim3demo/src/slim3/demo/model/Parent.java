package slim3.demo.model;

import java.io.Serializable;

import com.google.appengine.api.datastore.Key;

import org.slim3.datastore.Model;
import org.slim3.datastore.PrimaryKey;
import org.slim3.datastore.Version;

@Model
public class Parent implements Serializable {

    private static final long serialVersionUID = 1L;

    @PrimaryKey
    private Key key;

    @Version
    private Long version;

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
     * Returns the version.
     *
     * @return the version
     */
    public Long getVersion() {
        return version;
    }

    /**
     * Sets the version.
     *
     * @param version
     *            the version
     */
    public void setVersion(Long version) {
        this.version = version;
    }
}
