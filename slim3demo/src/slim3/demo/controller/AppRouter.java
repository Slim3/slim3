package slim3.demo.controller;

import org.slim3.controller.router.RouterImpl;

public class AppRouter extends RouterImpl {

    public AppRouter() {
        addRouting("/{app}/make", "/{app}?method=create");
        addRouting("/_ah/mail/{address}", "/mail/receive?address={address}");
        addRouting(
            "/{app}/edit/{key}/{version}",
            "/{app}/edit?key={key}&version={version}");
        addRouting(
            "/{app}/delete/{key}/{version}",
            "/{app}/delete?key={key}&version={version}");
    }

}
