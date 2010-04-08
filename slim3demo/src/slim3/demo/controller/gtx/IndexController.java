package slim3.demo.controller.gtx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

public class IndexController extends Controller {

    private static int COUNT = 10;

    @Override
    public Navigation run() throws Exception {
        List<Map<String, Long>> list = sessionScope("gtxResultList");
        if (list == null) {
            list = new ArrayList<Map<String, Long>>(COUNT);
            for (int i = 0; i < COUNT; i++) {
                Map<String, Long> map = new HashMap<String, Long>();
                map.put("tx", 0L);
                map.put("gtx", 0L);
                list.add(map);
            }
            sessionScope("gtxResultList", list);
        }
        return forward("index.jsp");
    }
}