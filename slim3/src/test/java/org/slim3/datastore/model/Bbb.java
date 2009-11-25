package org.slim3.datastore.model;

import java.io.Serializable;

import org.slim3.datastore.Model;

/**
 * @author higa
 * 
 */
@Model
public class Bbb extends Aaa implements Serializable {

    private static final long serialVersionUID = 1L;

    private HogeRef hogeRef = new HogeRef();

    /**
     * @return the hogeRef
     */
    public HogeRef getHogeRef() {
        return hogeRef;
    }

    /**
     * @param hogeRef
     *            the hogeRef to set
     */
    public void setHogeRef(HogeRef hogeRef) {
        this.hogeRef = hogeRef;
    }
}
