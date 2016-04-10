package kis.di.sample.mvc;

import javax.inject.Inject;
import javax.inject.Named;

import kis.di.annotation.Path;
import kis.di.mvc.RequestInfo;

/**
 *
 * @author naoki
 */
@Named
@Path("info")
public class RequestInfoController {
    @Inject
    RequestInfo requestInfo;
    
    @Path("index")
    public String index() {
        return String.format("<h1>Info</h1>"
                + "Host:%s<br/>"
                + "Path:%s<br/>"
                + "UserAgent:%s<br/>"
                + "SessionId:%s<br/>",
                requestInfo.getLocalAddress(), 
                requestInfo.getPath(),
                requestInfo.getUserAgent(),
                requestInfo.getSessionId());
                
    }
}
