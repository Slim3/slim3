package org.slim3.json.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.net.URL;

import net.arnx.jsonic.JSON;

import org.junit.Assert;
import org.junit.Test;
import org.slim3.datastore.Datastore;
import org.slim3.json.test.AppEngineTypeAttrsModel;
import org.slim3.json.test.meta.AppEngineTypeAttrsModelMeta;
import org.slim3.tester.TestEnvironment;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.Category;
import com.google.appengine.api.datastore.Email;
import com.google.appengine.api.datastore.GeoPt;
import com.google.appengine.api.datastore.IMHandle;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Link;
import com.google.appengine.api.datastore.PhoneNumber;
import com.google.appengine.api.datastore.PostalAddress;
import com.google.appengine.api.datastore.Rating;
import com.google.appengine.api.datastore.ShortBlob;
import com.google.appengine.api.datastore.Text;
import com.google.appengine.api.users.User;
import com.google.apphosting.api.ApiProxy;

public class AppEngineTypeAttrsModelTest {
	@Test
	public void modelToJson() throws Exception{
		Datastore.setGlobalCipherKey("0654813216578941");
		ApiProxy.setEnvironmentForCurrentThread(new TestEnvironment(){
			@Override
			public String getAppId() {
				return "slim3-gen";
			}
		});
		AppEngineTypeAttrsModel m = new AppEngineTypeAttrsModel();
		m.setKey(KeyFactory.createKey("test", 1000));
		m.setBlobKeyAttr(new BlobKey("Q3PqkweYlb4iWpp0BVw"));
		m.setCategoryAttr(new Category("partOfSpeech"));
		m.setEmailAttr(new Email("takawitter@test.com"));
		m.setBlobAttr(new Blob("hello".getBytes()));
		m.setGeoPtAttr(new GeoPt(10, 10));
		m.setImHandleAttr1(new IMHandle(IMHandle.Scheme.xmpp, "handle"));
		m.setImHandleAttr2(new IMHandle(new URL("http://aim.com"), "network"));
		m.setLinkAttr(new Link("link"));
		m.setPhoneNumberAttr(new PhoneNumber("000-0000-0000"));
		m.setPostalAddressAttr(new PostalAddress("address"));
		m.setRatingAttr(new Rating(70));
		m.setShortBlobAttr(new ShortBlob("hello".getBytes()));
		m.setTextAttr(new Text("hello"));
		m.setEncryptedTextAttr(new Text("hello"));
		m.setUser1(new User("user@test.com", "authDomain"));
		m.setUser2(new User("user@test.com", "authDomain", "userId"));
		m.setUser3(new User("user@test.com", "authDomain", "userId", "federatedId"));

		String json = AppEngineTypeAttrsModelMeta.get().modelToJson(m);
		System.out.println(json);
		JSON j = new JSON();
		j.setSuppressNull(true);
		System.out.println(j.format(m));

		assertEquals(
				"{" +
				"\"blobAttr\":\"aGVsbG8=\"" +
				",\"blobKeyAttr\":\"Q3PqkweYlb4iWpp0BVw\",\"categoryAttr\":\"partOfSpeech\"" +
				",\"emailAttr\":\"takawitter@test.com\"" +
				",\"encryptedTextAttr\":\"eeRXmeJQOo8HbwTHJ+R+WQ==\"" +
				",\"geoPtAttr\":{\"latitude\":10.0,\"longitude\":10.0}" +
				",\"imHandleAttr1\":{\"address\":\"handle\",\"protocol\":\"xmpp\"}" +
				",\"imHandleAttr2\":{\"address\":\"network\",\"protocol\":\"http://aim.com\"}" +
				",\"key\":\"aglzbGltMy1nZW5yCwsSBHRlc3QY6AcM\"" +
				",\"linkAttr\":\"link\",\"phoneNumberAttr\":\"000-0000-0000\"" +
				",\"postalAddressAttr\":\"address\",\"ratingAttr\":70" +
				",\"shortBlobAttr\":\"aGVsbG8=\"" +
				",\"textAttr\":\"hello\"" +
				",\"user1\":{\"authDomain\":\"authDomain\",\"email\":\"user@test.com\"}" +
				",\"user2\":{\"authDomain\":\"authDomain\",\"email\":\"user@test.com\",\"userId\":\"userId\"}" +
				",\"user3\":{\"authDomain\":\"authDomain\",\"email\":\"user@test.com\",\"federatedIdentity\":\"federatedId\",\"userId\":\"userId\"}" +
				"}"
				, json
				);
	}

