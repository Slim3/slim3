package slim3.demo.controller.blog;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.slim3.datastore.Datastore;
import org.slim3.tester.ControllerTestCase;

import slim3.demo.model.Blog;

public class EditControllerTest extends ControllerTestCase {

    @Test
    public void run() throws Exception {
        Blog blog = new Blog();
        blog.setTitle("aaa");
        blog.setContent("111");
        Datastore.put(blog);
        tester.param("key", Datastore.keyToString(blog.getKey()));
        tester.param("version", blog.getVersion());
        tester.start("/blog/edit");
        EditController controller = tester.getController();
        assertThat(controller, is(notNullValue()));
        assertThat(tester.isRedirect(), is(false));
        assertThat(tester.getDestinationPath(), is("/blog/edit.jsp"));
        assertThat(tester.asKey("key"), is(blog.getKey()));
        assertThat(tester.asString("title"), is(blog.getTitle()));
        assertThat(tester.asString("content"), is(blog.getContent()));
        assertThat(tester.asLong("version"), is(blog.getVersion()));
    }
}