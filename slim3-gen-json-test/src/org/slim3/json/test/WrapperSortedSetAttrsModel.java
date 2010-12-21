package org.slim3.json.test;

import java.util.SortedSet;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Model;

import com.google.appengine.api.datastore.Key;

@Model
public class WrapperSortedSetAttrsModel {
	public Key getKey() {
		return key;
	}
	public void setKey(Key key) {
		this.key = key;
	}
	public SortedSet<Boolean> getBooleanSortedSetAttr() {
		return booleanSortedSetAttr;
	}
	public void setBooleanSortedSetAttr(SortedSet<Boolean> booleanSortedSetAttr) {
		this.booleanSortedSetAttr = booleanSortedSetAttr;
	}
	public SortedSet<Short> getShortSortedSetAttr() {
		return shortSortedSetAttr;
	}
	public void setShortSortedSetAttr(SortedSet<Short> shortSortedSetAttr) {
		this.shortSortedSetAttr = shortSortedSetAttr;
	}
	public SortedSet<Integer> getIntegerSortedSetAttr() {
		return integerSortedSetAttr;
	}
	public void setIntegerSortedSetAttr(SortedSet<Integer> integerSortedSetAttr) {
		this.integerSortedSetAttr = integerSortedSetAttr;
	}
	public SortedSet<Long> getLongSortedSetAttr() {
		return longSortedSetAttr;
	}
	public void setLongSortedSetAttr(SortedSet<Long> longSortedSetAttr) {
		this.longSortedSetAttr = longSortedSetAttr;
	}
	public SortedSet<Float> getFloatSortedSetAttr() {
		return floatSortedSetAttr;
	}
	public void setFloatSortedSetAttr(SortedSet<Float> floatSortedSetAttr) {
		this.floatSortedSetAttr = floatSortedSetAttr;
	}
	public SortedSet<Double> getDoubleSortedSetAttr() {
		return doubleSortedSetAttr;
	}
	public void setDoubleSortedSetAttr(SortedSet<Double> doubleSortedSetAttr) {
		this.doubleSortedSetAttr = doubleSortedSetAttr;
	}

	@Attribute(primaryKey=true)
	private Key key;
	private SortedSet<Boolean> booleanSortedSetAttr;
	private SortedSet<Short> shortSortedSetAttr;
	private SortedSet<Integer> integerSortedSetAttr;
	private SortedSet<Long> longSortedSetAttr;
	private SortedSet<Float> floatSortedSetAttr;
	private SortedSet<Double> doubleSortedSetAttr;
}
