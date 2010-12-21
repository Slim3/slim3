package org.slim3.json.test;

import java.util.List;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Model;

import com.google.appengine.api.datastore.Key;

@Model
public class WrapperListAttrsModel {
	public Key getKey() {
		return key;
	}
	public void setKey(Key key) {
		this.key = key;
	}
	public List<Boolean> getBooleanListAttr() {
		return booleanListAttr;
	}
	public void setBooleanListAttr(List<Boolean> booleanListAttr) {
		this.booleanListAttr = booleanListAttr;
	}
	public List<Short> getShortListAttr() {
		return shortListAttr;
	}
	public void setShortListAttr(List<Short> shortListAttr) {
		this.shortListAttr = shortListAttr;
	}
	public List<Integer> getIntegerListAttr() {
		return integerListAttr;
	}
	public void setIntegerListAttr(List<Integer> integerListAttr) {
		this.integerListAttr = integerListAttr;
	}
	public List<Long> getLongListAttr() {
		return longListAttr;
	}
	public void setLongListAttr(List<Long> longListAttr) {
		this.longListAttr = longListAttr;
	}
	public List<Float> getFloatListAttr() {
		return floatListAttr;
	}
	public void setFloatListAttr(List<Float> floatListAttr) {
		this.floatListAttr = floatListAttr;
	}
	public List<Double> getDoubleListAttr() {
		return doubleListAttr;
	}
	public void setDoubleListAttr(List<Double> doubleListAttr) {
		this.doubleListAttr = doubleListAttr;
	}

	@Attribute(primaryKey=true)
	private Key key;
	private List<Boolean> booleanListAttr;
	private List<Short> shortListAttr;
	private List<Integer> integerListAttr;
	private List<Long> longListAttr;
	private List<Float> floatListAttr;
	private List<Double> doubleListAttr;
}
