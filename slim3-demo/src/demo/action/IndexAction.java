package demo.action;

import org.apache.struts.action.ActionForward;
import org.slim3.struts.annotation.Execute;

public class IndexAction {

    @Execute(validate = false)
    public ActionForward index() {
        return new ActionForward("index.jsp");
    }
}