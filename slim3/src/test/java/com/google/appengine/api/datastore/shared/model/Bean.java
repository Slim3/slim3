package com.google.appengine.api.datastore.shared.model;

import com.google.appengine.api.datastore.Key;
import com.google.gwt.user.client.rpc.IsSerializable;

import org.slim3.datastore.Attribute;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * @author galdolber
 *
 */
public class Bean implements IsSerializable {

	@Attribute(primaryKey = true)
	private Key key;

	private String s;
	
	private Double d;
	
	private Float f;
	
	private Boolean b;
	
	private Date da;
	
	private ArrayList<String> list;
	
	@Attribute(lob=true)
	private HashMap<String, String> map;
	
	/**
     * @return the key
     */
    public Key getKey() {
        return key;
    }

    /**
     * @param key the key to set
     */
    public void setKey(Key key) {
        this.key = key;
    }

    /**
     * @return the s
     */
    public String getS() {
        return s;
    }

    /**
     * @param s the s to set
     */
    public void setS(String s) {
        this.s = s;
    }

    /**
     * @return the d
     */
    public Double getD() {
        return d;
    }

    /**
     * @param d the d to set
     */
    public void setD(Double d) {
        this.d = d;
    }

    /**
     * @return the f
     */
    public Float getF() {
        return f;
    }

    /**
     * @param f the f to set
     */
    public void setF(Float f) {
        this.f = f;
    }

    /**
     * @return the b
     */
    public Boolean getB() {
        return b;
    }

    /**
     * @param b the b to set
     */
    public void setB(Boolean b) {
        this.b = b;
    }

    /**
     * @return the da
     */
    public Date getDa() {
        return da;
    }

    /**
     * @param da the da to set
     */
    public void setDa(Date da) {
        this.da = da;
    }

    /**
     * @return the list
     */
    public ArrayList<String> getList() {
        return list;
    }

    /**
     * @param list the list to set
     */
    public void setList(ArrayList<String> list) {
        this.list = list;
    }

    /**
     * @return the map
     */
    public HashMap<String, String> getMap() {
        return map;
    }

    /**
     * @param map the map to set
     */
    public void setMap(HashMap<String, String> map) {
        this.map = map;
    }
	
    /**
     * 
     */
    public Bean() {
    }
}
