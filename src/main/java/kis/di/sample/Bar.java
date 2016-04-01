package kis.di.sample;

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
    
    @InvokeLog
    void showMessage() {
        System.out.println(foo.getName() + " " + foo.getMessage());
    }
    
}
