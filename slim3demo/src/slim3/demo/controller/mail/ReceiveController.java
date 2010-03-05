package slim3.demo.controller.mail;

import java.util.Properties;

import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

public class ReceiveController extends Controller {

    @Override
    public Navigation run() throws Exception {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        MimeMessage message =
            new MimeMessage(session, request.getInputStream());
        System.out.println("from:" + message.getFrom()[0]);
        System.out.println("to:" + asString("address"));
        if (isDevelopment()) {
            System.out.println("subject:"
                + new String(message.getSubject().getBytes("8859_1"), "UTF-8"));
        } else {
            System.out.println("subject:" + message.getSubject());
        }
        Object content = message.getContent();
        if (content instanceof Multipart) {
            Multipart multipart = (Multipart) content;
            Part part = multipart.getBodyPart(0);
            System.out.println("content:" + part.getContent());
        } else {
            System.out.println("content:" + content);
        }
        return null;
    }
}
