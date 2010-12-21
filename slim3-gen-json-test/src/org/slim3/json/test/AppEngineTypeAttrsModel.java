package org.slim3.json.test;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Model;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.Category;
import com.google.appengine.api.datastore.Email;
import com.google.appengine.api.datastore.GeoPt;
import com.google.appengine.api.datastore.IMHandle;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Link;
import com.google.appengine.api.datastore.PhoneNumber;
import com.google.appengine.api.datastore.PostalAddress;
import com.google.appengine.api.datastore.Rating;
import com.google.appengine.api.datastore.ShortBlob;
import com.google.appengine.api.datastore.Text;
import com.google.appengine.api.users.User;

@Model
public class AppEngineTypeAttrsModel {
	public Key getKey() {
		return key;
	}
	public void setKey(Key key) {
		this.key = key;
	}
	public BlobKey getBlobKeyAttr() {
		return blobKeyAttr;
	}
	public void setBlobKeyAttr(BlobKey blobKeyAttr) {
		this.blobKeyAttr = blobKeyAttr;
	}
	public Email getEmailAttr() {
		return emailAttr;
	}
	public void setEmailAttr(Email emailAttr) {
		this.emailAttr = emailAttr;
	}
	public ShortBlob getShortBlobAttr() {
		return shortBlobAttr;
	}
	public void setShortBlobAttr(ShortBlob shortBlobAttr) {
		this.shortBlobAttr = shortBlobAttr;
	}
	public Blob getBlobAttr() {
		return blobAttr;
	}
	public void setBlobAttr(Blob blobAttr) {
		this.blobAttr = blobAttr;
	}
	public Text getTextAttr() {
		return textAttr;
	}
	public void setTextAttr(Text textAttr) {
		this.textAttr = textAttr;
	}
	public Text getEncryptedTextAttr() {
		return encryptedTextAttr;
	}
	public void setEncryptedTextAttr(Text encryptedTextAttr) {
		this.encryptedTextAttr = encryptedTextAttr;
	}
	public Category getCategoryAttr() {
		return categoryAttr;
	}
	public void setCategoryAttr(Category categoryAttr) {
		this.categoryAttr = categoryAttr;
	}
	public GeoPt getGeoPtAttr() {
		return geoPtAttr;
	}
	public void setGeoPtAttr(GeoPt geoPtAttr) {
		this.geoPtAttr = geoPtAttr;
	}
	public IMHandle getImHandleAttr1() {
		return imHandleAttr1;
	}
	public void setImHandleAttr1(IMHandle imHandleAttr1) {
		this.imHandleAttr1 = imHandleAttr1;
	}
	public IMHandle getImHandleAttr2() {
		return imHandleAttr2;
	}
	public void setImHandleAttr2(IMHandle imHandleAttr2) {
		this.imHandleAttr2 = imHandleAttr2;
	}
	public Link getLinkAttr() {
		return linkAttr;
	}
	public void setLinkAttr(Link linkAttr) {
		this.linkAttr = linkAttr;
	}
	public PhoneNumber getPhoneNumberAttr() {
		return phoneNumberAttr;
	}
	public void setPhoneNumberAttr(PhoneNumber phoneNumberAttr) {
		this.phoneNumberAttr = phoneNumberAttr;
	}
	public PostalAddress getPostalAddressAttr() {
		return postalAddressAttr;
	}
	public void setPostalAddressAttr(PostalAddress postalAddressAttr) {
		this.postalAddressAttr = postalAddressAttr;
	}
	public Rating getRatingAttr() {
		return ratingAttr;
	}
	public void setRatingAttr(Rating ratingAttr) {
		this.ratingAttr = ratingAttr;
	}
	public User getUser1() {
		return user1;
	}
	public void setUser1(User user1) {
		this.user1 = user1;
	}
	public User getUser2() {
		return user2;
	}
	public void setUser2(User user2) {
		this.user2 = user2;
	}
	public User getUser3() {
		return user3;
	}
	public void setUser3(User user3) {
		this.user3 = user3;
	}

	@Attribute(primaryKey=true)
	private Key key;
	private BlobKey blobKeyAttr;
	private Email emailAttr;
	private ShortBlob shortBlobAttr;
	private Blob blobAttr;
	private Text textAttr;
	@Attribute(cipher=true)
	private Text encryptedTextAttr;
	private Category categoryAttr;
	private GeoPt geoPtAttr;
	private IMHandle imHandleAttr1;
	private IMHandle imHandleAttr2;
	private Link linkAttr;
	private PhoneNumber phoneNumberAttr;
	private PostalAddress postalAddressAttr;
	private Rating ratingAttr;
	private User user1;
	private User user2;
	private User user3;
}
