package org.slim3.json.test;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Model;
import org.slim3.datastore.json.Default;
import org.slim3.datastore.json.Json;
import org.slim3.datastore.json.JsonCoder;
import org.slim3.datastore.json.JsonReader;
import org.slim3.datastore.json.JsonWriter;

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
	public String getHelloStringAttr() {
		return helloStringAttr;
	}
	public void setHelloStringAttr(String helloStringAttr) {
		this.helloStringAttr = helloStringAttr;
	}
	public String getIgnoredStringAttr() {
		return ignoredStringAttr;
	}
	public void setIgnoredStringAttr(String ignoredStringAttr) {
		this.ignoredStringAttr = ignoredStringAttr;
	}
	public String getAliasedStringAttr() {
		return aliasedStringAttr;
	}
	public void setAliasedStringAttr(String aliasedStringAttr) {
		this.aliasedStringAttr = aliasedStringAttr;
	}
	
	static class HelloCoder extends Default implements JsonCoder{
		public HelloCoder() {
		}
		@Override
		public String decode(JsonReader reader, String defaultValue) {
			return "hello";
		}
		@Override
		public void encode(JsonWriter writer, String value) {
			writer.writeString("hello");
		}
	}

	static interface Fuga extends JsonCoder{}
	@Attribute(primaryKey=true)
	private Key key;
	private String stringAttr;
	@Attribute(json=@Json(coder=HelloCoder.class))
	private String helloStringAttr;
	@Attribute(json=@Json(ignore=true))
	private String ignoredStringAttr;
	@Attribute(json=@Json(alias="alias"))
	private String aliasedStringAttr;
}
