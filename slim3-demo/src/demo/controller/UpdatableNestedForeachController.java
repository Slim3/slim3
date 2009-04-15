package demo.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts.action.ActionForward;
import org.slim3.struts.annotation.Controller;
import org.slim3.struts.annotation.Execute;

@Controller
public class UpdatableNestedForeachController {

    public List<List<Map<String, Object>>> mapItemsItems = new ArrayList<List<Map<String, Object>>>();

    @Execute(validate = false)
    public ActionForward index() {
        for (int i = 0; i < 10; i++) {
            List<Map<String, Object>> mapItems = new ArrayList<Map<String, Object>>();
            for (int j = 0; j < 2; j++) {
                Map<String, Object> m = new HashMap<String, Object>();
                m.put("id", "" + i + j);
                m.put("name", "name" + i + j);
                mapItems.add(m);
            }
            mapItemsItems.add(mapItems);
        }
        return new ActionForward("index.jsp");
    }

    @Execute(validate = false)
    public ActionForward submit() {
        return new ActionForward("index.jsp");
    }
}