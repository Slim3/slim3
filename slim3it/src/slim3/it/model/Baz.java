package slim3.it.model;

import javax.jdo.annotations.EmbeddedOnly;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

@PersistenceCapable
@EmbeddedOnly
public class Baz {

    @Persistent
    private String aaa;

    /**
     * @return the aaa
     */
    public String getAaa() {
        return aaa;
    }

    /**
     * @param aaa
     *            the aaa to set
     */
    public void setAaa(String aaa) {
        this.aaa = aaa;
    }
}
