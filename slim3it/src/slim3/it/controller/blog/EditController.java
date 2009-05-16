package slim3.it.controller.blog;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;
import org.slim3.jdo.JDOTemplate;
import org.slim3.util.BeanUtil;

import slim3.it.model.Blog;

import com.google.appengine.api.datastore.Key;

public class EditController extends Controller {

    private String id;

    private String title;

    private String content;

    private Blog blog;

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title
     *            the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * @param content
     *            the content to set
     */
    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public Navigation execute() {
        new JDOTemplate() {
            @Override
            public void doRun() {
                Key key = key(Blog.class, toLong(id));
                blog = pm.getObjectById(Blog.class, key);
            }
        }.run();
        setSessionAttribute("blog", blog);
        BeanUtil.copy(blog, this);
        return forward("/blog/edit.jsp");
    }
}
