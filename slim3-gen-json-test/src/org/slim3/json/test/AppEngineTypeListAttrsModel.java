package org.slim3.json.test;

import java.util.List;

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
public class AppEngineTypeListAttrsModel {
    public Key getKey() {
        return key;
    }

    public void setKey(Key key) {
        this.key = key;
    }

    public List<Key> getKeyListAttr() {
        return keyListAttr;
    }

    public void setKeyListAttr(List<Key> keyListAttr) {
        this.keyListAttr = keyListAttr;
    }

    public List<BlobKey> getBlobKeyListAttr() {
        return blobKeyListAttr;
    }

    public void setBlobKeyListAttr(List<BlobKey> blobKeyListAttr) {
        this.blobKeyListAttr = blobKeyListAttr;
    }

    public List<ShortBlob> getShortBlobListAttr() {
        return shortBlobListAttr;
    }

    public void setShortBlobListAttr(List<ShortBlob> shortBlobListAttr) {
        this.shortBlobListAttr = shortBlobListAttr;
    }

    public List<Blob> getBlobListAttr() {
        return blobListAttr;
    }

    public void setBlobListAttr(List<Blob> blobListAttr) {
        this.blobListAttr = blobListAttr;
    }

    public List<Text> getTextListAttr() {
        return textListAttr;
    }

    public void setTextListAttr(List<Text> textListAttr) {
        this.textListAttr = textListAttr;
    }

    public List<Category> getCategoryListAttr() {
        return categoryListAttr;
    }

    public void setCategoryListAttr(List<Category> categoryListAttr) {
        this.categoryListAttr = categoryListAttr;
    }

    public List<GeoPt> getGeoPtListAttr() {
        return geoPtListAttr;
    }

    public void setGeoPtListAttr(List<GeoPt> geoPtListAttr) {
        this.geoPtListAttr = geoPtListAttr;
    }

    public List<IMHandle> getImHandleListAttr() {
        return imHandleListAttr;
    }

    public void setImHandleListAttr(List<IMHandle> imHandleListAttr) {
        this.imHandleListAttr = imHandleListAttr;
    }

    public List<Link> getLinkListAttr() {
        return linkListAttr;
    }

    public void setLinkListAttr(List<Link> linkListAttr) {
        this.linkListAttr = linkListAttr;
    }

    public List<PhoneNumber> getPhoneNumberListAttr() {
        return phoneNumberListAttr;
    }

    public void setPhoneNumberListAttr(List<PhoneNumber> phoneNumberListAttr) {
        this.phoneNumberListAttr = phoneNumberListAttr;
    }

    public List<PostalAddress> getPostalAddressListAttr() {
        return postalAddressListAttr;
    }

    public void setPostalAddressListAttr(
            List<PostalAddress> postalAddressListAttr) {
        this.postalAddressListAttr = postalAddressListAttr;
    }

    public List<Rating> getRatingListAttr() {
        return ratingListAttr;
    }

    public void setRatingListAttr(List<Rating> ratingListAttr) {
        this.ratingListAttr = ratingListAttr;
    }

    public List<Email> getEmailListAttr() {
        return emailListAttr;
    }

    public void setEmailListAttr(List<Email> emailListAttr) {
        this.emailListAttr = emailListAttr;
    }

    public List<User> getUserListAttr() {
        return userListAttr;
    }

    public void setUserListAttr(List<User> userListAttr) {
        this.userListAttr = userListAttr;
    }

    @Attribute(primaryKey = true)
    private Key key;
    private List<Key> keyListAttr;
    private List<BlobKey> blobKeyListAttr;
    private List<ShortBlob> shortBlobListAttr;
    private List<Blob> blobListAttr;
    private List<Text> textListAttr;
    private List<Category> categoryListAttr;
    private List<GeoPt> geoPtListAttr;
    private List<IMHandle> imHandleListAttr;
    private List<Link> linkListAttr;
    private List<PhoneNumber> phoneNumberListAttr;
    private List<PostalAddress> postalAddressListAttr;
    private List<Rating> ratingListAttr;
    private List<Email> emailListAttr;
    private List<User> userListAttr;
}
