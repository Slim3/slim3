package slim3.demo.server.service;

import org.slim3.datastore.Datastore;

import slim3.demo.client.service.GreetingService;
import slim3.demo.shared.model.Bbb;

import com.google.appengine.api.datastore.Key;

/**
 * The server side implementation of the RPC service.
 */
public class GreetingServiceImpl implements GreetingService {

    public Key greetServer() {
        return Datastore.allocateId(Bbb.class);
    }
}
