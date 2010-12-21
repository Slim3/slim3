package org.slim3.json.test;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Model;
import org.slim3.datastore.json.Default;
import org.slim3.datastore.json.Json;
import org.slim3.datastore.json.JsonCoder;
import org.slim3.datastore.json.JsonReader;

import com.google.appengine.api.datastore.Key;

@Model
public class AnnotatedModel {
	public Key getKey() {
		return key;
	}
	public void setKey(Key key) {
		this.key = key;
	}
	public String getStringAttr() {
		return stringAttr;
	}
	public void setStringAttr(String stringAttr) {
		this.stringAttr = stringAttr;
	}

	static class Hoge extends Default implements JsonCoder{
		@Override
		public String decode(JsonReader reader, String defaultValue) {
			return null;
		}
		public Hoge(int i){}
	}
	static interface Fuga extends JsonCoder{}
	@Attribute(primaryKey=true)
	private Key key;
	@Attribute(json=@Json)
	private String stringAttr;
}
