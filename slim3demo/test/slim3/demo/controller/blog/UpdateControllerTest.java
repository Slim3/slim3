package slim3.demo.controller.blog;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.slim3.controller.validator.Errors;
import org.slim3.datastore.Datastore;
import org.slim3.tester.ControllerTestCase;

import slim3.demo.model.Blog;

public class UpdateControllerTest extends ControllerTestCase {

    @Test
    public void run() throws Exception {
        Blog blog = new Blog();
        blog.setTitle("aaa");
        blog.setContent("111");
        Datastore.put(blog);
        tester.param("key", Datastore.keyToString(blog.getKey()));
        tester.param("title", "aaa2");
        tester.param("content", "222");
        tester.param("version", blog.getVersion());
        tester.start("/blog/update");
        UpdateController controller = tester.getController();
        assertThat(controller, is(notNullValue()));
        assertThat(tester.isRedirect(), is(true));
        assertThat(tester.getDestinationPath(), is("/blog/"));
        blog = Datastore.get(Blog.class, blog.getKey());
        assertThat(blog, is(notNullValue()));
        assertThat(blog.getTitle(), is("aaa2"));
        assertThat(blog.getContent(), is("222"));
    }

    @Test
    public void validate() throws Exception {
        tester.start("/blog/update");
        UpdateController controller = tester.getController();
        assertThat(controller, is(notNullValue()));
        assertThat(tester.isRedirect(), is(false));
        assertThat(tester.getDestinationPath(), is("/blog/edit.jsp"));
        Errors errors = tester.getErrors();
        assertThat(errors.get("title"), is(notNullValue()));
        assertThat(errors.get("content"), is(notNullValue()));
    }
}
