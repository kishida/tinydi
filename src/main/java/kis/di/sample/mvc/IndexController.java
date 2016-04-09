package kis.di.sample.mvc;

import java.time.LocalDateTime;

import javax.inject.Named;

import kis.di.annotation.Path;

/**
 *
 * @author naoki
 */
@Named
@Path("")
public class IndexController {
    @Path("")
    public String index() {
        return "<h1>Hello</h1>" + LocalDateTime.now();
    }
    
    @Path("message")
    public String mes() {
        return "<h1>Message</h1>Nice to meet you!";
    }
}
