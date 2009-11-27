package slim3.demo.server.service;

import slim3.demo.client.service.GreetingService;

/**
 * The server side implementation of the RPC service.
 */
public class GreetingServiceImpl implements GreetingService {

    public String greetServer(String name) {
        return "Hello, " + name;
    }
}
