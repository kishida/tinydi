package kis.di;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 *
 * @author naoki
 */
public class Main {
    static class Context {
        static Map<String, Class> types = new HashMap<>();
        static Map<String, Object> beans = new HashMap<>();
        
        static void register(String name, Class type) {
            types.put(name, type);
        }
        
        static Object getBean(String name) {
            return beans.computeIfAbsent(name, key -> {
                Class type = types.get(name);
                Objects.requireNonNull(type, name + " not found.");
                try {
                    return type.newInstance();
                } catch (InstantiationException | IllegalAccessException ex) {
                    throw new RuntimeException(name + " can not instanciate", ex);
                }
            });
        }
    }
    
    static class Foo {
        String getMessage() {
            return "Hello!";
        }
    }
    
    static class Bar {
        void showMessage() {
            Foo foo = (Foo) Context.getBean("foo");
            System.out.println(foo.getMessage());
        }
    }
    
    public static void main(String[] args) {
        Context.register("foo", Foo.class);
        Context.register("bar", Bar.class);
        
        Bar bar = (Bar) Context.getBean("bar");
        bar.showMessage();
    }
}
