package demo.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts.action.ActionForward;
import org.slim3.struts.annotation.Execute;
import org.slim3.struts.annotation.SessionScope;

public class MultiboxController {

    @SessionScope
    public String[] multibox;

    public List<Map<String, String>> multiboxItems;

    @Execute(validate = false)
    public ActionForward index() {
        multiboxItems = new ArrayList<Map<String, String>>();
        for (int i = 1; i <= 3; i++) {
            Map<String, String> m = new HashMap<String, String>();
            m.put("value", String.valueOf(i));
            m.put("label", "label" + i);
            multiboxItems.add(m);
        }
        return new ActionForward("index.jsp");
    }

    @Execute(validate = false, reset = "reset")
    public ActionForward submit() {
        return new ActionForward("submit.jsp");
    }

    @Execute(validate = false)
    public ActionForward moveToIndexPage() {
        return new ActionForward("", true);
    }

    public void reset() {
        multibox = new String[0];
    }
}