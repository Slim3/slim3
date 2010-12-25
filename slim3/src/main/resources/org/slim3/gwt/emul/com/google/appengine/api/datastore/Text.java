package com.google.appengine.api.datastore;

import java.io.Serializable;

public final class Text implements Serializable {

	public static final long serialVersionUID = -678867336L;

	private String value;

	private Text() {
	}

	public Text(String value) {
		this.value = value;
	}

	@Override
	public boolean equals(Object object) {
		if (object instanceof Text) {
			Text key = (Text) object;
			return value.equals(key.value);
		} else {
			return false;
		}
	}

	public String getValue() {
		return value;
	}

	@Override
	public int hashCode() {
		return value.hashCode();
	}

	@Override
	public String toString() {
		String text = value;
		if (text.length() > 70)
			text = (new StringBuilder()).append(text.substring(0, 70))
					.append("...").toString();
		return (new StringBuilder()).append("<Text: ").append(text).append(">")
				.toString();
	}
}