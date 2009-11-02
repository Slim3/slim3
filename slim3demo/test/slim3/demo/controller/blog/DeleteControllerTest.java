package slim3.demo.controller.blog;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.slim3.datastore.Datastore;
import org.slim3.tester.ControllerTestCase;

import slim3.demo.model.Blog;

import com.google.appengine.api.datastore.KeyFactory;

public class DeleteControllerTest extends ControllerTestCase {

    @Test
    public void run() throws Exception {
        Blog blog = new Blog();
        Datastore.put(blog);
        tester.param("key", KeyFactory.keyToString(blog.getKey()));
        tester.param("version", blog.getVersion());
        tester.start("/blog/delete");
        DeleteController controller = tester.getController();
        assertThat(controller, is(notNullValue()));
        assertThat(tester.isRedirect(), is(true));
        assertThat(tester.getDestinationPath(), is("/blog/"));
        assertThat(tester.count(Blog.class), is(0));
    }
}
