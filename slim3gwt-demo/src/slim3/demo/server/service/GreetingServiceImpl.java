package slim3.demo.server.service;

import org.slim3.util.ServletContextLocator;

import slim3.demo.client.service.GreetingService;

/**
 * The server side implementation of the RPC service.
 */
public class GreetingServiceImpl implements GreetingService {

    public String greetServer(String input) {
        String serverInfo = ServletContextLocator.get().getServerInfo();
        String userAgent = "hoge234";
        // RequestLocator().getHeader("User-Agent");
        return "Hello, " + input + "!<br><br>I am running " + serverInfo
                + ".<br><br>It looks like you are using:<br>" + userAgent;
    }
}