	@Test
	public void modelToJson_null(){
		AppEngineTypeAttrsModel m = new AppEngineTypeAttrsModel();
		String json = AppEngineTypeAttrsModelMeta.get().modelToJson(m);
		assertEquals("{}", json);
	}

	@Test
	public void modelToJson_nullBlob(){
		AppEngineTypeAttrsModel m = new AppEngineTypeAttrsModel();
		m.setBlobAttr(new Blob(null));
		String json = AppEngineTypeAttrsModelMeta.get().modelToJson(m);
		assertEquals("{}", json);
	}

	@Test
	public void modelToJson_nullText(){
		AppEngineTypeAttrsModel m = new AppEngineTypeAttrsModel();
		m.setTextAttr(new Text(null));
		String json = AppEngineTypeAttrsModelMeta.get().modelToJson(m);
		assertEquals("{}", json);
	}

	@Test
	public void jsonToModel(){
		Datastore.setGlobalCipherKey("0654813216578941");
		AppEngineTypeAttrsModel m = AppEngineTypeAttrsModelMeta.get().jsonToModel(
				"{" +
				"\"blobAttr\":\"aGVsbG8=\"" +
				",\"blobKeyAttr\":\"Q3PqkweYlb4iWpp0BVw\",\"categoryAttr\":\"partOfSpeech\"" +
				",\"emailAttr\":\"takawitter@test.com\"" +
				",\"encryptedTextAttr\":\"eeRXmeJQOo8HbwTHJ+R+WQ==\"" +
				",\"geoPtAttr\":{\"latitude\":10.0,\"longitude\":10.0}" +
				",\"imHandleAttr1\":{\"address\":\"handle\",\"protocol\":\"xmpp\"}" +
				",\"imHandleAttr2\":{\"address\":\"network\",\"protocol\":\"http://aim.com\"}" +
				",\"key\":\"aglzbGltMy1nZW5yCwsSBHRlc3QY6AcM\"" +
				",\"linkAttr\":\"link\",\"phoneNumberAttr\":\"000-0000-0000\"" +
				",\"postalAddressAttr\":\"address\",\"ratingAttr\":70" +
				",\"shortBlobAttr\":\"aGVsbG8=\"" +
				",\"textAttr\":\"hello\"" +
				",\"user1\":{\"authDomain\":\"authDomain\",\"email\":\"user@test.com\"}" +
				",\"user2\":{\"authDomain\":\"authDomain\",\"email\":\"user@test.com\",\"userId\":\"userId\"}" +
				",\"user3\":{\"authDomain\":\"authDomain\",\"email\":\"user@test.com\",\"federatedIdentity\":\"federatedId\",\"userId\":\"userId\"}" +
				"}"
				);
		Assert.assertEquals("aglzbGltMy1nZW5yCwsSBHRlc3QY6AcM", KeyFactory.keyToString(m.getKey()));
		Assert.assertEquals("Q3PqkweYlb4iWpp0BVw", m.getBlobKeyAttr().getKeyString());
		Assert.assertEquals("partOfSpeech", m.getCategoryAttr().getCategory());
		Assert.assertEquals("takawitter@test.com", m.getEmailAttr().getEmail());
		Assert.assertArrayEquals("hello".getBytes(), m.getBlobAttr().getBytes());
		Assert.assertEquals(10, m.getGeoPtAttr().getLatitude(), 0.1);
		Assert.assertEquals(10, m.getGeoPtAttr().getLongitude(), 0.1);
		Assert.assertEquals("handle", m.getImHandleAttr1().getAddress());
		Assert.assertEquals(IMHandle.Scheme.xmpp.name(), m.getImHandleAttr1().getProtocol());
		Assert.assertEquals("network", m.getImHandleAttr2().getAddress());
		Assert.assertEquals("http://aim.com", m.getImHandleAttr2().getProtocol());
		Assert.assertEquals("link", m.getLinkAttr().getValue());
		Assert.assertEquals("000-0000-0000", m.getPhoneNumberAttr().getNumber());
		Assert.assertEquals("address", m.getPostalAddressAttr().getAddress());
		Assert.assertEquals(70, m.getRatingAttr().getRating());
		Assert.assertArrayEquals("hello".getBytes(), m.getShortBlobAttr().getBytes());
		Assert.assertEquals("hello", m.getTextAttr().getValue());
		Assert.assertEquals("hello", m.getEncryptedTextAttr().getValue());
		Assert.assertEquals("authDomain", m.getUser1().getAuthDomain());
		Assert.assertEquals("user@test.com", m.getUser1().getEmail());
		Assert.assertNull(m.getUser1().getUserId());
		Assert.assertNull(m.getUser1().getFederatedIdentity());
		Assert.assertEquals("authDomain", m.getUser2().getAuthDomain());
		Assert.assertEquals("user@test.com", m.getUser2().getEmail());
		Assert.assertEquals("userId", m.getUser2().getUserId());
		Assert.assertNull(m.getUser2().getFederatedIdentity());
		Assert.assertEquals("authDomain", m.getUser3().getAuthDomain());
		Assert.assertEquals("user@test.com", m.getUser3().getEmail());
		Assert.assertEquals("userId", m.getUser3().getUserId());
		Assert.assertEquals("federatedId", m.getUser3().getFederatedIdentity());
	}

