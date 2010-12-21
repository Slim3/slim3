package org.slim3.json.test;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Model;

import com.google.appengine.api.datastore.Key;

@Model
public class WrapperAttrsModel {
	public Key getKey() {
		return key;
	}
	public void setKey(Key key) {
		this.key = key;
	}
	public Boolean getBooleanAttr() {
		return booleanAttr;
	}
	public void setBooleanAttr(Boolean booleanAttr) {
		this.booleanAttr = booleanAttr;
	}
	public Short getShortAttr() {
		return shortAttr;
	}
	public void setShortAttr(Short shortAttr) {
		this.shortAttr = shortAttr;
	}
	public Integer getIntegerAttr() {
		return integerAttr;
	}
	public void setIntegerAttr(Integer integerAttr) {
		this.integerAttr = integerAttr;
	}
	public Long getLongAttr() {
		return longAttr;
	}
	public void setLongAttr(Long longAttr) {
		this.longAttr = longAttr;
	}
	public Float getFloatAttr() {
		return floatAttr;
	}
	public void setFloatAttr(Float floatAttr) {
		this.floatAttr = floatAttr;
	}
	public Double getDoubleAttr() {
		return doubleAttr;
	}
	public void setDoubleAttr(Double doubleAttr) {
		this.doubleAttr = doubleAttr;
	}

	@Attribute(primaryKey=true)
	private Key key;
	private Boolean booleanAttr;
	private Short shortAttr;
	private Integer integerAttr;
	private Long longAttr;
	private Float floatAttr;
	private Double doubleAttr;
}
