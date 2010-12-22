package org.slim3.json.test;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Model;

import com.google.appengine.api.datastore.Key;

@Model
public class PrimitiveArrayAttrsModel {
	public Key getKey() {
		return key;
	}
	public void setKey(Key key) {
		this.key = key;
	}
	public boolean[] getBooleans() {
		return booleans;
	}
	public void setBooleans(boolean[] booleans) {
		this.booleans = booleans;
	}
	public byte[] getBytes() {
		return bytes;
	}
	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}
	public char[] getChars() {
		return chars;
	}
	public void setChars(char[] chars) {
		this.chars = chars;
	}
	public short[] getShorts() {
		return shorts;
	}
	public void setShorts(short[] shorts) {
		this.shorts = shorts;
	}
	public int[] getInts() {
		return ints;
	}
	public void setInts(int[] ints) {
		this.ints = ints;
	}
	public long[] getLongs() {
		return longs;
	}
	public void setLongs(long[] longs) {
		this.longs = longs;
	}
	public float[] getFloats() {
		return floats;
	}
	public void setFloats(float[] floats) {
		this.floats = floats;
	}
	public double[] getDoubles() {
		return doubles;
	}
	public void setDoubles(double[] doubles) {
		this.doubles = doubles;
	}

	@Attribute(primaryKey=true)
	private Key key;
	@Attribute(lob=true)
	private boolean[] booleans;
	private byte[] bytes;
	@Attribute(lob=true)
	private char[] chars;
	@Attribute(lob=true)
	private short[] shorts;
	@Attribute(lob=true)
	private int[] ints;
	@Attribute(lob=true)
	private long[] longs;
	@Attribute(lob=true)
	private float[] floats;
	@Attribute(lob=true)
	private double[] doubles;
}
