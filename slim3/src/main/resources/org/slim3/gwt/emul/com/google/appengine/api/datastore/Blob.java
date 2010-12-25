package com.google.appengine.api.datastore;

import java.io.Serializable;
import java.util.Arrays;

public final class Blob implements Serializable {

	private static final long serialVersionUID = -343352586L;

	private byte bytes[];

	private Blob() {
	}

	public Blob(byte bytes[]) {
		this.bytes = bytes;
	}

	@Override
	public boolean equals(Object object) {
		if (object instanceof Blob) {
			Blob key = (Blob) object;
			return Arrays.equals(bytes, key.bytes);
		} else {
			return false;
		}
	}

	public byte[] getBytes() {
		return bytes;
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(bytes);
	}
	@Override
	public String toString() {
		return (new StringBuilder()).append("<Blob: ").append(bytes.length)
				.append(" bytes>").toString();
	}
}