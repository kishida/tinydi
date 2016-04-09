package kis.di.mvc;

import java.net.InetAddress;

import javax.inject.Named;

import kis.di.annotation.RequestScoped;
import lombok.Data;

/**
 *
 * @author naoki
 */
@Named
@RequestScoped
@Data
public class RequestInfo {
    private String path;
    private InetAddress localAddress;
    private String userAgent;
}
