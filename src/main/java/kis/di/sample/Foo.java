package kis.di.sample;

import javax.inject.Named;

import kis.di.annotation.InvokeLog;

/**
 * @author naoki
 */
@Named
public class Foo {

    String getMessage() {
        return "Hello!";
    }

    @InvokeLog
    String getName() {
        return "foo!";
    }
}
