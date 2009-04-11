package demo.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts.action.ActionForward;
import org.slim3.struts.annotation.Execute;

public class ForeachAction {

    public String id;

    public List<Map<String, Object>> mapItems = new ArrayList<Map<String, Object>>();

    @Execute(validate = false)
    public ActionForward index() {
        for (int i = 0; i < 10; i++) {
            Map<String, Object> m = new HashMap<String, Object>();
            m.put("id", i);
            m.put("name", "name" + i);
            mapItems.add(m);
        }
        return new ActionForward("index.jsp");
    }

    @Execute(validate = false)
    public ActionForward result() {
        return new ActionForward("result.jsp");
    }
}