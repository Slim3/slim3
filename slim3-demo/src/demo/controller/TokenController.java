package demo.controller;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.slim3.struts.annotation.Execute;
import org.slim3.struts.util.ActionMessagesUtil;
import org.slim3.struts.util.TokenUtil;

public class TokenController {

    @Execute(validate = false)
    public ActionForward index() {
        TokenUtil.saveToken();
        return new ActionForward("index.jsp");
    }

    @Execute(validate = false)
    public ActionForward result() {
        if (!TokenUtil.isTokenValid(true)) {
            ActionMessages errors = new ActionMessages();
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
                    "errors.invalid", "Transaction token"));
            ActionMessagesUtil.addErrorsIntoRequest(errors);
            return new ActionForward("index.jsp");
        }
        return new ActionForward("result.jsp");
    }
}