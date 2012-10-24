package org.slim3.json.test;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Assert;
import org.junit.Test;
import org.slim3.repackaged.org.json.JSONArray;
import org.slim3.repackaged.org.json.JSONException;
import org.slim3.repackaged.org.json.JSONObject;

import com.google.appengine.api.datastore.IMHandle;
import com.google.appengine.api.users.User;
import com.google.appengine.repackaged.com.google.common.util.Base64;
import com.google.appengine.repackaged.com.google.common.util.Base64DecoderException;

public class JSONObjectTest {
    @Test
    public void has() throws Exception {
        JSONObject j = new JSONObject("{}");
        Assert.assertFalse(j.has("attr"));
    }

    @Test
    public void nullTest() throws Exception {
        JSONObject j = new JSONObject("{}");
        try {
            Assert.assertNull(j.get("attr"));
            Assert.fail();
        } catch (JSONException e) {
        }
    }

    @Test
    public void optInt() throws Exception {
        JSONObject j = new JSONObject("{str: \"hello\", int: 3}");
        Assert.assertEquals(0, j.optInt("v"));
        Assert.assertEquals(0, j.optInt("str"));
        Assert.assertEquals("hello", j.optString("str"));
        Assert.assertEquals("3", j.optString("int"));
    }

    @Test
    public void wrapperIntValueTest() throws Exception {
        JSONObject j = new JSONObject("{value: 1}");
        Assert.assertEquals((Integer) 1, j.get("value"));
        try {
            j.getBoolean("value");
            Assert.fail();
        } catch (JSONException e) {
        }
        Assert.assertEquals(1, j.getDouble("value"), 0.1);
        Assert.assertEquals(1, j.getInt("value"));
        Assert.assertEquals(1, j.getLong("value"));
        Assert.assertEquals("1", j.getString("value"));
    }

    @Test
    public void wrapperFloatValueTest() throws Exception {
        JSONObject j = new JSONObject("{value: 1.1}");
        Assert.assertEquals((Double) 1.1, j.get("value"));
        try {
            j.getBoolean("value");
            Assert.fail();
        } catch (JSONException e) {
        }
        Assert.assertEquals(1.1, j.getDouble("value"), 0.1);
        Assert.assertEquals(1, j.getInt("value"));
        Assert.assertEquals(1, j.getLong("value"));
        Assert.assertEquals("1.1", j.getString("value"));
    }

    @Test
    public void wrapperListArrayTest() throws Exception {
        JSONObject j = new JSONObject("{\"values\":[1,2,3]}");
        JSONArray array = j.getJSONArray("values");
        Assert.assertEquals(3, array.length());
        Assert.assertEquals("1", array.getString(0));
        Assert.assertEquals("2", array.getString(1));
        Assert.assertEquals("3", array.getString(2));
    }

    @Test
    public void blobTest() throws Exception {
        JSONObject j = new JSONObject("{\"blobAttr\":\"aGVsbG8=\"}");
        Assert.assertArrayEquals(
            "hello".getBytes(),
            Base64.decode(j.getString("blobAttr")));
        try {
            Base64.decode("hello");
            Assert.fail();
        } catch (Base64DecoderException e) {

        }
    }

    @Test
    public void imHandle() throws Exception {
        JSONObject j =
            new JSONObject(
                "{"
                    + "\"imHandleAttr1\":{\"address\":\"handle\",\"protocol\":\"xmpp\"}"
                    + ",\"imHandleAttr2\":{\"address\":\"network\",\"protocol\":\"http://aim.com\"}"
                    + "}");
        JSONObject h = j.getJSONObject("imHandleAttr1");
        IMHandle i = null;
        IMHandle.Scheme s = null;
        URL u = null;
        try {
            s = IMHandle.Scheme.valueOf(h.getString("protocol"));
        } catch (IllegalArgumentException e) {
            try {
                u = new URL(h.getString("protocol"));
            } catch (MalformedURLException ex) {
            }
        }
        if (s != null) {
            i = new IMHandle(s, h.getString("address"));
        } else if (u != null) {
            i = new IMHandle(u, h.getString("address"));
        }
        Assert.assertEquals("handle", i.getAddress());
        Assert.assertEquals(IMHandle.Scheme.xmpp.name(), i.getProtocol());
    }

    @Test
    public void user() throws Exception {
        JSONObject j =
            new JSONObject(
                "{"
                    + "\"user1\":{\"authDomain\":\"authDomain\",\"email\":\"user@test.com\"}"
                    + "}");
        Assert.assertEquals("", j.optString("name"));
        Assert.assertNull(j.optString("name", null));
        JSONObject u = j.getJSONObject("user1");
        String domain = u.optString("authDomain", null);
        String email = u.optString("email", null);
        String uid = u.optString("userid", null);
        String fid = u.optString("federatedIdentity");
        if (domain != null && email != null) {
            if (uid == null) {
                new User(email, domain);
            } else if (fid == null) {
                new User(email, domain, uid);
            } else {
                new User(email, domain, uid, fid);
            }
        }
    }
    /*
     * "\"blobAttr\":\"aGVsbG8=\"" +
     * ",\"blobKeyAttr\":\"Q3PqkweYlb4iWpp0BVw\",\"categoryAttr\":\"partOfSpeech\""
     * + ",\"emailAttr\":\"takawitter@test.com\"" +
     * ",\"encryptedTextAttr\":\"eeRXmeJQOo8HbwTHJ+R+WQ==\"" +
     * ",\"geoPtAttr\":{\"latitude\":10.0,\"longitude\":10.0}" +
     * ",\"imHandleAttr1\":{\"address\":\"handle\",\"protocol\":\"xmpp\"}" +
     * ",\"imHandleAttr2\":{\"address\":\"network\",\"protocol\":\"http://aim.com\"}"
     * + ",\"key\":\"aglzbGltMy1nZW5yCwsSBHRlc3QY6AcM\"" +
     * ",\"linkAttr\":\"link\",\"phoneNumberAttr\":\"000-0000-0000\"" +
     * ",\"postalAddressAttr\":\"address\",\"ratingAttr\":70" +
     * ",\"shortBlobAttr\":\"aGVsbG8=\"" + ",\"textAttr\":\"hello\"" +
     * ",\"user1\":{\"authDomain\":\"authDomain\",\"email\":\"user@test.com\"}"
     * +
     * ",\"user2\":{\"authDomain\":\"authDomain\",\"email\":\"user@test.com\",\"userId\":\"userId\"}"
     * +
     * ",\"user3\":{\"authDomain\":\"authDomain\",\"email\":\"user@test.com\",\"federatedIdentity\":\"federatedId\",\"userId\":\"userId\"}"
     * +
     */
}
