package kis.di.sample;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author naoki
 */
@Named
public class Bar {
    
    @Inject
    Foo foo;
    
    void showMessage() {
        System.out.println(foo.getMessage());
    }
    
}
