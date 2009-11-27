package slim3.demo.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface GreetingServiceAsync {

    void greetServer(String name, AsyncCallback<String> callback);

}