	@Test
	public void nullCheck(){
		assertNull(new Blob(null).getBytes());
		try{
			new BlobKey(null);
			fail();
		} catch(IllegalArgumentException e){
		}
		try{
			new Category(null);
			fail();
		} catch(NullPointerException e){
		}
		try{
			new Email(null);
			fail();
		} catch(NullPointerException e){
		}
		// GeoPt
		try{
			new IMHandle((URL)null, null);
			fail();
		} catch(NullPointerException e){
		}
		try{
			new IMHandle((IMHandle.Scheme)null, null);
			fail();
		} catch(NullPointerException e){
		}
		try{
			new PhoneNumber(null);
			fail();
		} catch(NullPointerException e){
		}
		try{
			new PostalAddress(null);
			fail();
		} catch(NullPointerException e){
		}
		try{
			new ShortBlob(null);
			fail();
		} catch(NullPointerException e){
		}
		Assert.assertNull(new Text(null).getValue());
		try{
			new User(null, null);
			fail();
		} catch(NullPointerException e){
		}
		try{
			new User(null, null, null);
			fail();
		} catch(NullPointerException e){
		}
		try{
			new User(null, null, null, null);
			fail();
		} catch(NullPointerException e){
		}
	}

	@Test
	public void imHandle() throws Exception{
		IMHandle h1 = new IMHandle(IMHandle.Scheme.xmpp, "handle");
		IMHandle h2 = new IMHandle(new URL("http://aim.com"), "network");
		IMHandle[] hs = {h1, h2};
		for(IMHandle h : hs){
			Assert.assertNotNull(h.getProtocol());
			Assert.assertNotNull(h.getAddress());
			System.out.println("{\"protocol\":\"" + h.getProtocol() + "\",\"address\":\"" + h.getAddress() + "\"}");
		}
	}

	@Test
	public void keyTest(){
		Key key = KeyFactory.stringToKey("aglzbGltMy1nZW5yCwsSBHRlc3QY6AcM");
		Assert.assertEquals("test", key.getKind());
		Assert.assertEquals(1000, key.getId());
	}
}
