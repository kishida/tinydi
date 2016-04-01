package kis.di.sample;

import javax.inject.Named;

/**
 * @author naoki
 */
@Named
public class Foo {
    
    String getMessage() {
        return "Hello!";
    }
    
}
