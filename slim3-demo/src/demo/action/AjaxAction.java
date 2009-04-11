package demo.action;

import org.apache.struts.action.ActionForward;
import org.slim3.struts.annotation.Execute;
import org.slim3.struts.util.ResponseUtil;

public class AjaxAction {

    @Execute(validate = false)
    public ActionForward index() {
        return new ActionForward("index.jsp");
    }

    @Execute(validate = false)
    public ActionForward hello() {
        ResponseUtil.write("Hello World");
        return null;
    }
}