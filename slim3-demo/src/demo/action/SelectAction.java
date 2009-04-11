package demo.action;

import org.apache.struts.action.ActionForward;
import org.slim3.struts.annotation.Execute;

public class SelectAction {

    public String select;

    @Execute(validate = false)
    public ActionForward index() {
        select = "3";
        return new ActionForward("index.jsp");
    }

    @Execute(validate = false)
    public ActionForward submit() {
        return new ActionForward("index.jsp");
    }
}