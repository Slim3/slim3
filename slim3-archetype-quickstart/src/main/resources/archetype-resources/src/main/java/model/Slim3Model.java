package ${package}.model;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Model;

import com.google.appengine.api.datastore.Key;

@Model
public class Slim3Model {

	@Attribute(primaryKey=true)
	private Key key;
	
	private String prop1;

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
	 * @return the prop1
	 */
	public String getProp1() {
		return prop1;
	}

	/**
	 * @param prop1 the prop1 to set
	 */
	public void setProp1(String prop1) {
		this.prop1 = prop1;
	}
}
