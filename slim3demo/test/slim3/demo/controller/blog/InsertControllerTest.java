package slim3.demo.controller.blog;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.slim3.controller.validator.Errors;
import org.slim3.tester.ControllerTestCase;

import slim3.demo.model.Blog;

public class InsertControllerTest extends ControllerTestCase {

    @Test
    public void run() throws Exception {
        tester.param("title", "aaa");
        tester.param("content", "bbb");
        tester.start("/blog/insert");
        InsertController controller = tester.getController();
        assertThat(controller, is(notNullValue()));
        assertThat(tester.isRedirect(), is(true));
        assertThat(tester.getDestinationPath(), is("/blog/"));
        assertThat(tester.count(Blog.class), is(1));
    }

    @Test
    public void validate() throws Exception {
        tester.start("/blog/insert");
        InsertController controller = tester.getController();
        assertThat(controller, is(notNullValue()));
        assertThat(tester.isRedirect(), is(false));
        assertThat(tester.getDestinationPath(), is("/blog/create"));
        Errors errors = tester.getErrors();
        assertThat(errors.get("title"), is(notNullValue()));
        assertThat(errors.get("content"), is(notNullValue()));
        assertThat(tester.count(Blog.class), is(0));
    }
}
