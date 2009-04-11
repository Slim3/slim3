package demo.action;

import org.apache.struts.action.ActionForward;
import org.slim3.struts.annotation.Execute;

public class ConditionAction {

    public String id;

    @Execute(validate = false)
    public ActionForward index() {
        return new ActionForward("index.jsp");
    }
}