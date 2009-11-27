package slim3.demo.client.service;

import com.google.appengine.api.datastore.Key;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface GreetingServiceAsync {

    void greetServer(AsyncCallback<Key> callback);

}
