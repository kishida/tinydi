package kis.di.sample;

import java.time.LocalDateTime;

import javax.inject.Inject;
import javax.inject.Named;

import kis.di.annotation.InvokeLog;

/**
 * @author naoki
 */
@Named
public class Bar {
    
    @Inject
    Foo foo;
    
    @Inject
    Now now;
    
    @InvokeLog
    void showMessage() {
        System.out.println(foo.getName() + " " + foo.getMessage());
    }
 
    void longProcess() {
        now.setTime(LocalDateTime.now());
        System.out.println("start:" + now.getTime());
        try {
            Thread.sleep(5000);
        } catch (InterruptedException ex) {
        }
        System.out.println("end  :" + now.getTime());
        
    }
}
