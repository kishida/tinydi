package kis.di.sample;

import javax.inject.Inject;

/**
 * @author naoki
 */
public class Bar {
    
    @Inject
    Foo foo;
    
    void showMessage() {
        System.out.println(foo.getMessage());
    }
    
}
