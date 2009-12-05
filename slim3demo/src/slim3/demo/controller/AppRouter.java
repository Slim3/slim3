package slim3.demo.controller;

import org.slim3.controller.router.RouterImpl;

public class AppRouter extends RouterImpl {

    public AppRouter() {
        addRouting("/_ah/mail/{address}", "/mail/receive?address={address}");
    }

}
