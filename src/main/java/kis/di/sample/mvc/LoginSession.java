package kis.di.sample.mvc;

import java.time.LocalDateTime;

import javax.inject.Named;

import kis.di.annotation.SessionScoped;
import lombok.Data;

/**
 *
 * @author naoki
 */
@Named
@SessionScoped
@Data
public class LoginSession {
    boolean logined;
    LocalDateTime loginTime;
}
