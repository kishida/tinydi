package kis.di.sample;

import kis.di.Context;

/**
 * @author naoki
 */
public class Main {
    
    public static void main(String[] args) {
        Context.register("foo", Foo.class);
        Context.register("bar", Bar.class);
        
        Bar bar = (Bar) Context.getBean("bar");
        bar.showMessage();
    }
}
