package slim3.demo.controller.performance;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

import slim3.demo.cool.model.Bar;

public class ReflectionController extends Controller {

    private static final int COUNT = 10000;

    @Override
    public Navigation run() throws Exception {
        Constructor<?> ctr = Bar.class.getConstructor();
        Field field = Bar.class.getDeclaredField("sortValue");
        field.setAccessible(true);
        long start = System.currentTimeMillis();
        for (int i = 0; i < COUNT; i++) {
            Object bar = ctr.newInstance();
            field.set(bar, "hoge");
        }
        requestScope("result", System.currentTimeMillis() - start);
        return forward("reflection.jsp");
    }
}
