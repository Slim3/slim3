package org.slim3.json.test;

import java.util.Date;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Model;

import com.google.appengine.api.datastore.Key;

@Model
public class OtherJavaTypeAttrsModel {
	public Key getKey() {
		return key;
	}
	public void setKey(Key key) {
		this.key = key;
	}
	public String getStringAttr() {
		return stringAttr;
	}
	public void setStringAttr(String stringProp) {
		this.stringAttr = stringProp;
	}
	public String getEncryptedStringAttr() {
		return encryptedStringAttr;
	}
	public void setEncryptedStringAttr(String encryptedStringAttr) {
		this.encryptedStringAttr = encryptedStringAttr;
	}
	public Date getDateAttr() {
		return dateAttr;
	}
	public void setDateAttr(Date dateAttr) {
		this.dateAttr = dateAttr;
	}
	public WeekDay getEnumAttr() {
		return enumAttr;
	}
	public void setEnumAttr(WeekDay enumAttr) {
		this.enumAttr = enumAttr;
	}

	public enum WeekDay{
		Mon, Tue, Wed, Thi, Fri, Sat, Sun
	}

	@Attribute(primaryKey=true)
	private Key key;
	private String stringAttr;
	@Attribute(cipher=true)
	private String encryptedStringAttr;
	private Date dateAttr;
	private WeekDay enumAttr;
}
