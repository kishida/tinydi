package kis.di.sample.mvc;

import java.time.LocalDateTime;

import javax.inject.Inject;
import javax.inject.Named;

import kis.di.annotation.Path;

/**
 *
 * @author naoki
 */
@Named
@Path("login")
public class LoginController {
    
    @Inject
    LoginSession loginSession;
    
    @Path("index")
    public String index() {
        String title = "<h1>Login</h1>";
        if (loginSession.isLogined()) {
            return title + "Login at " + loginSession.getLoginTime();
        } else {
            return title + "Not Login";
        }
    }
    
    @Path("login")
    public String login() {
        loginSession.setLogined(true);
        loginSession.setLoginTime(LocalDateTime.now());
        return "<h1>Login</h1>login";
    }
    @Path("logout")
    public String logout() {
        loginSession.setLogined(false);
        return "<h1>Login</h1>logout";
    }
}
