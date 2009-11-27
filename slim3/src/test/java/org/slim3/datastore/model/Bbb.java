package org.slim3.datastore.model;

import java.io.Serializable;

import org.slim3.datastore.Model;
import org.slim3.datastore.ModelRef;

/**
 * @author higa
 * 
 */
@Model
public class Bbb extends Aaa implements Serializable {

    private static final long serialVersionUID = 1L;

    private ModelRef<Hoge> hogeRef = new ModelRef<Hoge>(Hoge.class);

    /**
     * @return the hogeRef
     */
    public ModelRef<Hoge> getHogeRef() {
        return hogeRef;
    }

    /**
     * @param hogeRef
     *            the hogeRef to set
     */
    public void setHogeRef(ModelRef<Hoge> hogeRef) {
        this.hogeRef = hogeRef;
    }
}
